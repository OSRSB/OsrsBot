package rsb.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import rsb.methods.MethodContext;
import rsb.methods.MethodProvider;
import rsb.wrappers.common.Clickable07;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RSWidget extends MethodProvider implements Clickable07 {

    private final int id;

    private final int parentId;

    private final Widget widget;

    private final Widget parentWidget;

    public RSWidget(final MethodContext ctx, final int parentId, final int id) {
        super(ctx);
        this.id = id;
        this.widget = ctx.client.getWidget(WidgetInfo.TO_GROUP(parentId), WidgetInfo.TO_CHILD(id));
        this.parentId = parentId;
        this.parentWidget = widget.getParent();
    }

    public RSWidget(final MethodContext ctx, final Widget widget) {
        super(ctx);
        if (widget != null) {
            this.id = widget.getId();
            this.widget = widget;
            this.parentWidget = widget.getParent();
            this.parentId = (parentWidget != null) ? parentWidget.getId() : -1;
        }
        else {
            this.id = -1;
            this.widget = null;
            this.parentWidget = null;
            this.parentId = -1;
        }
    }

    public boolean isValid() {
        return widget != null;
    }

    public boolean isVisible() {
        return isValid() && !widget.isHidden();
    }

    public boolean isSelfVisible() {return isValid() && !widget.isSelfHidden();}

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
        if (rect.contains(new Point (methods.mouse.getLocation().getX(), methods.mouse.getLocation().getY()))) {
            return false;
        }

        int min_x = rect.x + 1, min_y = rect.y + 1;
        int max_x = min_x + rect.width - 2, max_y = min_y + rect.height - 2;

        methods.mouse.move(random(min_x, max_x, rect.width / 3),
                random(min_y, max_y, rect.height / 3));
        return true;
    }

    /**
     * Gets the absolute x position of the child, calculated from
     * the beginning of the game screen
     *
     * @return the absolute x or -1 if null
     */
    public int getAbsoluteX() {
        return this.widget.getCanvasLocation().getX();
    }

    /**
     * Gets the absolute y position of the child, calculated from
     * the beginning of the game screen
     *
     * @return the absolute y position or -1 if null
     */
    public int getAbsoluteY() {
        return this.widget.getCanvasLocation().getY();
    }

    /**
     * Gets the area of this component
     *
     * @return the area or new Rectangle(-1, -1, -1, -1) if null
     */
    public Rectangle getArea() {
        return widget.getBounds();
    }

    /**
     * The child components (bank items etc) of this component.
     *
     * @return The components or RSWidget[0] if null
     */
    public RSWidget[] getComponents() {
        if (widget == null)
    {
        return null;
    }
        ArrayList<RSWidget> components = new ArrayList<>();


        Widget[] children = widget.getDynamicChildren();
        RSWidget[] childComponents;
        if (children != null) {
            childComponents = convertToRSWidget(children);
            if (childComponents != null) {
                for (RSWidget component : childComponents) {
                    RSWidget[] childNode = component.getComponents();
                    if (childNode != null) {
                        components.addAll(Arrays.asList(childNode));
                    }
                }
            }
        }
        children = widget.getStaticChildren();
        if (children != null) {
            childComponents = convertToRSWidget(children);
            if (childComponents != null)
            {
                for (RSWidget component : childComponents)
                {
                    RSWidget[] childNode = component.getComponents();
                    if (childNode != null)
                    {
                        components.addAll(Arrays.asList(childNode));
                    }
                }
            }
        }
        children = widget.getNestedChildren();
        if (children != null) {
            childComponents = convertToRSWidget(children);
            if (childComponents != null) {
                for (RSWidget component : childComponents) {
                    RSWidget[] childNode = component.getComponents();
                    if (childNode != null) {
                        components.addAll(Arrays.asList(childNode));
                    }
                }
            }
        }
        if (components.size() < 1) {
            components.add(this);
        }
        return components.toArray(new RSWidget[0]);
    }

    RSWidget[] convertToRSWidget(Widget[] widgets) {
        final RSWidget[] components = new RSWidget[widgets.length];
        for (int i = 0; i < widgets.length; i++) {
            components[i] = new RSWidget(methods, widgets[i]);
        }
        return components;
    }

    public RSWidgetItem[] getWidgetItems() {
        if (widget.getWidgetItems() != null) {
            WidgetItem[] widgetItems = widget.getWidgetItems().toArray(new WidgetItem[]{});
            RSWidgetItem[] items = new RSWidgetItem[widgetItems.length];
            for (int i = 0; i < items.length; i++) {
                items[i] = new RSWidgetItem(methods, widgetItems[i]);
            }
            return items;
        }
        return null;
    }

    public RSWidgetItem getWidgetItem(int idx) {
        return new RSWidgetItem(methods, widget.getWidgetItem(idx));
    }

    public RSWidget getDynamicComponent(int idx) {
        return new RSWidget(methods, widget.getChild(idx));
    }

    /**
     * Gets the child component at a given index
     *
     * @param idx The child index
     * @return The child component, or null
     */
    /*
    public RSWidget getComponent(int idx) {
        RSWidget[] components = getComponents();
        if (idx >= 0 && idx < components.length) {
            return components[idx];
        }
        return null;
    }
    */
    public RSWidget getComponent(int idx) {
        return new RSWidget(methods, methods.client.getWidget(WidgetInfo.TO_GROUP(this.getId()), idx));
    }

    /**
     * Gets the border thickness of this component
     *
     * @return the border thickness or -1 if null
     */
    public int getBorderThickness() {
        final Widget inter = this.widget;;
        if (inter != null) {
            return inter.getBorderType();
        }
        return -1;
    }

    /**
     * Gets the id of this component
     *
     * @return The id of this component, or -1 if component == null
     */
    public int getId() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getId();
        }
        return -1;
    }

    /**
     * Gets the index of this component
     *
     * @return The index of this component, or -1 if component == null
     */
    public int getIndex() {
        if (widget != null) {
            return id - parentId;
        }
        return -1;
    }

    /**
     * Gets the stack size of this component
     *
     * @return The stack size of this component, or -1 if component == null
     */
    public int getStackSize() {
        final Widget component = this.widget;
        if (component != null) {
            return component.getItemQuantity();
        }

        return -1;
    }

    /**
     * Gets the name of this component
     *
     * @return The name of this component, or "" if component == null
     */
    public String getName() {
        final Widget component = this.widget;
        if (component != null) {
            return component.getName();
        }

        return "";
    }

    /**
     * Gets the height of this component
     *
     * @return the height of this component or -1 if null
     */
    public int getHeight() {
        if (!isInScrollableArea()) {
            return getRealHeight();
        }

        final Widget childInterface = this.widget;
        if (childInterface != null) {
            return childInterface.getHeight() - 4;
        }
        return -1;
    }

    /**
     * Gets the sprite ID of this component
     *
     * @return the sprite ID or -1 if null
     */
    public int getSpriteId() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getSpriteId();
        }

        return -1;
    }

    /**
     * Gets the item ID of this component
     *
     * @return the item ID or -1 if null
     */
    public int getItemId() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getItemId();
        }

        return -1;
    }

    /**
     * Gets the model ID of this component
     *
     * @return the model ID or -1 if null
     */
    public int getModelId() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getModelId();
        }

        return -1;
    }

    /**
     * Gets the parent id of this component. It will first look at the internal
     * parentID, if that's -1 then it will search the RSInterfaceNC to find its
     * parent.
     *
     * @return the parentID or -1 if none
     */
    public int getParentId() {
        final Widget inter = this.widget;
        if (inter == null) {
            return -1;
        }
        return parentId;
    }

    /**
     * Gets the parent widget
     * @return  the parent widget for this RSWidget object
     */
    public RSWidget getParent() {
        return new RSWidget(methods, parentWidget.getParentId(), parentId);
    }

    /**
     * Gets the group index of this widget
     * @return the group index
     */
    public int getGroupIndex() {
        return WidgetInfo.TO_GROUP(widget.getId());
    }

    /**
     * Gets the child index of this widget
     * @return the child index
     */
    public int getChildIndex() {
        return WidgetInfo.TO_CHILD(widget.getId());
    }



    /**
     * Gets the absolute position of the child
     *
     * @return the absolute position or new Point(-1, -1) if null
     */
    public Point getLocation() {
        return new Point(getAbsoluteX(), getAbsoluteY());
    }

    /**
     * Returns the center point of this interface
     *
     * @return The center point of this interface
     */
    public Point getCenter() {
        return new Point(getAbsoluteX() + getWidth() / 2, getAbsoluteY() + getHeight() / 2);
    }

    /**
     * Gets the relative x position of the child, calculated from the beginning
     * of the interface
     *
     * @return the relative x position or -1 if null
     */
    public int getRelativeX() {
        final Widget childInterface = this.widget;
        if (childInterface != null) {
            return childInterface.getRelativeX();
        }
        return -1;
    }

    /**
     * Gets the relative y position of the child, calculated from the beginning
     * of the interface
     *
     * @return the relative y position -1 if null
     */
    public int getRelativeY() {
        final Widget childInterface = this.widget;
        if (childInterface != null) {
            return childInterface.getRelativeY();
        }
        return -1;
    }

    public int getVerticalScrollPosition() {
        final Widget childInterface = this.widget;
        if (childInterface != null) {
            return childInterface.getScrollY();
        }
        return -1;
    }

    public int getHorizontalScrollPosition() {
        final Widget childInterface =this.widget;
        if (childInterface != null) {
            return childInterface.getScrollX();
        }
        return -1;
    }

    public int getScrollableContentHeight() {
        final Widget childInterface = this.widget;
        if (childInterface != null) {
            return childInterface.getScrollHeight();
        }
        return -1;
    }

    public int getScrollableContentWidth() {
        final Widget childInterface = this.widget;
        if (childInterface != null) {
            return childInterface.getScrollWidth();
        }
        return -1;
    }

    public int getRealHeight() {
        final Widget childInterface = this.widget;
        if (childInterface != null) {
            return childInterface.getScrollHeight();
        }
        return -1;
    }

    public int getRealWidth() {
        final Widget childInterface = this.widget;
        if (childInterface != null) {
            return childInterface.getScrollWidth();
        }
        return -1;
    }

    public boolean isInScrollableArea() {
        //Check if we have a parent
        if (this.getParentId() == -1) {
            return false;
        }
        //Find scrollable area
        RSWidget scrollableArea = this.getParent();
        while ((scrollableArea.getScrollableContentHeight() == 0) && (scrollableArea.getParentId() != -1)) {
            scrollableArea = scrollableArea.getParent();
        }

        //Return if we are in a scrollable area
        return (scrollableArea.getScrollableContentHeight() != 0);
    }

    /**
     * Gets the selected action name of this component
     *
     * @return the selected action name or "" if null
     */
    public String getSelectedActionName() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getTargetVerb();
        }
        return "";
    }

    /**
     * Gets the text of this component
     *
     * @return the text or "" if null
     */
    public String getText() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getText();
        }
        return "";
    }

    /**
     * Gets the text color of this component
     *
     * @return the text color or -1 if null
     */
    public int getBackgroundColor() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getTextColor();
        }
        return -1;
    }

    /**
     * Gets the FontID of this component
     *
     * @return the fontID or -1 if null
     */
    public int getFontID() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getFontId();
        }
        return -1;
    }

    /**
     * Returns whether or not the text is shadowed
     *
     * @return true unless font isn't shadowed or component is null
     */
    public boolean isTextShadowed() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getTextShadowed();
        }
        return false;
    }

    /**
     * Gets the type of this component
     *
     * @return the type or -1 if null
     */
    public int getType() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getType();
        }
        return -1;
    }

    /**
     * Checks the actions of the child for a given substring
     *
     * @param phrase The phrase to check for
     * @return <tt>true</tt> if found
     */
    public boolean containsAction(final String phrase) {
        for (final String action : getActions()) {
            if (action.toLowerCase().contains(phrase.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the actions of this component.
     *
     * @return the actions or an empty array if null
     */
    public String[] getActions() {
        final Widget inter = this.widget;
        if (inter != null) {
            return inter.getActions();
        }
        return new String[0];
    }

    /**
     * Checks the text of this component for a given substring
     *
     * @param phrase The phrase to check for
     * @return <tt>true</tt> if the text contained the phrase
     * @see #getText()
     */
    public boolean containsText(final String phrase) {
        return getText().contains(phrase);
    }


    /**
     * Gets the value index array of this component
     *
     * @return the value index array or new int[0][0] if null
     */
    public int[][] getValueIndexArray() {
        //Outdated method likely, will need to remake later
		/*
		final net.runelite.api.widgets.Widget childInterface = this.widget;
		if (childInterface != null) {
			final int[][] vindex = childInterface.getValueIndexArray();
			if (vindex != null) { // clone does NOT deep copy
				final int[][] out = new int[vindex.length][0];
				for (int i = 0; i < vindex.length; i++) {
					final int[] cur = vindex[i];
					if (cur != null) {
						// clone, otherwise you have a pointer
						out[i] = cur.clone();
					}
				}
				return out;
			}
		}
		*/
        return new int[0][0];
    }

    /**
     * Gets the width of this component
     *
     * @return the width of the component or -1 if null
     */
    public int getWidth() {
        if (!isInScrollableArea()) {
            return getRealWidth();
        }

        final Widget childInterface = this.widget;
        if (childInterface != null) {
            return childInterface.getWidth() - 4;
        }
        return -1;
    }

    public Rectangle getBounds() {
        final Widget inter = this.widget;
        return inter.getBounds();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof RSWidget) {
            final RSWidget child = (RSWidget) obj;
            return (id == child.getId()) && child.parentWidget.equals(parentWidget);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.widget.hashCode();
    }




    public boolean isClickable() {
        return isVisible() && isValid() && isSelfVisible();
    }


}
