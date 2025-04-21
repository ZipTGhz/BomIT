package View.Dialog;

import View.Frame.GameFrame;
import View.Panel.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EndGameDialog extends JDialog {
    private static final int DIALOG_WIDTH = 450;
    private static final int DIALOG_HEIGHT = 200;
    private static final int BUTTON_WIDTH = 175;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_SPACING = 30;

    public EndGameDialog(JFrame owner, boolean isWin) {
        super(owner, true); // modal dialog
        setUndecorated(true); // Bỏ viền cửa sổ mặc định
        setBackground(new Color(0, 0, 0, 0)); // Trong suốt
        initComponents(isWin); // Khởi tạo với thông báo thắng/thua
    }

    private void initComponents(boolean isWin) {
        // 1) Background bo góc
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 255)); // Xám bán trong suốt
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        content.setLayout(new BorderLayout(0, 20));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setOpaque(false);
        setContentPane(content);

        // 2) Tiêu đề “GAME OVER”
        JLabel titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        content.add(titleLabel, BorderLayout.NORTH);

        // 3) Thông báo thắng/thua
        JLabel messageLabel = new JLabel(isWin ? "Congratulations, you win!" : "You lose.", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        messageLabel.setForeground(Color.BLACK);
        content.add(messageLabel, BorderLayout.CENTER);

        // 4) Panel 2 nút
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_SPACING, 0));
        btnPanel.setOpaque(false);

        JButton btnMainMenu = createStyledButton("MAIN MENU");
        btnMainMenu.addActionListener(e -> {
            // dispose();
            // GameFrame.getInstance().showPanel("MENU_PANEL");

            dispose();
            GamePanel.setPausedGame(true);
            GameFrame.getInstance().resetApplication();
            GameFrame.getInstance().showPanel("MENU_PANEL");
        });

        JButton btnExit = createStyledButton("EXIT");
        btnExit.addActionListener(e -> {
            System.exit(0); // Đóng ứng dụng
        });

        btnPanel.add(btnMainMenu);
        btnPanel.add(btnExit);
        content.add(btnPanel, BorderLayout.SOUTH);

        // 5) Kích thước và canh giữa
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(getOwner());
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
        };

        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);

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

    /** Hiển thị dialog với hiệu ứng nền mờ */
    public void showDialog() {
        GamePanel.setPausedGame(true);

        JFrame frame = (JFrame) getOwner();

        // Tạo và hiển thị lớp nền mờ
        JPanel glass = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        glass.setOpaque(false);
        frame.setGlassPane(glass);
        glass.setVisible(true);

        // Hiển thị dialog
        setVisible(true);

        // Ẩn lớp nền mờ sau khi dialog đóng
        glass.setVisible(false);
    }
}
