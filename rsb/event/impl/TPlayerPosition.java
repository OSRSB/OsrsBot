package net.runelite.client.rsb.event.impl;

import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.event.listener.TextPaintListener;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.methods.Players;
import net.runelite.client.rsb.util.StringUtil;
import net.runelite.client.rsb.wrappers.RSTile;

import java.awt.*;

public class TPlayerPosition implements TextPaintListener {

	private MethodContext ctx;

	public TPlayerPosition(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	public int drawLine(final Graphics render, int idx) {
		if (ctx.client.getLocalPlayer() != null) {
			final RSTile position = ctx.players.getMyPlayer().getLocation();
			StringUtil.drawLine(render, idx++, "Player " + position.getWorldLocation().toString());
			StringUtil.drawLine(render, idx++, "Player " + position.getLocalLocation(ctx).toString());
			return idx;
		}
		return idx+2;
	}

}
