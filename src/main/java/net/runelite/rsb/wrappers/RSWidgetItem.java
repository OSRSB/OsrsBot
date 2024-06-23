package net.runelite.rsb.wrappers;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.methods.MethodProvider;
import net.runelite.rsb.wrappers.common.ClickBox;
import net.runelite.rsb.wrappers.common.Clickable07;

import java.awt.*;


public class RSWidgetItem extends MethodProvider implements Clickable07 {

    private final WidgetItem item;
    private final Widget parent;

    private final ClickBox clickBox = new ClickBox(this);

    RSWidgetItem(MethodContext ctx, WidgetItem item) {
        super(ctx);
        this.item = item;
        this.parent = item.getWidget();
    }

    public boolean isValid() {
        return item != null;
    }
    /**
     * Performs the given action on this RSInterfaceChild if it is
     * showing (valid).
     *
     * @param action The menu action to click.
     * @return <code>true</code> if the action was clicked; otherwise <code>false</code>.
     */
    public boolean doAction(final String action) {
        return doAction(action, null);
    }

    /**
     * Performs the given action on this RSInterfaceChild if it is
     * showing (valid).
     *
     * @param action The menu action to click.
     * @param option The option of the menu action to click.
     * @return <code>true</code> if the action was clicked; otherwise <code>false</code>.
     */
    public boolean doAction(final String action, final String option) {
        return getClickBox().doAction(action, option);
    }

    /**
     * Left-clicks this component.
     *
     * @return <code>true</code> if the component was clicked.
     */
    public boolean doClick() {
        return doClick(true);
    }

    /**
     * Clicks this component.
     *
     * @param leftClick <code>true</code> to left-click; <code>false</code>
     *                  to right-click.
     * @return <code>true</code> if the component was clicked.
     */
    public boolean doClick(boolean leftClick) {
        return getClickBox().doClick(leftClick);
    }

    /**
     * Moves the mouse over this component (with normally distributed randomness)
     * if it is not already.
     *
     * @return <code>true</code> if the mouse was moved; otherwise <code>false</code>.
     */
    public boolean doHover() {
        return getClickBox().doHover();
    }

    public boolean isClickable() {
        return isValid() && !parent.isHidden() && !parent.isSelfHidden();
    }

    public Shape getClickShape() {
        return getArea();
    }

    @Override
    public ClickBox getClickBox() {
        return clickBox;
    }

    /**
     * Gets the canvas area of the item
     *
     * @return the canvas area
     */
    public Rectangle getArea() {
        return item.getCanvasBounds();
    }

    /**
     * Gets the item id of this item
     *
     * @return item id
     */
    public int getItemId() {
        return item.getId();
    }

    /**
     * Gets the stack size of the item
     *
     * @return the stack size
     */
    public int getStackSize() {
        return item.getQuantity();
    }

    public net.runelite.api.Point getItemLocation() {
        return item.getCanvasLocation();
    }

}
