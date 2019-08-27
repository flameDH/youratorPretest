package util;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import enums.ErrorCode;
import play.libs.Json;

public class ResponseHelper {

	public static ObjectNode genArrayResult(JSONArray iptArray, int total, String[] iptArrays, String[] resArrays) {
		ObjectNode rtnJson = Json.newObject();
		ArrayNode labelArray = _genArrayResult(iptArray, iptArrays, resArrays);
		if (labelArray == null)
			return null;
		else {
			rtnJson.set("DATA", labelArray);
			rtnJson.put("TOTAL", total);
			return genOKResult(rtnJson);
		}
	}

	public static ObjectNode genArrayResult(ObjectNode rtnJson, JSONArray iptArray, String keyName, int total)
			throws JSONException {
		genArrayResult(rtnJson, iptArray, keyName);
		rtnJson.put("TOTAL", total);
		return genOKResult(rtnJson);
	}

	public static ObjectNode genArrayResult(JSONArray iptArray, String[] iptArrays, String[] resArrays) {
		ObjectNode rtnJson = Json.newObject();
		ArrayNode labelArray = _genArrayResult(iptArray, iptArrays, resArrays);
		if (labelArray == null)
			return null;
		else {
			rtnJson.set("DATA", labelArray);
			return genOKResult(rtnJson);
		}
	}

	public static ObjectNode genArrayResult(ObjectNode rtnJson, JSONArray iptArray, String[] iptArrays,
			String[] resArrays, String keyName) {
		ArrayNode labelArray = _genArrayResult(iptArray, iptArrays, resArrays);
		if (labelArray != null) {
			rtnJson.set(keyName, labelArray);
		}
		return rtnJson;
	}

	public static ObjectNode genArrayResult(ObjectNode rtnJson, JSONArray iptArray, String keyName)
			throws JSONException {
		ArrayNode labelArray = Json.newArray();
		if (iptArray.length() != 0) {
			Iterator<String> keys = iptArray.getJSONObject(0).keys();
			ArrayList<String> keyArray = new ArrayList<String>();
			while (keys.hasNext()) {
				keyArray.add(keys.next());
			}
			return genArrayResult(rtnJson, iptArray, keyArray.toArray(new String[keyArray.size()]),
					keyArray.toArray(new String[keyArray.size()]), keyName);
		} else {
			rtnJson.set(keyName, labelArray);
		}
		return rtnJson;
	}

	public static ObjectNode genObjectResult(JSONObject iptJSON, String[] iptArrays, String[] resArrays) {
		return _genObjectResult(null, iptJSON, iptArrays, resArrays);
	}

	public static ObjectNode genObjectResult(ObjectNode rtnJSON, JSONObject iptJSON) {
		Iterator<String> keys = iptJSON.keys();
		ArrayList<String> keyArray = new ArrayList<String>();
		while (keys.hasNext()) {
			keyArray.add(keys.next());
		}
		return genObjectResult(rtnJSON, iptJSON, keyArray.toArray(new String[keyArray.size()]));
	}

	public static ObjectNode genObjectResult(JSONObject iptJSON) {
		ObjectNode rtnJSON = Json.newObject();
		Iterator<String> keys = iptJSON.keys();
		ArrayList<String> keyArray = new ArrayList<String>();
		while (keys.hasNext()) {
			keyArray.add(keys.next());
		}
		return genObjectResult(rtnJSON, iptJSON, keyArray.toArray(new String[keyArray.size()]));
	}

	public static ObjectNode genObjectResult(ObjectNode rtnJSON, JSONObject iptJSON, String keyName) {
		Iterator<String> keys = iptJSON.keys();
		ArrayList<String> keyArray = new ArrayList<String>();
		while (keys.hasNext()) {
			keyArray.add(keys.next());
		}
		return genObjectResult(rtnJSON, iptJSON, keyArray.toArray(new String[keyArray.size()]), keyName);
	}

	public static ObjectNode genObjectResult(ObjectNode rtnJSON, JSONObject iptJSON, String[] iptArrays,
			String[] resArrays) {
		return _genObjectResult(rtnJSON, iptJSON, iptArrays, resArrays);
	}

	public static ObjectNode genObjectResult(ObjectNode rtnJSON, JSONObject iptJSON, String[] iptArrays,
			String[] resArrays, String keyName) {
		ObjectNode newJSONNode = _genObjectResult(null, iptJSON, iptArrays, resArrays);
		rtnJSON.set(keyName, newJSONNode);
		return rtnJSON;
	}

	public static ObjectNode genObjectResult(ObjectNode rtnJSON, JSONObject iptJSON, String[] iptArrays) {
		return _genObjectResult(rtnJSON, iptJSON, iptArrays, null);
	}

	public static ObjectNode genObjectResult(ObjectNode rtnJSON, JSONObject iptJSON, String[] iptArrays,
			String keyName) {
		ObjectNode newJSONNode = _genObjectResult(null, iptJSON, iptArrays, null);
		rtnJSON.set(keyName, newJSONNode);
		return rtnJSON;
	}

	private static ObjectNode _genObjectResult(ObjectNode rtnJSON, JSONObject iptJSON, String[] iptArrays,
			String[] resArrays) {
		if (rtnJSON == null)
			rtnJSON = Json.newObject();
		for (int j = 0; j < iptArrays.length; j++) {
			try {
				if (resArrays != null) {
					putStringJSONs(iptJSON, rtnJSON, iptArrays[j], resArrays[j]);
				} else {
					putStringJSONs(iptJSON, rtnJSON, iptArrays[j], iptArrays[j]);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return rtnJSON;
	}

	private static ArrayNode _genArrayResult(JSONArray iptArray, String[] iptArrays, String[] resArrays) {
		ArrayNode labelArray = Json.newArray();
		for (int i = 0; i < iptArray.length(); i++) {
			try {

				JSONObject iptJSON = (JSONObject) iptArray.get(i);
				ObjectNode node = _genObjectResult(null, iptJSON, iptArrays, resArrays);
				labelArray.add(node);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		return labelArray;
	}

	public static ObjectNode genOKResult(ObjectNode rtnJson) {
		return genResult(rtnJson, ErrorCode.OK);
	}

	public static ObjectNode genOKResult() {
		ObjectNode rtnJson = Json.newObject();
		return genOKResult(rtnJson);
	}

	public static ObjectNode genResult(ObjectNode rtnJson, ErrorCode err) {
		rtnJson.put("ERR", err.getID());
		rtnJson.put("ERRMSG", err.getMsg());
		return rtnJson;
	}

	public static ObjectNode genErrorResult(ErrorCode err) {
		ObjectNode rtnJson = Json.newObject();
		return genResult(rtnJson, err);
	}
	
	public static ObjectNode genParamErrorResult() {
		ObjectNode rtnJson = Json.newObject();
		return genResult(rtnJson, ErrorCode.PARAM_ERR);
	}
	

	public static ObjectNode genErrorResult(int errorCode, String errorMsg) {
		ObjectNode rtnJson = Json.newObject();
		rtnJson.put("ERR", errorCode);
		rtnJson.put("ERRMSG", errorMsg);
		return rtnJson;
	}

	private static ObjectNode putStringJSONs(JSONObject fromJson, ObjectNode toJsons, String fromKey, String toKey)
			throws JSONException {
		if (fromJson.has(fromKey)) {
			toJsons.put(toKey, fromJson.getString(fromKey));
		} else {
			toJsons.put(toKey, "");
		}
		return toJsons;
	}
}
