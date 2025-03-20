package Model.Entity;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Collections.Vector2;
import Controller.CollisionChecker;
import Controller.Input;
import Model.GS;
import Model.GameManager;
import Model.IHash;
import Util.UtilityTools;

public class Player extends Character {

	public Player(int x, int y, int speed) {
		super(x, y, speed);
		this.heart = 3;
		initPlayer();
	}

	@Override
	public void update() {
		super.update();
		Vector2 curDir = handleMovement();

		if (Input.GetKey(KeyEvent.VK_SPACE))
			this.placeBomb();

		if (handleCollision(curDir))
			return;

		this.move(curDir.x, curDir.y);
	}

	@Override
	public void render(Graphics g) {
		sr.render(g, position);
		super.render(g);
	}

	@Override
	public void die() {
		return;
	}

	private boolean handleCollision(Vector2 curDir) {
		if (CollisionChecker.checkCharacterCollision(this, curDir))
			return true;

		ArrayList<Bomb> bombs = GameManager.getInstance().getBombs();
		for (Bomb bomb : bombs) {
			if (CollisionChecker.checkBombCollision(this, curDir, bomb))
				return true;
		}
		return false;
	}

	private Vector2 handleMovement() {
		int x = Input.GetAxisRaw(IHash.InputDirection.HORIZONTAL);
		int y = Input.GetAxisRaw(IHash.InputDirection.VERTICAL);
		if (x != 0 && y != 0) {
			int keyCode = Input.getLastKeyPressed();
			x = y = 0;
			switch (keyCode) {
			case KeyEvent.VK_S:
				y = 1;
				break;
			case KeyEvent.VK_A:
				x = -1;
				break;
			case KeyEvent.VK_D:
				x = 1;
				break;
			case KeyEvent.VK_W:
				y = -1;
				break;
			}
		}
		Vector2 curDir = new Vector2(x, y);
		if (curDir.x > 0) {
			sr.changeState(IHash.CharacterState.MOVE_RIGHT);
		} else if (curDir.x < 0) {
			sr.changeState(IHash.CharacterState.MOVE_LEFT);
		} else if (curDir.y > 0) {
			sr.changeState(IHash.CharacterState.MOVE_DOWN);
		} else if (curDir.y < 0) {
			sr.changeState(IHash.CharacterState.MOVE_UP);
		} else {
			int keyCode = Input.getLastKeyPressed();
			switch (keyCode) {
			case KeyEvent.VK_S:
				sr.changeState(IHash.CharacterState.IDLE_DOWN);
				break;
			case KeyEvent.VK_A:
				sr.changeState(IHash.CharacterState.IDLE_LEFT);
				break;
			case KeyEvent.VK_D:
				sr.changeState(IHash.CharacterState.IDLE_RIGHT);
				break;
			case KeyEvent.VK_W:
				sr.changeState(IHash.CharacterState.IDLE_UP);
				break;
			}
		}
		return curDir;
	}

	private void initPlayer() {
		collider.canRender = true;
		collider.offset = new Vector2(10, 17);
		collider.size = new Vector2(29, 27);

		sr.setInterval(2);

		try {
			sr.addState(IHash.CharacterState.IDLE_DOWN);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/Characters/Char 0/idleA/hero_idleA_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.IDLE_DOWN, idle);
			}

			sr.addState(IHash.CharacterState.IDLE_RIGHT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/idleB/hero_idleB_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.IDLE_RIGHT, idle);
			}

			sr.addState(IHash.CharacterState.IDLE_UP);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/idleC/hero_idleC_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.IDLE_UP, idle);
			}

			sr.addState(IHash.CharacterState.IDLE_LEFT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/idleD/hero_idleD_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.IDLE_LEFT, idle);
			}

			sr.addState(IHash.CharacterState.MOVE_DOWN);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/walkA/hero_walkA_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.MOVE_DOWN, idle);
			}

			sr.addState(IHash.CharacterState.MOVE_RIGHT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/walkB/hero_walkB_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.MOVE_RIGHT, idle);
			}

			sr.addState(IHash.CharacterState.MOVE_UP);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/walkC/hero_walkC_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.MOVE_UP, idle);
			}

			sr.addState(IHash.CharacterState.MOVE_LEFT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/walkD/hero_walkD_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.MOVE_LEFT, idle);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		sr.changeState(IHash.CharacterState.IDLE_DOWN);
	}

}
