package obj;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import enums.ErrorCode;

public class ParamResult {

	private boolean m_bIsSuccess = false;
	private ErrorCode m_errCode = ErrorCode.OK;
	private JSONObject m_userObj = null;
	private List<String> m_lostParam = null;

	public ParamResult(boolean isSuccess, JSONObject userObj, ErrorCode errCode, List<String> lostList) {
		m_bIsSuccess = isSuccess;
		m_errCode = errCode;
		m_userObj = userObj;
		m_lostParam = lostList;
	}

	public ParamResult(boolean isSuccess, JSONObject userObj, ErrorCode errCode) {
		m_bIsSuccess = isSuccess;
		m_errCode = errCode;
		m_userObj = userObj;
	}

	public boolean isSuccess() {
		return m_bIsSuccess;
	}

	public ErrorCode getErrorCode() {
		return m_errCode;
	}

	public JSONObject getUserObj() {
		return m_userObj;
	}

	public String getParamList() {
		if (m_lostParam != null) {
			return m_lostParam.stream().collect(Collectors.joining(","));
		}
		return "";
	}

}
