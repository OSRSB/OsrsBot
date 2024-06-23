package net.runelite.rsb.internal.input;

import lombok.Getter;
import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.internal.ScriptHandler;
import net.runelite.rsb.internal.listener.ScriptListener;
import net.runelite.rsb.script.Script;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

public class MouseMotionBlocker implements MouseMotionListener {

	private final BotLite bot;
	private MouseMotionListener[] mouseMotionListeners;

	@Getter
	private boolean enableInput = true;

	public MouseMotionBlocker(BotLite bot) {
		this.bot = bot;
	}

	public void setInput(boolean enableInput) {
		if (this.enableInput == enableInput) return;
		this.enableInput = enableInput;
		if (enableInput) unblockInput();
		else blockInput();
	}

	public void blockInput() {
		if (bot.client == null) return;
		java.awt.Canvas c = bot.client.getCanvas();
		mouseMotionListeners = c.getMouseMotionListeners();
		Arrays.stream(mouseMotionListeners).forEach(c::removeMouseMotionListener);
		c.addMouseMotionListener(this);
	}

	public void unblockInput() {
		if (bot.client == null) return;
		java.awt.Canvas c = bot.client.getCanvas();
		c.removeMouseMotionListener(this);
		Arrays.stream(mouseMotionListeners).forEach(c::addMouseMotionListener);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!e.getSource().getClass().getName().equals("client")) e.consume();
		for (MouseMotionListener l : mouseMotionListeners) {
			if (e.isConsumed()) break;
			l.mouseDragged(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!e.getSource().getClass().getName().equals("client")) e.consume();
		for (MouseMotionListener l : mouseMotionListeners) {
			if (e.isConsumed()) break;
			l.mouseMoved(e);
		}
	}
}
