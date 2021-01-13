#!/usr/bin/python3

import os

def GetIPAddress(url):
	LinuxCommand = "host " + url
	StartProcess = os.popen(LinuxCommand)
	results = str(StartProcess.read())

	marking = results.find('has address') + 12

	return results[marking:].splitlines()[0]

print(GetIPAddress('google.com'))
