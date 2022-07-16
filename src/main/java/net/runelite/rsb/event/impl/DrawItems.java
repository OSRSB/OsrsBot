package net.runelite.rsb.event.impl;

import net.runelite.api.Point;
import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.PaintListener;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.wrappers.RSGroundItem;
import net.runelite.rsb.wrappers.RSModel;
import net.runelite.rsb.wrappers.RSPlayer;
import net.runelite.rsb.wrappers.RSTile;

import java.awt.*;

public class DrawItems implements PaintListener {

	private final MethodContext ctx;

	public DrawItems(BotLite bot) {
		ctx = bot.getMethodContext();
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
		final int locX = location.getTile(ctx).getWorldLocation().getX();
		final int locY = location.getTile(ctx).getWorldLocation().getY();
		final int locPlane = location.getTile(ctx).getPlane();
		final int tHeight = metrics.getHeight();
		for (int x = locX - 25; x < locX + 25; x++) {
			for (int y = locY - 25; y < locY + 25; y++) {
				final Point screen = ctx.calc.tileToScreen(new RSTile(x, y, locPlane));
				if (screen == null || !ctx.calc.pointOnScreen(screen)) {
					continue;
				}
				final RSGroundItem[] items = ctx.groundItems.getAllAt(x, y);
				if (items.length > 0) {
					RSModel model = items[0].getModel();
					if (model != null) {
						render.setColor(Color.BLUE);
						for (Polygon polygon : model.getTriangles()) {
							render.drawPolygon(polygon);
						}
					}
				}
				for (int i = 0; i < items.length; i++) {
					render.setColor(Color.RED);
					render.fillRect((int) screen.getX() - 1, (int) screen.getY() - 1, 2, 2);
					final String s = "" + items[i].getItem().getID();
					final int ty = screen.getY() - tHeight * (i + 1) + tHeight / 2;
					final int tx = screen.getX() - metrics.stringWidth(s) / 2;
					render.setColor(Color.green);
					render.drawString(s, tx, ty);
				}
			}
		}
	}
}
