package rsb.event.impl;

import net.runelite.api.Point;
import rsb.botLauncher.RuneLite;
import rsb.event.listener.PaintListener;
import rsb.methods.MethodContext;
import rsb.wrappers.RSCharacter;
import rsb.wrappers.RSGroundItem;
import rsb.wrappers.RSModel;
import rsb.wrappers.RSObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

/**
 * @author GigiaJ
 * 
 */
public class DrawModel implements PaintListener, MouseListener {

	private static final HashMap<RSObject.Type, Color> color_map = new HashMap<RSObject.Type, Color>();

	static {
		color_map.put(RSObject.Type.GROUND, Color.BLACK);
		color_map.put(RSObject.Type.DECORATIVE, Color.YELLOW);
		color_map.put(RSObject.Type.GAME, Color.WHITE);
		color_map.put(RSObject.Type.WALL, Color.GRAY);
	}

	private static final String[] OPTIONS = {"Objects", "Players", "NPCs", "Piles"};
	private static boolean[] enabled = {true, true, true, true};

	private MethodContext ctx;

	public DrawModel(RuneLite bot) {
		this.ctx = bot.getMethodContext();
	}

	public void onRepaint(Graphics render) {
		drawRect(render);
		if (enabled[0]) {
			for (RSObject o : ctx.objects.getAll()) {
				RSModel model = o.getModel();
				if (model != null) {
					render.setColor(color_map.get(o.getType()));
					for (Polygon polygon : model.getTriangles()) {
						render.drawPolygon(polygon);
					}
					render.setColor(Color.GREEN);
					Point p = model.getPoint();
					render.fillOval(p.getX() - 1, p.getY() - 1, 2, 2);
				}
			}
		}
		if (enabled[1]) {
			for (RSCharacter c : ctx.players.getAll()) {
				RSModel model = c.getModel();
				if (model != null) {
					render.setColor(Color.RED);
					for (Polygon polygon : model.getTriangles()) {
						render.drawPolygon(polygon);
					}
				}
			}
		}
		if (enabled[2]) {
			for (RSCharacter c : ctx.npcs.getAll()) {
				RSModel model = c.getModel();
				if (model != null) {
					render.setColor(Color.MAGENTA);
					for (Polygon polygon : model.getTriangles()) {
						render.drawPolygon(polygon);
					}
				}
			}
		}
		if (enabled[3]) {
			for (RSGroundItem item : ctx.groundItems.getAll()) {
				RSModel model = item.getModel();
				if (model != null) {
					render.setColor(Color.CYAN);
					for (Polygon polygon : model.getTriangles()) {
						render.drawPolygon(polygon);
					}
				}
			}
		}
	}

	public final void drawRect(Graphics render) {
		Color j = Color.BLACK;
		Color w = Color.WHITE;
		for (int i = 0; i < OPTIONS.length; i++) {
			int alpha = 150;
			render.setColor(new Color(j.getRed(), j.getGreen(), j.getBlue(), alpha));
			if (enabled[i]) {
				render.setColor(new Color(w.getRed(), w.getGreen(), w.getBlue(), alpha));
			}
			render.fillRect(90 + (80 * i), 3, 80, 12);
			render.setColor(Color.white);
			if (enabled[i]) {
				render.setColor(Color.BLACK);
			}
			render.drawString(OPTIONS[i], 90 + (80 * i) + 10, 13);
			render.setColor(Color.black);
			render.drawRect(90 + (80 * i), 3, 80, 12);
		}
	}

	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i < OPTIONS.length; i++) {
			Rectangle rect = new Rectangle(90 + (80 * i), 3, 80, 12);
			if (rect.contains(e.getPoint())) {
				enabled[i] = !enabled[i];
				e.consume();
				return;
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {

	}
}
