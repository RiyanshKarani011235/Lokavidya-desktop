package com.iitb.lokavidya.core.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Demonstrates extracting all files from a zip file
 * 
 * @author Srikanth Reddy Lingala
 *
 */
public class ExtractZip {
	
	public ExtractZip(String zipLocation,String destination) {
		
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(zipLocation);
			
			// Extracts all files to the path specified
			zipFile.extractAll(destination);
			
		} catch (ZipException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ExtractZip("/home/frg/Downloads/poshanvatika.zip","/home/frg/Downloads");
	}

}

