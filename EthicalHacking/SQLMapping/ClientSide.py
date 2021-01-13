#!/usr/bin/python2.7

import socket
import struct

host = "192.168.0.31"	#get connection IP from ifconfig command, must be same with ServerSide.py
port = 1234		#any port within range, but must be same with ServerSide.py port to connect

#creating INET and raw socket
mySocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
mySocket.connect((host, port))

#recieving buffersize
message = mySocket.recv(1024)

print(message)
print(struct.unpack('hhl', message))
mySocket.close()

