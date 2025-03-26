package View;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Controller.Input;
import View.Panel.GamePanel;

public class GameFrame extends JFrame {
	private CardLayout _cardLayout;
	private JPanel _cardPanel;

	private MenuPanel _mp;
	private GameSettingPanel _gsp;
	private GamePanel _gp;

	public GameFrame() {
		initComponent();
		initUI();
		this.add(_cardPanel);
		this.addKeyListener(new Input());
	}

	public void start() {
		playGame();
		pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void showMainMenu() {
		_cardLayout.show(_cardPanel, "mp");
		_mp.requestFocusInWindow();
	}

	public void playGame() {
		_cardLayout.show(_cardPanel, "gp");
		_gp.start();
		_gp.requestFocusInWindow();
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

}
