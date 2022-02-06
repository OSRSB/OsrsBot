package rsb.methods;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ObjectID;
import net.runelite.api.widgets.WidgetInfo;
import rsb.internal.wrappers.Filter;
import rsb.internal.globval.GlobalSettingValues;
import rsb.internal.globval.GlobalWidgetId;
import rsb.internal.globval.GlobalWidgetInfo;
import rsb.wrappers.*;

import java.lang.Integer;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

@Slf4j
/**
 * Bank related operations.
 */
public class Bank extends MethodProvider {
	public static int[] BANKERS;
	public static int[] BANK_DEPOSIT_BOX;
	public static int[] BANK_CHESTS;
	public static int[] BANK_BOOTHS;

	public static Point[] UNREACHABLE_BANKERS = {
			new Point(3191, 3445), new Point(3180, 3433) // VARROCK EAST
	};

	Bank(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Assigns the ID constants for all the banking objects in RuneScape
	 */
	public void assignConstants() {
		ArrayList<Integer> bankers = new ArrayList<Integer>();
		ArrayList<Integer> bankBooths = new ArrayList<Integer>();
		ArrayList<Integer> bankChests = new ArrayList<Integer>();
		ArrayList<Integer> bankDepositBox = new ArrayList<Integer>();
		ObjectID ObjectIDs = new ObjectID();
		try {
			for (Field i : ObjectIDs.getClass().getDeclaredFields()) {
				i.setAccessible(true);
				if (i.getName().contains("BANK_BOOTH")) {
					bankBooths.add(Integer.valueOf(i.get(ObjectIDs).toString()));
				} else if (i.getName().contains("BANK_DEPOSIT_BOX")) {
					bankDepositBox.add(Integer.valueOf(i.get(ObjectIDs).toString()));
				} else if (i.getName().contains("BANK_CHEST")) {
					i.get(ObjectIDs);
					bankChests.add(Integer.valueOf(i.get(ObjectIDs).toString()));
				}
			}

			for (Field i : net.runelite.api.NpcID.class.getDeclaredFields()) {
				if (i.getName().contains("BANKER")) {
					bankers.add(Integer.valueOf(i.get(ObjectIDs).toString()));
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		BANKERS = toIntArray(bankers);
		BANK_BOOTHS = toIntArray(bankBooths);
		BANK_CHESTS = toIntArray(bankChests);
		BANK_DEPOSIT_BOX = toIntArray(bankDepositBox);

	}

	private static int[] toIntArray(ArrayList<Integer> list){
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
			ret[i] = list.get(i);
		return ret;
	}

	/**
	 * Closes the bank interface. Supports deposit boxes.
	 *
	 * @return <tt>true</tt> if the bank interface is no longer open.
	 */
	public boolean close() {
		if (isOpen()) {
			methods.interfaces.getComponent(GlobalWidgetInfo.BANK_DYNAMIC_COMPONENTS).getDynamicComponent(GlobalWidgetId.DYNAMIC_CLOSE_BUTTON).doClick();
			sleep(random(500, 600));
			return !isOpen();
		}
		if (isDepositOpen()) {
			methods.interfaces.getComponent(GlobalWidgetInfo.DEPOSIT_DYNAMIC_COMPONENTS).getDynamicComponent(GlobalWidgetId.DYNAMIC_CLOSE_BUTTON).doClick();
			sleep(random(500, 600));
			return !isDepositOpen();
		}
		return false;
	}


	public int getAvailableBankSpace (){
		if (isOpen()) {
			return Integer.parseInt(methods.interfaces.getComponent(GlobalWidgetInfo.BANK_ITEM_MAX).getText()) -
					Integer.parseInt(methods.interfaces.getComponent(GlobalWidgetInfo.BANK_ITEM_COUNT).getText());
		}
		return -1;
	}

	/**
	 * If bank is open, deposits specified amount of an item into the bank.
	 * Supports deposit boxes.
	 *
	 * @param itemID The ID of the item.
	 * @param number The amount to deposit. 0 deposits All. 1,5,10 deposit
	 *               corresponding amount while other numbers deposit X.
	 * @return <tt>true</tt> if successful; otherwise <tt>false</tt>.
	 */
	public boolean deposit(int itemID, int number) {
		if (isOpen() || isDepositOpen()) {
			if (number < 0) {
				throw new IllegalArgumentException("number < 0 (" + number + ")");
			}
			RSWidget item = null;
			int itemCount = 0;
			int invCount = isOpen() ? methods.inventory.getCount(true) : getBoxCount();
			if (!isOpen()) {
				boolean match = false;
				for (int i = 0; i < 28; i++) {
					RSWidget comp = methods.interfaces.getComponent(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER).getDynamicComponent(i);
					if (comp.getId() == itemID) {
						itemCount += comp.getStackSize();
						if (!match) {
							item = comp;
							match = true;
						}
					}
					if (itemCount > 1) {
						break;
					}
				}
			} else {
				item = methods.inventory.getItem(itemID).getComponent();
				itemCount = methods.inventory.getCount(true, itemID);
			}
			if (item == null) {
				return true;
			}
			switch (number) {
				case 0:
					item.doAction(itemCount > 1 ? "Deposit-All" : "Deposit-1");
					break;
				case 1:
					item.doClick();
					break;
				case 5:
					item.doAction("Deposit-" + number);
					break;
				default:
					if (!item.doAction("Deposit-" + number)) {
						if (item.doAction("Deposit-X")) {
							sleep(random(1000, 1300));
							methods.inputManager.sendKeys(String.valueOf(number), true);
						}
					}
					break;
			}
			sleep(300);
			int cInvCount = isOpen() ? methods.inventory.getCount(true) : getBoxCount();
			return cInvCount < invCount || cInvCount == 0;
		}
		return false;
	}

	/**
	 * Deposits all items in methods.inventory. Supports deposit boxes.
	 *
	 * @return <tt>true</tt> on success.
	 */
	public boolean depositAll() {
		if (isOpen()) {
			return methods.interfaces.getComponent(GlobalWidgetInfo.BANK_BUTTON_DEPOSIT_CARRIED_ITEMS).doClick();
		}
		return isDepositOpen() && methods.interfaces.getComponent(GlobalWidgetInfo.DEPOSIT_BUTTON_DEPOSIT_CARRIED_ITEMS).doClick();
	}

	/**
	 * Deposits all items in inventory except for the given IDs. Supports
	 * deposit boxes.
	 *
	 * @param items The items not to deposit.
	 * @return true on success.
	 */
	public boolean depositAllExcept(int... items) {
		if (isOpen() || isDepositOpen()) {
			boolean deposit = true;
			int invCount = isOpen() ? methods.inventory.getCount(true) : getBoxCount();
			outer:
			for (int i = 0; i < 28; i++) {
				RSWidget item = isOpen() ? methods.inventory.getItemAt(i).getComponent() :
						methods.interfaces.getComponent(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER).getDynamicComponent(i);
				if (item != null && item.getId() != -1) {
					for (int id : items) {
						if (item.getId() == id) {
							continue outer;
						}
					}
					for (int tries = 0; tries < 5; tries++) {
						deposit(item.getId(), 0);
						sleep(random(600, 900));
						int cInvCount = isOpen() ? methods.inventory.getCount(true) : getBoxCount();
						if (cInvCount < invCount) {
							invCount = cInvCount;
							continue outer;
						}
					}
					deposit = false;
				}
			}
			return deposit;
		}
		return false;
	}

	/**
	 * Deposit everything your player has equipped. Supports deposit boxes.
	 *
	 * @return <tt>true</tt> on success.
	 * @since 6 March 2009.
	 */
	public boolean depositAllEquipped() {
		if (isOpen()) {
			return methods.interfaces.getComponent(GlobalWidgetInfo.BANK_BUTTON_DEPOSIT_WORN_ITEMS).doClick();
		}
		return isDepositOpen() && methods.interfaces.getComponent(GlobalWidgetInfo.DEPOSIT_BUTTON_DEPOSIT_WORN_ITEMS).doClick();
	}

	/**
	 * Returns the sum of the count of the given items in the bank.
	 *
	 * @param items The array of items.
	 * @return The sum of the stacks of the items.
	 */
	public int getCount(final int... items) {
		int itemCount = 0;
		final RSItem[] inventoryArray = getItems();
		for (RSItem item : inventoryArray) {
			for (final int id : items) {
				if (item.getID() == id) {
					itemCount += item.getStackSize();
				}
			}
		}
		return itemCount;
	}

	/**
	 * Get current tab open in the bank.
	 *
	 * @return int of tab (0-8), or -1 if none are selected (bank is not open).
	 */
	public int getCurrentTab() {
		for (RSWidget widget : methods.interfaces.getComponent(GlobalWidgetInfo.BANK_TAB).getComponents()) {
			if (widget.getSpriteId() == GlobalSettingValues.SPRITE_SELECTED_VALUE) {
				return widget.getIndex();
			}
		}
		return -1;
	}

	/**
	 * Gets a tab
	 * @param index		Gets the bank tab at the specified index
	 * @return 			The bank tab
	 */
	public RSWidget getTab(int index) {
		return methods.interfaces.getComponent(GlobalWidgetInfo.BANK_TAB).getComponent(index);
	}

	/**
	 * Gets the deposit box interface.
	 *
	 * @return The deposit box <code>RSWidget</code>.
	 */
	public RSWidget getBoxInterface() {
		return methods.interfaces.get(GlobalWidgetId.INTERFACE_DEPOSIT_BOX);
	}

	/**
	 * Gets the <code>RSWidget</code> of the given item at the specified index.
	 *
	 * @param index The index of the item.
	 * @return <code>RSWidget</code> if item is found at index; otherwise null.
	 */
	public RSItem getItemAt(final int index) {
		final RSItem[] items = getItems();
		if (items != null) {
			for (final RSItem item : items) {
				if (WidgetInfo.TO_CHILD(item.getComponent().getId()) == index) {
					return item;
				}
			}
		}

		return null;
	}

	/**
	 * Gets the bank interface.
	 *
	 * @return The bank <code>RSWidget</code>.
	 */
	public RSWidget getInterface() {
		return methods.interfaces.get(GlobalWidgetId.INTERFACE_BANK);
	}

	/**
	 * Gets the first item with the provided ID in the bank.
	 *
	 * @param id ID of the item to get.
	 * @return The component of the item; otherwise null.
	 */
	public RSItem getItem(final int id) {
		final RSItem[] items = getItems();
		if (items != null) {
			for (final RSItem item : items) {
				if (item.getID() == id) {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the point on the screen for a given item. Numbered left to right then top to bottom.
	 *
	 * @param slot The index of the item.
	 * @return The point of the item or new Point(-1, -1) if null.
	 */
	public Point getItemPoint(final int slot) {
		if (slot < 0) {
			throw new IllegalArgumentException("slot < 0 " + slot);
		}
		final RSItem item = getItemAt(slot);
		if (item != null) {
			return item.getComponent().getLocation();
		}
		return new Point(-1, -1);
	}

	/**
	 * Gets all the items in the bank's inventory.
	 *
	 * @return an <code>RSItem</code> array of the bank's inventory interface.
	 */
	public RSItem[] getItems() {
		RSWidget bankInterface = getInterface();
		if ((bankInterface == null) || (bankInterface.getComponent(GlobalWidgetId.INTERFACE_BANK_INVENTORY) == null)) {
			return new RSItem[0];
		}
		RSWidget[] components = bankInterface.getComponent(GlobalWidgetId.INTERFACE_BANK_INVENTORY).getComponents();
		RSItem[] items = new RSItem[components.length];
		for (int i = 0; i < items.length; ++i) {
			items[i] = new RSItem(methods, components[i]);
		}
		return items;
	}

	/**
	 * Checks whether or not the bank is open.
	 *
	 * @return <tt>true</tt> if the bank interface is open; otherwise <tt>false</tt>.
	 */
	public boolean isOpen() {
		RSWidget bankInterface = getInterface();
		return bankInterface.isValid() && bankInterface.isVisible();
	}

	/**
	 * Checks whether or not the deposit box is open.
	 *
	 * @return <tt>true</tt> if the deposit box interface is open; otherwise <tt>false</tt>.
	 */
	public boolean isDepositOpen() {
		return methods.interfaces.get(GlobalWidgetId.INTERFACE_DEPOSIT_BOX).isValid();
	}

	private static class ReachableBankerFilter implements Filter<RSNPC> {
		@Override
		public boolean test(RSNPC npc) {
			final int id = npc.getID();
			final RSTile location = npc.getLocation();
			for (int banker : BANKERS) {
				if (banker == id) {
					for (Point unreachableBanker : UNREACHABLE_BANKERS) {
						if (unreachableBanker.equals(location)) {
							return false;
						}
					}
					return true;
				}
			}
			return false;
		}
	}

	public Object getNearest() {
		RSObject bankBooth = methods.objects.getNearest(BANK_BOOTHS);
		RSNPC banker = methods.npcs.getNearest(new ReachableBankerFilter());
		RSObject bankChest = methods.objects.getNearest(BANK_CHESTS);
		/* Find closest one, others are set to null. Remember distance and tile. */
		int lowestDist = Integer.MAX_VALUE;
		RSTile tile = null;
		if (bankBooth != null) {
			tile = bankBooth.getLocation();
			lowestDist = methods.calc.distanceTo(tile);
		}
		if (banker != null && methods.calc.distanceTo(banker) < lowestDist) {
			tile = banker.getLocation();
			lowestDist = methods.calc.distanceTo(tile);
			bankBooth = null;
		}
		if (bankChest != null && methods.calc.distanceTo(bankChest) < lowestDist) {
			bankBooth = null;
			banker = null;
		}
		return (bankChest == null) ? (banker == null) ? bankBooth : banker : bankChest;
	}


	/**
	 * Opens one of the supported banker NPCs, booths, or chests nearby. If they
	 * are not nearby, and they are not null, it will automatically walk to the
	 * closest one.
	 *
	 * @return <tt>true</tt> if the bank was opened; otherwise <tt>false</tt>.
	 */
	public boolean open() {
		if (isOpen()) {
			return true;
		}
		try {
			if (methods.menu.isOpen()) {
				methods.mouse.moveSlightly();
				sleep(random(20, 30));
			}
			RSObject bankBooth = methods.objects.getNearest(BANK_BOOTHS);
			RSNPC banker = methods.npcs.getNearest(new ReachableBankerFilter());
			RSObject bankChest = methods.objects.getNearest(BANK_CHESTS);
			/* Find closest one, others are set to null. Remember distance and tile. */
			int lowestDist = Integer.MAX_VALUE;
			RSTile tile = null;
			if (bankBooth != null) {
				tile = bankBooth.getLocation();
				lowestDist = methods.calc.distanceTo(tile);
			}
			if (banker != null && methods.calc.distanceTo(banker) < lowestDist) {
				tile = banker.getLocation();
				lowestDist = methods.calc.distanceTo(tile);
				bankBooth = null;
			}
			if (bankChest != null && methods.calc.distanceTo(bankChest) < lowestDist) {
				tile = bankChest.getLocation();
				lowestDist = methods.calc.distanceTo(tile);
				bankBooth = null;
				banker = null;
			}
			/* Open closest one, if any found */
			if (lowestDist < 5 && methods.calc.tileOnMap(tile) && methods.calc.canReach(tile, true)) {
				boolean didAction = false;
				if (bankBooth != null) {
					didAction = bankBooth.doAction("Use-Quickly");
				} else if (banker != null) {
					didAction = banker.doAction("Bank", "Banker");
				} else if (bankChest != null) {
					didAction = bankChest.doAction("Bank") || methods.menu.doAction("Use");
				}
				if (didAction) {
					int count = 0;
					while (!isOpen() && ++count < 10) {
						sleep(random(200, 400));
						if (methods.players.getMyPlayer().isLocalPlayerMoving()) {
							count = 0;
						}


					}
				} else {
					methods.camera.turnTo(tile);
				}
			} else if (tile != null) {
				methods.walking.walkTileMM(tile);
			}
			return isOpen();
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Opens one of the supported deposit boxes nearby. If they are not nearby, and they are not null,
	 * it will automatically walk to the closest one.
	 *
	 * @return <tt>true</tt> if the deposit box was opened; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean openDepositBox() {
		try {
			if (!isDepositOpen()) {
				if (methods.menu.isOpen()) {
					methods.mouse.moveSlightly();
					sleep(random(20, 30));
				}
				RSObject depositBox = methods.objects.getNearest(
						 BANK_DEPOSIT_BOX);
				if (depositBox != null && methods.calc.distanceTo(depositBox) < 8 && methods.calc.tileOnMap(
						depositBox.getLocation()) && methods.calc.canReach(
						depositBox.getLocation(), true)) {
					if (depositBox.doAction("Deposit")) {
						int count = 0;
						while (!isDepositOpen() && ++count < 10) {
							sleep(random(200, 400));

							if (methods.players.getMyPlayer().isLocalPlayerMoving()) {
								count = 0;
							}


						}
					} else {
						methods.camera.turnTo(depositBox, 20);
					}
				} else {
					if (depositBox != null) {
						methods.walking.walkTo(depositBox.getLocation());
					}
				}
			}
			return isDepositOpen();
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Opens the bank tab.
	 *
	 * @param tabNumber The tab number - e.g. view all is 1.
	 * @return <tt>true</tt> on success.
	 */
	public boolean openTab(final int tabNumber) {

		return isOpen() && getTab(tabNumber).doClick();
	}

	/**
	 * @return <tt>true</tt> if currently searching the bank.
	 */
	public boolean isSearchOpen() {
		// Setting 1248 is -2147483648 when search is enabled and -2013265920
		return (methods.settings.getSetting(1248) == -2147483648);
	}

	/**
	 * Searches for an item in the bank. Returns true if succeeded (does not
	 * necessarily mean it was found).
	 *
	 * @param itemName The item name to find.
	 * @return <tt>true</tt> on success.
	 */
	public boolean searchItem(final String itemName) {
		if (!isOpen()) {
			return false;
		}
		methods.interfaces.getComponent(GlobalWidgetInfo.BANK_BUTTON_SEARCH).doAction("Search");
		sleep(random(1000, 1500));
		if (!isSearchOpen()) {
			sleep(500);
		}
		if (isOpen() && isSearchOpen()) {
			methods.inputManager.sendKeys(itemName, false);
			sleep(random(300, 700));
			return true;
		}
		return false;
	}

	/**
	 * Sets the bank rearrange mode to insert.
	 *
	 * @return <tt>true</tt> on success.
	 */
	public boolean setRearrangeModeToInsert() {
		if (!isOpen()) {
			return false;
		}
		if (methods.settings.getSetting(GlobalSettingValues.SETTING_BANK_TOGGLE_REARRANGE_MODE) != 1) {
			methods.interfaces.getComponent(GlobalWidgetInfo.BANK_BUTTON_INSERT).doClick();
			sleep(random(500, 700));
		}
		return methods.settings.getSetting(GlobalSettingValues.SETTING_BANK_TOGGLE_REARRANGE_MODE) == 1;
	}

	/**
	 * Sets the bank rearrange mode to swap.
	 *
	 * @return <tt>true</tt> on success.
	 */
	public boolean setRearrangeModeToSwap() {
		if (!isOpen()) {
			return false;
		}
		if (methods.settings.getSetting(
				GlobalSettingValues.SETTING_BANK_TOGGLE_REARRANGE_MODE) != 0) {
			methods.interfaces.getComponent(GlobalWidgetInfo.BANK_BUTTON_SWAP).doClick();
			sleep(random(500, 700));
		}
		return methods.settings.getSetting(
				GlobalSettingValues.SETTING_BANK_TOGGLE_REARRANGE_MODE) == 0;
	}

	/**
	 * Sets the bank withdraw mode to item.
	 *
	 * @return <tt>true</tt> on success.
	 */
	public boolean setWithdrawModeToItem() {
		if (!isOpen()) {
			return false;
		}
		if (methods.settings.getSetting(
				GlobalSettingValues.SETTING_BANK_TOGGLE_WITHDRAW_MODE) != 0) {
			methods.interfaces.getComponent(GlobalWidgetInfo.BANK_BUTTON_ITEM).doClick();
			sleep(random(500, 700));
		}
		return methods.settings.getSetting(
				GlobalSettingValues.SETTING_BANK_TOGGLE_WITHDRAW_MODE) == 0;
	}

	/**
	 * Sets the bank withdraw mode to note.
	 *
	 * @return <tt>true</tt> on success.
	 */
	public boolean setWithdrawModeToNote() {
		if (!isOpen()) {
			return false;
		}
		if (methods.settings.getSetting(
				GlobalSettingValues.SETTING_BANK_TOGGLE_WITHDRAW_MODE) != 1) {
			methods.interfaces.getComponent(GlobalWidgetInfo.BANK_BUTTON_NOTE).doClick();
			sleep(random(500, 700));
		}
		return methods.settings.getSetting(
				GlobalSettingValues.SETTING_BANK_TOGGLE_WITHDRAW_MODE) == 1;
	}

	/**
	 * Tries to withdraw an item.
	 * 0 is All. 1,5,10 use Withdraw 1,5,10 while other numbers Withdraw X.
	 *
	 * @param itemID The ID of the item.
	 * @param count  The number to withdraw.
	 * @return <tt>true</tt> on success.
	 */
	public boolean withdraw(final int itemID, final int count) {
		if (!isOpen()) {
			return false;
		}
		if (count < 0) {
			throw new IllegalArgumentException("count (" + count + ") < 0");
		}
		RSItem rsi = getItem(itemID);
		if (rsi == null) {
			return false;
		}
		RSWidget item = rsi.getComponent();
		if (item == null) {
			return false;
		}
		while (item.getRelativeX() == 0 && methods.bank.getCurrentTab() != 0) {
			if (getTab(0).doClick()) {
				sleep(random(800, 1300));
			}
		}
		if (!methods.interfaces.scrollTo(item, methods.interfaces.getComponent(GlobalWidgetInfo.BANK_SCROLLBAR))) {
			return false;
		}
		int invCount = methods.inventory.getCount(true);
		//item.doClick(count == 1 ? true : false);
		String defaultAction = "Withdraw-" + count;
		String action = null;
		switch (count) {
			case 0:
				action = "Withdraw-All";
				break;
			case 1:
				break;
			case 5:
				action = defaultAction;
				break;
			case 10:
				action = defaultAction;
				break;
			default:
				int i = -1;
				try {
					i = Integer.parseInt(item.getActions()[4].toLowerCase().trim().replaceAll("\\D", ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (i == count) {
					action = defaultAction;
				}
				else if (item.doAction("Withdraw-X")) {
					sleep(random(1000, 1300));
					methods.keyboard.sendText(String.valueOf(count), true);
				}
		}
		if (action != null && item.doAction(action)) {
			sleep(random(1000, 1300));
		}
		int newInvCount = methods.inventory.getCount(true);
		return newInvCount > invCount || newInvCount == 28;
	}

	/**
	 * Gets the count of all the items in the inventory with the any of the
	 * specified IDs while deposit box is open.
	 *
	 * @param ids the item IDs to include
	 * @return The count.
	 */
	public int getBoxCount(int... ids) {
		if (!isDepositOpen()) {
			return -1;
		}
		int count = 0;
		for (int i = 0; i < 28; ++i) {
			for (int id : ids) {
				if (methods.interfaces.getComponent(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER).isValid()
						&& methods.interfaces.getComponent(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER).getComponent(i).getId() == id) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Gets the count of all items in your inventory ignoring stack sizes while
	 * deposit box is open.
	 *
	 * @return The count.
	 */
	public int getBoxCount() {
		if (!isDepositOpen()) {
			return -1;
		}
		int count = 0;
		for (int i = 0; i < 28; i++) {
			if (methods.interfaces.get(11).getComponent(17).isValid() && methods.interfaces.get(11).getComponent(
					17).getComponent(i).getId() != -1) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Gets the equipment items from the bank interface.
	 *
	 * @return All equipment items that are being worn.
	 */
	public RSItem[] getEquipmentItems() {
		if (methods.interfaces.getComponent(WidgetInfo.EQUIPMENT_INVENTORY_ITEMS_CONTAINER).isValid()) {
			return new RSItem[0];
		}
		RSWidget[] components = methods.interfaces.getComponent(WidgetInfo.EQUIPMENT_INVENTORY_ITEMS_CONTAINER).getComponents();
		RSItem[] items = new RSItem[components.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new RSItem(methods, components[i]);
		}
		return items;
	}

	/**
	 * Gets a equipment item from the bank interface.
	 *
	 * @param id ID of the item.
	 * @return RSItem
	 */
	public RSItem getEquipmentItem(final int id) {
		RSItem[] items = getEquipmentItems();
		if (items != null) {
			for (final RSItem item : items) {
				if (item.getID() == id) {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the ID of a equipment item based on name.
	 *
	 * @param name Name of the item.
	 * @return -1 if item is not found.
	 */
	public int getEquipmentItemID(final String name) {
		RSItem[] items = getEquipmentItems();
		if (items != null) {
			for (final RSItem item : items) {
				if (item.getName().contains(name)) {
					return item.getID();
				}
			}
		}
		return -1;
	}


	/**
	 * Gets the item ID of a item side the bank.
	 *
	 * @param name Name of the item.
	 * @return -1 if item is not found.
	 */
	public int getItemID(final String name) {
		RSItem[] items = getItems();
		if (items != null) {
			for (final RSItem item : items) {
				if (item.getName().toLowerCase().equals(name.toLowerCase())) {
					return item.getID();
				}
			}
			for (final RSItem item : items) {
				if (item.getName().toLowerCase().contains(name.toLowerCase())) {
					return item.getID();
				}
			}
		}
		return -1;
	}

}