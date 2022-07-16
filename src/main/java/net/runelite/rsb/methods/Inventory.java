package net.runelite.rsb.methods;

import net.runelite.api.ItemComposition;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.cache.definitions.ItemDefinition;
import net.runelite.client.ui.DrawManager;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.wrappers.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Inventory related operations.
 *
 * @author GigiaJ
 */
public class Inventory extends MethodProvider {

	private final int EMPTY_SLOT_ITEM_ID = 6512;

	Inventory(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Gets the inventory interface.
	 *
	 * @return the inventory interface
	 */
	public Map.Entry<String, RSWidget> getInterface() {
		final String INVENTORY = "inventory", BANK = "bank", STORE = "store", GRAND_EXCHANGE = "grandexchange", TRADE = "trade";
		HashMap<String, RSWidget> widgets = new HashMap<>();
		widgets.put(INVENTORY, methods.interfaces.getComponent(GlobalWidgetInfo.INVENTORY_ITEMS_CONTAINER));
		widgets.put(BANK, methods.interfaces.getComponent(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER)); //Bug #137 - Bank has its own inventory container
		widgets.put(STORE, methods.interfaces.getComponent(WidgetInfo.SHOP_INVENTORY_ITEMS_CONTAINER));
		widgets.put(GRAND_EXCHANGE, methods.interfaces.getComponent(WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER));
		widgets.put(TRADE, methods.interfaces.getComponent(GlobalWidgetInfo.TRADE_MAIN_SCREEN_WINDOW_CONTAINER));

		for (Map.Entry<String, RSWidget> entry : widgets.entrySet()) {
			if (entry.getKey().equals(INVENTORY) && methods.game.getCurrentTab() != InterfaceTab.INVENTORY) {
				methods.game.openTab(InterfaceTab.INVENTORY);
				sleep(random(50, 100));
				return entry;
			}
			if (isOpen(entry.getValue())) {
				return entry;
			}
		}
		return null;
	}

	private static boolean isOpen(RSWidget widget) {
		return (widget.isValid() && widget.isVisible());
	}

	/**
	 * Destroys any inventory items with the given ID.
	 *
	 * @param itemID The ID of items to destroy.
	 * @return <code>true</code> if the items were destroyed; otherwise
	 *         <code>false</code>.
	 */
	public boolean destroyItem(final int itemID) {
		RSItem item = getItem(itemID);
		if (!itemHasAction(item, "Destroy")) {
			return false;
		}
		while (item != null) {
			if (methods.interfaces.get(GlobalWidgetInfo.DIALOG_DESTROY_ITEM.getGroupId()).isValid()) {
				methods.interfaces.getComponent(GlobalWidgetInfo.DIALOG_DESTROY_ITEM_YES).doClick();
			} else {
				item.doAction("Destroy");
			}
			sleep(random(700, 1100));
			item = getItem(itemID);
		}
		return true;
	}

	/**
	 * Destroys any inventory items with the given name.
	 *
	 * @param name The name of items to destroy.
	 * @return <code>true</code> if the items were destroyed; otherwise
	 *         <code>false</code>.
	 */
	public boolean destroyItem(final String name) {
		return destroyItem(getItemID(name));
	}

	/**
	 * Drops all items with the same specified id.
	 *
	 * @param leftToRight <code>true</code> to drop items from left to right.
	 * @param items       The item IDs to drop
	 */
	public void dropAllExcept(final boolean leftToRight, final int... items) {
		RSTile startLocation = methods.players.getMyPlayer().getLocation();
		boolean found_droppable = true;
		while (found_droppable && getCountExcept(items) != 0) {
			if (methods.calc.distanceTo(startLocation) > 100) {
				break;
			}
			found_droppable = false;
			for (int j = 0; j < 28; j++) {
				int c = leftToRight ? j % 4 : j / 7;
				int r = leftToRight ? j / 4 : j % 7;
				RSItem curItem = getItems()[c + r * 4];
				int id;
				if (curItem != null && (id = curItem.getID()) != -1 && id != EMPTY_SLOT_ITEM_ID) {
					boolean isInItems = false;
					for (int i : items) {
						isInItems |= (i == id);
					}
					if (!isInItems) {
						found_droppable |= dropItem(c, r);
					}
				}
			}
			sleep(random(500, 800));
		}
	}

	/**
	 * Drops all items with the specified names.
	 *
	 * @param leftToRight <code>true</code> to drop items from left to right.
	 * @param names       The item names to drop
	 */
	public void dropAllExcept(final boolean leftToRight, final String... names) {
		dropAllExcept(leftToRight, getItemIDs(names));
	}

	/**
	 * Determines if the item contains the desired action.
	 *
	 * @param item   The item to check.
	 * @param action The item menu action to check.
	 * @return <code>true</code> if the item has the action; otherwise
	 *         <code>false</code>.
	 */
	public boolean itemHasAction(final RSItem item, final String action) {
		// Used to determine if an item is droppable/destroyable
		if (item == null) { return false; }
		ItemDefinition itemDef = item.getDefinition();
		if (itemDef != null) {
			for (String a : itemDef.getInterfaceOptions()) {
				if (a != null && a.equalsIgnoreCase(action)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Determines if inventory contains at least one item with the desired action.
	 *
	 * @param action The item menu action to check.
	 * @return <code>true</code> if any item has the action; otherwise
	 *         <code>false</code>.
	 */
	public boolean containsItemWithAction(final String action) {
		RSItem[] allInvItems = getItems();
		for (RSItem item : allInvItems) {
			if (itemHasAction(item, action)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns first item with desired action
	 *
	 * @param action The item menu action to check.
	 * @return The item, or <code>null</code> if not found.
	 */
	public RSItem getFirstWithAction(final String action) {
		RSItem[] allInvItems = getItems();
		for (RSItem item : allInvItems) {
			if (itemHasAction(item, action)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Returns all items with desired action
	 *
	 * @param action The item menu action to check.
	 * @return The items array with desired action.
	 */
	public RSItem[] getAllWithAction(final String action) {
		RSItem[] allInvItems = getItems();
		RSItem[] resultItems = new RSItem[allInvItems.length];
		for (int i = 0; i < allInvItems.length; i++) {
			if (itemHasAction(allInvItems[i], action)) {
				resultItems[i] = allInvItems[i];
			}
		}
		return resultItems;
	}

	/**
	 * Drops all items with the same specified id. This method drops items
	 * vertically going down the inventory.
	 *
	 * @param items The item IDs to drop.
	 * @return <code>true</code> at all times.
	 * @see #dropAllExcept(boolean, int...)
	 */
	public boolean dropAllExcept(final int... items) {
		dropAllExcept(false, items);
		return true;
	}

	/**
	 * Drops all items with the specified names. This method drops items
	 * vertically going down the inventory.
	 *
	 * @param names The item names to drop.
	 * @return <code>true</code> at all times.
	 * @see #dropAllExcept(boolean, int...)
	 */
	public boolean dropAllExcept(final String... names) {
		return dropAllExcept(getItemIDs(names));
	}

	/**
	 * Drops the item in the specified column and row.
	 *
	 * @param col The column the item is in.
	 * @param row The row the item is in.
	 * @return <code>true</code> if we tried to drop the item,
	 *         <code>false</code> if not (e.g., if item is undroppable)
	 */
	public boolean dropItem(final int col, final int row) {
		if (methods.interfaces.canContinue()) {
			methods.interfaces.clickContinue();
			sleep(random(800, 1300));
		}
		if (methods.game.getCurrentTab() != InterfaceTab.INVENTORY
				&& !methods.interfaces.get(WidgetIndices.Bank.GROUP_INDEX).isValid()
				&& !methods.interfaces.get(WidgetIndices.Store.GROUP_INDEX).isValid()) {
			methods.game.openTab(InterfaceTab.INVENTORY);
		}
		if (col < 0 || col > 3 || row < 0 || row > 6) {
			return false;
		}
		RSItem item = getItems()[col + row * 4];
		return item != null && item.getID() != -1 && item.getID() != EMPTY_SLOT_ITEM_ID && item.doAction("Drop");
	}

	/**
	 * Checks whether or not your inventory contains the provided item ID.
	 *
	 * @param itemID The item(s) you wish to evaluate.
	 * @return <code>true</code> if your inventory contains an item with the ID
	 *         provided; otherwise <code>false</code>.
	 * @see #containsOneOf(int...)
	 * @see #containsAll(int...)
	 */
	public boolean contains(final int itemID) {
		return getItem(itemID) != null;
	}

	/**
	 * Checks whether or not your inventory contains the provided item name.
	 *
	 * @param name The name you wish to evaluate.
	 * @return <code>true</code> if your inventory contains an item with the name
	 *         provided; otherwise <code>false</code>.
	 * @see #containsOneOf(int...)
	 * @see #containsAll(int...)
	 */
	public boolean contains(final String name) {
		return contains(getItemID(name));
	}

	/**
	 * Checks whether or not your inventory contains all of the provided item
	 * IDs.
	 *
	 * @param itemID The item(s) you wish to evaluate.
	 * @return <code>true</code> if your inventory contains at least one of all of
	 *         the item IDs provided; otherwise <code>false</code>.
	 * @see #containsOneOf(int...)
	 */
	public boolean containsAll(final int... itemID) {
		for (int i : itemID) {
			if (getItem(i) == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks whether or not your inventory contains all of the provided item
	 * IDs.
	 *
	 * @param names The item(s) you wish to evaluate.
	 * @return <code>true</code> if your inventory contains at least one of all of
	 *         the item IDs provided; otherwise <code>false</code>.
	 * @see #containsOneOf(int...)
	 */
	public boolean containsAll(final String... names) {
		if (!(getItemIDs(names).length == names.length)) return false;
		return containsAll(getItemIDs(names));
	}

	/**
	 * Checks whether or not your inventory contains at least one of the
	 * provided item IDs.
	 *
	 * @param itemID The item ID to check for.
	 * @return <code>true</code> if inventory contains one of the specified items;
	 *         otherwise <code>false</code>.
	 * @see #containsAll(int...)
	 */
	public boolean containsOneOf(final int... itemID) {
		RSItem[] items = getItems();
		for (RSItem item : items) {
			for (int i : itemID) {
				if (item.getID() == i) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks whether or not your inventory contains at least one of the
	 * provided item names.
	 *
	 * @param names The item names to check for.
	 * @return <code>true</code> if inventory contains one of the specified items;
	 *         otherwise <code>false</code>.
	 * @see #containsAll(int...)
	 */
	public boolean containsOneOf(final String... names) {
		return containsOneOf(getItemIDs(names));
	}

	/**
	 * Checks whether or not your inventory is full.
	 *
	 * @return <code>true</code> if your inventory contains 28 items; otherwise
	 *         <code>false</code>.
	 */
	public boolean isFull() {
		return getCount() == 28;
	}

	/**
	 * Checks whether or not an inventory item is selected.
	 *
	 * @return <code>true</code> if an item in your inventory is selected; otherwise
	 *         <code>false</code>.
	 */
	public boolean isItemSelected() {
		return getSelectedItemIndex() != -1;
	}

	/**
	 * Selects the first item in the inventory with the provided ID.
	 *
	 * @param itemID The ID of the item to select.
	 * @return <code>true</code> if the item was selected; otherwise <code>false</code>.
	 */
	public boolean selectItem(final int itemID) {
		final RSItem item = getItem(itemID);
		return item != null && selectItem(item);
	}

	/**
	 * Selects the first item in the inventory with the provided name.
	 *
	 * @param name The name of the item to select.
	 * @return <code>true</code> if the item was selected; otherwise <code>false</code>.
	 */
	public boolean selectItem(final String name) {
		return selectItem(getItemID(name));
	}

	/**
	 * Selects the specified item in the inventory
	 *
	 * @param item The item to select.
	 * @return <code>true</code> if the item was selected; otherwise <code>false</code>.
	 */
	public boolean selectItem(RSItem item) {
		final int itemID = item.getID();
		if (itemID == -1 || itemID == EMPTY_SLOT_ITEM_ID) { return false; }
		RSItem selItem = getSelectedItem();
		if (selItem != null && selItem.getID() == itemID) {
			return true;
		}
		if (!item.doAction("Use")) { return false; }
		/*
		for (int c = 0; c < 5 && getSelectedItem() == null; c++) {
			sleep(random(40, 60));
		}
		*/
		sleep(random(60, 80));
		selItem = getSelectedItem();
		return selItem != null && selItem.getID() == itemID;
	}

	/**
	 * Uses two items together.
	 *
	 * @param item       The item to use on another item.
	 * @param targetItem The item you want the first parameter to be used on.
	 * @return <code>true</code> if the "use" action had been used on both items;
	 *         otherwise <code>false</code>.
	 */
	public boolean useItem(final RSItem item, final RSItem targetItem) {
		if (methods.game.getCurrentTab() != InterfaceTab.INVENTORY) {
			methods.game.openTab(InterfaceTab.INVENTORY);
		}
		return selectItem(item) && targetItem.doAction("Use");
	}

	/**
	 * Uses two items together.
	 *
	 * @param itemID   The first item ID to use.
	 * @param targetID The item ID you want the first parameter to be used on.
	 * @return <code>true</code> if the first item has been "used" on the other;
	 *         otherwise <code>false</code>.
	 */
	public boolean useItem(final int itemID, final int targetID) {
		RSItem item = getItem(itemID);
		RSItem target = getItem(targetID);
		return item != null && target != null && useItem(item, target);
	}

	/**
	 * Uses two items together.
	 *
	 * @param name   The first item to use.
	 * @param targetName The item name you want the first parameter to be used on.
	 * @return <code>true</code> if the first item has been "used" on the other;
	 *         otherwise <code>false</code>.
	 */
	public boolean useItem(final String name, final String targetName) {
		return useItem(getItemID(name), getItemID(targetName));
	}

	/**
	 * Uses an item on an object.
	 *
	 * @param item         The item to use on another item.
	 * @param targetObject The RSObject you want the first parameter to be used on.
	 * @return <code>true</code> if the "use" action had been used on both the
	 *         RSItem and RSObject; otherwise <code>false</code>.
	 */
	public boolean useItem(RSItem item, RSObject targetObject) {
		if (methods.game.getCurrentTab() != InterfaceTab.INVENTORY) {
			methods.game.openTab(InterfaceTab.INVENTORY);
		}
		return selectItem(item) && targetObject.doAction("Use", targetObject.getName());
	}

	/**
	 * Uses an item on an object.
	 *
	 * @param itemID The item ID to use on the object.
	 * @param object The RSObject you want the item to be used on.
	 * @return <code>true</code> if the "use" action had been used on both the
	 *         RSItem and RSObject; otherwise <code>false</code>.
	 */
	public boolean useItem(final int itemID, final RSObject object) {
		RSItem item = getItem(itemID);
		return item != null && useItem(item, object);
	}

	/**
	 * Uses an item on an object.
	 *
	 * @param name The item name to use on the object.
	 * @param object The RSObject you want the item to be used on.
	 * @return <code>true</code> if the "use" action had been used on both the
	 *         RSItem and RSObject; otherwise <code>false</code>.
	 */
	public boolean useItem(final String name, final RSObject object) {
		return useItem(getItemID(name), object);
	}

	/**
	 * Randomizes a point.
	 *
	 * @param inventoryPoint The inventory point to be randomized.
	 * @return A randomized <code>Point</code> from the center of the given
	 *         <code>Point</code>.
	 */
	public Point randomizeItemPoint(final Point inventoryPoint) {
		return new Point(inventoryPoint.x + random(-10, 10), inventoryPoint.y
				+ random(-10, 10));
	}

	/**
	 * Gets the selected item name.
	 *
	 * @return The name of the current selected item, or null if none is
	 *         selected.
	 */
	public String getSelectedItemName() {
		RSWidget invInterface = getInterface().getValue();
		int index = getSelectedItemIndex();
		if (index == -1)
			return null;
		String name = invInterface.getComponents()[index].getName();
		return !isItemSelected() || name == null ? null : name.replaceAll(
				"<[\\w\\d]+=[\\w\\d]+>", "");
	}

	/**
	 * Gets the selected item index.
	 *
	 * @return The index of current selected item, or -1 if none is selected.
	 */
	public int getSelectedItemIndex() {
		RSWidget invInterface = getInterface().getValue();
			RSWidget[] comps = invInterface.getComponents();
			for (int i = 0; i < Math.min(28, comps.length); ++i) {
				if (comps[i].getBorderThickness() == 2) {
					return i;
				}
		}
		return -1;
	}

	/**
	 * Uses a callback to get the last drawn image and then performs the method getSelected to update the
	 * isSelectedValue
	 *
	 * @param comps the item to check if is selected
	 * @return the index of the item selected; otherwise -1
	 */
	public int checkIsSelected(RSWidgetItem[] comps) {
		class Selector {
			int selected = -1;
			public void setSelected(int selection) {
				selected = selection;
			}
			public int getSelected() {
				return selected;
			}
		}
		Selector selector = new Selector();
		Consumer<Image> imageCallback = (img) ->
		{ // This callback is on the game thread, move to executor thread
			{
				methods.runeLite.getInjector().getInstance(ScheduledExecutorService.class).submit(()-> {
					selector.setSelected(getSelected(img, comps));
					synchronized (selector) {
						selector.notify();
					}
				});
			}
		};
		methods.runeLite.getInjector().getInstance(DrawManager.class).requestNextFrameListener(imageCallback);
		synchronized (selector) {
			try {
				selector.wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
 		}
		return selector.getSelected();
	}

	/**
	 * Checks the image for the item position and then checks that set of bounds for any pure white pixels
	 * which would indicate that the item is selected (No items use 255, 255, 255)
	 * If it finds any the isSelected value is updated to true
	 *
	 * @param img the client image
	 * @param comps the item to check for if it is selected
	 * @return the index of the item selected; otherwise -1
	 */
	public int getSelected(Image img, RSWidgetItem[] comps) {
		BufferedImage im = new BufferedImage(methods.client.getCanvasWidth(), methods.client.getCanvasHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = im.getGraphics();
		graphics.drawImage(img, 0, 0, null);
		for (int c = 0; c < Math.min(comps.length, 28); ++c) {
			RSItem item = new RSItem(methods, comps[c]);
			int x = item.getItem().getItemLocation().getX();
			int y = item.getItem().getItemLocation().getY();
			BufferedImage itemImage = methods.runeLite.getItemManager().getImage(item.getID());
			int height = itemImage.getHeight();
			int width = itemImage.getWidth();
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < 2; j++) {
					if (i + j < width) {
						int colorValue = im.getRGB(x + i + j, y + i);
						Color color = new Color(colorValue);
						Color white = new Color(255, 255, 255, 255);
						if (color.equals(white)) {
							return c;
						}
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Gets the selected inventory item.
	 *
	 * @return The current selected item, or <code>null</code> if none is selected.
	 */
	public RSItem getSelectedItem() {
		int index = getSelectedItemIndex();
		return index == -1 ? null : getItemAt(index);
	}

	/**
	 * Clicks selected inventory item, if it's selected.
	 *
	 * @param leftClick <code>true</code> for left button click, <code>false</code> for right
	 *                  button.
	 * @return <code>true</code> if item was selected, <code>false</code> if not.
	 */
	public boolean clickSelectedItem(final boolean leftClick) {
		RSItem item = getSelectedItem();
		return item != null && item.doClick(true);
	}

	/**
	 * Left-clicks on the selected item.
	 *
	 * @return <code>true</code> if item was selected, <code>false</code> if not.
	 * @see #clickSelectedItem(boolean)
	 */
	public boolean clickSelectedItem() {
		return clickSelectedItem(true);
	}

	/**
	 * Gets inventory item at specified index.
	 *
	 * @param index The index of inventory item.
	 * @return The item, or <code>null</code> if not found.
	 */
	public RSItem getItemAt(final int index) {
		RSWidget invInterface = getInterface().getValue();
		RSWidget comp = invInterface.getComponent(index);
		return 0 <= index && index < 28 && comp.getId() != EMPTY_SLOT_ITEM_ID ? new RSItem(methods, comp) : null;
	}

	/**
	 * Gets all the items in the inventory.
	 *
	 * @return <code>RSItem</code> array of the current inventory items or new
	 *         <code>RSItem[0]</code>.
	 */
	public RSItem[] getItems() {
		RSWidget invInterface = getInterface().getValue();
		RSWidget[] invItems = invInterface.getComponents();
		RSItem[] items = new RSItem[invItems.length];
		for (int i = 0; i < invItems.length; i++) {
			if (invItems[i].getId() != EMPTY_SLOT_ITEM_ID)
				items[i] = new RSItem(methods, invItems[i]);
		}
		return items;
	}

	/**
	 * Gets all the items in the inventory matching any of the provided IDs.
	 *
	 * @param ids Valid IDs.
	 * @return <code>RSItem</code> array of the matching inventory items.
	 */
	public RSItem[] getItems(final int... ids) {
		LinkedList<RSItem> items = new LinkedList<>();
		for (RSItem item : getItems()) {
			for (int i : ids) {
				if (item.getID() == i) {
					items.add(item);
					break;
				}
			}
		}
		return items.toArray(new RSItem[items.size()]);
	}

	/**
	 * Gets all the items in the inventory matching any of the provided names.
	 *
	 * @param names Valid IDs.
	 * @return <code>RSItem</code> array of the matching inventory items.
	 */
	public RSItem[] getItems(final String... names) {
		return getItems(getItemIDs(names));
	}

	/**
	 * Gets all the items in the inventory. If the tab is not currently open, it
	 * does not open it and returns the last known array of items in the tab.
	 *
	 * @return <code>RSItem</code> array of the cached inventory items or new
	 *         <code>RSItem[0]</code>.
	 */
	public RSItem[] getCachedItems() {
		RSWidget invInterface = getInterface().getValue();
		if (invInterface == null) return null;
		RSWidget[] invItems = invInterface.getComponents();
		RSItem[] items = new RSItem[invItems.length];
		for (int i = 0; i < invItems.length; i++) {
			items[i] = new RSItem(methods, invItems[i]);
		}
		return items;
	}

	/**
	 * Gets the ID of an item in the inventory with a given name.
	 *
	 * @param name The name of the item you wish to find.
	 * @return The ID of the item or -1 if not in inventory.
	 */
	public int getItemID(final String name) {
		RSItem[] items = getItems();
		int slot = -1;
		for (RSItem item : items) {
			ItemDefinition def = item.getDefinition();
			if (def != null && def.getName().contains(name)) {
				slot = item.getID();
				break;
			}
		}
		return slot;
	}

	/**
	 * Gets the IDs of items in the inventory with given names.
	 *
	 * @param names The names of the item IDs you wish to find.
	 * @return The IDs of the items or null if not in inventory.
	 */
	public int[] getItemIDs(final String[] names) {
		ArrayList<Integer> itemIDs = new ArrayList<>();
		for (String name : names) {
			int itemID = getItemID(name);
			if (itemID != -1 && itemID != EMPTY_SLOT_ITEM_ID) {
				itemIDs.add(itemID);
			}
		}
		int[] arr = new int[itemIDs.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = itemIDs.get(i);
		}
		return arr;
	}

	/**
	 * Gets the first item in the inventory with any of the provided IDs.
	 *
	 * @param ids The IDs of the item to find.
	 * @return The first <code>RSItem</code> for the given IDs; otherwise null.
	 */
	public RSItem getItem(final int... ids) {
		RSItem[] items = getItems();
		for (RSItem item : items) {
			for (int id : ids) {
				if (item.getID() == id) {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the first item in the inventory with any of the provided names.
	 *
	 * @param names The names of the item to find.
	 * @return The first <code>RSItem</code> for the given names; otherwise null.
	 */
	public RSItem getItem(final String... names) {
		return getItem(getItemIDs(names));
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
	 * Gets the count of all the items in the inventory without any of the
	 * provided IDs. This ignores stack sizes.
	 *
	 * @param ids The item IDs to exclude.
	 * @return The count.
	 */
	public int getCountExcept(final int... ids) {
		return getCountExcept(false, ids);
	}

	/**
	 * Gets the count of all the items in the inventory without any of the
	 * provided names. This ignores stack sizes.
	 *
	 * @param names The item names to exclude.
	 * @return The count.
	 */
	public int getCountExcept(final String... names) {
		return getCountExcept(false, names);
	}

	/**
	 * Gets the count of all the items in the inventory without any of the
	 * provided IDs.
	 *
	 * @param includeStacks <code>true</code> to count the stack sizes of each item;
	 *                      <code>false</code> to count a stack as a single item.
	 * @param ids           The item IDs to exclude.
	 * @return The count.
	 */
	public int getCountExcept(final boolean includeStacks, final int... ids) {
		RSItem[] items = getItems();
		int count = 0;
		for (RSItem i : items) {
			if (i.getID() == -1) {
				continue;
			}
			boolean skip = false;
			for (int id : ids) {
				if (i.getID() == id) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				count += includeStacks ? i.getStackSize() : 1;
			}
		}
		return count;
	}

	/**
	 * Gets the count of all the items in the inventory without any of the
	 * provided names.
	 *
	 * @param includeStacks <code>true</code> to count the stack sizes of each item;
	 *                      <code>false</code> to count a stack as a single item.
	 * @param names           The item names to exclude.
	 * @return The count.
	 */
	public int getCountExcept(final boolean includeStacks, final String... names) {
		return getCountExcept(includeStacks, getItemIDs(names));
	}

	/**
	 * Gets the count of all the items in the inventory with the any of the
	 * specified IDs. This ignores stack sizes.
	 *
	 * @param itemIDs the item IDs to include
	 * @return The count.
	 */
	public int getCount(final int... itemIDs) {
		return getCount(false, itemIDs);
	}

	/**
	 * Gets the count of all the items in the inventory with the any of the
	 * specified names. This ignores stack sizes.
	 *
	 * @param names the item names to include
	 * @return The count.
	 */
	public int getCount(final String... names) {
		return getCount(false, names);
	}

	/**
	 * Gets the count of all the items in the inventory with the any of the
	 * specified IDs.
	 *
	 * @param includeStacks <code>true</code> to count the stack sizes of each item;
	 *                      <code>false</code> to count a stack as a single item.
	 * @param itemIDs       the item IDs to include
	 * @return The count.
	 */
	public int getCount(final boolean includeStacks, final int... itemIDs) {
		int total = 0;
		for (RSItem item : getItems()) {
			if (item == null) {
				continue;
			}
			for (int ID : itemIDs) {
				if (item.getID() == ID) {
					total += includeStacks ? item.getStackSize() : 1;
				}
			}
		}
		return total;
	}

	/**
	 * Gets the count of all the items in the inventory with the any of the
	 * specified names.
	 *
	 * @param includeStacks <code>true</code> to count the stack sizes of each item;
	 *                      <code>false</code> to count a stack as a single item.
	 * @param names       the item names to include
	 * @return The count.
	 */
	public int getCount(final boolean includeStacks, final String... names) {
		return getCount(includeStacks, getItemIDs(names));
	}

	/**
	 * Gets the count of all items in your inventory ignoring stack sizes.
	 *
	 * @return The count.
	 */
	public int getCount() {
		return getCount(false);
	}

	/**
	 * Gets the count of all items in your inventory.
	 *
	 * @param includeStacks <code>false</code> if stacked items should be counted as a single
	 *                      item; otherwise <code>true</code>.
	 * @return The count.
	 */
	public int getCount(final boolean includeStacks) {
		int count = 0;
		RSItem[] items = getItems();
		for (RSItem item : items) {
			int iid = item.getID();
			if (iid != -1 && iid != EMPTY_SLOT_ITEM_ID) {
				if (includeStacks) {
					count += item.getStackSize();
				} else {
					++count;
				}
			}
		}
		return count;
	}
}
