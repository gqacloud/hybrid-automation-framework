package com.framework.utils;

import org.apache.commons.text.RandomStringGenerator;

public class RandomDataUtils {

	// Generate alphabetic strings
	public static String randomString() {
		RandomStringGenerator stringGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').get();
		String randomstring = stringGenerator.generate(7);
		return randomstring;

	}

	// Generate alphanumeric strings
	public static String getRandomAlphaNumeric(int length) {
		return new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(Character::isLetterOrDigit).build()
				.generate(9);
	}

	/*
	 * public String aphaNumeric() { RandomStringGenerator stringGenerator = new
	 * RandomStringGenerator.Builder() .withinRange('0','z')
	 * .filteredBy(Character::isLetterOrDigit).get(); String alphanumeric =
	 * stringGenerator.generate(9); return alphanumeric;
	 * 
	 * }
	 */
	
	// Example: random email
	public static String getRandomEmail() {
		return getRandomAlphaNumeric(8) + "@gmail.com";
	}

	public static String randomNumber() {
		RandomStringGenerator stringGenerator = new RandomStringGenerator.Builder().withinRange('0', '9').get();
		String randomNum = stringGenerator.generate(10);
		return randomNum;

	}

}
