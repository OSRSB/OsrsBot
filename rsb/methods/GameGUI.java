package net.runelite.client.rsb.methods;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

/**
 * For internal use to find GUI components.
 *
 * @author Qauters
 */
class GameGUI extends MethodProvider {

	private int ind_GUI;
	private int ind_Minimap;
	private int ind_Compass;
	private int[] ind_Tabs;

	public GameGUI(MethodContext ctx) {
		super(ctx);
		resetIDs();
	}

	/**
	 * If GUI is out of sync, resets GUI.
	 */
	private synchronized void checkGUI() {
	}

	/**
	 * @return The compasses <tt>RSInterface</tt>;otherwise null.
	 */
	public synchronized Widget getCompass() {
		return methods.client.getWidget(WidgetInfo.FIXED_VIEWPORT.getId(), 10);
	}

	/**
	 * @return The minimaps <tt>RSInterface</tt>; otherwise null.
	 */
	public synchronized Widget getMinimapInterface() {
		return methods.client.getWidget(WidgetInfo.TO_GROUP(WidgetInfo.FIXED_VIEWPORT_MINIMAP.getGroupId()), WidgetInfo.TO_CHILD(WidgetInfo.FIXED_VIEWPORT_MINIMAP.getId()));
	}

	/**
	 * @param id The ID of the tab.
	 * @return The specified tab <tt>RSInterface</tt>; otherwise null.
	 */
	public synchronized Widget getTab(final int id) {
		return methods.client.getWidget(WidgetInfo.FIXED_VIEWPORT.getGroupId(), id);
	}

	/**
	 * Resets the GameGUI class IDs.
	 */
	private synchronized void resetIDs() {
		ind_GUI = -1;
		ind_Minimap = -1;
		ind_Compass = -1;

		ind_Tabs = new int[17];
		for (int i = 0; i < ind_Tabs.length; i++) {
			ind_Tabs[i] = -1;
		}
	}
}
