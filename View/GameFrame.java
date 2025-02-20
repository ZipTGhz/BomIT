package View;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;

public class GameFrame extends JFrame {
	private static final GameFrame _instance = new GameFrame();

	private CardLayout _cardLayout;
	private JPanel _cardPanel;

	private MenuPanel _mp;
	private GameSettingPanel _gsp;
	private GamePanel _gp;

	public GameFrame() {
		initComponent();
		initUI();
		this.add(_cardPanel);
	}

	private void initComponent() {
		_cardLayout = new CardLayout();
		_cardPanel = new JPanel(_cardLayout);
		_mp = new MenuPanel();
		_gsp = new GameSettingPanel();
		_gp = new GamePanel();

		_cardPanel.add(_mp, "mp");
		_cardPanel.add(_gsp, "gsp");
		_cardPanel.add(_gp, "gp");
	}

	private void initUI() {
		this.setTitle("Boom IT");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	public static GameFrame getInstance() {
		return _instance;
	}

	public void start() {
		
		showMainMenu();
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void showMainMenu() {
		_cardLayout.show(_cardPanel, "mp");
		_mp.requestFocusInWindow();
	}

	public void playGame() {
		_cardLayout.show(_cardPanel, "gp");
		_gp.requestFocusInWindow();
	}

}
