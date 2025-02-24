package Model;

/** GS = Game Settings */
public interface GS {
	public interface Config {
		final int GAME_WIDTH = 1024, GAME_HEIGHT = 768;
		final int FPS = 120;
		final double DELTA_TIME = 1.0 / FPS;

		final int ENTITY_SIZE = 80;
		final int BLOCK_SIZE = 80;
	}
}
