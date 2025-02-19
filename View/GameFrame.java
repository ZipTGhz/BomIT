package View;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;

public class GameFrame extends JFrame {
	private static final GameFrame instance = new GameFrame();

	private CardLayout cardLayout;
	private JPanel cardPanel;

	private MenuPanel mp;
	private GamePanel gp;

	public GameFrame() {
		initComponent();
		initUI();
	}

	private void initComponent() {
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		mp = new MenuPanel();
		gp = new GamePanel();

		cardPanel.add(mp, "mp");
		cardPanel.add(gp, "gp");
	}

	private void initUI() {
		this.setTitle("Boom IT");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	public static GameFrame getInstance() {
		return instance;
	}

	public void start() {
		this.add(cardPanel);
		showMainMenu();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void showMainMenu() {
		cardLayout.show(cardPanel, "mp");
		mp.requestFocusInWindow();
	}

	public void playGame() {
		cardLayout.show(cardPanel, "gp");
		gp.requestFocusInWindow();
	}

}
