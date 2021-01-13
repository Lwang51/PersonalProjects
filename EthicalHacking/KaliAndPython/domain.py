#!/usr/bin/python3

from tld import get_tld

def GetDomainName(url):
	DomainName = get_tld(url)
	return DomainName

print(GetDomainName('https://google.com'))
print(GetDomainName('https://www.redit.com'))
