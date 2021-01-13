#import socket library
import socket
#import sys

#create socket object
mySocket = socket.socket()
print("socket created successfully")

#reserve a port on computer (can be anything, but within range)
port = 43210

#binding to port, but we do not have an ip, so leave it with an empty string in the ip field
#server will listen to any request coming from other computers on the network
mySocket.bind(('', port))
print("Socket is bounded to port %s" % (port))

#place socket into listening mode
mySocket.listen(5)
print("Socket is now listening")

#make an forever loop until error occurs of if program is interrupted
while True:
	#establish connection with client
	c, addr = mySocket.accept()
	print("Got a connection from ", addr)
	c.send("Thank you for connecting!\n")
	#close connection with client
	c.close()
