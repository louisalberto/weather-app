import sys
import timeit
import random
import math
#import numpy as np
import fileinput
import urllib2, urllib
import os

def main(argv):
	#print 'hello'
	approx = 2*[0.0]
	argv = map(float, argv)		
	approx[0] = (argv[0]+argv[1]+ argv[2])/3
	approx[1] = (argv[3]+argv[4]+ argv[5])/3
	
	#round everything to 3 decimal places
	for i in range(0, len(approx)):
		approx[i] = round(approx[i], 3)
	print approx[0],approx[1]

if __name__ == '__main__':
	main(sys.argv[1:])

