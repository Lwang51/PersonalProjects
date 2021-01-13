#!/usr/bin/python3

import nmap

"""
nm = nmap.PortScannerAsync()

def callback_result(host, scan_result):
	print ('--------------------')
	print (host, scan_result)

#search up what is your ip address is
nm.scan('198.27.69.198', arguments = "-O -v", callback = callback_result)

while nm.still_scanning():
	print("Waiting >>>")
	nm.wait(2)

nm1 = nmap.PortScanner()

a = nm1.nmap_version()
print(a)
"""

#updated

nm = nmap.PortScanner()

print(nm.nmap_version())

nm.scan('192.168.0.0-255', '1-1024', '-v --version-all')

#print(nm.scaninfo())		#either these two or the next section
#print(nm.csv())

print(nm.scanstats())
print(nm['192.168.146.1'].state())
print(nm['192.168.146.1'].all_protocols())
print(nm['192.168.146.1']['tcp'].keys())
