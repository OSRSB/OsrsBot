package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.PaintListener;
import rsb.internal.wrappers.TileFlags;
import rsb.methods.MethodContext;
import rsb.methods.Web;
import rsb.wrappers.RSPlayer;
import rsb.wrappers.RSTile;

import java.awt.*;
import java.util.Iterator;
import java.util.Map;

public class DrawWeb implements PaintListener {

	private final MethodContext ctx;

	private Point tileToMap(final RSTile tile, final RSPlayer player) {
		double minimapAngle = -1 * Math.toRadians(ctx.camera.getAngle());
		int x = (tile.getLocalLocation(ctx).getX() - player.getLocation().getLocalLocation(ctx).getX()) * 4 - 2;
		int y = (player.getLocation().getLocalLocation(ctx).getY() - tile.getLocalLocation(ctx).getY()) * 4 - 2;
		return new Point((int) Math.round(x * Math.cos(minimapAngle) + y * Math.sin(minimapAngle) + 628), (int) Math.round(y * Math.cos(minimapAngle) - x * Math.sin(minimapAngle) + 87));
	}

	public DrawWeb(RuneLite bot) {
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
		Iterator<Map.Entry<RSTile, TileFlags>> rs = Web.map.entrySet().iterator();
		while (rs.hasNext()) {
			TileFlags t = rs.next().getValue();
			render.setColor(t.isWalkable() ? t.isQuestionable() ? Color.yellow : Color.green : t.isWater() ? Color.cyan : Color.red);
			Point p = tileToMap(t.getTile(), player);
			render.drawLine(p.x, p.y, p.x, p.y);
		}
	}
}