package rsb.walker.dax_api.walker_engine.local_pathfinding;

import rsb.wrappers.subwrap.WalkerTile;

public class AStarNode {

    private int x, y, z, collisionData;
    private AStarNode parent;
    private int moveCost, heuristic, f;

    private boolean traversed, current, destination;

    public AStarNode(int x, int y, int z, int collisionData){
        this.x = x;
        this.y = y;
        this.z = z;
        this.collisionData = collisionData;
        moveCost = 0;
        heuristic = 0;
        f = 0;
        traversed = false;
        current = false;
        destination = false;
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
        return blockedNorth() && blockedEast() && blockedSouth() && blockedWest() && !isWalkable();
    }

    public static boolean blockedNorth(int collisionData){
        return CollisionFlags.checkFlag(collisionData, CollisionFlags.NORTH)
                || CollisionFlags.checkFlag(collisionData, CollisionFlags.BLOCKED_NORTH_WALL);
    }

    public static boolean blockedEast(int collisionData){
        return CollisionFlags.checkFlag(collisionData, CollisionFlags.EAST)
                || CollisionFlags.checkFlag(collisionData, CollisionFlags.BLOCKED_EAST_WALL);
    }
    public static boolean blockedSouth(int collisionData){
        return CollisionFlags.checkFlag(collisionData, CollisionFlags.SOUTH)
                || CollisionFlags.checkFlag(collisionData, CollisionFlags.BLOCKED_SOUTH_WALL);
    }
    public static boolean blockedWest(int collisionData){
        return CollisionFlags.checkFlag(collisionData, CollisionFlags.WEST)
                || CollisionFlags.checkFlag(collisionData, CollisionFlags.BLOCKED_WEST_WALL);
    }

    public static boolean isWalkable(int collisionData){
        return !(CollisionFlags.checkFlag(collisionData, CollisionFlags.OCCUPIED)
                || CollisionFlags.checkFlag(collisionData, CollisionFlags.SOLID)
                || CollisionFlags.checkFlag(collisionData, CollisionFlags.BLOCKED)
                || CollisionFlags.checkFlag(collisionData, CollisionFlags.CLOSED));
    }

    public static boolean isInitialized(int collisionData){
        return CollisionFlags.checkFlag(collisionData, CollisionFlags.INITIALIZED);
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

    public int getCollisionData() {
        return collisionData;
    }

    public AStarNode getParent() {
        return parent;
    }

    public void setParent(AStarNode parent) {
        this.parent = parent;
    }

    public int calculateMoveCost(AStarNode aStarNode){
        int moveCost = (int)(Math.sqrt(Math.pow((aStarNode.x - x) * 10, 2) + (Math.pow((aStarNode.y - y) * 10, 2))));
        if (parent != null){
            moveCost += parent.getMoveCost();
        }
        return moveCost;
    }

    public int getMoveCost() {
        return moveCost;
    }

    public void setMoveCost(int moveCost) {
        this.moveCost = moveCost;
    }

    public int calculateHeuristic(AStarNode destination){
        return (Math.abs(x-destination.x) + Math.abs(y-destination.y)) * 10;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    @Override
    public int hashCode() {
        return hashCode(this);
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (obj == this){
            return true;
        }
        if (!(obj instanceof AStarNode)){
            return false;
        }
        AStarNode compare = (AStarNode) obj;
        return compare.x == x && compare.y == y && compare.z == z;
    }

    public static int hashCode(AStarNode aStarNode) {
        long bits = 7L;
        bits = 31L * bits + Double.doubleToLongBits(aStarNode.x);
        bits = 31L * bits + Double.doubleToLongBits(aStarNode.y);
        bits = 31L * bits + Double.doubleToLongBits(aStarNode.z);
        return (int) (bits ^ (bits >> 32));
    }

    public static int hashCode(int x, int y, int z) {
        long bits = 7L;
        bits = 31L * bits + Double.doubleToLongBits(x);
        bits = 31L * bits + Double.doubleToLongBits(y);
        bits = 31L * bits + Double.doubleToLongBits(z);
        return (int) (bits ^ (bits >> 32));
    }

    public String stringID(){
        return x + " " + y + " " + z;
    }

    public WalkerTile toWalkerTile(){
        return new WalkerTile(x, y, z/*, TYPES.WORLD*/);
    }

    public static String stringID(WalkerTile tile){
        return tile.getX() + " " + tile.getY() + " " + tile.getPlane();
    }

    public boolean isDestination() {
        return destination;
    }

    public void setDestination(boolean destination) {
        this.destination = destination;
    }

    public boolean isTraversed() {
        return traversed;
    }

    public void setTraversed(boolean traversed) {
        this.traversed = traversed;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
    @Override
    public String toString(){
        return "AStarNode(" + x + ", " + y + ", " + z + ")[" + collisionData + "]";
    }
}
