import math

# Game (main.py) settings
RES = WIDTH, HEIGHT = 1600, 900
HALF_WIDTH = WIDTH // 2
HALF_HEIGHT = HEIGHT // 2
FPS = 60


# player.py settings
PLAYER_POS = 1.5, 5  # position in mini_map
PLAYER_ANGLE = 0  # Angle of player's direction.
PLAYER_SPEED = 0.004  # Speed towards the player's direction
PLAYER_ROT_SPEED = 0.002  # Speed of the player's rotation

# aim
INITIAL_CENTER = HALF_WIDTH, HALF_HEIGHT
AIM_RADIUS = min(HALF_HEIGHT, HALF_WIDTH) // 8
AIM_SPEED = 2.0
MAX_AIM_HEIGHT = HEIGHT - 0.001
MAX_AIM_WIDTH = WIDTH - 0.001
MIN_AIM_HEIGHT = 0.001
MIN_AIM_WIDTH = 0.001


# raycasting.py settings
FOV = math.pi / 3  # Field of view
HALF_FOV = FOV / 2
NUM_RAYS = WIDTH // 2
HALF_NUM_RAYS = NUM_RAYS // 2
DELTA_ANGLE = FOV / NUM_RAYS
MAX_DEPTH = 20

SCREEN_DIST = HALF_WIDTH / math.tan(HALF_FOV)

SCALE = WIDTH // NUM_RAYS


HOST, PORT = "localhost", 2020
