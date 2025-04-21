package View.Panel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import Components.Front.CharacterCard;
import Components.Front.GameButton;
import Interfaces.IGS;
import Model.GameManager;
import Model.Entity.Character;
import Util.UtilityTools;
import View.Dialog.PauseDialog;
import View.Frame.GameFrame;

//vấn đề kinh điển khi xử lý "pause – resume" với đồng hồ chạy bằng delta time (IGS.DELTA_TIME
public class GamePanel extends JPanel implements Runnable {
	private static boolean paused = false;
	boolean isWin = true;  // Hoặc false, tùy thuộc vào kết quả game

	public static void setPausedGame(boolean value) {
		paused = value;
	}

	private Thread gameThread;

	private CharacterCard[] charCards;

	private final String TIME = "TIME: ";
	private GameButton btnPause;
	private PauseDialog pauseDialog;

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
			if (!paused) {
				currentTime = System.nanoTime();
				delta += (currentTime - lastTime) / drawInterval;
				lastTime = currentTime;

				if (delta >= 1) {
					GameManager.getInstance().update();
					this.update();
					this.repaint();
					delta--;
				}
			} else {
				// Nếu đang pause thì đợi 10ms rồi check lại (tránh CPU bị full load)
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
			btnPause.setOnClickAction(() -> {
				pauseDialog = new PauseDialog(GameFrame.getInstance());
				pauseDialog.showDialog();
				System.out.println("PAUSE BUTTON CLICKED");
			});
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
