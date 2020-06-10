package net.runelite.client.rsb.methods;

import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.rsb.internal.wrappers.Filter;
import net.runelite.client.rsb.wrappers.RSItem;
import net.runelite.client.rsb.wrappers.RSWidget;

/**
 * Equipment related operations.
 */
public class Equipment extends MethodProvider {

	public static final int ITEM_SLOTS = 10;
	public static final int INTERFACE_EQUIPMENT = WidgetInfo.EQUIPMENT.getGroupId();
	public static final int HELMET = 6;
	public static final int CAPE = 7;
	public static final int NECK = 8;
	public static final int WEAPON = 9;
	public static final int BODY = 10;
	public static final int SHIELD = 11;
	public static final int LEGS = 12;
	public static final int HANDS = 13;
	public static final int FEET = 14;
	public static final int RING = 15;
	public static final int AMMO = 16;

	Equipment(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Gets the equipment interface.
	 *
	 * @return the equipment interface
	 */
	public RSWidget getInterface() {
		// Tab needs to be open for it to update its content -.-
		if (methods.game.getCurrentTab() != Game.TAB_EQUIPMENT) {
			if (methods.bank.isOpen()) {
				methods.bank.close();
			}
			methods.game.openTab(Game.TAB_EQUIPMENT);
			sleep(random(900, 1500));
		}
		return methods.interfaces.get(INTERFACE_EQUIPMENT);
	}

	/**
	 * Gets the equipment array.
	 *
	 * @return An array containing all equipped items
	 */
	public RSItem[] getItems() {
		RSWidget[] equip = getInterface().getComponents();
		RSItem[] items = new RSItem[ITEM_SLOTS];
		for (int i = HELMET; i < items.length + HELMET; i++) {
			items[i] = new RSItem(methods, equip[i].getDynamicComponent(1));
		}
		return items;
	}

	/**
	 * Gets the cached equipment array (i.e. does not open the interface).
	 *
	 * @return The items equipped as seen when the equipment tab was last
	 *         opened.
	 */
	public RSItem[] getCachedItems() {
		RSWidget equipment = methods.interfaces.get(INTERFACE_EQUIPMENT);
		RSWidget[] components = equipment.getComponents();
		RSItem[] items = new RSItem[ITEM_SLOTS];
		for (int i = HELMET; i < items.length + HELMET; i++) {
			items[i] = new RSItem(methods, components[i].getDynamicComponent(1));
		}
		return items;
	}

	/**
	 * Gets the equipment item at a given index.
	 *
	 * @param index The item index.
	 * @return The equipped item.
	 */
	public RSItem getItem(int index) {
		return new RSItem(methods, getInterface().getComponent(index));
	}


	public RSItem[] find(final Filter<RSItem> filter) {
		RSItem[] rsItems = getItems();
		RSItem[] filterItems = new RSItem[]{};
		for (RSItem item : rsItems) {
			if (item == null) {
				continue;
			}
			if (filter.accept(item)) {
				RSItem[] addItems = new RSItem[filterItems.length+1];
				addItems[filterItems.length] = item;
				filterItems = addItems;
			}
		}
		return filterItems;
	}


	/**
	 * Returns the number of items equipped excluding stack sizes.
	 *
	 * @return Amount of items currently equipped.
	 */
	public int getCount() {
		return ITEM_SLOTS - getCount(-1);
	}

	/**
	 * Returns the number of items matching a given ID equipped excluding stack
	 * sizes.
	 *
	 * @param itemID The item ID to count. Same as the equipment/item id in the
	 *               inventory.
	 * @return Amount of specified item currently equipped.
	 * @see #getItems()
	 */
	public int getCount(int itemID) {
		int count = 0;
		for (RSItem item : getItems()) {
			if (item.getID() == itemID) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Checks whether the player has all of the given items equipped.
	 *
	 * @param items The item ID to check for. Same as the equipment/item id in the
	 *              inventory.
	 * @return <tt>true</tt> if specified item is currently equipped; otherwise
	 *         <tt>false</tt>.
	 * @see #getItems()
	 */
	public boolean containsAll(int... items) {
		RSItem[] equips = getItems();
		int count = 0;
		for (int item : items) {
			for (RSItem equip : equips) {
				if (equip.getID() == item) {
					count++;
					break;
				}
			}
		}
		return count == items.length;
	}

	/**
	 * Checks if the player has one (or more) of the given items equipped.
	 *
	 * @param items The IDs of items to check for.
	 * @return <tt>true</tt> if the player has one (or more) of the given items
	 *         equipped; otherwise <tt>false</tt>.
	 */
	public boolean containsOneOf(int... items) {
		for (RSItem item : getItems()) {
			for (int id : items) {
				if (item.getID() == id) {
					return true;
				}
			}
		}
		return false;
	}

}
