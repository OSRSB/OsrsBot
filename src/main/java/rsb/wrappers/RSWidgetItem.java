package rsb.wrappers;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import rsb.methods.MethodContext;
import rsb.methods.MethodProvider;

import java.awt.*;


public class RSWidgetItem extends MethodProvider {

    private WidgetItem item;
    private Widget parent;

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
     * @return <tt>true</tt> if the action was clicked; otherwise <tt>false</tt>.
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
     * @return <tt>true</tt> if the action was clicked; otherwise <tt>false</tt>.
     */
    public boolean doAction(final String action, final String option) {
        if (!isValid()) {
            return false;
        }
        Rectangle rect = getArea();
        if (rect.x == -1 || rect.y == -1 || rect.width == -1 || rect.height == -1) {
            return false;
        }
        if (!rect.contains(new Point (methods.mouse.getLocation().getX(), methods.mouse.getLocation().getY()))) {
            int min_x = rect.x + 1, min_y = rect.y + 1;
            int max_x = min_x + rect.width - 2, max_y = min_y + rect.height - 2;

            methods.mouse.move(random(min_x, max_x, rect.width / 3),
                    random(min_y, max_y, rect.height / 3));
            sleep(random(40, 80));
        }
        return methods.menu.doAction(action, option);
    }

    /**
     * Left-clicks this component.
     *
     * @return <tt>true</tt> if the component was clicked.
     */
    public boolean doClick() {
        return doClick(true);
    }

    /**
     * Clicks this component.
     *
     * @param leftClick <tt>true</tt> to left-click; <tt>false</tt>
     *                  to right-click.
     * @return <tt>true</tt> if the component was clicked.
     */
    public boolean doClick(boolean leftClick) {
        if (!isValid()) {
            return false;
        }

        Rectangle rect = getArea();
        if (rect.x == -1 || rect.y == -1 || rect.width == -1 || rect.height == -1) {
            return false;
        }
        if (rect.contains(new Point (methods.mouse.getLocation().getX(), methods.mouse.getLocation().getY()))) {
            methods.mouse.click(true);
            return true;
        }

        int min_x = rect.x + 1, min_y = rect.y + 1;
        int max_x = min_x + rect.width - 2, max_y = min_y + rect.height - 2;

        methods.mouse.click(random(min_x, max_x, rect.width / 3),
                random(min_y, max_y, rect.height / 3), leftClick);
        return true;
    }

    /**
     * Moves the mouse over this component (with normally distributed randomness)
     * if it is not already.
     *
     * @return <tt>true</tt> if the mouse was moved; otherwise <tt>false</tt>.
     */
    public boolean doHover() {
        if (!isValid()) {
            return false;
        }

        Rectangle rect = getArea();
        if (rect.x == -1 || rect.y == -1 || rect.width == -1 || rect.height == -1) {
            return false;
        }
        if (rect.contains(new Point(methods.mouse.getLocation().getX(), methods.mouse.getLocation().getY()))) {
            return false;
        }

        int min_x = rect.x + 1, min_y = rect.y + 1;
        int max_x = min_x + rect.width - 2, max_y = min_y + rect.height - 2;

        methods.mouse.move(random(min_x, max_x, rect.width / 3),
                random(min_y, max_y, rect.height / 3));
        return true;
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
     * Gets the item's index in the parent widget
     *
     * @return item index
     */
    public int getItemIndex() {
        return item.getIndex();
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
