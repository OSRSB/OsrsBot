package rsb.event.impl;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.methods.Game;
import rsb.methods.GameGUI;
import rsb.util.StringUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TTab implements TextPaintListener {

	private final Game game;

	public TTab(RuneLite bot) {
		game = bot.getMethodContext().game;
	}

	public int drawLine(final Graphics render, int idx) {
		final GameGUI.Tab cTab = game.getCurrentTab();
		StringUtil.drawLine(render, idx++,
				//Ints.asList(Game.TABS).indexOf lets us actually find the object with the value of cTab rather than the obvious array out of bounds you'd normally get
				"Current Tab: " + cTab + (cTab != null ? " (" + cTab.getName() + ")" : ""));
		return idx;
	}

}
