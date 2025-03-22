package Model.Core;

import java.util.ArrayList;
import java.util.List;

public abstract class MonoBehaviour {
    private static final List<MonoBehaviour> instances = new ArrayList<>();
    private boolean enabled = true;

    public MonoBehaviour() {
        instances.add(this);
        awake();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void awake() {} // Gọi khi khởi tạo

    public void start() {} // Gọi khi game bắt đầu

    public void update() {} // Gọi mỗi frame

    public void render() {}

    public void onDestroy() {} // Gọi khi object bị xóa

    // Chạy vòng lặp Update()
    public static void runGameLoop() {
        for (MonoBehaviour instance : instances) {
            if (instance.isEnabled()) {
                instance.update();
            }
        }
    }

    public static void destroy(MonoBehaviour obj) {
        obj.onDestroy();
        instances.remove(obj);
    }
}
