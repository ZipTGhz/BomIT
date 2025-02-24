package View;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import Model.GameManager;
import Model.GS;


public class GamePanel extends JPanel implements Runnable {
	private Thread gameThread;

	public GamePanel() {
		config();
		init();
	}

	private void config() {
		this.setPreferredSize(
				new Dimension(GS.Config.GAME_WIDTH, GS.Config.GAME_HEIGHT));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}

	private void init() {
		this.addKeyListener(GameManager.getInstance().getInput());
	}

	public void startGameThread() {
		if (gameThread == null) {
			gameThread = new Thread(this);
			gameThread.start();
		}
	}

	@Override
	public void run() {
		double drawInterval = 1e9 / GS.Config.FPS;
		long lastTime = System.nanoTime();
		long currentTime;
		double delta = 0;

		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			if (delta >= 1) {
				GameManager.getInstance().update();
				this.repaint();
				--delta;
			}
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		GameManager.getInstance().render(g);
		g.dispose();
	}

}
