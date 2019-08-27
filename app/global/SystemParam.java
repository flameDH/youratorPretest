package global;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SystemParam implements IDBParam {

	private String ip = "";
	private int port = 80;
	private boolean isHttps = false;
	private String urlHeaders = "";

	public SystemParam(String configPath) {
		Config cfg = ConfigFactory.load();
		cfg = cfg.getConfig(configPath);
		ip = cfg.getString("IP");
		port = cfg.getInt("PORT");
		isHttps = cfg.getBoolean("IS_HTTPS");

		if (isHttps) {
			urlHeaders = "https://" + ip;
			if (port != 443) {
				urlHeaders = urlHeaders + ":" + port + "/";
			} else {
				urlHeaders = urlHeaders + "/";
			}
		} else {
			urlHeaders = urlHeaders + "http://" + ip;
			if (port != 80) {
				urlHeaders = urlHeaders + ":" + port + "/";
			} else {
				urlHeaders = urlHeaders + "/";
			}
		}
	}

	public String getUrlHeaders() {
		return urlHeaders;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public boolean isHttps() {
		return isHttps;
	}

}
