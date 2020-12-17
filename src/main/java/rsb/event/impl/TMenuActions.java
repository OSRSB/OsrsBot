package rsb.event.impl;

import net.runelite.api.MenuEntry;
import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.methods.Menu;
import rsb.util.StringUtil;

import java.awt.*;

public class TMenuActions implements TextPaintListener {

	private final Menu menu;

	public TMenuActions(RuneLite bot) {
		menu = bot.getMethodContext().menu;
	}

	public int drawLine(final Graphics render, int idx) {
		final MenuEntry[] items = menu.getEntries();
		int i = 0;
		for (final MenuEntry item : items) {
			StringUtil.drawLine(render, idx++, i++ + ": [red]" + item.getOption() + " " + item.getTarget());
		}
		return idx;
	}
}
