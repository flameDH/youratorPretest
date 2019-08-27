package controllers.job;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

public class JobHandler {
	public ArrayNode processJob(JSONArray raw) throws JSONException {
		ArrayNode result = Json.newArray();
		for(int i=0;i<raw.length();i++) {
			JSONObject temp =raw.getJSONObject(i);
			ObjectNode buffer = Json.newObject(); 
			
			buffer.put("jobId", temp.getInt("JOB_ID"));
			buffer.put("jobName", temp.getString("JOB_NAME"));
			buffer.put("IS_OPEN", temp.getString("IS_OPEN"));
			
			result.add(buffer);
		}
		
		return result;
	}
	
	public ObjectNode processDetail(JSONObject raw) throws JSONException {
		ObjectNode result= Json.newObject();
		
		result.put("JOB_ID", raw.getInt("JOB_ID"));
		result.put("JOB_NAME", raw.getString("JOB_NAME"));
		result.put("CONTENT", raw.getString("CONTENT"));
		result.put("REQUIREMENT", raw.getString("REQUIREMENT"));
		result.put("BENEFIT", raw.getString("BENEFIT"));
		result.put("SALARY", raw.getString("SALARY"));
		result.put("IS_OPEN", raw.getInt("IS_OPEN"));
		
		return result;
	}
	
}
