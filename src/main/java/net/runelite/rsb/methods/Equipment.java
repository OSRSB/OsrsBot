package net.runelite.rsb.methods;

import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.query.RSEquipmentItemQueryBuilder;
import net.runelite.rsb.wrappers.RSItem;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Equipment related operations.
 */
public class Equipment extends MethodProvider {
	private static final int EQUIPMENT_ITEM_SLOTS = 11;
	static final Map<Integer, Integer> runeliteIndexToWidgetChildIndex = Stream.of(new Integer[][]{
			{EquipmentInventorySlot.HEAD.getSlotIdx(), WidgetIndices.WornEquipmentTab.HELMET_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.CAPE.getSlotIdx(), WidgetIndices.WornEquipmentTab.CAPE_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.AMULET.getSlotIdx(), WidgetIndices.WornEquipmentTab.AMULET_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.WEAPON.getSlotIdx(), WidgetIndices.WornEquipmentTab.WEAPON_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.BODY.getSlotIdx(), WidgetIndices.WornEquipmentTab.BODY_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.SHIELD.getSlotIdx(), WidgetIndices.WornEquipmentTab.SHIELD_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.LEGS.getSlotIdx(), WidgetIndices.WornEquipmentTab.LEGS_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.GLOVES.getSlotIdx(), WidgetIndices.WornEquipmentTab.GLOVES_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.BOOTS.getSlotIdx(), WidgetIndices.WornEquipmentTab.BOOTS_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.RING.getSlotIdx(), WidgetIndices.WornEquipmentTab.RING_DYNAMIC_CONTAINER},
			{EquipmentInventorySlot.AMMO.getSlotIdx(), WidgetIndices.WornEquipmentTab.AMMO_DYNAMIC_CONTAINER}
	}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

	Equipment(final MethodContext ctx) {
		super(ctx);
	}


	public RSEquipmentItemQueryBuilder query() {
		return new RSEquipmentItemQueryBuilder();
	}
	/**
	 * Gets the equipment interface.
	 *
	 * @return the equipment interface
	 */
	private RSWidget getInterface() {
		return methods.interfaces.get(WidgetIndices.WornEquipmentTab.GROUP_INDEX);
	}

	public boolean isOpen() {
		return methods.game.getCurrentTab() == InterfaceTab.EQUIPMENT;
	}

	public boolean open() {
		return methods.game.openTab(InterfaceTab.EQUIPMENT);
	}

	public static int getRuneliteIndexToWidgetChildIndex(int index) {
		return runeliteIndexToWidgetChildIndex.get(index);
	}

	/**
	 * Gets the equipment array.
	 *
	 * @return An array containing all equipped items
	 */
	public RSItem[] getItems() {
		getInterface();
		List<RSItem> items = new ArrayList<RSItem>();
		ItemContainer container = methods.client.getItemContainer(InventoryID.EQUIPMENT);
		if (container == null) {
			return new RSItem[]{};
		}
		Item[] cachedItems = container.getItems();
		for (int i = 0; i < cachedItems.length; i++) {
			if (cachedItems[i].getId() != -1) {
				RSWidget slotItem = methods.interfaces.getComponent(WidgetIndices.WornEquipmentTab.GROUP_INDEX, runeliteIndexToWidgetChildIndex.get(i)).getDynamicComponent(1);
				items.add(new RSItem(methods, slotItem, cachedItems[i]));
			}
		}
		return items.toArray(new RSItem[0]);
	}

	/**
	 * Gets the equipment item at a given index.
	 *
	 * @param index The item index. See EquipmentInventorySlot
	 * @return The equipped item.
	 */
	public RSItem getItem(int index) {
		ItemContainer container = methods.client.getItemContainer(InventoryID.EQUIPMENT);
		if (container == null) {
			return null;
		}
		Item cachedItem = container.getItem(index);
		RSWidget widget = getInterface().getComponent(runeliteIndexToWidgetChildIndex.get(index));
		return cachedItem == null ? null : new RSItem(methods, widget, cachedItem);
	}

	/**
	 * Gets the equipment item at a given index.
	 *
	 * @param index The item index. See EquipmentInventorySlot
	 * @return The equipped item.
	 */
	public RSItem getItem(EquipmentInventorySlot index) {
		return getItem(index.getSlotIdx());
	}

	public RSItem[] find(final Predicate<RSItem> filter) {
		RSItem[] rsItems = getItems();
		RSItem[] filterItems = new RSItem[]{};
		for (RSItem item : rsItems) {
			if (item == null) {
				continue;
			}
			if (filter.test(item)) {
				RSItem[] addItems = new RSItem[filterItems.length + 1];
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
	 * <code>false</code>.
	 * @see #getItems()
	 */
	public boolean containsAll(final int... items) {
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
	 * equipped; otherwise <code>false</code>.
	 */
	public boolean containsOneOf(final int... items) {
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
