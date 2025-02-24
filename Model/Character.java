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

	public void placeBomb() {
		if (GameManager.getInstance().isCharacterPlaceBomb(this) == false) {
			Vector2 placePos = this.position.add(this.collider.offset).add(this.collider.size);
			placePos.x -= this.collider.size.x / 2;
			placePos.y -= this.collider.size.y / 4;

			placePos.y = placePos.y / GS.Config.BLOCK_SIZE * GS.Config.BLOCK_SIZE;
			placePos.x = placePos.x / GS.Config.BLOCK_SIZE * GS.Config.BLOCK_SIZE;

			Bomb bomb = new Bomb(placePos);
			GameManager.getInstance().addBomb(bomb, this);
		}
	}

	public void die() {
		// DO SOMETHING
	}

	public boolean isAlive() {
		return heart != 0;
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

	@Override
	public void update() {}

	@Override
	public void render(Graphics g) {
		if (collider.canRender) {
			Vector2 worldPos = position.add(collider.offset);
			g.setColor(Color.GREEN);
			g.drawRect(worldPos.x, worldPos.y, collider.size.x, collider.size.y);
		}

		Vector2 placePos = this.position.add(this.collider.offset).add(this.collider.size);
		placePos.x -= this.collider.size.x / 2;
		placePos.y -= this.collider.size.y / 4;

		placePos.y = placePos.y / GS.Config.BLOCK_SIZE * GS.Config.BLOCK_SIZE;
		placePos.x = placePos.x / GS.Config.BLOCK_SIZE * GS.Config.BLOCK_SIZE;
		g.setColor(Color.WHITE);
		g.drawRect(placePos.x, placePos.y, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);

	}


}
