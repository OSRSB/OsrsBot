package net.runelite.rsb.event.impl;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.TextPaintListener;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.util.StringUtil;

import java.awt.*;

public class TFPS implements TextPaintListener {
	private MethodContext ctx;

	public TFPS(BotLite bot) {
		this.ctx = bot.getMethodContext();
	}

	public int drawLine(final Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, String.format("%2d fps", ctx.client.getFPS()));
		return idx;
	}
}