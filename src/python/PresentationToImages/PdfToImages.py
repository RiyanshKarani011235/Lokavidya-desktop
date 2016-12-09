import os

def PdfToImages(pdfFile, format='jpeg') : 
	# generating image file name
	convertedFile = ''
	for element in pdfFile.split('.')[:-1] : 
		convertedFile += element + '.'
	convertedFile += format

	# getting the "convertedImages" directory
	working_directory_list = __file__.split(os.sep)[:-1]
	working_directory = os.sep
	for element in working_directory_list : 
		working_directory = os.path.join(working_directory, element)
	working_directory += os.sep
	print('working directory : ' + working_directory)
	image_directory = os.path.join(working_directory, 'convertedImages/')

	# convert to Image
	os.system('convert ' + pdfFile + ' ' + image_directory + convertedFile)

	# resizing the Images
	for root, dirs, files in os.walk(image_directory) : 
		print(files)
		for file in files : 
			if file.split('.')[-1] == 'png' : 
				print('converting ' + os.path.join(root, file))
				os.system('convert ' + os.path.join(root, file) + ' -resize 720x540 ' + os.path.join(root, file))