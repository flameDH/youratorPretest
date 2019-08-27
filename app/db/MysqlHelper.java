package db;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Strings;

import util.LoggerUtil;

public class MysqlHelper {
	private static final String SELECT_HEADER = "SELECT ";
	public static final String LIKE_STR = "%";
	public static final String COMMA_STR = ",";

	public JSONArray executeSql(String sql) {
		return executeSql(sql, null);
	}

	public JSONArray executeSql(String sql, JSONArray params) {
		JSONArray rtn = new JSONArray();
		try {
			rtn = _executeSql(sql, params);
		} catch (Exception e) {
			LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
			return null;
		}
		return rtn;
	}

	public JSONArray executeStoreprocedure(String sql, JSONArray params) {
		JSONArray rtn = new JSONArray();
		try {
			rtn = _executeStoreprocedure(sql, params);
		} catch (Exception e) {
			LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
			return null;
		}
		return rtn;
	}

	public boolean executeNonSelectStoreprocedure(String sql, JSONArray params) {
		JSONArray result = executeStoreprocedure(sql, params);
		if (result != null)
			return true;
		else
			return false;
	}

	public int executeNonSelectStoreprocedureWithRtnCode(String sql, JSONArray params, String rtnCodeKey)
			throws JSONException {
		JSONArray result = executeStoreprocedure(sql, params);
		int rtnCode = -1;
		if (result != null && result.length() > 0) {
			rtnCode = result.getJSONObject(0).getInt(rtnCodeKey);
		}
		return rtnCode;
	}

	public int executeBatchSql(String sql, JSONArray[] params) throws SQLException {
		int totalUpdate = 0;
		try {
			totalUpdate = _executeBatchSql(sql, params);
		} catch (Exception e) {
			LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
			return -1;
		}
		return totalUpdate;
	}

	public boolean executeNonSelectSql(String sql) {
		return executeNonSelectSql(sql, null);
	}

	public boolean executeNonSelectSql(String sql, JSONArray params) {
		try {
			JSONArray reult = _executeSql(sql, params);
			if (reult != null) {
				return true;
			} else {
				return false;
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public DbErrorCode executeNonSelectSqlWithErrorCode(String sql, JSONArray params) {
		try {
			JSONArray reult = _executeSql(sql, params);
			if (reult != null) {
				return DbErrorCode.OK;
			} else {
				return DbErrorCode.SYS_ERROR;
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			return DbErrorCode.DUPLICATE;
		} catch (SQLException e) {
			if (e.getSQLState().equals(DbErrorCode.DUPLICATE.getDbErrorCode())) {
				return DbErrorCode.DUPLICATE;
			} else {
				return DbErrorCode.SYS_ERROR;
			}
		} catch (Exception e) {
			return DbErrorCode.SYS_ERROR;
		}
	}

	private JSONArray _executeSql(String sql, JSONArray params) throws Exception {
		JSONArray rtn = new JSONArray();
		if (MysqlConnector.isEnable()) {
			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			try {
				sql = trimSql(sql);
				connection = MysqlConnector.getConnection();
				stmt = connection.prepareStatement(sql);
				setParams(stmt, params, connection);
				if (sql.toUpperCase().startsWith(SELECT_HEADER)) {

					long start = System.currentTimeMillis();
					resultSet = stmt.executeQuery();
					long end = System.currentTimeMillis();
					long diff = end - start;
					if ((diff) > 100) {
						LoggerUtil.logDbDebug("[EXECUTE WARNING] TIME:" + diff + " > 100 == " + sql);
					} else {
						// LoggerUtil.logDbDebug("[EXECUTE DEBUG] " + diff + "="
						// + sql);
					}

					ResultSetMetaData metaData = resultSet.getMetaData();
					setReturnArray(resultSet, metaData, rtn);
				} else {
					long start = System.currentTimeMillis();
					stmt.execute();
					long end = System.currentTimeMillis();
					long diff = end - start;
					if ((diff) > 100) {
						LoggerUtil.logDbDebug("[EXECUTE WARNING] TIME:" + diff + " > 100 == " + sql);
					} else {
						// LoggerUtil.logDbDebug("[EXECUTE DEBUG] " + diff + "="
						// + sql);
					}
				}
			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
				throw e;
			} finally {
				doCloseHandling(resultSet, connection, stmt);
			}
		}
		return rtn;
	}

	private JSONArray _executeStoreprocedure(String sql, JSONArray params) throws Exception {
		JSONArray rtn = new JSONArray();
		if (MysqlConnector.isEnable()) {
			Connection connection = null;
			CallableStatement stmt = null;
			ResultSet resultSet = null;
			try {
				sql = trimStoreSql(sql);
				connection = MysqlConnector.getConnection();
				stmt = connection.prepareCall(sql);
				setParams(stmt, params, connection);
				long start = System.currentTimeMillis();
				stmt.execute();
				long end = System.currentTimeMillis();
				long diff = end - start;
				if ((diff) > 100) {
					LoggerUtil.logDbDebug("[EXECUTE WARNING] TIME:" + diff + " > 100 == " + sql);
				} else {
					// LoggerUtil.logDbDebug("[EXECUTE DEBUG] " + diff + "=" +
					// sql);
				}
				resultSet = stmt.getResultSet();
				ResultSetMetaData metaData = null;
				if (resultSet != null) {
					metaData = resultSet.getMetaData();
					setReturnArray(resultSet, metaData, rtn);
				}

			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
				throw e;
			} finally {
				doCloseHandling(resultSet, connection, stmt);
			}
		}
		return rtn;
	}

	private int _executeBatchSql(String sql, JSONArray[] params) throws Exception {
		int totalUpdate = 0;

		if (MysqlConnector.isEnable()) {
			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			try {
				connection = MysqlConnector.getConnection();
				connection.setAutoCommit(false);
				sql = trimSql(sql);
				stmt = connection.prepareStatement(sql);
				for (int i = 0; i < params.length; i++) {
					JSONArray param = null;
					if (params != null)
						param = params[i];
					setParams(stmt, param, connection);
					stmt.addBatch();
				}
				long start = System.currentTimeMillis();
				int[] numUpdates = stmt.executeBatch();
				long end = System.currentTimeMillis();
				long diff = end - start;
				if ((diff) > 100) {
					LoggerUtil.logDbDebug("[EXECUTE WARNING] TIME:" + diff + " > 100 == " + sql);
				} else {
					// LoggerUtil.logDbDebug("[EXECUTE DEBUG] " + diff + "=" +
					// sql);
				}

				for (int i = 0; i < numUpdates.length; i++) {
					if (numUpdates[i] != -2)
						totalUpdate++;
				}
				connection.commit();
			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
				connection.rollback();
				throw e;
			} finally {
				doCloseHandling(resultSet, connection, stmt);
			}
		}
		return totalUpdate;
	}

	private void setParams(PreparedStatement stmt, JSONArray params, Connection connection)
			throws SQLException, JSONException {
		if (params != null) {
			for (int i = 0; i < params.length(); i++) {
				int index = i + 1;
				Object param = params.get(i);
				if (param != null) {
					if (param instanceof Integer) {
						stmt.setInt(index, (Integer) param);
					} else if (param instanceof Array) {
						stmt.setArray(index, (Array) param);
					} else if (param instanceof Long) {
						stmt.setLong(index, (Long) param);
					} else if (param instanceof byte[]) {
						stmt.setBytes(index, (byte[]) param);
					} else if (param instanceof JSONArray) {
						Object[] arr = new Object[((JSONArray) param).length()];
						for (int j = 0; j < ((JSONArray) param).length(); j++) {
							arr[j] = ((JSONArray) param).get(j);
						}
						stmt.setArray(index, connection.createArrayOf("VARCHAR", arr));
					} else {
						if ((JSONObject.NULL.equals(param)))
							stmt.setNull(index, Types.VARCHAR);
						else {
							stmt.setString(index,
									((String) param).replaceAll("<\\s*script\\s*>|<\\s*/\\s*script\\s*>", ""));
						}
					}
				} else {
					stmt.setNull(index, Types.VARCHAR);
				}
			}
		}
	}

	private void setReturnArray(ResultSet resultSet, ResultSetMetaData metaData, JSONArray rtn)
			throws SQLException, JSONException {
		while (resultSet.next()) {
			JSONObject json = new JSONObject();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				int type = metaData.getColumnType(i);
				if (resultSet.getString(i) == null) {

				} else if (type == Types.BLOB || type == Types.LONGVARBINARY) {
					byte[] imageBytes = resultSet.getBytes(i);
					String imageBase64 = DatatypeConverter.printBase64Binary(imageBytes);
					json.put(metaData.getColumnLabel(i), imageBase64);
				} else if (type == Types.INTEGER || type == Types.TINYINT) {
					json.put(metaData.getColumnLabel(i), resultSet.getInt(i));
				} else {
					json.put(metaData.getColumnLabel(i), resultSet.getString(i));
				}
			}
			rtn.put(json);
		}
	}

	private void doCloseHandling(ResultSet resultSet, Connection connection, PreparedStatement stmt) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LoggerUtil.logDebug("[DB Error] , e = " + e);
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				LoggerUtil.logDebug("[DB Error] , e = " + e);
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LoggerUtil.logDebug("[DB Error] , e = " + e);
			}
		}
	}

	private String trimSql(String query) {
		return query.trim();
	}

	private String trimStoreSql(String query) {
		if (MysqlConnector.isMariaDB())
			return "{ " + trimSql(query) + "}";
		else
			return trimSql(query);
	}

	public static String genArrayInSqlStatement(String sql, JSONArray params, String[] iptArray) throws JSONException {

		sql = sql + "(";

		for (int i = 0; i < iptArray.length; i++) {
			sql = sql + "? ,";
			params.put(iptArray[i]);
		}
		sql = sql.substring(0, sql.length() - 1);
		sql = sql + ")";
		return sql;
	}

	public static String genArrayInSqlStatement(String sql, JSONArray params, JSONArray iptArray) throws JSONException {

		sql = sql + "(";

		for (int i = 0; i < iptArray.length(); i++) {
			sql = sql + "? ,";
			params.put(iptArray.get(i));
		}
		sql = sql.substring(0, sql.length() - 1);
		sql = sql + ")";
		return sql;
	}

	public static String genIntPreparedStatement(JSONArray iptArray) throws JSONException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iptArray.length(); i++) {
			int id = iptArray.getInt(i);
			sb.append(id).append(COMMA_STR);
		}
		return sb.toString();
	}

	public static String genStrPreparedStatement(JSONArray iptArray) throws JSONException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iptArray.length(); i++) {
			String id = iptArray.getString(i);
			sb.append(id).append(COMMA_STR);
		}
		return sb.toString();
	}

	public static String genIntPreparedStatement(Integer[] iptArray) throws JSONException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iptArray.length; i++) {
			int id = iptArray[i];
			sb.append(id).append(COMMA_STR);
		}
		return sb.toString();
	}

	public static String genStrPreparedStatement(String[] iptArray) throws JSONException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iptArray.length; i++) {
			String id = iptArray[i];
			sb.append(id).append(COMMA_STR);
		}
		return sb.toString();
	}

	public static String genLikeKey(String key) throws JSONException {
		StringBuffer sb = new StringBuffer();
		if (!Strings.isNullOrEmpty(key)) {
			sb.append(LIKE_STR).append(key).append(LIKE_STR);
		}
		return sb.toString();
	}

	public static String genArrayInSqlStatement(String sql, JSONArray params, Integer[] iptArray) throws JSONException {

		sql = sql + "(";

		for (int i = 0; i < iptArray.length; i++) {
			sql = sql + "? ,";
			params.put(iptArray[i]);
		}
		sql = sql.substring(0, sql.length() - 1);
		sql = sql + ")";
		return sql;
	}

	public JSONArray executeTransactionSql(ArrayList<String> sqlList, ArrayList<JSONArray> paramsList) {
		JSONArray rtn = new JSONArray();
		if (MysqlConnector.isEnable()) {
			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet resultSet = null;

			try {
				if (sqlList.size() != paramsList.size()) {
					throw new IllegalArgumentException("[DB Error] mismatch ArrayList size, sqlList: "
							+ String.valueOf(sqlList.size()) + " paramsList: " + String.valueOf(paramsList.size()));
				}
				connection = MysqlConnector.getConnection();
				connection.setAutoCommit(false);

				for (int i = 0; i < sqlList.size(); i++) {
					stmt = connection.prepareStatement(trimSql(sqlList.get(i)));
					setParams(stmt, paramsList.get(i), connection);
					// 若為SELECT句，execute()回true且有resultSet
					if (stmt.execute()) {
						resultSet = stmt.getResultSet();
						ResultSetMetaData metaData = null;
						if (resultSet != null) {
							metaData = resultSet.getMetaData();
							setReturnArray(resultSet, metaData, rtn);
						}
					}
				}

				connection.commit();
				return rtn;
			} catch (IllegalArgumentException e) {
				LoggerUtil.logException(e);
				return null;
			} catch (SQLException e) {
				try {
					connection.rollback();
					LoggerUtil.logDebug("[DB Error] commit fail, e = " + e);
					return null;
				} catch (Exception eRoll) {
					LoggerUtil.logDebug("[DB Error] rollback fail");
					return null;
				}
			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] e = " + e);
				return null;
			} finally {
				doCloseHandling(resultSet, connection, stmt);
			}
		}
		LoggerUtil.logDebug("[DB Error] MysqlConnector is disable, firstSql = " + sqlList.get(0));
		return null;
	}


}
