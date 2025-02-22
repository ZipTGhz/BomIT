
package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import Collections.Vector2;

public class PlayerController implements KeyListener {
	private Set<Integer> pressedKeys = new HashSet<>();

	private Vector2 _dirState = Vector2.zero();
	private Vector2 _lastInput = Vector2.zero();

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		pressedKeys.add(e.getKeyCode());
		handleDirection();

		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_W) {
			_lastInput = Vector2.up();
		} else if (keyCode == KeyEvent.VK_S) {
			_lastInput = Vector2.down();
		} else if (keyCode == KeyEvent.VK_A) {
			_lastInput = Vector2.left();
		} else if (keyCode == KeyEvent.VK_D) {
			_lastInput = Vector2.right();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys.remove(e.getKeyCode());
		handleDirection();
	}

	private void handleDirection() {
		_dirState = Vector2.zero();
		if (pressedKeys.contains(KeyEvent.VK_W)) {
			_dirState.y -= 1;
		}
		if (pressedKeys.contains(KeyEvent.VK_S)) {
			_dirState.y += 1;
		}
		if (pressedKeys.contains(KeyEvent.VK_A)) {
			_dirState.x -= 1;
		}
		if (pressedKeys.contains(KeyEvent.VK_D)) {
			_dirState.x += 1;
		}
	}

	public Vector2 getDirection() {
		if (_dirState.x != 0 && _dirState.y != 0) {
			return _lastInput;
		}
		return _dirState;
	}

	public Vector2 getLastInput() {
		return _lastInput;
	}
}
