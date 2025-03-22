package Model.Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Collections.Vector2;
import Components.Back.SpriteRenderer;
import Model.ImageCategory;
import Model.SpriteFactory;

public abstract class Entity {
	protected Vector2 position;
	protected SpriteRenderer sr;
	protected BufferedImage thumnail;

	public Entity(int x, int y) {
		position = new Vector2(x, y);
		sr = new SpriteRenderer();
	}

	public Entity(Vector2 position) {
		this.position = position.clone();
		sr = new SpriteRenderer();
	}

	public Vector2 getWorldPosition() {
		return position;
	}

	public abstract void update();

	public abstract void render(Graphics g);

	public BufferedImage getThumnail() {
		return thumnail;
	}

	protected void loadSprites(String spriteName, String[] stateNames) {
		ImageCategory category = null;
		if (spriteName != null)
			category = SpriteFactory.getInstance().getImageCategory(spriteName);
		else
			category = SpriteFactory.getInstance().getRandCharImg();

		for (String state : stateNames) {
			ArrayList<BufferedImage> images = category.getImages(state);
			sr.addSprite(state, images);
			if (thumnail == null)
				thumnail = images.get(0);
		}
	}

}
