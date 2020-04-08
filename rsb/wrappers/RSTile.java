package net.runelite.client.rsb.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.rsb.methods.MethodContext;

@Slf4j
public class RSTile {

    private final int NO_PLANE_SET = -99;

    private int x;

    private int y;

    private int plane;

    public RSTile(Tile tile) {
        this.x = tile.getWorldLocation().getX();
        this.y = tile.getWorldLocation().getY();
        this.plane = tile.getWorldLocation().getPlane();
    }

    public RSTile(int x, int y, int plane) {
        this.x = x;
        this.y = y;
        this.plane = plane;
    }

    public RSTile(int x, int y) {
        this.x = x;
        this.y = y;
        this.plane = -99;
    }

    public RSTile(WorldPoint worldPoint) {
        this.x = worldPoint.getX();
        this.y = worldPoint.getY();
        this.plane = worldPoint.getPlane();
    }

    public LocalPoint getLocalLocation(MethodContext ctx) {
        return this.getTile(ctx).getLocalLocation();
    }

    public WorldPoint getWorldLocation() {
        return new WorldPoint(x, y, plane);
    }


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
            return (tile.x == x) && (tile.y == y) && (tile.plane == plane);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(X: " + x + ", Y:" + y + ", Plane:" + plane + ")";
    }
}
