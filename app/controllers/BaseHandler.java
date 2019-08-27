package controllers;

import enums.ErrorCode;

public class BaseHandler {

	public ErrorCode checkGetListParam(int start, int size) {
		return checkGetListParam(start, size, "");
	}

	public ErrorCode checkGetListParam(int start, int size, String key) {
		if (start < 0 || size <= 0 || key == null) {
			return ErrorCode.PARAM_ERR;
		}
		return ErrorCode.OK;
	}
}
