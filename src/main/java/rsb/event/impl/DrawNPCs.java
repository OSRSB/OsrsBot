package rsb.event.impl;

import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import rsb.botLauncher.RuneLite;
import rsb.event.listener.PaintListener;
import rsb.methods.MethodContext;
import rsb.wrappers.RSNPC;

import java.awt.*;

public class DrawNPCs implements PaintListener {

	private final MethodContext ctx;

	public DrawNPCs(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	public void onRepaint(final Graphics render) {
		if (!ctx.game.isLoggedIn()) {
			return;
		}

		final FontMetrics metrics = render.getFontMetrics();
		for (RSNPC npc : ctx.npcs.getAll()) {
			final Point location = ctx.calc.tileToScreen(npc.getLocation(), npc.getHeight());
			//Point location = npc.getScreenLocation();
			if (location == null || !ctx.calc.pointOnScreen(location)) {
				continue;
			}
			render.setColor(Color.RED);
			render.fillRect((int) location.getX() - 1, (int) location.getY() - 1, 2, 2);
			String s = "" + npc.getID();
			render.setColor(npc.isInCombat() ? Color.red :/* npc.isMoving() ? Color.green :*/ Color.WHITE);
			render.drawString(s, location.getX() - metrics.stringWidth(s) / 2, location.getY() - metrics.getHeight() / 2);
			// int x = element.getX();
			// x -= ((int)(x >> 7)) << 7;
			if (npc.getAnimation() != -1 || npc.getGraphic() != -1) {
				s = "(A: " + npc.getAnimation() + " | G: " + npc.getGraphic() + " | L: " + npc.getLevel() +")";
				render.drawString(s, location.getX() - metrics.stringWidth(s) / 2, location.getY() - metrics.getHeight() * 3 / 2);
			}
			// s = "" + element.isMoving();
			// render.drawString(s, location.x - metrics.stringWidth(s) / 2,
			// location.y - metrics.getHeight() * 5 / 2);
		}
	}
}
