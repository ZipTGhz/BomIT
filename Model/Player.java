package Model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import Collections.Vector2;
import Controller.CollisionChecker;
import Util.UtilityTools;

public class Player extends Character {

	public Player(int x, int y, int speed) {
		super(x, y, speed);
		this.heart = 3;
		initPlayer();
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
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.IDLE_DOWN, idle);
			}

			sr.addState(IHash.CharacterState.IDLE_RIGHT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/idleB/hero_idleB_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.IDLE_RIGHT, idle);
			}

			sr.addState(IHash.CharacterState.IDLE_UP);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/idleC/hero_idleC_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.IDLE_UP, idle);
			}

			sr.addState(IHash.CharacterState.IDLE_LEFT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/idleD/hero_idleD_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.IDLE_LEFT, idle);
			}

			sr.addState(IHash.CharacterState.MOVE_DOWN);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/walkA/hero_walkA_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.MOVE_DOWN, idle);
			}

			sr.addState(IHash.CharacterState.MOVE_RIGHT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/walkB/hero_walkB_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.MOVE_RIGHT, idle);
			}

			sr.addState(IHash.CharacterState.MOVE_UP);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/walkC/hero_walkC_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.MOVE_UP, idle);
			}

			sr.addState(IHash.CharacterState.MOVE_LEFT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/characters/char 0/walkD/hero_walkD_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
				sr.addSprite(IHash.CharacterState.MOVE_LEFT, idle);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		super.update();
		Vector2 curDir = GameManager.getInstance().getInput().getDirection();
		if (curDir.x > 0) {
			sr.changeState(IHash.CharacterState.MOVE_RIGHT);
		} else if (curDir.x < 0) {
			sr.changeState(IHash.CharacterState.MOVE_LEFT);
		} else if (curDir.y > 0) {
			sr.changeState(IHash.CharacterState.MOVE_DOWN);
		} else if (curDir.y < 0) {
			sr.changeState(IHash.CharacterState.MOVE_UP);
		} else {
			Vector2 lastInput = GameManager.getInstance().getInput().getLastInput();
			if (lastInput.equals(Vector2.left())) {
				sr.changeState(IHash.CharacterState.IDLE_LEFT);
			} else if (lastInput.equals(Vector2.right())) {
				sr.changeState(IHash.CharacterState.IDLE_RIGHT);
			} else if (lastInput.equals(Vector2.up())) {
				sr.changeState(IHash.CharacterState.IDLE_UP);
			} else if (lastInput.equals(Vector2.down())) {
				sr.changeState(IHash.CharacterState.IDLE_DOWN);
			} else {
				sr.changeState(IHash.CharacterState.IDLE_DOWN);
			}
		}

		if (GameManager.getInstance().getInput().isSpacePressed()) {
			this.placeBomb();
		}

		if (CollisionChecker.checkCharacterCollision(this, curDir)) {
			return;
		}
		this.move(curDir.x, curDir.y);
	}

	@Override
	public void render(Graphics g) {

		sr.render(g, position);
		super.render(g);
	}


}
