package Components.Back;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import Collections.Vector2;

/**
 * Lớp SpriteRenderer chịu trách nhiệm quản lý và hiển thị các sprite của đối
 * tượng trong game. Hỗ trợ thay đổi trạng thái, hiển thị sprite theo từng khung
 * hình và thiết lập tốc độ thay đổi sprite.
 */
public class SpriteRenderer {
	/** Danh sách trạng thái và các sprite tương ứng. */
	private HashMap<String, ArrayList<BufferedImage>> states;

	/**
	 * Khoảng thời gian nghỉ với mỗi interval frame để vẽ tiếp một hoạt ảnh mới.
	 * <p>
	 * Mặc định cứ 5 frame được vẽ ra sẽ thay đổi một sprite mới.
	 */
	private int interval = 5;

	/** Biến theo dõi số lượng frame đã được vẽ ra (Sử dụng nội bộ). */
	private int frameCount = 0;

	/**
	 * Vị trí của sprite hiện tại trong danh sách sprite của trạng thái hiện tại.
	 * <p>
	 * Giá trị này sẽ đặt lại thành 0 khi vẽ hết tất cả sprite hoặc thay đổi trạng
	 * thái.
	 */
	private int spriteIndex = 0;

	/**
	 * Trạng thái hiện tại của đối tượng, sẽ thay đổi khi thực hiện một hành động cụ
	 * thể.
	 */
	private String currentState = "";

	/** Khởi tạo một SpriteRenderer mới với danh sách trạng thái rỗng. */
	public SpriteRenderer() {
		states = new HashMap<>();
	}

	/**
	 * Thêm một sprite vào trạng thái cụ thể.
	 *
	 * @param stateName Tên của trạng thái cần thêm sprite.
	 * @param sprite    Ảnh cần thêm vào danh sách sprite của trạng thái.
	 */
	public void addSprite(String stateName, BufferedImage sprite) {
		states.computeIfAbsent(stateName, _ -> new ArrayList<>()).add(sprite);
	}

	/**
	 * Thêm nhiều sprite vào trạng thái cụ thể.
	 *
	 * @param stateName Tên của trạng thái cần thêm sprite.
	 * @param sprites   Các ảnh cần thêm vào danh sách sprite của trạng thái.
	 */
	public void addSprite(String stateName, ArrayList<BufferedImage> sprites) {
		states.computeIfAbsent(stateName, _ -> new ArrayList<>()).addAll(sprites);
	}

	/**
	 * Thiết lập khoảng thời gian nghỉ giữa các frame để thay đổi sprite.
	 *
	 * @param interval Số frame cần đợi trước khi chuyển sang sprite tiếp theo.
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * Lấy giá trị khoảng thời gian nghỉ giữa các frame.
	 *
	 * @return Giá trị khoảng thời gian nghỉ.
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * Thay đổi trạng thái hiện tại của sprite renderer.
	 *
	 * @param stateName Tên trạng thái cần thay đổi.
	 * @return {@code true} nếu thay đổi thành công, {@code false} nếu trạng thái
	 *         không tồn tại.
	 */
	public void changeState(String stateName) {
		if (!states.containsKey(stateName) || currentState.equals(stateName)) {
			return;
		}
		currentState = stateName;
		spriteIndex = 0;
	}

	public void forceChangeState(String stateName) {
		if (!states.containsKey(stateName)) {
			return;
		}
		currentState = stateName;
		spriteIndex = 0;
	}

	/**
	 * Hiển thị sprite hiện tại tại vị trí cụ thể.
	 *
	 * @param g             Đối tượng Graphics để vẽ sprite.
	 * @param worldPosition Vị trí vẽ sprite.
	 */
	public void render(Graphics g, Vector2 worldPosition) {
		ArrayList<BufferedImage> sprites = states.get(currentState);
		if (sprites != null && !sprites.isEmpty()) {
			g.drawImage(sprites.get(spriteIndex), worldPosition.x, worldPosition.y, null);

			frameCount++;
			if (frameCount >= interval) {
				spriteIndex = (spriteIndex + 1) % sprites.size();
				frameCount = 0;
			}
		}
	}
}
