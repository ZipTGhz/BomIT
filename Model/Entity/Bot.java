package Model.Entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import Collections.Vector2;
import Controller.CollisionChecker;
import Model.AI;
import Model.GS;
import Model.GameManager;
import Model.IHash;
import Model.Map.Tile;
import Util.UtilityTools;

public class Bot extends Character {
	private static double INTERVAL = 0.75;
	private int difficult;
	private Random random = null;
	private double lastTime = INTERVAL;

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
		lastTime += GS.Config.DELTA_TIME;
		// Độ khó ảnh hưởng đến mảng đường đi của bot
		if (lastTime >= INTERVAL) {
			lastTime -= INTERVAL;
			if (this.difficult == 0)
				easyMode();
			else
				mediumMode();

			for (Vector2 pos : path) {
				System.out.print("(" + pos.x + ", " + pos.y + ") ");
			}
			if (path.size() != 0)
				System.out.println();
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

		if (this.position.x != nextPos.x && this.position.y != nextPos.y) {
			nextPos = Vector2.align(this.position, nextPos);
		}
		Vector2 curDir = Vector2.getDirection(this.position, nextPos);
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
			lastTime = INTERVAL;
			if (GameManager.getInstance().isCharacterPlaceBomb(this) == false) {
				placeBomb();
			}
		}

		ArrayList<Bomb> bombs = GameManager.getInstance().getBombs();
		for (Bomb bomb : bombs) {
			if (CollisionChecker.checkBombCollision(this, curDir, bomb)) {
				return;
			}
		}

		this.move(curDir.x, curDir.y);

		if (GameManager.getInstance().isCharacterPlaceBomb(this) == false
				&& isPlayerInExplosionRange()) {
			placeBomb();
		}
	}

	@Override
	public void placeBomb() {
		super.placeBomb();
		lastTime = INTERVAL;
	}

	@Override
	public void render(Graphics g) {
		sr.render(g, position);
		super.render(g);
	}

	@Override
	public void die() {
		GameManager.getInstance().deleteCharacter(this);
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
				idleImage = UtilityTools.scaleImage(idleImage, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
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
				walkImage = UtilityTools.scaleImage(walkImage, GS.Config.BLOCK_SIZE,
						GS.Config.BLOCK_SIZE);
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
		if (random == null) {
			random = new Random();
			this.mediumMode();
			return;
		}

		boolean[][] dangerZone = GameManager.getInstance().getDangerZone();

		if (AI.isInDangerZone(this.position)) {
			this.mediumMode();
			return;
		}

		ArrayList<Vector2> possiblePath = new ArrayList<>();
		Vector2 curTilePos = this.position.divide(GS.Config.BLOCK_SIZE);
		int[][] dir = new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { 0, 0 } };
		for (int[] d : dir) {
			Vector2 nextPath = curTilePos.add(d[0], d[1]);
			if (dangerZone[nextPath.y][nextPath.x])
				continue;

			int tileIndex = GameManager.getInstance().getTileMap().getTileIndex(nextPath.y,
					nextPath.x);
			Tile tile = GameManager.getInstance().getTileMap().getTile(tileIndex);
			if (tile.getIsCollision() == false)
				possiblePath.add(nextPath);
		}

		pathIdx = 0;
		path.clear();
		if (possiblePath.size() != 0) {
			int randInt = random.nextInt(possiblePath.size());
			path.add(possiblePath.get(randInt));
		} else {
			path.add(curTilePos);
		}
	}

	private void mediumMode() {
		// Chạy thuật toán tìm đường
		Character player = GameManager.getInstance().getAllCharacters()[0];
		Vector2 centerPlayer = player.position.add(player.getColliderOffset());
		centerPlayer.x += player.collider.size.x / 2;
		centerPlayer.y += player.collider.size.y / 2;

		Vector2 topLeftBot = this.position.add(collider.offset);

		path = AI.calculateMove(topLeftBot, centerPlayer);
		pathIdx = 0;
	}

	private boolean isPlayerInExplosionRange() {
		Character[] characters = GameManager.getInstance().getAllCharacters();
		Character player = characters[0];
		Vector2 topLeftPlayer = player.position.add(player.collider.offset);
		Rectangle playerRect = new Rectangle(topLeftPlayer.x, topLeftPlayer.y,
				player.collider.size.x, player.collider.size.y);

		Vector2 placePos = this.getPlacePostion();

		ArrayList<Vector2> explodeZone = Bomb.explodeZoneIfPlace(placePos);
		for (Vector2 zone : explodeZone) {
			Rectangle zoneRect = new Rectangle(zone.x, zone.y, GS.Config.BLOCK_SIZE,
					GS.Config.BLOCK_SIZE);
			if (zoneRect.intersects(playerRect)) {
				return true;
			}
		}
		return false;
	}

}
