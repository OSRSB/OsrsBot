package net.runelite.client.rsb.event.impl;

import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.event.listener.TextPaintListener;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.util.StringUtil;

import java.awt.*;

public class TMenu implements TextPaintListener {

	private final MethodContext ctx;

	public TMenu(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	public int drawLine(Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, "Menu " + (ctx.menu.isOpen() ? "Open" : "Closed"));
		StringUtil.drawLine(render, idx++, "Menu Location: (" +
				ctx.menu.getMenuX() + "," + ctx.menu.getMenuY() + ")");
		return idx;
	}
}
