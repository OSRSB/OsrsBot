package rsb.event.impl;

import net.runelite.api.Client;
import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.methods.MethodContext;
import rsb.util.StringUtil;

import java.awt.*;

public class TFPS implements TextPaintListener {

	private MethodContext ctx;

	public TFPS(RuneLite bot) {
		this.ctx = bot.getMethodContext();
	}

	public int drawLine(final Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, String.format("%2d fps", ctx.client.getFPS()));
		return idx;
	}
}
