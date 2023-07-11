package net.runelite.rsb.methods;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.Point;
import net.runelite.rsb.wrappers.common.Positionable;
import net.runelite.rsb.wrappers.RSTile;

import java.awt.*;

/**
 * Game world and projection calculations.
 */
@Slf4j
public class Calculations extends MethodProvider {
	private final Render render = new Render();
	private final RenderData renderData = new RenderData();

	/**
	 * Creates the singleton for calculations
	 * @param ctx	The bot context to associate this calculations object with
	 */
	Calculations(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Checks whether a given tile is on the minimap.
	 *
	 * @param t The Tile to check.
	 * @return <code>true</code> if the RSTile is on the minimap; otherwise
	 *         <code>false</code>.
	 * @see #tileToMinimap(RSTile)
	 */
	public boolean tileOnMap(RSTile t) {
		return tileToMinimap(t) != null;
	}

	/**
	 * Checks whether the centroid of a given tile is on the screen.
	 *
	 * @param t The RSTile to check.
	 * @return <code>true</code> if the RSTile is on the screen; otherwise
	 *         <code>false</code>.
	 */
	public boolean tileOnScreen(RSTile t) {
		Point point = tileToScreen(t, 0.5, 0.5, 0);
		return (point != null) && pointOnScreen(point);
	}

	/**
	 * Returns the Point on screen where a given tile is shown on the minimap.
	 *
	 * @param t The RSTile to check.
	 * @return <code>Point</code> within minimap; otherwise
	 *         <code>new Point(-1, -1)</code>.
	 */

	@SuppressWarnings("unused")
	public Point tileToMinimap(RSTile t) {
		return worldToMinimap(t.getWorldLocation().getX(), t.getWorldLocation().getY());
	}

	/**
	 * Checks whether a point is within the rectangle that determines the bounds
	 * of game screen. This will work fine when in fixed mode. In resizable mode
	 * it will exclude any points that are less than 253 pixels from the right
	 * of the screen or less than 169 pixels from the bottom of the screen,
	 * giving a rough area.
	 *
	 * @param check The point to check.
	 * @return <code>true</code> if the point is within the rectangle; otherwise
	 *         <code>false</code>.
	 */
	public boolean pointOnScreen(Point check) {
		int x = check.getX(), y = check.getY();
		return x > methods.client.getViewportXOffset() && x < methods.client.getViewportWidth()
				&& y > methods.client.getViewportYOffset() && y < methods.client.getViewportHeight();
	}

	/**
	 * Calculates the distance between two points.
	 *
	 * @param curr The first point.
	 * @param dest The second point.
	 * @return The distance between the two points, using the distance formula.
	 * @see #distanceBetween(RSTile, RSTile)
	 */
	public double distanceBetween(Point curr, Point dest) {
		return Math.sqrt(((curr.getX() - dest.getX()) * (curr.getX() - dest.getX())) + ((curr.getY() - dest.getY()) * (curr.getY() - dest.getY())));
	}

	/**
	 * Returns a random double in a specified range
	 *
	 * @param min Minimum value (inclusive).
	 * @param max Maximum value (exclusive).
	 * @return The random <code>double</code> generated.
	 */
	@Override
	public double random(double min, double max) {
		return Math.min(min, max) + methods.random.nextDouble()
				* Math.abs(max - min);
	}

	/**
	 * Will return the closest tile that is on screen to the given tile.
	 *
	 * @param tile Tile you want to get to.
	 * @return <code>RSTile</code> that is onScreen.
	 */
	public RSTile getTileOnScreen(RSTile tile) {
		try {
			if (tileOnScreen(tile)) {
				return tile;
			} else {
				RSTile loc =  new RSTile(methods.client.getLocalPlayer().getWorldLocation().getX(),
						methods.client.getLocalPlayer().getWorldLocation().getY(), methods.client.getPlane());
				RSTile halfWayTile = new RSTile((tile.getWorldLocation().getX() +
						loc.getWorldLocation().getX()) / 2, (tile.getWorldLocation().getY() +
						loc.getWorldLocation().getY()) / 2, methods.client.getPlane());

				if (tileOnScreen(halfWayTile)) {
					return halfWayTile;
				} else {
					return getTileOnScreen(halfWayTile);
				}
			}
		} catch (StackOverflowError soe) {
			return null;
		}
	}
	/**
	 * Returns the angle to a given tile in degrees anti-clockwise from the
	 * positive x axis (where the x-axis is from west to east).
	 *
	 * @param t The target tile
	 * @return The angle in degrees
	 */
	public int angleToTile(RSTile t) {
		RSTile me = new RSTile(methods.client.getLocalPlayer().getWorldLocation().getX(),
				methods.client.getLocalPlayer().getWorldLocation().getY(), methods.client.getPlane());
		int angle = (int) Math.toDegrees(Math.atan2(t.getWorldLocation().getY() - me.getWorldLocation().getY(),
				t.getWorldLocation().getX() - me.getWorldLocation().getX()));
		return angle >= 0 ? angle : 360 + angle;
	}

	/**
	 * Returns the screen location of a Tile with given 3D x, y and height
	 * offset values.
	 *
	 * @param tile   RSTile for which the screen location should be calculated.
	 * @param dX     Distance from bottom left of the tile to bottom right. Ranges
	 *               from 0-1;
	 * @param dY     Distance from bottom left of the tile to top left. Ranges from
	 *               0-1;
	 * @param height Height offset (normal to the ground) to return the
	 *               <code>Point</code> at.
	 * @return <code>Point</code> based on position on the game plane; otherwise
	 *         <code>new Point(-1, -1)</code>.
	 */
	public Point tileToScreen(final RSTile tile, final double dX, final double dY, final int height) {
		RSTile walkerTile = new RSTile(tile).toLocalTile();
		return Perspective.localToCanvas(methods.client, new LocalPoint(walkerTile.getX(), walkerTile.getY()), methods.client.getPlane(), height);
	}

	/**
	 * Returns the screen location of a Tile with a given 3D height offset.
	 *
	 * @param tile   RSTile for which the screen location should be calculated.
	 * @param height Height offset (normal to the ground) to return the
	 *               <code>Point</code> at.
	 * @return <code>Point</code> based on position on the game plane; if null
	 *         <code>new Point(-1, -1)</code>.
	 * @see #tileToScreen(RSTile, double, double, int)
	 */
	public Point tileToScreen(final RSTile tile, final int height) {
		return tileToScreen(tile, 0.5, 0.5, height);
	}

	/**
	 * Returns the screen location of the south-west corner of the given tile.
	 *
	 * @param tile RSTile for which the screen location should be calculated.
	 * @return Center <code>Point</code> of the RSTile at a height of 0; if null
	 *         <code>new Point(-1, -1)</code>.
	 * @see #tileToScreen(RSTile, int)
	 */
	public Point tileToScreen(final RSTile tile) {
		return tileToScreen(tile, 0);
	}

	/**
	 * Returns the diagonal distance to a given Positionable.
	 *
	 * @param positionable The destination tile.
	 * @return Distance to <code>Positionable</code>.
	 */
	public int distanceTo(Positionable positionable) {
		return positionable == null ? -1 : (int) distanceBetween(methods.players.getMyPlayer().getLocation(), positionable.getLocation());
	}

	/**
	 * Returns the diagonal distance to a given x,y,z coordinate.
	 *
	 * @param x The destination x coordinate.
	 * @param y The destination x coordinate.
	 * @param z The destination x coordinate.
	 * @return Distance to new RSTile(x,y,z).
	 */
	public int distanceTo(int x, int y, int z) {
		return (int) distanceBetween(methods.players.getMyPlayer().getLocation(), new RSTile(x,y,z));
	}

	/**
	 * Returns the diagonal distance (hypot) between two RSTiles.
	 *
	 * @param curr The starting tile.
	 * @param dest The destination tile.
	 * @return The diagonal distance between the two <code>RSTile</code>s.
	 * @see #distanceBetween(Point, Point)
	 */
	public double distanceBetween(RSTile curr, RSTile dest) {
		return Math.sqrt((curr.getWorldLocation().getX() - dest.getWorldLocation().getX()) *
				(curr.getWorldLocation().getX() - dest.getWorldLocation().getX()) +
				(curr.getWorldLocation().getY() - dest.getWorldLocation().getY()) *
				(curr.getWorldLocation().getY() - dest.getWorldLocation().getY()));
	}

	/**
	 * Returns the length of the path generated to a given RSTile.
	 *
	 * @param dest     The destination tile.
	 * @param isObject <code>true</code> if reaching any tile adjacent to the destination
	 *                 should be accepted.
	 * @return <code>true</code> if reaching any tile adjacent to the destination
	 *         should be accepted.
	 */
	public int pathLengthTo(RSTile dest, boolean isObject) {
		RSTile curPos = methods.players.getMyPlayer().getLocation();
		return pathLengthBetween(curPos, dest, isObject);
	}

	/**
	 * Returns the length of the path generates between two RSTiles.
	 *
	 * @param start    The starting tile.
	 * @param dest     The destination tile.
	 * @param isObject <code>true</code> if reaching any tile adjacent to the destination
	 *                 should be accepted.
	 * @return <code>true</code> if reaching any tile adjacent to the destination
	 *         should be accepted.
	 */
	public int pathLengthBetween(RSTile start, RSTile dest, boolean isObject) {
		return dijkstraDist(start.getWorldLocation().getX() - methods.client.getBaseX(), // startX
				start.getWorldLocation().getY() - methods.client.getBaseY(), // startY
				dest.getWorldLocation().getX() - methods.client.getBaseX(), // destX
				dest.getWorldLocation().getY() - methods.client.getBaseY(), // destY
				isObject); // if it's an object, accept any adjacent tile
	}

	/**
	 * checks whether a given RSTile is reachable.
	 *
	 * @param dest     The <code>RSTile</code> to check.
	 * @param isObject True if an instance of <code>RSObject</code>.
	 * @return <code>true</code> if player can reach specified Object; otherwise
	 *         <code>false</code>.
	 */
	public boolean canReach(RSTile dest, boolean isObject) {
		return pathLengthTo(dest, isObject) != -1;
	}

	/**
	 * Returns the screen Point of given absolute x and y values in the game's
	 * 3D plane.
	 *
	 * @param x x value based on the game plane.
	 * @param y y value based on the game plane.
	 * @return <code>Point</code> within minimap; otherwise
	 *         <code>new Point(-1, -1)</code>.
	 */
	public Point worldToMinimap(double x, double y) {
		LocalPoint test = LocalPoint.fromWorld(methods.client, (int) x, (int) y);
		if (test!=null)
			return Perspective.localToMinimap(methods.client,  test, 2500);
		return null;
	}

	/**
	 * Returns the screen location of a given point on the ground. This accounts
	 * for the height of the ground at the given location.
	 *
	 * @param x      x value based on the game plane.
	 * @param y      y value based on the game plane.
	 * @param height height offset (normal to the ground).
	 * @return <code>Point</code> based on screen; otherwise
	 *         <code>new Point(-1, -1)</code>.
	 */
	public Point groundToScreen(final int x, final int y, final int height) {
		return Perspective.localToCanvas(methods.client, x, y, height);
	}

	/**
	 * Returns the height of the ground at the given location in the game world.
	 *
	 * @param x x value based on the game plane.
	 * @param y y value based on the game plane.
	 * @return The ground height at the given location; otherwise <code>0</code>
	 *         .
	 */
	public int tileHeight(final int x, final int y) {
		return Perspective.getTileHeight(methods.client, new LocalPoint(x, y), methods.client.getPlane());

	}

	/**
	 * Returns the screen location of a given 3D point in the game world.
	 *
	 * @param x x value on the game plane.
	 * @param y y value on the game plane.
	 * @param z z value on the game plane.
	 * @return <code>Point</code> based on screen; otherwise
	 *         <code>new Point(-1, -1)</code>.
	 */
	public Point worldToScreen(int x, int y, int z) {
		LocalPoint local = LocalPoint.fromWorld(methods.client, x, y);
		if (local == null) {
			local = new LocalPoint(x, y);
		}
		return Perspective.localToCanvas(methods.client, local, z);
	}

	/**
	 * Returns the screen location of a given 3D point in the game world.
	 *
	 * @param x 		x value on the game plane.
	 * @param y 		y value on the game plane.
	 * @param plane     the game level (plane) value.
	 * @param z 		z value on the game plane.
	 * @return <code>Point</code> based on screen; otherwise
	 *         <code>new Point(-1, -1)</code>.
	 */
	public Point worldToScreen(int x, int y, int plane, int z) {
		LocalPoint local = LocalPoint.fromWorld(methods.client, x, y);
		if (local == null) {
			local = new LocalPoint(x, y);
		}
		return Perspective.localToCanvas(methods.client, local, plane, z);
	}


	public Polygon getTileBoundsPoly(Positionable positionable, int additionalHeight) {
		return Perspective.getCanvasTilePoly(methods.client, positionable.getLocation().getLocalLocation(methods));
	}

	public Point getRandomPolyPoint(Polygon polygon) {
		return new Point(polygon.xpoints[random(0,polygon.npoints)], polygon.ypoints[random(0,polygon.npoints)]);
	}

	/**
	 * @param startX   the startX (0 < startX < 104)
	 * @param startY   the startY (0 < startY < 104)
	 * @param destX    the destX (0 < destX < 104)
	 * @param destY    the destY (0 < destY < 104)
	 * @param isObject if it's an object, it will find path which touches it.
	 * @return The distance of the shortest path to the destination; or -1 if no
	 *         valid path to the destination was found.
	 */
	private int dijkstraDist(final int startX, final int startY, final int destX, final int destY,
	                         final boolean isObject) {
		final int[][] prev = new int[104][104];
		final int[][] dist = new int[104][104];
		final int[] path_x = new int[4000];
		final int[] path_y = new int[4000];
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				prev[xx][yy] = 0;
				dist[xx][yy] = 99999999;
			}
		}
		int curr_x = startX;
		int curr_y = startY;
		prev[startX][startY] = 99;
		dist[startX][startY] = 0;
		int path_ptr = 0;
		int step_ptr = 0;
		path_x[path_ptr] = startX;
		path_y[path_ptr++] = startY;
		final byte[][] blocks = methods.client.getTileSettings()[methods.game.getPlane()];
		final int pathLength = path_x.length;
		boolean foundPath = false;
		while (step_ptr != path_ptr) {
			curr_x = path_x[step_ptr];
			curr_y = path_y[step_ptr];
			if (Math.abs(curr_x - destX) + Math.abs(curr_y - destY) == (isObject ? 1 : 0)) {
				foundPath = true;
				break;
			}
			step_ptr = (step_ptr + 1) % pathLength;
			final int cost = dist[curr_x][curr_y] + 1;
			// south
			if ((curr_y > 0) && (prev[curr_x][curr_y - 1] == 0) && ((blocks[curr_x + 1][curr_y] & 0x1280102) == 0)) {
				path_x[path_ptr] = curr_x;
				path_y[path_ptr] = curr_y - 1;
				path_ptr = (path_ptr + 1) % pathLength;
				prev[curr_x][curr_y - 1] = 1;
				dist[curr_x][curr_y - 1] = cost;
			}
			// west
			if ((curr_x > 0) && (prev[curr_x - 1][curr_y] == 0) && ((blocks[curr_x][curr_y + 1] & 0x1280108) == 0)) {
				path_x[path_ptr] = curr_x - 1;
				path_y[path_ptr] = curr_y;
				path_ptr = (path_ptr + 1) % pathLength;
				prev[curr_x - 1][curr_y] = 2;
				dist[curr_x - 1][curr_y] = cost;
			}
			// north
			if ((curr_y < 104 - 1) && (prev[curr_x][curr_y + 1] == 0) && ((blocks[curr_x + 1][curr_y + 2] &
					0x1280120) == 0)) {
				path_x[path_ptr] = curr_x;
				path_y[path_ptr] = curr_y + 1;
				path_ptr = (path_ptr + 1) % pathLength;
				prev[curr_x][curr_y + 1] = 4;
				dist[curr_x][curr_y + 1] = cost;
			}
			// east
			if ((curr_x < 104 - 1) && (prev[curr_x + 1][curr_y] == 0) && ((blocks[curr_x + 2][curr_y + 1] &
					0x1280180) == 0)) {
				path_x[path_ptr] = curr_x + 1;
				path_y[path_ptr] = curr_y;
				path_ptr = (path_ptr + 1) % pathLength;
				prev[curr_x + 1][curr_y] = 8;
				dist[curr_x + 1][curr_y] = cost;
			}
			// south west
			if ((curr_x > 0) && (curr_y > 0) && (prev[curr_x - 1][curr_y - 1] == 0) && ((blocks[curr_x][curr_y] &
					0x128010e) == 0) && ((blocks[curr_x][curr_y + 1] & 0x1280108) == 0) && ((blocks[curr_x +
					1][curr_y] & 0x1280102) == 0)) {
				path_x[path_ptr] = curr_x - 1;
				path_y[path_ptr] = curr_y - 1;
				path_ptr = (path_ptr + 1) % pathLength;
				prev[curr_x - 1][curr_y - 1] = 3;
				dist[curr_x - 1][curr_y - 1] = cost;
			}
			// north west
			if ((curr_x > 0) && (curr_y < 104 - 1) && (prev[curr_x - 1][curr_y + 1] == 0) && (
					(blocks[curr_x][curr_y + 2] & 0x1280138) == 0) && ((blocks[curr_x][curr_y + 1] & 0x1280108) ==
					0) && ((blocks[curr_x + 1][curr_y + 2] & 0x1280120) == 0)) {
				path_x[path_ptr] = curr_x - 1;
				path_y[path_ptr] = curr_y + 1;
				path_ptr = (path_ptr + 1) % pathLength;
				prev[curr_x - 1][curr_y + 1] = 6;
				dist[curr_x - 1][curr_y + 1] = cost;
			}
			// south east
			if ((curr_x < 104 - 1) && (curr_y > 0) && (prev[curr_x + 1][curr_y - 1] == 0) && ((blocks[curr_x +
					2][curr_y] & 0x1280183) == 0) && ((blocks[curr_x + 2][curr_y + 1] & 0x1280180) == 0) && (
					(blocks[curr_x + 1][curr_y] & 0x1280102) == 0)) {
				path_x[path_ptr] = curr_x + 1;
				path_y[path_ptr] = curr_y - 1;
				path_ptr = (path_ptr + 1) % pathLength;
				prev[curr_x + 1][curr_y - 1] = 9;
				dist[curr_x + 1][curr_y - 1] = cost;
			}
			// north east
			if ((curr_x < 104 - 1) && (curr_y < 104 - 1) && (prev[curr_x + 1][curr_y + 1] == 0) && ((blocks[curr_x
					+ 2][curr_y + 2] & 0x12801e0) == 0) && ((blocks[curr_x + 2][curr_y + 1] & 0x1280180) == 0) && (
					(blocks[curr_x + 1][curr_y + 2] & 0x1280120) == 0)) {
				path_x[path_ptr] = curr_x + 1;
				path_y[path_ptr] = curr_y + 1;
				path_ptr = (path_ptr + 1) % pathLength;
				prev[curr_x + 1][curr_y + 1] = 12;
				dist[curr_x + 1][curr_y + 1] = cost;
			}
		}
		return foundPath ? dist[curr_x][curr_y] : -1;
	}

	public static java.awt.Point convertRLPointToAWTPoint(Point point) {
		return new java.awt.Point(point.getX(), point.getY());
	}

	public boolean hasLineOfSight(RSTile start, RSTile end) {
		return start.getTile(methods).hasLineOfSightTo(end.getTile(methods));
	}
	public boolean hasLineOfSight(RSTile end) {
		return methods.players.getMyPlayer().getLocation().getTile(methods).hasLineOfSightTo(end.getTile(methods));
	}

	static class Render {
		float absoluteX1 = 0, absoluteX2 = 0;
		float absoluteY1 = 0, absoluteY2 = 0;
		int xMultiplier = 512, yMultiplier = 512;
		int zNear = 50, zFar = 3500;
	}

	static class RenderData {
		float xOff = 0, xX = 32768, xY = 0, xZ = 0;
		float yOff = 0, yX = 0, yY = 32768, yZ = 0;
		float zOff = 0, zX = 0, zY = 0, zZ = 32768;
	}

	public static final int[] SIN_TABLE = new int[16384];
	public static final int[] COS_TABLE = new int[16384];

	static {
		final double d = 0.00038349519697141029D;
		for (int i = 0; i < 16384; i++) {
			Calculations.SIN_TABLE[i] = (int) (32768D * Math.sin(i * d));
			Calculations.COS_TABLE[i] = (int) (32768D * Math.cos(i * d));
		}
	}
}
