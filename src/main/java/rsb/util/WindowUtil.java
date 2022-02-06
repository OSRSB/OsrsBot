package rsb.util;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window and dialog utilities.
 *
 * @author Enfilade
 */
public class WindowUtil {

	/**
	 * Returned from showConfirmDialog when the user hits the "Yes" button.
	 */
	public final static int YES_OPTION = JOptionPane.YES_OPTION;

	/**
	 * Returned from showConfirmDialog when the user hits the "No" button.
	 */
	public final static int NO_OPTION = JOptionPane.NO_OPTION;

	/**
	 * Returned from showConfirmDialog when the user hits the "Cancel" button.
	 */
	public final static int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;

	/**
	 * Returned from showConfirmDialog when the user hits the "OK" button.
	 */
	public final static int OK_OPTION = JOptionPane.OK_OPTION;

	/**
	 * Returned from showConfirmDialog when the user closes the dialog.
	 */
	public final static int DIALOG_CLOSED = JOptionPane.CLOSED_OPTION;

	/**
	 * Use this for a confirm dialog with options "Yes", "No", and "Cancel" (in that order)
	 */
	public final static int YES_NO_CANCEL = JOptionPane.YES_NO_CANCEL_OPTION;

	/**
	 * Use this for a confirm dialog with options "Yes" and "No" (in that order)
	 */
	public final static int YES_NO = JOptionPane.YES_NO_OPTION;

	/**
	 * Use this for a confirm dialog with options "Ok" and "Cancel" (in that order)
	 */
	public final static int OK_CANCEL = JOptionPane.OK_CANCEL_OPTION;

	private static JFrame frame = null;

	/**
	 * Sets the frame that WindowUtil will use. This method only sets it
	 * on the first call, to prevent tampering.
	 *
	 * @param f The JFrame that WindowUtil should use.
	 */
	public static void setFrame(JFrame f) {
		if (frame == null) {
			frame = f;
		}
	}

	/**
	 * Shows a dialog window that appears centered in front of the Bot window
	 * that can be used to alert the user.
	 *
	 * This dialog will keep focus until the user closes it.
	 *
	 * @param message The message to be shown within the dialog.
	 */
	public static void showDialog(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}

	/**
	 * Shows an input dialog with the given message. Text entered into the
	 * dialog box will be returned.
	 *
	 * This dialog will keep focus until the user hits OK or the user
	 * closes it.
	 *
	 * @param message The message to be shown in the dialog.
	 * @return A string containing input the user entered into the box, or null if the user
	 *         hit "Cancel" or closed the dialog.
	 */
	public static String showInputDialog(String message) {
		return JOptionPane.showInputDialog(frame, message);
	}

	/**
	 * Shows a confirm dialog with "Yes", "No", and "Cancel" buttons.
	 *
	 * @param message The message to appear in the dialog.
	 * @return an int representing the selected value. This value will be:
	 *         JOptionPane. This can be any of the following:
	 *         YES_OPTION - if the "Yes" option was pressed.
	 *         NO_OPTION - if the "No" option was pressed.
	 *         CANCEL_OPTION - if the "Cancel" option was pressed.
	 *         DIALOG_CLOSED - if the user closed the dialog.
	 */
	public static int showConfirmDialog(String message) {
		return JOptionPane.showConfirmDialog(frame, message);
	}

	/**
	 * Shows a confirm dialog with the specified buttons.
	 *
	 * This dialog will keep focus until the user closes it.
	 *
	 * @param message The message to be displayed in the dialog.
	 * @param type    The type of window. This can be any of the following:
	 *                YES_NO_CANCEL - for a dialog that has "Yes", "No", and "Cancel" options.
	 *                YES_NO - for a dialog that has "Yes" and "No" options.
	 *                OK_CANCEL - for a dialog that has "Ok" and "Cancel" options.
	 * @return an int specifying what option was pressed. This can be any of the following:
	 *         YES_OPTION - if the "Yes" option was pressed.
	 *         NO_OPTION - if the "No" option was pressed.
	 *         OK_OPTION - if the "Ok" option was pressed.
	 *         CANCEL_OPTION - if the "Cancel" option was pressed.
	 *         DIALOG_CLOSED - if the dialog was closed without an option being pressed.
	 */
	public static int showConfirmDialog(String message, int type) {
		return JOptionPane.showConfirmDialog(frame, message, "Confirm", type);
	}

	/**
	 * Centers a JFrame so that its position is in the center of
	 * the window.
	 *
	 * @param f The JFrame to be positioned.
	 */
	public static void position(JFrame f) {
		f.setLocationRelativeTo(frame);
	}

	/**
	 * Gets the window's point on screen.
	 *
	 * @return a Point representing its point on screen.
	 */
	public static Point getWindowLocation() {
		return frame.getLocationOnScreen();
	}

	/**
	 * Minimizes the window.
	 */
	public static void minimizeWindow() {
		frame.setState(JFrame.ICONIFIED);
	}

	/**
	 * Restores the window (if it is minimized).
	 */
	public static void restoreWindow() {
		frame.setState(JFrame.NORMAL);
	}

	/**
	 * "Unmaximizes" the window. This is the same thing as clicking
	 * the "Restore Down" button on Windows when the window is maximized.
	 */
	public static void unmaximizeWindow() {
		frame.setExtendedState(JFrame.NORMAL);
	}

	/**
	 * Maximizes the window.
	 */
	public static void maximizeWindow() {
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}


}