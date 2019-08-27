package util;

import play.Logger;

/**
 * Created by wchiou on 2016/10/5.
 */
public class LoggerUtil {

	static Logger.ALogger m_apiLogger = Logger.of("api");
	static Logger.ALogger m_dbLogger = Logger.of("db");

	public static void logException(Exception ex) {

		m_apiLogger.error(String.format("[%s] exception :", Thread.currentThread().getStackTrace()[2].getClassName()),
				ex);
	}

	public static void logDebug(String msg) {

		m_apiLogger.debug(String.format("[%s] :", Thread.currentThread().getStackTrace()[2].getClassName()) + msg);
	}

	public static void logDbDebug(String msg) {

		m_dbLogger.debug(String.format("[%s] :", Thread.currentThread().getStackTrace()[2].getClassName()) + msg);
	}

}
