package net.runelite.rsb.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.methods.Web;
import net.runelite.rsb.wrappers.common.ClickBox;
import net.runelite.rsb.wrappers.common.Clickable07;
import net.runelite.rsb.wrappers.common.Positionable;
import net.runelite.rsb.wrappers.RSTile;

import java.awt.*;

/**
 * A class to assign coordinates and game-levels to tile objects for internal use
 * Should be using World location values. Not local or scene.
 */
@Slf4j
public class RSTile implements Clickable07, Positionable {

    private final int NO_PLANE_SET = -99;
    protected int x;
    protected int y;
    protected int plane;

    private MethodContext ctx;
    private TYPES type;

    public enum TYPES {
        ANIMABLE, LOCAL, WORLD, SCENE;
    }
    /**
     * Creates an RSTile object based on a RuneScape tile object.
     * @param tile  The RuneScape tile to assign coordinates from
     */
    public RSTile(Tile tile) {
        this.x = tile.getWorldLocation().getX();
        this.y = tile.getWorldLocation().getY();
        this.plane = tile.getWorldLocation().getPlane();
        this.ctx = Web.methods;
        type = TYPES.WORLD;
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
        this.ctx = Web.methods;
        this.type = TYPES.WORLD;
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
        this.ctx = Web.methods;
        type = TYPES.WORLD;
    }

    public RSTile(int x, int y, int plane, TYPES type) {
        this(x,y, plane);
        this.ctx = Web.methods;
        this.type = type;
    }

    public RSTile(int x, int y, TYPES type) {
        this(x, y);
        this.ctx = Web.methods;
        this.type = type;
    }

    public RSTile(RSTile tile) {
        this(tile.getX(), tile.getY(), Web.methods.client.getPlane());
        this.ctx = Web.methods;
        type = tile.type;
    };

    public LocalPoint getLocalLocation(MethodContext ctx) {
        return this.getTile(ctx).getLocalLocation();
    }

    public LocalPoint getLocalLocation() {
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

    @Override
    public RSTile getLocation() {
        return new RSTile(this);
    }


    @Override
    public boolean isClickable() {
        return ctx.calc.tileOnScreen(this.toWorldTile());
    }


    @Override
    public boolean doAction(String action) {
        return ctx.tiles.doAction(this.toWorldTile(), action);
    }

    @Override
    public boolean doAction(String action, String option) {
        return ctx.tiles.doAction(this.toWorldTile(), action, option);
    }

    @Override
    public boolean doClick() {
        return ctx.tiles.doAction(this.toWorldTile(), "Walk here");
    }

    @Deprecated
    @Override
    public boolean doClick(boolean leftClick) {
        return ctx.tiles.doAction(this.toWorldTile(), "Walk here");
    }

    @Override
    public boolean doHover() {
        Point p = ctx.calc.tileToScreen(this.toWorldTile());
        if (isClickable()) {
            ctx.mouse.move(p);
            return true;
        }
        return false;
    }

    public Shape getClickShape() {
        return ctx.calc.getTileBoundsPoly(this.toWorldTile(), 0);
    }
    public ClickBox getClickBox() {
        return new ClickBox(this);
    }

    @Override
    public boolean turnTo() {
        if (isClickable()) {
            ctx.camera.turnTo(this.toWorldTile());
            return true;
        }
        return false;
    }


    public boolean isOnScreen() {
        return ctx.calc.tileOnScreen(this.toWorldTile());
    }

    public RSTile toWorldTile() {
        RSTile tile = new RSTile(this);
        if (tile.type == TYPES.LOCAL) {
            WorldPoint point = WorldPoint.fromLocal(ctx.client, new LocalPoint(x, y));
            tile.x = point.getX();
            tile.y = point.getY();
            tile.plane = ctx.client.getPlane();
        }
        if (tile.type == TYPES.SCENE) {
            tile.x = ctx.client.getBaseX() + x;
            tile.y = ctx.client.getBaseY() + y;
            tile.plane = ctx.client.getPlane();
            //WorldPoint.fromScene(ctx.client, x, y, plane);
        }
        tile.type = TYPES.WORLD;
        return tile;
    }

    public RSTile toLocalTile() {
        RSTile tile = new RSTile(this);
        if (tile.type == TYPES.WORLD) {
            int baseX = ctx.client.getBaseX();
            int baseY = ctx.client.getBaseY();
            LocalPoint point = LocalPoint.fromScene(x - baseX, y - baseY);
            tile.x = point.getX();
            tile.y = point.getY();
        } if (tile.type == TYPES.SCENE) {
            LocalPoint point = LocalPoint.fromScene(x, y);
            tile.x = point.getX();
            tile.y = point.getY();
        }
        tile.type = TYPES.LOCAL;
        return tile;
    }

    public RSTile toSceneTile() {
        RSTile tile = new RSTile(this);
        if (tile.type != TYPES.SCENE) {
            if (tile.type == TYPES.WORLD) {
                tile.toLocalTile();
            }
            LocalPoint point = LocalPoint.fromWorld(ctx.client, x, y);
            tile.x = point.getSceneX();
            tile.y = point.getSceneY();
            tile.type = TYPES.SCENE;
        }
        return tile;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlane() {
        return plane;
    }

    public TYPES getType() {
        return type;
    }


    public int distanceTo(Positionable positionable) {
        return (int) ctx.calc.distanceBetween(this.toWorldTile(), positionable.getLocation());
    }

    public double distanceToDouble(Positionable positionable) {
        return ctx.calc.distanceBetween(this.toWorldTile(), positionable.getLocation());
    }

    /**
     * Changes the underlying tile
     * @param x
     * @param y
     * @return
     */
    public RSTile translate(int x, int y) {
        this.x = this.x + x;
        this.y = this.y + y;
        return this;
    }
    /**
     * Changes the underlying tile
     * @param x
     * @param y
     * @param plane
     * @return
     */
    public RSTile translate(int x, int y, int plane) {
        this.x = this.x + x;
        this.y = this.y + y;
        this.plane = this.plane + plane;
        return this;
    }

    /**
     * Changes the underlying tile
     * @param tile
     * @return
     */
    public RSTile translate(RSTile tile) {
        this.x = this.x + tile.x;
        this.y = this.y + tile.y;
        this.plane = this.plane + tile.plane;
        return this;
    }

    /**
     * Returns a new tile offset from original
     * @param x
     * @param y
     * @return
     */
    public RSTile offset(int x, int y) {
        return new RSTile(this.x + x, this.y + y, this.plane);
    }

    /**
     * Returns a new tile offset from original
     * @param x
     * @param y
     * @param plane
     * @return
     */
    public RSTile offset(int x, int y, int plane) {
        return new RSTile(this.x + x, this.y + y, this.plane + plane);
    }

    /**
     * Returns a new tile offset from original
     * @param tile
     * @return
     */
    public RSTile offset(RSTile tile) {
        return new RSTile(this.x + tile.x, this.y + tile.y, this.plane + tile.plane);
    }

    public int getWorldX() {
        if (type == TYPES.WORLD) {
            return x;
        }
        return toWorldTile().x;
    }

    public int getWorldY() {
        if (type == TYPES.WORLD) {
            return y;
        }
        return toWorldTile().y;
    }

    public int getSceneX() {
        if (type == TYPES.SCENE) {
            return x;
        }
        return toSceneTile().x;
    }
    public int getSceneY() {
        if (type == TYPES.SCENE) {
            return y;
        }
        return toSceneTile().y;
    }
}
