#Python server side code

##let the ambiguity remain##

#simple TCP Client Server in Python that will listen to a certain port importing the socket and system library

import socket
import sys

#creating a socket object
mySocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print("Socket has been successfully created")

#empty string so that all host interfaces are available
HOST = ''

#reserving a port that we can open, then bind the socket object to that particular port
PORT = 8080

try:
	mySocket.bind((HOST, PORT))
except socket.error as msg:
	print('Binding has failed. Error Code: ' + str(msg[0]) + ' Message ' + msg[1])

print("Socket object is bound to the port ", PORT)

#putting the socket into listening mode
mySocket.listen(10)

print("Socket is listening")

while True:
	c, addr = mySocket.accept()
	print("Got a connection from ", addr)

