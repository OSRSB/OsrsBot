package net.runelite.rsb.methods;

import net.runelite.api.*;
import net.runelite.cache.definitions.ObjectDefinition;
import net.runelite.rsb.query.RSObjectQueryBuilder;
import net.runelite.rsb.wrappers.RSObject;
import net.runelite.rsb.wrappers.RSTile;
import net.runelite.rsb.wrappers.RSTile;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Provides access to in-game physical objects.
 */
public class Objects extends MethodProvider {
    Objects(final MethodContext ctx) {
        super(ctx);
    }

    /**
     * A filter that accepts all matches.
     */
    public static final Predicate<RSObject> ALL_FILTER = new Predicate<>() {
        public boolean test(RSObject object) {
            return true;
        }
    };

    public RSObjectQueryBuilder query() {
        return new RSObjectQueryBuilder();
    }

    /**
     * Returns all the <code>RSObject</code>s in the local region.
     *
     * @return An <code>RSObject[]</code> of all objects in the loaded region.
     */
    public RSObject[] getAll() {
        return getAll(Objects.ALL_FILTER);
    }

    /**
     * Returns all the <code>RSObject</code>s in the local region accepted by the
     * provided Filter.
     *
     * @param filter Filters out unwanted objects.
     * @return An <code>RSObject[]</code> of all the accepted objects in the loaded
     * region.
     */
    public RSObject[] getAll(final Predicate<RSObject> filter) {
        Set<RSObject> objects = new LinkedHashSet<>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                for (RSObject o : getAtLocal(x, y, -1)) {
                    if (o.getObj() == null) {
                        continue;
                    }
                    if (filter.test(o)) {
                        objects.add(o);
                    }
                }
            }
        }
        return objects.toArray(new RSObject[objects.size()]);
    }

    /**
     * Returns all the named <code>RSObject</code>s in the local region.
     *
     * @param names Names of objects accepted.
     * @return An <code>RSObject[]</code> of all the accepted objects in the loaded
     * region.
     */
    public RSObject[] getAll(final String... names) {
        return getAll(o -> {
            for (String name : names) {
                if (o.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Returns the <code>RSObject</code> that is nearest out of all objects that are
     * accepted by the provided Filter.
     *
     * @param filter Filters out unwanted objects.
     * @return An <code>RSObject</code> representing the nearest object that was
     * accepted by the filter; or null if there are no matching objects
     * in the current region.
     */
    @Nullable
    public RSObject getNearest(final Predicate<RSObject> filter) {
        RSObject cur = null;
        double dist = Double.MAX_VALUE;
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Set<RSObject> objects = getAtLocal(x, y, -1);
                for (RSObject o : objects) {
                    if (o == null) {
                        continue;
                    }
                    if (filter.test(o)) {
                        double distTmp = methods.calc.distanceBetween(
                                methods.players.getMyPlayer().getLocation(),
                                o.getLocation());
                        if (distTmp < dist) {
                            cur = o;
                            dist = distTmp;
                        }
                    }
                }
            }
        }
        return cur;
    }

    /**
     * Returns the <code>RSObject</code> that is nearest out of all objects that are
     * accepted by the provided Filter.
     *
     * @param distance The distance away from the player to check
     * @param filter Filters out unwanted objects.
     * @return An <code>RSObject</code> representing the nearest object that was
     * accepted by the filter; or null if there are no matching objects
     * in the current region.
     */
    @Nullable
    public RSObject getNearest(final int distance, final Predicate<RSObject> filter) {
        RSObject cur = null;
        double dist = -1;
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                int distanceToCheck = methods.calc.distanceTo(new RSTile(x, y, methods.client.getPlane(), RSTile.TYPES.SCENE).toWorldTile());
                if (distanceToCheck < distance) {
                    Set<RSObject> objects = getAtLocal(x, y, -1);
                    for (RSObject o : objects) {
                        if (o == null) {
                            continue;
                        }
                        if (filter.test(o)) {
                            double distTmp = methods.calc.distanceBetween(
                                    methods.players.getMyPlayer().getLocation(),
                                    o.getLocation());
                            if (cur == null) {
                                dist = distTmp;
                                cur = o;
                            } else if (distTmp < dist) {
                                cur = o;
                                dist = distTmp;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return cur;
    }

    /**
     * Returns the <code>RSObject</code> that is nearest, out of all of the
     * RSObjects with the provided ID(s).
     *
     * @param ids The ID(s) of the RSObject that you are searching.
     * @return An <code>RSObject</code> representing the nearest object with one of
     * the provided IDs; or null if there are no matching objects in the
     * current region.
     */
    @Nullable
    public RSObject getNearest(final int... ids) {
        return getNearest(o -> {
            for (int id : ids) {
                if (o.getID() == id) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Returns the <code>RSObject</code> that is nearest, out of all of the
     * RSObjects with the provided name(s).
     *
     * @param names The name(s) of the RSObject that you are searching.
     * @return An <code>RSObject</code> representing the nearest object with one of
     * the provided names; or null if there are no matching objects in
     * the current region.
     */
    @Nullable
    public RSObject getNearest(final String... names) {
        return getNearest(o -> {
            ObjectDefinition def = o.getDef();
            if (def != null) {
                for (String name : names) {
                    if (name.equals(def.getName())) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    /**
     * Returns the <code>RSObject</code> that is nearest, out of all of the
     * RSObjects with the provided name(s).
     *
     * @param distance The distance from the player to search within
     * @param names The name(s) of the RSObject that you are searching.
     * @return An <code>RSObject</code> representing the nearest object with one of
     * the provided names; or null if there are no matching objects in
     * the current region.
     */
    @Nullable
    public RSObject findNearest(final int distance, final String... names) {
        return getNearest(distance, o -> {
            ObjectDefinition def = o.getDef();
            if (def != null) {
                for (String name : names) {
                    if (name.equals(def.getName())) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    /**
     * Returns the top <code>RSObject</code> on the specified tile.
     *
     * @param t The tile on which to search.
     * @return The top RSObject on the provided tile; or null if none found.
     */
    @Nullable
    public RSObject getTopAt(final RSTile t) {
        return getTopAt(t, -1);
    }

    /**
     * Returns the top <code>RSObject</code> on the specified tile matching types
     * specified by the flags in the provided mask.
     *
     * @param t    The tile on which to search.
     * @param mask The type flags.
     * @return The top RSObject on the provided tile matching the specified
     * flags; or null if none found.
     */
    @Nullable
    public RSObject getTopAt(final RSTile t, int mask) {
        RSObject[] objects = getAt(t, mask);
        return objects.length > 0 ? objects[0] : null;
    }

    /**
     * Returns the <code>RSObject</code>s which are on the specified <code>RSTile</code>
     * matching types specified by the flags in the provided mask.
     *
     * @param t    The tile on which to search.
     * @param mask  The type flags.
     * @return An RSObject[] of the objects on the specified tile.
     */
    public RSObject[] getAt(final RSTile t, int mask) {
        Set<RSObject> objects = getAtLocal(
                t.getWorldLocation().getX() - methods.client.getBaseX(),
                t.getWorldLocation().getY() - methods.client.getBaseY(), mask);
        return objects.toArray(new RSObject[0]);
    }

    /**
     * Returns the <code>RSObject</code>s which are on the specified <code>RSTile</code>
     * .
     *
     * @param t The tile on which to search.
     * @return An RSObject[] of the objects on the specified tile.
     */
    public RSObject[] getAllAt(final RSTile t) {
        Set<RSObject> objects = getAtLocal(
                t.getWorldLocation().getX() - methods.client.getBaseX(),
                t.getWorldLocation().getY() - methods.client.getBaseY(), -1);
        return objects.toArray(new RSObject[objects.size()]);
    }

    /**
     * Returns the <code>RSObject</code> that is located at the tile specified by the coordinates x and y in the
     * current plane (0 = ground, 1 = level 1, 2 = level 2, etc.). By using a mask you can then filter the type of
     * objects you wish to get from this location.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @param mask  The type enumeration.
     * @return  An RSObject[] of the objects on the specified tile.
     */
    private Set<RSObject> getAtLocal(int x, int y, int mask) {
        Set<RSObject> objects = new LinkedHashSet<>();
        if (methods.client.getTileSettings() == null) {
            return objects;
        }

        int plane = methods.client.getPlane();
        Tile tile = methods.client.getScene().getTiles()[plane][x][y];

        if (tile != null) {
            if (mask == -1 || (mask & 1) == 1) {
                for (GameObject gameObject : tile.getGameObjects()) {
                    if (gameObject != null) {
                        addObject(objects,  new RSObject(methods, gameObject, RSObject.Type.GAME, plane));
                    }
                }
            }
            if (mask == -1 || (mask >> 1 & 1) == 1) {
                TileObject tileObject =  tile.getDecorativeObject();
                if (tileObject != null) {
                    addObject(objects, new RSObject(methods, tile.getDecorativeObject(), RSObject.Type.DECORATIVE, plane));
                }
            }
            if (mask == -1 || (mask >> 2 & 1) == 1) {
                GroundObject groundObject = tile.getGroundObject();
                if (groundObject != null) {
                    addObject(objects, new RSObject(methods, groundObject, RSObject.Type.GROUND, plane));
                }
            }
            if (mask == -1 || (mask >> 3 & 1) == 1) {
                WallObject wallObject = tile.getWallObject();
                if (wallObject != null) {
                    addObject(objects, new RSObject(methods, wallObject, RSObject.Type.WALL, plane));
                }
            }
        }

        return objects;
    }

    private void addObject(Set<RSObject> objects, RSObject object) {
        if (object.getDef() != null) {
            objects.add(object);
        }
    }
}
