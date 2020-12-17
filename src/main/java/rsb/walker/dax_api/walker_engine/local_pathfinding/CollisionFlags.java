package rsb.walker.dax_api.walker_engine.local_pathfinding;

public class CollisionFlags {
    public static final int OPEN          = 0x0;
    public static final int OCCUPIED      = 0x100;
    public static final int SOLID         = 0x20000;
    public static final int BLOCKED       = 0x200000;
    public static final int CLOSED        = 0xFFFFFF;
    public static final int INITIALIZED = 0x1000000;

    public static final int NORTH  = 0x2; //fences etc
    public static final int EAST   = 0x8;
    public static final int SOUTH  = 0x20;
    public static final int WEST   = 0x80;

    public static final int NORTHWEST = 0x1;
    public static final int NORTHEAST = 0x4;
    public static final int SOUTHEAST = 0x10;
    public static final int SOUTHWEST = 0x40;

    public static final int EAST_NORTH = EAST | NORTH;
    public static final int EAST_SOUTH = EAST | SOUTH;
    public static final int WEST_SOUTH = WEST | SOUTH;
    public static final int WEST_NORTH = WEST | NORTH;

    public static final int BLOCKED_NORTH_WALL = 0x400; // blocked == walls
    public static final int BLOCKED_EAST_WALL = 0x1000;
    public static final int BLOCKED_SOUTH_WALL = 0x4000;
    public static final int BLOCKED_WEST_WALL = 0x10000;

    public static final int BLOCKED_NORTHEAST = 0x800;
    public static final int BLOCKED_SOUTHEAST = 0x2000;
    public static final int BLOCKED_NORTHWEST = 0x200;
    public static final int BLOCKED_SOUTHWEST = 0x8000;

    public static final int BLOCKED_EAST_NORTH = BLOCKED_EAST_WALL | BLOCKED_NORTH_WALL;
    public static final int BLOCKED_EAST_SOUTH = BLOCKED_EAST_WALL | BLOCKED_SOUTH_WALL;
    public static final int BLOCKED_WEST_SOUTH = BLOCKED_WEST_WALL | BLOCKED_SOUTH_WALL;
    public static final int BLOCKED_WEST_NORTH = BLOCKED_WEST_WALL | BLOCKED_NORTH_WALL;

    public static boolean checkFlag(int flag, int checkFlag){
        return (flag & checkFlag) == checkFlag;
    }

}