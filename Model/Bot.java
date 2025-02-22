package Model;

import java.awt.Graphics;
import java.util.Random;

public class Bot extends Character {
	private Random random = new Random();


	public Bot(int x, int y, int speed) {
		super(x, y, speed);
		this.heart = 1;
	}


	@Override
	public void update() {}

	@Override
	public void render(Graphics g) {
		super.render(g);
	}


}
