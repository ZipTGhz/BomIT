package Model;

import java.awt.Color;
import java.awt.Graphics;
import Collections.Vector2;
import Components.BoxCollider;

public abstract class Character extends Entity {
	protected int speed;
	protected int heart;
	protected BoxCollider collider;

	public Character(int x, int y, int speed) {
		super(x, y);
		this.speed = speed;
		collider = new BoxCollider();
	}

	public void move(int dx, int dy) {
		position.x += dx * speed;
		position.y += dy * speed;
	}

	public void placeBomb() {}

	public void die() {
		// DO SOMETHING
	}

	public boolean isAlive() {
		return heart != 0;
	}

	@Override
	public void render(Graphics g) {
		if (collider.canRender) {
			Vector2 worldPos = position.add(collider.offset);
			g.setColor(Color.GREEN);
			g.drawRect(worldPos.x, worldPos.y, collider.size.x, collider.size.y);
		}
	}

	public Vector2 getColliderOffset() {
		return collider.offset;
	}

	public Vector2 getColliderSize() {
		return collider.size;
	}

	public int getSpeed() {
		return speed;
	}
}
