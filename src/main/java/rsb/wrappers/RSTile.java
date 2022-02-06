package rsb.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import rsb.methods.MethodContext;

/**
 * A class to assign coordinates and game-levels to tile objects for internal use
 * Should be using World location values. Not local or scene.
 */
@Slf4j
public class RSTile  {

    private final int NO_PLANE_SET = -99;
    protected int x;
    protected int y;
    protected int plane;

    /**
     * Creates an RSTile object based on a RuneScape tile object.
     * @param tile  The RuneScape tile to assign coordinates from
     */
    public RSTile(Tile tile) {
        this.x = tile.getWorldLocation().getX();
        this.y = tile.getWorldLocation().getY();
        this.plane = tile.getWorldLocation().getPlane();
    }

    /**
     * Creates a RSTile object based on the following x and y associated with a particular plane
     * @param x     the x value unassigned to any particular plane
     * @param y     the y value unassigned to any particular plane
     * @param plane the plane (game-level) for this particular tile
     */
    public RSTile(int x, int y, int plane) {
        this.x = x;
        this.y = y;
        this.plane = plane;
    }

    /**
     * Creates a RSTile object based on the following x and y unassigned to a plane
     * @deprecated Should not be used. Planes are vital to determining game level
     * @param x     the x value unassigned to any particular plane
     * @param y     the y value unassigned to any particular plane
     */
    @Deprecated
    public RSTile(int x, int y) {
        this.x = x;
        this.y = y;

        //Creates a debug for later development to fix instances in the API where this occurs
        String debugMsg =
                "\n This exception is thrown when the plane is not set when creating a new tile. It isn't necessarily an issue." +
                "\n However,it is useful for fixing potential issues within the API. If the exception is thrown within the API report " +
                "\n this exception.";
        NoPlaneException exception = new NoPlaneException("No Plane Set. Defaulting to -99.");
        try {
            this.plane = NO_PLANE_SET;
            throw exception;
        } catch (NoPlaneException e) {
            log.debug(debugMsg, exception);
        }
    }

    /**
     * Creates an RSTile using the coordinates from the passed WorldPoint
     * This is not the same as a LocalPoint and local values will cause issues if attempted to be constructed
     * as a RSTile in this manner.
     * @param worldPoint    a point object containing World relevant data such as x, y, and plane.
     */
    public RSTile(WorldPoint worldPoint) {
        this.x = worldPoint.getX();
        this.y = worldPoint.getY();
        this.plane = worldPoint.getPlane();
    }

    public LocalPoint getLocalLocation(MethodContext ctx) {
        return this.getTile(ctx).getLocalLocation();
    }

    /**
     * Gets the WorldPoint (with world x y values) for this RSTile
     * @return  the WorldPoint values for this RSTile
     */
    public WorldPoint getWorldLocation() {
        return new WorldPoint(x, y, plane);
    }

    /**
     * Returns this tile object based on a specified method context (bot instance)
     * @param ctx   The method context to check for
     * @return      The in-game Tile object unwrapped
     */
    public Tile getTile(MethodContext ctx) {
        if (plane == NO_PLANE_SET) {
            plane = ctx.client.getPlane();
        }
        WorldPoint worldPoint = new WorldPoint(x, y, plane);
        if (worldPoint.isInScene(ctx.client)) {
            LocalPoint localPoint = LocalPoint.fromWorld(ctx.client, worldPoint);
            return ctx.client.getScene().getTiles()[worldPoint.getPlane()][localPoint.getSceneX()][localPoint.getSceneY()];
        }
        return null;
    }

    /**
     * Generates a tile with randomized values relative to this RSTile's location
     * @param maxXDeviation     the deviation amount for x
     * @param maxYDeviation     the deviation amount for y
     * @return  a RSTile that is deviated relative to this RSTile object
     */
    public RSTile randomize(final int maxXDeviation, final int maxYDeviation) {
        int x = this.x;
        int y = this.y;
        if (maxXDeviation > 0) {
            double d = Math.random() * 2 - 1.0;
            d *= maxXDeviation;
            x += (int) d;
        }
        if (maxYDeviation > 0) {
            double d = Math.random() * 2 - 1.0;
            d *= maxYDeviation;
            y += (int) d;
        }
        return new RSTile(x, y);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof RSTile) {
            final RSTile tile = (RSTile) obj;
            return (tile.x == x) && (tile.y == y) && ((tile.plane != NO_PLANE_SET && this.plane != NO_PLANE_SET) && (tile.plane == plane));
        }
        return false;
    }

    @Override
    public String toString() {
        return "(X: " + x + ", Y:" + y + ", Plane:" + plane + ")";
    }


    class NoPlaneException extends Exception {
        NoPlaneException(String message) {
            super(message);
        }
    }

    /**
     * Do not use
     * @return Always false
     */
    @Deprecated
    public boolean turnTo() {
        return false;
    }

}
