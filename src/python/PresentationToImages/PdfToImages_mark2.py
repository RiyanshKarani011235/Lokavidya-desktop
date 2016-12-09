import os

def pdfToImages(pdfPath, tempImagePath) : 
	print('converting PDF to images')
	command = '/opt/ImageMagick/bin/convert ' + pdfPath + ' ' + tempImagePath
	print(command)
	os.system(command)