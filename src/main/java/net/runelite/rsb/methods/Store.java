package net.runelite.rsb.methods;

import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.wrappers.*;

import javax.annotation.Nullable;
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
	 * @return <code>true</code> on success
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
	 * @return <code>true</code> if the interface is no longer open
	 */
	public boolean close() {
		if (!isOpen()) {
			return true;
		}

		if (methods.interfaces.getComponent(GlobalWidgetInfo.STORE_DYNAMIC_COMPONENTS)
				.getDynamicComponent(WidgetIndices.DynamicComponents.Global.DYNAMIC_CLOSE_BUTTON).doClick()) {
			sleep(random(500, 600));
			return !isOpen();
		} else {
			return false;
		}
	}

	/**
	 * Gets the store interface.
	 *
	 * @return the store <code>RSWidget</code>
	 */
	public RSWidget getInterface() {
		return methods.interfaces.get(WidgetIndices.Store.GROUP_INDEX);
	}

	/**
	 * Gets the item at a given component index.
	 *
	 * @param index The index of the component based off of the components in the
	 *              Store interface.
	 * @return <code>RSWidget</code> for the item at the given index; otherwise
	 * null.
	 */
	@Nullable
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
	 * @return The <code>RSWidget</code> of the item; otherwise null.
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
	 * @return An <code>RSWidget</code> array representing all of the components
	 * in the stores <code>RSWidget</code>.
	 */
	public RSItem[] getItems() {
		RSWidget storeInterface = getInterface();
		if ((storeInterface == null)
				|| (storeInterface.getComponent(WidgetIndices.Store.ITEMS_DYNAMIC_CONTAINER) == null)) {
			return null;
		}

		ArrayList<RSItem> items = new ArrayList<>();
		RSWidget[] components = storeInterface.getComponent(
				WidgetIndices.Store.ITEMS_DYNAMIC_CONTAINER).getComponents();

		for (RSWidget component : components) {

			if (component != null && component.getId() != -1) {
				items.add(new RSItem(methods, component));
			}
		}

		return items.toArray(new RSItem[0]);
	}

	/**
	 * Returns whether the store interface is open.
	 *
	 * @return <code>true</code> if the store interface is open, otherwise
	 * <code>false</code>.
	 */
	public boolean isOpen() {
		return getInterface().isValid();
	}

}
