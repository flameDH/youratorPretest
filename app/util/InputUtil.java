package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.common.base.Strings;

public class InputUtil {

	private static final SimpleDateFormat DATE_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String DIGIT_REGEX = "[0-9]+";
	private static final String ALPHABAT_REGEX = "[A-Za-z]+";

	public static boolean isValidDate(String yymmdd) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(DATE_SDF.parse(yymmdd));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isAllDigit(String phone) {
		if (!Strings.isNullOrEmpty(phone)) {
			if (phone.matches(DIGIT_REGEX)) {
				return true;
			}
		}
		return false;
	}
	public static boolean isAllAlpha(String phone) {
		if (!Strings.isNullOrEmpty(phone)) {
			if (phone.matches(ALPHABAT_REGEX)) {
				return true;
			}
		}
		return false;
	}
}
