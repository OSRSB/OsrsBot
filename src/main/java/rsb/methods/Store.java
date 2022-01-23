package rsb.methods;

import rsb.internal.globval.GlobalWidgetId;
import rsb.internal.globval.GlobalWidgetInfo;
import rsb.wrappers.*;

import java.util.ArrayList;

/**
 * Store related operations.
 */
public class Store extends MethodProvider {

	Store(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Tries to buy an item. 0 is All. 1, 5 and 10 use buy 1/5/10 while the
	 * other numbers use buy x.
	 *
	 * @param itemID The id of the item.
	 * @param count  The number to buy.
	 * @return <tt>true</tt> on success
	 */
	public boolean buy(final int itemID, final int count) {
		if (count < 0) {
			return false;
		}
		if (!isOpen()) {
			return false;
		}
		final int inventoryCount = methods.inventory.getCount(true);
		RSItem item = getItem(itemID);
		if (item != null) {
			if (count >= 500) {
				if (item.doAction("Buy 500")) {
					sleep(random(500, 700));
					return buy(itemID, (count - 500));
				} else {
					return false;
				}
			} else if (count >= 50) {
				if (item.doAction("Buy 50")) {
					sleep(random(500, 700));
					return buy(itemID, (count - 50));
				} else {
					return false;
				}
			} else if (count >= 10) {
				if (item.doAction("Buy 10")) {
					sleep(random(500, 700));
					return buy(itemID, (count - 10));
				} else {
					return false;
				}
			} else if (count >= 5) {
				if (item.doAction("Buy 5")) {
					sleep(random(500, 700));
					return buy(itemID, (count - 5));
				} else {
					return false;
				}
			} else if (count >= 1) {
				if (item.doAction("Buy 1")) {
					sleep(random(500, 700));
					return buy(itemID, (count - 1));
				} else {
					return false;
				}
			} else {
				return methods.inventory.getCount(true) > inventoryCount;
			}
		}
		return false;
	}

	/**
	 * Closes the store interface.
	 *
	 * @return <tt>true</tt> if the interface is no longer open
	 */
	public boolean close() {
		if (!isOpen()) {
			return true;
		}

		if (methods.interfaces.getComponent(GlobalWidgetInfo.STORE_DYNAMIC_COMPONENTS).getDynamicComponent(GlobalWidgetId.DYNAMIC_CLOSE_BUTTON).doClick()) {
			sleep(random(500, 600));
			return !isOpen();
		} else {
			return false;
		}
	}

	/**
	 * Gets the store interface.
	 *
	 * @return the store <tt>RSWidget</tt>
	 */
	public RSWidget getInterface() {
		return methods.interfaces.get(GlobalWidgetId.INTERFACE_STORE);
	}

	/**
	 * Gets the item at a given component index.
	 *
	 * @param index The index of the component based off of the components in the
	 *              Store interface.
	 * @return <tt>RSWidget</tt> for the item at the given index; otherwise
	 *         null.
	 */
	public RSItem getItemAt(final int index) {
		final RSItem[] items = getItems();
		if (items != null) {
			for (final RSItem item : items) {
				if (item.getComponent().getIndex() == index) {
					return item;
				}
			}
		}

		return null;
	}

	/**
	 * Gets the first item found with the given id.
	 *
	 * @param id ID of the item to get
	 * @return The <tt>RSWidget</tt> of the item; otherwise null.
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
	 * Gets all the items in the store inventory.
	 *
	 * @return An <tt>RSWidget</tt> array representing all of the components
	 *         in the stores <tt>RSWidget</tt>.
	 */
	public RSItem[] getItems() {
		RSWidget storeInterface = getInterface();
		if ((storeInterface == null)
				|| (storeInterface.getComponent(GlobalWidgetId.INTERFACE_STORE_ITEMS) == null)) {
			return null;
		}

		ArrayList<RSItem> items = new ArrayList<>();
		RSWidget[] components = storeInterface.getComponent(
				GlobalWidgetId.INTERFACE_STORE_ITEMS).getComponents();

		for (RSWidget component : components) {

			if (component != null && component.getId() != -1) {
				items.add(new RSItem(methods, component));
			}
		}

		return items.toArray(new RSItem[0]);
	}

	/**
	 * Returns whether or not the store interface is open.
	 *
	 * @return <tt>true</tt> if the store interface is open, otherwise
	 *         <tt>false</tt>.
	 */
	public boolean isOpen() {
		return getInterface().isValid();
	}

}
