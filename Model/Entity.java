package Model;

import java.awt.Graphics;
import Collections.Vector2;
import Components.SpriteRenderer;

public abstract class Entity {
	protected Vector2 position;
	protected SpriteRenderer spriteRenderer;

	public Entity(int x, int y) {
		position = new Vector2(x, y);
		spriteRenderer = new SpriteRenderer();
	}

	public Entity(Vector2 position) {
		this.position = position;
	}

	/**
	 * Trả về toạ độ thế giới của thực thể
	 * @return
	 */
	public Vector2 getWorldPosition() {
		return position;
	}

	public abstract void update();

	public abstract void render(Graphics g);


}
