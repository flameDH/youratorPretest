package util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
	private static final String NUMBERS = "0123456789";
	private static final String ALBABETS = "abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ";
	private static final String ALLSYMBOLS = "0123456789abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ";

	public static String generateNumber(int length) {
		return genString(length, NUMBERS.length(), NUMBERS);
	}

	public static String generateAlbabets(int length) {
		return genString(length, ALBABETS.length(), ALBABETS);

	}

	public static String generateSymbols(int length) {
		return genString(length, ALLSYMBOLS.length(), ALLSYMBOLS);
	}

	private static String genString(int length, int numLength, String key) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb = sb.append(key.charAt(ThreadLocalRandom.current().nextInt(0, numLength)));
		}
		return sb.toString();
	}

}
