package db;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.LoggerUtil;

public class MysqlBatchHelper {
	private static final String SELECT_HEADER = "SELECT ";
	public static final String LIKE_STR = "%";
	public static final String COMMA_STR = ",";

	private static final String BEGIN_SQL = "BEGIN;";
	private static final String COMMIT_SQL = "COMMIT;";

	public Connection getConnection(boolean isAutoCommit) throws Exception {
		Connection connection = null;
		if (MysqlConnector.isEnable()) {
			try {
				connection = MysqlConnector.getConnection();
				connection.setAutoCommit(isAutoCommit);
			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] e = " + e);
				throw e;
			}
		}
		return connection;
	}

	public boolean closeConnection(Connection connection) throws Exception {
		if (MysqlConnector.isEnable() && connection != null) {
			try {
				connection = MysqlConnector.getConnection();
				connection.setAutoCommit(true);
				return true;
			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] , e = " + e);
				throw e;
			}
		}
		return false;
	}

	public boolean startTransaction(Connection connection) throws Exception {
		if (MysqlConnector.isEnable() && connection != null) {
			try {
				return executeNonSelectSql(connection, BEGIN_SQL);
			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] e = " + e);
				throw e;
			}
		}
		return false;
	}

	public boolean commitTransaction(Connection connection) throws Exception {
		if (MysqlConnector.isEnable() && connection != null) {
			try {
				return executeNonSelectSql(connection, COMMIT_SQL);
			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] e = " + e);
				throw e;
			}
		}
		return false;
	}

	public boolean rollbackTransaction(Connection connection) throws Exception {
		if (MysqlConnector.isEnable() && connection != null) {
			try {
				connection.rollback();
				return true;
			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] e = " + e);
				throw e;
			}
		}
		return false;
	}

	public JSONArray executeSql(Connection connection, String sql) throws Exception {
		return executeSql(connection, sql, null);
	}

	public JSONArray executeSql(Connection connection, String sql, JSONArray params) throws Exception {
		JSONArray rtn = new JSONArray();
		try {
			rtn = _executeSql(connection, sql, params);
		} catch (Exception e) {
			LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
			throw e;
		}
		return rtn;
	}

	public int executeBatchSql(Connection connection, String sql, JSONArray[] params) throws Exception {
		int totalUpdate = 0;
		try {
			totalUpdate = _executeBatchSql(connection, sql, params);
		} catch (Exception e) {
			LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
			throw e;
		}
		return totalUpdate;
	}

	public boolean executeNonSelectSql(Connection connection, String sql) throws Exception {
		return executeNonSelectSql(connection, sql, null);
	}

	public boolean executeNonSelectSql(Connection connection, String sql, JSONArray params) throws Exception {
		try {
			JSONArray reult = _executeSql(connection, sql, params);
			if (reult != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public JSONArray executeStoreprocedure(Connection connection, String sql, JSONArray params) throws Exception {
		JSONArray rtn = new JSONArray();
		try {
			rtn = _executeStoreprocedure(connection, sql, params);
		} catch (Exception e) {
			LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
			throw e;
		}
		return rtn;
	}

	private JSONArray _executeSql(Connection connection, String sql, JSONArray params) throws Exception {
		JSONArray rtn = new JSONArray();
		if (MysqlConnector.isEnable()) {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			try {
				sql = trimSql(sql);
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
				doCloseHandling(resultSet, stmt);
			}
		}
		return rtn;
	}

	private int _executeBatchSql(Connection connection, String sql, JSONArray[] params) throws Exception {
		int totalUpdate = 0;

		if (MysqlConnector.isEnable()) {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			try {
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
			} catch (Exception e) {
				LoggerUtil.logDebug("[DB Error] sql = " + sql + ", param = " + params + ", e = " + e);
				throw e;
			} finally {
				doCloseHandling(resultSet, stmt);
			}
		}
		return totalUpdate;
	}

	private JSONArray _executeStoreprocedure(Connection connection, String sql, JSONArray params) throws Exception {
		JSONArray rtn = new JSONArray();
		if (MysqlConnector.isEnable()) {
			CallableStatement stmt = null;
			ResultSet resultSet = null;
			try {
				sql = trimStoreSql(sql);
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
				doCloseHandling(resultSet, stmt);
			}
		}
		return rtn;
	}

	private String trimStoreSql(String query) {
		if (MysqlConnector.isMariaDB())
			return "{ " + trimSql(query) + "}";
		else
			return trimSql(query);
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

	private void doCloseHandling(ResultSet resultSet, PreparedStatement stmt) {
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
	}

	private String trimSql(String query) {
		return query.trim();
	}

}
