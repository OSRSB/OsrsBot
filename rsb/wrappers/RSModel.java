package net.runelite.client.rsb.wrappers;

import net.runelite.api.Client;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.model.Triangle;
import net.runelite.api.model.Vertex;
import net.runelite.api.Point;
import net.runelite.client.rsb.internal.wrappers.Filter;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.methods.MethodProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * A screen space model.
 *
 * @author Jacmob, SpeedWing
 */
public class RSModel extends MethodProvider {

	/**
	 * Returns a filter that matches against the array of point indices for the
	 * A vertices of each triangle. Use in scripts is discouraged.
	 *
	 * @param vertex_a The array of indices for A vertices.
	 * @return The vertex point index based model filter.
	 */
	public static Filter<RSModel> newVertexFilter(final int[] vertex_a) {
		return new Filter<RSModel>() {
			public boolean accept(RSModel m) {
				return Arrays.equals(m.indices1, vertex_a);
			}
		};
	}

	protected Model model;

	protected int[] xPoints;
	protected int[] yPoints;
	protected int[] zPoints;

	protected int[] indices1;
	protected int[] indices2;
	protected int[] indices3;

	public RSModel(MethodContext ctx, Model model) {
		super(ctx);
		this.model = model;
		xPoints = model.getVerticesX();
		yPoints = model.getVerticesY();
		zPoints = model.getVerticesZ();
		indices1 = model.getTrianglesX();
		indices2 = model.getTrianglesY();
		indices3 = model.getTrianglesZ();
	}

	protected int getLocalX() {
		return -1;
	}

	protected int getLocalY() {
		return -1;
	}

	protected void update(){}

	/**
	 * @param p A point on the screen
	 * @return true of the point is within the bounds of the model
	 */
	private boolean contains(Point p) {
		if (this == null) {
			return false;
		}

		Polygon[] triangles = getTriangles();
		for (Polygon poly : triangles) {
			if (poly.contains(new java.awt.Point(p.getX(), p.getY()))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Clicks the RSModel.
	 *
	 * @param leftClick if true it left clicks.
	 * @return true if clicked.
	 */
	public boolean doClick(boolean leftClick) {
		try {
			for (int i = 0; i < 10; i++) {
				methods.mouse.move(getPoint());
				if (this.contains(methods.mouse.getLocation())) {
					methods.mouse.click(leftClick);
					return true;
				}
			}
		} catch (Exception ignored) {
		}
		return false;
	}

	/**
	 * Clicks the RSModel and clicks the menu action
	 *
	 * @param action the action to be clicked in the menu
	 * @param target the option of the action to be clicked in the menu
	 * @return true if clicked, false if failed.
	 */
	public boolean doAction(String action, String... target) {
		try {
			for (int i = 0; i < 10; i++) {
				methods.mouse.move(getPoint());
				if (this.contains(methods.mouse.getLocation())) {
					if (methods.menu.doAction(action, target)) {
						return true;
					}
				}
			}
		} catch (Exception ignored) {
		}
		return false;
	}

	/**
	 * Clicks the RSModel and clicks the menu action
	 *
	 * @param action the action to be clicked in the menu
	 * @return true if clicked, false if failed.
	 */
	public boolean doAction(String action) {
		return doAction(action, null);
	}

	/**
	 * Returns a random screen point.
	 *
	 * @return A screen point, or Point(-1, -1) if the model is not on screen.
	 * @see #getCentralPoint()
	 * @see #getPointOnScreen()
	 */
	public Point getPoint() {
		update();
		int len = indices1.length;
		int sever = random(0, len);
		Point point = getPointInRange(sever, len);
		if (point != null) {
			return point;
		}
		point = getPointInRange(0, sever);
		if (point != null) {
			return point;
		}
		return new Point(-1, -1);
	}

	/**
	 * Returns all the screen points.
	 *
	 * @return All the points that are on the screen, if the model is not on the
	 *         screen it will return null.
	 */
	public Point[] getPoints() {
		if (this == null) {
			return null;
		}
		Polygon[] polys = getTriangles();
		Point[] points = new Point[polys.length * 3];
		int index = 0;
		for (Polygon poly : polys) {
			for (int i = 0; i < 3; i++) {
				points[index++] = new Point(poly.xpoints[i], poly.ypoints[i]);
			}
		}
		return points;
	}

	/**
	 * Gets a point on a model that is on screen.
	 *
	 * @return First point that it finds on screen else a random point on screen
	 *         of an object.
	 */
	public Point getPointOnScreen() {
		ArrayList<Point> list = new ArrayList<>();
		try {
			Polygon[] tris = getTriangles();
			for (Polygon p : tris) {
				for (int j = 0; j < p.xpoints.length; j++) {
					Point firstPoint = new Point(p.xpoints[j], p.ypoints[j]);
					if (methods.calc.pointOnScreen(firstPoint)) {
						return firstPoint;
					} else {
						list.add(firstPoint);
					}
				}
			}
		} catch (Exception ignored) {
		}
		return list.size() > 0 ? list.get(random(0, list.size())) : null;
	}

	/**
	 * Generates a rough central point. Performs the calculation by first
	 * generating a rough point, and then finding the point closest to the rough
	 * point that is actually on the RSModel.
	 *
	 * @return The rough central point.
	 */
	public Point getCentralPoint() {
		try {
			/* Add X and Y of all points, to get a rough central point */
			int x = 0, y = 0, total = 0;
			for (Polygon poly : getTriangles()) {
				for (int i = 0; i < poly.npoints; i++) {
					x += poly.xpoints[i];
					y += poly.ypoints[i];
					total++;
				}
			}
			Point central = new Point(x / total, y / total);
			/*
							* Find a real point on the character that is closest to the central
							* point
							*/
			Point curCentral = null;
			double dist = 20000;
			for (Polygon poly : getTriangles()) {
				for (int i = 0; i < poly.npoints; i++) {
					Point p = new Point(poly.xpoints[i], poly.ypoints[i]);
					if (!methods.calc.pointOnScreen(p)) {
						continue;
					}
					double dist2 = methods.calc.distanceBetween(central, p);
					if (curCentral == null || dist2 < dist) {
						curCentral = p;
						dist = dist2;
					}
				}
			}
			return curCentral;
		} catch (Exception ignored) {
		}
		return new Point(-1, -1);
	}

	/**
	 * Returns an array of triangles containing the screen points of this model.
	 *
	 * @return The on screen triangles of this model.
	 */

	public Polygon[] getTriangles() {
		update();
		LinkedList<Polygon> polygons = new LinkedList<Polygon>();
		int locX = getLocalX();
		int locY = getLocalY();
		int len = indices1.length;
		int height = methods.calc.tileHeight(locX, locY);
		for (int i = 0; i < len; ++i) {
			Point one = methods.calc.worldToScreen(locX + xPoints[indices1[i]],
					locY + zPoints[indices1[i]], height + yPoints[indices1[i]]);
			Point two = methods.calc.worldToScreen(locX + xPoints[indices2[i]],
					locY + zPoints[indices2[i]], height + yPoints[indices2[i]]);
			Point three = methods.calc.worldToScreen(locX
					+ xPoints[indices3[i]], locY + zPoints[indices3[i]], height
					+ yPoints[indices3[i]]);

			if (one.getX() >= 0 && two.getX() >= 0 && three.getX() >= 0) {
				polygons.add(new Polygon(new int[]{one.getX(), two.getX(), three.getX()},
						new int[]{one.getY(), two.getY(), three.getY()}, 3));
			}
		}
		return polygons.toArray(new Polygon[polygons.size()]);
	}

	/**
	 * Moves the mouse onto the RSModel.
	 */
	public void hover() {
		methods.mouse.move(getPoint());
	}

	/**
	 * Returns true if the provided object is an RSModel with the same x, y and
	 * z points as this model. This method compares all of the values in the
	 * three vertex arrays.
	 *
	 * @return <tt>true</tt> if the provided object is a model with the same
	 *         points as this.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof RSModel) {
			RSModel m = (RSModel) o;
			return Arrays.equals(indices1, m.indices1)
					&& Arrays.equals(xPoints, m.xPoints)
					&& Arrays.equals(yPoints, m.yPoints)
					&& Arrays.equals(zPoints, m.zPoints);
		}
		return false;
	}

	private Point getPointInRange(int start, int end) {
		int locX = getLocalX();
		int locY = getLocalY();
		int height = methods.calc.tileHeight(locX, locY);
		for (int i = start; i < end; ++i) {
			Point one = methods.calc.worldToScreen(locX + xPoints[indices1[i]],
					locY + zPoints[indices1[i]], height + yPoints[indices1[i]]);
			int x = -1, y = -1;
			if (one.getX()>= 0) {
				x = one.getX();
				y = one.getY();
			}
			Point two = methods.calc.worldToScreen(locX + xPoints[indices2[i]],
					locY + zPoints[indices2[i]], height + yPoints[indices2[i]]);
			if (two.getX()>= 0) {
				if (x >= 0) {
					x = (x + two.getX()) / 2;
					y = (y + two.getY()) / 2;
				} else {
					x = two.getX();
					y = two.getY();
				}
			}
			Point three = methods.calc.worldToScreen(locX
					+ xPoints[indices3[i]], locY + zPoints[indices3[i]], height
					+ yPoints[indices3[i]]);
			if (three.getX()>= 0) {
				if (x >= 0) {
					x = (x + three.getX()) / 2;
					y = (y + three.getY()) / 2;
				} else {
					x = three.getX();
					y = three.getY();
				}
			}
			if (x >= 0) {
				return new Point(x, y);
			}
		}
		return null;
	}

}