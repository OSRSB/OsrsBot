package net.runelite.client.rsb.event.impl;

import net.runelite.api.Client;
import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.event.listener.TextPaintListener;
import net.runelite.client.rsb.util.StringUtil;

import java.awt.*;

public class TCamera implements TextPaintListener {

	private final Client client;

	public TCamera(RuneLite bot) {
		client = bot.getClient();
	}

	public int drawLine(final Graphics render, int idx) {
		final String camPos = "Camera Position (x,y,z): (" + client.getCameraX() + ", " + client.getCameraY() + ", " + client.getCameraZ() + ")";
		final String camAngle = "Camera Angle (pitch, yaw): (" + client.getCameraPitch() + ", " + client.getCameraYaw() + ")";
		//final String detailLvl = "Detail lvl: " + (client.getDetailInfo() != null ? client.getDetailInfo().getDetailLevel() : "null");

		StringUtil.drawLine(render, idx++, camPos);
		StringUtil.drawLine(render, idx++, camAngle);
		//Methods.drawLine(render, idx++, detailLvl);
		return idx;
	}
}
