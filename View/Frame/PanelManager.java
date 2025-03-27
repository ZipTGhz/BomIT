package View.Frame;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class PanelManager {
    private Map<String, JPanel> mp = new HashMap<>();

    public PanelManager() {}

    public void addPanel(String key, JPanel panel) {
        mp.put(key, panel);
    }

    public JPanel getPanel(String key) {
        return mp.getOrDefault(key, null);
    }
}
