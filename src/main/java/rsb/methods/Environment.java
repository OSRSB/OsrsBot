package rsb.methods;

import rsb.script.Random;
import rsb.script.ScriptManifest;
import rsb.util.ScreenshotUtil;

import java.awt.image.BufferedImage;

/**
 * Bot environment related operations.
 *
 * @author GigiaJ
 */
public class Environment extends MethodProvider {

	public static final int INPUT_MOUSE = 1;
	public static final int INPUT_KEYBOARD = 2;

	public Environment(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Controls the available means of user input when user input is disabled.
	 *
	 * Disable all: <tt>setUserInput(0);</tt>
	 * Enable keyboard only:
	 * <tt>setUserInput(Environment.INPUT_KEYBOARD);</tt>
	 * Enable mouse and keyboard:
	 * <tt>setUserInput(Environment.INPUT_MOUSE | Environment.INPUT_KEYBOARD);</tt>
	 *
	 * @param mask flags indicating which types of input to allow
	 */
	public void setUserInput(int mask) {
		methods.runeLite.getScriptHandler().updateInput(methods.runeLite, mask);
	}

	/**
	 * Takes and saves a screenshot.
	 *
	 * @param hideUsername <tt>true</tt> to cover the player's username; otherwise
	 *                     <tt>false</tt>
	 */
	public void saveScreenshot(boolean hideUsername) {
		ScreenshotUtil.saveScreenshot(methods.runeLite, hideUsername);
	}

	/**
	 * Takes a screenshot.
	 *
	 * @param hideUsername <tt>true</tt> to cover the player's username; otherwise
	 *                     <tt>false</tt>
	 * @return The screen capture image.
	 */
	public BufferedImage takeScreenshot(boolean hideUsername) {
		return ScreenshotUtil.takeScreenshot(methods.runeLite, hideUsername);
	}

	/**
	 * Enables a random event solver.
	 *
	 * @param name the anti-random's (manifest) name (case insensitive)
	 * @return <tt>true</tt> if random was found and set to enabled; otherwise
	 *         <tt>false</tt>
	 */
	public boolean enableRandom(String name) {
		for (final Random random : methods.runeLite.getScriptHandler().getRandoms()) {
			if (random.getClass().getAnnotation(ScriptManifest.class).name().toLowerCase().equals(name.toLowerCase())) {
				if (random.isEnabled()) {
					return true;
				} else {
					random.setEnabled(true);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Disables a random event solver.
	 *
	 * @param name the anti-random's (manifest) name (case insensitive)
	 * @return <tt>true</tt> if random was found and set to disabled; otherwise
	 *         <tt>false</tt>
	 */
	public boolean disableRandom(String name) {
		for (final Random random : methods.runeLite.getScriptHandler().getRandoms()) {
			if (random.getClass().getAnnotation(ScriptManifest.class).name().toLowerCase().equals(name.toLowerCase())) {
				if (!random.isEnabled()) {
					return true;
				} else {
					random.setEnabled(false);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Enables all random event solvers.
	 */
	public void enableRandoms() {
		for (final Random random : methods.runeLite.getScriptHandler().getRandoms()) {
			if (!random.isEnabled()) {
				random.setEnabled(true);
			}
		}
	}

	/**
	 * Disables all randoms.
	 *
	 */
	@Deprecated
	public void disbleRandoms() {
		disableRandoms();
	}

	public void disableRandoms() {
		for (final Random random : methods.runeLite.getScriptHandler().getRandoms()) {
			if (random.isEnabled()) {
				random.setEnabled(false);
			}
		}
	}
}
