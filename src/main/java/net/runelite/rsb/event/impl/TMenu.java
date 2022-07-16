package net.runelite.rsb.event.impl;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.TextPaintListener;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.util.StringUtil;

import java.awt.*;

public class TMenu implements TextPaintListener {

	private final MethodContext ctx;

	public TMenu(BotLite bot) {
		ctx = bot.getMethodContext();
	}

	public int drawLine(Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, "Menu " + (ctx.menu.isOpen() ? "Open" : "Closed"));
		StringUtil.drawLine(render, idx++, "Menu Location: (" +
				(ctx.menu.isOpen() ? (ctx.menu.getLocation().toString()) : "(0, 0)") + ")");
		return idx;
	}
}
