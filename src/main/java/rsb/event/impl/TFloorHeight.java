package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.TextPaintListener;
import rsb.methods.Game;
import rsb.util.StringUtil;

import java.awt.*;

public class TFloorHeight implements TextPaintListener {

	private final Game game;

	public TFloorHeight(RuneLite bot) {
		game = bot.getMethodContext().game;
	}

	public int drawLine(final Graphics render, int idx) {
		final int floor = game.getPlane();
		StringUtil.drawLine(render, idx++, "Floor " + floor);
		return idx;
	}

}
