package net.runelite.client.rsb.methods;

import net.runelite.api.ItemDefinition;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.rsb.wrappers.*;
import net.runelite.client.ui.DrawManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Inventory related operations.
 *
 * @author Jacmob, Aut0r, kiko
 */
public class Inventory extends MethodProvider {

	//Identifies whether an item is selected or not
	public volatile boolean isSelected = false;

	/**
	 * Enumerations of the group IDs for item containers
	 * Used in class to identify the different interfaces for inventory
	 */
	private enum InventoryInterfaceId {
		//INVENTORY_CONTAINER_GROUP_ID
		INVENTORY(149),
		//BANK_INVENTORY_ITEMS_CONTAINER_GROUP_ID
		BANK(15),
		//STORE_INVENTORY_CONTAINER_GROUP_ID
		STORE(301),
		//GRAND_EXCHANGE_INVENTORY_GROUP_ID
		GRAND_EXCHANGE(467),
		//TRADE_INVENTORY_GROUP_ID
		TRADE(336);
		private int index;

		InventoryInterfaceId(int index) {
			this.index = index;
		}

		int getIndex() {
			return index;
		}

	}

	/**
	 * Holds the widget and name of the widget for in file use
	 */
	private class InventoryInterface {
		private String name;
		private RSWidget widget;
		InventoryInterface(RSWidget widget, String name) {
			this.widget = widget;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public RSWidget getWidget() {
			return widget;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setWidget(RSWidget widget) {
			this.widget = widget;
		}
	}

	/**
	 * Possibly unnecessary, but used in the selection methods
	 * Holds values for the return of the <type>BufferedImage</type>
	 * method getRGB
	 */
	private class Color {
		// Components will be in the range of 0..255:
		int blue;
		int green;
		int red;
		int alpha;

		Color(int color) {
			blue = color & 0xff;
			green = (color & 0xff00) >> 8;
			red = (color & 0xff0000) >> 16;
			alpha = (color & 0xff000000) >>> 24;
		}

		Color(int red, int green, int blue, int alpha) {
			this.blue = blue;
			this.green = green;
			this.red = red;
			this.alpha = alpha;
		}

		private boolean equals(Color color) {
			if (this.red != color.red) {
				return false;
			}
			if (this.blue != color.blue) {
				return false;
			}
			if (this.green != color.green) {
				return false;
			}
			if (this.alpha != color.alpha) {
				return false;
			}
			return true;
		}
	}

	Inventory(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Gets the inventory interface.
	 *
	 * @return the inventory interface
	 */
	public InventoryInterface getInterface() {
		final String INVENTORY = "inventory", BANK = "bank", STORE = "store", GRAND_EXCHANGE = "grandexchange", TRADE = "trade";
		RSWidget inventoryWidget = methods.interfaces.getComponent(InventoryInterfaceId.INVENTORY.getIndex(), 0),
				bankWidget = methods.interfaces.getComponent(InventoryInterfaceId.BANK.getIndex(), 3),
				storeWidget = methods.interfaces.getComponent(InventoryInterfaceId.STORE.getIndex(), 0),
				grandExchangeWidget = methods.interfaces.getComponent(InventoryInterfaceId.GRAND_EXCHANGE.getIndex(), 0),
				tradeWidget = methods.interfaces.getComponent(InventoryInterfaceId.TRADE.getIndex(), 0);
		if (bankWidget.isValid() && bankWidget.isVisible()) {
				return new InventoryInterface(bankWidget, BANK);
		}
		if (storeWidget.isValid() && storeWidget.isVisible()) {
				return new InventoryInterface(storeWidget, STORE);
		}
		if (grandExchangeWidget.isValid() && grandExchangeWidget.isVisible()) {
			return new InventoryInterface(grandExchangeWidget, GRAND_EXCHANGE);
		}
		if (tradeWidget.isValid() && tradeWidget.isVisible()) {
			return new InventoryInterface(tradeWidget, TRADE);
		}

		// Tab has to be open for us to get content
		if (methods.game.getCurrentTab() != Game.TAB_INVENTORY) {
			methods.game.openTab(Game.TAB_INVENTORY);
			sleep(random(400, 900));
		}

		return new InventoryInterface(inventoryWidget, INVENTORY);
	}


	/**
	 * Destroys any inventory items with the given ID.
	 *
	 * @param itemID The ID of items to destroy.
	 * @return <tt>true</tt> if the items were destroyed; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean destroyItem(final int itemID) {
		RSItem item = getItem(itemID);
		if (!itemHasAction(item, "Destroy")) {
			return false;
		}
		while (item != null) {
			if (methods.interfaces.get(WidgetInfo.DESTROY_ITEM.getGroupId()).isValid()) {
				methods.interfaces.getComponent(WidgetInfo.DESTROY_ITEM.getGroupId(), WidgetInfo.DESTROY_ITEM_YES.getChildId()).doClick();
			} else {
				item.doAction("Destroy");
			}
			sleep(random(700, 1100));
			item = getItem(itemID);
		}
		return true;
	}

	/**
	 * Drops all items with the same specified id.
	 *
	 * @param leftToRight <tt>true</tt> to drop items from left to right.
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
				if (curItem != null
						&& (id = curItem.getID()) != -1) {
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
	 * Determines if the item contains the desired action.
	 *
	 * @param item   The item to check.
	 * @param action The item menu action to check.
	 * @return <tt>true</tt> if the item has the action; otherwise
	 *         <tt>false</tt>.
	 * @author Aut0r
	 */
	public boolean itemHasAction(final RSItem item, final String action) {
		// Used to determine if an item is droppable/destroyable
		if (item == null) {
			return false;
		}
		ItemDefinition itemDef = item.getDefinition();
		if (itemDef != null) {
			for (String a : itemDef.getInventoryActions()) {
				if (a != null && a.equalsIgnoreCase(action)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Drops all items with the same specified id. This method drops items
	 * vertically going down the inventory.
	 *
	 * @param items The item IDs to drop.
	 * @return <tt>true</tt> at all times.
	 * @see #dropAllExcept(boolean, int...)
	 */
	public boolean dropAllExcept(final int... items) {
		dropAllExcept(false, items);
		return true;
	}

	/**
	 * Drops the item in the specified column and row.
	 *
	 * @param col The column the item is in.
	 * @param row The row the item is in.
	 * @return <tt>true</tt> if we tried to drop the item,
	 *         <tt>false</tt> if not (e.g., if item is undroppable)
	 */
	public boolean dropItem(final int col, final int row) {
		if (methods.interfaces.canContinue()) {
			methods.interfaces.clickContinue();
			sleep(random(800, 1300));
		}
		if (methods.game.getCurrentTab() != Game.TAB_INVENTORY
				&& !methods.interfaces.get(Bank.INTERFACE_BANK).isValid()
				&& !methods.interfaces.get(Store.INTERFACE_STORE).isValid()) {
			methods.game.openTab(Game.TAB_INVENTORY);
		}
		if (col < 0 || col > 3 || row < 0 || row > 6) {
			return false;
		}
		RSItem item = getItems()[col + row * 4];
		return item != null && item.getID() != -1 && item.doAction("Drop");
	}

	/**
	 * Checks whether or not your inventory contains the provided item ID.
	 *
	 * @param itemID The item(s) you wish to evaluate.
	 * @return <tt>true</tt> if your inventory contains an item with the ID
	 *         provided; otherwise <tt>false</tt>.
	 * @see #containsOneOf(int...)
	 * @see #containsAll(int...)
	 */
	public boolean contains(final int itemID) {
		return getItem(itemID) != null;
	}

	/**
	 * Checks whether or not your inventory contains all of the provided item
	 * IDs.
	 *
	 * @param itemID The item(s) you wish to evaluate.
	 * @return <tt>true</tt> if your inventory contains at least one of all of
	 *         the item IDs provided; otherwise <tt>false</tt>.
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
	 * Checks whether or not your inventory contains at least one of the
	 * provided item IDs.
	 *
	 * @param itemID The item ID to check for.
	 * @return <tt>true</tt> if inventory contains one of the specified items;
	 *         otherwise <tt>false</tt>.
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
	 * Checks whether or not your inventory is full.
	 *
	 * @return <tt>true</tt> if your inventory contains 28 items; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean isFull() {
		return getCount() == 28;
	}

	/**
	 * Checks whether or not an inventory item is selected.
	 *
	 * @return <tt>true</tt> if an item in your inventory is selected; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean isItemSelected() {
		return getSelectedItemIndex() != -1;
	}

	/**
	 * Selects the first item in the inventory with the provided ID.
	 *
	 * @param itemID The ID of the item to select.
	 * @return <tt>true</tt> if the item was selected; otherwise <tt>false</tt>.
	 */
	public boolean selectItem(final int itemID) {
		final RSItem item = getItem(itemID);
		return item != null && selectItem(item);
	}

	/**
	 * Selects the specified item in the inventory
	 *
	 * @param item The item to select.
	 * @return <tt>true</tt> if the item was selected; otherwise <tt>false</tt>.
	 */
	public boolean selectItem(RSItem item) {
		final int itemID = item.getID();
		if (itemID == -1) {
			return false;
		}
		RSItem selItem = getSelectedItem();
		if (selItem != null && selItem.getID() == itemID) {
			return true;
		}
		if (!item.doAction("Use")) {
			return false;
		}
		for (int c = 0; c < 5 && getSelectedItem() == null; c++) {
			sleep(random(200, 300));
		}
		selItem = getSelectedItem();
		return selItem != null && selItem.getID() == itemID;
	}

	/**
	 * Uses two items together.
	 *
	 * @param item       The item to use on another item.
	 * @param targetItem The item you want the first parameter to be used on.
	 * @return <tt>true</tt> if the "use" action had been used on both items;
	 *         otherwise <tt>false</tt>.
	 */
	public boolean useItem(final RSItem item, final RSItem targetItem) {
		if (methods.game.getCurrentTab() != Game.TAB_INVENTORY) {
			methods.game.openTab(Game.TAB_INVENTORY);
		}
		return selectItem(item) && targetItem.doAction("Use");
	}

	/**
	 * Uses two items together.
	 *
	 * @param itemID   The first item ID to use.
	 * @param targetID The item ID you want the first parameter to be used on.
	 * @return <tt>true</tt> if the first item has been "used" on the other;
	 *         otherwise <tt>false</tt>.
	 */
	public boolean useItem(final int itemID, final int targetID) {
		RSItem item = getItem(itemID);
		RSItem target = getItem(targetID);
		return item != null && target != null && useItem(item, target);
	}

	/**
	 * Uses an item on an object.
	 *
	 * @param item         The item to use on another item.
	 * @param targetObject The RSObject you want the first parameter to be used on.
	 * @return <tt>true</tt> if the "use" action had been used on both the
	 *         RSItem and RSObject; otherwise <tt>false</tt>.
	 */
	public boolean useItem(RSItem item, RSObject targetObject) {
		if (methods.game.getCurrentTab() != Game.TAB_INVENTORY) {
			methods.game.openTab(Game.TAB_INVENTORY);
		}
		return selectItem(item) && targetObject.doAction("Use", targetObject.getName());
	}

	/**
	 * Uses an item on an object.
	 *
	 * @param itemID The item ID to use on the object.
	 * @param object The RSObject you want the item to be used on.
	 * @return <tt>true</tt> if the "use" action had been used on both the
	 *         RSItem and RSObject; otherwise <tt>false</tt>.
	 */
	public boolean useItem(final int itemID, final RSObject object) {
		RSItem item = getItem(itemID);
		return item != null && useItem(item, object);
	}

	/**
	 * Randomizes a point.
	 *
	 * @param inventoryPoint The inventory point to be randomized.
	 * @return A randomized <tt>Point</tt> from the center of the given
	 *         <tt>Point</tt>.
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
		if (getInterface().getWidget().getGroupIndex() == InventoryInterfaceId.INVENTORY.getIndex()) {
			String name = new RSItem(methods, getInterface().getWidget().getWidgetItems()[getSelectedItemIndex()]).getName();
			return !isItemSelected() || name == null ? null : name.replaceAll(
					"<[\\w\\d]+=[\\w\\d]+>", "");
		}
		else {
			String name = getInterface().getWidget().getComponents()[getSelectedItemIndex()].getName();
			return !isItemSelected() || name == null ? null : name.replaceAll(
					"<[\\w\\d]+=[\\w\\d]+>", "");
		}
	}

	/**
	 * Gets the selected item index.
	 *
	 * @return The index of current selected item, or -1 if none is selected.
	 */
	public int getSelectedItemIndex() {
		if (getInterface().getWidget().getGroupIndex() == InventoryInterfaceId.INVENTORY.getIndex()) {
			RSWidgetItem[] comps = getInterface().getWidget().getWidgetItems();
			for (int i = 0; i < Math.min(28, comps.length); ++i) {
				try {
					isSelected = false;
					checkIsSelected(new RSItem(methods, comps[i]));
					if (isSelected) {
						return i;
					}
				} catch (NullPointerException e) {
					//This means the selected item is null so we return here -1;
					return -1;
				}
			}

		}
		else {
			RSWidget[] comps = getInterface().getWidget().getComponents();
			for (int i = 0; i < Math.min(28, comps.length); ++i) {
				if (comps[i].getBorderThickness() == 2) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Uses a callback to get the last drawn image and then performs the method getSelected to update the
	 * isSelectedValue
	 *
	 * @param item the item to check if is selected
	 */
	public void checkIsSelected(RSItem item) {
		Consumer<Image> imageCallback = (img) ->
		{
			// This callback is on the game thread, move to executor thread
			methods.runeLite.getInjector().getInstance(ScheduledExecutorService.class).submit(() ->
			getSelected(img, item));
		};

		methods.runeLite.getInjector().getInstance(DrawManager.class).requestNextFrameListener(imageCallback);
	}

	/**
	 * Checks the image for the item position and then checks that set of bounds for any pure white pixels
	 * which would indicate that the item is selected (No items use 255, 255, 255)
	 * If it finds any the isSelected value is updated to true
	 *
	 * @param img the client image
	 * @param item the item to check for if it is selected
	 */
	public void getSelected(Image img, RSItem item) {
		int x = item.getItem().getItemLocation().getX();
		int y = item.getItem().getItemLocation().getY();
		BufferedImage im = new BufferedImage(methods.client.getCanvasWidth(), methods.client.getCanvasHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = im.getGraphics();
		graphics.drawImage(img, 0, 0, null);
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
						isSelected = true;
						return;
					}
				}
			}
		}
	}


	/**
	 * Gets the selected inventory item.
	 *
	 * @return The current selected item, or <tt>null</tt> if none is selected.
	 */
	public RSItem getSelectedItem() {
		int index = getSelectedItemIndex();
		return index == -1 ? null : getItemAt(index);
	}

	/**
	 * Clicks selected inventory item, if it's selected.
	 *
	 * @param leftClick <tt>true</tt> for left button click, <tt>false</tt> for right
	 *                  button.
	 * @return <tt>true</tt> if item was selected, <tt>false</tt> if not.
	 */
	public boolean clickSelectedItem(final boolean leftClick) {
		RSItem item = getSelectedItem();
		return item != null && item.doClick(true);
	}

	/**
	 * Left-clicks on the selected item.
	 *
	 * @return <tt>true</tt> if item was selected, </tt>false</tt> if not.
	 * @see #clickSelectedItem(boolean)
	 */
	public boolean clickSelectedItem() {
		return clickSelectedItem(true);
	}

	/**
	 * Gets inventory item at specified index.
	 *
	 * @param index The index of inventory item.
	 * @return The item, or <tt>null</tt> if not found.
	 */
	public RSItem getItemAt(final int index) {
		if (getInterface().getWidget().getGroupIndex() == InventoryInterfaceId.INVENTORY.getIndex()) {
			RSWidgetItem comp = getInterface().getWidget().getWidgetItems()[index];
			return 0 <= index && index < 28 && comp != null ? new RSItem(methods,
					comp) : null;
		}
		RSWidget comp = getInterface().getWidget().getComponent(index);
		return 0 <= index && index < 28 && comp != null ? new RSItem(methods,
				comp) : null;
	}

	/**
	 * Gets all the items in the inventory.
	 *
	 * @return <tt>RSItem</tt> array of the current inventory items or new
	 *         <tt>RSItem[0]</tt>.
	 */
	public RSItem[] getItems() {
		RSWidget invIface = getInterface().getWidget();
		if (invIface.getGroupIndex() == InventoryInterfaceId.INVENTORY.getIndex()) {
			if (invIface != null) {
				RSWidgetItem[] invItems = invIface.getWidgetItems();
				RSItem[] items = new RSItem[invItems.length];
				for (int i = 0; i < invItems.length; i++) {
					items[i] = new RSItem(methods, invItems[i]);
				}
				return items;
			}
		}
		if (invIface != null) {
			RSWidget[] invItems = invIface.getComponents();
			RSItem[] items = new RSItem[invItems.length];
			for (int i = 0; i < invItems.length; i++) {
				items[i] = new RSItem(methods, invItems[i]);
			}
			return items;
		}

		return new RSItem[0];
	}

	/**
	 * Gets all the items in the inventory matching any of the provided IDs.
	 *
	 * @param ids Valid IDs.
	 * @return <tt>RSItem</tt> array of the matching inventory items.
	 */
	public RSItem[] getItems(final int... ids) {
		LinkedList<RSItem> items = new LinkedList<RSItem>();
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
	 * Gets all the items in the inventory. If the tab is not currently open, it
	 * does not open it and returns the last known array of items in the tab.
	 *
	 * @return <tt>RSItem</tt> array of the cached inventory items or new
	 *         <tt>RSItem[0]</tt>.
	 */
	public RSItem[] getCachedItems() {
		RSWidget invIface = getInterface().getWidget();
		if (invIface.getGroupIndex() == InventoryInterfaceId.INVENTORY.getIndex()) {
			if (invIface != null) {
				RSWidgetItem[] invItems = invIface.getWidgetItems();
				RSItem[] items = new RSItem[invItems.length];
				for (int i = 0; i < invItems.length; i++) {
					items[i] = new RSItem(methods, invItems[i]);
				}
				return items;
			}
		}
		if (invIface != null) {
			RSWidget[] invItems = invIface.getComponents();
			RSItem[] items = new RSItem[invItems.length];
			for (int i = 0; i < invItems.length; i++) {
				items[i] = new RSItem(methods, invItems[i]);
			}
			return items;
		}


		return new RSItem[0];
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
	 * Gets the first item in the inventory with any of the provided IDs.
	 *
	 * @param ids The IDs of the item to find.
	 * @return The first <tt>RSItem</tt> for the given IDs; otherwise null.
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
	 * provided IDs.
	 *
	 * @param includeStacks <tt>true</tt> to count the stack sizes of each item;
	 *                      <tt>false</tt> to count a stack as a single item.
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
	 * specified IDs.
	 *
	 * @param includeStacks <tt>true</tt> to count the stack sizes of each item;
	 *                      <tt>false</tt> to count a stack as a single item.
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
	 * @param includeStacks <tt>false</tt> if stacked items should be counted as a single
	 *                      item; otherwise <tt>true</tt>.
	 * @return The count.
	 */
	public int getCount(final boolean includeStacks) {
		int count = 0;
		RSItem[] items = getItems();
		for (RSItem item : items) {
			int iid = item.getID();
			if (iid != -1) {
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
