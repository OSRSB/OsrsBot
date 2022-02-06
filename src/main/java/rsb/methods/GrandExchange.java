package rsb.methods;

import net.runelite.api.SpriteID;
import rsb.internal.globval.GlobalWidgetId;
import rsb.internal.globval.GlobalWidgetInfo;
import rsb.wrappers.RSItem;
import rsb.wrappers.RSWidget;

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
		RSWidget widget = methods.interfaces.get(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW);
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
			boolean openClerk = random(0,10) > 3;
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
					.getDynamicComponent(GlobalWidgetId.DYNAMIC_CLOSE_BUTTON).doClick();
		}
		return false;
	}

	/**
	 * Checks Grand Exchange slots for an any activity (1-8)
	 *
	 * @param slot An int for the corresponding slot.
	 * @return <tt>True</tt> if the slot is free from activity.
	 */
	public boolean checkSlotIsEmpty(int slot) {
		try {
			int slotComponent = GlobalWidgetId.GRAND_EXCHANGE_OFFER_BOXES[slot];
			if (isOpen()) {
				if (methods.interfaces.getComponent(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW, slotComponent).getDynamicComponent(
						GlobalWidgetId.GRAND_EXCHANGE_SLOT_TITLE).containsText("Empty")) {
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
	 * Checks if the Grand Exchange slot is locked or not
	 *
	 * @param slot An int for the corresponding slot.
	 * @return <tt>True</tt> if the user is a member else <tt>false</tt> for slots 4-8
	 */
	public boolean checkSlotLocked(int slot) {
			int slotComponent = GlobalWidgetId.GRAND_EXCHANGE_OFFER_BOXES[slot];
			if (isOpen()) {
				RSWidget geWidget = methods.interfaces.getComponent(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW, slotComponent);
				boolean isBuyHovered = geWidget.getDynamicComponent(GlobalWidgetId.GRAND_EXCHANGE_BUY_ICON).getSpriteId() == SpriteID.GE_MAKE_OFFER_BUY_HOVERED;
				boolean isSellHovered = geWidget.getDynamicComponent(GlobalWidgetId.GRAND_EXCHANGE_SELL_ICON).getSpriteId() == SpriteID.GE_MAKE_OFFER_SELL_HOVERED;
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
	 *         Will return null if no items are being sold.
	 */
	public String checkSlot(int slot) {
		int slotComponent = GlobalWidgetId.GRAND_EXCHANGE_OFFER_BOXES[slot];
		if (isOpen()) {
			RSWidget nameWidget = methods.interfaces.getComponent(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW, slotComponent).getDynamicComponent(
					GlobalWidgetId.GRAND_EXCHANGE_ITEM_NAME);
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
	 *         0 = Not found.
	 */
	public int findItem(String name) {
		final int NUMBER_OF_SLOTS = 8;
		for (int i = 1; i <= NUMBER_OF_SLOTS; i++) {
			if (isOpen()) {
				if (checkSlotIsEmpty(i) && !checkSlotLocked(i)) {
					int slotComponent = GlobalWidgetId.GRAND_EXCHANGE_OFFER_BOXES[i];
					String s = methods.interfaces.getComponent(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW,
							slotComponent).getDynamicComponent(GlobalWidgetId.GRAND_EXCHANGE_ITEM_NAME).getText();
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
	 * @return <tt>true</tt> if Complete, otherwise <tt>false</tt>
	 */
	public boolean checkCompleted(int slot) {
		if (!checkSlotIsEmpty(slot) && !checkSlotLocked(slot)) {
			if (slot != 0) {
				int slotComponent = GlobalWidgetId.GRAND_EXCHANGE_OFFER_BOXES[slot];
				if (methods.interfaces.getComponent(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW, slotComponent).containsAction(
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
		}
		else {
			collectAs += "-item";
		}
		RSWidget geWidget = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_COLLECTION_AREA);
		collectFromSlot(geWidget, collectAs);
		collectAs += "s";
		collectFromSlot(geWidget, collectAs);
	}

	/**
	 * Internal-use. Collects attempts to collect from both slots if the widgets contain the action passed
	 *
	 * @param widget		The widget to check to collect from
	 * @param collectAs		The string to check for in the widget
	 */
	private void collectFromSlot(RSWidget widget, String collectAs) {
		if (widget.getComponent(GlobalWidgetId.GRAND_EXCHANGE_COLLECT_BOX_ONE).containsAction(collectAs)) {
			methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_COLLECT_AREA_ONE).doAction(collectAs);
			sleep(random(400, 900));
		}
		if (widget.getComponent(GlobalWidgetId.GRAND_EXCHANGE_COLLECT_BOX_TWO).containsAction(collectAs)) {
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
	 * @param slot An int for the corresponding slot, of which to check
	 * @param asNote whether the item should be withdrawn as a note
	 */
	public void collectItem(int slot, boolean asNote) {
		collectItem(slot, false, asNote);
	}

	/**
	 * Gets any items that may be in the offer.
	 *
	 * @param slot An int for the corresponding slot, of which to check
	 * @param toBank whether the item should be sent to the bank
	 * @param asNote whether the item should be withdrawn as a note
	 */
	public void collectItem(int slot, boolean toBank, boolean asNote) {
		if (!isOpen()) {
			open();
		}
		if (isOpen()) {
			if (toBank) {
				methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_DESCRIPTION)
						.getDynamicComponent(GlobalWidgetId.GRAND_EXCHANGE_DESCRIPTION_COLLECT).doAction("Collect");
				return;
			}
			int slotComponent = GlobalWidgetId.GRAND_EXCHANGE_OFFER_BOXES[slot];
			if (!checkSlotIsEmpty(slot) && !checkSlotLocked(slot)) {
				RSWidget geWidget = methods.interfaces.get(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW);
				if (geWidget.getComponent(slotComponent).isValid() && !geWidget.getComponent(slotComponent).isVisible()) {
					geWidget.getComponent(slotComponent).doAction("Veiw Offer");
					sleep(random(700, 1200));
				}
				if (geWidget.getComponent(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW).isValid() &&
						geWidget.getComponent(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW).isVisible()) {
					collect(asNote);
				}
				else {
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
	 * @param quantity the number of items to list for
	 * @param priceChange the number of times to press the price per item +5% or -5% buttons
	 * @return <tt>true</tt> if the offer was attempted to be made; otherwise <tt>false</tt>
	 */
	public boolean createOffer(int quantity, int priceChange) {
		//Dynamic child of offer window
		final int OFFER_CONFIRM = 54;

		RSWidget offerWindow = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_OFFER_WINDOW);

		//Decides what order to do these in and gives an off number, so we are inconsistent in a weird manner
		boolean randomlyInput = (random(0,10) < 4);
		if (randomlyInput) {
			setPrice(priceChange);
			setQuantity(quantity);
		}
		else {
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
	 * Sets the quantity of items in the offer interface randomizing how it goes about doing so too
	 *
	 * @param quantity the quantity to list
	 */
	public void setQuantity(int quantity) {
		//Dynamic child of offer window
		final int OFFER_1 = 45;
		final int OFFER_10 = 46;
		final int OFFER_100 = 47;
		final int OFFER_1000_OR_ALL_BUTTON = 48;
		final int OFFER_ENTER_AMOUNT = 49;
		RSWidget offerWindow = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_OFFER_WINDOW);
		if (quantity == 1) {
			return;
		}
		/*
		Picks a random number to decide how many clicks to allow for (to throw off pattern detection)
		Then proceeds to check the quantity against it to decide whether to click the buttons or to simply
		Type in our amount.
		 */
		int randomMax = random(3,6);
		double limit = Math.log(randomMax) % 1;
		double logQuantity = (Math.log(quantity) % 1);
		if (logQuantity < limit) {
			switch ((int) (Math.log(quantity) - logQuantity)) {
				case (0):
					for (int i = 0; i < quantity; i++) {
						offerWindow.getDynamicComponent(OFFER_1).doClick();
						sleep(random(20, 50));
						}
					break;
				case(1):
					//IE is not 12,13,14 ect
					if (nth(quantity, 1) == 0) {
						for (int i = 0; i < quantity / 10; i++) {
							offerWindow.getDynamicComponent(OFFER_10).doClick();
							sleep(random(20, 50));
						}
						break;
					}
				case(2):
					//IE is not 12,13,14 ect
					if (nth(quantity, 1) == 0) {
						//IE is not 122,133,144 ect
						if (nth(quantity, 2) == 0) {
							for (int i = 0; i < quantity / 100; i++) {
								offerWindow.getDynamicComponent(OFFER_100).doClick();
								sleep(random(20, 50));
							}
							break;
						}
					}
				case(3):
					//IE is not 12,13,14 ect
					if (nth(quantity, 1) == 0) {
						//IE is not 122,133,144 ect
						if (nth(quantity, 2) == 0) {
							//IE is not 1222,1333,1444 ect
							if (nth(quantity, 3) == 0) {
								RSWidget button = offerWindow.getDynamicComponent(OFFER_1000_OR_ALL_BUTTON);
								if (button.getText() != "All") {
									for (int i = 0; i < quantity / 1000; i++) {
										button.doClick();
										sleep(random(20, 50));
									}
									break;
								}
							}
						}
					}
				default:
					offerWindow.getDynamicComponent(OFFER_ENTER_AMOUNT).doClick();
					sleep(random(20,100));
					RSWidget chatbox = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_SEARCH_INPUT);
					if (chatbox.isValid() && chatbox.isVisible()) {
						methods.keyboard.sendText(String.valueOf(quantity), true);
					}
					break;
			}
		}
	}

	/**
	 * Checks if the user has coins in their inventory
	 *
	 * @return <tt>True</tt> unless the user does not have coins
	 */
	private boolean hasCoins() {
		return methods.inventory.getItemID("Coins") != -1;
	}

	/**
	 * Buys an item from the grand exchange
	 * @param id the id of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean buy(int id, int quantity, int priceChange, boolean roundQuantity) {
		return buy(methods.client.getItemDefinition(id).getName(), quantity, priceChange, roundQuantity);
	}

	/**
	 * Buys an item from the grand exchange
	 * @param name the name of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
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
			RSWidget buyButton = methods.interfaces.getComponent(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW,
					GlobalWidgetId.GRAND_EXCHANGE_OFFER_BOXES[freeSlot()]).getDynamicComponent(GlobalWidgetId.GRAND_EXCHANGE_BUY_BUTTON);
			if (buyButton.isValid() && buyButton.isVisible()) {
				buyButton.doAction("Create");
				if (roundQuantity) {
					quantity = (quantity * random(85, 120)) / 100;
				}
				sleep(random(200,400));
				RSWidget chatbox = methods.interfaces.getComponent(GlobalWidgetInfo.GRAND_EXCHANGE_SEARCH_INPUT);
				if (chatbox.isValid() && chatbox.isVisible()) {
					methods.keyboard.sendText(name, true);
				}
				sleep(random(80,600));
				createOffer(quantity, priceChange);
			}
		}
		return false;
	}

	/**
	 * Buys an item from the grand exchange
	 * @param name the name of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean buyItem(String name, int quantity, int priceChange) {
		return buy(name, quantity, priceChange, false);
	}

	/**
	 * Buys an item from the grand exchange
	 * @param name the name of the item
	 * @param quantity the number of items to buy
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean buyItem(String name, int quantity, boolean roundQuantity) {
		return buy(name, quantity, 0, roundQuantity);
	}

	/**
	 * Buys an item from the grand exchange
	 * @param name the name of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean buyItem(String name, int quantity, int priceChange, boolean roundQuantity) {
		return buy(name, quantity, priceChange, roundQuantity);
	}

	/**
	 * Buys an item from the grand exchange
	 * @param id the id of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean buyItem(int id, int quantity, int priceChange) {
		return buy(id, quantity, priceChange, false);
	}

	/**
	 * Buys an item from the grand exchange
	 * @param id the id of the item
	 * @param quantity the number of items to buy
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean buyItem(int id, int quantity, boolean roundQuantity) {
		return buy(id, quantity, 0, roundQuantity);
	}

	/**
	 * Buys an item from the grand exchange
	 * @param id the id of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean buyItem(int id, int quantity, int priceChange, boolean roundQuantity) {
		return buy(id, quantity, priceChange, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 * @param id the id of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean sell(int id, int quantity, int priceChange, boolean roundQuantity) {
		return sell(methods.client.getItemDefinition(id).getName(), quantity, priceChange, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 * @param name the name of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
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
		boolean sellFromInventory = (random(0,10) > 4);
		if (isOpen()) {
			RSWidget sellButton = methods.interfaces.getComponent(GlobalWidgetId.INTERFACE_GRAND_EXCHANGE_WINDOW, GlobalWidgetId.GRAND_EXCHANGE_OFFER_BOXES[freeSlot()])
					.getDynamicComponent(GlobalWidgetId.GRAND_EXCHANGE_SELL_BUTTON);
			if (sellButton.isValid() && sellButton.isVisible() && (items.getComponent() != null && items.getComponent().isVisible())) {
				if (sellFromInventory) {
					items.doAction("Offer");
				}
				else {
					sellButton.doAction("Create");
				}
				if (roundQuantity) {
					quantity = (quantity * random(85, 120)) / 100;
				}
				sleep(random(20,300));
				items.doAction("Offer");
				sleep(random(200, 400));
				createOffer(quantity, priceChange);
			}
		}
		return false;
	}

	/**
	 * Sells an item in the grand exchange
	 * @param name the name of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean sellItem(String name, int quantity, int priceChange, boolean roundQuantity) {
		return sell(name, quantity, priceChange, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 * @param name the name of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean sellItem(String name, int quantity, int priceChange) {
		return sell(name, quantity, priceChange, false);
	}

	/**
	 * Sells an item in the grand exchange
	 * @param name the name of the item
	 * @param quantity the number of items to buy
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean sellItem(String name, int quantity, boolean roundQuantity) {
		return sell(name, quantity, 0, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 * @param name the name of the item
	 * @param quantity the number of items to buy
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean sellItem(String name, int quantity) {
		return sell(name, quantity, 0, false);
	}

	/**
	 * Sells an item in the grand exchange
	 * @param id the id of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean sellItem(int id, int quantity, int priceChange, boolean roundQuantity) {
		return sell(id, quantity, priceChange, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 * @param id the id of the item
	 * @param quantity the number of items to buy
	 * @param priceChange the number of times to press the price change buttons (-5% or +5%)
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean sellItem(int id, int quantity, int priceChange) {
		return sell(id, quantity, priceChange, false);
	}

	/**
	 * Sells an item in the grand exchange
	 * @param id the id of the item
	 * @param quantity the number of items to buy
	 * @param roundQuantity decides whether we need exactly this amount or not
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean sellItem(int id, int quantity, boolean roundQuantity) {
		return sell(id, quantity, 0, roundQuantity);
	}

	/**
	 * Sells an item in the grand exchange
	 * @param id the id of the item
	 * @param quantity the number of items to buy
	 * @return <tt>True</tt> unless we can't buy
	 */
	public boolean sellItem(int id, int quantity) {
		return sell(id, quantity, 0, false);
	}
	
}