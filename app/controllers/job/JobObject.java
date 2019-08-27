package controllers.job;

import com.fasterxml.jackson.databind.JsonNode;

public class JobObject {
	public int id;
	public int companyId;
	public String name="";
	public String content="";
	public String requirement="";
	public String benefit="";
	public String salary="";
	
	public JobObject() {}
	
	public JobObject(JsonNode input) {
		
		id = input.get("JOB_ID").asInt();
		companyId = input.get("COM_ID").asInt();
		name = input.get("JOB_NAME").asText();
		
		content = input.get("CONTENT").asText();
		requirement= input.get("REQUIREMENT").asText();
		benefit = input.get("BENEFIT").asText();
		salary = input.get("SALARY").asText();
	}
	
}
