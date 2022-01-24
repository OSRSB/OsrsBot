package rsb.methods;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import java.awt.event.KeyEvent;

/**
 * For internal use to find GUI components.
 *
 * @author GigiaJ
 */
public class GameGUI extends MethodProvider {

	public GameGUI(MethodContext ctx) {
		super(ctx);
		setGUI();
	}

	/**
	 * Sets the GUI values to default
	 * Adjusts them upon the first isFixed() check
	 */
	private synchronized void setGUI() {
	}

	/**
	 * @return The compasses <tt>RSInterface</tt>;otherwise null.
	 */
	public synchronized Widget getCompass() {
		//TODO Add resizable support for compass
		return (isFixed()) ? methods.client.getWidget(WidgetInfo.FIXED_VIEWPORT.getId(), 10) : null;
	}

	/**
	 * @return The minimaps <tt>RSInterface</tt>; otherwise null.
	 */
	public synchronized Widget getMinimapInterface() {
		return (isFixed()) ? methods.client.getWidget(WidgetInfo.FIXED_VIEWPORT_MINIMAP) :
				methods.client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_WIDGET);
	}

	/**
	 * @param tab The enumerated tab containing WidgetInfo of the tab.
	 * @return The specified tab <tt>RSInterface</tt>; otherwise null.
	 */
	public synchronized Widget getTab(final Tab tab) {
		return (isFixed() ? methods.client.getWidget(tab.getFixedInfo()) :
				methods.client.getWidget(tab.getResizeInfo()));
	}

	/**
	 * Determines whether or no the client is currently in the fixed display
	 * mode.
	 *
	 * @return <tt>true</tt> if in fixed mode; otherwise <tt>false</tt>.
	 */
	public boolean isFixed() {
		//TODO: Add resizable check and changes
		return true;//!methods.client.isResized();
	}

	public enum Tab {
		COMBAT("Combat Styles", KeyEvent.VK_F5, WidgetInfo.FIXED_VIEWPORT_COMBAT_TAB, WidgetInfo.RESIZABLE_VIEWPORT_COMBAT_TAB),
		STATS("Stats", 0, WidgetInfo.FIXED_VIEWPORT_STATS_TAB, WidgetInfo.RESIZABLE_VIEWPORT_STATS_TAB),
		QUESTS("Quest Journals", 0, WidgetInfo.FIXED_VIEWPORT_QUESTS_TAB, WidgetInfo.RESIZABLE_VIEWPORT_QUESTS_TAB),
		INVENTORY("Inventory", KeyEvent.VK_F1, WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB, WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_TAB),
		EQUIPMENT("Worn Equipment", KeyEvent.VK_F2, WidgetInfo.FIXED_VIEWPORT_EQUIPMENT_TAB, WidgetInfo.RESIZABLE_VIEWPORT_EQUIPMENT_TAB),
		PRAYER("Prayer List", KeyEvent.VK_F3, WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB, WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB),
		MAGIC("Magic Spellbook", KeyEvent.VK_F4, WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB, WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB),
		FRIENDS("Friends List", 0, WidgetInfo.FIXED_VIEWPORT_FRIENDS_TAB, WidgetInfo.RESIZABLE_VIEWPORT_FRIENDS_TAB),
		OPTIONS("Options", 0, WidgetInfo.FIXED_VIEWPORT_OPTIONS_TAB, WidgetInfo.RESIZABLE_VIEWPORT_OPTIONS_TAB),
		MUSIC("Music Player", 0, WidgetInfo.FIXED_VIEWPORT_MUSIC_TAB, WidgetInfo.RESIZABLE_VIEWPORT_MUSIC_TAB),
		LOGOUT("Exit", 0, WidgetInfo.FIXED_VIEWPORT_LOGOUT_TAB, WidgetInfo.RESIZABLE_VIEWPORT_LOGOUT_TAB);


		private String name;
		private int functionKey;
		private WidgetInfo fixedInfo;
		private WidgetInfo resizeInfo;

		Tab(String name, int functionKey, WidgetInfo fixedInfo, WidgetInfo resizeInfo) {
			this.fixedInfo = fixedInfo;
			this.resizeInfo = resizeInfo;
		}

		public String getName() {
			return name;
		}

		public int getFunctionKey() {
			return functionKey;
		}

		public WidgetInfo getFixedInfo() {
			return fixedInfo;
		}

		public WidgetInfo getResizeInfo() {
			return resizeInfo;
		}




	}

}


