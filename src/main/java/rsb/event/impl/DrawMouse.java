package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.internal.input.VirtualMouse;
import rsb.event.listener.PaintListener;
import rsb.methods.MethodContext;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DrawMouse implements PaintListener {
	private final MethodContext ctx;
	private final List<Cross> clicks = new LinkedList<Cross>();
	private final Object lock = new Object();

	public DrawMouse(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	private double getRot() {
		return System.currentTimeMillis() % 3600 / 10.0D;
	}

	public void onRepaint(final Graphics render) {
		VirtualMouse mouse = ctx.virtualMouse;
		if (mouse != null) {
			final Point location = new Point(mouse.getClientX(), mouse.getClientY());
			Graphics2D g = (Graphics2D) render.create();
			Graphics2D gg = (Graphics2D) render.create();
			g.setColor(Color.GREEN);
			g.rotate(Math.toRadians(getRot()), location.x, location.y);
			g.drawLine(location.x, location.y - 5, location.x, location.y + 5);
			g.drawLine(location.x - 5, location.y, location.x + 5, location.y);
			if (mouse.isClientPressed() && ((clicks.size() > 0 && clicks.get(clicks.size() - 1).getAge() > 100 && clicks.get(clicks.size() - 1).getStart() != mouse.getClientPressTime()) || clicks.size() == 0)) {
				Cross newCross = new Cross(1500, mouse.getClientPressTime(), location, getRot());
				if (!clicks.contains(newCross)) {
					clicks.add(newCross);
				}
			}
			synchronized (lock) {
				Iterator<Cross> clickIterator = clicks.listIterator();
				while (clickIterator.hasNext()) {
					Cross toDraw = clickIterator.next();
					if (toDraw.handle()) {
						drawPoint(toDraw.getLocation(), toDraw.getRot(), gg, toDraw.getAlpha());
					} else {
						clicks.remove(toDraw);
					}
				}
			}
		}
	}

	private void drawPoint(Point location, double rot, Graphics2D g, int al) {
		Graphics2D g1 = (Graphics2D) g.create();
		g1.setColor(new Color(255, 0, 0, al));
		g1.rotate(rot, location.x, location.y);
		g1.drawLine(location.x, location.y - 5, location.x, location.y + 5);
		g1.drawLine(location.x - 5, location.y, location.x + 5, location.y);
	}

	private class Cross {
		private final long time, st;
		private final Point location;
		private final double rot;

		public Cross(long lifetime, long st, Point loc, double rot) {
			this.time = System.currentTimeMillis() + lifetime;
			location = loc;
			this.rot = rot;
			this.st = st;
		}

		public long getStart() {
			return st;
		}

		public long getAge() {
			return time - System.currentTimeMillis();
		}

		public int getAlpha() {
			return Math.min(255, Math.max(0, (int) (256.0D * (getAge() / 1500.0D))));
		}

		public boolean handle() {
			return System.currentTimeMillis() <= time;
		}

		public double getRot() {
			return rot;
		}

		public Point getLocation() {
			return location;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Cross) {
				Cross oo = (Cross) o;
				return oo.location.equals(this.location);
			}
			return false;
		}
	}
}