package controllers.company;

import com.fasterxml.jackson.databind.JsonNode;

public class CompanyObject {
	public int id;
	public String name;
	public String intro="";
	public String concept="";
	public String report ="";
	public String salary ="";

	public CompanyObject() {
		
	}
	
	public CompanyObject(JsonNode input) {
		
		id = input.get("ID").asInt();

		intro = input.get("INTRO").asText();
		concept=input.get("CONCEPT").asText();
		report =input.get("REPORT").asText();
		salary = input.get("SALARY").asText();
	}
	
}
