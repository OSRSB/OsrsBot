package rsb.walker.dax_api.api_lib.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.runelite.api.coords.WorldPoint;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.wrappers.common.Positionable;

public class Point3D {


    private int x, y, z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public JsonElement toJson() {
        return new Gson().toJsonTree(this);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Positionable toPositionable() {
        return new Positionable() {
            public WalkerTile getAnimablePosition() {
                return new WalkerTile(x, y, z);
            }

            public boolean adjustCameraTo() {
                return false;
            }

            @Override
            public WalkerTile getLocation() {
                return new WalkerTile(new WorldPoint(x, y, z));
            }

            @Override
            public boolean turnTo() {
                return false;
            }
        };
    }

    public static Point3D fromPositionable(Positionable positionable) {
        WalkerTile WalkerTile = positionable.getLocation();
        return new Point3D(WalkerTile.getX(), WalkerTile.getY(), WalkerTile.getPlane());
    }

}
