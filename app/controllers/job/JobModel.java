package controllers.job;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.MysqlHelper;

public class JobModel {
	private MysqlHelper m_mysqlHelper = new MysqlHelper();
	
	// JOBLSIT
	public JSONArray list(int companyId,boolean admin,boolean random) {
		
		StringBuffer sql = new StringBuffer("SELECT JOB_ID,JOB_NAME,IS_OPEN FROM JOB WHERE IS_DELETE=0 ");
		
		JSONArray params = new JSONArray();
		
		if(companyId!=-1) {
			sql.append(" AND COMPANY_ID = ? ");
			params.put(companyId);
		}
		
		if(!admin) {
			sql.append(" AND IS_OPEN=1 ");
		}
		
		if(random) {
			sql.append( "ORDER BY RAND() LIMIT 10" );
		}
		
		return m_mysqlHelper.executeSql(sql.toString(), params);
	}

	//JOBDETAIL
	public JSONObject detail(int jobId) throws JSONException {
		String sql=" SELECT JOB_ID,JOB_NAME,CONTENT,REQUIREMENT,BENEFIT,SALARY,IS_OPEN FROM JOB"
				+ " WHERE IS_DELETE=0 AND JOB_ID=? ";
		
		JSONArray params = new JSONArray();
		params.put(jobId);
		
		return m_mysqlHelper.executeSql(sql, params).getJSONObject(0);
	}
	
	//職缺狀態處理
	public boolean is_open(int jobId,int state) {
		String sql ="UPDATE JOB SET IS_OPEN=1-IS_OPEN WHERE JOB_ID=?";
		
		JSONArray params = new JSONArray();
		params.put(jobId);
		//params.put(state);
		
		return m_mysqlHelper.executeNonSelectSql(sql, params);
	}

	//刪除職缺
	public boolean delete(int jobId) {
		String sql="UPDATE JOB SET IS_DELETE =1 WHERE JOB_ID=?";
		
		JSONArray params = new JSONArray();
		params.put(jobId);
		
		return m_mysqlHelper.executeNonSelectSql(sql, params);
	}
	
	//新增
	public boolean create (JobObject param) {
		String sql="INSERT INTO JOB(JOB_NAME,COMPANY_ID,CONTENT,REQUIREMENT,BENEFIT,SALARY)"
				+ " VALUES(?,?,?,?,?,?)";
		
		JSONArray params = new JSONArray();
		params.put(param.name);
		params.put(param.companyId);
		params.put(param.content);
		params.put(param.requirement);
		params.put(param.benefit);
		params.put(param.salary);
		
		return m_mysqlHelper.executeNonSelectSql(sql, params);
	}

	public boolean update (JobObject param) {
		String sql ="UPDATE JOB SET JOB_NAME=?,CONTENT=?,REQUIREMENT=?,BENEFIT=?,"
				+ "SALARY=? WHERE JOB_ID=?";
		
		JSONArray params = new JSONArray();
		params.put(param.name);
		params.put(param.content);
		params.put(param.requirement);
		params.put(param.benefit);
		params.put(param.salary);
		params.put(param.id);
		
		return m_mysqlHelper.executeNonSelectSql(sql, params);
	}
}
