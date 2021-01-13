#how to connect to Google using the socket programming in Python

#import socket from library
import socket
import sys

try:
	mySocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	print('Socket sucessfully created')
except socket.error as err:
	print("Socket creation failed with error %s" % (err))

#default port for the socket
port = 80

try:
	host_ip = socket.gethostbyname('www.google.com')
except socket.gaierror:
	print("there was an error resolving the host")

	sys.exit()

#connecting to server
mySocket.connect((host_ip, port))

print("Socket has successfully connected to Google on port == %s" % (host_ip))
