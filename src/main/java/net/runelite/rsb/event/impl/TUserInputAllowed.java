package net.runelite.rsb.event.impl;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.TextPaintListener;
import net.runelite.rsb.util.StringUtil;
import java.awt.*;

public class TUserInputAllowed implements TextPaintListener {

	private final BotLite bot;

	public TUserInputAllowed(BotLite bot) {
		this.bot = bot;
	}

	public int drawLine(final Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, "User Input: " +
				(bot.inputFlags == 0 && !bot.overrideInput ?
						"[red]Disabled (" + bot.inputFlags + ")" : "[green]Enabled"));
		return idx;
	}
}
