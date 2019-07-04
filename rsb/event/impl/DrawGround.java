package net.runelite.client.rsb.event.impl;

import net.runelite.api.Point;
import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.event.listener.PaintListener;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.wrappers.RSGroundItem;
import net.runelite.client.rsb.wrappers.RSPlayer;
import net.runelite.client.rsb.wrappers.RSTile;

import java.awt.*;

public class DrawGround implements PaintListener {

	private final MethodContext ctx;

	public DrawGround(RuneLite bot) {
		this.ctx = bot.getMethodContext();
	}

	public void onRepaint(final Graphics render) {
		if (!ctx.game.isLoggedIn()) {
			return;
		}
		final RSPlayer player = ctx.players.getMyPlayer();
		if (player == null) {
			return;
		}
		render.setColor(Color.WHITE);
		final RSTile location = player.getLocation();
		for (int x = location.getWorldLocation().getX() - 25; x < location.getWorldLocation().getX() + 25; x++) {
			for (int y = location.getWorldLocation().getY() - 25; y < location.getWorldLocation().getY() + 25; y++) {
				final RSGroundItem[] item = ctx.groundItems.getAllAt(x, y);
				if ((item == null) || (item.length == 0)) {
					continue;
				}
				final Point screen = ctx.calc.tileToScreen(item[0].getLocation());
				if (ctx.calc.pointOnScreen(screen)) {
					render.drawString("" + item[0].getItem().getID(), location.getWorldLocation().getX() - 10, location.getWorldLocation().getY());
				}
			}
		}
	}
}
