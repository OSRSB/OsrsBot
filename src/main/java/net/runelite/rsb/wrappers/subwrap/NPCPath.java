package net.runelite.rsb.wrappers.subwrap;

import net.runelite.api.coords.WorldArea;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.methods.MethodProvider;
import net.runelite.rsb.wrappers.RSLocalPath;
import net.runelite.rsb.wrappers.RSNPC;
import net.runelite.rsb.wrappers.RSTile;
import net.runelite.rsb.wrappers.common.Positionable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NPCPath extends MethodProvider {
    public RSNPC npc;
    static final Map<Pair<Integer, Integer>, Integer> toDirectionMap = Map.of(
            Pair.of(0, 1), RSLocalPath.WALL_NORTH,
            Pair.of(0, -1), RSLocalPath.WALL_SOUTH,
            Pair.of(1, 0), RSLocalPath.WALL_EAST,
            Pair.of(-1, 0), RSLocalPath.WALL_WEST,
            Pair.of(1, 1), RSLocalPath.WALL_NORTH_EAST | RSLocalPath.WALL_NORTH | RSLocalPath.WALL_EAST,
            Pair.of(-1, 1), RSLocalPath.WALL_NORTH_WEST | RSLocalPath.WALL_NORTH | RSLocalPath.WALL_WEST,
            Pair.of(1, -1), RSLocalPath.WALL_SOUTH_EAST | RSLocalPath.WALL_SOUTH | RSLocalPath.WALL_EAST,
            Pair.of(-1, -1), RSLocalPath.WALL_SOUTH_WEST | RSLocalPath.WALL_SOUTH | RSLocalPath.WALL_WEST);
    static final Map<Integer, Pair<Integer, Integer>> fromDirectionMap = Map.of(
            RSLocalPath.WALL_NORTH, Pair.of(0, 1),
            RSLocalPath.WALL_SOUTH, Pair.of(0, -1),
            RSLocalPath.WALL_EAST, Pair.of(1, 0),
            RSLocalPath.WALL_WEST, Pair.of(-1, 0),
            RSLocalPath.WALL_NORTH_EAST, Pair.of(1, 1),
            RSLocalPath.WALL_NORTH_WEST, Pair.of(-1, 1),
            RSLocalPath.WALL_SOUTH_EAST, Pair.of(1, -1),
            RSLocalPath.WALL_SOUTH_WEST, Pair.of(-1, -1));
    static final int diagonalDirections = RSLocalPath.WALL_NORTH_EAST | RSLocalPath.WALL_NORTH_WEST | RSLocalPath.WALL_SOUTH_EAST | RSLocalPath.WALL_SOUTH_WEST;
    static final int horizontalDirections = RSLocalPath.WALL_EAST | RSLocalPath.WALL_WEST;
    static final int verticalDirections = RSLocalPath.WALL_NORTH | RSLocalPath.WALL_SOUTH;

    public NPCPath(MethodContext ctx, RSNPC npc) {
        super(ctx);
        this.npc = npc;
    }

    public boolean hasLineOfSight(RSTile start, RSTile end) {
        RSTile closestTile = null;
        double minDistance = Float.POSITIVE_INFINITY;
        for (int i = 0; i < npc.getAccessor().getWorldArea().getWidth(); i++) {
            for (int j = 0; j < npc.getAccessor().getWorldArea().getHeight(); j++) {
                RSTile tile = offsetTile(start, Pair.of(i,j));
                double distance = offsetTile(start, Pair.of(i,j)).getLocation().distanceToDouble(end);
                if (minDistance > distance) {
                    minDistance = distance;
                    closestTile = tile;
                }
            }
        }
        return end.getTile(methods).hasLineOfSightTo(closestTile.getTile(methods));
    }
    public boolean hasLineOfSight(Positionable end) {
        return hasLineOfSight(npc.getLocation(), end.getLocation());
    }
    public boolean hasLineOfSight(RSTile end) {
        return hasLineOfSight(npc.getLocation(), end);
    }

    public boolean isSafeSpotted(RSTile start, RSTile end) {
        RSTile[] path = getPath(start, end);
        RSTile lastTile = path[path.length - 1];
        for (int i = 0; i < npc.getAccessor().getWorldArea().getWidth(); i++) {
            for (int j = 0; j < npc.getAccessor().getWorldArea().getHeight(); j++) {
                int manhattanDistance = Math.abs(offsetTile(lastTile, Pair.of(i, j)).getWorldLocation().getX() - end.getWorldLocation().getX()) +
                        Math.abs(offsetTile(lastTile, Pair.of(i, j)).getWorldLocation().getY() - end.getWorldLocation().getY());
                if (manhattanDistance <= 1) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isSafeSpotted(Positionable end) {
        return isSafeSpotted(npc.getLocation(), end.getLocation());
    }

    public boolean isSafeSpotted(RSTile end) {
        return isSafeSpotted(npc.getLocation(), end);
    }

    public RSTile[] getPath(RSTile start, RSTile end) {
        List<RSTile> returnList = new ArrayList<>();
        returnList.add(start);
        while (start.getWorldLocation().getX() != end.getWorldLocation().getX() || start.getWorldLocation().getY() != end.getWorldLocation().getY()) {
            start = getNextTile(start , end);
            if (start.getWorldLocation().getX() == -1) {
                break;
            }
            returnList.add(start);
        }
        return returnList.toArray(new RSTile[0]);
    }

    public RSTile[] getPath(Positionable end) {
        return getPath(npc.getLocation(), end.getLocation());
    }

    public RSTile[] getPath(RSTile end) {
        return getPath(npc.getLocation(), end);
    }

    public RSTile offsetTile(RSTile tile, Pair<Integer, Integer> offset) {
        return new RSTile(tile.getWorldLocation().getX() + offset.getKey(),
                tile.getWorldLocation().getY() + offset.getValue(),
                tile.getWorldLocation().getPlane());
    }

    public RSTile offsetTile(RSTile tile, RSTile tile2) {
        return new RSTile(tile.getWorldLocation().getX() + tile2.getWorldLocation().getX(),
                tile.getWorldLocation().getY() + tile2.getWorldLocation().getY(),
                tile.getWorldLocation().getPlane() + tile2.getWorldLocation().getPlane());
    }

    public RSTile getNextTile(RSTile start, RSTile end) {
        WorldArea area = npc.getAccessor().getWorldArea();
        int[][] flags = methods.client.getCollisionMaps()[methods.game.getPlane()].getFlags();

        // directionMask is all possible directions NPC wants to move
        int directionMask = toDirectionMap.get(Pair.of(
                Integer.compare(end.getWorldLocation().getX(), start.getWorldLocation().getX()),
                Integer.compare(end.getWorldLocation().getY(), start.getWorldLocation().getY())));

        // check collisions so that directionMask becomes all directions NPC can move
        for (int i = 0; i < npc.getAccessor().getWorldArea().getWidth(); i++) {
            for (int j = 0; j < npc.getAccessor().getWorldArea().getHeight(); j++) {
                int blockedDirectionMask = 0;
                // for each tile around point set blockedDirectionMask to that tiles direction if that direction is blocked
                for (Map.Entry<Integer, Pair<Integer, Integer>> entry : fromDirectionMap.entrySet()) {
                    RSTile blockedTile = offsetTile(offsetTile(start, Pair.of(i, j)), entry.getValue());
                    if ((flags[blockedTile.getLocalLocation(methods).getSceneX()][blockedTile.getLocalLocation(methods).getSceneY()] & RSLocalPath.BLOCKED) != 0) {
                        blockedDirectionMask = blockedDirectionMask | entry.getKey();
                    }
                }
                directionMask = directionMask &
                        ~(directionMask & flags[offsetTile(start, Pair.of(i, j)).getLocalLocation(methods).getSceneX()]
                                [offsetTile(start, Pair.of(i, j)).getLocalLocation(methods).getSceneY()]) &
                        ~blockedDirectionMask;
            }
        }
        // Filter directionMask to prefer diagonal then horizontal then vertical movements
        if ((directionMask & diagonalDirections) != 0 && (directionMask & horizontalDirections) != 0 && (directionMask & verticalDirections) != 0) {
            directionMask = directionMask & diagonalDirections;
        }
        else if ((directionMask & horizontalDirections) != 0){
            directionMask = directionMask & horizontalDirections;
        }
        else if ((directionMask & verticalDirections) != 0){
            directionMask = directionMask & verticalDirections;
        }
        else {
            return new RSTile(-1, -1, -1);
        }

        // return a new tile from directionMask
        Pair<Integer, Integer> pair = fromDirectionMap.get(directionMask);
        return offsetTile(start, pair);
    }
}