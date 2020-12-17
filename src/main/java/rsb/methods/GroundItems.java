package rsb.methods;

import net.runelite.api.*;
import rsb.internal.wrappers.Filter;
import rsb.wrappers.RSGroundItem;
import rsb.wrappers.RSItem;
import rsb.wrappers.RSTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to ground items.
 */
public class GroundItems extends MethodProvider {
	
	//This will hold the maximum number of tiles away the client can render
	private static final int MAX_RENDER_RANGE = 25;

	public static final Filter<RSGroundItem> ALL_FILTER = new Filter<RSGroundItem>() {
		public boolean test(RSGroundItem item) {
			return true;
		}
	};

	GroundItems(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Returns all ground items in the loaded area.
	 *
	 * @return All ground items in the loaded area.
	 */
	public RSGroundItem[] getAll() {
		return getAll(MAX_RENDER_RANGE, ALL_FILTER);
	}

	/**
	 * Returns all matching ground items in the loaded area.
	 *
	 * @param filter Filters out unwanted matches.
	 * @return All ground items
	 */
	public RSGroundItem[] getAll(Filter<RSGroundItem> filter) {
		return getAll(MAX_RENDER_RANGE, filter);
	}

	/**
	 * Returns all ground items within the provided range.
	 *
	 * @param range The range (max distance in all directions) in which to check
	 *              items for.
	 * @return <tt>RSGroundItem</tt> array containing all of the items in range.
	 */
	public RSGroundItem[] getAll(int range) {
		return getAll(range, ALL_FILTER);
	}

	/**
	 * Returns all matching ground items within the provided range.
	 *
	 * @param range  The range (max distance in all directions) in which to check
	 *               items for.
	 * @param filter Filters out unwanted matches.
	 * @return <tt>RSGroundItem</tt> array containing all of the items in range.
	 */
	public RSGroundItem[] getAll(int range, Filter<RSGroundItem> filter) {
		ArrayList<RSGroundItem> temp = new ArrayList<RSGroundItem>();
		int pX = methods.players.getMyPlayer().getLocation().getWorldLocation().getX();
		int pY = methods.players.getMyPlayer().getLocation().getWorldLocation().getY();
		int minX = pX - range, minY = pY - range;
		int maxX = pX + range, maxY = pY + range;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				RSGroundItem[] items = getAllAt(x, y);
				if (items != null)
 				for (RSGroundItem item : items) {
 					if (item.getItem() == null) {
 						continue;
					}
					if (filter.test(item)) {
						temp.add(item);
					}
				}
			}
		}
		return temp.toArray(new RSGroundItem[temp.size()]);
	}

	/**
	 * Returns the nearest ground item that is accepted by the provided Filter.
	 *
	 * @param filter Filters out unwanted matches.
	 * @return The nearest item that is accepted by the provided Filter; or
	 *         null.
	 */
	public RSGroundItem getNearest(Filter<RSGroundItem> filter) {
		int dist = 9999999;
		int pX = methods.players.getMyPlayer().getLocation().getWorldLocation().getX();
		int pY = methods.players.getMyPlayer().getLocation().getWorldLocation().getY();
		int minX = pX - MAX_RENDER_RANGE, minY = pY - MAX_RENDER_RANGE;
		int maxX = pX + MAX_RENDER_RANGE, maxY = pY + MAX_RENDER_RANGE;
		RSGroundItem itm = null;
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				RSGroundItem[] items = getAllAt(x, y);
				if (items != null)
				for (RSGroundItem item : items) {
					if (item.getItem() == null) {
						continue;
					}
					if (filter.test(item)
							&& methods.calc.distanceTo(item.getLocation()) < dist) {
						dist = methods.calc.distanceTo(item.getLocation());
						itm = item;
					}
				}
			}
		}
		return itm;
	}

	/**
	 * Returns the nearest item on the ground with an ID that matches any of the
	 * IDs provided.
	 *
	 * @param ids The IDs to look for.
	 * @return RSItemTile of the nearest item with the an ID that matches any in
	 *         the array of IDs provided; or null if no matching ground items
	 *         were found.
	 */
	public RSGroundItem getNearest(final int... ids) {
		return getNearest(new Filter<RSGroundItem>() {
			public boolean test(RSGroundItem item) {
				int iid = item.getItem().getID();
				for (int id : ids) {
					if (id == iid) {
						return true;
					}
				}
				return false;
			}
		});
	}

	/**
	 * Returns all the ground items at a tile on the current plane.
	 *
	 * @param x The x position of the tile in the world.
	 * @param y The y position of the tile in the world.
	 * @return An array of the ground items on the specified tile.
	 */
	public RSGroundItem[] getAllAt(int x, int y) {
		if (!methods.game.isLoggedIn()) {
			return new RSGroundItem[0];
		}
		List<RSGroundItem> list = new ArrayList<>();

		RSTile rsTile = new RSTile(x, y, methods.client.getPlane());
		Tile tile = rsTile.getTile(methods);
		if (tile == null) {
			return null;
		}

		List<TileItem> groundItems = tile.getGroundItems();

		if (groundItems != null && !groundItems.isEmpty()) {
			for (TileItem item : groundItems) {
				list.add(new RSGroundItem(methods, rsTile, new RSItem(methods, item)));
			}
		}

		return list.toArray(new RSGroundItem[list.size()]);
	}

	/**
	 * Returns all the ground items at a tile on the current plane.
	 *
	 * @param t The tile.
	 * @return An array of the ground items on the specified tile.
	 */
	public RSGroundItem[] getAllAt(RSTile t) {
		return getAllAt(t.getWorldLocation().getX(), t.getWorldLocation().getY());
	}

}
