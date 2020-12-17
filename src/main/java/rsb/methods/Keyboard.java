package rsb.methods;

/**
 * Keyboard related operations.
 */
public class Keyboard extends MethodProvider {

	Keyboard(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Presses and releases a given key.
	 *
	 * @param c The character to press.
	 */
	public void sendKey(final char c) {
		methods.inputManager.sendKey(c);
	}

	/**
	 * Types a given string.
	 *
	 * @param text       The text to press/send.
	 * @param pressEnter <tt>true</tt> to press enter after pressing the text.
	 */
	public void sendText(final String text, final boolean pressEnter) {
		methods.inputManager.sendKeys(text, pressEnter);
	}

	/**
	 * Types a given string instantly.
	 *
	 * @param text       The text to press/send.
	 * @param pressEnter <tt>true</tt> to press enter after pressing the text.
	 */
	public void sendTextInstant(final String text, final boolean pressEnter) {
		methods.inputManager.sendKeysInstant(text, pressEnter);
	}

	/**
	 * Presses and holds a given key.
	 *
	 * @param c The character to press.
	 * @see #releaseKey(char)
	 */
	public void pressKey(final char c) {
		methods.inputManager.pressKey(c);
	}

	/**
	 * Releases a given held key.
	 *
	 * @param c The character to release.
	 * @see #pressKey(char)
	 */
	public void releaseKey(final char c) {
		methods.inputManager.releaseKey(c);
	}
}
