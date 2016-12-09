import os
import time
import pip
import pdf_to_png

pptPath = 'tamil.pptx'
pdfPath = ''
open_office_python_path = '/Applications/LibreOffice.app/Contents/Resources/python DocumentConverter.py'
libre_office_path = '/Applications/LibreOffice.app/Contents/MacOS/soffice'

for element in pptPath.split('.')[:-1] : 
	pdfPath += element + '.'
pdfPath += 'pdf'
print('saving pdf at ' + pdfPath)

# r, w = os.pipe()
# r,w=os.fdopen(r,'r',0), os.fdopen(w,'w',0)

# start LibreOffice on port 2002 in a separate process
try : 
	if os.fork() > 0 : 
		# main thread
		# r.close()
		time.sleep(5)
		print('converting')
		os.system(open_office_python_path + ' ' + pptPath + ' ' + pdfPath)
		print('done converting')
		pdf_to_png.pdf_to_png(pdfPath)
		print('converting to png done')
		# raise SystemExit(0)
except : 
	# raise RuntimeError('fork failed') 
	pass
else : 
	# fork1 successful
	pass

# child thread
# w.close()
print('starting LibreOffice on port 2002')
os.system(libre_office_path + ' "--headless" "-accept=socket,port=2002;urp;"')
# r.read()
# print('exiting')
# try : 
# 	raise SystemExit(0)
# except : 
# 	pass
