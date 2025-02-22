package Model;

public interface IHash {
    public interface SpriteState {
        // Cho các khối
        final String BLOCK = "BLOCK";

        // Cho nhân vật
        final String MOVE_LEFT = "MOVE_LEFT";
        final String MOVE_RIGHT = "MOVE_RIGHT";
        final String MOVE_UP = "MOVE_UP";
        final String MOVE_DOWN = "MOVE_DOWN";

        final String IDLE_LEFT = "IDLE_LEFT";
        final String IDLE_RIGHT = "IDLE_RIGHT";
        final String IDLE_UP = "IDLE_UP";
        final String IDLE_DOWN = "IDLE_DOWN";
    }
}
