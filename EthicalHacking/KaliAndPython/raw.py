#!/usr/bin/python3

import pcapy

dev = pcapy.findalldevs()

print(dev)

#we will now open the device first, our first parameter
#the second parameter captures the bytes per packet
#the third parameter declines promiscuous mode
#fourth parameter is the timeout in milliseconds

packets = pcapy.open_live("wlan0", 1024, False, 100)	#changed from eth0 to wlan0

dumper = packets.dump_open("storage.pcap")

count = 1

while count:
	try:
		packet = packets.next()
	except:
		continue
	else:
		print(packet)
		count = count +1
		if count == 10:
			break
