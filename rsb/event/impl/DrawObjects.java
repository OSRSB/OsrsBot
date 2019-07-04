package net.runelite.client.rsb.event.impl;

import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.event.listener.PaintListener;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.wrappers.RSObject;
import net.runelite.client.rsb.wrappers.RSPlayer;
import net.runelite.client.rsb.wrappers.RSTile;
import net.runelite.api.Point;

import java.awt.*;
import java.util.HashMap;

public class DrawObjects implements PaintListener {

	private final MethodContext ctx;

	public DrawObjects(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	private static final HashMap<RSObject.Type, Color> color_map = new HashMap<RSObject.Type, Color>();

	static {
		color_map.put(RSObject.Type.BOUNDARY, Color.BLACK);
		color_map.put(RSObject.Type.FLOOR_DECORATION, Color.YELLOW);
		color_map.put(RSObject.Type.INTERACTABLE, Color.WHITE);
		color_map.put(RSObject.Type.WALL_DECORATION, Color.GRAY);
	}

	public void onRepaint(final Graphics render) {
		if (!ctx.game.isLoggedIn()) {
			return;
		}
		final RSPlayer player = ctx.players.getMyPlayer();
		if (player == null) {
			return;
		}
		final FontMetrics metrics = render.getFontMetrics();
		final RSTile location = player.getLocation();
		final int locX = location.getWorldLocation().getX();
		final int locY = location.getWorldLocation().getY();
		final int tHeight = metrics.getHeight();
		for (int x = locX - 25; x < locX + 25; x++) {
			for (int y = locY - 25; y < locY + 25; y++) {
				RSTile tile = new RSTile(x, y, ctx.client.getPlane());
				final Point screen = ctx.calc.tileToScreen(tile);
				if (!ctx.calc.pointOnScreen(screen)) {
					continue;
				}
				final RSObject[] objects = ctx.objects.getAllAt(tile);
				int i = 0;
				for (RSObject object : objects) {
					Point real = ctx.calc.tileToScreen(object.getLocation());
					if (!ctx.calc.pointOnScreen(real)) {
						continue;
					}
					if (screen.getX() > -1) {
						render.setColor(Color.GREEN);
						render.fillRect(screen.getX() - 1, screen.getY() - 1, 2, 2);
						render.setColor(Color.RED);
						render.drawLine(screen.getX(), screen.getY(), real.getX(), real.getY());
					}
					final String s = "" + object.getID();
					final int ty = real.getY() - tHeight / 2 - (i++) * 15;
					final int tx = real.getX() - metrics.stringWidth(s) / 2;
					render.setColor(color_map.get(object.getType()));
					render.drawString(s, tx, ty);
				}
			}
		}
	}
}
