package util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.SystemUtils;

import com.google.common.base.Strings;

import play.Play;
import play.mvc.Http.MultipartFormData.FilePart;

public class FileHelper {
	private static boolean IS_WINDOWS = SystemUtils.IS_OS_WINDOWS;
	private static String WINDOWS_SPLIT = ":";
	public static final String DIR_SEPERATE = File.separator;
//	public static String PROJECT_PATH = Play.application().path().getPath() + DIR_SEPERATE;

	public static List<String> readFile(String fileName) throws IOException {
		fileName = genProdFilePath(fileName);
		List<String> records = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				records.add(line);
			}
			return records;
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public static void writeFile(String filePath, String data) throws IOException {
		File file = newFile(filePath);
		Writer out = null;

		try {

			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			out.write(data);
			out.flush();
		} finally {
			if (out != null)
				out.close();
		}
	}

	public static void deleteFile(String filePath) throws IOException {
		File file = newFile(filePath);
		if (file.exists() && !file.isDirectory()) {
			file.delete();
		}
	}

	// public static void deleteFile(String filePath) throws IOException {
	// File file = newFile(filePath);
	// if (file.exists()) {
	// if (file.isDirectory()) {
	// if (file.list().length == 0) {
	// file.delete();
	// } else {
	// String files[] = file.list();
	// for (String temp : files) {
	// deleteFile(temp);
	// }
	// if (file.list().length == 0) {
	// file.delete();
	// }
	// }
	// } else {
	// file.delete();
	// }
	// }
	// }

	public static boolean moveFile(String filePath, String targetPath) throws IOException {
		try {
			File file = newFile(filePath);
			File targetFile = newFile(targetPath);
			if (file.exists()) {
				createFileFolder(targetFile);
				if (file.renameTo(targetFile)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			LoggerUtil.logException(e);
			return false;
		}
	}

	public static boolean copyFile(String filePath, String targetPath) throws IOException {
		try {
			File file = newFile(filePath);
			File targetFile = newFile(targetPath);
			if (file.exists()) {
				createFileFolder(targetFile);
				Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isFileExist(String filePath) throws IOException {
		try {
			File file = newFile(filePath);
			return file.exists();
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isFileDir(String filePath) throws IOException {
		try {
			File file = newFile(filePath);
			return file.isDirectory();
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean createFolder(String filePath) throws IOException {
		try {
			File file = newFile(filePath);
			if (file.exists())
				return false;
			file.mkdirs();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getExt(String filePath) throws IOException {
		try {
			String extension = "";
			if (!Strings.isNullOrEmpty(filePath)) {
				int i = filePath.lastIndexOf('.');
				if (i > 0) {
					extension = filePath.substring(i + 1);
				}
			}
			return extension;
		} catch (Exception e) {
			return null;
		}
	}

	public static String genNewFileName(String fileName, String injectStr) throws IOException {
		try {
			String extension = "";
			if (!Strings.isNullOrEmpty(fileName)) {

				int i = fileName.lastIndexOf('.');
				if (i > 0) {
					extension = fileName.substring(i + 1);
				}

			}
			return extension;
		} catch (Exception e) {
			return null;
		}
	}

	public static BufferedImage getImageSize(String filePath) throws IOException {
		return ImageIO.read(newFile(filePath));

	}

	public static String getFileMD5(String filePath) throws IOException, NoSuchAlgorithmException {

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(newFile(filePath));
			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	public static long getFileSize(String filePath) {
		File file = newFile(filePath);
		return file.length();
	}

	public static String[] getFileList(String filePath) {
		File file = newFile(filePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				return file.list();
			}
		}
		return new String[0];
	}

	public static File newFile(String filePath) {
		filePath = genProdFilePath(filePath);
		return new File(filePath);
	}

	public static String genProdFilePath(String filePath) {
		if (IS_WINDOWS) {
			if (Play.isProd() && !Strings.isNullOrEmpty(filePath) && !isWindowsHeadDir(filePath)) {
//				filePath = PROJECT_PATH + filePath;
			}
			return filePath;
		} else {
			if (Play.isProd() && !Strings.isNullOrEmpty(filePath) && !filePath.startsWith(DIR_SEPERATE)) {
//				filePath = PROJECT_PATH + filePath;
			}
			return filePath;
		}
	}

	private static boolean isWindowsHeadDir(String filePath) {

		if (filePath.length() < 2)
			return false;
		String secondStr = String.valueOf(filePath.charAt(1));
		if (WINDOWS_SPLIT.equals(secondStr))
			return true;
		return false;
	}

	public static void deleteUploadFiles(List<FilePart<File>> files) {
		if (files != null && files.size() > 0) {
			for (FilePart<File> filePart : files) {
				File file = filePart.getFile();
				if (file != null) {
					try {
						FileHelper.deleteFile(file.getAbsolutePath());
					} catch (IOException e) {
						LoggerUtil.logException(e);
					}
				}
			}
		}
	}

	private static void createFileFolder(File file) {
		File parant = file.getParentFile();
		if (!parant.exists())
			parant.mkdirs();
	}
}
