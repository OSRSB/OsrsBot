package net.runelite.rsb.methods;

import net.runelite.api.Skill;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.internal.globval.enums.Prayers;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.ArrayList;

/**
 * Prayer related operations.
 *
 * @author GigiaJ
 */
public class Prayer extends MethodProvider {

	Prayer(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Returns true if designated prayer is turned on.
	 *
	 * @param prayer The prayer to check.
	 * @return <code>true</code> if enabled; otherwise <code>false</code>.
	 */
	public boolean isPrayerOn(Prayers prayer) {
		RSWidget[] prayers = methods.interfaces.getComponent(GlobalWidgetInfo.PRAYER_STANDARD_BOOK)
				.getComponents();
		for (RSWidget c : prayers) {
			if (GlobalWidgetInfo.TO_CHILD(c.getId()) == prayer.getPrayerId()) {
				return c.getDynamicComponent(WidgetIndices.DynamicComponents.PrayerWidget.ICON_SPRITE).isSelfVisible();
			}
		}
		return false;
	}

	/**
	 * Returns true if the quick prayer interface has been used to activate
	 * prayers.
	 *
	 * @return <code>true</code> if quick prayer is on; otherwise <code>false</code>.
	 */
	public boolean isQuickPrayerOn() {
		final int QUICK_PRAYER_SPRITE = 1066;
		//Located two items below the one that contains the name "Quick prayer"
		//Is likely the active sprite for prayer
		return methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_QUICK_PRAYER_ORB_SPRITE)
				.getSpriteId() == QUICK_PRAYER_SPRITE;
	}

	/**
	 * Activates/deactivates a prayer via interfaces.
	 *
	 * @param prayer   The prayer to activate.
	 * @param activate <code>true</code> to activate; <code>false</code> to deactivate.
	 * @return <code>true</code> if the interface was clicked; otherwise
	 *         <code>false</code>.
	 */
	public boolean activatePrayer(final Prayers prayer, final boolean activate) {
		if (isPrayerOn(prayer) == activate) {
			return false;
		}
		RSWidget pray = methods.interfaces.getComponent(WidgetIndices.PrayersTab.PRAYERS_CONTAINER, prayer.getPrayerId());
		if ((pray.getBackgroundColor() != -1) == activate) {
			return false;
		}
		if (methods.game.getCurrentTab() != InterfaceTab.PRAYER && methods.game.openTab(InterfaceTab.PRAYER)) {
			sleep(random(100, 200));
		}
		return pray.doAction(activate ? "Activate" : "Deactivate");
	}

	/**
	 * Activates/deactivates quick prayers via interfaces.
	 *
	 * @param activate <code>true</code> to activate; <code>false</code> to deactivate.
	 * @return <code>true</code> if the interface was clicked; otherwise
	 *         <code>false</code>.
	 */
	public boolean activateQuickPrayer(final boolean activate) {
		return methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_QUICK_PRAYER_ORB).doAction(
				activate ? "Activate" : "Deactivate");
	}

	/**
	 * Sets up the quick prayers for the user
	 *
	 * @param unsetPrevious whether or not the previous quick prayers should be unset
	 * @param prayers the prayers to activate with quick prayers
	 *
	 * @return <code>True</code> unless unable to access the interface
	 */
	public boolean setQuickPrayers(boolean unsetPrevious, Prayers... prayers) {
		final int SET_PRAYER_SPRITE = 181;
		RSWidget quickPrayers = methods.interfaces.getComponent(GlobalWidgetInfo.QUICK_PRAYER_PRAYERS);
		methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_QUICK_PRAYER_ORB).doAction("Setup");
		sleep(random(400,700));
		if (quickPrayers.isValid() && quickPrayers.isVisible()) {
			if (unsetPrevious) {
				for (RSWidget quickPrayer : quickPrayers.getComponents()) {
					if (quickPrayer.getSpriteId() == SET_PRAYER_SPRITE) {
						quickPrayer.doAction("Toggle");
						sleep(random(600, 800));
					}
				}
			}
			RSWidget[] quickPrayersInterface = quickPrayers.getComponents();
			for (Prayers prayer : prayers) {
				for (RSWidget quickPrayer : quickPrayersInterface) {
					if (quickPrayer.getName().contains(prayer.name())) {
						quickPrayer.doAction("Toggle");
						sleep(random(600, 800));
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns an array of RSWidgets representing the prayers that are
	 * selected.
	 *
	 * @return An <code>RSWidget</code> array containing all the components
	 *         that represent selected prayers.
	 */
	public RSWidget[] getSelectedPrayers() {
		ArrayList<RSWidget> selected = new ArrayList<>();
		RSWidget[] prayers = methods.interfaces.getComponent(GlobalWidgetInfo.PRAYER_STANDARD_BOOK).getComponents();
		for (RSWidget prayer : prayers) {
			if (prayer.getDynamicComponent(WidgetIndices.DynamicComponents.PrayerWidget.ICON_SPRITE).isSelfVisible()) {
				selected.add(prayer);
			}
		}
		return selected.toArray(new RSWidget[selected.size()]);
	}

	/**
	 * Gets the remaining prayer points.
	 *
	 * @return The number of prayer points left.
	 */
	public int getPrayerLeft() {
		return Integer.parseInt(methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_QUICK_PRAYER_ORB_TEXT)
				.getText());
	}

	/**
	 * Gets the percentage of prayer points left based on the players current
	 * prayer level.
	 *
	 * @return The percentage of prayer points left.
	 */
	public int getPrayerPercentLeft() {
		return (100 * getPrayerLeft())
				/ methods.skills.getCurrentLevel(Skill.PRAYER.ordinal());
	}

	/**
	 * Provides Prayer Book(s) Information.
	 * This is written in preparation for future prayer books
	 * Likely they'll be similar to MagicBook and simply be one set of child components
	 * of the one we list here
	 *
	 * @author GigiaJ
	 */
	public enum PrayerBook {
		NORMAL(WidgetIndices.PrayersTab.PRAYERS_CONTAINER),
		NULL(-1);

		private final int index;

		PrayerBook(int index) {
			this.index = index;
		}

		int getIndex() {
			return this.index;
		}
	}
}
