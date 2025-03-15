package Model;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import Collections.Vector2;
import Util.UtilityTools;

public class Bot extends Character {
	private static long INTERVAL = 1500;
	private int difficult;
	private Random random = new Random();
	private long lastTime = 0;

	private ArrayList<Vector2> path = new ArrayList<>();
	int pathIdx = 0;

	public Bot(int x, int y, int speed, int difficult) {
		super(x, y, speed);
		this.heart = 1;
		this.difficult = difficult;
		initBot();
	}

	@Override
	public void update() {
		// Độ khó ảnh hưởng đến mảng đường đi của bot
		if (this.difficult == 0) {
			easyMode();
		} else {
			normalMode();
		}

		// Dựa theo path đã tính ở trên
		// thực hiện di chuyển theo
		while (pathIdx < path.size()) {
			Vector2 pathPos = path.get(pathIdx).clone();
			pathPos.x *= GS.Config.BLOCK_SIZE;
			pathPos.y *= GS.Config.BLOCK_SIZE;
			if (Vector2.distance(this.position, pathPos) > GS.Config.EPSILON) {
				break;
			}
			pathIdx++;
		}

		Vector2 nextPos = null;
		if (pathIdx < path.size()) {
			nextPos = path.get(pathIdx).clone();
			nextPos.x *= GS.Config.BLOCK_SIZE;
			nextPos.y *= GS.Config.BLOCK_SIZE;
		} else {
			nextPos = this.position;
		}

		Vector2 curDir = Vector2.getDirection(this.position, nextPos);
		// System.out.println(curDir.x + " " + curDir.y);
		if (curDir.x > 0) {
			sr.changeState(IHash.CharacterState.MOVE_RIGHT);
		} else if (curDir.x < 0) {
			sr.changeState(IHash.CharacterState.MOVE_LEFT);
		} else if (curDir.y > 0) {
			sr.changeState(IHash.CharacterState.MOVE_DOWN);
		} else if (curDir.y < 0) {
			sr.changeState(IHash.CharacterState.MOVE_UP);
		} else {
			sr.changeState(IHash.CharacterState.IDLE_DOWN);
			lastTime = 0;
			if (canDestroyObstacle()) {
				placeBomb();
			}
		}
		this.move(curDir.x, curDir.y);

		if (isPlayerInExplosionRange()) {
			placeBomb();
		}
	}

	@Override
	public void placeBomb() { 
		super.placeBomb();
		lastTime = 0;
	}

	@Override
	public void render(Graphics g) {
		sr.render(g, position);
		super.render(g);
	}

	private void initBot() {
		collider.canRender = true;
		collider.offset = new Vector2(10, 17);
		collider.size = new Vector2(29, 27);

		sr.setInterval(10);

		sr.addState(IHash.CharacterState.IDLE_DOWN);
		sr.addState(IHash.CharacterState.IDLE_LEFT);
		sr.addState(IHash.CharacterState.IDLE_RIGHT);
		sr.addState(IHash.CharacterState.IDLE_UP);

		try {
			URL url = getClass().getResource("/Resources/Character without weapon/idle");
			File folder = new File(url.toURI());
			File[] files = folder.listFiles();

			for (int i = 0; i < files.length; ++i) {
				BufferedImage idleImage = ImageIO.read(files[i]);
				idleImage = UtilityTools.scaleImage(idleImage, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				if (i < 4) {
					sr.addSprite(IHash.CharacterState.IDLE_DOWN, idleImage);
				} else if (i < 8) {
					sr.addSprite(IHash.CharacterState.IDLE_LEFT, idleImage);
				} else if (i < 12) {
					sr.addSprite(IHash.CharacterState.IDLE_RIGHT, idleImage);
				} else {
					sr.addSprite(IHash.CharacterState.IDLE_UP, idleImage);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		sr.addState(IHash.CharacterState.MOVE_DOWN);
		sr.addState(IHash.CharacterState.MOVE_LEFT);
		sr.addState(IHash.CharacterState.MOVE_RIGHT);
		sr.addState(IHash.CharacterState.MOVE_UP);

		try {
			URL url = getClass().getResource("/Resources/Character without weapon/walk");
			File folder = new File(url.toURI());
			File[] files = folder.listFiles();

			for (int i = 0; i < files.length; ++i) {
				BufferedImage walkImage = ImageIO.read(files[i]);
				walkImage = UtilityTools.scaleImage(walkImage, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
				if (i < 4) {
					sr.addSprite(IHash.CharacterState.MOVE_DOWN, walkImage);
				} else if (i < 8) {
					sr.addSprite(IHash.CharacterState.MOVE_LEFT, walkImage);
				} else if (i < 12) {
					sr.addSprite(IHash.CharacterState.MOVE_RIGHT, walkImage);
				} else {
					sr.addSprite(IHash.CharacterState.MOVE_UP, walkImage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void easyMode() {
		// Đổ random 4 hướng
	}

	private void normalMode() {
		// Chạy thuật toán tìm đường mỗi 'interval' mili giây
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastTime >= INTERVAL) {
			lastTime = currentTime;
			Character[] characters = GameManager.getInstance().getAllCharacters();
			Character player = characters[0];

			Vector2 topLeftBot = this.position.add(collider.offset);
			Vector2 centerBot = topLeftBot.add(new Vector2(collider.size.x / 2, collider.size.y / 2));
			Vector2 rightBottomBot = topLeftBot.add(collider.size);

			Vector2 centerPlayer = player.position.add(player.getColliderOffset());
			centerPlayer.x += player.collider.size.x / 2;
			centerPlayer.y += player.collider.size.y / 2;

			// ArrayList<Vector2> path1 = AI.calculateMove(topLeftBot, centerPlayer);
			// ArrayList<Vector2> path2 = AI.calculateMove(rightBottomBot, centerPlayer);
			// if (path1.size() < path2.size()) {
			// path = path2;
			// } else {
			// path = path1;
			// }
			path = AI.calculateMove(topLeftBot, centerPlayer);
			pathIdx = 0;
			for (Vector2 pos : path) {
				System.out.print("(" + pos.x + ", " + pos.y + ") ");
			}
			if (path.size() != 0)
				System.out.println();
		}
	}

	private boolean isPlayerInExplosionRange() {
		Character[] characters = GameManager.getInstance().getAllCharacters();
		Character player = characters[0];
		Vector2 topLeftPlayer = player.position.add(player.collider.offset);
		Rectangle playerRect = new Rectangle(topLeftPlayer.x, topLeftPlayer.y, player.collider.size.x,
				player.collider.size.y);

		Vector2 placePos = this.getPlacePostion();

		ArrayList<Vector2> explodeZone = Bomb.explodeZoneIfPlace(placePos);
		for (Vector2 zone : explodeZone) {
			Rectangle zoneRect = new Rectangle(zone.x, zone.y, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
			if (zoneRect.intersects(playerRect)) {
				return true;
			}
		}
		return false;
	}

	private boolean canDestroyObstacle() { return true; }

}
