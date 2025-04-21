package View.Panel;

import Interfaces.IGS;
import Model.GameSettings;
import Model.SoundManager;
import Model.SoundType;
import Util.UtilityTools;
import View.Frame.GameFrame;
import View.Panel.GameSetting.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameSettingPanel extends JPanel {

    private int btnWidth = 200, btnHeight = 55;
    // Hình ảnh
    private int bgX, bgY;
    private BufferedImage backgroundImage;

    private GameSettings settings;
    private ContentPanel contentPanel;
    private TitlePanel titlePanel;
    private EnemiesPanel enemiesPanel;
    private DifficultyPanel difficultyPanel;
    private BottomPanel bottomPanel;

    public GameSettingPanel() {
        settings = GameSettings.getInstance();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(IGS.GAME_WIDTH, IGS.GAME_HEIGHT));

        // Create components
        contentPanel = new ContentPanel();
        titlePanel = new TitlePanel();
        enemiesPanel = new EnemiesPanel();
        difficultyPanel = new DifficultyPanel();
        bottomPanel = new BottomPanel();

        // Set up main panel for settings
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setOpaque(false);

        // Add components to settings panel
        settingsPanel.add(enemiesPanel);
        settingsPanel.add(Box.createVerticalStrut(20));
        settingsPanel.add(difficultyPanel);

        // Add components to content panel
        contentPanel.addToContent(titlePanel, BorderLayout.NORTH);
        contentPanel.addToContent(settingsPanel, BorderLayout.CENTER);
        contentPanel.addToContent(bottomPanel, BorderLayout.SOUTH);

        // Add content panel to main panel
        add(contentPanel, BorderLayout.CENTER);

        bottomPanel.setBackAction(() -> {
            GameFrame.getInstance().showPanel("MENU_PANEL");
        });
        bottomPanel.setNextAction(() -> {
            saveSettings();
            // GameFrame.getInstance().playGame();
            GameFrame.getInstance().showPanel("GAME_PANEL");
        });

        loadCurrentSettings();
    }

    private void loadCurrentSettings() {
        enemiesPanel.setSelectedEnemies(settings.getNumEnemies());
        difficultyPanel.setSelectedDifficulty(settings.getDifficulty());
    }

    private void saveSettings() {
        settings.setNumEnemies(enemiesPanel.getSelectedEnemies());
        settings.setDifficulty(difficultyPanel.getSelectedDifficulty());
    }

    private void init() {
        try {
            File[] files = UtilityTools.getFiles("/Resources/MenuPanel", ".png", ".jpg", ".jpeg");
            BufferedImage original = ImageIO.read(files[0]);
            backgroundImage = UtilityTools.scaleImage(original, original.getWidth() + 30, original.getHeight());
            bgX = (IGS.GAME_WIDTH - backgroundImage.getWidth()) / 1;
            bgY = (IGS.GAME_HEIGHT - backgroundImage.getHeight()) / 1;
            SoundManager.getInstance().playMusic(SoundType.BACKGROUND_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, bgX, bgY, null);
        }
    }
}