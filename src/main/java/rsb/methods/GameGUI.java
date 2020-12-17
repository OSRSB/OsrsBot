package rsb.methods;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

/**
 * For internal use to find GUI components.
 *
 * @author Qauters
 */
public class GameGUI extends MethodProvider {

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
		//TO DO Add resizable support for compass
		return (methods.game.isFixed()) ? methods.client.getWidget(WidgetInfo.FIXED_VIEWPORT.getId(), 10) : null;
	}

	/**
	 * @return The minimaps <tt>RSInterface</tt>; otherwise null.
	 */
	public synchronized Widget getMinimapInterface() {
		return (methods.game.isFixed()) ? methods.client.getWidget(WidgetInfo.FIXED_VIEWPORT_MINIMAP) :
				methods.client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_WIDGET);
	}

	/**
	 * @param id The ID of the tab.
	 * @return The specified tab <tt>RSInterface</tt>; otherwise null.
	 */
	public synchronized Widget getTab(final int id) {
		return (methods.game.isFixed() ? methods.client.getWidget(WidgetInfo.FIXED_VIEWPORT.getGroupId(), id) :
				methods.client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_OLD_SCHOOL_BOX.getGroupId(), id));
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
