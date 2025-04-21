package View.Dialog;

import View.Frame.GameFrame;
import View.Panel.GamePanel;

import javax.swing.*;
import java.awt.*;

public class PauseDialog extends JDialog {
    private static final int DIALOG_WIDTH    = 450;
    private static final int DIALOG_HEIGHT   = 200;
    private static final int BUTTON_WIDTH    = 175;
    private static final int BUTTON_HEIGHT   = 50;
    private static final int BUTTON_SPACING  = 30;

    public PauseDialog(JFrame owner) {
        super(owner, true);            // modal dialog
        setUndecorated(true);// bỏ viền cửa sổ mặc định
        setBackground(new Color(0, 0, 0, 0));
        initComponents();
    }

    private void initComponents() {
        // 1) Background bo góc
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 255)); // xám bán trong suốt
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        content.setLayout(new BorderLayout(0, 20));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setOpaque(false);
        setContentPane(content);

        // 2) Tiêu đề “PAUSE”
        JLabel lbl = new JLabel("PAUSE", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        lbl.setForeground(Color.BLACK);
        content.add(lbl, BorderLayout.NORTH);

        // 3) Panel 2 nút
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_SPACING, 0));
        btnPanel.setOpaque(false);

        JButton btnMainMenu = createStyledButton("MAIN MENU");
        btnMainMenu.addActionListener(e -> {
            dispose();
            GamePanel.setPausedGame(true);
            GameFrame.getInstance().resetApplication();
            GameFrame.getInstance().showPanel("MENU_PANEL");
        });

        JButton btnResume = createStyledButton("RESUME");
        btnResume.addActionListener(e -> {
            dispose();
            GamePanel.setPausedGame(false);
            SwingUtilities.invokeLater(() -> getOwner().requestFocusInWindow());
        });

        btnPanel.add(btnMainMenu);
        btnPanel.add(btnResume);
        content.add(btnPanel, BorderLayout.CENTER);

        // 4) Kích thước và canh giữa
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

            @Override
            protected void paintBorder(Graphics g) {
//                Graphics2D g2 = (Graphics2D) g.create();
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2.setColor(Color.CYAN);
//                g2.setStroke(new BasicStroke(2));
//                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
//                g2.dispose();
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
