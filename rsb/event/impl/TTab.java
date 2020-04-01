package net.runelite.client.rsb.event.impl;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.event.listener.TextPaintListener;
import net.runelite.client.rsb.methods.Game;
import net.runelite.client.rsb.util.StringUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import static net.runelite.client.rsb.methods.Game.TABS;

public class TTab implements TextPaintListener {

	private final Game game;

	public TTab(RuneLite bot) {
		game = bot.getMethodContext().game;
	}

	public int drawLine(final Graphics render, int idx) {
		final int cTab = game.getCurrentTab();
		StringUtil.drawLine(render, idx++,
				//Ints.asList(Game.TABS).indexOf lets us actually find the object with the value of cTab rather than the obvious array out of bounds you'd normally get
				"Current Tab: " + cTab + (cTab != -1 ? " (" + Game.TAB_NAMES[(Ints.asList(Game.TABS).indexOf(cTab))] + ")" : ""));
		return idx;
	}

}
