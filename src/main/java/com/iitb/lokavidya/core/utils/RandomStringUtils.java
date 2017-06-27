package com.iitb.lokavidya.core.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomStringUtils {
	
	// CONSTANTS
	private static final String ALPHANEUMERIC_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public static String getSystemTimeString() {
		return (new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS")).format(new Date());
	}

	public static String getRandomAlphaneumericString(int len){
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder(len);
		for(int i = 0; i < len; i++) {
			sb.append( ALPHANEUMERIC_CHARACTERS.charAt( rnd.nextInt(ALPHANEUMERIC_CHARACTERS.length()) ) );
		}
		return sb.toString();
	}
	
	public static String getUniqueRandomString() {
		return getSystemTimeString() + "-" + getRandomAlphaneumericString(20);
	}

}
