package net.runelite.rsb.methods;

import net.runelite.api.Skill;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.internal.globval.enums.Prayers;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.ArrayList;

import static net.runelite.rsb.internal.globval.VarbitIndices.QUICK_PRAYER;

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
		return methods.client.getVarbitValue(prayer.getVarbit()) == 1;
	}

	/**
	 * Returns true if the quick prayer interface has been used to activate
	 * prayers.
	 *
	 * @return <code>true</code> if quick prayer is on; otherwise <code>false</code>.
	 */
	public boolean isQuickPrayerOn() {
		return methods.client.getVarbitValue(QUICK_PRAYER) == 1;
	}

	/**
	 * Activates/deactivates a prayer via interfaces.
	 *
	 * @param prayer   The prayer to activate.
	 * @param activate <code>true</code> to activate; <code>false</code> to deactivate.
	 * @return <code>true</code> if the interface was clicked; otherwise
	 * <code>false</code>.
	 */
	public boolean activatePrayer(final Prayers prayer, final boolean activate) {
		if (isPrayerOn(prayer) == activate) {
			return true;
		}
		if (methods.game.openTab(InterfaceTab.PRAYER)) {
			RSWidget pray = getPrayerWidget(prayer);
			return pray.doAction(activate ? "Activate" : "Deactivate");
		}
		return false;
	}

	public boolean activatePrayer(final Prayers prayer) {
		return activatePrayer(prayer, true);
	}

	/**
	 * Activates/deactivates quick prayers via interfaces.
	 *
	 * @param activate <code>true</code> to activate; <code>false</code> to deactivate.
	 * @return <code>true</code> if the interface was clicked; otherwise
	 * <code>false</code>.
	 */
	public boolean activateQuickPrayer(final boolean activate) {
		if (isQuickPrayerOn() == activate) {
			return true;
		}
		return getQuickPrayerOrbWidget().doAction(
				activate ? "Activate" : "Deactivate");
	}

	public boolean activateQuickPrayer() {
		return activateQuickPrayer(true);
	}

	/**
	 * Sets up the quick prayers for the user
	 *
	 * @param unsetPrevious whether the previous quick prayers should be unset
	 * @param prayers       the prayers to activate with quick prayers
	 * @return <code>True</code> unless unable to access the interface
	 */
	public boolean setQuickPrayers(boolean unsetPrevious, Prayers... prayers) {
		final int SET_PRAYER_SPRITE = 181;
		methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_QUICK_PRAYER_ORB).doAction("Setup");
		sleep(1000);
		RSWidget quickPrayers = methods.interfaces.getComponent(GlobalWidgetInfo.QUICK_PRAYER_PRAYERS);
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
					if (quickPrayer.getName().toLowerCase().contains(prayer.name().replaceAll("_", " ").toLowerCase())) {
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
	 * Returns an array of Prayers representing the prayers that are
	 * selected.
	 *
	 * @return An <code>Prayers</code> array containing all the components
	 *         that represent selected prayers.
	 */
	public Prayers[] getSelectedPrayers() {
		ArrayList<Prayers> selected = new ArrayList<>();
		for (Prayers prayer : Prayers.values()) {
			if (isPrayerOn(prayer)) {
				selected.add(prayer);
			}
		}
		return selected.toArray(new Prayers[selected.size()]);
	}


	public RSWidget getPrayerWidget(Prayers prayer) {
		return methods.interfaces.getComponent(WidgetIndices.PrayersTab.GROUP_ID, prayer.getPrayerChildIndex());
	}

	public RSWidget getQuickPrayerOrbWidget() {
		return methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_QUICK_PRAYER_ORB);
	}

	/**
	 * Checks if the player has the prayer level to cast a prayer.
	 *
	 * @param prayer the prayer to check
	 * @return true if player has required prayer level
	 */
	public boolean canCast(Prayers prayer) {
		return prayer.getRequiredLevel() <= methods.skills.getRealLevel(Skill.PRAYER.ordinal());
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
