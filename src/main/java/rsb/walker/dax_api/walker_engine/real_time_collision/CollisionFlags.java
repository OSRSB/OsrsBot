package rsb.walker.dax_api.walker_engine.real_time_collision;


public class CollisionFlags {

    public static final int OPEN                = 0x0;
    public static final int OCCUPIED            = 0x100;
    public static final int SOLID               = 0x20000;
    public static final int BLOCKED             = 0x200000;
    public static final int CLOSED              = 0xFFFFFF;
    public static final int INITIALIZED         = 0x1000000; //or unitialized. i dont fucking know

    public static final int NORTH               = 0x2;
    public static final int EAST                = 0x8;
    public static final int SOUTH               = 0x20;
    public static final int WEST                = 0x80;

    public static final int BLOCKED_NORTH_WALL  = 0x400;
    public static final int BLOCKED_EAST_WALL   = 0x1000;
    public static final int BLOCKED_SOUTH_WALL  = 0x4000;
    public static final int BLOCKED_WEST_WALL   = 0x10000;

    public static boolean check(int flag, int checkFlag){
        return (flag & checkFlag) == checkFlag;
    }
}
