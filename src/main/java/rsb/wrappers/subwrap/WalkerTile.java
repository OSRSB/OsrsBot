package rsb.wrappers.subwrap;

import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import rsb.methods.MethodContext;
import rsb.methods.Web;
import rsb.wrappers.RSTile;
import rsb.wrappers.common.Clickable07;
import rsb.wrappers.common.Positionable;

public class WalkerTile extends RSTile implements Clickable07, Positionable {

    private MethodContext ctx;

    private TYPES type;

    public WalkerTile(RSTile tile) {
        super(tile.getWorldLocation());
        this.ctx = Web.methods;
        type = TYPES.WORLD;
    }

    public WalkerTile(WalkerTile tile) {
        super(tile.getX(), tile.getY(), Web.methods.client.getPlane());
        this.ctx = Web.methods;
        type = tile.type;
    };

    public WalkerTile(WorldPoint point) {
        super(point);
        this.ctx = Web.methods;
        type = TYPES.WORLD;
    }


    public WalkerTile(int x, int y, int plane) {
        super(x, y, plane);
        this.ctx = Web.methods;
        this.type = TYPES.WORLD;
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

    public WalkerTile toWorldTile() {
        WalkerTile walkerTile = new WalkerTile(this);
        if (walkerTile.type == TYPES.LOCAL) {
            WorldPoint point = WorldPoint.fromLocal(ctx.client, new LocalPoint(x, y));
            walkerTile.x = point.getX();
            walkerTile.y = point.getY();
            walkerTile.plane = ctx.client.getPlane();
        }
        if (walkerTile.type == TYPES.SCENE) {
            walkerTile.x = ctx.client.getBaseX() + x;
            walkerTile.y = ctx.client.getBaseY() + y;
            //WorldPoint.fromScene(ctx.client, x, y, plane);
        }
        walkerTile.type = TYPES.WORLD;
        return walkerTile;
    }

    public WalkerTile toLocalTile() {
        WalkerTile walkerTile = new WalkerTile(this);
        if (walkerTile.type == TYPES.WORLD) {
            int baseX = ctx.client.getBaseX();
            int baseY = ctx.client.getBaseY();
            LocalPoint point = LocalPoint.fromScene(x - baseX, y - baseY);
            walkerTile.x = point.getX();
            walkerTile.y = point.getY();
        } if (walkerTile.type == TYPES.SCENE) {
            LocalPoint point = LocalPoint.fromScene(x, y);
            walkerTile.x = point.getX();
            walkerTile.y = point.getY();
        }
        walkerTile.type = TYPES.LOCAL;
        return walkerTile;
    }

    public WalkerTile toSceneTile() {
        WalkerTile walkerTile = new WalkerTile(this);
        if (walkerTile.type != TYPES.SCENE) {
            if (walkerTile.type == TYPES.WORLD) {
                walkerTile.toLocalTile();
            }
            walkerTile.x = walkerTile.x >>> Perspective.LOCAL_COORD_BITS;
            walkerTile.y = walkerTile.y >>> Perspective.LOCAL_COORD_BITS;
            walkerTile.type = TYPES.SCENE;
        }
        return walkerTile;
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
        ANIMABLE, LOCAL, WORLD, SCENE;
    }

    @Override
    public WalkerTile getLocation() {
        return this;
    }

    public int distanceTo(Positionable positionable) {
        return (int) ctx.calc.distanceBetween(this.toWorldTile(), positionable.getLocation());
    }

    public double distanceToDouble(Positionable positionable) {
        return ctx.calc.distanceBetween(this.toWorldTile(), positionable.getLocation());
    }

    public WalkerTile translate(int x, int y) {
        this.x = this.x + x;
        this.y = this.y + y;
        return this;
    }
}
