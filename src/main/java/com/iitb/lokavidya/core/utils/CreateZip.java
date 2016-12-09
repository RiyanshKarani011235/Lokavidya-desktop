package com.iitb.lokavidya.core.utils;

/*
* Copyright 2010 Srikanth Reddy Lingala  
* 
* Licensed under the Apache License, Version 2.0 (the "License"); 
* you may not use this file except in compliance with the License. 
* You may obtain a copy of the License at 
* 
* http://www.apache.org/licenses/LICENSE-2.0 
* 
* Unless required by applicable law or agreed to in writing, 
* software distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and 
* limitations under the License. 
*/


import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;


public class CreateZip {
	
	public CreateZip(String fileLocation, String zipLocation) {
		
		try {
			zipLocation=new File(zipLocation,(new File(fileLocation).getName()+".zip")).getAbsolutePath();
			System.out.println(zipLocation);
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(zipLocation);
			
			// Folder to add
			
			// Initiate Zip Parameters which define various properties such
			// as compression method, etc.
			ZipParameters parameters = new ZipParameters();
			
			// set compression method to store compression
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			
			// Set the compression level
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			
			// Add folder to the zip file
			zipFile.addFolder(fileLocation, parameters);
			
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new CreateZip("/home/frg/Documents/one","/home/frg/Documents");
	}
	
}
