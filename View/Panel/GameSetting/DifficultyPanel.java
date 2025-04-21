package View.Panel.GameSetting;

import javax.swing.*;
import java.awt.*;

public class DifficultyPanel extends JPanel {
    private ButtonGroup group;
    private static final int SPACING = 20;

    public DifficultyPanel() {
        setLayout(new BorderLayout(10, 10));
        setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("DIFFICULT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setPreferredSize(new Dimension(150, 30));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.WEST);

        // Options
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACING, 5));
        optionsPanel.setOpaque(false);
        group = new ButtonGroup();

        String[] options = {"EASY", "NORMAL"};
        for (int i = 0; i < options.length; i++) {
            StyledRadioButton radio = new StyledRadioButton(options[i]);
            radio.setActionCommand(String.valueOf(i)); // Gán giá trị tương ứng (0, 1, 2,...)
            group.add(radio);
            optionsPanel.add(radio);
        }

        add(optionsPanel, BorderLayout.CENTER);
    }

    public int getSelectedDifficulty() {
        ButtonModel selectedModel = group.getSelection();
        if (selectedModel != null) {
            return Integer.parseInt(selectedModel.getActionCommand());
        }
        return 0; // Mặc định nếu chưa chọn gì
    }

    public void setSelectedDifficulty(int index) {
        for (Component c : ((JPanel)getComponent(1)).getComponents()) {
            if (c instanceof JRadioButton) {
                JRadioButton btn = (JRadioButton)c;
                if (btn.getActionCommand().equals(String.valueOf(index))) {
                    btn.setSelected(true);
                    break;
                }
            }
        }
    }
} 