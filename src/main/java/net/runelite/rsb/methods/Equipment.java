package net.runelite.rsb.methods;

import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.wrappers.RSItem;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.function.Predicate;

/**
 * Equipment related operations.
 */
public class Equipment extends MethodProvider {
	private static final int EQUIPMENT_ITEM_SLOTS = 11;

	Equipment(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Gets the equipment interface.
	 *
	 * @return the equipment interface
	 */
	public RSWidget getInterface() {
		return getInterface(true);
	}

	private RSWidget getInterface(boolean update) {
		// Tab needs to be open for it to update its content -.-
		if (update && methods.game.getCurrentTab() != InterfaceTab.EQUIPMENT) {
			if (methods.bank.isOpen()) {
				methods.bank.close();
			}
			methods.game.openTab(InterfaceTab.EQUIPMENT);
			sleep(random(900, 1500));
		}
		return methods.interfaces.get(WidgetIndices.WornEquipmentTab.GROUP_INDEX);
	}

	/**
	 * Gets the equipment array.
	 *
	 * @return An array containing all equipped items
	 */
	public RSItem[] getItems() {
		getInterface(true);
		RSItem[] items = new RSItem[EQUIPMENT_ITEM_SLOTS];
		for (int i = 0; i < items.length; i++) {
			RSWidget slotItem = methods.interfaces.getComponent(WidgetIndices.WornEquipmentTab.GROUP_INDEX, i + WidgetIndices.WornEquipmentTab.HEAD_DYNAMIC_CONTAINER).getDynamicComponent(1);
			items[i] = new RSItem(methods, slotItem);
		}
		return items;
	}

	/**
	 * Gets the cached equipment array (i.e. does not open the interface).
	 *
	 * @return The items equipped as seen when the equipment tab was last
	 *         opened.
	 */
	private RSItem[] getCachedItems() {
		RSWidget[] equipment = getInterface(false).getComponents();
		RSItem[] items = new RSItem[EQUIPMENT_ITEM_SLOTS];
		for (int i = 0; i < items.length; i++) {
			RSWidget slotItem = equipment[i + WidgetIndices.WornEquipmentTab.HEAD_DYNAMIC_CONTAINER].getDynamicComponent(1);
			items[i] = new RSItem(methods, slotItem);
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


	public RSItem[] find(final Predicate<RSItem> filter) {
		RSItem[] rsItems = getItems();
		RSItem[] filterItems = new RSItem[]{};
		for (RSItem item : rsItems) {
			if (item == null) {
				continue;
			}
			if (filter.test(item)) {
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
		return EQUIPMENT_ITEM_SLOTS - getCount(-1);
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
	 * @return <code>true</code> if specified item is currently equipped; otherwise
	 *         <code>false</code>.
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
	 * @return <code>true</code> if the player has one (or more) of the given items
	 *         equipped; otherwise <code>false</code>.
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
