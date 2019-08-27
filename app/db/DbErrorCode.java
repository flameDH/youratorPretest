package db;

public enum DbErrorCode {

	OK(0, "", ""), DUPLICATE(30000, "資料重複", "30000"), SYS_ERROR(40000, "SQL錯誤", "40000");

	private int m_id;
	private String m_msg;
	private String m_dbErrorCode;

	DbErrorCode(int id, String msg, String dbError) {
		m_id = id;
		m_msg = msg;
		m_dbErrorCode = dbError;

	}

	public String getMsg() {
		return m_msg;
	}

	public int getID() {
		return m_id;
	}

	public String getDbErrorCode() {
		return m_dbErrorCode;
	}
}
