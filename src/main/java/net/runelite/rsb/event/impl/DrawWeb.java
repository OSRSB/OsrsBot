package net.runelite.rsb.event.impl;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.PaintListener;
import net.runelite.rsb.internal.wrappers.TileFlags;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.methods.Web;
import net.runelite.rsb.wrappers.RSPlayer;
import net.runelite.rsb.wrappers.RSTile;

import java.awt.*;
import java.util.Map;

public class DrawWeb implements PaintListener {

	private final MethodContext ctx;

	private Point tileToMap(final RSTile tile, final RSPlayer player) {
		double minimapAngle = -1 * Math.toRadians(ctx.camera.getAngle());
		int x = (tile.getLocalLocation(ctx).getX() - player.getLocation().getLocalLocation(ctx).getX()) * 4 - 2;
		int y = (player.getLocation().getLocalLocation(ctx).getY() - tile.getLocalLocation(ctx).getY()) * 4 - 2;
		return new Point((int) Math.round(x * Math.cos(minimapAngle) + y * Math.sin(minimapAngle) + 628), (int) Math.round(y * Math.cos(minimapAngle) - x * Math.sin(minimapAngle) + 87));
	}

	public DrawWeb(BotLite bot) {
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
		for (Map.Entry<RSTile, TileFlags> rsTileTileFlagsEntry : Web.map.entrySet()) {
			TileFlags t = rsTileTileFlagsEntry.getValue();
			render.setColor(t.isWalkable() ? t.isQuestionable() ? Color.yellow : Color.green : t.isWater() ? Color.cyan : Color.red);
			Point p = tileToMap(t.getTile(), player);
			render.drawLine(p.x, p.y, p.x, p.y);
		}
	}
}