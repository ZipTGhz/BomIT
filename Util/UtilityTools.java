package Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UtilityTools {
	public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
		// Tạo một ảnh rỗng với kích thước người dùng mong muốn
		BufferedImage scaled = new BufferedImage(width, height, original.getType());
		// Lấy đồ hoạ của ảnh để vẽ lên ảnh rỗng đó
		Graphics2D g2d = scaled.createGraphics();
		// Vẽ ảnh gốc lên scaled với kích thước mong muốn
		g2d.drawImage(original, 0, 0, width, height, null);
		// Giải phóng tài nguyên sau khi xong, tránh rò rỉ bộ nhớ
		g2d.dispose();
		return scaled;
	}

	public static BufferedImage[] scaleImage(BufferedImage[] images, int width, int height) {
		BufferedImage[] res = new BufferedImage[images.length];

		for (int i = 0; i < images.length; ++i) {
			BufferedImage original = images[i];
			// Tạo một ảnh rỗng với kích thước người dùng mong muốn
			BufferedImage scaled = new BufferedImage(width, height, original.getType());
			// Lấy đồ hoạ của ảnh để vẽ lên ảnh rỗng đó
			Graphics2D g2d = scaled.createGraphics();
			// Vẽ ảnh gốc lên scaled với kích thước mong muốn
			g2d.drawImage(original, 0, 0, width, height, null);
			// Giải phóng tài nguyên sau khi xong, tránh rò rỉ bộ nhớ
			g2d.dispose();
			res[i] = scaled;
		}

		return res;
	}

	public static BufferedImage[] splitImage(BufferedImage original, int size) {
		int width = original.getWidth(), height = original.getHeight();

		BufferedImage[] res = new BufferedImage[width / size * height / size];
		int index = 0;

		for (int y = 0; y < height; y += size) {
			for (int x = 0; x < width; x += size) {
				BufferedImage subImage = original.getSubimage(x, y, size, size);
				res[index++] = subImage;
			}
		}

		return res;
	}

	public static <T> T[] mergeArrays(T[] arr1, T[] arr2) {
		T[] merged = Arrays.copyOf(arr1, arr1.length + arr2.length);
		System.arraycopy(arr2, 0, merged, arr1.length, arr2.length);
		return merged;
	}

	public static Color getAverageColor(BufferedImage image) {
		long sumRed = 0, sumGreen = 0, sumBlue = 0;
		int width = image.getWidth();
		int height = image.getHeight();
		int totalPixels = width * height;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = new Color(image.getRGB(x, y));
				sumRed += color.getRed();
				sumGreen += color.getGreen();
				sumBlue += color.getBlue();
			}
		}

		int avgRed = (int) (sumRed / totalPixels);
		int avgGreen = (int) (sumGreen / totalPixels);
		int avgBlue = (int) (sumBlue / totalPixels);
		return new Color(avgRed, avgGreen, avgBlue);
	}

	public static File[] getFiles(String path) {
		try {
			URL url = UtilityTools.class.getResource(path);
			File folder = new File(url.toURI());
			File[] files = folder.listFiles();
			return files;
		} catch (Exception e) {
			System.out.println("Xảy ra lỗi khi đọc thư mục:");
			e.printStackTrace();
			return new File[0];
		}
	}

	public static File[] getFiles(String path, String... filters) {
		try {
			URL url = UtilityTools.class.getResource(path);
			File folder = new File(url.toURI());

			Set<String> filterSet = new HashSet<>(Arrays.asList(filters)); // Dùng Set để tìm nhanh hơn
			return folder.listFiles((FilenameFilter) (_, name) -> {
				int i = name.lastIndexOf('.');
				if (i == -1 || i == name.length() - 1)
					return false;
				String extension = name.substring(i);
				return filterSet.contains(extension);
			});

		} catch (Exception e) {
			System.out.println("Xảy ra lỗi khi đọc thư mục:");
			e.printStackTrace();
			return new File[0];
		}
	}
}
