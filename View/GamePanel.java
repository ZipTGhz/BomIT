package View;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import Model.Bot;
import Model.IGameSettings;
import Model.Player;

/*
 * Xử lý tất cả logic chơi game ở đây
 */
public class GamePanel extends JPanel implements Runnable {
	// Thành phần hệ thống
	private Thread _gameThread;

	// Người chơi, bot, bản đồ
	private Player _player;
	private Bot[] _bots;

	public GamePanel() {
		this.setPreferredSize(
				new Dimension(IGameSettings.Config.GAME_WIDTH, IGameSettings.Config.GAME_HEIGHT));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}

	@Override
	public void run() {
		double drawInterval = 1e9 / IGameSettings.Config.FPS;
		long lastTime = System.nanoTime();
		long currentTime;
		double delta = 0;

		while (_gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			if (delta >= 1) {
				// Nhận input và vẽ lại ở đây
				_player.Update();
				for (Bot bot : _bots) {
					bot.Update();
				}
				this.repaint();
				--delta;
			}
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
	}
}
