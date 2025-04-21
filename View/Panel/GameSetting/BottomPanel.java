package View.Panel.GameSetting;

import View.Frame.GameFrame;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_SPACING = 50;
    
    private JButton backButton;
    private JButton nextButton;

    public BottomPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, BUTTON_SPACING, 10));

        // Tạo các nút với kích thước cố định
        backButton = createStyledButton("BACK");
        nextButton = createStyledButton("NEXT");

        // Thêm padding để căn giữa theo chiều dọc
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(backButton);
        add(nextButton);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.CYAN);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };

        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }
        });

        return button;
    }

    public void setBackAction(Runnable action) {
        backButton.addActionListener(e -> {
            action.run();
            GameFrame.getInstance().requestFocus();
        });
    }

    public void setNextAction(Runnable action) {
        nextButton.addActionListener(e -> {
            action.run();
            GameFrame.getInstance().requestFocus();
        });
    }
} 