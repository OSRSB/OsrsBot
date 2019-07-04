package net.runelite.client.rsb.event.impl;

import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.event.listener.TextPaintListener;
import net.runelite.client.rsb.methods.Game;
import net.runelite.client.rsb.util.StringUtil;

import java.awt.*;

public class TTab implements TextPaintListener {

	private final Game game;

	public TTab(RuneLite bot) {
		game = bot.getMethodContext().game;
	}

	public int drawLine(final Graphics render, int idx) {
		final int cTab = game.getCurrentTab();
		StringUtil.drawLine(render, idx++,
				"Current Tab: " + cTab + (cTab != -1 ? " (" + Game.TAB_NAMES[cTab] + ")" : ""));
		return idx;
	}

}
