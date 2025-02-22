package View;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import Controller.CollisionChecker;
import Controller.PlayerController;
import Model.Bot;
import Model.IGameSettings;
import Model.Player;
import Model.TileMap;

/*
 * Xử lý tất cả logic chơi game ở đây
 */
public class GamePanel extends JPanel implements Runnable {
	// Thành phần hệ thống
	private Thread gameThread;
	private CollisionChecker collisionChecker;
	private PlayerController playerController;

	// Người chơi, bot, bản đồ
	private TileMap tileMap;
	private Player player;
	private Bot[] _bots;

	public GamePanel() {
		this.setPreferredSize(
				new Dimension(IGameSettings.Config.GAME_WIDTH, IGameSettings.Config.GAME_HEIGHT));
		this.setDoubleBuffered(true);
		this.setFocusable(true);

		init();
	}

	private void init() {
		tileMap = new TileMap();

		collisionChecker = new CollisionChecker(this);
		playerController = new PlayerController();
		player = new Player(240, 240, 2, this);
		this.addKeyListener(playerController);
	}

	public void startGameThread() {
		if (gameThread == null) {
			gameThread = new Thread(this);
			gameThread.start();
		}
	}

	public TileMap getTileMap() {
		return tileMap;
	}

	public PlayerController getPlayerController() {
		return playerController;
	}

	public CollisionChecker getCollisionChecker() {
		return collisionChecker;
	}

	@Override
	public void run() {
		double drawInterval = 1e9 / IGameSettings.Config.FPS;
		long lastTime = System.nanoTime();
		long currentTime;
		double delta = 0;

		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			if (delta >= 1) {
				// Nhận input và vẽ lại ở đây
				player.update();
				// for (Bot bot : _bots) {
				// bot.update();
				// }
				tileMap.update();
				this.repaint();
				--delta;
			}
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		tileMap.render(g);
		player.render(g);
		// for (Bot bot : _bots) {
		// bot.render(g);
		// }
		g.dispose();
	}
}
