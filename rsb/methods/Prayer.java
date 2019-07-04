package net.runelite.client.rsb.methods;

import net.runelite.api.Skill;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.rsb.wrappers.RSWidget;

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

	private final int PRAYER_BOOK_OFFSET = 5;
	public static enum Book {

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
				27, 70), RIGOUR(28, 74), AUGURY(29, 77),

		// New curse prayer book?
		PROTECT_ITEM2(0, 50), SAP_WARRIOR(1, 50), SAP_RANGER(2, 52), SAP_MAGE(
				3, 54), SAP_SPIRIT(4, 56), BERSERKER(5, 59), DEFLECT_SUMMONING(
				6, 62), DEFLECT_MAGIC(7, 65), DEFLECT_MISSLE(8, 68), DEFLECT_MELEE(
				9, 71), LEECH_ATTACK(10, 74), LEECH_RANGE(11, 76), LEECH_MAGIC(
				12, 78), LEECH_DEFENCE(13, 80), LEECH_STRENGTH(14, 82), LEECH_ENERGY(
				15, 84), LEECH_SPECIAL_ATTACK(16, 86), WRATH(17, 89), SOUL_SPLIT(
				18, 92), TURMOIL(19, 95);

		private final int index;
		private final int level;

		Book(int index, int level) {
			this.index = index;
			this.level = level;
		}

		public int getIndex() {
			return index;
		}

		public int getRequiredLevel() {
			return level;
		}

	}

	/**
	 * Checks if the player's prayer book is set to Ancient Curses.
	 *
	 * @return <tt>true</tt> if Curses are enabled; otherwise <tt>false</tt>.
	 */
	public boolean isCursing() {
		return methods.interfaces.getComponent(271, 7).getComponents().length < 29;
	}

	/**
	 * Returns true if designated prayer is turned on.
	 *
	 * @param prayer The prayer to check.
	 * @return <tt>true</tt> if enabled; otherwise <tt>false</tt>.
	 */
	public boolean isPrayerOn(Book prayer) {
		RSWidget[] prayers = methods.interfaces.getComponent(271, 7)
				.getComponents();
		for (RSWidget c : prayers) {
			if (WidgetInfo.TO_CHILD(c.getId()) == prayer.getIndex()
					&& c.getBackgroundColor() != -1) {
				return true;
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
		return methods.interfaces.getComponent(Game.INTERFACE_PRAYER_ORB, 2)
				.getBackgroundColor() == 782;
	}

	/**
	 * Activates/deactivates a prayer via interfaces.
	 *
	 * @param prayer   The prayer to activate.
	 * @param activate <tt>true</tt> to activate; <tt>false</tt> to deactivate.
	 * @return <tt>true</tt> if the interface was clicked; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean setPrayer(final Book prayer, final boolean activate) {
		if (isPrayerOn(prayer) == activate) {
			return false;
		}
		RSWidget pray = methods.interfaces.getComponent(WidgetInfo.PRAYER_THICK_SKIN.getGroupId(), 0).getComponent(PRAYER_BOOK_OFFSET + prayer.getIndex());//271, 7).getComponent(prayer.getIndex());
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
	public boolean setQuickPrayer(final boolean activate) {
		return methods.interfaces.getComponent(749, 2).doAction(
				activate ? "on" : "off");
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
		RSWidget[] prayers = methods.interfaces.getComponent(WidgetInfo.PRAYER_THICK_SKIN.getGroupId(), 0).getComponents();//(271, 7)	.getComponents();
		for (RSWidget prayer : prayers) {
			if (prayer.getBackgroundColor() != -1) {
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
				Game.INTERFACE_PRAYER_ORB, 4).getText());
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
