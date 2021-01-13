import socket
import struct

host = "192.168.0.31"	#get connection IP from ifconfig command
port = 1234		#any port within range

mySocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
mySocket.bind((host, port))
mySocket.listen(1)
connection, address = mySocket.accept()
print("Connected to ", address)

#struct pack method passes two parameters - format string and values
message = struct.pack('hhl', 1, 2, 3)
connection.send(message)
connection.close()

