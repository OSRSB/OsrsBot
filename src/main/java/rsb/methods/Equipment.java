package rsb.methods;

import rsb.internal.globval.GlobalWidgetId;
import rsb.wrappers.RSItem;
import rsb.wrappers.RSWidget;

import java.util.function.Predicate;

/**
 * Equipment related operations.
 */
public class Equipment extends MethodProvider {
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
		if (methods.game.getCurrentTab() != GameGUI.Tab.EQUIPMENT) {
			if (methods.bank.isOpen()) {
				methods.bank.close();
			}
			methods.game.openTab(GameGUI.Tab.EQUIPMENT);
			sleep(random(900, 1500));
		}
		return methods.interfaces.get(GlobalWidgetId.INTERFACE_EQUIPMENT);
	}

	/**
	 * Gets the equipment array.
	 *
	 * @return An array containing all equipped items
	 */
	public RSItem[] getItems() {
		RSWidget[] equip = getInterface().getComponents();
		RSItem[] items = new RSItem[GlobalWidgetId.INTERFACE_EQUIPMENT_ITEM_SLOTS];
		for (int i = 0; i < items.length; i++) {
			items[i] = new RSItem(methods, equip[i + GlobalWidgetId.EquipmentSlotId.INTERFACE_EQUIPMENT_HELMET].getDynamicComponent(1));
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
		RSWidget equipment = getInterface();
		RSWidget[] components = equipment.getComponents();
		RSItem[] items = new RSItem[GlobalWidgetId.INTERFACE_EQUIPMENT_ITEM_SLOTS];
		for (int i = 0; i < items.length; i++) {
			items[i] = new RSItem(methods, components[i + GlobalWidgetId.EquipmentSlotId.INTERFACE_EQUIPMENT_HELMET].getDynamicComponent(1));
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
		return GlobalWidgetId.INTERFACE_EQUIPMENT_ITEM_SLOTS - getCount(-1);
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
