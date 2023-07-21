package net.runelite.rsb.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AABB;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.geometry.SimplePolygon;
import net.runelite.api.model.Jarvis;
import net.runelite.rsb.internal.wrappers.Filter;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.methods.MethodProvider;
import net.runelite.rsb.util.StdRandom;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A screen space model.
 */
@Slf4j
public class RSModel extends MethodProvider {
	/**
	 * Returns a filter that matches against the array of point indices for the
	 * A vertices of each triangle. Use in scripts is discouraged.
	 *
	 * @param vertex_a The array of indices for A vertices.
	 * @return The vertex point index based model filter.
	 */
	public static Filter<RSModel> newVertexFilter(final int[] vertex_a) {
		return m -> Arrays.equals(m.indices1, vertex_a);
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
			indices1 = model.getFaceIndices1();
			indices2 = model.getFaceIndices2();
			indices3 = model.getFaceIndices3();
		} else {
			this.model = null;
		}
	}

	protected int getLocalX() {
		return -1;
	}

	protected int getLocalY() {
		return -1;
	}

	protected int getLocalZ() {
		return Perspective.getTileHeight(methods.client, new LocalPoint(getLocalX(), getLocalY()), methods.client.getPlane());
	}

	protected void update() {
	}

	/**
	 * @param p A point on the screen
	 * @return true of the point is within the bounds of the model
	 */
	public boolean contains(Point p) {
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
	 * @param leftClick if true it left-clicks.
	 * @return true if clicked.
	 */
	public boolean doClick(boolean leftClick) {
		try {
			for (int i = 0; i < 10; i++) {
				methods.mouse.move(getPointNearCenter());
				if (this.contains(methods.mouse.getLocation())) {
					methods.mouse.click(leftClick);
					return true;
				}
			}
		} catch (Exception ignored) {
			log.debug("Model click error", ignored);
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
			for (int i = 0; i < 3; i++) {
				if (!this.contains(methods.mouse.getLocation())) {
					methods.mouse.move(getPointNearCenter());
					methods.mouse.move(getPointNearCenter());
				}
				if (methods.menu.doAction(action, target)) {
					return true;
				}
			}
		} catch (Exception ignored) {
			log.debug("Model action perform error", ignored);
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
		return doAction(action, (String[]) null);
	}

	/**
	 * Returns a random screen point.
	 *
	 * @return A screen point, or Point(-1, -1) if the model is not on screen.
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
	 * screen it will return null.
	 */
	public Point[] getPoints() {
		Polygon[] polys = getTriangles();
		ArrayList<Point> points = new ArrayList<>();

		int index = 0;
		for (Polygon poly : polys) {
			for (int i = 0; i < 3; i++) {
				points.add(index++, new Point(poly.xpoints[i], poly.ypoints[i]));
			}
		}
		return points.toArray(new Point[0]);
	}

	/**
	 * Gets a point on a model that is on screen.
	 *
	 * @return First point that it finds on screen else a random point on screen
	 * of an object.
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
			log.debug("Model failed to get points on screen", ignored);
		}
		return list.size() > 0 ? list.get(random(0, list.size())) : null;
	}

	/**
	 * Returns an array of triangles containing the screen points of this model.
	 *
	 * @return The on screen triangles of this model.
	 */
	public Polygon[] getTriangles() {
		final int NO_MODEL = 1;
		if (model == null) {
			Polygon tilePoly = Perspective.getCanvasTilePoly(methods.client, new LocalPoint(getLocalX(), getLocalY()));
			return new Polygon[]{tilePoly};
		}
		int count = model.getVerticesCount();

		int[] x2d = new int[count];
		int[] y2d = new int[count];

		int localX = getLocalX();
		int localY = getLocalY();

		Perspective.modelToCanvas(methods.client, count, localX, localY, getLocalZ(), getOrientation(), model.getVerticesX(), model.getVerticesZ(), model.getVerticesY(), x2d, y2d);
		ArrayList<Polygon> polys = new ArrayList<>(model.getFaceCount());

		int[] trianglesX = model.getFaceIndices1();
		int[] trianglesY = model.getFaceIndices2();
		int[] trianglesZ = model.getFaceIndices3();

		double averageTriangleLength = (double) (trianglesX.length + trianglesY.length + trianglesZ.length) / 3;

		for (int triangle = 0; triangle < count; ++triangle) {
			if (averageTriangleLength <= NO_MODEL) {
				return null;
			}
			if (averageTriangleLength <= triangle) {
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
		return polys.toArray(new Polygon[0]);
	}

	/**
	 * Moves the mouse onto the RSModel.
	 */
	public void hover() {
		methods.mouse.move(getPointNearCenter());
	}

	/**
	 * Returns true if the provided object is an RSModel with the same x, y and
	 * z points as this model. This method compares all the values in the
	 * three vertex arrays.
	 *
	 * @return <code>true</code> if the provided object is a model with the same
	 * points as this.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof RSModel m) {
			return Arrays.equals(indices1, m.indices1)
					&& Arrays.equals(xPoints, m.xPoints)
					&& Arrays.equals(yPoints, m.yPoints)
					&& Arrays.equals(zPoints, m.zPoints);
		}
		return false;
	}

	public Point getCenterPoint() {
		Polygon[] triangles = this.getTriangles();
		int min_x = Integer.MAX_VALUE, max_x = Integer.MIN_VALUE, min_y = Integer.MAX_VALUE, max_y = Integer.MIN_VALUE;

		for (Polygon triangle : triangles) {
			for (int i = 0; i < triangle.npoints; ++i) {
				if (triangle.xpoints[i] < min_x) {
					min_x = triangle.xpoints[i];
				}
				if (triangle.xpoints[i] > max_x) {
					max_x = triangle.xpoints[i];
				}
				if (triangle.ypoints[i] < min_y) {
					min_y = triangle.ypoints[i];
				}
				if (triangle.ypoints[i] > max_y) {
					max_y = triangle.ypoints[i];
				}
			}
		}
		return new Point((max_x + min_x) / 2, (max_y + min_y) / 2);
	}

	/**
	 * This function first chooses a random triangle from the model polygons.
	 * Then it generates two random numbers between 0 and 1.
	 * These numbers are used as barycentric coordinates to generate
	 * a point that is guaranteed to be within the chosen triangle.
	 *
	 * @return a random point that collides with the polygons of this model.
	 */
	public Point getPointNearCenter() {
		Polygon[] triangles = this.getTriangles();
		int min_x = Integer.MAX_VALUE, max_x = Integer.MIN_VALUE, min_y = Integer.MAX_VALUE, max_y = Integer.MIN_VALUE;

		for (Polygon triangle : triangles) {
			for (int i = 0; i < triangle.npoints; ++i) {
				if (triangle.xpoints[i] < min_x) {
					min_x = triangle.xpoints[i];
				}
				if (triangle.xpoints[i] > max_x) {
					max_x = triangle.xpoints[i];
				}
				if (triangle.ypoints[i] < min_y) {
					min_y = triangle.ypoints[i];
				}
				if (triangle.ypoints[i] > max_y) {
					max_y = triangle.ypoints[i];
				}
			}
		}

		int centerX = (max_x + min_x) / 2;
		int centerY = (max_y + min_y) / 2;
		
		int x = (int)StdRandom.gaussian(min_x, max_x, centerX, (max_x - min_x) / 3);
		int y = (int)StdRandom.gaussian(min_y, max_y, centerY, (max_y - min_y) / 3);
		
		return new Point(x, y);
	}

	protected Point getPointInRange(int start, int end) {
		int locX = getLocalX();
		int locY = getLocalY();
		int height = methods.calc.tileHeight(locX, locY);
		Polygon[] triangles = this.getTriangles();
		ArrayList<Point> points = new ArrayList<>();
		for (int i = start; i < end && i < triangles.length; i++)
			for (int n = 0; n < triangles[i].npoints; n++)
				points.add(new Point(triangles[i].xpoints[n], triangles[i].ypoints[n]));
		if (points.isEmpty()) return null;
		// Return a random point from the list
		int randomIndex = StdRandom.uniform(points.size());
		return points.get(randomIndex);
	}


	public int getOrientation() {
		return 0;
	}

	public int getIndexCount() {
		return (model != null) ? model.getFaceCount() : 0;
	}

	public int getVertexCount() {
		return (model != null) ? model.getVerticesCount() : 0;
	}

	public Model getModel() {
		return model;
	}

	public Polygon getConvexHull() {
		AABB ab = model.getAABB(0);
		int ex = ab.getExtremeX();
		if (ex == -1) {
			// dynamic models don't get stored when they render where this normally happens
			model.calculateBoundsCylinder();
			model.getAABB(0);
			ex = ab.getExtremeX();
		}

		int x1 = ab.getCenterX();
		int y1 = ab.getCenterZ();
		int z1 = ab.getCenterY();

		int ey = ab.getExtremeZ();
		int ez = ab.getExtremeY();

		int x2 = x1 + ex;
		int y2 = y1 + ey;
		int z2 = z1 + ez;

		x1 -= ex;
		y1 -= ey;
		z1 -= ez;

		int[] xa = new int[]{
				x1, x2, x1, x2,
				x1, x2, x1, x2
		};
		int[] ya = new int[]{
				y1, y1, y2, y2,
				y1, y1, y2, y2
		};
		int[] za = new int[]{
				z1, z1, z1, z1,
				z2, z2, z2, z2
		};

		int[] x2d = new int[8];
		int[] y2d = new int[8];

		Perspective.modelToCanvas(methods.client, 8, getLocalX(), getLocalY(), getLocalZ(), getOrientation(), xa, ya, za, x2d, y2d);
		SimplePolygon simplePolygon = Jarvis.convexHull(x2d, y2d);
		return new Polygon(simplePolygon.getX(), simplePolygon.getY(), simplePolygon.size());
	}
}
