package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.internal.globval.enums.InterfaceTab;
import rsb.methods.Game;
import rsb.util.StringUtil;

import java.awt.*;

public class TTab implements TextPaintListener {
	private final Game game;

	public TTab(RuneLite bot) {
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