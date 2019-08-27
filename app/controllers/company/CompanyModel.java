package controllers.company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.MysqlHelper;

public class CompanyModel {
	private MysqlHelper m_mysqlHelper = new MysqlHelper();
	
	//company List
	public JSONArray companyList(boolean random) {
		StringBuffer sql= new StringBuffer(" SELECT ID,NAME FROM COMPANY ");
		
		if(random) {
			sql.append(" ORDER BY RAND() LIMIT 5 ");
		}
		
		return m_mysqlHelper.executeSql(sql.toString());
	}
	
	//company Detail
	public JSONObject companyDetail(int companyID) throws JSONException {
		String sql ="SELECT * FROM COMPANY "
				+ " WHERE ID=? ";
		
		JSONArray params = new JSONArray();
		params.put(companyID);
		
		return m_mysqlHelper.executeSql(sql, params).getJSONObject(0);
	}
	
	//update company info
	public boolean update(CompanyObject obj) {
		String sql ="UPDATE COMPANY SET INTRO=?,CONCEPT=?,REPORT=?,SALARY=? WHERE ID=?";
		
		JSONArray params = new JSONArray();
		params.put(obj.intro);
		params.put(obj.concept);
		params.put(obj.report);
		params.put(obj.salary);
		params.put(obj.id);
		
		return m_mysqlHelper.executeNonSelectSql(sql, params);
	}
	
}
