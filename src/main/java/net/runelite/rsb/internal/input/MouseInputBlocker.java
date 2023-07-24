package net.runelite.rsb.internal.input;

import lombok.Getter;
import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.internal.ScriptHandler;
import net.runelite.rsb.internal.listener.ScriptListener;
import net.runelite.rsb.script.Script;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class MouseInputBlocker implements MouseListener {
	private final BotLite bot;
	private MouseListener[] mouseListeners;

	@Getter
	boolean enableInput = true;

	public MouseInputBlocker(BotLite bot) {
		this.bot = bot;
	}

	public void setInput(boolean enableInput) {
		if (this.enableInput == enableInput) return;
		this.enableInput = enableInput;
		if (this.enableInput) unblockInput();
		else blockInput();
	}

	public void blockInput() {
		if (bot.client == null) return;
		java.awt.Canvas c = bot.client.getCanvas();
		mouseListeners = c.getMouseListeners();
		Arrays.stream(mouseListeners).forEach(c::removeMouseListener);
		c.addMouseListener(this);
	}

	public void unblockInput() {
		if (bot.client == null) return;
		java.awt.Canvas c = bot.client.getCanvas();
		c.removeMouseListener(this);
		Arrays.stream(mouseListeners).forEach(c::addMouseListener);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!e.getSource().getClass().getName().equals("client")) e.consume();
		for (MouseListener l : mouseListeners) {
			if (e.isConsumed()) break;
			l.mouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!e.getSource().getClass().getName().equals("client")) e.consume();
		for (MouseListener l : mouseListeners) {
			if (e.isConsumed()) break;
			l.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!e.getSource().getClass().getName().equals("client")) e.consume();
		for (MouseListener l : mouseListeners) {
			if (e.isConsumed()) break;
			l.mouseReleased(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (!e.getSource().getClass().getName().equals("client")) e.consume();
		for (MouseListener l : mouseListeners) {
			if (e.isConsumed()) break;
			l.mouseEntered(e);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (!e.getSource().getClass().getName().equals("client")) e.consume();
		for (MouseListener l : mouseListeners) {
			if (e.isConsumed()) break;
			l.mouseExited(e);
		}
	}

}
