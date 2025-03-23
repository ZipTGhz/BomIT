package View.Panel.GamePanel;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Interfaces.IGS;
import Model.GameManager;

public class GamePanel extends JPanel implements Runnable {
	private static boolean paused = false;
	public static void setPausedGame(boolean paused) {
		GamePanel.paused = paused;
	}

	private RightMenu rightMenu = new RightMenu();

	private Thread gameThread;

	public GamePanel() {
		config();
	}

	public void start() {
		if (gameThread == null) {
			gameThread = new Thread(this);
			gameThread.start();
		}
	}

	@Override
	public void run() {
		double drawInterval = 1e9 / IGS.FPS;
		long lastTime = System.nanoTime();
		long currentTime;
		double delta = 0;

		while (gameThread != null) {
			if (paused)
				break;
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			if (delta >= 1) {
				GameManager.getInstance().update();
				rightMenu.update();
				this.repaint();
				--delta;
			}
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		GameManager.getInstance().render(g);
		rightMenu.render(g);
		g.dispose();
	}

	private void config() {
		this.setPreferredSize(new Dimension(IGS.GAME_WIDTH, IGS.GAME_HEIGHT));
		this.setDoubleBuffered(true);
		this.setLayout(null);
	}

}
