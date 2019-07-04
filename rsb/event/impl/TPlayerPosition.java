package net.runelite.client.rsb.event.impl;

import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.event.listener.TextPaintListener;
import net.runelite.client.rsb.methods.Players;
import net.runelite.client.rsb.util.StringUtil;
import net.runelite.client.rsb.wrappers.RSTile;

import java.awt.*;

public class TPlayerPosition implements TextPaintListener {

	private final Players players;

	public TPlayerPosition(RuneLite bot) {
		players = bot.getMethodContext().players;
	}

	public int drawLine(final Graphics render, int idx) {
		final RSTile position = players.getMyPlayer().getLocation();
		StringUtil.drawLine(render, idx++, "Position: " + position);
		return idx;
	}

}
