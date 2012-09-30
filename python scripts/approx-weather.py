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
	approx = 0.0
	argv = map(float, argv)		
	approx = sum(argv)/len(argv)
	print round(approx, 3);

if __name__ == '__main__':
	main(sys.argv[1:])

