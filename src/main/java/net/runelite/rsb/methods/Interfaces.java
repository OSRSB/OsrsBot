package net.runelite.rsb.methods;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.wrappers.RSWidget;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
	private final Map<Integer, RSWidget> sparseMap = new HashMap<>();


	/**
	 * Gets a widget corresponding to the index
	 *
	 * @param index The index of the interface.
	 * @return The <code>RSWidget</code> for the given index.
	 */
	@Subscribe
	public RSWidget get(final int index) {
		return new RSWidget(methods, methods.client.getWidget(index, 0));
	}

	/**
	 * Gets a widget corresponding to the indexes
	 *
	 * @param index      The parent interface index
	 * @param childIndex The component index
	 * @return <code>RSWidget</code> for the given index and child index.
	 */
	@Subscribe
	public RSWidget getComponent(final int index, final int childIndex) {
		return new RSWidget(methods, methods.client.getWidget(index, childIndex));
	}

	/**
	 * Gets a widget corresponding to the widget info
	 *
	 * @param info The WidgetInfo for the corresponding RSWidget to retrieve
	 * @return The RSWidget for the WidgetInfo
	 */
	@Subscribe
	public RSWidget getComponent(WidgetInfo info) {
		return new RSWidget(methods, methods.client.getWidget(info.getGroupId(), info.getChildId()));
	}

	/**
	 * Gets a widget corresponding to the widget info
	 *
	 * @param info The WidgetInfo for the corresponding RSWidget to retrieve
	 * @return The RSWidget for the WidgetInfo
	 */
	@Subscribe
	public RSWidget getComponent(GlobalWidgetInfo info) {
		return new RSWidget(methods, methods.client.getWidget(info.getGroupId(), info.getChildId()));
	}

	/**
	 * Checks for the click here to continue widget
	 *
	 * @return <code>true</code> if continue component is valid; otherwise
	 * <code>false</code>.
	 */
	public boolean canContinue() {
		return getContinueComponent() != null;
	}

	/**
	 * Clicks the click here to continue widget
	 *
	 * @return <code>true</code> if continue component was clicked; otherwise
	 * <code>false</code>.
	 */
	public boolean clickContinue() {
		RSWidget cont = getContinueComponent();
		return cont != null && cont.isValid() && cont.doClick(true);
	}

	public boolean clickContinue(boolean hotKey) {
		RSWidget cont = getContinueComponent();
		if (cont != null && cont.isValid()) {
			if (!hotKey) {
				return cont.doClick(true);
			} else {
				methods.keyboard.sendText(" ", false);
			}
		}
		return false;
	}

	/**
	 * Gets the click here to continue widget
	 *
	 * @return <code>RSWidget</code> containing "Click here to continue";
	 * otherwise null.
	 */
	@Subscribe
	public RSWidget getContinueComponent() {
		Widget widget = methods.client.getWidget(GlobalWidgetInfo.DIALOG_NPC_CONTINUE.getPackedId());
		if (widget != null && !widget.isHidden()) {
			return new RSWidget(methods, methods.client.getWidget(GlobalWidgetInfo.DIALOG_NPC_CONTINUE.getGroupId(), GlobalWidgetInfo.DIALOG_NPC_CONTINUE.getChildId()));
		}
		if (widget == null) {
			widget = methods.client.getWidget(GlobalWidgetInfo.DIALOG_PLAYER_CONTINUE.getPackedId());
			if (widget != null && !widget.isHidden())
				return new RSWidget(methods, methods.client.getWidget(GlobalWidgetInfo.DIALOG_PLAYER_CONTINUE.getGroupId(), GlobalWidgetInfo.DIALOG_PLAYER_CONTINUE.getChildId()));
		}
		if (widget == null) {
			widget = methods.client.getWidget(GlobalWidgetInfo.DIALOG_CONTINUE.getPackedId());
			if (widget != null && !widget.isHidden())
				return new RSWidget(methods, methods.client.getWidget(GlobalWidgetInfo.DIALOG_CONTINUE.getGroupId(), GlobalWidgetInfo.DIALOG_CONTINUE.getChildId()));
		}
		if (widget == null) {
			widget = methods.client.getWidget(GlobalWidgetInfo.DIALOG_LEVEL_UP_CONTINUE.getPackedId());
			if (widget != null && !widget.isHidden())
				return new RSWidget(methods, methods.client.getWidget(GlobalWidgetInfo.DIALOG_LEVEL_UP_CONTINUE.getGroupId(), GlobalWidgetInfo.DIALOG_LEVEL_UP_CONTINUE.getChildId()));
		}
		if (widget == null) {
			widget = methods.client.getWidget(GlobalWidgetInfo.DIALOG_QUEST_CONTINUE.getPackedId());
			if (widget != null && !widget.isHidden())
				return new RSWidget(methods, methods.client.getWidget(GlobalWidgetInfo.DIALOG_QUEST_CONTINUE.getGroupId(), GlobalWidgetInfo.DIALOG_QUEST_CONTINUE.getChildId()));
		}
		if (widget == null) {
			widget = methods.client.getWidget(GlobalWidgetInfo.DIALOG_UNKNOWN_CONTINUE.getPackedId());
			if (widget != null && !widget.isHidden())
				return new RSWidget(methods, methods.client.getWidget(GlobalWidgetInfo.DIALOG_UNKNOWN_CONTINUE.getGroupId(), GlobalWidgetInfo.DIALOG_UNKNOWN_CONTINUE.getChildId()));
		}
		return null;
	}

	/**
	 * Performs the given action on this RSWidgetChild if it is showing
	 * (valid).
	 *
	 * @param c      The component widget to click
	 * @param action The menu action to click.
	 * @return <code>true</code> if the action was clicked; otherwise <code>false</code>
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
		if (actual.contains(new Point(methods.mouse.getLocation().getX(), methods.mouse.getLocation().getY()))
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
	 * @return <code>true</code> if the option was clicked; otherwise <code>false</code>
	 * .
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
	 * Clicks the dialogue option that contains the desired string.
	 *
	 * @param inter   The interface of the dialogue menu.
	 * @param options The text we want to click.
	 * @return <code>true</code> if the option was clicked; otherwise <code>false</code>
	 * .
	 */
	public boolean clickDialogueOption(final RSWidget inter, String... options) {
		// This is superfluous but it just makes life a little easier
		// so you don't have to look up the component.
		// Just grab the interface and the text you want to click.
		if (inter.isValid()) {
			for (RSWidget c : inter.getComponents()) {
				for (String option : options) {
					if (c.getText().toLowerCase().contains(option.toLowerCase())) {
						return c.doClick();
					}
				}
			}
		}
		return false;
	}

	private static final int CUTSCENE_VARBIT = 542;

	public boolean isCutsceneActive() {
		return methods.client.getVarbitValue(CUTSCENE_VARBIT) > 0;
	}

	/**
	 * Lazily returns an active dialog widget (player chat options, npc chat, player chat, object chat)
	 *
	 * @return RSWidget or null if no dialog active
	 * .
	 */
	public RSWidget getDialogWidget() {
		RSWidget widget = getComponent(WidgetInfo.DIALOG_OPTION_OPTIONS);
		if (widget != null && widget.isValid()) {
			return widget;
		}
		widget = getComponent(WidgetInfo.DIALOG_NPC_TEXT);
		if (widget != null && widget.isValid()) {
			return widget;
		}
		widget = getComponent(WidgetInfo.DIALOG_PLAYER_TEXT);
		if (widget != null && widget.isValid()) {
			return widget;
		}
		widget = getComponent(WidgetInfo.DIALOG_SPRITE_TEXT);
		if (widget != null && widget.isValid()) {
			return widget;
		}
		return null;
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

		if (scrollBar.getComponents().length != 6 || component.isVisibleInScrollableArea()) {
			return true; // no scrollbar, so probably not scrollable
		}

		// Find scrollable area
		RSWidget scrollableArea = component;
		while ((scrollableArea.getScrollableContentHeight() == 0)
				&& (scrollableArea.getParentId() != -1)) {
			scrollableArea = scrollableArea.getParent();
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
		int areaHeight = scrollableArea.getHeight();

		if (component.isVisibleInScrollableArea()) {
			return true;
		}

		// Calculate scroll bar position to click
		RSWidget scrollBarArea = scrollBar.getDynamicComponent(0);

		int contentHeight = scrollableArea.getScrollableContentHeight();

		// scrollBarArea.getHeight() is returning -1. I think because it's a dynamic component?
		int pos = (int) (((scrollBarArea.getBounds().getHeight()) / (double) contentHeight) *
				(component.getRelativeY() + random(-areaHeight / 3, areaHeight / 3)));

		pos = Math.min((int) scrollBarArea.getBounds().getHeight() - 1, Math.max(1, pos));

		// Click on the scrollbar
		methods.mouse.click(
				scrollBarArea.getAbsoluteX()
						+ random(0, scrollBarArea.getRealWidth()),
				scrollBarArea.getAbsoluteY() + pos, true);

		// Wait a bit
		sleep(random(100, 200));

		return component.isVisibleInScrollableArea();
	}

	/**
	 * Waits for an interface to be closed/opened.
	 *
	 * @param iface The interface to wait for.
	 * @param valid True if open, false if close.
	 * @param timer Milliseconds to wait for the interface to open/close.
	 * @return <code>true</code> if the interface was successfully closed/opened.
	 */
	public boolean waitFor(RSWidget iface, boolean valid, int timer) {
		for (int w = 0; w < timer && iface.isValid() == valid; w++) {
			sleep(1);
		}
		return iface.isValid() == valid;
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
	 * @return <code>true</code> f the interface was interacted with; else <code>false</code>
	 */
	// TODO: why the heck should i be forced to make only first option if multiple choices exist!
	@Subscribe
	public boolean makeX(int amount) {
		RSWidget widget = null;
		if (amount == -1) {
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BUTTON_ALL_DYNAMIC_CONTAINER));
			if (!widget.isValid()) {
				return false;
			}
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BUTTON_ALL_DYNAMIC_CONTAINER)).getDynamicComponent(9);
			//Determines if the widget is already selected
			if (!widget.containsText("<col=ffffff>")) {
				clickComponent(widget, "All");
			}
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BOTTOM_BAR_FIRST_CHOICE_BAR_DYN_CONTAINER));
			return clickComponent(widget, "Make");
		} else if (amount == 1) {
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BUTTON_ONE_DYNAMIC_CONTAINER));
			if (!widget.isValid()) {
				return false;
			}
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BUTTON_ONE_DYNAMIC_CONTAINER)).getDynamicComponent(9);
			if (!widget.containsText("<col=ffffff>")) {
				clickComponent(widget, "1");
			}
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BOTTOM_BAR_FIRST_CHOICE_BAR_DYN_CONTAINER));
			return clickComponent(widget, "Make " + Menu.stripFormatting(widget.getName()));
		} else if (amount == 5) {
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BUTTON_FIVE_DYNAMIC_CONTAINER));
			if (!widget.isValid()) {
				return false;
			}
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BUTTON_FIVE_DYNAMIC_CONTAINER)).getDynamicComponent(9);
			if (!widget.containsText("<col=ffffff>")) {
				clickComponent(widget, "5");
			}
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BOTTOM_BAR_FIRST_CHOICE_BAR_DYN_CONTAINER));
			return clickComponent(widget, "Make " + Menu.stripFormatting(widget.getName()));
		} else {
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BUTTON_X_DYNAMIC_CONTAINER));
			if (!widget.isValid()) {
				return false;
			}
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BUTTON_X_DYNAMIC_CONTAINER)).getDynamicComponent(9);
			clickComponent(widget, "X");
			methods.keyboard.sendText(String.valueOf(amount), true);
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BUTTON_X_DYNAMIC_CONTAINER)).getDynamicComponent(9);
			if (!widget.containsText("<col=ffffff>")) {
				clickComponent(widget, String.valueOf(amount));
			}
			widget = new RSWidget(methods, methods.client.getWidget(WidgetIndices.MakeDialog.GROUP_INDEX, WidgetIndices.MakeDialog.BOTTOM_BAR_FIRST_CHOICE_BAR_DYN_CONTAINER));
			return clickComponent(widget, "Make " + Menu.stripFormatting(widget.getName()));
		}
	}
}
