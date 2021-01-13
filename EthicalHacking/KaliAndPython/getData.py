#simple TCP Client Server in Python that will listen to a certain port

#importing socket library
import socket
import sys

#creating socket object
mySocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
mySocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
print("Socket has been successfully created")

HOST = ''
PORT = 8080

try:
	mySocket.bind((HOST, PORT))

except socket.error as msg:
	print('Binding has failed. Error code: ' + str(msg[0]) + ' Messgae ' + msg[1])
	sys.exit()

print("Socket object biinding is complete to the port ", PORT)

mySocket.listen(5)

print("Socket is listening")
c, addr = mySocket.accept()
data = c.recv(512)

if data:
	file = open("store.dat", "+w")
	print("Connection from address: ", addr[0])
	file.write(addr[0])
	file.write(" : ")
	file.writedata.decode("utf-8")
	file.close()

mySocket.close()
