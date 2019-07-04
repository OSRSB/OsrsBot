package net.runelite.client.rsb.event.impl;

import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.internal.input.VirtualMouse;
import net.runelite.client.rsb.event.listener.TextPaintListener;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.util.StringUtil;

import java.awt.*;

public class TMousePosition implements TextPaintListener {

	private final MethodContext ctx;

	public TMousePosition(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	public int drawLine(final Graphics render, int idx) {
		final VirtualMouse mouse = ctx.virtualMouse;
		if (mouse != null) {
			final int mouse_x = mouse.getClientX();
			final int mouse_y = mouse.getClientY();
			String off = mouse.isClientPresent() ? "" : " (off)";
			StringUtil.drawLine(render, idx++, "Mouse Position: (" + mouse_x + "," + mouse_y + ")" + off);
		}

		return idx;
	}
}
