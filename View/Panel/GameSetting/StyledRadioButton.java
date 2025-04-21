package View.Panel.GameSetting;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class StyledRadioButton extends JRadioButton {
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 40;
    private Color defaultColor = new Color(128, 128, 128, 180); // Màu xám mờ
    private Color selectedColor = new Color(0, 102, 204);    // Màu xanh

    public StyledRadioButton(String text) {
        super(text);
        setup();
    }

    private void setup() {
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 24));
        setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        // Custom painting
        setOpaque(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ background
        if (isSelected()) {
            g2.setColor(selectedColor);
        } else {
            g2.setColor(defaultColor);
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Vẽ border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);

        // Vẽ text
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(getText(), g2);
        int x = (getWidth() - (int) r.getWidth()) / 2;
        int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();

        g2.drawString(getText(), x, y);

        g2.dispose();
    }
} 