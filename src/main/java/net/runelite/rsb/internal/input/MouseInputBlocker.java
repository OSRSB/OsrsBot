package net.runelite.rsb.internal.input;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.internal.ScriptHandler;
import net.runelite.rsb.internal.listener.ScriptListener;
import net.runelite.rsb.script.Script;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class MouseInputBlocker implements MouseListener, ScriptListener {

	private final BotLite bot;
	private MouseListener[] mouseListeners;

	public MouseInputBlocker(BotLite bot) {
		this.bot = bot;
	}

	private void blockInput() {
		if (bot.client == null) return;
		java.awt.Canvas c = bot.client.getCanvas();
		mouseListeners = c.getMouseListeners();
		Arrays.stream(mouseListeners).forEach(c::removeMouseListener);
		c.addMouseListener(this);
	}

	private void unblockInput() {
		if (bot.client == null) return;
		java.awt.Canvas c = bot.client.getCanvas();
		c.removeMouseListener(this);
		Arrays.stream(mouseListeners).forEach(c::addMouseListener);
	}

	@Override
	public void scriptStarted(ScriptHandler handler, Script script) {
		blockInput();
	}

	@Override
	public void scriptStopped(ScriptHandler handler, Script script) {
		unblockInput();
	}

	@Override
	public void scriptResumed(ScriptHandler handler, Script script) {
		blockInput();
	}

	@Override
	public void scriptPaused(ScriptHandler handler, Script script) {
		unblockInput();
	}

	@Override
	public void inputChanged(BotLite bot, int mask) {

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
