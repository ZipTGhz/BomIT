package Model.Entity;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import Collections.Vector2;
import Controller.CollisionChecker;
import Interfaces.IGS;
import Interfaces.IHash;
import Model.GameManager;
import Model.AI.AI;
import Model.Map.Tile;

public class Bot extends Character {
	private static double INTERVAL;
	private int difficult;
	private Random random = null;
	private double lastTime;
	private ArrayList<Vector2> path = new ArrayList<>();
	private int pathIdx = 0;

	public Bot(int x, int y, int speed, int difficult) {
		super(x, y, speed);
		this.health = 1;
		this.difficult = difficult;
		if (difficult == 0)
			INTERVAL = 1.25;
		else
			INTERVAL = 0.5;
		lastTime = INTERVAL;
	}

	public Bot(Vector2 positon, int speed, int difficult) {
		super(positon, speed);
		this.health = 1;
		this.difficult = difficult;
		if (difficult == 0)
			INTERVAL = 1;
		else
			INTERVAL = 0.5;
		lastTime = INTERVAL;
	}

	@Override
	public void update() {
		handleAIPath();

		Vector2 curDir = handleMovement();

		if (handleCollision(curDir))
			return;

		this.move(curDir.x, curDir.y);

		if (GameManager.getInstance().isCharacterPlaceBomb(this) == false && isPlayerInExplosionRange()) {
			placeBomb();
		}
	}

	@Override
	public void placeBomb() {
		super.placeBomb();
		lastTime = INTERVAL;
	}

	private void handleAIPath() {
		lastTime += IGS.DELTA_TIME;
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
	}

	private Vector2 handleMovement() {
		// Dựa theo path đã tính ở trên
		// thực hiện di chuyển theo
		while (pathIdx < path.size()) {
			Vector2 pathPos = path.get(pathIdx).clone();
			pathPos.x *= IGS.BLOCK_SIZE;
			pathPos.y *= IGS.BLOCK_SIZE;
			if (Vector2.distance(this.position, pathPos) > IGS.EPSILON) {
				break;
			}
			pathIdx++;
		}

		Vector2 nextPos = null;
		if (pathIdx < path.size()) {
			nextPos = path.get(pathIdx).clone();
			nextPos.x *= IGS.BLOCK_SIZE;
			nextPos.y *= IGS.BLOCK_SIZE;
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
		return curDir;
	}

	private boolean handleCollision(Vector2 curDir) {
		ArrayList<Bomb> bombs = GameManager.getInstance().getBombs();
		for (Bomb bomb : bombs) {
			if (CollisionChecker.checkBombCollision(this, curDir, bomb)) {
				return true;
			}
		}
		return false;
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
		Vector2 curTilePos = this.position.divide(IGS.BLOCK_SIZE);
		int[][] dir = new int[][] { { -1, 0 }, { 1, 0 }, { 0, 0 }, { 0, -1 }, { 0, 1 } };
		for (int[] d : dir) {
			Vector2 nextPath = curTilePos.add(d[0], d[1]);
			if (dangerZone[nextPath.y][nextPath.x])
				continue;

			int tileIndex = GameManager.getInstance().getTileMap().getTileIndex(nextPath.y, nextPath.x);
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
		Rectangle playerRect = new Rectangle(topLeftPlayer.x, topLeftPlayer.y, player.collider.size.x,
				player.collider.size.y);

		Vector2 placePos = this.getPlacePostion();

		ArrayList<Vector2> explodeZone = Bomb.explodeZoneIfPlace(placePos);
		for (Vector2 zone : explodeZone) {
			Rectangle zoneRect = new Rectangle(zone.x, zone.y, IGS.BLOCK_SIZE, IGS.BLOCK_SIZE);
			if (zoneRect.intersects(playerRect)) {
				return true;
			}
		}
		return false;
	}

}
