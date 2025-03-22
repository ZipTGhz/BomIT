package Interfaces;

/** GS = Game Settings */
public interface IGS {

	final int BLOCK_SIZE = 48;
	final int GAME_WIDTH = BLOCK_SIZE * 22, GAME_HEIGHT = BLOCK_SIZE * 15;
	final int FPS = 60;
	final double DELTA_TIME = 1.0 / FPS;

	// final int OFFSET_X = 128, OFFSET_Y = 20;
	final double EPSILON = 1e-6;

	
}
