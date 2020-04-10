package net.runelite.client.rsb.wrappers;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.rsb.internal.wrappers.Filter;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.methods.MethodProvider;

import java.awt.*;
import java.util.ArrayList;
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
		if (model != null) {
			this.model = model;
			xPoints = model.getVerticesX();
			yPoints = model.getVerticesY();
			zPoints = model.getVerticesZ();
			indices1 = model.getTrianglesX();
			indices2 = model.getTrianglesY();
			indices3 = model.getTrianglesZ();
		}
		else {
			this.model = null;
		}
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
		int len = model.getVerticesCount();
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
		if (model == null) {
			return null;
		}

		int count = model.getVerticesCount();

		int[] x2d = new int[count];
		int[] y2d = new int[count];

		int localX = getLocalX();
		int localY = getLocalY();

		final int tileHeight = Perspective.getTileHeight(methods.client, new LocalPoint(localX, localY), methods.client.getPlane());



		Perspective.modelToCanvas(methods.client, count, localX, localY, tileHeight, getOrientation(), model.getVerticesX(), model.getVerticesZ(), model.getVerticesY(), x2d, y2d);
		ArrayList polys = new ArrayList(model.getTrianglesCount());

		int[] trianglesX = model.getTrianglesX();
		int[] trianglesY = model.getTrianglesY();
		int[] trianglesZ = model.getTrianglesZ();

		for (int triangle = 0; triangle < count; ++triangle) {
			if (trianglesX.length < triangle) {
				break;
			}
			if (trianglesY.length < triangle) {
				break;
			}
			if (trianglesZ.length < triangle) {
				break;
			}
			int[] xx =
					{
							x2d[trianglesX[triangle]], x2d[trianglesY[triangle]], x2d[trianglesZ[triangle]]
					};

			int[] yy =
					{
							y2d[trianglesX[triangle]], y2d[trianglesY[triangle]], y2d[trianglesZ[triangle]]
					};

			polys.add(new Polygon(xx, yy, 3));
		}

		return (Polygon[]) polys.toArray(new Polygon[0]);
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

	protected Point getPointInRange(int start, int end) {
		int locX = getLocalX();
		int locY = getLocalY();
		int height = methods.calc.tileHeight(locX, locY);
		Polygon[] triangles = this.getTriangles();
		for (int i = start; i < end; i++) {
			if (i < triangles.length) {
				for (int n = 0; n < triangles[i].npoints; n++) {
					return new Point(triangles[i].xpoints[n], triangles[i].ypoints[n]);
				}
			}
		}
		return null;
	}

	public int getOrientation() {
		return 0;
	}

}