package Util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

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
}
