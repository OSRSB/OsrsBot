package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.util.StringUtil;
import java.awt.*;

public class TUserInputAllowed implements TextPaintListener {

	private final RuneLite bot;

	public TUserInputAllowed(RuneLite bot) {
		this.bot = bot;
	}

	public int drawLine(final Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, "User Input: " +
				(bot.inputFlags == 0 && !bot.overrideInput ?
						"[red]Disabled (" + bot.inputFlags + ")" : "[green]Enabled"));
		return idx;
	}
}
