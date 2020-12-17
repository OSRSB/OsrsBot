package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.PaintListener;
import rsb.methods.MethodContext;
import rsb.wrappers.RSTile;
import net.runelite.api.Point;

import java.awt.*;

public class DrawBoundaries implements PaintListener {

	private final Point[][] minimapPoints = new Point[105][105];
	private final Point[][] screenPoints = new Point[105][105];

	private final MethodContext ctx;

	public DrawBoundaries(RuneLite bot) {
		this.ctx = bot.getMethodContext();
	}

	public void onRepaint(final Graphics render) {
		if (!ctx.game.isLoggedIn()) {
			return;
		}
		final int blocks[][] = ctx.client.getTileHeights()[ctx.client.getPlane()];
		final int baseX = ctx.client.getBaseX();
		final int baseY = ctx.client.getBaseY();
		for (int i = 0; i < screenPoints.length; i++) {
			for (int j = 0; j < screenPoints[i].length; j++) {
				final int x = i + baseX - 1;
				final int y = j + baseY - 1;
				Point mini = ctx.calc.worldToMinimap(x - 0.5, y - 0.5);
				if ((mini.getX() == -1) || (mini.getY() == -1)) {
					mini = null;
				}
				minimapPoints[i][j] = mini;
				Point screen = ctx.calc.tileToScreen(new RSTile(x, y, ctx.client.getPlane()), 0, 0, 0);
				if ((screen.getX() == -1) || (screen.getY() == -1)) {
					screen = null;
				}
				screenPoints[i][j] = screen;
			}
		}

		render.setColor(Color.YELLOW);
		for (int i = 1; i < 104; i++) {
			for (int j = 1; j < 104; j++) {
				final int curBlock = blocks[i][j];
				final Point miniBL = minimapPoints[i][j];
				final Point miniBR = minimapPoints[i][j + 1];
				final Point miniTL = minimapPoints[i + 1][j];
				final Point miniTR = minimapPoints[i + 1][j + 1];
				final Point bl = screenPoints[i][j];
				final Point br = screenPoints[i][j + 1];
				final Point tl = screenPoints[i + 1][j];
				final Point tr = screenPoints[i + 1][j + 1];
				if ((curBlock & 0x1280100) != 0) {
					render.setColor(Color.black);
					if ((tl != null) && (br != null) && (tr != null) && (bl != null)) {
						render.fillPolygon(new int[]{bl.getX(), br.getX(), tr.getX(), tl.getX()}, new int[]{bl.getY(), br.getY(), tr.getY(), tl.getY()}, 4);
					}
					if ((miniBL != null) && (miniBR != null) && (miniTR != null) && (miniTL != null)) {
						render.fillPolygon(new int[]{miniBL.getX(), miniBR.getX(), miniTR.getX(), miniTL.getX()},
								new int[]{miniBL.getY(), miniBR.getY(), miniTR.getY(), miniTL.getY()}, 4);
					}
				}
				if (((blocks[i][j - 1] & 0x1280102) != 0) || ((curBlock & 0x1280120) != 0)) {
					render.setColor(Color.RED);
					if ((tl != null) && (bl != null)) {
						render.drawLine(bl.getX(), bl.getY(), tl.getX(), tl.getY());
					}
					if ((miniBL != null) && (miniTL != null)) {
						render.drawLine(miniBL.getX(), miniBL.getY(), miniTL.getX(), miniTL.getY());
					}
				}
				if (((blocks[i - 1][j] & 0x1280108) != 0) || ((curBlock & 0x1280180) != 0)) {
					render.setColor(Color.RED);
					if ((br != null) && (bl != null)) {
						render.drawLine(bl.getX(), bl.getY(), br.getX(), br.getY());
					}
					if ((miniBR != null) && (miniBL != null)) {
						render.drawLine(miniBL.getX(), miniBL.getY(), miniBR.getX(), miniBR.getY());
					}
				}
				/*
														 * render.setColor(Color.cyan); if ((curBlock & (1<<20)) != 0) {
														 * if (miniBL != null && miniBR != null && miniTR != null &&
														 * miniTL != null) { render.fillPolygon(new
														 * int[]{miniBL.getX(),miniBR.getX(),miniTR.getX(),miniTL.getX()}, new
														 * int[]{miniBL.getY(),miniBR.getY(),miniTR.getY(),miniTL.getY()},4); } if (tl !=
														 * null && br != null && tr != null && bl != null) {
														 * render.fillPolygon(new int[]{bl.getX(),br.getX(),tr.getX(),tl.getX()}, new
														 * int[]{bl.getY(),br.getY(),tr.getY(),tl.getY()},4); } }
														 */
				// Point miniCent = Calculations.worldToMinimap(i+ baseX, j+
				// baseY);
				// Point cent = Calculations.tileToScreen(i+ baseX, j+ baseY,
				// 0.5,0.5, 0);
				/*
														 * if (cent.getX() != -1 && cent.getY() != -1) {
														 * render.setColor(Color.yellow); render.drawString("" +
														 * Calculations.getRealDistanceTo(cur.getX()-baseX,
														 * cur.getY()-baseY, i, j, false), (int)cent.getX(),
														 * (int)cent.getY()); }
														 */
			}
		}
		final Point mini = ctx.players.getMyPlayer().getMinimapLocation();
		render.setColor(Color.red);
		render.fillRect((int) mini.getX() - 1, (int) mini.getY() - 1, 2, 2);
	}
}
