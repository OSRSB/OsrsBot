package rsb.internal.input;

import lombok.extern.slf4j.Slf4j;
import rsb.methods.MethodProvider;

import java.awt.event.MouseListener;
import java.awt.*;
import java.awt.event.*;
@Slf4j
public abstract class Mouse extends Focus implements MouseListener, net.runelite.client.input.MouseWheelListener {


	private int clientX;
	private int clientY;
	private int clientPressX = -1;
	private int clientPressY = -1;
	private long clientPressTime = -1;
	private boolean clientPresent;
	private boolean clientPressed;
	private boolean clientInFocus;

	public abstract Component getComponent();

	public int getX() {
		return clientX;
	}

	public int getY() {
		return clientY;
	}

	public int getPressX() {
		return clientPressX;
	}

	public int getPressY() {
		return clientPressY;
	}

	public long getPressTime() {
		return clientPressTime;
	}

	public boolean isPressed() {
		return clientPressed;
	}

	public boolean isPresent() {
		return clientPresent;
	}

	public boolean isInFocus() { return clientInFocus;}

	public void _focusGained(FocusEvent e) {
		clientInFocus = true;
	}

	public void _focusLost(FocusEvent e) {
		clientInFocus = false;
	}

	public final void mouseClicked(MouseEvent e) {
		//
		clientX = e.getX();
		clientY = e.getY();
	}

	public final void mouseDragged(MouseEvent e) {
		clientX = e.getX();
		clientY = e.getY();
	}

	public final void mouseEntered(MouseEvent e) {
		clientPresent = true;
		clientX = e.getX();
		clientY = e.getY();
	}

	public final void mouseExited(MouseEvent e) {
		clientPresent = false;
		clientX = e.getX();
		clientY = e.getY();
	}

	public final void mouseMoved(MouseEvent e) {
		log.debug("X: " + e.getX() + "\nY: " + e.getY() +"\n");
		clientX = e.getX();
		clientY = e.getY();
	}

	public final void mousePressed(MouseEvent e) {
		clientPressed = true;
		clientX = e.getX();
		clientY = e.getY();
	}

	public final void mouseReleased(MouseEvent e) {
		clientX = e.getX();
		clientY = e.getY();
		clientPressX = e.getX();
		clientPressY = e.getY();
		clientPressTime = System.currentTimeMillis();
		clientPressed = false;

		/**
		 * Needs to be able to send it to all instances of the bot and currently
		 * this isn't possible because only one instance of the bot exists
		 * When a bot array is made we will be able to properly perform this action
		 */
		if (isInFocus()) {
			MethodProvider.methods.virtualMouse.sendEvent(e);
		}

	}

	public MouseWheelEvent mouseWheelMoved(MouseWheelEvent e) {
		try {
			return e;
		} catch (AbstractMethodError ame) {
			// it might not be implemented!
		}
		return e;
	}


}
