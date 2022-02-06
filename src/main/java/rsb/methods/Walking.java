package rsb.methods;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.widgets.WidgetInfo;
import rsb.wrappers.*;

import java.lang.reflect.Field;

/**
 * Walking related operations.
 */
public class Walking extends MethodProvider {
	Walking(final MethodContext ctx) {
		super(ctx);
	}

	private RSPath lastPath;
	private RSTile lastDestination;
	private RSTile lastStep;
	private Field  collisionField = null;

	/**
	 * Creates a new path based on a provided array of tile waypoints.
	 *
	 * @param tiles The waypoint tiles.
	 * @return An RSTilePath.
	 */
	public RSTilePath newTilePath(final RSTile[] tiles) {
		if (tiles == null) {
			throw new IllegalArgumentException("null waypoint list");
		}
		return new RSTilePath(methods, tiles);
	}

	/**
	 * Generates a path from the player's current location to a destination
	 * tile.
	 *
	 * @param destination The destination tile.
	 * @return The path as an RSPath.
	 */
	public RSPath getPath(final RSTile destination) {
		return new RSLocalPath(methods, destination);
	}

	/**
	 * Determines whether or not a given tile is in the loaded map area.
	 *
	 * @param tile The tile to check.
	 * @return <tt>true</tt> if local; otherwise <tt>false</tt>.
	 */
	public boolean isLocal(final RSTile tile) {
		int[][] flags = getCollisionFlags(methods.game.getPlane());
		int x = tile.getWorldLocation().getX() - methods.game.getBaseX();
		int y = tile.getWorldLocation().getY() - methods.game.getBaseY();
		return (flags != null && x >= 0 && y >= 0 && x < flags.length && y < flags.length);
	}

	/**
	 * Walks one tile towards the given destination using a generated path.
	 *
	 * @param destination The destination tile.
	 * @return <tt>true</tt> if the next tile was walked to; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean walkTo(final RSTile destination) {
		if (destination.equals(lastDestination)
				&& methods.calc.distanceTo(lastStep) < 10) {
			return lastPath.traverse();
		}
		lastDestination = destination;
		lastPath = getPath(destination);
		if (!lastPath.isValid()) {
			return false;
		}
		lastStep = lastPath.getNext();
		return lastPath.traverse();
	}

	/**
	 * Walks to the given tile using the minimap with 1 tile randomness.
	 *
	 * @param t The tile to walk to.
	 * @return <tt>true</tt> if the tile was clicked; otherwise <tt>false</tt>.
	 * @see #walkTileMM(RSTile, int, int)
	 */
	public boolean walkTileMM(final RSTile t) {
		return walkTileMM(t, 0, 0);
	}

	/**
	 * Walks to the given tile using the minimap with given randomness.
	 *
	 * @param t The tile to walk to.
	 * @param x The x randomness (between 0 and x-1).
	 * @param y The y randomness (between 0 and y-1).
	 * @return <tt>true</tt> if the tile was clicked; otherwise <tt>false</tt>.
	 */
	public boolean walkTileMM(final RSTile t, final int x, final int y) {
		int xx = t.getWorldLocation().getX(), yy = t.getWorldLocation().getY();
		if (x > 0) {
			if (random(1, 2) == random(1, 2)) {
				xx += random(0, x);
			} else {
				xx -= random(0, x);
			}
		}
		if (y > 0) {
			if (random(1, 2) == random(1, 2)) {
				yy += random(0, y);
			} else {
				yy -= random(0, y);
			}
		}
		RSTile dest = new RSTile(xx, yy, t.getWorldLocation().getPlane());
		if (!methods.calc.tileOnMap(dest)) {
			dest = getClosestTileOnMap(dest);
		}
		Point p = methods.calc.tileToMinimap(dest);
		if (p.getX() != -1 && p.getY() != -1) {
			methods.mouse.move(p);
			Point p2 = methods.calc.tileToMinimap(dest);
			if (p2.getX() != -1 && p2.getY() != -1) {
				if (!methods.mouse.getLocation().equals(p2)) {//We must've moved while walking, move again!
					methods.mouse.move(p2);
				}
				if (!methods.mouse.getLocation().equals(p2)) {//Get exact since we're moving... should be removed?
					methods.mouse.hop(p2);
				}
				methods.mouse.click(true);
				return true;
			}
		}
		return false;
	}

	/**
	 * Walks to the given tile using the minimap with given randomness.
	 *
	 * @param t The tile to walk to.
	 * @param r The maximum deviation from the tile to allow.
	 * @return <tt>true</tt> if the tile was clicked; otherwise <tt>false</tt>.
	 */
	public boolean walkTileMM(final RSTile t, final int r) {
		int x = t.getWorldLocation().getX();
		int y = t.getWorldLocation().getY();
		if (random(1, 2) == random(1, 2)) {
			x += random(0, r);
		} else {
			x -= random(0, r);
		}
		if (random(1, 2) == random(1, 2)) {
			y += random(0, r);
		} else {
			y -= random(0, r);
		}
		RSTile dest = new RSTile(x, y);
		return !methods.players.getMyPlayer().getLocation().equals(dest) && walkTileMM(dest, 0, 0);
	}

	/**
	 * Walks to a tile using onScreen clicks and not the MiniMap. If the tile is
	 * not on the screen, it will find the closest tile that is on screen and it
	 * will walk there instead.
	 *
	 * @param tileToWalk Tile to walk.
	 * @return True if successful.
	 */
	public boolean walkTileOnScreen(final RSTile tileToWalk) {
		return methods.tiles.doAction(methods.calc.getTileOnScreen(tileToWalk), "Walk ");
	}


	/**
	 * Turns run on or off using the game GUI controls.
	 *
     * @param enable <tt>true</tt> to enable run, <tt>false</tt> to disable it.
     * @return	if run was attempted to be enabled <tt>true</tt>; otherwise <tt>false</tt>
     */
	public boolean setRun(final boolean enable) {
		if (isRunEnabled() != enable) {
			return methods.interfaces.getComponent(WidgetInfo.MINIMAP_RUN_ORB).doClick();
		}
        return false;
    }

	/**
	 * Generates a path from the player's current location to a destination
	 * tile.
	 *
	 * @param destination The destination tile.
	 * @return The path as an RSTile array.
	 */
	@Deprecated
	public RSTile[] findPath(RSTile destination) {
		RSLocalPath path = new RSLocalPath(methods, destination);
		if (path.isValid()) {
			RSTilePath tp = path.getCurrentTilePath();
			if (tp != null) {
				return tp.toArray();
			}
		}
		return new RSTile[0];
	}

	/**
	 * Randomizes a single tile.
	 *
	 * @param tile          The RSTile to randomize.
	 * @param maxXDeviation Max X distance from tile.getX().
	 * @param maxYDeviation Max Y distance from tile.getY().
	 * @return The randomized tile.
	 */
	@Deprecated
	public RSTile randomize(RSTile tile, int maxXDeviation, int maxYDeviation) {
		return tile.randomize(maxXDeviation, maxYDeviation);
	}

	/**
	 * Returns the closest tile on the minimap to a given tile.
	 *
	 * @param tile The destination tile.
	 * @return Returns the closest tile to the destination on the minimap.
	 */
	public RSTile getClosestTileOnMap(final RSTile tile) {
		if (!methods.calc.tileOnMap(tile) && methods.game.isLoggedIn()) {
			RSTile loc = methods.players.getMyPlayer().getLocation();
			RSTile walk = new RSTile((loc.getWorldLocation().getX() + tile.getWorldLocation().getX()) / 2,
					(loc.getWorldLocation().getY() + tile.getWorldLocation().getY()) / 2, tile.getWorldLocation().getPlane());
			return methods.calc.tileOnMap(walk) ? walk
					: getClosestTileOnMap(walk);
		}
		return tile;
	}

	/**
	 * Returns whether or not run is enabled.
	 *
	 * @return <tt>true</tt> if run mode is enabled; otherwise <tt>false</tt>.
	 */
	public boolean isRunEnabled() {
		return methods.settings.getSetting(173) == 1;
	}

	/**
	 * Returns the player's current run energy.
	 *
	 * @return The player's current run energy.
	 */
	public int getEnergy() {
		return methods.client.getEnergy();
	}

	/**
	 * Gets the destination tile (where the flag is on the minimap). If there is
	 * no destination currently, null will be returned.
	 *
	 * @return The current destination tile, or null.
	 */
	public RSTile getDestination() {

		return (methods.client.getLocalDestinationLocation() != null) ? new RSTile(methods.client.getLocalDestinationLocation().getX(),
				methods.client.getLocalDestinationLocation().getY(), methods.client.getPlane()) : null;
	}

	/**
	 * Gets the collision flags for a given floor level in the loaded region.
	 *
	 * @param plane The floor level (0, 1, 2 or 3).
	 * @return the collision flags.
	 */
	public int[][] getCollisionFlags(final int plane) {
		int[][] flags = null;
		if (getCollisionField() == null) {
			setCollisionField();
		}
		getCollisionField().setAccessible(true);
		try {
			flags = ((CollisionData[]) getCollisionField().get(methods.client))[plane].getFlags();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return flags;
	}

	/**
	 * Sets the reflective field to access for CollisionMaps
	 */
	private void setCollisionField() {
		for (Field field : methods.client.getClass().getDeclaredFields()) {
			//Checks if the field type is an array and if the name is shorter than 5 characters
			if (field.getType().getTypeName().contains("[]") && field.getType().getTypeName().length() < 5) {
				if (field.getModifiers() == 8) {
					field.setAccessible(true);
					String obType = getObType(field);
					if (obType != null) {
						this.collisionField = field;
					}
				}
			}
		}
	}

	/**
	 * Gets the field for CollisionMaps
	 * @return
	 */
	private Field getCollisionField() {
		return this.collisionField;
	}

	/**
	 * Checks the obfuscated field to determine if it is collisiondata or not. If it is, then the method will return
	 * its obfuscated name.
	 * @param field The field from the client class to check
	 * @return The obfuscated type name for CollisionData[]
	 */
	public String getObType(Field field) {
		String typeName = null;
		try {
			typeName = ((CollisionData[]) field.get(methods.client)).getClass().getTypeName();
		} catch (IllegalAccessException | ClassCastException e) {
			//This will cause a number of class cast exceptions while searching for a match
			//This is a byproduct of using reflection and attempting to create an algorithm
			//To find the field we want
		}
		return typeName;
	}


	// DEPRECATED

	/**
	 * Randomizes a single tile.
	 *
	 * @param tile          The RSTile to randomize.
	 * @param maxXDeviation Max X distance from tile.getX().
	 * @param maxYDeviation Max Y distance from tile.getY().
	 * @return The randomized tile.
	 *             .
	 */
	@Deprecated
	public RSTile randomizeTile(RSTile tile, int maxXDeviation,
	                            int maxYDeviation) {
		return randomize(tile, maxXDeviation, maxYDeviation);
	}

	/**
	 * Walks towards the end of a path. This method should be looped.
	 *
	 * @param path The path to walk along.
	 * @return <tt>true</tt> if the next tile was reached; otherwise
	 *         <tt>false</tt>.
	 * @see #walkPathMM(RSTile[], int)
	 */
	@Deprecated
	public boolean walkPathMM(RSTile[] path) {
		return walkPathMM(path, 16);
	}

	/**
	 * Walks towards the end of a path. This method should be looped.
	 *
	 * @param path    The path to walk along.
	 * @param maxDist See {@link #nextTile(RSTile[], int)}.
	 * @return <tt>true</tt> if the next tile was reached; otherwise
	 *         <tt>false</tt>.
	 * @see #walkPathMM(RSTile[], int, int)
	 */
	@Deprecated
	public boolean walkPathMM(RSTile[] path, int maxDist) {
		return walkPathMM(path, maxDist, 1, 1);
	}

	/**
	 * Walks towards the end of a path. This method should be looped.
	 *
	 * @param path  The path to walk along.
	 * @param randX The X value to randomize each tile in the path by.
	 * @param randY The Y value to randomize each tile in the path by.
	 * @return <tt>true</tt> if the next tile was reached; otherwise
	 *         <tt>false</tt>.
	 * @see #walkPathMM(RSTile[], int, int, int)
	 */
	@Deprecated
	public boolean walkPathMM(RSTile[] path, int randX, int randY) {
		return walkPathMM(path, 16, randX, randY);
	}

	/**
	 * Walks towards the end of a path. This method should be looped.
	 *
	 * @param path    The path to walk along.
	 * @param maxDist See {@link #nextTile(RSTile[], int)}.
	 * @param randX   The X value to randomize each tile in the path by.
	 * @param randY   The Y value to randomize each tile in the path by.
	 * @return <tt>true</tt> if the next tile was reached; otherwise
	 *         <tt>false</tt>.
	 */
	@Deprecated
	public boolean walkPathMM(RSTile[] path, int maxDist, int randX, int randY) {
		try {
			RSTile next = nextTile(path, maxDist);
			return next != null && walkTileMM(next, randX, randY);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Walks to the end of a path via the screen. This method should be looped.
	 *
	 * @param path The path to walk along.
	 * @return <tt>true</tt> if the next tile was reached; otherwise
	 *         <tt>false</tt>.
	 * @see #walkPathOnScreen(RSTile[], int)
	 */
	@Deprecated
	public boolean walkPathOnScreen(RSTile[] path) {
		return walkPathOnScreen(path, 16);
	}

	/**
	 * Walks a path using onScreen clicks and not the MiniMap. If the next tile
	 * is not on the screen, it will find the closest tile that is on screen and
	 * it will walk there instead.
	 *
	 * @param path    Path to walk.
	 * @param maxDist Max distance between tiles in the path.
	 * @return True if successful.
	 */
	@Deprecated
	public boolean walkPathOnScreen(RSTile[] path, int maxDist) {
		RSTile next = nextTile(path, maxDist);
		if (next != null) {
			RSTile os = methods.calc.getTileOnScreen(next);
			return os != null && methods.tiles.doAction(os, "Walk");
		}
		return false;
	}

	/**
	 * Reverses an array of tiles.
	 *
	 * @param other The <tt>RSTile</tt> path array to reverse.
	 * @return The reverse <tt>RSTile</tt> path for the given <tt>RSTile</tt>
	 *         path.
	 */
	@Deprecated
	public RSTile[] reversePath(RSTile[] other) {
		RSTile[] t = new RSTile[other.length];
		for (int i = 0; i < t.length; i++) {
			t[i] = other[other.length - i - 1];
		}
		return t;
	}

	/**
	 * Returns the next tile to walk to on a path.
	 *
	 * @param path The path.
	 * @return The next <tt>RSTile</tt> to walk to on the provided path; or
	 *         <code>null</code> if far from path or at destination.
	 * @see #nextTile(RSTile[], int)
	 */
	@Deprecated
	public RSTile nextTile(RSTile path[]) {
		return nextTile(path, 17);
	}

	/**
	 * Returns the next tile to walk to in a path.
	 *
	 * @param path     The path.
	 * @param skipDist If the distance to the tile after the next in the path is less
	 *                 than or equal to this distance, the tile after next will be
	 *                 returned rather than the next tile, skipping one. This
	 *                 interlacing aids continuous walking.
	 * @return The next <tt>RSTile</tt> to walk to on the provided path; or
	 *         <code>null</code> if far from path or at destination.
	 */
	@Deprecated
	public RSTile nextTile(RSTile path[], int skipDist) {
		int dist = 99;
		int closest = -1;
		for (int i = path.length - 1; i >= 0; i--) {
			RSTile tile = path[i];
			int d = methods.calc.distanceTo(tile);
			if (d < dist) {
				dist = d;
				closest = i;
			}
		}

		int feasibleTileIndex = -1;

		for (int i = closest; i < path.length; i++) {

			if (methods.calc.distanceTo(path[i]) <= skipDist) {
				feasibleTileIndex = i;
			} else {
				break;
			}
		}

		if (feasibleTileIndex == -1) {
			return null;
		} else {
			return path[feasibleTileIndex];
		}
	}

	/**
	 * Randomizes a path of tiles.
	 *
	 * @param path          The RSTiles to randomize.
	 * @param maxXDeviation Max X distance from tile.getX().
	 * @param maxYDeviation Max Y distance from tile.getY().
	 * @return The new, randomized path.
	 */
	@Deprecated
	public RSTile[] randomizePath(RSTile[] path, int maxXDeviation,
	                              int maxYDeviation) {
		RSTile[] rez = new RSTile[path.length];
		for (int i = 0; i < path.length; i++) {
			rez[i] = randomize(path[i], maxXDeviation, maxYDeviation);
		}
		return rez;
	}
}
