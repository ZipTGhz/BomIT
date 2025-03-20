package Collections;

import Model.GS;
import Model.GameManager;
import Model.Map.Tile;

/**
 * Lớp đại diện cho một vector 2D với tọa độ nguyên (x, y).
 */
public class Vector2 {
	/** Tọa độ x của vector. */
	public int x;

	/** Tọa độ y của vector. */
	public int y;

	/**
	 * Khởi tạo một vector 2D với tọa độ x, y.
	 *
	 * @param x Giá trị trục x.
	 * @param y Giá trị trục y.
	 */
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Phép cộng vector với giá trị x, y cụ thể.
	 *
	 * @param x Giá trị cần cộng vào trục x.
	 * @param y Giá trị cần cộng vào trục y.
	 * @return Một vector mới với giá trị x, y đã được cộng.
	 */
	public Vector2 add(int x, int y) {
		return new Vector2(this.x + x, this.y + y);
	}

	/**
	 * Phép cộng giữa hai vector.
	 *
	 * @param other Vector cần cộng.
	 * @return Một vector mới là tổng của vector hiện tại và vector {@code other}.
	 */
	public Vector2 add(Vector2 other) {
		return new Vector2(this.x + other.x, this.y + other.y);
	}

	/**
	 * Nhân vector với một số nguyên và trả về một vector mới với giá trị đã nhân.
	 *
	 * @param scalar Giá trị số nguyên để nhân với vector.
	 * @return Một vector mới có tọa độ được nhân với {@code scalar}.
	 */
	public Vector2 scale(int scalar) {
		return new Vector2(this.x * scalar, this.y * scalar);
	}

	/**
	 * So sánh hai vector có bằng nhau hay không.
	 *
	 * @param other Vector cần so sánh.
	 * @return {@code true} nếu hai vector có cùng tọa độ, {@code false} nếu khác
	 *         nhau.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true; // Cùng tham chiếu thì chắc chắn bằng nhau
		if (obj == null || getClass() != obj.getClass())
			return false; // Khác kiểu thì không bằng

		Vector2 other = (Vector2) obj;
		return this.x == other.x && this.y == other.y;
	}

	/**
	 * Tạo một bản sao của vector hiện tại.
	 *
	 * @return Một vector mới có cùng tọa độ x, y với vector gốc.
	 */
	public Vector2 clone() {
		return new Vector2(this.x, this.y);
	}

	/**
	 * Trả về vector (0, 0).
	 *
	 * @return Một vector mới có giá trị tọa độ (0, 0).
	 */
	public static Vector2 zero() {
		return new Vector2(0, 0);
	}

	/**
	 * Trả về vector (1, 1).
	 *
	 * @return Một vector mới có giá trị tọa độ (1, 1).
	 */
	public static Vector2 one() {
		return new Vector2(1, 1);
	}

	/**
	 * Trả về vector hướng lên trên (0, -1).
	 *
	 * @return Một vector mới có giá trị tọa độ (0, -1).
	 */
	public static Vector2 up() {
		return new Vector2(0, -1);
	}

	/**
	 * Trả về vector hướng xuống dưới (0, 1).
	 *
	 * @return Một vector mới có giá trị tọa độ (0, 1).
	 */
	public static Vector2 down() {
		return new Vector2(0, 1);
	}

	/**
	 * Trả về vector hướng sang trái (-1, 0).
	 *
	 * @return Một vector mới có giá trị tọa độ (-1, 0).
	 */
	public static Vector2 left() {
		return new Vector2(-1, 0);
	}

	/**
	 * Trả về vector hướng sang phải (1, 0).
	 *
	 * @return Một vector mới có giá trị tọa độ (1, 0).
	 */
	public static Vector2 right() {
		return new Vector2(1, 0);
	}

	public static double distance(Vector2 a, Vector2 b) {
		double x = Math.pow(a.x - b.x, 2);
		double y = Math.pow(a.y - b.y, 2);
		return Math.sqrt(x + y);
	}

	public static Vector2 getDirection(Vector2 current, Vector2 next) {
		if (next.y > current.y) {
			return Vector2.down();
		} else if (next.x < current.x) {
			return Vector2.left();
		} else if (next.x > current.x) {
			return Vector2.right();
		} else if (next.y < current.y) {
			return Vector2.up();
		} else {
			return Vector2.zero();
		}
	}

	public static Vector2 align(Vector2 current, Vector2 next) {
		Vector2 res = null;
		double minDist = Double.MAX_VALUE;

		for (int x = -GS.Config.BLOCK_SIZE; x <= GS.Config.BLOCK_SIZE; x += GS.Config.BLOCK_SIZE) {
			Vector2 tmp = new Vector2(next.x + x, next.y);
			int tileIndex = GameManager.getInstance().getTileMap()
					.getTileIndex(tmp.y / GS.Config.BLOCK_SIZE, tmp.x / GS.Config.BLOCK_SIZE);
			Tile tile = GameManager.getInstance().getTileMap().getTile(tileIndex);
			if (tile.getIsCollision() == false) {
				double curDist = Vector2.distance(current, tmp);
				if (curDist < minDist) {
					minDist = curDist;
					res = tmp;
				}
			}
		}

		for (int y = -GS.Config.BLOCK_SIZE; y <= GS.Config.BLOCK_SIZE; y += GS.Config.BLOCK_SIZE) {
			Vector2 tmp = new Vector2(next.x, next.y + y);
			int tileIndex = GameManager.getInstance().getTileMap()
					.getTileIndex(tmp.y / GS.Config.BLOCK_SIZE, tmp.x / GS.Config.BLOCK_SIZE);
			Tile tile = GameManager.getInstance().getTileMap().getTile(tileIndex);
			if (tile.getIsCollision() == false) {
				double curDist = Vector2.distance(current, tmp);
				if (curDist < minDist) {
					minDist = curDist;
					res = tmp;
				}
			}

		}

		return res;
	}
}
