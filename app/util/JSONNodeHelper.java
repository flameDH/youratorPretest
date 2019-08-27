package util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

public class JSONNodeHelper {

	public static String getString(JsonNode json, String key, String defStr) {
		if (json.has(key))
			return json.get(key).asText();
		return defStr;
	}

	public static int getInt(JsonNode json, String key, int defInt) {
		if (json.has(key))
			return json.get(key).asInt();
		return defInt;
	}

	public static ObjectNode putStrFromJSON(JSONObject fromJson, ObjectNode toJsons, String fromKey, String toKey)
			throws JSONException {
		if (fromJson.has(fromKey)) {
			toJsons.put(toKey, fromJson.getString(fromKey));
		}
		return toJsons;
	}

	public static ArrayNode transJSONArrayToJSONNode(JSONArray fromJson) throws JSONException {

		ArrayNode labelArray = Json.newArray();
		for (int i = 0; i < fromJson.length(); i++) {
			labelArray.add(fromJson.getString(i));
		}
		return labelArray;
	}
}
