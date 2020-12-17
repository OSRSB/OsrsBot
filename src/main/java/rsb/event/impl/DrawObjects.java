package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.PaintListener;
import rsb.methods.MethodContext;
import rsb.wrappers.RSObject;
import rsb.wrappers.RSPlayer;
import rsb.wrappers.RSTile;
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
		color_map.put(RSObject.Type.GROUND, Color.BLACK);
		color_map.put(RSObject.Type.DECORATIVE, Color.YELLOW);
		color_map.put(RSObject.Type.GAME, Color.WHITE);
		color_map.put(RSObject.Type.WALL, Color.GRAY);
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
		final int locX = location.getTile(ctx).getWorldLocation().getX();//location.getWorldLocation().getX();
		final int locY = location.getTile(ctx).getWorldLocation().getY();//location.getWorldLocation().getY();
		final int locPlane = location.getTile(ctx).getPlane();
		final int tHeight = metrics.getHeight();
		for (int x = locX - 25; x < locX + 25; x++) {
			for (int y = locY - 25; y < locY + 25; y++) {
				RSTile tile = new RSTile(x, y, locPlane);
				final Point screen = ctx.calc.tileToScreen(tile);
				if (screen == null || !ctx.calc.pointOnScreen(screen)) {
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
