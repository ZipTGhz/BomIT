package Model.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Collections.Vector2;
import Components.BoxCollider;
import Interfaces.IDamageable;
import Interfaces.IGS;
import Interfaces.IHash;
import Model.GameManager;

public abstract class Character extends Entity implements IDamageable {
	private static String[] stateNames = new String[] { IHash.CharacterState.IDLE_DOWN, IHash.CharacterState.IDLE_RIGHT,
			IHash.CharacterState.IDLE_UP, IHash.CharacterState.IDLE_LEFT, IHash.CharacterState.MOVE_DOWN,
			IHash.CharacterState.MOVE_RIGHT, IHash.CharacterState.MOVE_UP, IHash.CharacterState.MOVE_LEFT, };

	protected int speed;
	protected int health;
	protected BoxCollider collider;

	public Character(int x, int y, int speed) {
		super(x, y);
		this.speed = speed;
		collider = new BoxCollider();
		initCharacter();
	}

	public Character(Vector2 position, int speed) {
		super(position);
		this.speed = speed;
		collider = new BoxCollider();
		initCharacter();
	}

	public void move(int dx, int dy) {
		position.x += dx * speed;
		position.y += dy * speed;
	}

	public void placeBomb() {
		Vector2 placePos = this.getPlacePostion();

		ArrayList<Bomb> bombs = GameManager.getInstance().getBombs();
		for (Bomb bomb : bombs) {
			if (placePos.equals(bomb.position))
				return;
		}

		if (GameManager.getInstance().isCharacterPlaceBomb(this) == false) {
			Bomb bomb = new Bomb(placePos);
			GameManager.getInstance().addBomb(bomb, this);
		}
	}

	public boolean isAlive() {
		return health != 0;
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
		sr.render(g, this.position);
		if (collider.canRender) {
			Vector2 worldPos = position.add(collider.offset);
			g.setColor(Color.GREEN);
			g.drawRect(worldPos.x, worldPos.y, collider.size.x, collider.size.y);
		}

		Vector2 placePos = this.position.add(this.collider.offset).add(this.collider.size);
		placePos.x -= this.collider.size.x / 2;
		placePos.y -= this.collider.size.y / 4;

		placePos.y = placePos.y / IGS.BLOCK_SIZE * IGS.BLOCK_SIZE;
		placePos.x = placePos.x / IGS.BLOCK_SIZE * IGS.BLOCK_SIZE;
		g.setColor(Color.WHITE);
		g.drawRect(placePos.x, placePos.y, IGS.BLOCK_SIZE, IGS.BLOCK_SIZE);

	}

	@Override
	public void damage() {
		this.health = Math.max(0, this.health - 1);
		if (this.health == 0)
			die();
	}

	protected void initCharacter() {
		collider.canRender = true;
		collider.offset = new Vector2(11, 27);
		collider.size = new Vector2(26, 20);

		this.loadSprites(null, stateNames);

		sr.setInterval(10);
		sr.changeState(IHash.CharacterState.IDLE_DOWN);
	}

	private void die() {
		GameManager.getInstance().deleteCharacter(this);
	}

	protected Vector2 getPlacePostion() {
		Vector2 placePos = this.position.add(this.collider.offset).add(this.collider.size);
		placePos.x -= this.collider.size.x / 2;
		placePos.y -= this.collider.size.y / 4;

		placePos.y = placePos.y / IGS.BLOCK_SIZE * IGS.BLOCK_SIZE;
		placePos.x = placePos.x / IGS.BLOCK_SIZE * IGS.BLOCK_SIZE;

		return placePos;
	}
}
