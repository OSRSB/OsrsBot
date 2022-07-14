package net.runelite.rsb.event.impl;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.TextPaintListener;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.methods.Game;
import net.runelite.rsb.util.StringUtil;

import java.awt.*;

public class TTab implements TextPaintListener {
	private final Game game;

	public TTab(BotLite bot) {
		game = bot.getMethodContext().game;
	}

	public int drawLine(final Graphics render, int idx) {
		final InterfaceTab currentTab = game.getCurrentTab();
		StringUtil.drawLine(render, idx++,
				//Ints.asList(Game.TABS).indexOf lets us actually find the object with the value of cTab rather than the obvious array out of bounds you'd normally get
				"Current Tab: " + currentTab + (currentTab != null ? " (" + currentTab.getName() + ")" : ""));
		return idx;
	}
}