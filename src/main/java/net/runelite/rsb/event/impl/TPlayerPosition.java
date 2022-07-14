package net.runelite.rsb.event.impl;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.TextPaintListener;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.util.StringUtil;
import net.runelite.rsb.wrappers.RSTile;

import java.awt.*;

public class TPlayerPosition implements TextPaintListener {
	private MethodContext ctx;

	public TPlayerPosition(BotLite bot) {
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
