package util;

import org.json.JSONArray;
import org.json.JSONException;

public class StringHelper {

	private static String DB_COMMA = ",";

	public static String genDBInStatement(JSONArray input) throws JSONException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			sb = sb.append(input.getString(i)).append(DB_COMMA);
		}
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - 1);
		} else {
			return sb.toString();
		}
	}
}
