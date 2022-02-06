package rsb.methods;

import net.runelite.api.Actor;
import net.runelite.api.Skill;
import net.runelite.api.widgets.WidgetInfo;
import rsb.internal.globval.GlobalSettingValues;
import rsb.internal.globval.GlobalWidgetInfo;
import rsb.wrappers.*;

/**
 * Combat related operations.
 */
public class Combat extends MethodProvider {

	public Combat(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Eats at the desired HP %.
	 *
	 * @param percent The health percentage to eat at; 10%-90%
	 * @param foods   Array of Foods we can eat.
	 * @return <tt>true</tt> once we eaten to the health % (percent); otherwise
	 *         <tt>false</tt>.
	 */
	@Deprecated
	public boolean Eat(final int percent, final int... foods) {
		return eat(percent, foods);
	}

	/**
	 * Eats at the desired HP %.
	 *
	 * @param percent The health percentage to eat at; 10%-90%
	 * @param foods   Array of Foods we can eat.
	 * @return <tt>true</tt> once we eaten to the health % (percent); otherwise
	 *         <tt>false</tt>.
	 */
	public boolean eat(final int percent, final int... foods) {
		int firstPercent = getHealth();
		for (int food : foods) {
			if (!methods.inventory.contains(food)) {
				continue;
			}
			if (methods.inventory.getItem(food).doAction("Eat")) {
				for (int i = 0; i < 100; i++) {
					sleep(random(100, 300));
					if (firstPercent < percent) {
						break;
					}
				}
			}
			if (getHealth() >= percent) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Turns auto-retaliate on or off in the combat tab.
	 *
	 * @param enable <tt>true</tt> to enable; <tt>false</tt> to disable.
	 */
	public void setAutoRetaliate(final boolean enable) {
		final RSWidget autoRetal = methods.interfaces.getComponent(GlobalWidgetInfo.COMBAT_AUTO_RETALIATE);
		if (isAutoRetaliateEnabled() != enable) {
			if (methods.game.getCurrentTab() != GameGUI.Tab.COMBAT) {
				methods.game.openTab(GameGUI.Tab.COMBAT);
			}
			if (methods.game.getCurrentTab() == GameGUI.Tab.COMBAT
					&& autoRetal != null) {
				autoRetal.doClick();
			}
		}
	}

	/**
	 * Returns whether or not the auto-retaliate option is enabled.
	 *
	 * @return <tt>true</tt> if retaliate is enabled; otherwise <tt>false</tt>.
	 */
	public boolean isAutoRetaliateEnabled() {
		return methods.settings.getSetting(GlobalSettingValues.SETTING_AUTO_RETALIATE) == 0;
	}

	/**
	 * Gets the attack mode.
	 *
	 * @return The current fight mode setting.
	 */
	public int getFightMode() {
		return methods.settings.getSetting(GlobalSettingValues.SETTING_COMBAT_STYLE);
	}

	/**
	 * Sets the attack mode.
	 *
	 * @param fightMode The fight mode to set it to. From 0-3 corresponding to the 4
	 *                  attacking modes; Else if there is only 3 attacking modes then,
	 *                  from 0-2 corresponding to the 3 attacking modes
	 * @return <tt>true</tt> if the interface was clicked; otherwise
	 *         <tt>false</tt>.
	 * @see #getFightMode()
	 */
	public boolean setFightMode(int fightMode) {
		if (fightMode != getFightMode()) {
			methods.game.openTab(GameGUI.Tab.COMBAT);
			if (fightMode == 0) {
				return methods.interfaces.getComponent(WidgetInfo.COMBAT_STYLE_ONE).doClick();
			} else if (fightMode == 1) {
				return methods.interfaces.getComponent(WidgetInfo.COMBAT_STYLE_TWO).doClick();
			} else if (fightMode == 2) {
				return methods.interfaces.getComponent(WidgetInfo.COMBAT_STYLE_THREE).doClick();
			} else if (fightMode == 3) {
				return methods.interfaces.getComponent(WidgetInfo.COMBAT_STYLE_FOUR).doClick();
			}
		}
		return false;
	}

	/**
	 * Gets the current Wilderness Level. Written by Speed.
	 *
	 * @return The current wilderness level otherwise, 0.
	 */
	public int getWildernessLevel() {
		RSWidget widget = methods.interfaces.getComponent(WidgetInfo.PVP_WILDERNESS_LEVEL);
		return (widget.isValid() && widget.isVisible()) ? Integer.parseInt(widget.getText().replace("Level: ", "").trim()) : 0;

	}

	/**
	 * Gets the current player's life points.
	 *
	 * @return The current life points if the interface is valid; otherwise 0.
	 */
	public int getLifePoints() {
		try {
			return Integer.parseInt(methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_HEALTH_ORB_TEXT).getText());
		} catch (NumberFormatException ex) {
			return 0;
		}
	}


	/**
	 * 	-1 : Poison immune
	 * 	  Normal poison damage is ceil( this / 5.0f )
	 * 	  If this is greater than or equal to 1000000, the player is envenomed.
	 * 	   Venom damage is (this - 999997) * 2
	 */

	/**
	 * Returns whether or not we're poisoned.
	 *
	 * @return <tt>true</tt> if poisoned; otherwise <tt>false</tt>.
	 */

	public boolean isPoisoned() {
		return 0 < methods.settings.getSetting(GlobalSettingValues.SETTING_POISON) && methods.settings.getSetting(GlobalSettingValues.SETTING_POISON) < 1000000;
	}

	/**
	 * Returns whether we're envenomed
	 *
	 * @return	<tt>true</tt> if the local player is envenomed; otherwise <tt>false</tt>
	 */
	public boolean isEnvenomed() {
		return methods.settings.getSetting(GlobalSettingValues.SETTING_POISON) > 1000000;
	}

	/**
	 * Returns the damage we're taking from venom
	 *
	 * @return the venom damage if envenomed; otherwise 0;
	 */

	public int getVenomDamage() {
		if (isEnvenomed())
			return (methods.settings.getSetting(GlobalSettingValues.SETTING_POISON) - 999997) * 2;
		return 0;
	}

	/**
	 * Returns whether or not the special-attack option is enabled.
	 *
	 * @return <tt>true</tt> if special is enabled; otherwise <tt>false</tt>.
	 */
	public boolean isSpecialEnabled() {
		return methods.settings.getSetting(GlobalSettingValues.SETTING_SPECIAL_ATTACK_ENABLED) == 1;
	}

	/**
	 * Gets the special bar energy amount.
	 *
	 * @return The current spec energy.
	 */
	public int getSpecialBarEnergy() {
		try {
			return Integer.parseInt(methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_SPEC_ORB_TEXT).getText()
					.trim());
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	/**
	 * Gets the current player's prayer points.
	 *
	 * @return The current prayer points if the interface is valid; otherwise 0.
	 */
	public int getPrayerPoints() {
		try {
			return Integer.parseInt(methods.interfaces.getComponent(WidgetInfo.MINIMAP_PRAYER_ORB_TEXT).getText()
					.trim());
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	/**
	 * Gets the current player's health as a percentage of full health.
	 *
	 * @return The current percentage health remaining.
	 */
	public int getHealth() {
		return ((getLifePoints() * 100) / methods.skills.getRealLevel(Skill.HITPOINTS.ordinal()));
	}

	/**
	 * Checks if your character is interacting with an Npc.
	 *
	 * @param npc The Npc we want to fight.
	 * @return <tt>true</tt> if interacting; otherwise <tt>false</tt>.
	 */
	public boolean isAttacking(final RSNPC npc) {
		// Helpful for new scripters confused by the function of isInCombat()
		Actor interact = methods.players.getMyPlayer().getInteracting();
		return interact != null && interact.equals(npc.getAccessor());
	}

	/**
	 * Checks whether the desired Npc is dead.
	 *
	 * @param npc The RSNPC to check.
	 * @return <tt>true</tt> if the Npc is dead or dying; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean isDead(final RSNPC npc) {
		// getHPPercent() can return 0 when the Npc has a sliver of health left
		// getAnimation() confirms a death animation is playing (to prevent
		// false positives)
		// getInteracting() confirms because it will no longer interact if
		// dead/dying
		return npc == null || !npc.isValid() || (npc.getHPPercent() == 0 && npc.getAnimation() != -1 && npc
				.getInteracting() == null);
	}
}
