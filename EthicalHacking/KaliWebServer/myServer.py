#program to create a Kali Web Server
import socket
import sys

#pass an empty string to make all interfaces available
HOST = ''

#chose any arbitrary port number (within a range of 0 to 65535 with  0 and 1023 reserved)
PORT = 8080

mySocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket has been created')

#try binding socket to local host and port
try:
	mySocket.bind((HOST, PORT))
except socket.error as msg:
	print('Binding has failed. Error Code: ' + str(msg[0]) + ' Message ' + msg[1])
	sys.exit()
print('Socket bind is complete. Proceeding to make it listen...')

#Server is listening now on socket
mySocket.listen(10)
print('socket is now listening')

#let server keep talking with client
while 1:
	#waiting to accept a connection - blocking call
	connection, address = mySocket.accept()
	print('Connected with ' + address[0] + ':' + str(address[1]))
mySocket.close()
