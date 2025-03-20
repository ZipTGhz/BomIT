package Model;

/** GS = Game Settings */
public interface GS {
	public interface Config {

		final int GAME_WIDTH = 1024, GAME_HEIGHT = 768;
		final int FPS = 80;
		final double DELTA_TIME = 1.0 / FPS;

		// final int OFFSET_X = 128, OFFSET_Y = 20;
		final int BLOCK_SIZE = 48;
		final double EPSILON = 1e-6;
	}
}
