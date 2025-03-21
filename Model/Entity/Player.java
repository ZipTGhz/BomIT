package Model.Entity;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Collections.Vector2;
import Controller.CollisionChecker;
import Controller.Input;
import Interfaces.IHash;
import Model.GameManager;

public class Player extends Character {

	public Player(int x, int y, int speed) {
		super(x, y, speed);
		this.health = 3;
	}

	@Override
	public void update() {
		super.update();
		Vector2 curDir = handleMovement();

		if (Input.GetKey(KeyEvent.VK_SPACE))
			this.placeBomb();

		if (handleCollision(curDir))
			return;

		this.move(curDir.x, curDir.y);
	}

	private boolean handleCollision(Vector2 curDir) {
		if (CollisionChecker.checkCharacterCollision(this, curDir))
			return true;

		ArrayList<Bomb> bombs = GameManager.getInstance().getBombs();
		for (Bomb bomb : bombs) {
			if (CollisionChecker.checkBombCollision(this, curDir, bomb))
				return true;
		}
		return false;
	}

	private Vector2 handleMovement() {
		int x = Input.GetAxisRaw(IHash.InputDirection.HORIZONTAL);
		int y = Input.GetAxisRaw(IHash.InputDirection.VERTICAL);
		if (x != 0 && y != 0) {
			int keyCode = Input.getLastKeyPressed();
			x = y = 0;
			switch (keyCode) {
			case KeyEvent.VK_S:
				y = 1;
				break;
			case KeyEvent.VK_A:
				x = -1;
				break;
			case KeyEvent.VK_D:
				x = 1;
				break;
			case KeyEvent.VK_W:
				y = -1;
				break;
			}
		}
		Vector2 curDir = new Vector2(x, y);
		if (curDir.x > 0) {
			sr.changeState(IHash.CharacterState.MOVE_RIGHT);
		} else if (curDir.x < 0) {
			sr.changeState(IHash.CharacterState.MOVE_LEFT);
		} else if (curDir.y > 0) {
			sr.changeState(IHash.CharacterState.MOVE_DOWN);
		} else if (curDir.y < 0) {
			sr.changeState(IHash.CharacterState.MOVE_UP);
		} else {
			int keyCode = Input.getLastKeyPressed();
			switch (keyCode) {
			case KeyEvent.VK_S:
				sr.changeState(IHash.CharacterState.IDLE_DOWN);
				break;
			case KeyEvent.VK_A:
				sr.changeState(IHash.CharacterState.IDLE_LEFT);
				break;
			case KeyEvent.VK_D:
				sr.changeState(IHash.CharacterState.IDLE_RIGHT);
				break;
			case KeyEvent.VK_W:
				sr.changeState(IHash.CharacterState.IDLE_UP);
				break;
			}
		}
		return curDir;
	}

}
