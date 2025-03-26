package View.Panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Components.Front.CharacterCard;
import Components.Front.GameButton;
import Interfaces.IGS;
import Model.GameManager;
import Model.Entity.Character;
import Util.UtilityTools;

public class GamePanel extends JPanel implements Runnable {
	private static boolean paused = false;

	public static void setPausedGame(boolean paused) {
		GamePanel.paused = paused;
	}

	private Thread gameThread;

	private CharacterCard[] charCards;

	private final String TIME = "TIME: ";
	private GameButton btnPause;

	public GamePanel() {
		config();
		init();
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
				this.update();
				this.repaint();
				--delta;
			}
		}

	}

	public void update() {
		for (CharacterCard charCard : charCards) {
			charCard.update();
		}
	}

	public void render(Graphics g) {
		for (CharacterCard charCard : charCards) {
			charCard.render(g);
		}

		g.drawString(TIME, 850, 550);
		double remainingTime = GameManager.getInstance().getRemainingTime();
		String timeText = String.valueOf(Math.round(remainingTime));
		g.drawString(timeText, 980, 550);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		GameManager.getInstance().render(g);
		this.render(g);
		btnPause.render(g);
		g.dispose();
	}

	private void init() {
		Character[] characters = GameManager.getInstance().getAllCharacters();
		charCards = new CharacterCard[characters.length];
		int offset_x = 835, offset_y = 20;

		for (int i = 0; i < characters.length; ++i) {
			CharacterCard tmp = new CharacterCard(characters[i], offset_x, offset_y);
			charCards[i] = tmp;
			offset_y += 125;
		}

		loadButtons();

	}

	private void loadButtons() {
		loadBtnPause();
	}

	private void loadBtnPause() {
		try {
			File[] files = UtilityTools.getFiles("/Resources/Images/Buttons/Pause", ".png");
			BufferedImage normal = ImageIO.read(files[0]);
			BufferedImage clicked = ImageIO.read(files[1]);
			BufferedImage hover = ImageIO.read(files[2]);

			normal = UtilityTools.scaleImage(normal, normal.getWidth() * 3, normal.getHeight() * 3);
			clicked = UtilityTools.scaleImage(clicked, clicked.getWidth() * 3, clicked.getHeight() * 3);
			hover = UtilityTools.scaleImage(hover, hover.getWidth() * 3, hover.getHeight() * 3);

			btnPause = new GameButton(normal, clicked, hover, 860, 600);
			btnPause.registerAdapter(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void config() {
		this.setPreferredSize(new Dimension(IGS.GAME_WIDTH, IGS.GAME_HEIGHT));
		this.setDoubleBuffered(true);
		this.setLayout(null);
	}
}
