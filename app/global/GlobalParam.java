package global;

public class GlobalParam {

	public static String UPLOAD_DIR = "upload";
	public static String IMG_DIR = "img";
	public static String SNAP_DIR = "snap";

	private static DBParam dbParam = null;
	private static SystemParam sysParam = null;

	static {
		dbParam = new DBParam("CUSTOM.DB");
		sysParam = new SystemParam("CUSTOM.SERVER");

	}

	public static DBParam getDBParam() {
		return dbParam;
	}

	public static SystemParam getSysParam() {
		return sysParam;
	}

}
