package View.Panel.GameSetting;

import javax.swing.*;
import java.awt.*;

public class TitlePanel extends JPanel {
    public TitlePanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String text = "GAME SETTINGS";
        Font font = new Font("Arial", Font.BOLD, 36);
        g2.setFont(font);

        // Tính toán để căn giữa
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        // Vẽ viền (outline)
        g2.setColor(Color.BLACK); // viền đen
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    g2.drawString(text, x + dx, y + dy);
                }
            }
        }

        // Vẽ chữ chính
        g2.setColor(Color.CYAN); // màu chính
        g2.drawString(text, x, y);

        g2.dispose();
    }
}
