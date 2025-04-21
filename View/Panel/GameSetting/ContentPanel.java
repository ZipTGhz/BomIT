package View.Panel.GameSetting;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {
    private Image backgroundImage;
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 180); // Màu đen mờ 70%
    private static final int CONTENT_WIDTH = 700;  // Chiều rộng của content
    private static final int CONTENT_HEIGHT = 350; // Chiều cao của content

    public ContentPanel() {
        setLayout(new GridBagLayout()); // Sử dụng GridBagLayout để căn giữa
        setOpaque(false);

        // Panel chính chứa nội dung
        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setOpaque(false);
        mainContent.setPreferredSize(new Dimension(CONTENT_WIDTH, CONTENT_HEIGHT));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource("/Resources/MenuPanel/background.jpeg")).getImage();

        // Thêm mainContent vào center
        add(mainContent);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Vẽ background image
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Vẽ overlay mờ cho toàn bộ panel
        g2d.setColor(OVERLAY_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Tạo vùng trong suốt cho content
        int x = (getWidth() - CONTENT_WIDTH) / 2;
        int y = (getHeight() - CONTENT_HEIGHT) / 2;
        
        // Vẽ background cho content area
        g2d.setColor(new Color(0, 0, 0, 100)); // Màu đen mờ nhẹ cho content
        g2d.fillRoundRect(x, y, CONTENT_WIDTH, CONTENT_HEIGHT, 20, 20);
        
        // Vẽ border cho content area
        g2d.setColor(new Color(255, 255, 255, 50)); // Viền trắng mờ
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, CONTENT_WIDTH, CONTENT_HEIGHT, 20, 20);

        g2d.dispose();
    }

    // Phương thức để thêm components vào mainContent
    public void addToContent(Component comp, String constraints) {
        // Lấy mainContent panel (component đầu tiên được add)
        JPanel mainContent = (JPanel) getComponent(0);
        mainContent.add(comp, constraints);
    }
} 