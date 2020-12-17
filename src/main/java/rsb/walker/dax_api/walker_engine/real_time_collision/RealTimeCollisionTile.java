package rsb.walker.dax_api.walker_engine.real_time_collision;

import rsb.walker.dax_api.shared.PathFindingNode;
import rsb.walker.dax_api.shared.RSRegion;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;


public class RealTimeCollisionTile extends PathFindingNode {

    private int x, y, z, collisionData;

    private RealTimeCollisionTile(int x, int y, int z, int collisionData){
        this.x = x;
        this.y = y;
        this.z = z;
        this.collisionData = collisionData;
    }

    public void setCollisionData(int collisionData) {
        this.collisionData = collisionData;
    }

    public boolean blockedNorth(){
        return blockedNorth(this.collisionData);
    }

    public boolean blockedEast(){
        return blockedEast(this.collisionData);
    }
    public boolean blockedSouth(){
        return blockedSouth(this.collisionData);
    }
    public boolean blockedWest(){
        return blockedWest(this.collisionData);
    }

    public boolean isWalkable(){
        return isWalkable(this.collisionData);
    }

    public boolean isInitialized(){
        return isInitialized(this.collisionData);
    }

    public static boolean blockedNorth(int collisionData){
        return CollisionFlags.check(collisionData, CollisionFlags.NORTH) || CollisionFlags.check(collisionData, CollisionFlags.BLOCKED_NORTH_WALL);
    }

    public static boolean blockedEast(int collisionData){
        return CollisionFlags.check(collisionData, CollisionFlags.EAST) || CollisionFlags.check(collisionData, CollisionFlags.BLOCKED_EAST_WALL);
    }
    public static boolean blockedSouth(int collisionData){
        return CollisionFlags.check(collisionData, CollisionFlags.SOUTH) || CollisionFlags.check(collisionData, CollisionFlags.BLOCKED_SOUTH_WALL);
    }
    public static boolean blockedWest(int collisionData){
        return CollisionFlags.check(collisionData, CollisionFlags.WEST) || CollisionFlags.check(collisionData, CollisionFlags.BLOCKED_WEST_WALL);
    }

    public static boolean isWalkable(int collisionData){
//        if(!isInitialized(collisionData))
//            return true;
        return !(CollisionFlags.check(collisionData, CollisionFlags.OCCUPIED)
                || CollisionFlags.check(collisionData, CollisionFlags.SOLID)
                || CollisionFlags.check(collisionData, CollisionFlags.BLOCKED)
                || CollisionFlags.check(collisionData, CollisionFlags.CLOSED));
    }

    public static boolean isInitialized(int collisionData){
        return !(blockedNorth(collisionData) && blockedEast(collisionData) && blockedSouth(collisionData) && blockedWest(collisionData) && !isWalkable(collisionData)) || CollisionFlags.check(collisionData, CollisionFlags.INITIALIZED);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }


    @Override
    public Collection<PathFindingNode> getNeighbors(HashSet<RSRegion> limit) {
        return null;
    }

    @Override
    public Collection<PathFindingNode> getNeighbors() {
        Collection<PathFindingNode> neighbors = new HashSet<>();
        boolean nNeighbor = false, eNeighbor = false, sNeighbor = false, wNeighbor = false;
        RealTimeCollisionTile n = get(getX(), getY() + 1, getZ());
        if (!blockedNorth()) {
            if (n != null && n.isWalkable()){
                neighbors.add(n);
                nNeighbor = true;
            }
        }
        RealTimeCollisionTile e = get(getX() + 1, getY(), getZ());
        if (!blockedEast()) {
            if (e != null && e.isWalkable()){
                neighbors.add(e);
                eNeighbor = true;
            }
        }
        RealTimeCollisionTile s = get(getX(), getY() - 1, getZ());
        if (!blockedSouth()) {
            if (s != null && s.isWalkable()){
                neighbors.add(s);
                sNeighbor = true;
            }
        }
        RealTimeCollisionTile w = get(getX() - 1, getY(), getZ());
        if (!blockedWest()) {
            if (w != null && w.isWalkable()){
                neighbors.add(w);
                wNeighbor = true;
            }
        }

        if (nNeighbor && eNeighbor){
            if (!n.blockedEast() && !e.blockedNorth()) {
                RealTimeCollisionTile ne = get(getX() + 1, getY() + 1, getZ());
                if (ne != null && ne.isWalkable()) {
                    neighbors.add(ne);
                }
            }
        }
        if (sNeighbor && eNeighbor){
            if (!s.blockedEast() && !e.blockedNorth()) {
                RealTimeCollisionTile se = get(getX() + 1, getY() - 1, getZ());
                if (se != null && se.isWalkable()) {
                    neighbors.add(se);
                }
            }
        }
        if (sNeighbor && wNeighbor){
            if (!s.blockedWest() && !w.blockedSouth()) {
                RealTimeCollisionTile sw = get(getX() - 1, getY() - 1, getZ());
                if (sw != null && sw.isWalkable()) {
                    neighbors.add(sw);
                }
            }
        }
        if (nNeighbor && wNeighbor){
            if (!n.blockedWest() && !w.blockedNorth()) {
                RealTimeCollisionTile nw = get(getX() - 1, getY() + 1, getZ());
                if (nw != null && nw.isWalkable()) {
                    neighbors.add(nw);
                }
            }
        }
        return neighbors;
    }

    private static HashMap<Integer, HashMap<Integer, HashMap<Integer, RealTimeCollisionTile>>> xMap = new HashMap<>();
    private static HashSet<RealTimeCollisionTile> allCached = new HashSet<>();

    public static RealTimeCollisionTile get(int x, int y, int z){
        HashMap<Integer, HashMap<Integer, RealTimeCollisionTile>> yMap = xMap.get(x);
        if (yMap == null){
            return null;
        }
        HashMap<Integer, RealTimeCollisionTile> zMap = yMap.get(y);
        if (zMap == null){
            return null;
        }
        return zMap.get(z);
    }

    public static RealTimeCollisionTile create(int x, int y, int z, int collision){
        RealTimeCollisionTile realTimeCollisionTile = new RealTimeCollisionTile(x, y, z, collision);
        if (!realTimeCollisionTile.isInitialized()){
            return null;
        }
        HashMap<Integer, HashMap<Integer, RealTimeCollisionTile>> yMap = xMap.computeIfAbsent(realTimeCollisionTile.getX(), k -> new HashMap<>());
        HashMap<Integer, RealTimeCollisionTile> zMap = yMap.computeIfAbsent(realTimeCollisionTile.getY(), k -> new HashMap<>());
        zMap.put(realTimeCollisionTile.getZ(), realTimeCollisionTile);
        allCached.add(realTimeCollisionTile);
        return realTimeCollisionTile;
    }

    public static HashSet<RealTimeCollisionTile> getAllInitialized(){
        return allCached;
    }

    public static void clearMemory(){
        xMap = new HashMap<>();
        allCached = new HashSet<>();
    }

}
