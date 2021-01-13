#!/usr/bin/python2.7

import socket

mySocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)	#nonreuseable port until it is closed
mySock = socket.socket(socket.SOL_SOCKET, socket.SO_REUSEADDR)	#reusing port

HOST = ''
PORT = 8080
mySocket.bind((HOST,PORT))

#keep IP address argument  as emtpy sting so that it can take any value

mySocket.listen(2)

print("I am waiting for a client...")

(client, (ip, sock)) = mySocket.accept()

print("Recieved connection from ", ip)

print("Starting ECHO Server object that will echo to the client")

#lets build some false data
data = "false"

while len(data):
	data = client.recv(2048)
	print("Client sent this data: ", data)
	client.send(data)

print("Closing our connection after sending data to the client...")

client.close()

