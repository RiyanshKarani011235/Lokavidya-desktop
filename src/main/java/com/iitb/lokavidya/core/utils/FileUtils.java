package com.iitb.lokavidya.core.utils;

import java.io.File;

public class FileUtils {
	
	private static String mSystemTempDirectoryPath = System.getProperty("java.io.tmpdir");
	private static String tempDirectory = getNewUniqueTempDirectory();

	public static String getNewUniqueTempDirectory() {
		File newUniqueTempFolder = new File(mSystemTempDirectoryPath, RandomStringUtils.getUniqueRandomString());
		// make sure that the temp directory is unique
		while(newUniqueTempFolder.exists()) {
			newUniqueTempFolder = new File(mSystemTempDirectoryPath, RandomStringUtils.getUniqueRandomString());
		}
		newUniqueTempFolder.mkdir();
		return newUniqueTempFolder.toString();
	}
	
	public static String getNewUniqueFileName(String extension) {
		return new File(tempDirectory, RandomStringUtils.getRandomAlphaneumericString(20) + "." + extension).getAbsolutePath();
	}
	
	public static void deleteDirectory(String directoryPath) {
		if(directoryPath != null && new File(directoryPath).exists())
		new File(directoryPath).delete();
	}
	
	public static void main(String args[]) {
		String tempDirectoryPath = getNewUniqueTempDirectory();
		System.out.println(new File(tempDirectoryPath).exists());
		System.out.println(tempDirectoryPath);
		deleteDirectory(tempDirectoryPath);
		System.out.println(new File(tempDirectoryPath).exists());
		System.out.println(getNewUniqueFileName("wav"));
	}
	
}
