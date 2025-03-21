package Model.Entity;

import java.awt.Graphics;

import Collections.Vector2;
import Components.SpriteRenderer;
import Model.ImageCategory;
import Model.SpriteFactory;

public abstract class Entity {
	protected Vector2 position;
	protected SpriteRenderer sr;

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

	protected void loadSprites(String spriteName, String[] stateNames) {
		ImageCategory category = null;
		if (spriteName != null)
			category = SpriteFactory.getInstance().getImageCategory(spriteName);
		else
			category = SpriteFactory.getInstance().getRandCharImg();

		for (String state : stateNames)
			sr.addSprite(state, category.getImages(state));
	}

}
