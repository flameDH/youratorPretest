package db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

import conf.MysqlConfig;
import global.DBParam;
import global.GlobalParam;

public class MysqlConnector {
	private final static BasicDataSource dataSource;
	private final static boolean m_bIsMariaDB;
	private final static boolean m_bIsEnable;

	static {

		DBParam param = GlobalParam.getDBParam();
		dataSource = new BasicDataSource();

		MysqlConfig conf = new MysqlConfig(param.isEnble(), param.getIp(), param.getPort(), param.getSchemaName(),
				param.getUser(), param.getPass(), param.getMaxActive(), param.getMaxWait(), param.getMaxIdle(),
				param.isMariaDB(), param.getConnectionProperty(), param.getMinEvictableIdle(),
				param.getTimeBetweenEviction());
		m_bIsMariaDB = conf.isMariaDB();
		m_bIsEnable = conf.isEnble();
		if (m_bIsEnable) {
			dataSource.setDriverClassName(conf.getDriverClass());
			dataSource.setUrl(conf.getUrl());
			dataSource.setUsername(conf.getUser());
			dataSource.setPassword(conf.getPass());
			dataSource.setMaxActive(conf.getMaxActive());
			dataSource.setMaxWait(conf.getMaxWait());
			dataSource.setMaxIdle(conf.getMaxIdle());
			dataSource.setConnectionProperties(conf.getConnectionProperty());
			dataSource.setMinEvictableIdleTimeMillis(conf.getMinEvictableIdle());
			dataSource.setTimeBetweenEvictionRunsMillis(conf.getTimeBetweenEviction());
		}
	}

	private MysqlConnector() {
	}

	public static boolean isMariaDB() {
		return m_bIsMariaDB;
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public static boolean isEnable() {
		return m_bIsEnable;
	}

}
