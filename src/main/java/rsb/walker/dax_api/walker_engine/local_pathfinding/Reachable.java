package rsb.walker.dax_api.walker_engine.local_pathfinding;


import rsb.methods.Web;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.shared.helpers.BankHelper;
import rsb.wrappers.common.Positionable;


import java.util.*;

public class Reachable {

    private WalkerTile[][] map;

    /**
     * Generates reachable map from player position
     */
    public Reachable() {
        this(null);
    }

    private Reachable(WalkerTile homeTile) {
        map = generateMap(homeTile != null ? homeTile : new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
    }

    public boolean canReach(WalkerTile position) {
        position = position.toWorldTile();
        WalkerTile playerPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        if (playerPosition.getX() == position.getX() && playerPosition.getY() == position.getY()) {
            return true;
        }
        return getParent(position.toSceneTile()) != null;
    }

    public boolean canReach(int x, int y) {
        WalkerTile playerPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        if (playerPosition.getX() == x && playerPosition.getY() == y) {
            return true;
        }
        WalkerTile position = convertToLocal(x, y);
        return getParent(position) != null;
    }

    public WalkerTile closestTile(Collection<WalkerTile> tiles) {
        WalkerTile closest = null;
        double closestDistance = Integer.MAX_VALUE;
        WalkerTile playerPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        for (WalkerTile positionable : tiles) {
            double distance = playerPosition.distanceToDouble(positionable);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = positionable;
            }
        }
        return closest;
    }

    /**
     * @param x
     * @param y
     * @return parent tile of x and y through BFS.
     */
    public WalkerTile getParent(int x, int y) {
        WalkerTile position = convertToLocal(x, y);
        return getParent(position);
    }

    public WalkerTile getParent(Positionable positionable) {
        WalkerTile tile = positionable.getLocation();
        if (tile.getType() != WalkerTile.TYPES.SCENE) {
            tile = tile.toSceneTile();
        }
        int x = tile.getX(), y = tile.getY();
        if (x < 0 || y < 0) {
            return null;
        }
        if (x >= 104 || y >= 104 || x >= map.length || y >= map[x].length){
            return null;
        }
        return map[x][y];
    }

    /**
     * @param x
     * @param y
     * @return Distance to tile. Max integer value if unreachable. Does not account for positionable behind doors
     */
    public int getDistance(int x, int y) {
        WalkerTile position = convertToLocal(x, y);
        return getDistance(position);
    }

    /**
     * @param positionable
     * @return path to tile. Does not account for positionable behind doors
     */
    public ArrayList<WalkerTile> getPath(Positionable positionable) {
        WalkerTile position = convertToLocal(positionable.getLocation().getX(), positionable.getLocation().getY());
        int x = position.getX(), y = position.getY();
        return getPath(x, y);
    }

    /**
     * @param x
     * @param y
     * @return null if no path.
     */
    public ArrayList<WalkerTile> getPath(int x, int y) {
        ArrayList<WalkerTile> path = new ArrayList<>();
        WalkerTile playerPos = new WalkerTile(Web.methods.players.getMyPlayer().getLocation()).toSceneTile();
        if (x == playerPos.getX() && y == playerPos.getY()) {
            return path;
        }
        if (x < 0 || y < 0) {
            return null;
        }
        if (x >= 104 || y >= 104) {
            return null;
        }
        if (map[x][y] == null) {
            return null;
        }
        WalkerTile tile = new WalkerTile(x, y, new WalkerTile(Web.methods.players.getMyPlayer().getLocation()).getWorldLocation().getPlane(), WalkerTile.TYPES.SCENE);
        while ((tile = map[tile.getX()][tile.getY()]) != null) {
            path.add(tile.toWorldTile());
        }
        Collections.reverse(path);
        return path;
    }

    public int getDistance(Positionable positionable) {
        WalkerTile position = convertToLocal(positionable.getLocation().getX(), positionable.getLocation().getY());
        int x = position.getX(), y = position.getY();
        WalkerTile playerPos = new WalkerTile(Web.methods.players.getMyPlayer().getLocation()).toSceneTile();
        if (x == playerPos.getX() && y == playerPos.getY()) {
            return 0;
        }
        if (x < 0 || y < 0) {
            return Integer.MAX_VALUE;
        }
        if (x >= 104 || y >= 104) {
            return Integer.MAX_VALUE;
        }
        if (map[x][y] == null) {
            return Integer.MAX_VALUE;
        }
        int length = 0;
        WalkerTile tile = position;
        while ((tile = map[tile.getX()][tile.getY()]) != null) {
            length++;
        }
        return length;
    }

    private static WalkerTile convertToLocal(int x, int y) {
        WalkerTile position = new WalkerTile(x, y, new WalkerTile(Web.methods.players.getMyPlayer().getLocation()).getWorldLocation().getPlane(), x >= 104 || y >= 104 ? WalkerTile.TYPES.WORLD : WalkerTile.TYPES.SCENE);
        if (position.getType() != WalkerTile.TYPES.SCENE) {
            position = position.toSceneTile();
        }
        return position;
    }

    public static WalkerTile getBestWalkableTile(Positionable positionable, Reachable reachable) {
        WalkerTile localPosition = positionable.getLocation().toSceneTile();
        HashSet<WalkerTile> building = BankHelper.getBuilding(positionable);
        boolean[][] traversed = new boolean[104][104];
        WalkerTile[][] parentMap = new WalkerTile[104][104];
        Queue<WalkerTile> queue = new LinkedList<>();
        int[][] collisionData = Web.methods.walking.getCollisionFlags(Web.methods.client.getPlane());
        if(collisionData == null)
            return null;

        queue.add(localPosition);
        try {
            traversed[localPosition.getX()][localPosition.getY()] = true;
            parentMap[localPosition.getX()][localPosition.getY()] = null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

        while (!queue.isEmpty()) {
            WalkerTile currentLocal = queue.poll();
            int x = currentLocal.getX(), y = currentLocal.getY();

            int currentCollisionFlags = collisionData[x][y];
            if (AStarNode.isWalkable(currentCollisionFlags)) {
                if (reachable != null && !reachable.canReach(currentLocal.toWorldTile().getX(), currentLocal.toWorldTile().getY())) {
                    continue;
                }
                if (building != null && building.size() > 0) {
                    if (building.contains(currentLocal.toWorldTile())) {
                        return currentLocal.toWorldTile();
                    }
                    continue; //Next tile because we are now outside of building.
                } else {
                    return currentLocal.toWorldTile();
                }
            }

            for (Direction direction : Direction.values()) {
                if (!direction.isValidDirection(x, y, collisionData)) {
                    continue; //Cannot traverse to tile from current.
                }

                WalkerTile neighbor = direction.getPointingTile(currentLocal);
                int destinationX = neighbor.getX(), destinationY = neighbor.getY();
                if (traversed[destinationX][destinationY]) {
                    continue; //Traversed already
                }
                traversed[destinationX][destinationY] = true;
                parentMap[destinationX][destinationY] = currentLocal;
                queue.add(neighbor);
            }

        }
        return null;
    }

    /**
     * @return gets collision map.
     */
    public static Reachable getMap() {
        return new Reachable(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
    }

    public static Reachable getMap(WalkerTile homeTile) {
        return new Reachable(homeTile);
    }

    /**
     * @return local reachable tiles
     */
    private static WalkerTile[][] generateMap(WalkerTile homeTile) {
        WalkerTile localPlayerPosition = homeTile.toSceneTile();
        boolean[][] traversed = new boolean[104][104];
        WalkerTile[][] parentMap = new WalkerTile[104][104];
        Queue<WalkerTile> queue = new LinkedList<>();
        int[][] collisionData = Web.methods.walking.getCollisionFlags(Web.methods.client.getPlane());

        if(collisionData == null)
            return new WalkerTile[][]{};

        queue.add(localPlayerPosition);
        try {
            traversed[localPlayerPosition.getX()][localPlayerPosition.getY()] = true;
            parentMap[localPlayerPosition.getX()][localPlayerPosition.getY()] = null;
        } catch (Exception e) {
            return parentMap;
        }

        while (!queue.isEmpty()) {
            WalkerTile currentLocal = queue.poll();
            int x = currentLocal.getX(), y = currentLocal.getY();

            int currentCollisionFlags = collisionData[x][y];
            if (!AStarNode.isWalkable(currentCollisionFlags)) {
                continue;
            }

            for (Direction direction : Direction.values()) {
                if (!direction.isValidDirection(x, y, collisionData)) {
                    continue; //Cannot traverse to tile from current.
                }

                WalkerTile neighbor = direction.getPointingTile(currentLocal);
                int destinationX = neighbor.getX(), destinationY = neighbor.getY();
                if (traversed[destinationX][destinationY]) {
                    continue; //Traversed already
                }
                traversed[destinationX][destinationY] = true;
                parentMap[destinationX][destinationY] = currentLocal;
                queue.add(neighbor);
            }

        }
        return parentMap;
    }

    public enum Direction {
        EAST(1, 0),
        NORTH(0, 1),
        WEST(-1, 0),
        SOUTH(0, -1),
        NORTH_EAST(1, 1),
        NORTH_WEST(-1, 1),
        SOUTH_EAST(1, -1),
        SOUTH_WEST(-1, -1),
        ;

        int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public WalkerTile getPointingTile(WalkerTile tile) {
            return tile.translate(x, y);
        }

        public boolean isValidDirection(int x, int y, int[][] collisionData) {
            try {
                switch (this) {
                    case NORTH:
                        return !AStarNode.blockedNorth(collisionData[x][y]);
                    case EAST:
                        return !AStarNode.blockedEast(collisionData[x][y]);
                    case SOUTH:
                        return !AStarNode.blockedSouth(collisionData[x][y]);
                    case WEST:
                        return !AStarNode.blockedWest(collisionData[x][y]);
                    case NORTH_EAST:
                        if (AStarNode.blockedNorth(collisionData[x][y]) || AStarNode.blockedEast(collisionData[x][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x + 1][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x][y + 1])) {
                            return false;
                        }
                        if (AStarNode.blockedNorth(collisionData[x + 1][y])) {
                            return false;
                        }
                        if (AStarNode.blockedEast(collisionData[x][y + 1])) {
                            return false;
                        }
                        return true;
                    case NORTH_WEST:
                        if (AStarNode.blockedNorth(collisionData[x][y]) || AStarNode.blockedWest(collisionData[x][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x - 1][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x][y + 1])) {
                            return false;
                        }
                        if (AStarNode.blockedNorth(collisionData[x - 1][y])) {
                            return false;
                        }
                        if (AStarNode.blockedWest(collisionData[x][y + 1])) {
                            return false;
                        }
                        return true;
                    case SOUTH_EAST:
                        if (AStarNode.blockedSouth(collisionData[x][y]) || AStarNode.blockedEast(collisionData[x][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x + 1][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x][y - 1])) {
                            return false;
                        }
                        if (AStarNode.blockedSouth(collisionData[x + 1][y])) {
                            return false;
                        }
                        if (AStarNode.blockedEast(collisionData[x][y - 1])) {
                            return false;
                        }
                        return true;
                    case SOUTH_WEST:
                        if (AStarNode.blockedSouth(collisionData[x][y]) || AStarNode.blockedWest(collisionData[x][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x - 1][y])) {
                            return false;
                        }
                        if (!AStarNode.isWalkable(collisionData[x][y - 1])) {
                            return false;
                        }
                        if (AStarNode.blockedSouth(collisionData[x - 1][y])) {
                            return false;
                        }
                        if (AStarNode.blockedWest(collisionData[x][y - 1])) {
                            return false;
                        }
                        return true;
                    default:
                        return false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }
    }

}
