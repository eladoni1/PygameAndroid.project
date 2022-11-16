import socket
import threading
from queue import Queue
from threading import Thread
from settings import *
import struct


class JoystickCommunication:
    def __init__(self):
        self.__prev_left = (0, 0)
        self.__prev_right = (0, 0)
        self.__q = Queue()
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.bind((HOST, PORT))
        self.__UDP_thread = Thread(target=self.producer, args=(self.__q,))
        self.__UDP_thread.daemon = True
        self.__UDP_thread.start()


    # A thread that produces (UDP) data

    def producer(self, q):
        try:
            while True:
                data, addr = self.sock.recvfrom(1024)
                first = data[:4]
                second = data[4:8]
                which = data[8:]
                joystick_pos = struct.unpack('>f', first)[0], struct.unpack('>f', second)[0]
                is_right = False
                if len(which) and which[0]:
                    is_right = True
                else:
                    is_right = False
                q.put((joystick_pos, is_right))
        except:
            print("Error: Exception at Communication Thread.")

    def close(self):
        self.sock.shutdown(socket.SHUT_RDWR)

    @property
    def joystick_pos(self):
        if not self.__q.empty():
            data = self.__q.get()
            if data[1]:
                self.__prev_right = data[0]
            else:
                self.__prev_left = data[0]
            return self.__prev_left, self.__prev_right
        else:
            return self.__prev_left, self.__prev_right




