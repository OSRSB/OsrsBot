package rsb.methods;

import net.runelite.api.Skill;
import net.runelite.api.widgets.WidgetInfo;
import rsb.wrappers.RSWidget;

import java.util.ArrayList;

/**
 * Prayer related operations.
 *
 * @author Aut0r, kiko
 */
public class Prayer extends MethodProvider {

	Prayer(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Provides Prayer Book(s) Information.
	 *
	 * @author Aut0r
	 */

	private static final int PRAYER_BOOK_OFFSET = 5;
	public static final int INTERFACE_PRAYER_BOOK = WidgetInfo.PRAYER_THICK_SKIN.getGroupId();
	public static final int INTERFACE_PRAYERS = PRAYER_BOOK_OFFSET - 1;
	//The dynamic component in each prayer widget that is visible when prayers are active (the white border)
	public static final int ACTIVE_PRAYER_BORDER = 1;

	public enum Book {

		THICK_SKIN(0, 1), BURST_OF_STRENGTH(1, 4), CLARITY_OF_THOUGHT(2, 7), SHARP_EYE(
				3, 8), MYSTIC_WILL(4, 9), ROCK_SKIN(5, 10), SUPERHUMAN_STRENGTH(
				6, 13), IMPROVED_REFLEXES(7, 16), RAPID_RESTORE(8, 19), RAPID_HEAL(
				9, 22), PROTECT_ITEM(10, 25), HAWK_EYE(11, 26), MYSTIC_LORE(12,
				27), STEEL_SKIN(13, 28), ULTIMATE_STRENGTH(
				14, 31), INCREDIBLE_REFLEXES(
				15, 34), PROTECT_FROM_SUMMONING(16, 35), PROTECT_FROM_MAGIC(17,
				37), PROTECT_FROM_MISSILES(18,
				40), PROTECT_FROM_MELEE(
				19, 43), EAGLE_EYE(
				20, 44), MYSTIC_MIGHT(21, 45), RETRIBUTION(22, 46), REDEMPTION(
				23, 49), SMITE(24, 52), CHIVALRY(25, 60), RAPID_RENEWAL(26, 65), PIETY(
				27, 70), RIGOUR(28, 74), AUGURY(29, 77);

		private final int index;
		private final int level;

		Book(int index, int level) {
			this.index = index;
			this.level = level;
		}

		public int getIndex() {
			return PRAYER_BOOK_OFFSET + index;
		}

		public int getRequiredLevel() {
			return level;
		}

	}

	/**
	 * Returns true if designated prayer is turned on.
	 *
	 * @param prayer The prayer to check.
	 * @return <tt>true</tt> if enabled; otherwise <tt>false</tt>.
	 */
	public boolean isPrayerOn(Book prayer) {
		RSWidget[] prayers = methods.interfaces.getComponent(INTERFACE_PRAYER_BOOK, INTERFACE_PRAYERS)
				.getComponents();
		for (RSWidget c : prayers) {
			if (WidgetInfo.TO_CHILD(c.getId()) == prayer.getIndex()) {
				return c.getDynamicComponent(ACTIVE_PRAYER_BORDER).isSelfVisible();
			}
		}
		return false;
	}

	/**
	 * Returns true if the quick prayer interface has been used to activate
	 * prayers.
	 *
	 * @return <tt>true</tt> if quick prayer is on; otherwise <tt>false</tt>.
	 */
	public boolean isQuickPrayerOn() {
		final int QUICK_PRAYER_SPRITE = 1066;
		//Located two items below the one that contains the name "Quick prayer"
		//Is likely the active sprite for prayer
		int prayerSprite = WidgetInfo.MINIMAP_QUICK_PRAYER_ORB.getChildId() + 2;
		return methods.interfaces.getComponent(WidgetInfo.MINIMAP_PRAYER_ORB.getGroupId(), prayerSprite)
				.getSpriteId() == QUICK_PRAYER_SPRITE;
	}

	/**
	 * Activates/deactivates a prayer via interfaces.
	 *
	 * @param prayer   The prayer to activate.
	 * @param activate <tt>true</tt> to activate; <tt>false</tt> to deactivate.
	 * @return <tt>true</tt> if the interface was clicked; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean activatePrayer(final Book prayer, final boolean activate) {
		if (isPrayerOn(prayer) == activate) {
			return false;
		}
		RSWidget pray = methods.interfaces.getComponent(INTERFACE_PRAYER_BOOK, prayer.getIndex());
		if ((pray.getBackgroundColor() != -1) == activate) {
			return false;
		}
		if (methods.game.getCurrentTab() != Game.TAB_PRAYER && methods.game.openTab(Game.TAB_PRAYER)) {
			sleep(random(100, 200));
		}
		return pray.doAction(activate ? "Activate" : "Deactivate");
	}

	/**
	 * Activates/deactivates quick prayers via interfaces.
	 *
	 * @param activate <tt>true</tt> to activate; <tt>false</tt> to deactivate.
	 * @return <tt>true</tt> if the interface was clicked; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean activateQuickPrayer(final boolean activate) {
		return methods.interfaces.getComponent(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB.getGroupId(), WidgetInfo.MINIMAP_QUICK_PRAYER_ORB.getChildId()).doAction(
				activate ? "Activate" : "Deactivate");
	}

	/**
	 * Sets up the quick prayers for the user
	 *
	 * @param unsetPrevious whether or not the previous quick prayers should be unset
	 * @param prayers the prayers to activate with quick prayers
	 *
	 * @return <tt>True</tt> unless unable to access the interface
	 */
	public boolean setQuickPrayers(boolean unsetPrevious, Book... prayers) {
		RSWidget quickPrayers = methods.interfaces.getComponent(WidgetInfo.QUICK_PRAYER_PRAYERS.getGroupId(), WidgetInfo.QUICK_PRAYER_PRAYERS.getChildId());
		methods.interfaces.getComponent(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB.getGroupId(), WidgetInfo.MINIMAP_QUICK_PRAYER_ORB.getChildId()).doAction("Setup");
		sleep(random(400,700));
		if (quickPrayers.isValid() && quickPrayers.isVisible()) {
			if (unsetPrevious) {
				for (RSWidget quickPrayer : quickPrayers.getComponents()) {
					if (quickPrayer.getSpriteId() == 181) {
						quickPrayer.doAction("Toggle");
						sleep(random(600, 800));
					}
				}
			}
			RSWidget[] quickPrayersInterface = quickPrayers.getComponents();
			for (Book prayer : prayers) {
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
		ArrayList<RSWidget> selected = new ArrayList<RSWidget>();
		RSWidget[] prayers = methods.interfaces.getComponent(WidgetInfo.PRAYER_THICK_SKIN.getGroupId(), 0).getComponents();
		for (RSWidget prayer : prayers) {
			if (prayer.getDynamicComponent(ACTIVE_PRAYER_BORDER).isSelfVisible()) {
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
		return Integer.parseInt(methods.interfaces.getComponent(
				WidgetInfo.MINIMAP_PRAYER_ORB.getGroupId(), Game.INTERFACE_PRAYER_ORB_AMOUNT).getText());
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

}
