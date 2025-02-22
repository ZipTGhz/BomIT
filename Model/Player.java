package Model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import Collections.Vector2;
import Util.UtilityTools;
import View.GamePanel;


public class Player extends Character {
	private GamePanel gp;

	public Player(int x, int y, int speed, GamePanel gp) {
		super(x, y, speed);
		this.gp = gp;
		this.heart = 3;
		initPlayer();
	}

	private void initPlayer() {
		collider.canRender = true;
		collider.offset = new Vector2(43, 79);
		collider.size = new Vector2(45, 40);

		spriteRenderer.setInterval(2);

		try {
			spriteRenderer.addState(IHash.SpriteState.IDLE_DOWN);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/hero/idleA/hero_idleA_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, IGameSettings.Config.ENTITY_SIZE,
						IGameSettings.Config.ENTITY_SIZE);
				spriteRenderer.addSprite(IHash.SpriteState.IDLE_DOWN, idle);
			}

			spriteRenderer.addState(IHash.SpriteState.IDLE_RIGHT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/hero/idleB/hero_idleB_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, IGameSettings.Config.ENTITY_SIZE,
						IGameSettings.Config.ENTITY_SIZE);
				spriteRenderer.addSprite(IHash.SpriteState.IDLE_RIGHT, idle);
			}

			spriteRenderer.addState(IHash.SpriteState.IDLE_UP);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/hero/idleC/hero_idleC_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, IGameSettings.Config.ENTITY_SIZE,
						IGameSettings.Config.ENTITY_SIZE);
				spriteRenderer.addSprite(IHash.SpriteState.IDLE_UP, idle);
			}

			spriteRenderer.addState(IHash.SpriteState.IDLE_LEFT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/hero/idleD/hero_idleD_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, IGameSettings.Config.ENTITY_SIZE,
						IGameSettings.Config.ENTITY_SIZE);
				spriteRenderer.addSprite(IHash.SpriteState.IDLE_LEFT, idle);
			}

			spriteRenderer.addState(IHash.SpriteState.MOVE_DOWN);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/hero/walkA/hero_walkA_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, IGameSettings.Config.ENTITY_SIZE,
						IGameSettings.Config.ENTITY_SIZE);
				spriteRenderer.addSprite(IHash.SpriteState.MOVE_DOWN, idle);
			}

			spriteRenderer.addState(IHash.SpriteState.MOVE_RIGHT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/hero/walkB/hero_walkB_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, IGameSettings.Config.ENTITY_SIZE,
						IGameSettings.Config.ENTITY_SIZE);
				spriteRenderer.addSprite(IHash.SpriteState.MOVE_RIGHT, idle);
			}

			spriteRenderer.addState(IHash.SpriteState.MOVE_UP);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/hero/walkC/hero_walkC_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, IGameSettings.Config.ENTITY_SIZE,
						IGameSettings.Config.ENTITY_SIZE);
				spriteRenderer.addSprite(IHash.SpriteState.MOVE_UP, idle);
			}

			spriteRenderer.addState(IHash.SpriteState.MOVE_LEFT);
			for (int i = 0; i <= 16; ++i) {
				BufferedImage idle = ImageIO
						.read(getClass().getResourceAsStream("/Resources/hero/walkD/hero_walkD_00"
								+ (i < 10 ? "0" : "") + String.valueOf(i) + ".png"));
				idle = UtilityTools.scaleImage(idle, IGameSettings.Config.ENTITY_SIZE,
						IGameSettings.Config.ENTITY_SIZE);
				spriteRenderer.addSprite(IHash.SpriteState.MOVE_LEFT, idle);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		Vector2 curDir = gp.getPlayerController().getDirection();
		if (curDir.x > 0) {
			spriteRenderer.changeState(IHash.SpriteState.MOVE_RIGHT);
		} else if (curDir.x < 0) {
			spriteRenderer.changeState(IHash.SpriteState.MOVE_LEFT);
		} else if (curDir.y > 0) {
			spriteRenderer.changeState(IHash.SpriteState.MOVE_DOWN);
		} else if (curDir.y < 0) {
			spriteRenderer.changeState(IHash.SpriteState.MOVE_UP);
		} else {
			Vector2 lastInput = gp.getPlayerController().getLastInput();
			if (lastInput.equals(Vector2.left())) {
				spriteRenderer.changeState(IHash.SpriteState.IDLE_LEFT);
			} else if (lastInput.equals(Vector2.right())) {
				spriteRenderer.changeState(IHash.SpriteState.IDLE_RIGHT);
			} else if (lastInput.equals(Vector2.up())) {
				spriteRenderer.changeState(IHash.SpriteState.IDLE_UP);
			} else if (lastInput.equals(Vector2.down())) {
				spriteRenderer.changeState(IHash.SpriteState.IDLE_DOWN);
			} else {
				spriteRenderer.changeState(IHash.SpriteState.IDLE_DOWN);
			}
		}

		if (gp.getCollisionChecker().checkCollision(this, curDir)) {
			return;
		}

		this.move(curDir.x, curDir.y);
	}

	@Override
	public void render(Graphics g) {
		spriteRenderer.render(g, position);
		super.render(g);
	}


}
