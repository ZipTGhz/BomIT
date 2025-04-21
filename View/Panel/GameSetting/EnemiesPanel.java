package View.Panel.GameSetting;

import javax.swing.*;
import java.awt.*;

public class EnemiesPanel extends JPanel {
    private ButtonGroup group;
    private static final int SPACING = 20;

    public EnemiesPanel() {
        setLayout(new BorderLayout(10, 10));
        setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("ENEMIES  ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(150, 30));
        add(titleLabel, BorderLayout.WEST);

        // Options
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, SPACING, 5));
        optionsPanel.setOpaque(false);
        group = new ButtonGroup();

        String[] options = {"1", "2", "3"};
        for (String option : options) {
            StyledRadioButton radio = new StyledRadioButton(option);
            group.add(radio);
            optionsPanel.add(radio);
        }

        add(optionsPanel, BorderLayout.CENTER);
    }

    public int getSelectedEnemies() {
        for (Component c : ((JPanel)getComponent(1)).getComponents()) {
            if (c instanceof JRadioButton && ((JRadioButton)c).isSelected()) {
                return Integer.parseInt(((JRadioButton)c).getText());
            }
        }
        return 1; // Default value
    }

    public void setSelectedEnemies(int enemies) {
        for (Component c : ((JPanel)getComponent(1)).getComponents()) {
            if (c instanceof JRadioButton && ((JRadioButton)c).getText().equals(String.valueOf(enemies))) {
                ((JRadioButton)c).setSelected(true);
                break;
            }
        }
    }
} 