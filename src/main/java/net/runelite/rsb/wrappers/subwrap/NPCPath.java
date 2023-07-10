package net.runelite.rsb.wrappers.subwrap;

import net.runelite.api.coords.WorldArea;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.methods.MethodProvider;
import net.runelite.rsb.wrappers.RSLocalPath;
import net.runelite.rsb.wrappers.RSNPC;
import net.runelite.rsb.wrappers.RSTile;
import net.runelite.rsb.wrappers.common.Positionable;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class NPCPath extends MethodProvider {
    public RSNPC[] npcs;
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

    public NPCPath(MethodContext ctx, RSNPC... npcs) {
        super(ctx);
        this.npcs = npcs;
    }

    public NPCPath setNPCS(RSNPC... npcs) {
        this.npcs = npcs;
        return this;
    }

    public RSTile getNearestSafespotWithLOS(Positionable end, int limit, int weaponRange) {
        return getNearestSafespot(end, limit, (x) -> Arrays.stream(npcs).anyMatch((npc) -> npc.hasLineOfSight(x) && x.distanceTo(npc) <= weaponRange));
    }

    public RSTile getNearestSafespot(Positionable end, int limit, Predicate<RSTile> predicate){
        int[][] flags = methods.client.getCollisionMaps()[methods.game.getPlane()].getFlags();
        RSTile center = end.getLocation();
        int i = center.getX(), j = center.getY();
        // Snake iterator; iterates from center outward in concentric squares
        RSTile current = new RSTile(i , j, center.getPlane());
        if (isSafeSpotted(current) && predicate.test(current)) return current;
        for (int layer = 1; layer <= limit; layer++, j--, i--) {
            while (i < center.getX() + layer)
                if (isSafeSpotted(current = new RSTile(++i , j, center.getPlane())) && predicate.test(current) &&
                    ((flags[current.getSceneX()][current.getSceneY()] & RSLocalPath.BLOCKED) == 0))
                        return current;
            while (j < center.getY() + layer)
                if (isSafeSpotted(current = new RSTile(i , ++j, center.getPlane())) && predicate.test(current) &&
                    ((flags[current.getSceneX()][current.getSceneY()] & RSLocalPath.BLOCKED) == 0))
                        return current;
            while (i > center.getX() - layer)
                if (isSafeSpotted(current = new RSTile(--i , j, center.getPlane())) && predicate.test(current) &&
                    ((flags[current.getSceneX()][current.getSceneY()] & RSLocalPath.BLOCKED) == 0))
                        return current;
            while (j > center.getY() - layer)
                if (isSafeSpotted(current = new RSTile(i , --j, center.getPlane())) && predicate.test(current) &&
                    ((flags[current.getSceneX()][current.getSceneY()] & RSLocalPath.BLOCKED) == 0))
                        return current;
        }
        return null;
    }
    public RSTile getNearestSafespot(Positionable end, int limit) {
        return getNearestSafespot(end, limit, (x) -> true);
    }

    public boolean isSafeSpotted(Positionable end) {
        for (RSNPC npc : npcs) {
            RSTile[] path = getPath(npc, npc.getLocation(), end.getLocation());
            RSTile nearestTile = npc.getNearestTile(path[path.length - 1], end);
            if (nearestTile.getLocation().distanceToDouble(end) < 1.1) {
                return false;
            }
        }
        return true;
    }

    static public RSTile[] getPath(RSNPC npc, RSTile start, RSTile end) {
        List<RSTile> returnList = new ArrayList<>();
        returnList.add(start);
        while (start.getX() != end.getX() || start.getY() != end.getY()) {
            start = getNextTile(npc, start , end);
            if (start.getWorldLocation().getX() == -1) {
                break;
            }
            returnList.add(start);
        }
        return returnList.toArray(new RSTile[0]);
    }

    static public RSTile[] getPath(RSNPC npc, Positionable end) {
        return getPath(npc, npc.getLocation(), end.getLocation());
    }


    static public RSTile getNextTile(RSNPC npc, RSTile start, RSTile end) {
        WorldArea area = npc.getAccessor().getWorldArea();
        int[][] flags = methods.client.getCollisionMaps()[methods.game.getPlane()].getFlags();

        // directionMask is all possible directions NPC wants to move
        int directionMask = toDirectionMap.get(Pair.of(
                Integer.compare(end.getX(), start.getX()),
                Integer.compare(end.getY(), start.getY())));

        // check collisions so that directionMask becomes all directions NPC can move
        for (int i = 0; i < npc.getWidth(); i++) {
            for (int j = 0; j < npc.getHeight(); j++) {
                int blockedDirectionMask = 0;
                // for each tile around point set blockedDirectionMask to that tiles direction if that direction is blocked
                for (Map.Entry<Integer, Pair<Integer, Integer>> entry : fromDirectionMap.entrySet()) {
                    RSTile blockedTile = start.offset(i + entry.getValue().getKey(), j + entry.getValue().getValue());
                    if ((flags[blockedTile.getSceneX()][blockedTile.getSceneY()] & RSLocalPath.BLOCKED) != 0) {
                        blockedDirectionMask = blockedDirectionMask | entry.getKey();
                    }
                }
                directionMask = directionMask &
                        ~(directionMask & flags[start.offset(i,j).getSceneX()]
                                [start.offset(i,j).getSceneY()]) &
                        ~blockedDirectionMask;
            }
        }
        // Filter directionMask to prefer diagonal then horizontal then vertical movements
        if ((directionMask & diagonalDirections) != 0 &&
                (directionMask & horizontalDirections) != 0 &&
                (directionMask & verticalDirections) != 0) {
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
        return start.offset(pair.getKey(), pair.getValue());
    }
}