package View.Frame;

import javax.swing.JFrame;

import Controller.Input;
import View.Panel.GamePanel;
import View.Panel.GameSettingPanel;
import View.Panel.MenuPanel;

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
		GamePanel gp = (GamePanel) panelManager.getPanel(GAME_PANEL);
		this.getContentPane().removeAll();
		this.add(gp);
		gp.start();
		gp.requestFocusInWindow();
	}

	private void initUI() {
		setTitle("Boom IT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}

	private void initComponent() {
		panelManager = new PanelManager();
		panelManager.addPanel(MENU_PANEL, new MenuPanel());
		panelManager.addPanel(GAME_SETTING_PANEL, new GameSettingPanel());
		panelManager.addPanel(GAME_PANEL, new GamePanel());
	}

}
