package net.runelite.rsb.methods;

import net.runelite.api.SpriteID;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.wrappers.RSItem;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.Arrays;
import java.util.Optional;

/**
 * Obtains information on tradeable items from the Grand Exchange website and
 * Grand Exchange ingame interaction.
 *
 * @author GigiaJ
 */
public class GrandExchange extends MethodProvider {

	GrandExchange(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Checks if Grand Exchange is open.
	 *
	 * @return True if it's open, otherwise false.
	 */
	public boolean isOpen() {
		RSWidget widget = methods.interfaces.get(WidgetIndices.GrandExchange.PARENT_CONTAINER);
		return widget.isValid() && widget.isVisible();
	}

	/**
	 * Opens Grand Exchange window.
	 *
	 * @return True if it's open, otherwise false.
	 */
	public boolean open() {
		if (!isOpen()) {
			//Makes sure we randomly choose which way we open the interface
			boolean openClerk = random(0, 10) > 3;
			if (openClerk)
				return methods.npcs.getNearest("Grand Exchange Clerk").doAction("Exchange");
			else
				return methods.objects.getNearest("Grand Exchange booth").doAction("Exchange");
		}
		return true;
	}

	public boolean close() {
		if (isOpen()) {
			return methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_INTERFACE_LAYOUT)
					.getDynamicComponent(WidgetIndices.DynamicComponents.Global.DYNAMIC_CLOSE_BUTTON).doClick();
		}
		return false;
	}

	/**
	 * Checks Grand Exchange slots for an any activity (1-8)
	 *
	 * @param slot An int for the corresponding slot.
	 * @return <code>True</code> if the slot is free from activity.
	 */
	public boolean checkSlotIsEmpty(int slot) {
		try {
			int slotComponent = mapSlotToSlotIndex(slot);
			if (isOpen()) {
				return methods.interfaces.getComponent(WidgetIndices.GrandExchange.PARENT_CONTAINER, slotComponent)
						.getDynamicComponent(WidgetIndices.GrandExchange.OFFER_STATUS_ITEM_DESCRIPTION_LABEL).containsText("Empty");
			} else {
				return false;
			}
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * Checks if the Grand Exchange slot is locked or not
	 *
	 * @param slot An int for the corresponding slot.
	 * @return <code>True</code> if the user is a member else <code>false</code> for slots 4-8
	 */
	public boolean checkSlotLocked(int slot) {
		int slotComponent = mapSlotToSlotIndex(slot);
		if (isOpen()) {
			RSWidget geWidget = methods.interfaces.getComponent(WidgetIndices.GrandExchange.PARENT_CONTAINER, slotComponent);
			boolean isBuyHovered = geWidget.getDynamicComponent(WidgetIndices.DynamicComponents.GrandExchangeSlot.BUY_ICON_SPRITE).getSpriteId() == SpriteID.GE_MAKE_OFFER_BUY_HOVERED;
			boolean isSellHovered = geWidget.getDynamicComponent(WidgetIndices.DynamicComponents.GrandExchangeSlot.SELL_ICON_SPRITE).getSpriteId() == SpriteID.GE_MAKE_OFFER_SELL_HOVERED;
			//Obviously both can't be hovered at the same time so it must be locked
			if (isBuyHovered && isSellHovered) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks Grand Exchange slot and returns name
	 *
	 * @param slot The slot to check
	 * @return The item name as a string equal to the item being sold/brought
	 * Will return null if no items are being sold.
	 */
	public String checkSlot(int slot) {
		int slotComponent = mapSlotToSlotIndex(slot);
		if (isOpen()) {
			RSWidget nameWidget = methods.interfaces.getComponent(WidgetIndices.GrandExchange.PARENT_CONTAINER, slotComponent).getDynamicComponent(
					WidgetIndices.DynamicComponents.GrandExchangeSlot.ITEM_NAME_LABEL);
			if (nameWidget.isValid() && nameWidget.isVisible()) {
				return nameWidget.getText();
			}
		}
		return null;
	}

	/**
	 * Checks Grand Exchange slots for an item.
	 *
	 * @param name The name of the item to check for.
	 * @return An int of the corresponding slot.
	 * 0 = Not found.
	 */
	public int findItem(String name) {
		final int NUMBER_OF_SLOTS = 8;
		for (int i = 1; i <= NUMBER_OF_SLOTS; i++) {
			if (isOpen()) {
				if (checkSlotIsEmpty(i) && !checkSlotLocked(i)) {
					int slotComponent = mapSlotToSlotIndex(i);
					String s = methods.interfaces.getComponent(WidgetIndices.GrandExchange.PARENT_CONTAINER,
							slotComponent).getDynamicComponent(WidgetIndices.DynamicComponents.GrandExchangeSlot.ITEM_NAME_LABEL).getText();
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
	 * 0 = No empty slots.
	 */
	public int freeSlot() {
		final int NUMBER_OF_SLOTS = 8;
		for (int i = 1; i <= NUMBER_OF_SLOTS; i++) {
			if (checkSlotIsEmpty(i) && !checkSlotLocked(i)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Will check a slot for to see if an item has completed.
	 *
	 * @param slot The slot to check.
	 * @return <code>true</code> if Complete, otherwise <code>false</code>
	 */
	public boolean checkCompleted(int slot) {
		if (!checkSlotIsEmpty(slot) && !checkSlotLocked(slot)) {
			if (slot != 0) {
				int slotComponent = mapSlotToSlotIndex(slot);
				RSWidget cmp = methods.interfaces.getComponent(WidgetIndices.GrandExchange.PARENT_CONTAINER, slotComponent);
				if (cmp.getActions() != null && cmp.containsAction(
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
	 * A method to collect items from a slot as note or as the item itself
	 *
	 * @param asNote whether the item should be withdrawn as a note
	 */
	public void collect(boolean asNote) {
		String collectAs = "Collect";
		if (asNote) {
			collectAs += "-note";
		} else {
			collectAs += "-item";
		}
		RSWidget geWidget = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_COLLECTION_AREA);
		collectFromSlot(geWidget, collectAs);
		collectAs += "s";
		collectFromSlot(geWidget, collectAs);
	}

	/**
	 * A method to collect all items to inventory or the bank
	 *
	 * @param toBank whether the item should be collect to the bank
	 */
	public void collectAll(boolean toBank) {
		String collectAs = "Collect";
		if (!toBank) {
			collectAs += " to inventory";
		} else {
			collectAs += " to bank";
		}
		RSWidget geWidget = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_TITLE);
		geWidget = geWidget.getDynamicComponent(0);
		geWidget.doAction(collectAs);
		sleep(random(400, 900));
	}

	/**
	 * Internal-use. Collects attempts to collect from both slots if the widgets contain the action passed
	 *
	 * @param widget    The widget to check to collect from
	 * @param collectAs The string to check for in the widget
	 */
	private void collectFromSlot(RSWidget widget, String collectAs) {
		if (widget.getComponent(WidgetIndices.DynamicComponents.GrandExchangeCollectionArea.RIGHT_ITEM_SPRITE).containsAction(collectAs)) {
			methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_COLLECT_AREA_ONE).doAction(collectAs);
			sleep(random(400, 900));
		}
		if (widget.getComponent(WidgetIndices.DynamicComponents.GrandExchangeCollectionArea.LEFT_ITEM_SPRITE).containsAction(collectAs)) {
			methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_COLLECT_AREA_TWO).doAction(collectAs);
			sleep(random(400, 900));
		}
	}

	/**
	 * Gets any item that is in the offer as the item and to the inventory
	 *
	 * @param slot An int for the corresponding slot, of which to check
	 */
	public void collectItem(int slot) {
		collectItem(slot, false, false);
	}

	/**
	 * Gets any item that is in the offer as the item and in the form designated
	 *
	 * @param slot   An int for the corresponding slot, of which to check
	 * @param asNote whether the item should be withdrawn as a note
	 */
	public void collectItem(int slot, boolean asNote) {
		collectItem(slot, false, asNote);
	}

	/**
	 * Gets any items that may be in the offer.
	 *
	 * @param slot   An int for the corresponding slot, of which to check
	 * @param toBank whether the item should be sent to the bank
	 * @param asNote whether the item should be withdrawn as a note
	 */
	public void collectItem(int slot, boolean toBank, boolean asNote) {
		if (!isOpen()) {
			open();
		}
		if (isOpen()) {
			if (toBank) {
				methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_TITLE)
						.getDynamicComponent(WidgetIndices.DynamicComponents.GrandExchangeOfferTittle.BUTTON_COLLECT_SPRITE)
						.doAction("Collect to bank");
				return;
			}
			int slotComponent = mapSlotToSlotIndex(slot);
			if (!checkSlotIsEmpty(slot) && !checkSlotLocked(slot)) {
				RSWidget geWidget = methods.interfaces.get(WidgetIndices.GrandExchange.PARENT_CONTAINER);
				if (geWidget.getComponent(slotComponent).isValid() && !geWidget.getComponent(slotComponent).isVisible()) {
					geWidget.getComponent(slotComponent).doAction("View Offer");
					sleep(random(700, 1200));
				}
				if (geWidget.getComponent(WidgetIndices.GrandExchange.PARENT_CONTAINER).isValid() &&
						geWidget.getComponent(WidgetIndices.GrandExchange.PARENT_CONTAINER).isVisible()) {
					collect(asNote);
				} else {
					collectItem(slot, false, asNote);
				}
			}
		}
	}

	/**
	 * Gets the current visible offer quantity
	 *
	 * @return the current offer quantity
	 */
	private int getCurrentOfferQuantity() {
		//Dynamic child in offer container
		final int OFFER_QUANTITY = 32;
		RSWidget offerWindow = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_OFFER_WINDOW);
		if (offerWindow.isValid() && offerWindow.isVisible()) {
			return Integer.parseInt(offerWindow.getDynamicComponent(OFFER_QUANTITY).getText());
		}
		return -1;
	}

	/**
	 * Gets the current visible offer price per item
	 *
	 * @return the current offer price per item
	 */
	private int getCurrentOfferPrice() {
		//Dynamic child in offer container
		final int OFFER_PRICE_PER_ITEM = 39;
		RSWidget offerWindow = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_OFFER_WINDOW);
		if (offerWindow.isValid() && offerWindow.isVisible()) {
			return Integer.parseInt(offerWindow.getDynamicComponent(OFFER_PRICE_PER_ITEM).getText().replaceAll("coin", "").replaceAll("s", "").trim());
		}
		return -1;
	}

	/**
	 * Creates an offer based on the parameters given
	 *
	 * @param quantity    the number of items to list for
	 * @param priceChange the number of times to press the price per item +5% or -5% buttons
	 * @return <code>true</code> if the offer was attempted to be made; otherwise <code>false</code>
	 */
	public boolean createOffer(int quantity, int priceChange) {
		//Dynamic child of offer window
		final int OFFER_CONFIRM = 54;

		RSWidget offerWindow = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_OFFER_WINDOW);

		//Decides what order to do these in and gives an off number, so we are inconsistent in a weird manner
		boolean randomlyInput = (random(0, 10) < 4);
		if (randomlyInput) {
			setPrice(priceChange);
			setQuantity(quantity);
		} else {
			setQuantity(quantity);
			setPrice(priceChange);
		}
		return offerWindow.getDynamicComponent(OFFER_CONFIRM).doClick();
	}

	/**
	 * Decides how many times to press the respective button to set the price per item (-5% or +5%)
	 *
	 * @param priceChange the number of button presses
	 */
	public void setPrice(int priceChange) {
		//Dynamic child of offer window
		final int OFFER_DOWN = 10;
		final int OFFER_UP = 13;
		RSWidget offerWindow = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_OFFER_WINDOW);
		if (priceChange == 0) {
			return;
		}
		if (offerWindow.isValid() && offerWindow.isVisible()) {
			int buttonToPress = (priceChange < 0) ? OFFER_DOWN : OFFER_UP;
			for (int i = 0; i < Math.abs(priceChange); i++) {
				offerWindow.getDynamicComponent(buttonToPress).doClick();
				sleep(random(30, 100));
			}
		}
	}

	/**
	 * Returns the number of clicks required to set an offer quantity with the +1, +10, +100, +1000 buttons
	 *
	 * @param quantity the quantity you would like to offer
	 * @return the number of clicks to set that quantity or Integer.MAX_VALUE if not possible
	 */
	public int getNumberClick(int quantity) {
		// Clicking +1 when you open an offer window makes quantity 2 but +10 makes quantity 10 not 11
		if (quantity < 10) {
			return quantity - 1;
		}
		if (quantity >= 10000) {
			return Integer.MAX_VALUE;
		}
		int requiredClicks = 0;
		for (int i = 3; i >= 0; i--) {
			int subtract = nth(quantity, i);
			if (subtract != 0) {
				requiredClicks += nth(quantity, i);
				continue;
			}
		}
		return requiredClicks;
	}

	/**
	 * Sets the quantity of items in the offer interface randomizing how it goes about doing so too
	 *
	 * @param quantity the quantity to list
	 */
	public void setQuantity(int quantity) {
		// OFFER_1 = 45, OFFER_10 = 46, OFFER_100 = 47, OFFER_1000 = 48
		final int[] offerButtons = {45, 46, 47, 48};
		final int OFFER_ENTER_AMOUNT = 49;

		RSWidget offerWindow = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_OFFER_WINDOW);

		/*
		Picks a random number to decide how many clicks to allow for (to throw off pattern detection)
		Then proceeds to check the quantity against it to decide whether to click the buttons or to simply
		Type in our amount.
		 */

		int randomClicks = random(3, 6);
		int requiredClicks = getNumberClick(quantity);

		if (requiredClicks > randomClicks) {
			offerWindow.getDynamicComponent(OFFER_ENTER_AMOUNT).doClick();
			sleep(random(20, 100));
			RSWidget chatbox = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_SEARCH_INPUT);
			if (chatbox.isValid() && chatbox.isVisible()) {
				methods.keyboard.sendText(String.valueOf(quantity), true);
			}
		} else {
			for (int i = 0; i < requiredClicks; i++) {
				for (int j = 3; j >= 0; j--) {
					int mostSignificantDecimal = nth(quantity, j);
					if (mostSignificantDecimal != 0) {
						// j now represents the power of the most significant decimal
						if (offerWindow.getDynamicComponent(offerButtons[j]).doClick()) {
							sleep(random(20, 50));
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Checks if the user has coins in their inventory
	 *
	 * @return <code>True</code> unless the user does not have coins
	 */
	private boolean hasCoins() {
		return methods.inventory.getItemID("Coins") != -1;
	}

	/**
	 * Buys an item from the grand exchange
	 *
	 * @param id            the id of the item
	 * @param quantity      the number of items to buy
	 * @param priceChange   the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean buy(int id, int quantity, int priceChange, boolean roundQuantity) {
		return buy(methods.client.getItemDefinition(id).getName(), quantity, priceChange, roundQuantity);
	}

	/**
	 * Buys an item from the grand exchange
	 *
	 * @param name          the name of the item
	 * @param quantity      the number of items to buy
	 * @param priceChange   the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean buy(String name, int quantity, int priceChange, boolean roundQuantity) {
		if (name == null || quantity <= 0) {
			return false;
		}
		if (!hasCoins()) {
			return false;
		}
		if (!isOpen()) {
			open();
			sleep(random(50, 250));
		}
		if (isOpen()) {
			RSWidget buyButton = methods.interfaces.getComponent(WidgetIndices.GrandExchange.PARENT_CONTAINER,
					mapSlotToSlotIndex(freeSlot())).getDynamicComponent(WidgetIndices.DynamicComponents.GrandExchangeSlot.BUY_ICON_SPRITE);
			if (buyButton.isValid() && buyButton.isVisible()) {
				buyButton.doAction("Create");
				if (roundQuantity) {
					quantity = (quantity * random(85, 120)) / 100;
				}
				sleep(random(200, 400));
				RSWidget chatbox = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_SEARCH_INPUT);
				if (chatbox.isValid() && chatbox.isVisible()) {
					methods.keyboard.sendText(name, false);
					sleep(2000);
					RSWidget[] items = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_SEARCH_DYNAMIC_CONTAINER).getComponents();
					Optional<RSWidget> widgetOptional = Arrays.stream(items).filter((x) -> Menu.stripFormatting(x.getName()).equals(name)).findFirst();
					if (widgetOptional.isPresent()) {
						if (widgetOptional.get().doAction("Select")) {
							sleep(2000);
						}
						;
					}
				}
				sleep(random(80, 600));
				if (Arrays.stream(methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_OFFER_WINDOW).getComponents())
						.anyMatch((x) -> x.getText().equals(name))) {
					return createOffer(quantity, priceChange);
				}
			}
		}
		return false;
	}

	/**
	 * Buys an item from the grand exchange
	 *
	 * @param name        the name of the item
	 * @param quantity    the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean buyItem(String name, int quantity, int priceChange) {
		return buy(name, quantity, priceChange, false);
	}

	/**
	 * Buys an item from the grand exchange
	 *
	 * @param name          the name of the item
	 * @param quantity      the number of items to buy
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean buyItem(String name, int quantity, boolean roundQuantity) {
		return buy(name, quantity, 0, roundQuantity);
	}

	/**
	 * Buys an item from the grand exchange
	 *
	 * @param name          the name of the item
	 * @param quantity      the number of items to buy
	 * @param priceChange   the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean buyItem(String name, int quantity, int priceChange, boolean roundQuantity) {
		return buy(name, quantity, priceChange, roundQuantity);
	}

	/**
	 * Buys an item from the grand exchange
	 *
	 * @param id          the id of the item
	 * @param quantity    the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean buyItem(int id, int quantity, int priceChange) {
		return buy(id, quantity, priceChange, false);
	}

	/**
	 * Buys an item from the grand exchange
	 *
	 * @param id            the id of the item
	 * @param quantity      the number of items to buy
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean buyItem(int id, int quantity, boolean roundQuantity) {
		return buy(id, quantity, 0, roundQuantity);
	}

	/**
	 * Buys an item from the grand exchange
	 *
	 * @param id            the id of the item
	 * @param quantity      the number of items to buy
	 * @param priceChange   the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean buyItem(int id, int quantity, int priceChange, boolean roundQuantity) {
		return buy(id, quantity, priceChange, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param id            the id of the item
	 * @param quantity      the number of items to buy
	 * @param priceChange   the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sell(int id, int quantity, int priceChange, boolean roundQuantity) {
		return sell(methods.client.getItemDefinition(id).getName(), quantity, priceChange, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param name          the name of the item
	 * @param quantity      the number of items to buy
	 * @param priceChange   the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sell(String name, int quantity, int priceChange, boolean roundQuantity) {
		if (name == null || quantity <= 0) {
			return false;
		}
		RSItem items = methods.inventory.getItem(methods.inventory.getItemID(name));
		if (items == null) {
			return false;
		}
		if (!isOpen()) {
			open();
			sleep(random(50, 250));
		}
		boolean sellFromInventory = (random(0, 10) > 4);
		if (isOpen()) {
			RSWidget sellButton = methods.interfaces.getComponent(WidgetIndices.GrandExchange.PARENT_CONTAINER,
					mapSlotToSlotIndex(freeSlot())).getDynamicComponent(WidgetIndices.DynamicComponents.GrandExchangeSlot.SELL_ICON_SPRITE);
			if (sellButton.isValid() && sellButton.isVisible() && (items.getComponent() != null && items.getComponent().isVisible())) {
				if (sellFromInventory) {
					items.doAction("Offer");
				} else {
					sellButton.doAction("Create");
				}
				if (roundQuantity) {
					quantity = (quantity * random(85, 120)) / 100;
				}
				sleep(random(20, 300));
				items.doAction("Offer");
				sleep(random(200, 400));
				return createOffer(quantity, priceChange);
			}
		}
		return false;
	}

	public int mapSlotToSlotIndex(int slot) {
		return switch (slot) {
			case 1 -> WidgetIndices.GrandExchange.FIRST_SLOT_DYNAMIC_CONTAINER;
			case 2 -> WidgetIndices.GrandExchange.SECOND_SLOT_DYNAMIC_CONTAINER;
			case 3 -> WidgetIndices.GrandExchange.THIRD_SLOT_DYNAMIC_CONTAINER;
			case 4 -> WidgetIndices.GrandExchange.FOURTH_SLOT_DYNAMIC_CONTAINER;
			case 5 -> WidgetIndices.GrandExchange.FIFTH_SLOT_DYNAMIC_CONTAINER;
			case 6 -> WidgetIndices.GrandExchange.SIXTH_SLOT_DYNAMIC_CONTAINER;
			case 7 -> WidgetIndices.GrandExchange.SEVENTH_SLOT_DYNAMIC_CONTAINER;
			case 8 -> WidgetIndices.GrandExchange.EIGHT_SLOT_DYNAMIC_CONTAINER;
			default -> -1;
		};
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param name          the name of the item
	 * @param quantity      the number of items to buy
	 * @param priceChange   the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sellItem(String name, int quantity, int priceChange, boolean roundQuantity) {
		return sell(name, quantity, priceChange, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param name        the name of the item
	 * @param quantity    the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sellItem(String name, int quantity, int priceChange) {
		return sell(name, quantity, priceChange, false);
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param name          the name of the item
	 * @param quantity      the number of items to buy
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sellItem(String name, int quantity, boolean roundQuantity) {
		return sell(name, quantity, 0, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param name     the name of the item
	 * @param quantity the number of items to buy
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sellItem(String name, int quantity) {
		return sell(name, quantity, 0, false);
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param id            the id of the item
	 * @param quantity      the number of items to buy
	 * @param priceChange   the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sellItem(int id, int quantity, int priceChange, boolean roundQuantity) {
		return sell(id, quantity, priceChange, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param id          the id of the item
	 * @param quantity    the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sellItem(int id, int quantity, int priceChange) {
		return sell(id, quantity, priceChange, false);
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param id            the id of the item
	 * @param quantity      the number of items to buy
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sellItem(int id, int quantity, boolean roundQuantity) {
		return sell(id, quantity, 0, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 *
	 * @param id       the id of the item
	 * @param quantity the number of items to buy
	 * @return <code>True</code> unless we can't buy
	 */
	public boolean sellItem(int id, int quantity) {
		return sell(id, quantity, 0, false);
	}
}