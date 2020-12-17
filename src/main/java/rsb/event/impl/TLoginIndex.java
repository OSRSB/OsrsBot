package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.methods.Game;
import rsb.util.StringUtil;

import java.awt.*;

public class TLoginIndex implements TextPaintListener {

	private final Game game;

	public TLoginIndex(RuneLite bot) {
		game = bot.getMethodContext().game;
	}

	public int drawLine(final Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, "Client State: " + game.getClientState());
		return idx;
	}

}
