package View.Frame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class PanelManager {
    private static PanelManager instance;
    private Map<String, JPanel> mp = new HashMap<>();
    private GameFrame gameFrame;

    public PanelManager() {
    }

    public static PanelManager getInstance() {
        if (instance == null) {
            instance = new PanelManager();
        }
        return instance;
    }

    public void setGameFrame(GameFrame frame) {
        this.gameFrame = frame;
    }

    public void addPanel(String key, JPanel panel) {
        mp.put(key, panel);
    }

    public JPanel getPanel(String key) {
        return mp.getOrDefault(key, null);
    }
}
