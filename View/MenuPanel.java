package View;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Interfaces.IGS;

public class MenuPanel extends JPanel {
	private Image background_image;

	public MenuPanel() {
		this.setPreferredSize(new Dimension(IGS.GAME_WIDTH, IGS.GAME_HEIGHT));

		initPanel();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background_image, 0, 0, null);
	}

	private void initPanel() {
		ImageIcon bg_icon = new ImageIcon(getClass().getResource("/Resources/background.png"));
		background_image = bg_icon.getImage();
	}
}
