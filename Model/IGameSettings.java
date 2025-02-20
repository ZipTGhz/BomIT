package Model;

public interface IGameSettings {
	public interface Config {
		final int GAME_WIDTH = 1024, GAME_HEIGHT = 768;
		final int FPS = 120;
	}

	enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
}
