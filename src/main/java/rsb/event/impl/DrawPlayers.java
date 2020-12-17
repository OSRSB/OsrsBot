package rsb.event.impl;

import net.runelite.api.Point;
import rsb.botLauncher.RuneLite;
import rsb.event.listener.PaintListener;
import rsb.methods.MethodContext;
import rsb.wrappers.RSPlayer;

import java.awt.*;

public class DrawPlayers implements PaintListener {

	private final MethodContext ctx;

	public DrawPlayers(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	public void onRepaint(final Graphics render) {
		if (!ctx.game.isLoggedIn()) {
			return;
		}
		final RSPlayer[] players = ctx.players.getAll();
		if (players == null) {
			return;
		}
		final FontMetrics metrics = render.getFontMetrics();
		for (RSPlayer element : players) {
			if (element == null) {
				continue;
			}
			final RSPlayer player = element;
			final Point location = ctx.calc.tileToScreen(player.getLocation(), player.getHeight());
			if (location == null || !ctx.calc.pointOnScreen(location)) {
				continue;
			}
			render.setColor(Color.RED);
			render.fillRect((int) location.getX() - 1, (int) location.getY() - 1, 2, 2);
			String s = "" + player.getName() + " (" + player.getCombatLevel() + ")";
			render.setColor(player.isInCombat() ? Color.RED : /*player.isMoving() ? Color.GREEN :*/ Color.WHITE);
			render.drawString(s, location.getX() - metrics.stringWidth(s) / 2, location.getY() - metrics.getHeight() / 2);
			String msg = player.getMessage();
			boolean raised = false;
			if (player.getAnimation() != -1 || player.getGraphic() != -1) {

					s = "(A: " + player.getAnimation() + " | L: " + player.getLevel() + " | G: " + player.getGraphic() + ")";

				render.drawString(s, location.getX() - metrics.stringWidth(s) / 2, location.getY() - metrics.getHeight() * 3 / 2);
				raised = true;
			}
			if (msg != null) {
				render.setColor(Color.ORANGE);
				render.drawString(msg, location.getX() - metrics.stringWidth(msg) / 2,
						location.getY() - metrics.getHeight() * (raised ? 5 : 3) / 2);
			}
		}
	}
}
