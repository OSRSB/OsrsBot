package rsb.methods;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import rsb.internal.globval.GlobalWidgetInfo;
import rsb.wrappers.RSWidget;

import java.awt.*;
import java.util.*;

/**
 * Provides access to interfaces.
 */
public class Interfaces extends MethodProvider {

	public Interfaces(final MethodContext ctx) {
		super(ctx);
	}

	// A cache of all the interfaces. Only as big as the maximum size of the
	// client's cache.
	private RSWidget[] mainCache = new RSWidget[0];
	// If it doesn't fit in the above cache.
	private final Map<Integer, RSWidget> sparseMap = new HashMap<Integer, RSWidget>();


	/**
	 * Gets a widget corresponding to the index
	 *
	 * @param index The index of the interface.
	 * @return The <tt>RSWidget</tt> for the given index.
	 */
	public RSWidget get(final int index) {
		return new RSWidget(methods, methods.client.getWidget(index, 0));
	}

	/**
	 * Gets a widget corresponding to the indexes
	 *
	 * @param index      The parent interface index
	 * @param childIndex The component index
	 * @return <tt>RSWidget</tt> for the given index and child index.
	 */
	public RSWidget getComponent(final int index, final int childIndex) {
		return new RSWidget(methods, methods.client.getWidget(index, childIndex));
	}

	/**
	 * Gets a widget corresponding to the widget info
	 * @param info		The WidgetInfo for the corresponding RSWidget to retrieve
	 * @return			The RSWidget for the WidgetInfo
	 */
	public RSWidget getComponent(WidgetInfo info) {
		return new RSWidget(methods, methods.client.getWidget(info.getGroupId(), info.getChildId()));
	}

	/**
	 * Gets a widget corresponding to the widget info
	 * @param info		The WidgetInfo for the corresponding RSWidget to retrieve
	 * @return			The RSWidget for the WidgetInfo
	 */
	public RSWidget getComponent(GlobalWidgetInfo info) {
		return new RSWidget(methods, methods.client.getWidget(info.getGroupId(), info.getChildId()));
	}

	/**
	 * Checks for the click here to continue widget
	 * @return <tt>true</tt> if continue component is valid; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean canContinue() {
		return getContinueComponent() != null;
	}

	/**
	 * Clicks the click here to continue widget
	 * @return <tt>true</tt> if continue component was clicked; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean clickContinue() {
		RSWidget cont = getContinueComponent();
		return cont != null && cont.isValid() && cont.doClick(true);
	}

	/**
	 * Gets the click here to continue widget
	 * @return <tt>RSWidget</tt> containing "Click here to continue";
	 *         otherwise null.
	 */
	public RSWidget getContinueComponent() {
		Widget widget = methods.client.getWidget(WidgetInfo.DIALOG_NPC_TEXT.getGroupId(), WidgetInfo.DIALOG_NPC_TEXT.getChildId());
		if (widget != null && !widget.isHidden())
		{
			return new RSWidget(methods, WidgetInfo.DIALOG_NPC_TEXT.getGroupId(),  WidgetInfo.DIALOG_NPC_TEXT.getChildId());
		}
		return null;
	}

	/**
	 * Performs the given action on this RSWidgetChild if it is showing
	 * (valid).
	 *
	 * @param c			The component widget to click
	 * @param action 	The menu action to click.
	 *
	 * @return <tt>true</tt> if the action was clicked; otherwise <tt>false</tt>
	 */
	public boolean clickComponent(final RSWidget c, final String action) {
		if (!c.isValid()) {
			return false;
		}
		Rectangle rect = c.getArea();
		if (rect.x == -1) {
			return false;
		}
		// 1 pixel is not enough for all components
		int minX = rect.x + 2, minY = rect.y + 2, width = rect.width - 4, height = rect.height - 4;
		Rectangle actual = new Rectangle(minX, minY, width, height);
		// Check if the menu already contains the action otherwise reposition
		// before clicking
		if (actual.contains(new Point (methods.mouse.getLocation().getX(), methods.mouse.getLocation().getY()))
				&& methods.menu.contains(action)
				&& methods.menu.doAction(action)) {
			return true;
		}
		methods.mouse.move(random(minX, minX + width),
				random(minY, minY + height));
		return methods.menu.doAction(action);
	}

	/**
	 * Clicks the dialogue option that contains the desired string.
	 *
	 * @param inter  The interface of the dialogue menu.
	 * @param option The text we want to click.
	 * @return <tt>true</tt> if the option was clicked; otherwise <tt>false</tt>
	 *         .
	 */
	public boolean clickDialogueOption(final RSWidget inter, String option) {
		// This is superfluous but it just makes life a little easier
		// so you don't have to look up the component.
		// Just grab the interface and the text you want to click.
		if (inter.isValid()) {
			option = option.toLowerCase();
			for (RSWidget c : inter.getComponents()) {
				if (c.getText().toLowerCase().contains(option)) {
					return c.doClick();
				}
			}
		}
		return false;
	}

	/**
	 * Scrolls to the component
	 *
	 * @param component   component to scroll to
	 * @param scrollBarID scrollbar to scroll with
	 * @return true when scrolled successfully
	 */
	public boolean scrollTo(RSWidget component, int scrollBarID) {
		RSWidget scrollBar = getComponent(scrollBarID, 0);
		return scrollTo(component, scrollBar);
	}

	/**
	 * Scrolls to the component
	 *
	 * @param component component to scroll to
	 * @param scrollBar scrollbar to scroll with
	 * @return true when scrolled successfully
	 */
	public boolean scrollTo(RSWidget component, RSWidget scrollBar) {
		// Check arguments
		if (component == null || scrollBar == null || !component.isValid()) {
			return false;
		}

		if (scrollBar.getComponents().length != 6) {
			return true; // no scrollbar, so probably not scrollable
		}

		// Find scrollable area
		RSWidget scrollableArea = component;
		while ((scrollableArea.getScrollableContentHeight() == 0)
				&& (scrollableArea.getParentId() != -1)) {
			scrollableArea = getComponent(scrollableArea.getParentId(), 0);
		}

		// Check scrollable area
		if (scrollableArea.getId() == -1) {
			return true;
		}
		if (scrollableArea.getScrollableContentHeight() == 0) {
			return false;
		}

		// Get scrollable area height
		int areaY = scrollableArea.getAbsoluteY();
		int areaHeight = scrollableArea.getRealHeight();

		// Check if the component is already visible
		if ((component.getAbsoluteY() >= areaY)
				&& (component.getAbsoluteY() <= areaY + areaHeight
				- component.getRealHeight())) {
			return true;
		}

		// Calculate scroll bar position to click
		RSWidget scrollBarArea = scrollBar.getComponent(0);
		int contentHeight = scrollableArea.getScrollableContentHeight();

		int pos = (int) ((float) scrollBarArea.getRealHeight() / contentHeight * (component
				.getRelativeY() + random(-areaHeight / 2, areaHeight / 2
				- component.getRealHeight())));
		if (pos < 0) // inner
		{
			pos = 0;
		} else if (pos >= scrollBarArea.getRealHeight()) {
			pos = scrollBarArea.getRealHeight() - 1; // outer
		}

		// Click on the scrollbar
		methods.mouse.click(
				scrollBarArea.getAbsoluteX()
						+ random(0, scrollBarArea.getRealWidth()),
				scrollBarArea.getAbsoluteY() + pos, true);

		// Wait a bit
		sleep(random(200, 400));

		// Scroll to it if we missed it
		while (component.getAbsoluteY() < areaY
				|| component.getAbsoluteY() > (areaY + areaHeight - component
				.getRealHeight())) {
			boolean scrollUp = component.getAbsoluteY() < areaY;
			scrollBar.getComponent(scrollUp ? 4 : 5).doAction("");

			sleep(random(100, 200));
		}

		// Return whether or not the component is visible now.
		return (component.getAbsoluteY() >= areaY)
				&& (component.getAbsoluteY() <= areaY + areaHeight
				- component.getRealHeight());
	}

	/**
	 * Waits for an interface to be closed/opened.
	 *
	 * @param iface The interface to wait for.
	 * @param valid True if open, false if close.
	 * @param timer Milliseconds to wait for the interface to open/close.
	 * @return <tt>true</tt> if the interface was successfully closed/opened.
	 */
	public boolean waitFor(RSWidget iface, boolean valid, int timer) {
		for (int w = 0; w < timer && iface.isValid() == valid ? true : false; w++) {
			sleep(1);
		}
		return iface.isValid() == valid ? true : false;
	}

	public boolean isInterfaceSubstantiated(int index) {
		return get(index).isVisible() && get(index).isValid() && get(index).isSelfVisible();
	}

	public boolean isValid(int index) {
		return get(index).isValid();
	}

	/**
	 * Uses the make interface to make items
	 * Use -1 to make all
	 *
	 * @param amount The number of items to make
	 * @return <tt>true</tt> f the interface was interacted with; else <tt>false</tt>
	 */
	public boolean makeX(int amount) {
		RSWidget widget = null;
		if (amount == -1) {
			//270.12
			widget = new RSWidget(methods, methods.client.getWidget(270, 12));
			if (!widget.isValid()) {
				return false;
			}
			widget = new RSWidget(methods, methods.client.getWidget(270, 12)).getDynamicComponent(9);
			//Determines if the widget is already selected
			if (!widget.containsText("<col=ffffff>")) {
				clickComponent(widget, "All");
			}
			widget = new RSWidget(methods, methods.client.getWidget(270, 14));
			return clickComponent(widget, "Make");
		} else
		if (amount == 1) {
			//270.7
			widget = new RSWidget(methods, methods.client.getWidget(270, 7));
			if (!widget.isValid()) {
				return false;
			}
			widget = new RSWidget(methods, methods.client.getWidget(270, 7)).getDynamicComponent(9);
			if (!widget.containsText("<col=ffffff>")) {
				clickComponent(widget, "1");
			}
			widget = new RSWidget(methods, methods.client.getWidget(270, 14));
			return clickComponent(widget, "Make " + Menu.stripFormatting(widget.getName()));
		} else
		if (amount == 5) {
			//270.8
			widget = new RSWidget(methods, methods.client.getWidget(270, 8));
			if (!widget.isValid()) {
				return false;
			}
			widget = new RSWidget(methods, methods.client.getWidget(270, 8)).getDynamicComponent(9);
			if (!widget.containsText("<col=ffffff>")) {
				clickComponent(widget, "5");
			}
			widget = new RSWidget(methods, methods.client.getWidget(270, 14));
			return clickComponent(widget, "Make " + Menu.stripFormatting(widget.getName()));
		}
		else {
			//270.11
			widget = new RSWidget(methods, methods.client.getWidget(270, 11));
			if (!widget.isValid()) {
				return false;
			}
			widget = new RSWidget(methods, methods.client.getWidget(270, 11)).getDynamicComponent(9);
			clickComponent(widget, "X");
			methods.keyboard.sendText(String.valueOf(amount), true);
			widget = new RSWidget(methods, methods.client.getWidget(270, 11)).getDynamicComponent(9);
			if (!widget.containsText("<col=ffffff>")) {
				clickComponent(widget, String.valueOf(amount));
			}
			widget = new RSWidget(methods, methods.client.getWidget(270, 14));
			return clickComponent(widget, "Make " + Menu.stripFormatting(widget.getName()));
		}
	}

}
