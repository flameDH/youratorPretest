package enums;

public enum ErrorCode {

	OK(0, ""), SYS_ERR(400, "系統發生錯誤"), PARAM_ERR(401, "參數錯誤，請檢查是否有未填寫資料"), TOKEN_EXPIRE(403, "請重新登入")
	, DATA_NOTFOUND(404, "查無資料"),  USER_EXIST(404, "使用者已經存在"), USER_NO_EXIST(405, "使用者不存在")
	, PASS_ERROR(406, "密碼錯誤"), PERMISSION_DENIED(407, "權限不足")
	, FORGET_PASSWD_EXPIRE(408, "忘記密碼連結失效")
	, USER_OR_PASS_ERROR(409, "帳號或密碼錯誤")
	, NEED_RELOGIN(410, "你的資料已經更新，請重登")	

	;

	private int m_id;
	private String m_msg;

	ErrorCode(int id, String msg) {
		m_id = id;
		m_msg = msg;
	}

	public String getMsg() {
		return m_msg;
	}

	public int getID() {
		return m_id;
	}
}
