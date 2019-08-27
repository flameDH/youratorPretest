package controllers.company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

public class CompanyHandler {
	

	public ArrayNode processCom(JSONArray raw) throws JSONException {
		ArrayNode result = Json.newArray();
		
		for(int i=0;i<raw.length();i++) {
			JSONObject temp =raw.getJSONObject(i);
			ObjectNode buffer = Json.newObject(); 
			
			buffer.put("comId", temp.getInt("ID"));
			buffer.put("comName", temp.getString("NAME"));
			
			result.add(buffer);
		}
		
		return result;
	}
	
	public ObjectNode processDetail(JSONObject raw) throws JSONException {
		ObjectNode result= Json.newObject();
		
		result.put("NAME", raw.getString("NAME"));
		result.put("INTRO", raw.getString("INTRO"));
		result.put("CONCEPT", raw.getString("CONCEPT"));
		result.put("REPORT", raw.getString("REPORT"));
		result.put("SALARY", raw.getString("SALARY"));
		
		return result;
	}
	
	
}
