package Model.Entity;

import java.awt.Graphics;

import Collections.Vector2;
import Components.SpriteRenderer;

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

	/**
	 * Trả về toạ độ thế giới của thực thể
	 * 
	 * @return
	 */
	public Vector2 getWorldPosition() { return position; }

	public abstract void update();

	public abstract void render(Graphics g);

}
