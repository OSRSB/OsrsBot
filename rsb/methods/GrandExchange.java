package net.runelite.client.rsb.methods;

import net.runelite.api.widgets.WidgetInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Obtains information on tradeable items from the Grand Exchange website and
 * Grand Exchange ingame interaction.
 *
 * @author Aion, Boolean, Debauchery
 */
public class GrandExchange extends MethodProvider {


	public static final int INTERFACE_GRAND_EXCHANGE_WINDOW = WidgetInfo.GRAND_EXCHANGE_WINDOW_CONTAINER.getGroupId();
	public static final int INTERFACE_GRAND_EXCHANGE_SELL_INVENTORY = 0;
	public static final int INTERFACE_BUY_SEARCH_BOX = 389;

	public static final int GRAND_EXCHANGE_BUY_BUTTON = 3;
	public static final int GRAND_EXCHANGE_SELL_BUTTON = 4;

	public static final int[] GRAND_EXCHANGE_OFFER_BOXES = {7, 8, 9, 10, 11, 12, 13, 14};
	public static final int GRAND_EXCHANGE_COLLECT_BOX_ONE = 209;
	public static final int GRAND_EXCHANGE_COLLECT_BOX_TWO = 211;

	public static final int[] GRAND_EXCHANGE_CLERK = {6528, 6529};

	GrandExchange(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Checks if Grand Exchange is open.
	 *
	 * @return True if it's open, otherwise false.
	 */
	public boolean isOpen() {
		return methods.interfaces.get(INTERFACE_GRAND_EXCHANGE_WINDOW)
				.isValid() && methods.interfaces.get(INTERFACE_GRAND_EXCHANGE_WINDOW).isVisible();
	}

	/**
	 * Opens Grand Exchange window.
	 *
	 * @return True if it's open, otherwise false.
	 */
	public boolean open() {
		if (!isOpen()) {
			return methods.npcs.getNearest(GRAND_EXCHANGE_CLERK).doAction("Exchange");
		}
		return true;
	}

	/**
	 * Checks Grand Exchange slots for an any activity (1-6)
	 *
	 * @param slot An int for the corresponding slot.
	 * @return <tt>True</tt> if the slot is free from activity.
	 */
	public boolean checkSlotIsEmpty(int slot) {
		try {
			int slotComponent = GRAND_EXCHANGE_OFFER_BOXES[slot];
			if (isOpen()) {
				if (methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW, slotComponent).getComponent(
						10).containsText("Empty")) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * Checks Grand Exchange slot and returns ID
	 *
	 * @param slot The slot to check
	 * @return The item name as a string equal to the item being sold/brought
	 *         Will return null if no items are being sold.
	 */
	public String checkSlot(int slot) {
		try {
			int slotComponent = GRAND_EXCHANGE_OFFER_BOXES[slot];
			if (isOpen()) {
				if (methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW, slotComponent).getComponent(
						10).isValid()) {
					return methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW, slotComponent).getComponent(
							10).getText();
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Checks Grand Exchange slots for an item.
	 *
	 * @param name The name of the item to check for.
	 * @return An int of the corresponding slot.
	 *         0 = Not found.
	 */
	public int findItem(String name) {
		for (int i = 1; i <= 6; i++) {
			if (isOpen()) {
				if (checkSlotIsEmpty(i)) {
					int slotComponent = GRAND_EXCHANGE_OFFER_BOXES[i];
					String s = methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW,
							slotComponent).getComponent(18).getText();
					if (s.equals(name)) {
						return i;
					}
				}
			}
		}
		return 0;
	}

	/**
	 * Finds first empty slot.
	 *
	 * @return An int of the corresponding slot.
	 *         0 = No empty slots.
	 */
	public int freeSlot() {
		for (int i = 1; i <= 6; i++) {
			if (checkSlotIsEmpty(i)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Will check a slot for to see if an item has completed.
	 *
	 * @param slot The slot to check.
	 * @return <tt>true</tt> if Complete, otherwise <tt>false</tt>
	 */
	public boolean checkCompleted(int slot) {
		if (!checkSlotIsEmpty(slot)) {
			if (slot != 0) {
				int slotComponent = GRAND_EXCHANGE_OFFER_BOXES[slot];
				if (methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW, slotComponent).containsAction(
						"Abort Offer")) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets any items that may be in the offer.
	 * TODO; Add a collect from bank.
	 *
	 * @param slot An int for the corresponding slot, of which to check
	 */
	public void getItem(int slot) {
		if (isOpen()) {
			open();
		}
		if (isOpen()) {
			int slotComponent = GRAND_EXCHANGE_OFFER_BOXES[slot];
			methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW, slotComponent).containsAction(
					"Veiw Offer");
			sleep(random(700, 1200));
			if (methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW,
					GRAND_EXCHANGE_COLLECT_BOX_TWO).containsAction("Collect")) {
				methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW,
						GRAND_EXCHANGE_COLLECT_BOX_TWO).doAction("Collect");
				sleep(random(400, 900));
			}
			if (methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW,
					GRAND_EXCHANGE_COLLECT_BOX_ONE).containsAction("Collect")) {
				methods.interfaces.getComponent(INTERFACE_GRAND_EXCHANGE_WINDOW,
						GRAND_EXCHANGE_COLLECT_BOX_ONE).doAction("Collect");
				sleep(random(400, 900));
			}
		}
	}
}