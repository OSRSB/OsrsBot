package net.runelite.client.rsb.wrappers;


import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.methods.MethodProvider;
import net.runelite.client.rsb.methods.Web;
import net.runelite.client.rsb.wrappers.common.Clickable07;
import net.runelite.client.rsb.wrappers.subwrap.RSMenuNode;

import java.util.concurrent.Callable;
import java.util.function.Predicate;

/**
 * Represents an item (with an id and stack size). May or may not
 * wrap a component.
 */
public class RSItem extends MethodProvider implements Clickable07 {

	private final int id;
	private final int stack;
	private RSWidget component;
	private RSWidgetItem item;

	public RSItem(final MethodContext ctx, final RSWidgetItem item) {
		super(ctx);
		this.id = item.getItemId();
		this.stack = item.getStackSize();
		this.item = item;
	}

	public RSItem(final MethodContext ctx, final TileItem item) {
		super(ctx);
		id = item.getId();
		stack = item.getQuantity();
		//This is only used for ground objects and thus does not need component declared
	}


	public RSItem(final MethodContext ctx, final RSWidget item) {
		super(ctx);
		id = item.getItemId();
		stack = item.getStackSize();
		component = item;
	}

	/**
	 * Gets this item's definition if available.
	 *
	 * @return The RSItemDef; or <code>null</code> if unavailable.
	 */
	public ItemComposition getDefinition() {
		return methods.client.getItemDefinition(id);
	}

	/**
	 * Gets this item's id.
	 *
	 * @return The id.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Gets this item's stack size.
	 *
	 * @return The stack size.
	 */
	public int getStackSize() {
		return stack;
	}

	/**
	 * Returns whether or not this item has an available definition.
	 *
	 * @return <tt>true</tt> if an item definition is available;
	 *         otherwise <tt>false</tt>.
	 */
	public boolean hasDefinition() {
		return getDefinition() != null;
	}

	/**Gets the item wrapped by this RSItem
	 *
	 * @return The wrapped item or <code>null</code>
	 */
	public RSWidgetItem getItem() {
		return item;
	}

	/**
	 * Gets the component wrapped by this RSItem.
	 *
	 * @return The wrapped component or <code>null</code>.
	 */
	public RSWidget getComponent() {
		return component;
	}

	/**
	 * Checks whether or not a valid component is being wrapped.
	 *
	 * @return <tt>true</tt> if there is a visible wrapped component.
	 */
	public boolean isComponentValid() {
		return component != null && component.isVisible();
	}

	/**
	 * Checks whether or not a valid item is being wrapped.
	 *
	 * @Return <tt>true</tt> if there is a visible wrapped item
	 */
	public boolean isItemValid() {
		return item.isValid();
	}


	/**
	 * Gets the name of this item using the wrapped component's name
	 * if available, otherwise the definition if available.
	 *
	 * @return The item's name or <code>null</code> if not found.
	 */
	public String getName() {
		if (item == null) {
			return null;
		}
		if (component != null && component.getName() != null) {
			return component.getName();
		} else {
			ItemComposition definition = getDefinition();
			if (definition != null) {
				return definition.getName();
			}
		}
		return null;
	}

	/**
	 * Provides a list of inventory actions for the given item.
	 * @return The list of inventory actions for the item
	 */
	public String[] getInventoryActions() {
		if (item == null) {
			return null;
		}
		ItemComposition definition = getDefinition();
		if (definition != null) {
			return definition.getInventoryActions();
		}
		return null;
	}

	public String[] getGroundActions() {
		if (item == null) {
			return null;
		}
		ItemComposition definition = getDefinition();
		if (definition != null) {
			//Recreate method def.getGroundItems
			return null;
		}
		return null;
	}


	/**
	 * Performs the given action on the component wrapped by
	 * this RSItem if possible.
	 *
	 * @param action The action to perform.
	 * @return <tt>true</tt> if the component was clicked
	 *         successfully; otherwise <tt>false</tt>.
	 */
	public boolean doAction(final String action) {
		return doAction(action, null);
	}

	/**
	 * Performs the given action on the component wrapped by
	 * this RSItem if possible.
	 *
	 * @param action The action to perform.
	 * @param option The option of the action to perform.
	 * @return <tt>true</tt> if the component was clicked
	 *         successfully; otherwise <tt>false</tt>.
	 */
	public boolean doAction(final String action, final String option) {
		return (component != null) ?  component.doAction(action, option) : item.doAction(action, option);
	}

	public boolean doAction(Predicate<RSMenuNode> predicate) {
		component.doClick(false);
		for (RSMenuNode menuNode : Web.methods.chooseOption.getMenuNodes()) {
			if (predicate.test(menuNode)) {
				return (component != null) ? component.doAction(menuNode.getAction(), menuNode.getTarget()) : item.doAction(menuNode.getAction(), menuNode.getTarget());
			}
		}
		return false;
	}

	/**
	 * Clicks the component wrapped by this RSItem if possible.
	 *
	 * @param left <tt>true</tt> if the component should be
	 *             left-click; <tt>false</tt> if it should be right-clicked.
	 * @return <tt>true</tt> if the component was clicked
	 *         successfully; otherwise <tt>false</tt>.
	 */
	public boolean doClick(boolean left) {
		return (component != null) ? component.doClick(left) : item.doClick(left);
	}

	public boolean doClick() {
		return (component != null) ? component.doClick(true) : item.doClick(true);
	}


	public boolean doHover() {
		return (component != null) && component.doHover();
	}


	public boolean isClickable() {
		return component.isValid() && component.isVisible() && component.isSelfVisible();
	}
}
