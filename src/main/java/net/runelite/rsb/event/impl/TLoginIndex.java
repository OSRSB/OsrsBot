package net.runelite.rsb.event.impl;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.TextPaintListener;
import net.runelite.rsb.methods.Game;
import net.runelite.rsb.util.StringUtil;

import java.awt.*;

public class TLoginIndex implements TextPaintListener {

	private final Game game;

	public TLoginIndex(BotLite bot) {
		game = bot.getMethodContext().game;
	}

	public int drawLine(final Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, "Client State: " + game.getClientState());
		return idx;
	}

}
