package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.methods.MethodContext;
import rsb.methods.Players;
import rsb.util.StringUtil;
import rsb.wrappers.RSTile;

import java.awt.*;

public class TPlayerPosition implements TextPaintListener {

	private MethodContext ctx;

	public TPlayerPosition(RuneLite bot) {
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
