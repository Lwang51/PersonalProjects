#Find the IP address of a given website 

import socket

ip = socket.gethostbyname("google.com")
print(ip)
