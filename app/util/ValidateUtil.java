package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ValidateUtil {
	private static SimpleDateFormat m_sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat m_fullsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static boolean isValidDate(String str) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(m_sdf.parse(str));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isValidDateTime(String str) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(m_fullsdf.parse(str));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
