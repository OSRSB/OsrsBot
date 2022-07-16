package net.runelite.rsb.event.impl;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.TextPaintListener;
import net.runelite.rsb.methods.Game;
import net.runelite.rsb.util.StringUtil;

import java.awt.*;

public class TFloorHeight implements TextPaintListener {

	private final Game game;

	public TFloorHeight(BotLite bot) {
		game = bot.getMethodContext().game;
	}

	public int drawLine(final Graphics render, int idx) {
		final int floor = game.getPlane();
		StringUtil.drawLine(render, idx++, "Floor " + floor);
		return idx;
	}

}
