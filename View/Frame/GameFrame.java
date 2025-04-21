package View.Frame;

import javax.swing.*;

import Controller.Input;
import Model.GameManager;
import Model.GameSettings;
import Model.SpriteFactory;
import View.Panel.GamePanel;
import View.Panel.GameSettingPanel;
import View.Panel.MenuPanel;

import java.awt.*;

public class GameFrame extends JFrame {

	private static GameFrame instance = new GameFrame();

	public static GameFrame getInstance() {
		return instance;
	}

	private PanelManager panelManager;

	private final String MENU_PANEL = "MENU_PANEL";
	private final String GAME_SETTING_PANEL = "GAME_SETTING_PANEL";
	private final String GAME_PANEL = "GAME_PANEL";

	private GameFrame() {
		initComponent();
		initUI();
		addKeyListener(new Input());
	}

	public void start() {
		showMainMenu();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void showMainMenu() {
		MenuPanel mp = (MenuPanel) panelManager.getPanel(MENU_PANEL);
		this.getContentPane().removeAll();
		this.add(mp);
		mp.requestFocusInWindow();
	}

	public void showGameMenuSetting() {
		GameSettingPanel gsp = (GameSettingPanel) panelManager.getPanel(GAME_SETTING_PANEL);
		this.getContentPane().removeAll();
		this.add(gsp);
		gsp.requestFocusInWindow();
	}

	public void playGame() {
		// if (getKeyListeners().length == 0) {
		// addKeyListener(new Input());
		// }
		GamePanel gp = (GamePanel) panelManager.getPanel(GAME_PANEL);
		this.getContentPane().removeAll();
		this.add(gp);
		int botNum = GameSettings.getInstance().getNumEnemies();
		int bofDiff = GameSettings.getInstance().getDifficulty();
		gp.start(botNum, bofDiff);
		gp.requestFocusInWindow();
		setVisible(true);
	}

	private void initUI() {
		setTitle("Boom IT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}

	public void showPanel(String panelKey) {
		// cach 1
		// if(panelKey.equals(MENU_PANEL)) {
		// showMainMenu();
		// }
		// else if(panelKey.equals(GAME_SETTING_PANEL)) {
		// showGameMenuSetting();
		// }
		// else if(panelKey.equals(GAME_PANEL)){
		// playGame();
		// }
		// getContentPane().revalidate();
		// getContentPane().repaint();
		JPanel p;

		switch (panelKey) {
		case MENU_PANEL:
			p = panelManager.getPanel(MENU_PANEL);
			break;
		case GAME_SETTING_PANEL:
			p = panelManager.getPanel(GAME_SETTING_PANEL);
			break;
		case GAME_PANEL:
			p = panelManager.getPanel(GAME_PANEL);
			break;
		default:
			return;
		}

		getContentPane().removeAll();
		getContentPane().add(p);

		getContentPane().revalidate();
		getContentPane().repaint();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		requestFocusInWindow();

		if (panelKey.equals(GAME_PANEL)) {
			int botNum = GameSettings.getInstance().getNumEnemies();
			int bofDiff = GameSettings.getInstance().getDifficulty();
			((GamePanel) p).start(botNum, bofDiff);
		}
	}

	private void initComponent() {
		panelManager = new PanelManager();
		panelManager.addPanel(MENU_PANEL, new MenuPanel());
		panelManager.addPanel(GAME_SETTING_PANEL, new GameSettingPanel());
		panelManager.addPanel(GAME_PANEL, new GamePanel());
	}

	public void resetApplication() {
		// 1. Reset toàn bộ trạng thái logic
		GameManager.getInstance().resetGameManager();
		GameSettings.getInstance().resetGameSetting();
		GamePanel gp = (GamePanel) panelManager.getPanel(GAME_PANEL);
		SpriteFactory.getInstance().resetRandCharImg();
		gp.stop();

		// 2. Tạo lại toàn bộ các panel
		panelManager = PanelManager.getInstance(); // lấy lại singleton
		panelManager.addPanel("MENU_PANEL", new MenuPanel());
		panelManager.addPanel("GAME_SETTING_PANEL", new GameSettingPanel());
		panelManager.addPanel("GAME_PANEL", new GamePanel());

		// 3. Hiện lại menu chính
		showMainMenu();
	}

}
