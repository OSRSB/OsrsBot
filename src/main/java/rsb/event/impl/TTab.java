package rsb.event.impl;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.methods.Game;
import rsb.util.StringUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import static rsb.methods.Game.TABS;

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
