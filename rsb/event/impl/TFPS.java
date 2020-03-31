package net.runelite.client.rsb.event.impl;

import net.runelite.api.Client;
import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.event.listener.TextPaintListener;
import net.runelite.client.rsb.util.StringUtil;

import java.awt.*;

public class TFPS implements TextPaintListener {

	private int fps;
	public TFPS(RuneLite bot) {
		fps = bot.getClient().getFPS();
	}

	public int drawLine(final Graphics render, int idx) {
		StringUtil.drawLine(render, idx++, String.format("%2d fps", fps));
		return idx;
	}
}
