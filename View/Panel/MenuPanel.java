package View.Panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Components.Front.GameButton;
import Components.Front.GameDialog;
import Interfaces.IGS;
import Model.SoundManager;
import Model.SoundType;
import Util.UtilityTools;
import View.Frame.GameFrame;

public class MenuPanel extends JPanel {
	// Cấu hình chung
	private int btnWidth = 200, btnHeight = 55;
	// Hình ảnh
	private int bgX, bgY;
	private BufferedImage backgroundImage;
	// Nút bấm
	private GameButton btnStart, btnHowToPlay, btnSound;
	// Menu phụ
	private GameDialog howToPlayDialog;

	public MenuPanel() {
		this.setPreferredSize(new Dimension(IGS.GAME_WIDTH, IGS.GAME_HEIGHT));
		init();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Vẽ hình nền
		g.drawImage(backgroundImage, bgX, bgY, null);

		// Vẽ nút
		btnStart.render(g);
		btnHowToPlay.render(g);
		btnSound.render(g);

		// Hiển thị ảnh hướng dẫn + nút Close nếu được bật
		if (howToPlayDialog.isEnabled())
			howToPlayDialog.render(g);
	}

	private void init() {
		try {
			File[] files = UtilityTools.getFiles("/Resources/MenuPanel", ".png", ".jpg", ".jpeg");
			loadImages(files);
			loadButtons(files);
			loadDialogs(files);
			SoundManager.getInstance().playMusic(SoundType.BACKGROUND_1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadImages(File[] files) throws Exception {
		backgroundImage = ImageIO.read(files[0]);
		bgX = (IGS.GAME_WIDTH - backgroundImage.getWidth()) / 5;
		bgY = (IGS.GAME_HEIGHT - backgroundImage.getHeight()) / 5;
	}

	private void loadButtons(File[] files) throws Exception {
		// Nút start
		int startButtonX = IGS.GAME_WIDTH - btnWidth - 120;
		int startButtonY = IGS.GAME_HEIGHT / 2 - 120;
		btnStart = new GameButton(ImageIO.read(files[4]), null, null, startButtonX, startButtonY, btnWidth, btnHeight);
		btnStart.registerAdapter(this);
		btnStart.setOnClickAction(new Runnable() {
			@Override
			public void run() {
				// GameFrame.getInstance().showGameMenuSetting();
				repaint();
			}
		});

		// Nút how to play
		int howToPlayButtonX = startButtonX;
		int howToPlayButtonY = startButtonY + btnHeight + 20;
		btnHowToPlay = new GameButton(ImageIO.read(files[2]), null, null, howToPlayButtonX, howToPlayButtonY, btnWidth,
				btnHeight);
		btnHowToPlay.registerAdapter(this);
		btnHowToPlay.setOnClickAction(new Runnable() {

			@Override
			public void run() {
				boolean currentState = howToPlayDialog.isEnabled();
				howToPlayDialog.setEnabled(!currentState);
				repaint();
			}

		});

		// Nút âm thanh
		int soundButtonX = (IGS.GAME_WIDTH - 50) / 2;
		int soundButtonY = IGS.GAME_HEIGHT - 50 - 20;
		btnSound = new GameButton(ImageIO.read(files[5]), ImageIO.read(files[3]), null, soundButtonX, soundButtonY);
		btnSound.setToggle(true);
		btnSound.registerAdapter(this);
		btnSound.setOnClickAction(new Runnable() {
			@Override
			public void run() {
				boolean state = btnSound.getCurrentState() == GameButton.NORMAL;
				SoundManager.getInstance().setBackgroundState(state);
				repaint();
			}
		});
	}

	private void loadDialogs(File[] files) throws Exception {
		BufferedImage controlImage = ImageIO.read(files[1]);
		int imgWidth1 = controlImage.getWidth() / 3;
		int imgHeight1 = controlImage.getHeight() / 3;
		// Căn giữa ảnh hướng dẫn
		int controlX = (IGS.GAME_WIDTH - imgWidth1) / 2;
		int controlY = (IGS.GAME_HEIGHT - imgHeight1) / 2;
		howToPlayDialog = new GameDialog(this, controlX, controlY, imgWidth1, imgHeight1);
		howToPlayDialog.addImage(controlImage, 0, 0, imgWidth1, imgHeight1);
	}
}
