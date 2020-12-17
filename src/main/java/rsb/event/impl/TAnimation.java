package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.methods.MethodContext;
import rsb.util.StringUtil;

import java.awt.*;

public class TAnimation implements TextPaintListener {

	private final MethodContext ctx;

	public TAnimation(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	public int drawLine(final Graphics render, int idx) {
		int animation;
		if (ctx.game.isLoggedIn()) {
			animation = ctx.players.getMyPlayer().getAnimation();
		} else {
			animation = -1;
		}
		StringUtil.drawLine(render, idx++, "Animation " + animation);
		return idx;
	}

}
