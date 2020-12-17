package rsb.walker.dax_api.walker.utils.path;

import rsb.methods.Calculations;
import rsb.methods.Web;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.walker_engine.local_pathfinding.AStarNode;
import rsb.walker.dax_api.walker_engine.local_pathfinding.Reachable;
import rsb.wrappers.RSCharacter;
import rsb.wrappers.common.Positionable;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

/**
 * For local pathing ONLY. Anything outside your region will return unexpected results.
 */
public class DaxPathFinder {

    public static class Destination {
        private WalkerTile tile;
        private Destination parent;
        private int distance;

        public Destination(WalkerTile tile, Destination parent, int distance) {
            this.tile = tile;
            this.parent = parent;
            this.distance = distance;
        }

        public WalkerTile getLocalTile() {
            return tile;
        }

        public WalkerTile getWorldTile() {
            return tile.toWorldTile();
        }

        public Destination getParent() {
            return parent;
        }

        public int getDistance() {
            return distance;
        }

        public List<WalkerTile> getPath() {
            return DaxPathFinder.getPath(this);
        }
    }

    /**
     * Method for grabbing the path your character is currently walking.
     *
     * @return The path your character is following.
     */
    public static List<WalkerTile> getWalkingQueue() {
        return getWalkingQueue(getMap());
    }

    /**
     * Method for grabbing the path your character is currently walking.
     *
     * @param map
     * @return The path your character is following.
     */
    public static List<WalkerTile> getWalkingQueue(Destination[][] map) {
        WalkerTile destination = new WalkerTile(Web.methods.walking.getDestination());
        if (destination == null) {
            destination = getNextWalkingTile();
        }
        return destination != null ? getPath(map, destination) : null;
    }

    /**
     *
     * Method to check if your character is walking to a destination.
     *
     * @param tile
     * @return true if your character is walking or will walk to that tile in the next game tick.
     */
    public static boolean isWalkingTowards(WalkerTile tile){
        WalkerTile tile1 = getNextWalkingTile();
        return tile1 != null && tile1.equals(tile);
    }

    /**
     *
     * Next tile that your character is moving to in the current/next game tick.
     *
     * @return The next tile that your character is walking to
     */
    public static WalkerTile getNextWalkingTile(){
        ArrayList<WalkerTile> tiles = getWalkingHistory();
        return tiles.size() > 0 && !tiles.get(0).equals(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())) ? tiles.get(0) : null;
    }

    /**
     *
     * @param tile
     * @return Distance to a tile accounting for collision. Integer.MAX_VALUE if not reachable.
     */
    public static int distance(Positionable tile) {
        return distance(getMap(), tile.getLocation());
    }

    public static int distance(Destination[][] map, Positionable tile) {
        WalkerTile worldTile = tile.getLocation().toSceneTile();
        int x = worldTile.getX(), y = worldTile.getY();

        if (!validLocalBounds(tile)) {
            return Integer.MAX_VALUE;
        }

        Destination destination = map[x][y];
        return destination == null ? Integer.MAX_VALUE : destination.distance;
    }

    public static boolean canReach(WalkerTile tile) {
        return canReach(getMap(), tile);
    }

    public static boolean canReach(Destination[][] map, WalkerTile tile) {
        if (tile.getPlane() != new WalkerTile(Web.methods.players.getMyPlayer().getLocation()).getWorldLocation().getPlane()) return false;
        WalkerTile worldTile = tile.getType() != WalkerTile.TYPES.SCENE ? tile.toSceneTile() : tile;
        int x = worldTile.getX(), y = worldTile.getY();
        if (!validLocalBounds(tile) || x > map.length || y > map[x].length) {
            return false;
        }
        Destination destination = map[x][y];
        return destination != null;
    }

    public static List<WalkerTile> getPath(WalkerTile tile) {
        return getPath(getMap(), tile);
    }

    public static List<WalkerTile> getPath(Destination destination) {
        Stack<WalkerTile> WalkerTiles = new Stack<>();
        Destination parent = destination;
        while (parent != null) {
            WalkerTiles.add(parent.getWorldTile());
            parent = parent.parent;
        }
        return new ArrayList<>(WalkerTiles);
    }

    public static List<WalkerTile> getPath(Destination[][] map, WalkerTile tile) {
        WalkerTile worldTile = tile.getType() != WalkerTile.TYPES.SCENE ? tile.toSceneTile() : tile;
        int x = worldTile.getX(), y = worldTile.getY();

        Destination destination = map[x][y];

        if (destination == null) {
            return null;
        }

        return destination.getPath();
    }

    public static Destination[][] getMap() {
        final WalkerTile home = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).toSceneTile();
        Destination[][] map = new Destination[104][104];
        int[][] collisionData = Web.methods.walking.getCollisionFlags(Web.methods.client.getPlane());
        if(collisionData == null || collisionData.length < home.getX() || collisionData[home.getX()].length < home.getY()){
            return map;
        }

        Queue<Destination> queue = new LinkedList<>();
        queue.add(new Destination(home, null, 0));
        map[home.getX()][home.getY()] = queue.peek();

        while (!queue.isEmpty()) {
            Destination currentLocal = queue.poll();

            int x = currentLocal.getLocalTile().getX(), y = currentLocal.getLocalTile().getY();
            Destination destination = map[x][y];

            for (Reachable.Direction direction : Reachable.Direction.values()) {
                if (!direction.isValidDirection(x, y, collisionData)) {
                    continue; //Cannot traverse to tile from current.
                }

                WalkerTile neighbor = direction.getPointingTile(currentLocal.getLocalTile());
                int destinationX = neighbor.getX(), destinationY = neighbor.getY();

                if (!AStarNode.isWalkable(collisionData[destinationX][destinationY])) {
                    continue;
                }

                if (map[destinationX][destinationY] != null) {
                    continue; //Traversed already
                }

                map[destinationX][destinationY] = new Destination(neighbor, currentLocal, (destination != null) ? destination.getDistance() + 1 : 0);
                queue.add(map[destinationX][destinationY]);
            }

        }
        return map;
    }

    public static void drawQueue(Destination[][] map, Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        List<WalkerTile> path = getWalkingQueue(map);
        if (path == null) {
            return;
        }

        WalkerTile previousTile = path.get(0);
        for (int i = 1; i < path.size(); i++) {
            Point point1 = Calculations.convertRLPointToAWTPoint(Web.methods.calc.tileToScreen(path.get(i), 0));
            Point point2 = Calculations.convertRLPointToAWTPoint(Web.methods.calc.tileToScreen(previousTile, 0));
            if (point1 == null || point1.x == -1 || point2 == null || point2.x == -1) {
                continue;
            }
            g.setColor(new Color(255, 0, 11, 116));
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(point1.x, point1.y, point2.x, point2.y);
            previousTile = path.get(i);
        }

    }

    public static void drawPaths(Destination[][] map, Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        for (Destination[] destinations : map) {
            for (Destination destination : destinations) {

                if (destination == null || destination.getParent() == null) {
                    continue;
                }

                WalkerTile tile = destination.getWorldTile();
                WalkerTile parent = destination.getParent().getWorldTile();

                if (!tile.isOnScreen() && !parent.isOnScreen()) {
                    continue;
                }

                Point point1 = Calculations.convertRLPointToAWTPoint(Web.methods.calc.tileToScreen(tile, 0));
                Point point2 = Calculations.convertRLPointToAWTPoint(Web.methods.calc.tileToScreen(parent, 0));

                if (point1 == null || point1.x == -1 || point2 == null || point2.x == -1) {
                    continue;
                }

                g.setColor(new Color(255, 255, 255, 60));
                g.setStroke(new BasicStroke(1));
                g.drawLine(point1.x, point1.y, point2.x, point2.y);
            }
        }
    }

    private static boolean validLocalBounds(Positionable positionable) {
        WalkerTile tile = positionable.getLocation().getType() == WalkerTile.TYPES.SCENE ? positionable.getLocation() : positionable.getLocation().toSceneTile();
        return tile.getX() >= 0 && tile.getX() < 104 && tile.getY() >= 0 && tile.getY() < 104;
    }

    private static ArrayList<WalkerTile> getWalkingHistory(){
        return getWalkingHistory(Web.methods.players.getMyPlayer());
    }

    private static ArrayList<WalkerTile> getWalkingHistory(RSCharacter rsCharacter){
        ArrayList<WalkerTile> walkingQueue = new ArrayList<>();
        if (rsCharacter == null){
            return walkingQueue;
        }
        int plane = rsCharacter.getLocation().getWorldLocation().getPlane();
        int[] xIndex = rsCharacter.getPathX(), yIndex = rsCharacter.getPathY();
        for (int i = 0; i < xIndex.length && i < yIndex.length; i++) {
            walkingQueue.add(new WalkerTile(xIndex[i], yIndex[i], plane, WalkerTile.TYPES.SCENE).toWorldTile());
        }
        return walkingQueue;
    }

}
