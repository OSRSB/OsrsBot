package net.runelite.client.rsb.methods;

import net.runelite.api.*;
import net.runelite.client.rsb.internal.wrappers.Filter;
import net.runelite.client.rsb.wrappers.RSObject;
import net.runelite.client.rsb.wrappers.RSTile;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Provides access to in-game physical objects.
 */
public class Objects extends MethodProvider {

    /**
     * A filter that accepts all matches.
     */
    public static final Filter<RSObject> ALL_FILTER = new Filter<RSObject>() {
        public boolean accept(RSObject npc) {
            return true;
        }
    };

    Objects(final MethodContext ctx) {
        super(ctx);
    }

    /**
     * Returns all the <tt>RSObject</tt>s in the local region.
     *
     * @return An <tt>RSObject[]</tt> of all objects in the loaded region.
     */
    public RSObject[] getAll() {
        return getAll(Objects.ALL_FILTER);
    }

    /**
     * Returns all the <tt>RSObject</tt>s in the local region accepted by the
     * provided Filter.
     *
     * @param filter Filters out unwanted objects.
     * @return An <tt>RSObject[]</tt> of all the accepted objects in the loaded
     * region.
     */
    public RSObject[] getAll(final Filter<RSObject> filter) {
        Set<RSObject> objects = new LinkedHashSet<RSObject>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                for (RSObject o : getAtLocal(x, y, -1)) {
                    if (filter.accept(o)) {
                        objects.add(o);
                    }
                }
            }
        }
        return objects.toArray(new RSObject[objects.size()]);
    }

    /**
     * Returns the <tt>RSObject</tt> that is nearest out of all objects that are
     * accepted by the provided Filter.
     *
     * @param filter Filters out unwanted objects.
     * @return An <tt>RSObject</tt> representing the nearest object that was
     * accepted by the filter; or null if there are no matching objects
     * in the current region.
     */
    public RSObject getNearest(final Filter<RSObject> filter) {
        RSObject cur = null;
        double dist = -1;
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Set<RSObject> objs = getAtLocal(x, y, -1);
                for (RSObject o : objs) {
                    if (filter.accept(o)) {
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
        return cur;
    }

    /**
     * Returns the <tt>RSObject</tt> that is nearest, out of all of the
     * RSObjects with the provided ID(s).
     *
     * @param ids The ID(s) of the RSObject that you are searching.
     * @return An <tt>RSObject</tt> representing the nearest object with one of
     * the provided IDs; or null if there are no matching objects in the
     * current region.
     */
    public RSObject getNearest(final int... ids) {
        return getNearest(new Filter<RSObject>() {
            public boolean accept(RSObject o) {
                for (int id : ids) {
                    if (o.getID() == id) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Returns the <tt>RSObject</tt> that is nearest, out of all of the
     * RSObjects with the provided name(s).
     *
     * @param names The name(s) of the RSObject that you are searching.
     * @return An <tt>RSObject</tt> representing the nearest object with one of
     * the provided names; or null if there are no matching objects in
     * the current region.
     */
    public RSObject getNearest(final String... names) {
        return getNearest(new Filter<RSObject>() {
            public boolean accept(RSObject o) {
                ObjectDefinition def = o.getDef();
                if (def != null) {
                    for (String name : names) {
                        if (name.equals(def.getName())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * Returns the top <tt>RSObject</tt> on the specified tile.
     *
     * @param t The tile on which to search.
     * @return The top RSObject on the provided tile; or null if none found.
     */
    public RSObject getTopAt(final RSTile t) {
        return getTopAt(t, -1);
    }

    /**
     * Returns the top <tt>RSObject</tt> on the specified tile matching types
     * specified by the flags in the provided mask.
     *
     * @param t    The tile on which to search.
     * @param mask The type flags.
     * @return The top RSObject on the provided tile matching the specified
     * flags; or null if none found.
     */
    public RSObject getTopAt(final RSTile t, final int mask) {
        RSObject[] objects = getAt(t, mask);
        return objects.length > 0 ? objects[0] : null;
    }

    /**
     * Returns the <tt>RSObject</tt>s which are on the specified <tt>RSTile</tt>
     * matching types specified by the flags in the provided mask.
     *
     * @param t    The tile on which to search.
     * @param mask The type flags.
     * @return An RSObject[] of the objects on the specified tile.
     */
    public RSObject[] getAt(final RSTile t, final int mask) {
        Set<RSObject> objects = getAtLocal(
                t.getWorldLocation().getX() - methods.client.getBaseX(),
                t.getWorldLocation().getY() - methods.client.getBaseY(), mask);
        return objects.toArray(new RSObject[objects.size()]);
    }

    /**
     * Returns the <tt>RSObject</tt>s which are on the specified <tt>RSTile</tt>
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

    private Set<RSObject> getAtLocal(int x, int y, final int mask) {
        Client client = methods.client;
        Set<RSObject> objects = new LinkedHashSet<RSObject>();
        if (client.getTileSettings() == null) {
            return objects;
        }

        try {
            int plane = client.getPlane();
            Tile tile = client.getScene().getTiles()[plane][x][y];

            if (tile != null) {
                RSObject rsObj;
                TileObject obj;

                x += methods.client.getBaseX();
                y += methods.client.getBaseY();

                for (GameObject gameObject : tile.getGameObjects()) {
                    objects.add(new RSObject(methods, gameObject, RSObject.Type.GAME, plane));
                }
                objects.add(new RSObject(methods, tile.getDecorativeObject(), RSObject.Type.DECORATIVE, plane));
                objects.add(new RSObject(methods, tile.getGroundObject(), RSObject.Type.GROUND, plane));
                objects.add(new RSObject(methods, tile.getWallObject(), RSObject.Type.WALL, plane));
            }
        } catch (Exception ignored) {
        }
        return objects;
    }

}
