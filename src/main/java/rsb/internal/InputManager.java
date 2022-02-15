package rsb.internal;

import lombok.extern.slf4j.Slf4j;
import rsb.botLauncher.RuneLite;
import net.runelite.api.Client;
import rsb.internal.input.Canvas;

import java.applet.Applet;
import java.awt.event.*;

@Slf4j
public class InputManager {

	private final java.util.Random random = new java.util.Random();
	private final MouseHandler mouseHandler = new MouseHandler(this);
	private final RuneLite bot;

	private byte dragLength = 0;

	/**
	 * The side of the screen off which the mouse last moved.
	 */
	private int side = random(1, 5);

	public InputManager(RuneLite bot) {
		this.bot = bot;
	}

	private boolean isOnCanvas(final int x, final int y) {
		return x > 0 && x < bot.getCanvas().getWidth() && y > 0 && y < bot.getCanvas().getHeight();
	}

	public void clickMouse(final boolean left) {
		if (!bot.getMethodContext().mouse.isPresent()) {
			return; // Can't click off the canvas
		}
		pressMouse(getX(), getY(), left);
		sleepNoException(random(50, 100));
		releaseMouse(getX(), getY(), left);
	}

	/**
	 * Drag the mouse from the current position to a certain other position.
	 *
	 * @param x the x coordinate to drag to
	 * @param y the y coordinate to drag to
	 */
	public void dragMouse(final int x, final int y) {
		pressMouse(getX(), getY(), true);
		sleepNoException(random(300, 500));
		windMouse(getX(), getY(), x, y);
		sleepNoException(random(300, 500));
		releaseMouse(x, y, true);
	}

	@SuppressWarnings("unused")
	private void gainFocus() {
		final Canvas cw = getCanvasWrapper();
		if (!cw.hasFocus()) {
			cw.setFocused(true);
		}
	}

	private Canvas getCanvasWrapper() {
		return (Canvas) getTarget().getComponent(0);
	}

	private Client getClient() {
		return bot.getClient();
	}

	private char getKeyChar(final char c) {
		if ((c >= 36) && (c <= 40)) {
			return KeyEvent.VK_UNDEFINED;
		} else {
			return c;
		}
	}

	private Applet getTarget() {
		return (Applet) getClient();
	}

	public int getX() {
		return bot.getMethodContext().virtualMouse.getClientX();//getClient().getMouseCanvasPosition().getX();
	}

	public int getY() {
		return bot.getMethodContext().virtualMouse.getClientY();//getClient().getMouseCanvasPosition().getY();
	}

	public void holdKey(final int keyCode, final int ms) {
		KeyEvent ke;
		ke = new KeyEvent(getTarget(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, (char) keyCode);
		bot.getMethodContext().virtualKeyboard.sendEvent(ke);

		if (ms > 500) {
			ke = new KeyEvent(getTarget(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + 500, 0, keyCode,
					(char) keyCode);
			bot.getMethodContext().virtualKeyboard.sendEvent(ke);
			final int ms2 = ms - 500;
			for (int i = 37; i < ms2; i += random(20, 40)) {
				ke = new KeyEvent(getTarget(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + i + 500, 0, keyCode,
						(char) keyCode);
				bot.getMethodContext().virtualKeyboard.sendEvent(ke);
			}
		}
		final int delay2 = ms + random(-30, 30);
		ke = new KeyEvent(getTarget(), KeyEvent.KEY_RELEASED, System.currentTimeMillis() + delay2, 0, keyCode,
				(char) keyCode);
		bot.getMethodContext().virtualKeyboard.sendEvent(ke);
	}

	public void hopMouse(final int x, final int y) {
		moveMouse(x, y);
	}

	@SuppressWarnings("unused")
	private void loseFocus() {
		final Canvas cw = getCanvasWrapper();
		if (cw.hasFocus()) {
			cw.setFocused(false);
		}
	}

	private void moveMouse(final int x, final int y) {
		// Firstly invoke drag events
		if (bot.getMethodContext().mouse.isPressed()) {
			final MouseEvent me = new MouseEvent(getTarget(), MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), 0,
					x, y, 0, false);

			bot.getMethodContext().virtualMouse.sendEvent(me);
			if ((dragLength & 0xFF) != 0xFF) {
				dragLength++;
			}
		}

		if (!bot.getMethodContext().mouse.isPresent()) {
			if (isOnCanvas(x, y)) { // Entered
				final MouseEvent me = new MouseEvent(getTarget(), MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(),
						0, x, y, 0, false);
				bot.getMethodContext().virtualMouse.sendEvent(me);
			}
		} else if (!isOnCanvas(x, y)) {
			final MouseEvent me = new MouseEvent(getTarget(), MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, x,
					y, 0, false);
			bot.getMethodContext().virtualMouse.sendEvent(me);
			int w = bot.getCanvas().getWidth(), h = bot.getCanvas().getHeight(), d = 50;
			if (x < d) {
				if (y < d) {
					side = 4; // top
				} else if (y > h + d) {
					side = 2; // bottom
				} else {
					side = 1; // left
				}
			} else if (x > w) {
				side = 3; // right
			} else {
				side = random(1, 5);
			}
		} else if (!bot.getMethodContext().mouse.isPressed()) {
			final MouseEvent me = new MouseEvent(getTarget(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x,
					y, 0, false);
			bot.getMethodContext().virtualMouse.sendEvent(me);
		}
	}

	/**
	 * @param x       the x value
	 * @param y       the y value
	 * @param randomX x-axis randomness (added to x)
	 * @param randomY y-axis randomness (added to y)
	 */
	public void moveMouse(final int x, final int y, final int randomX, final int randomY) {
		moveMouse(MouseHandler.DEFAULT_MOUSE_SPEED, x, y, randomX, randomY);
	}

	/**
	 * Moves the mouse to the specified point at a certain sped.
	 *
	 * @param speed   the lower, the faster.
	 * @param x       the x value
	 * @param y       the y value
	 * @param randomX x-axis randomness (added to x)
	 * @param randomY y-axis randomness (added to y)
	 */
	public void moveMouse(final int speed, final int x, final int y, final int randomX, final int randomY) {
		int thisX = getX(), thisY = getY();
		if (!isOnCanvas(thisX, thisY)) {
			// on which side of canvas should it enter
			switch (side) {
				case 1:
					thisX = -1;
					thisY = random(0, bot.getCanvas().getHeight());
					break;
				case 2:
					thisX = random(0, bot.getCanvas().getWidth());
					thisY = bot.getCanvas().getHeight() + 1;
					break;
				case 3:
					thisX = bot.getCanvas().getWidth() + 1;
					thisY = random(0, bot.getCanvas().getHeight());
					break;
				case 4:
					thisX = random(0, bot.getCanvas().getWidth());
					thisY = -1;
					break;
			}
		}
		windMouse(x, y);
		//windMouse(speed, thisX, thisY, random(x, x + randomX), random(y, y + randomY));
	}

	public void pressKey(final char ch) {
		KeyEvent ke;
		ke = new KeyEvent(getTarget(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, ch, getKeyChar(ch));
		bot.getMethodContext().virtualKeyboard.sendEvent(ke);
	}

	private void pressMouse(final int x, final int y, final boolean left) {
		if (bot.getMethodContext().mouse.isPressed() || !bot.getMethodContext().mouse.isPresent()) {
			return;
		}
		final MouseEvent me = new MouseEvent(getTarget(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x, y,
				1, false, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
		bot.getMethodContext().virtualMouse.sendEvent(me);
	}

	public int random(final int min, final int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random.nextInt(n));
	}

	public void releaseKey(final char ch) {
		KeyEvent ke;
		ke = new KeyEvent(getTarget(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), InputEvent.ALT_DOWN_MASK, ch,
				getKeyChar(ch));
		bot.getMethodContext().virtualKeyboard.sendEvent(ke);
	}

	private void releaseMouse(final int x, final int y, final boolean leftClick) {
		if (!bot.getMethodContext().mouse.isPressed()) {
			return;
		}
		MouseEvent me = new MouseEvent(getTarget(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x, y, 1,
				false, leftClick ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
		bot.getMethodContext().virtualMouse.sendEvent(me);

		if ((dragLength & 0xFF) <= 3) {
			me = new MouseEvent(getTarget(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x, y, 1, false,
					leftClick ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
			bot.getMethodContext().virtualMouse.sendEvent(me);
		}
		// reset
		dragLength = 0;
	}

	public void sendKey(final char c) {
		sendKey(c, 0);
	}

	private void sendKey(final char ch, final int delay) {
		boolean shift = false;
		int code = ch;
		if ((ch >= 'a') && (ch <= 'z')) {
			code -= 32;
		} else if ((ch >= 'A') && (ch <= 'Z')) {
			shift = true;
		}
		KeyEvent ke;
		if ((code == KeyEvent.VK_LEFT) || (code == KeyEvent.VK_UP) || (code == KeyEvent.VK_DOWN)) {
			ke = new KeyEvent(getTarget(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + delay, 0, code,
					getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
			bot.getMethodContext().virtualKeyboard.sendEvent(ke);
			final int delay2 = random(50, 120) + random(0, 100);
			ke = new KeyEvent(getTarget(), KeyEvent.KEY_RELEASED, System.currentTimeMillis() + delay2, 0, code,
					getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
			bot.getMethodContext().virtualKeyboard.sendEvent(ke);
		} else {
			if (!shift) {
				ke = new KeyEvent(getTarget(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + delay, 0, code,
						getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
				bot.getMethodContext().virtualKeyboard.sendEvent(ke);
				// Event Typed
				ke = new KeyEvent(getTarget(), KeyEvent.KEY_TYPED, System.currentTimeMillis() + 0, 0, 0, ch, 0);
				bot.getMethodContext().virtualKeyboard.sendEvent(ke);
				// Event Released
				final int delay2 = random(50, 120) + random(0, 100);
				ke = new KeyEvent(getTarget(), KeyEvent.KEY_RELEASED, System.currentTimeMillis() + delay2, 0, code,
						getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
				bot.getMethodContext().virtualKeyboard.sendEvent(ke);
			} else {
				// Event Pressed for shift key
				final int s1 = random(25, 60) + random(0, 50);
				ke = new KeyEvent(getTarget(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + s1,
						InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED,
						KeyEvent.KEY_LOCATION_LEFT);
				bot.getMethodContext().virtualKeyboard.sendEvent(ke);

				// Event Pressed for char to send
				ke = new KeyEvent(getTarget(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + delay,
						InputEvent.SHIFT_DOWN_MASK, code, getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
				bot.getMethodContext().virtualKeyboard.sendEvent(ke);
				// Event Typed for char to send
				ke = new KeyEvent(getTarget(), KeyEvent.KEY_TYPED, System.currentTimeMillis() + 0,
						InputEvent.SHIFT_DOWN_MASK, 0, ch, 0);
				bot.getMethodContext().virtualKeyboard.sendEvent(ke);
				// Event Released for char to send
				final int delay2 = random(50, 120) + random(0, 100);
				ke = new KeyEvent(getTarget(), KeyEvent.KEY_RELEASED, System.currentTimeMillis() + delay2,
						InputEvent.SHIFT_DOWN_MASK, code, getKeyChar(ch), KeyEvent.KEY_LOCATION_STANDARD);
				bot.getMethodContext().virtualKeyboard.sendEvent(ke);

				// Event Released for shift key
				final int s2 = random(25, 60) + random(0, 50);
				ke = new KeyEvent(getTarget(), KeyEvent.KEY_RELEASED, System.currentTimeMillis() + s2,
						InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED,
						KeyEvent.KEY_LOCATION_LEFT);
				bot.getMethodContext().virtualKeyboard.sendEvent(ke);
			}
		}
	}

	public void sendKeys(final String text, final boolean pressEnter) {
		sendKeys(text, pressEnter, 50, 100);
	}

	public void sendKeys(final String text, final boolean pressEnter, final int delay) {
		sendKeys(text, pressEnter, delay, delay);
	}

	public void sendKeys(final String text, final boolean pressEnter, final int minDelay, final int maxDelay) {
		final char[] chs = text.toCharArray();
		for (final char element : chs) {
			sendKey(element, random(minDelay, maxDelay));
			sleepNoException(random(minDelay, maxDelay));
		}
		if (pressEnter) {
			sendKey((char) KeyEvent.VK_ENTER, random(minDelay, maxDelay));
		}
	}

	public void sendKeysInstant(final String text, final boolean pressEnter) {
		for (final char c : text.toCharArray()) {
			sendKey(c, 0);
		}
		if (pressEnter) {
			sendKey((char) KeyEvent.VK_ENTER, 0);
		}
	}

	public void sleepNoException(final long t) {
		try {
			Thread.sleep(t);
		} catch (final Exception ignored) {
			log.debug("Sleep exception in input manager", ignored);
		}
	}

	/**
	 * Moves the mouse from a certain point to another at the default speed.
	 *
	 * @param curX    the x value to move from
	 * @param curY    the y value to move from
	 * @param targetX the x value to move to
	 * @param targetY the y value to move to
	 * @see #windMouse(int, int, int, int, int)
	 */
	public void windMouse(final int curX, final int curY, final int targetX, final int targetY) {
		windMouse(MouseHandler.DEFAULT_MOUSE_SPEED, curX, curY, targetX, targetY);
	}

	/**
	 * Moves the mouse from a certain point to another, with specified speed.
	 *
	 * @param speed   the lower, the faster.
	 * @param curX    the x value to move from
	 * @param curY    the y value to move from
	 * @param targetX the x value to move to
	 * @param targetY the y value to move to
	 */
	@Deprecated
	public void windMouse(final int speed, final int curX, final int curY, final int targetX, final int targetY) {
		mouseHandler.moveMouse(speed, curX, curY, targetX, targetY, 0, 0);
	}

	public void windMouse(final int x, final int y) {
		mouseHandler.moveMouse(x, y);
	}

}
