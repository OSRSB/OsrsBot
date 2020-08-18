package net.runelite.client.rsb.wrappers.subwrap;

import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.methods.Web;
import net.runelite.client.rsb.wrappers.RSTile;
import net.runelite.client.rsb.wrappers.common.Clickable07;
import net.runelite.client.rsb.wrappers.common.Positionable;

public class WalkerTile extends RSTile implements Clickable07, Positionable {

    private MethodContext ctx;

    private TYPES type;

    public WalkerTile(RSTile tile) {
        super(tile.getWorldLocation());
        this.ctx = Web.methods;
        type = TYPES.WORLD;
    }

    public WalkerTile(WalkerTile tile) {
        super(tile.getWorldLocation());
        this.ctx = Web.methods;
        type = TYPES.WORLD;
    };

    public WalkerTile(WorldPoint point) {
        super(point);
        this.ctx = Web.methods;
        type = TYPES.WORLD;
    }


    public WalkerTile(int x, int y, int plane) {
        super(x, y, plane);
        this.ctx = Web.methods;
    }

    public WalkerTile(int x, int y, int plane, TYPES type) {
        super(x, y, plane);
        this.ctx = Web.methods;
        this.type = type;
    }

    public WalkerTile(int x, int y, TYPES type) {
        super(x, y);
        this.ctx = Web.methods;
        this.type = type;
    }

    public LocalPoint getLocalLocation() {
        return this.getTile(ctx).getLocalLocation();
    }

    @Override
    public boolean isClickable() {
        return ctx.calc.tileOnScreen(this);
    }


    @Override
    public boolean doAction(String action) {
        return ctx.tiles.doAction(this, action);
    }

    @Override
    public boolean doAction(String action, String option) {
        return ctx.tiles.doAction(this, action, option);
    }

    @Override
    public boolean doClick() {
        return ctx.tiles.doAction(this, "Walk here");
    }

    @Deprecated
    @Override
    public boolean doClick(boolean leftClick) {
        return ctx.tiles.doAction(this, "Walk here");
    }

    @Override
    public boolean doHover() {
        Point p = ctx.calc.tileToScreen(this);
        if (isClickable()) {
            ctx.mouse.move(p);
            return true;
        }
        return false;
    }

    @Override
    public boolean turnTo() {
        if (isClickable()) {
            ctx.camera.turnTo(this);
            return true;
        }
        return false;
    }

    public boolean isOnScreen() {
        return ctx.calc.tileOnScreen(this);
    }

    public WalkerTile toWorldTile() {
        if (getLocalLocation() != (new LocalPoint(x, y))) {
            return this;
        }
        WorldPoint point = getWorldLocation();
        x = point.getX();
        y = point.getY();
        plane = ctx.client.getPlane();
        type = TYPES.WORLD;
        return this;
    }

    public WalkerTile toLocalTile() {
        if (getWorldLocation() != (new WorldPoint(x, y, plane))) {
            return this;
        }
        LocalPoint point = getLocalLocation();
        x = point.getX();
        y = point.getY();
        type = TYPES.LOCAL;
        return this;
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

    public enum TYPES {
        ANIMABLE, LOCAL, WORLD;
    }

    @Override
    public WalkerTile getLocation() {
        return this;
    }

    public int distanceTo(Positionable positionable) {
        return (int) ctx.calc.distanceBetween(this, positionable.getLocation());
    }

    public double distanceToDouble(Positionable positionable) {
        return ctx.calc.distanceBetween(this, positionable.getLocation());
    }

    public WalkerTile translate(int x, int y) {
        this.x = this.x + x;
        this.y = this.y + y;
        return this;
    }
}
