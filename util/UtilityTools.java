package util;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public class UtilityTools {

    public static int getRefreshRate() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();

        int refreshRate = dm.getRefreshRate();
        if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
            return 60;
        }
        return refreshRate;
    }

    public static BufferedImage scaleImage(
            BufferedImage original,
            int width,
            int height
    ) {
        BufferedImage scale = new BufferedImage(width, height, original.getType());
        Graphics2D g2d = scale.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);
        return scale;
    }
}