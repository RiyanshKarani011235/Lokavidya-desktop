import os
import time
# import PdfToImages
import sys

if len(sys.argv) != 3 : 
	raise SystemExit(1)

def PresentationToImages(pptPath) : 

	libreOfficePythonPath = findLibreOfficePythonPath()

	pdfPath = ''
	pdfPathList = pptPath.split(os.sep)[:-1]
	for element in pdfPathList : 
		pdfPath += element + os.sep
	print(pdfPath)

	pptName = pptPath.split(os.sep)[-1]
	pdfNameList = pptName.split('.')[:-1]
	pdfName = ''
	for element in pdfNameList : 
		pdfName += element + '.'
	pdfName += 'pdf'
	pdfFile = pdfPath + pdfName
	print(pdfFile)

	# start LibreOffice on port 2002 in a separate process
	# try : 
		# if os.fork() > 0 : 
			# main thread
			# time.sleep(2) # wait for the libreOffice port to start
			# print(libreOfficePythonPath + ' --convert-to pdf --outdir ' + pdfPath + ' ' + pptPath)
			# command = libreOfficePythonPath + ' --headless --invisible --convert-to pdf --outdir ' + pdfPath + ' ' + pptPath
			# print(command)
			# os.system(command)
			# print('converting PDF to images')
			# PdfToImages.PdfToImages(pdfPath)
			# print('done converting')
			# raise SystemExit(0)
	# except : 	
		# pass
	# else : 
		# pass

	command = libreOfficePythonPath + ' --headless --invisible --convert-to pdf --outdir ' + pdfPath + ' ' + pptPath
	print(command)
	os.system(command)
	print('converting PDF to images')
	print(pdfFile)
	# PdfToImages.PdfToImages(pdfFile)
	# print('done converting')
	image_directory = os.path.join(pdfPath, 'convertedImages')

	# convert to Image
	os.system('convert ' + pdfFile + ' ' + image_directory + convertedFile)

	# resizing the Images
	for root, dirs, files in os.walk(image_directory) : 
		print(files)
		for file in files : 
			if file.split('.')[-1] == 'png' : 
				print('converting ' + os.path.join(root, file))
				os.system('convert ' + os.path.join(root, file) + ' -resize 720x540 ' + os.path.join(root, file))

	# print('starting LibreOffice on port 2002')
	# os.system(libreOfficePythonPath + ' "--headless" "-accept=socket,port=2002;urp;"')

def findLibreOfficePythonPath() : 
	return '/Applications/LibreOffice.app/Contents/MacOS/soffice'
	# TODO : implement logic

def relocateImages() :
	relocationLocation = sys.argv[2]

presentation = ''
if len(sys.argv) == 1 : 
	presentation = 'tamil.pptx'
else : 
	presentation = sys.argv[1]

PresentationToImages(presentation)
relocateImages()