package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.methods.MethodContext;
import rsb.util.StringUtil;

import java.awt.*;

public class TMenu implements TextPaintListener {

	private final MethodContext ctx;

	public TMenu(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	public int drawLine(Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, "Menu " + (ctx.menu.isOpen() ? "Open" : "Closed"));
		StringUtil.drawLine(render, idx++, "Menu Location: (" +
				(ctx.menu.isOpen() ? (ctx.menu.getLocation().toString()) : "(0, 0)") + ")");
		return idx;
	}
}
