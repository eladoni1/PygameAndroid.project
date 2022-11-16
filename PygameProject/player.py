from settings import *
import pygame as pg
import math


class Player:
    def __init__(self, game):
        self.game = game
        self.x, self.y = PLAYER_POS
        self.angle = PLAYER_ANGLE
        self.aim_circle = list(INITIAL_CENTER)

    def movement(self):  # See /photos for more information - calculating dx and dy using trigonometry.
        sin_a = math.sin(self.angle)
        cos_a = math.cos(self.angle)
        dx, dy = 0, 0
        speed = PLAYER_SPEED * self.game.delta_time
        speed_sin = speed * sin_a
        speed_cos = speed * cos_a

        keys = pg.key.get_pressed()
        if keys[pg.K_w]:
            dx += speed_cos
            dy += speed_sin
        if keys[pg.K_s]:
            dx -= speed_cos
            dy -= speed_sin
        if keys[pg.K_a]:
            dx += speed_sin
            dy -= speed_cos
        if keys[pg.K_d]:
            dx -= speed_sin
            dy += speed_cos

        # Increasing movement by the vector's x and y values.
        self.check_wall_collision(dx, dy)

        if keys[pg.K_LEFT]:
            self.angle -= PLAYER_ROT_SPEED * self.game.delta_time
        if keys[pg.K_RIGHT]:
            self.angle += PLAYER_ROT_SPEED * self.game.delta_time

        self.angle %= math.tau

    def joystick_movement(self):  # See /photos for more information - calculating dx and dy using trigonometry.
        sin_a = math.sin(self.angle)
        cos_a = math.cos(self.angle)
        dx, dy = 0, 0
        speed = PLAYER_SPEED * self.game.delta_time
        speed_sin = speed * sin_a
        speed_cos = speed * cos_a

        joystick_data = self.game.joystick_communication.joystick_pos
        xLeftJoystick, yLeftJoystick = joystick_data[0]
        xRightJoystick, yRightJoystick = joystick_data[1]

        # keys = pg.key.get_pressed()
        # if keys[pg.K_w]:
        #     dx += speed_cos
        #     dy += speed_sin
        # if keys[pg.K_s]:
        #     dx -= speed_cos
        #     dy -= speed_sin
        # if keys[pg.K_a]:
        #     dx += speed_sin
        #     dy -= speed_cos
        # if keys[pg.K_d]:
        #     dx -= speed_sin
        #     dy += speed_cos

        dx, dy = yLeftJoystick * speed_cos + xLeftJoystick * speed_sin, \
                 yLeftJoystick * speed_sin + xLeftJoystick * -1 * speed_cos

        # ADDED - AIM
        new_aim_x = self.aim_circle[0] - xRightJoystick * AIM_SPEED * self.game.delta_time
        new_aim_y = self.aim_circle[1] - yRightJoystick * AIM_SPEED * self.game.delta_time
        # while new_aim_x >= WIDTH or new_aim_x <= 0 or new_aim_y >= HEIGHT or new_aim_y <= 0 :
        #     new_aim_x = (new_aim_x + self.aim_circle[0]) // 2
        #     new_aim_y = (new_aim_y + self.aim_circle[1]) // 2
        #     if new_aim_x >= WIDTH:
        #         new_aim_x -= 0.000001
        #     if new_aim_x <= 0:
        #         new_aim_x += 0.000001
        #     if new_aim_y >= HEIGHT:
        #         new_aim_y -= 0.000001
        #     if new_aim_y <= 0:
        #         new_aim_y += 0.000001
        if new_aim_x >= WIDTH:
            new_aim_x = MAX_AIM_WIDTH
            self.angle -= xRightJoystick * PLAYER_ROT_SPEED * self.game.delta_time
        if new_aim_x <= 0:
            new_aim_x = MIN_AIM_WIDTH
            self.angle -= xRightJoystick * PLAYER_ROT_SPEED * self.game.delta_time
        if new_aim_y >= HEIGHT:
            new_aim_x = MAX_AIM_HEIGHT
        if new_aim_y <= 0:
            new_aim_y = MIN_AIM_HEIGHT


        self.aim_circle[0] = new_aim_x
        self.aim_circle[1] = new_aim_y


        # ADDED - AIM
        # REMOVED - AIM
        # _rotation = xRightJoystick * PLAYER_ROT_SPEED * self.game.delta_time
        # self.angle -= _rotation
        # REMOVED - AIM

        # if FPS != 0:
        #     dx /= FPS
        #     dy /= FPS
        # else:
        #     dx /= ((self.game.clock.get_fps() + 1) / 2)
        #     dy /= ((self.game.clock.get_fps() + 1) / 2)

        # Increasing movement by the vector's x and y values.
        self.check_wall_collision(dx, dy)

        # if keys[pg.K_LEFT]:
        #     self.angle -= PLAYER_ROT_SPEED * self.game.delta_time
        # if keys[pg.K_RIGHT]:
        #     self.angle += PLAYER_ROT_SPEED * self.game.delta_time

        # if FPS != 0:
        #     self.angle -= PLAYER_ROT_SPEED * self.game.delta_time * (_rotation / (FPS / 60))
        # else:
        #     self.angle -= PLAYER_ROT_SPEED * self.game.delta_time * (_rotation / ((self.game.clock.get_fps() + 1) / 60))

        self.angle %= math.tau

    def check_wall(self, x, y):
        return (x, y) not in self.game.map.world_map

    def check_wall_collision(self, dx, dy):
        # We are checking each time for dx and dy (and not together) so maybe one of them can be applied, but not the other.
        if self.check_wall(int(self.x + dx), int(self.y)):
            self.x += dx
        if self.check_wall(int(self.x), int(self.y + dy)):
            self.y += dy

    def draw(self):
        # Drawing the line of direction. first point representing where the player is, second represent direction.
        # pg.draw.line(self.game.screen, 'yellow', (self.x * 100, self.y * 100),
        #              (self.x * 100 + WIDTH * math.cos(self.angle), self.y * 100 + WIDTH * math.sin(self.angle)), 2)
        pg.draw.circle(self.game.screen, 'green', (self.x * 100, self.y * 100), 15)

    def update(self):
        self.joystick_movement()
        # self.movement()
        # self.movement()

    @property
    def pos(self):
        return self.x, self.y

    @property
    def map_pos(self):
        return int(self.x), int(self.y)

    @property
    def aim(self):
        return self.aim_circle
