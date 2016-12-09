import os
import sys

def getWorkingDirectory() : 

	# print('windows')
	workingDirectoryList = __file__.split(os.sep)[:-1]
	# print(workingDirectoryList)
	workingDirectory = os.sep
	for element in workingDirectoryList : 
		workingDirectory = os.path.join(workingDirectory, element)
		if workingDirectory[-1] != os.sep : 
			workingDirectory += os.sep
	return workingDirectory
	
	# else : 
	# 	pass

	# workingDirectoryList = __file__.split(os.sep)[:-1]
	# print(workingDirectoryList)
	# workingDirectory = os.sep
	# for element in workingDirectoryList : 
	# 	workingDirectory = os.path.join(workingDirectory, element)
	# return workingDirectory + os.sep

# print(getWorkingDirectory())