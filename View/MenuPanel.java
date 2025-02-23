package View;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import Model.IGameSettings;
import View.Sound;
public class MenuPanel extends JPanel {
	private BufferedImage backgroundImage;
	private BufferedImage startButtonImage, howToPlayButtonImage;
	private BufferedImage controlImage;
	private BufferedImage closeButtonImage;
	private BufferedImage muteImage, unmuteImage;
	private boolean showControlImage = false;
	private boolean isMuted = false;
	private Clip backgroundMusic;
	private int buttonWidth = 200;
	private int buttonHeight = 55;
	private int startButtonX, startButtonY;
	private int howToPlayButtonX, howToPlayButtonY;
	private int closeButtonX, closeButtonY;
	private int closeButtonSize = 30;
	private int soundButtonX, soundButtonY;
	private int soundButtonSize = 50; // Kích thước nút âm thanh
	private int currentFramePosition = 0; // Lưu vị trí nhạc khi tắt

	public MenuPanel() {
		System.out.println("Tạo MenuPanel!");
		this.setPreferredSize(new Dimension(IGameSettings.Config.GAME_WIDTH, IGameSettings.Config.GAME_HEIGHT));
		initPanel();
		loadAndPlayMusic();

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();
				int mouseY = e.getY();

				if (mouseX >= howToPlayButtonX && mouseX <= howToPlayButtonX + buttonWidth &&
						mouseY >= howToPlayButtonY && mouseY <= howToPlayButtonY + buttonHeight) {

					showControlImage = !showControlImage;
					repaint();
				}

				if (showControlImage && mouseX >= closeButtonX && mouseX <= closeButtonX + closeButtonSize &&
						mouseY >= closeButtonY && mouseY <= closeButtonY + closeButtonSize) {

					showControlImage = false;
					repaint();
				}

				if (mouseX >= soundButtonX && mouseX <= soundButtonX + soundButtonSize &&
						mouseY >= soundButtonY && mouseY <= soundButtonY + soundButtonSize) {

					isMuted = !isMuted;

					if (isMuted) {
						if (backgroundMusic != null && backgroundMusic.isRunning()) {
							backgroundMusic.stop();
							backgroundMusic.flush(); // Xóa bộ đệm
						}
					} else {
						if (backgroundMusic != null) {
							backgroundMusic.setFramePosition(0);
							backgroundMusic.start();
							backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
						}
					}

					repaint();
				}

			}
		});

	}
	private void loadAndPlayMusic() {
		try {
			// Nếu nhạc đã được mở, không load lại
			if (backgroundMusic != null && backgroundMusic.isOpen()) {
				return;
			}

			URL musicPath = getClass().getResource("/Resources/song.wav");
			if (musicPath == null) {
				throw new RuntimeException("Không tìm thấy file nhạc!");
			}

			AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicPath);
			backgroundMusic = AudioSystem.getClip();
			backgroundMusic.open(audioStream);

			if (!isMuted) { // Chỉ phát nhạc nếu không bị mute
				backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
				backgroundMusic.start();
			}

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}




	private void initPanel() {
		try {
			backgroundImage = ImageIO.read(getClass().getResource("/Resources/background.jpg"));
			startButtonImage = ImageIO.read(getClass().getResource("/Resources/start.png"));
			howToPlayButtonImage = ImageIO.read(getClass().getResource("/Resources/how_to_play.png"));
			controlImage = ImageIO.read(getClass().getResource("/Resources/control.jpeg"));
			closeButtonImage = ImageIO.read(getClass().getResource("/Resources/close.png"));
			muteImage = ImageIO.read(getClass().getResource("/Resources/mute.png"));
			unmuteImage = ImageIO.read(getClass().getResource("/Resources/unmute.png"));

			// Tọa độ nút chính
			startButtonX = IGameSettings.Config.GAME_WIDTH - buttonWidth - 120;
			startButtonY = IGameSettings.Config.GAME_HEIGHT / 2 - 120;

			howToPlayButtonX = startButtonX;
			howToPlayButtonY = startButtonY + buttonHeight + 20;

			// Vị trí nút âm thanh (ở giữa, dưới cùng)
			soundButtonX = (IGameSettings.Config.GAME_WIDTH - soundButtonSize) / 2;
			soundButtonY = IGameSettings.Config.GAME_HEIGHT - soundButtonSize - 20;

		} catch (IOException e) {
			throw new RuntimeException("Lỗi load ảnh!", e);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Lấy kích thước gốc của ảnh
		int imgWidth = backgroundImage.getWidth();
		int imgHeight = backgroundImage.getHeight();

		// Căn giữa hình nền
		int bgX = (getWidth() - imgWidth) / 5;
		int bgY = (getHeight() - imgHeight) / 5;

		g.drawImage(backgroundImage, bgX, bgY, null);

		// Vẽ nút
		g.drawImage(startButtonImage, startButtonX, startButtonY, buttonWidth, buttonHeight, null);
		g.drawImage(howToPlayButtonImage, howToPlayButtonX, howToPlayButtonY, buttonWidth, buttonHeight, null);

		// Hiển thị ảnh hướng dẫn + nút Close nếu được bật
		if (showControlImage && controlImage != null) {
			int imgWidth1 = controlImage.getWidth() / 3;
			int imgHeight1 = controlImage.getHeight() / 3;

			// Căn giữa ảnh hướng dẫn
			int controlX = (getWidth() - imgWidth1) / 2;
			int controlY = (getHeight() - imgHeight1) / 2;

			// Vẽ ảnh hướng dẫn
			g.drawImage(controlImage, controlX, controlY, imgWidth1, imgHeight1, null);

			// Vị trí nút Close (góc phải trên của ảnh hướng dẫn)
			closeButtonX = controlX + imgWidth1 - closeButtonSize - 20;
			closeButtonY = controlY + 20;

			// Vẽ nút Close
			g.drawImage(closeButtonImage, closeButtonX, closeButtonY, closeButtonSize, closeButtonSize, null);
		}

		int adjustedHeight = soundButtonSize - 10;

		BufferedImage soundIcon = isMuted ? muteImage : unmuteImage;
		int drawHeight = isMuted ? soundButtonSize : adjustedHeight;
		int drawY = isMuted ? soundButtonY : soundButtonY + 6;

		g.drawImage(soundIcon, soundButtonX, drawY, soundButtonSize, drawHeight, null);


	}
}
