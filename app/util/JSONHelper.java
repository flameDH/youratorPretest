package util;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.base.Strings;

public class JSONHelper {

	public static JSONArray setArrayDefaultJSONStrValue(JSONArray jsonArray, String[] keys, String[] defStrs) {
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				jsonObj = setDefaultJSONStrValue(jsonObj, keys, defStrs);
			}
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONObject setDefaultJSONStrValue(JSONObject jsonObj, String[] keys, String[] defStrs) {
		try {
			for (int j = 0; j < keys.length; j++) {
				String key = keys[j];
				String defStr = defStrs[j];
				if (!jsonObj.has(key) || jsonObj.getString(key).equals("")) {
					jsonObj.put(key, defStr);
				}
			}
			return jsonObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getString(JSONObject jsonObj, String key, String defStr) {
		try {
			if (jsonObj.has(key) && !Strings.isNullOrEmpty(jsonObj.getString(key))) {
				return jsonObj.getString(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defStr;
	}

	public static int getInt(JSONObject jsonObj, String key, int defStr) {
		try {
			if (jsonObj.has(key)) {
				return jsonObj.getInt(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defStr;
	}

}
