package global;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class DBParam implements IDBParam {

	private boolean isMariaDB;
	private boolean enble;
	private String user;
	private String pass;
	private int maxActive;
	private int maxWait;
	private int maxIdle;
	private String ip;
	private int port;
	private String schemaName;
	private String connectionProperty;

	private int minEvictableIdle;
	private int timeBetweenEviction;

	public DBParam(String configPath) {
		Config cfg = ConfigFactory.load();
		cfg = cfg.getConfig(configPath);
		enble = cfg.getBoolean("ENABLE");
		ip = cfg.getString("IP");
		port = cfg.getInt("PORT");
		isMariaDB = cfg.getBoolean("IS_MARIADB");

		schemaName = cfg.getString("SCHEMA");
		user = cfg.getString("USER");
		pass = cfg.getString("PASS");
		maxActive = cfg.getInt("MAX_ACTIVE");
		maxWait = cfg.getInt("MAX_WAIT");
		maxIdle = cfg.getInt("MAX_IDLE");
		
		
		minEvictableIdle = cfg.getInt("MIN_EVICTABLE_IDLE");
		timeBetweenEviction = cfg.getInt("TIME_BETWEEN_EVICTION");

		connectionProperty = cfg.getString("CONNECTION_PROPERTY");
	}

	public boolean isMariaDB() {
		return isMariaDB;
	}

	public boolean isEnble() {
		return enble;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public String getConnectionProperty() {
		return connectionProperty;
	}

	public int getMinEvictableIdle() {
		return minEvictableIdle;
	}

	public void setMinEvictableIdle(int minEvictableIdle) {
		this.minEvictableIdle = minEvictableIdle;
	}

	public int getTimeBetweenEviction() {
		return timeBetweenEviction;
	}

	public void setTimeBetweenEviction(int timeBetweenEviction) {
		this.timeBetweenEviction = timeBetweenEviction;
	}
}
