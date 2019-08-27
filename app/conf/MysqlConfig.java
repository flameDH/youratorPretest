package conf;

public class MysqlConfig {

	private static final String MARIADB_URL = "jdbc:mariadb";
	private static final String MYSQL_URL = "jdbc:mysql";

	private static final String MARIADB_DRIVER = "org.mariadb.jdbc.Driver";
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	boolean m_bIsMariaDB;

	boolean m_bEnble;
	String m_sUser;
	String m_sPass;
	int m_nMaxActive;
	int m_nMaxWait;
	int m_nMaxIdle;

	String m_sIP;

	int m_nPort;
	String m_sSchemaName;

	String m_sUrl = "";
	String m_sConnectionProperty = "";

	int m_nMinEvictableIdle;
	int m_nTimeBetweenEviction;

	// ------------------------------------------------------------

	public MysqlConfig(boolean enble, String ip, int port, String schemaName, String sUser, String sPass,
			int nMaxActive, int nMaxWait, int nMaxIdle, boolean isMariaDB, String connectionProperty,
			int minEvictableIdle, int timeBetweenEviction) {
		m_bEnble = enble;
		m_sUser = sUser;
		m_sPass = sPass;
		m_nMaxActive = nMaxActive;
		m_nMaxWait = nMaxWait;
		m_nMaxIdle = nMaxIdle;
		m_bIsMariaDB = isMariaDB;

		m_sIP = ip;
		m_nPort = port;
		m_sSchemaName = schemaName;
		if (m_bIsMariaDB) {
			m_sUrl = MARIADB_URL + "://" + ip + ":" + port + "/" + schemaName;
		} else {
			m_sUrl = MYSQL_URL + "://" + ip + ":" + port + "/" + schemaName;
		}

		m_nMinEvictableIdle = minEvictableIdle;
		m_nTimeBetweenEviction = timeBetweenEviction;

		m_sConnectionProperty = connectionProperty;

	}

	public String getUrl() {
		return m_sUrl;
	}

	public String getDriverClass() {
		if (m_bIsMariaDB) {
			return MARIADB_DRIVER;
		} else {
			return MYSQL_DRIVER;
		}
	}

	public boolean isMariaDB() {
		return m_bIsMariaDB;
	}

	public boolean isEnble() {
		return m_bEnble;
	}

	public void setEnble(boolean enble) {
		this.m_bEnble = enble;
	}

	public String getUser() {
		return m_sUser;
	}

	public void setUser(String user) {
		this.m_sUser = user;
	}

	public String getPass() {
		return m_sPass;
	}

	public void setPass(String pass) {
		this.m_sPass = pass;
	}

	public int getMaxActive() {
		return m_nMaxActive;
	}

	public void setMaxActive(int maxActive) {
		this.m_nMaxActive = maxActive;
	}

	public int getMaxWait() {
		return m_nMaxWait;
	}

	public void setMaxWait(int maxWait) {
		this.m_nMaxWait = maxWait;
	}

	public int getMaxIdle() {
		return m_nMaxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.m_nMaxIdle = maxIdle;
	}

	public String getConnectionProperty() {
		return m_sConnectionProperty;
	}

	public void setConnectionProperty(String connectionProperty) {
		this.m_sConnectionProperty = connectionProperty;
	}

	public int getMinEvictableIdle() {
		return m_nMinEvictableIdle;
	}

	public void setMinEvictableIdle(int minEvictableIdle) {
		this.m_nMinEvictableIdle = minEvictableIdle;
	}

	public int getTimeBetweenEviction() {
		return m_nTimeBetweenEviction;
	}

	public void setTimeBetweenEviction(int timeBetweenEviction) {
		this.m_nTimeBetweenEviction = timeBetweenEviction;
	}

}
