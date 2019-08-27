package util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Strings;

public class ImgHelper {

	private static final int IMG_PNG_TYPE = BufferedImage.TYPE_INT_ARGB;
	private static final int IMG_IMG_TYPE = BufferedImage.TYPE_INT_RGB;

	private static Set<String> IMG_SET = new HashSet<String>();
	private static String DEFAULT_IMG_TYPE = "JPG";
	private static String PNG_IMG_TYPE = "PNG";
	public static final int THUMB_MAX_SIZE = 400;

	static {
		IMG_SET.add(DEFAULT_IMG_TYPE);
		IMG_SET.add("JPEG");
		IMG_SET.add(PNG_IMG_TYPE);
		IMG_SET.add("BMP");
		IMG_SET.add("GIF");
	}

	public static boolean isImgType(String fileType) {
		boolean isImgType = false;
		String[] types = fileType.split("\\.");
		if (types.length > 1) {
			String type = types[types.length - 1];
			if (IMG_SET.contains(type.toUpperCase().trim())) {
				isImgType = true;
			}
		}

		return isImgType;
	}

	public BufferedImage cropImage(BufferedImage src, int x, int y, int resolutionW, int resolutionH) {
		// 照片的真正高跟寬
		int imageH = src.getHeight();
		int imageW = src.getWidth();

		int cropH = 0;
		int cropW = 0;

		int startX = 0;
		int startY = 0;

		if (y < 0) {
			if (Math.abs(y) >= imageH)
				return null;
			startY = Math.abs(y);
			cropH = imageH - startY;

		} else {
			// 不能大於resolutionH
			if (y >= resolutionH)
				return null;
			startY = 0;
			cropH = resolutionH - y;
		}

		if ((cropH + startY) > imageH) {
			cropH = imageH - startY;
		}

		if (cropH > resolutionH) {
			cropH = resolutionH;
		}

		if (x < 0) {
			if (Math.abs(x) >= imageW)
				return null;
			startX = Math.abs(x);
			cropW = imageW - startX;

		} else {
			// 不能大於resolutionH
			if (x >= resolutionW)
				return null;
			startX = 0;
			cropW = resolutionW - x;
		}

		if ((cropW + startX) > imageW) {
			cropW = imageW - startX;
		}
		if (cropW > resolutionW) {
			cropW = resolutionW;
		}

		BufferedImage dest = src.getSubimage(startX, startY, cropW, cropH);
		return dest;
	}

	public BufferedImage resizeImg(BufferedImage iptImg, int maxSize, String ext) throws JSONException {
		JSONObject sizeObj = calResize(iptImg.getWidth(), iptImg.getHeight(), maxSize);
		BufferedImage scaledBI = null;
		if (PNG_IMG_TYPE.equalsIgnoreCase(ext)) {
			scaledBI = new BufferedImage(sizeObj.getInt("W"), sizeObj.getInt("H"), IMG_PNG_TYPE);
		} else {
			scaledBI = new BufferedImage(sizeObj.getInt("W"), sizeObj.getInt("H"), IMG_IMG_TYPE);
		}

		Graphics2D g = scaledBI.createGraphics();
		g.drawImage(iptImg, 0, 0, sizeObj.getInt("W"), sizeObj.getInt("H"), null);
		g.dispose();
		if (PNG_IMG_TYPE.equalsIgnoreCase(ext)) {
			g.setComposite(AlphaComposite.Clear);
		} else {
			g.setComposite(AlphaComposite.Src);
		}
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return scaledBI;
	}

	public BufferedImage resizeImg(BufferedImage iptImg, BigDecimal ratio, String ext) throws JSONException {
		int width = iptImg.getWidth();
		int height = iptImg.getHeight();

		width = Integer.parseInt(new BigDecimal(width).multiply(ratio).setScale(0, RoundingMode.HALF_UP).toString());
		height = Integer.parseInt(new BigDecimal(height).multiply(ratio).setScale(0, RoundingMode.HALF_UP).toString());

		BufferedImage scaledBI = null;
		if (PNG_IMG_TYPE.equalsIgnoreCase(ext)) {
			scaledBI = new BufferedImage(width, height, IMG_PNG_TYPE);
		} else {
			scaledBI = new BufferedImage(width, height, IMG_IMG_TYPE);
		}

		Graphics2D g = scaledBI.createGraphics();
		g.drawImage(iptImg, 0, 0, width, height, null);
		g.dispose();
		if (PNG_IMG_TYPE.equalsIgnoreCase(ext)) {
			g.setComposite(AlphaComposite.Clear);
		} else {
			g.setComposite(AlphaComposite.Src);
		}
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		return scaledBI;
	}

	public BufferedImage resizeImg(BufferedImage iptImg, String ext) throws JSONException {
		return resizeImg(iptImg, THUMB_MAX_SIZE, ext);
	}

	public BufferedImage resizeImg(BufferedImage iptImg, int width, int height, String ext) throws JSONException {

		BufferedImage scaledBI = null;
		if (PNG_IMG_TYPE.equalsIgnoreCase(ext)) {
			scaledBI = new BufferedImage(width, height, IMG_PNG_TYPE);
		} else {
			scaledBI = new BufferedImage(width, height, IMG_IMG_TYPE);
		}

		Graphics2D g = scaledBI.createGraphics();
		g.drawImage(iptImg, 0, 0, width, height, null);
		g.dispose();
		if (PNG_IMG_TYPE.equalsIgnoreCase(ext)) {
			g.setComposite(AlphaComposite.Clear);
		} else {
			g.setComposite(AlphaComposite.Src);
		}
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		return scaledBI;
	}

	private JSONObject calResize(int imgWidth, int imgHeight, int maxSize) throws JSONException {
		JSONObject rtn = new JSONObject();
		if (imgWidth < imgHeight) {
			if (imgHeight > maxSize) {
				BigDecimal ratio = new BigDecimal(imgHeight).divide(new BigDecimal(maxSize), 2, RoundingMode.HALF_UP);
				rtn.put("H", String.valueOf(maxSize));
				rtn.put("W", new BigDecimal(imgWidth).divide(ratio, 0, RoundingMode.HALF_UP).toString());
				return rtn;
			}
		} else {
			if (imgWidth > maxSize) {
				BigDecimal ratio = new BigDecimal(imgWidth).divide(new BigDecimal(maxSize), 2, RoundingMode.HALF_UP);
				rtn.put("H", new BigDecimal(imgWidth).divide(ratio, 0, RoundingMode.HALF_UP).toString());
				rtn.put("W", String.valueOf(maxSize));
				return rtn;
			}
		}
		rtn.put("H", String.valueOf(imgHeight));
		rtn.put("W", String.valueOf(imgWidth));
		return rtn;
	}

	public BufferedImage getBufferedImage(String filePath) {
		BufferedImage image = null;
		File imageFile = FileHelper.newFile(filePath);
		try {
			image = ImageIO.read(imageFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return image;
	}

	public boolean bufferToFile(BufferedImage image, String outputPath, String ext) throws JSONException, IOException {
		File f = FileHelper.newFile(outputPath);
		boolean isInSet = false;
		synchronized (IMG_SET) {
			if (IMG_SET.contains(ext.toUpperCase())) {
				isInSet = true;
			}
		}
		if (!Strings.isNullOrEmpty(ext) && isInSet) {
			return ImageIO.write(image, ext.toUpperCase(), f);
		} else {
			return ImageIO.write(image, DEFAULT_IMG_TYPE, f);
		}
	}


}
