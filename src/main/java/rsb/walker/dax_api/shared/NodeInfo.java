package rsb.walker.dax_api.shared;

import rsb.walker.dax_api.walker_engine.real_time_collision.RealTimeCollisionTile;

import java.util.HashMap;


public class NodeInfo {

    private static HashMap<Integer, HashMap<Integer, HashMap<Integer, Details>>> observableXMap = new HashMap<>(), pathTileXMap = new HashMap<>(), realTime = new HashMap<>();

    public static Details get(PathFindingNode pathFindingNode){

        HashMap<Integer, HashMap<Integer, HashMap<Integer, Details>>> xMap = null;
        if (pathFindingNode instanceof RealTimeCollisionTile){
            xMap = realTime;
        }
        if (xMap == null){
            throw new NullPointerException("xMap should never be null for NodeInfo.");
        }

        HashMap<Integer, HashMap<Integer, Details>> yMap = xMap.get(pathFindingNode.getX());
        if (yMap == null){
            return null;
        }
        HashMap<Integer, Details> zMap = yMap.get(pathFindingNode.getY());
        if (zMap == null){
            return null;
        }
        Details details = zMap.get(pathFindingNode.getZ());
        if (details == null){
            return null;
        }
        return details;
    }

    public static Details create(PathFindingNode pathFindingNode){
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Details>>> xMap = null;
        if (pathFindingNode instanceof RealTimeCollisionTile){
            xMap = realTime;
        }
        if (xMap == null){
            throw new NullPointerException("xMap should never be null for NodeInfo.");
        }
        HashMap<Integer, HashMap<Integer, Details>> yMap = xMap.computeIfAbsent(pathFindingNode.getX(), k -> new HashMap<>());
        HashMap<Integer, Details> zMap = yMap.computeIfAbsent(pathFindingNode.getY(), k -> new HashMap<>());
        return zMap.computeIfAbsent(pathFindingNode.getZ(), k -> new Details());
    }


    public static void clearMemory(Class c){
//        System.out.println("Clearing memory of " + c.getSimpleName());
        if (c == RealTimeCollisionTile.class){
            realTime.clear();
            realTime = new HashMap<>();
        }
    }

    public static class Details {
        public PathFindingNode parent;
        public int moveCost, heuristic, f;
        public boolean current, traversed, start, end, path;
    }

}
