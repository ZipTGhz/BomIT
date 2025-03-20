
package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import Model.IHash;

public class Input implements KeyListener {
	private static Set<Integer> pressedKeys = new HashSet<>();
	private static int lastKeyPressed = -1;

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		pressedKeys.add(e.getKeyCode());
		lastKeyPressed = e.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys.remove(e.getKeyCode());
	}

	public static int GetAxisRaw(String axisName) {
		int result = 0;
		switch (axisName) {
		case IHash.InputDirection.HORIZONTAL:
			if (pressedKeys.contains(KeyEvent.VK_A))
				result -= 1;
			if (pressedKeys.contains(KeyEvent.VK_D))
				result += 1;
			break;
		case IHash.InputDirection.VERTICAL:
			if (pressedKeys.contains(KeyEvent.VK_W))
				result -= 1;
			if (pressedKeys.contains(KeyEvent.VK_S))
				result += 1;
			break;
		default:
			break;
		}
		return result;
	}

	public static int getLastKeyPressed() {
		return lastKeyPressed;
	}

	public static boolean GetKey(int keyCode) {
		return pressedKeys.contains(keyCode);
	}
}
