# import os
# import sys
# import subprocess
# from GetWorkingDirectory import getWorkingDirectory
# from PdfToImages_mark2 import pdfToImages

# usageString = 'Incorrect format : Usage : [presentation] [output_location]'
# if len(sys.argv) != 3 : 
# 	print(usageString)
# 	raise SystemExit(1)

# presentationPath = sys.argv[1]
# outputLocation = sys.argv[2]
# outputImageFormat = 'jpeg'

# workingDirectory = getWorkingDirectory()
# fileName = presentationPath.split(os.sep)[-1].split('.')[0]
# pdfPath = workingDirectory + fileName + '.pdf'
# tempImagePath = workingDirectory + 'convertedImages' + os.sep + fileName + '.' + outputImageFormat

# def getLibreOfficePythonPath() : 
# 	return '/Applications/LibreOffice.app/Contents/MacOS/soffice'

# # convert presentation to PDF
# print('converting presentation to PDF')
# command = getLibreOfficePythonPath() + ' --headless --invisible --convert-to pdf --outdir ' + workingDirectory + ' ' + presentationPath
# os.system(command)
# print('saving PDF to : ' + pdfPath)

# # clean "convertedImages" directory
# for root, dirs, files in os.walk(workingDirectory + 'convertedImages') : 
# 	for file in files : 
# 		os.system('rm ' + os.path.join(root, file))

# # convert PDF to corresponding page PDFS
# command = workingDirectory + 'cpdf-binaries-master/OSX-Intel/cpdf -split ' + pdfPath + ' -o ' + workingDirectory + 'convertedImages' + os.sep + fileName + '-%%%%' + '.pdf'
# print(command)
# os.system(command)

# # convert individual page PDF's to JPEG's
# for root, dirs, files in os.walk(workingDirectory + 'convertedImages') : 
# 	for file in files : 
# 		if file.split('.')[-1] == 'pdf' : 
# 			command = getLibreOfficePythonPath() + ' --headless --invisible --convert-to ' + outputImageFormat + ' --outdir ' + workingDirectory + 'convertedImages' + ' ' + os.path.join(root, file)
# 			os.system(command)

# # add ghostScript "gs" to path
# print(os.environ['PATH'])
# os.environ['PATH'] = os.environ['PATH'] + ':/usr/local/bin'
# print(os.environ['PATH'])

# # convert PDF to images
# print('converting PDF to images')
# command = '/opt/ImageMagick/bin/convert ' + pdfPath + ' ' + tempImagePath
# print(command)
# os.system(command)
# pdfToImages(pdfPath, tempImagePath)

import os
import sys
import subprocess
from GetWorkingDirectory import getWorkingDirectory
from PdfToImages_mark2 import pdfToImages

usageString = 'Incorrect format : Usage : [presentation] [output_location]'
if len(sys.argv) != 3 : 
	print(usageString)
	raise SystemExit(1)

operatingSystem = ''
if 'win' in sys.platform and 'darwin' not in sys.platform : 
	operatingSystem = 'win'
elif 'darwin' in sys.platform : 
	operatingSystem = 'macosx' 
elif 'linux' in sys.platform : 
	operatingSystem = 'linux'

print('operatingSystem : ' + operatingSystem)
print('hello world ------------------------ testing --------------------- hello world')
presentationPath = sys.argv[1]
outputLocation = sys.argv[2]
outputImageFormat = 'jpeg'

workingDirectory = getWorkingDirectory()
fileName = presentationPath.split(os.sep)[-1].split('.')[0]
pdfPath = workingDirectory + fileName + '.pdf'
tempImagePath = workingDirectory + 'convertedImages' + os.sep + fileName + '.' + outputImageFormat
# tempImagePath = workingDirectory + 'convertedImages' + os.sep + 'img_' + '.' + outputImageFormat

def getLibreOfficePythonPath() : 
	if operatingSystem == 'win' : 
		return '"C:\Program Files (x86)\LibreOffice 5\program\soffice.exe"'
	else : 
		return '/Applications/LibreOffice.app/Contents/MacOS/soffice'

# convert presentation to PDF
print('converting presentation to PDF')
outDir = workingDirectory 
if operatingSystem == 'win' : 
	if outDir[-1] == os.sep : 
		outDir = outDir[:-1]

# tempDir = os.environ['TEMP'] + 'LibreOfficeTemp'
# print(tempDir)
# raise SystemExit(0)

# os.system('mkdir ' + tempDir)
# raise SystemExit(0)

command = getLibreOfficePythonPath() + ' --headless --invisible --convert-to pdf --outdir ' + outDir + ' ' + presentationPath
print(command)
os.system(command)
print('saving PDF to : ' + pdfPath)
# raise SystemExit(0)

# clean "convertedImages" directory
removeCommand = 'rm'
if operatingSystem == 'win' : 
	removeCommand = 'del'
for root, dirs, files in os.walk(workingDirectory + 'convertedImages') : 
	for file in files : 
		os.system(removeCommand + ' ' + os.path.join(root, file))

# convert PDF to corresponding page PDFS
# command = workingDirectory + 'cpdf-binaries-master/OSX-Intel/cpdf -split ' + pdfPath + ' -o ' + workingDirectory + 'convertedImages' + os.sep + fileName + '-%%%%' + '.pdf'
# print(command)
# os.system(command)

# convert individual page PDF's to JPEG's
# for root, dirs, files in os.walk(workingDirectory + 'convertedImages') : 
# 	for file in files : 
# 		if file.split('.')[-1] == 'pdf' : 
# 			command = getLibreOfficePythonPath() + ' --headless --invisible --convert-to ' + outputImageFormat + ' --outdir ' + workingDirectory + 'convertedImages' + ' ' + os.path.join(root, file)
			# os.system(command)

def getConvertPath() : 
	if operatingSystem == 'win' : 
		return '"C:\Program Files\ImageMagick-7.0.3-Q16\magick.exe"'
	else : 
		return '/opt/ImageMagick/bin/convert '

def getGsPath() : 
	return ':/usr/local/bin'

# add ghostScript "gs" to path
print(os.environ['PATH'])
os.environ['PATH'] = os.environ['PATH'] + getGsPath()
print(os.environ['PATH'])

# convert PDF to images
print('converting PDF to images')
command = getConvertPath() + ' ' + pdfPath + ' ' + tempImagePath
print(command)
os.system(command)
# pdfToImages(pdfPath, tempImagePath)

# generate list of output files
fileNameList = []
for root, dirs, files in os.walk(workingDirectory + 'convertedImages') : 
	for file in files : 
		if file.split('.')[-1] == outputImageFormat : 
			fileNameList.append(file)

# resize images
for file in fileNameList : 
	command = getConvertPath() + ' ' + file + ' -resize 720x540\! ' + file
	os.system(command)

# move images to "outputLocation"
moveCommand = 'mv'
if operatingSystem == 'win' : 
	moveCommand = 'move'
for root, dirs, files in os.walk(workingDirectory + 'convertedImages') : 
	for file in files : 
		os.system(moveCommand + ' ' + os.path.join(root, file) + ' ' + outputLocation)

# 
echoCommand = 'echo'
pipeCommand = '>>'
if operatingSystem == 'win' : 
	echoCommand = 'echo.'
	pipeCommand = '>'
outputfilesListName = 'outputFilesList.txt'
os.system(removeCommand + ' ' + workingDirectory + outputfilesListName)
# os.system(echoCommand + ' ' + '"" ' + pipeCommand + ' ' + workingDirectory + outputfilesListName)
# os.mkdir(workingDirectory + outputfilesListName)
with open(workingDirectory + outputfilesListName, 'w+') : 
	pass

fileNameString = ''
for fileName in fileNameList : 
	fileNameString += fileName + ','
fileNameString = fileNameString[:-1]

command = echoCommand + ' ' + fileNameString + ' ' + pipeCommand + ' ' + workingDirectory + outputfilesListName
print(command)
os.system(command)