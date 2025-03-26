package Interfaces;

public interface IHash {
    public interface CharacterState {
        final String MOVE_LEFT = "MOVE_LEFT";
        final String MOVE_RIGHT = "MOVE_RIGHT";
        final String MOVE_UP = "MOVE_UP";
        final String MOVE_DOWN = "MOVE_DOWN";

        final String IDLE_LEFT = "IDLE_LEFT";
        final String IDLE_RIGHT = "IDLE_RIGHT";
        final String IDLE_UP = "IDLE_UP";
        final String IDLE_DOWN = "IDLE_DOWN";
    }

    public interface BombState {
        final String IDLE = "IDLE";
        final String EXPLOSION = "EXPLOSION";
    }

    public interface InputDirection {
        final String HORIZONTAL = "HORIZONTAL";
        final String VERTICAL = "VERTICAL";
    }

    public interface SpriteName {
        final String HEART = "HEART";
        final String BOMB = "BOMB";
        final String SPEED = "SPEED";
    }
}
