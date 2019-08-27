package util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import play.data.DynamicForm;

public class ParamUtil {
	public static boolean hasParam(JsonNode json, String[] keys) {
		for (String key : keys) {
			if (!json.has(key))
				return false;
		}
		return true;
	}

	public static boolean hasParam(DynamicForm form, String[] keys) {
		for (String key : keys) {
			if (form.get(key) == null)
				return false;
		}
		return true;
	}

	public static List<String> checkParam(DynamicForm form, String[] keys) {
		List<String> result = new ArrayList<String>();
		for (String key : keys) {
			if (form.get(key) == null) {
				result.add(key);
			}
		}
		return result;
	}

	public static List<String> checkParam(JsonNode json, String[] keys) {
		List<String> result = new ArrayList<String>();
		for (String key : keys) {
			if (!json.has(key)) {
				result.add(key);
			}
		}
		return result;
	}

	public static boolean isInt(String param) {
		try {
			Integer.parseInt(param);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
