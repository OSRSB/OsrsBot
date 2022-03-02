package rsb.methods;

import net.runelite.api.Actor;
import net.runelite.api.Skill;
import rsb.internal.globval.VarpIndices;
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
	 * @return <code>true</code> once we eaten to the health % (percent); otherwise
	 *         <code>false</code>.
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
	 * @return <code>true</code> once we eaten to the health % (percent); otherwise
	 *         <code>false</code>.
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
	 * @param enable <code>true</code> to enable; <code>false</code> to disable.
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
	 * @return <code>true</code> if retaliate is enabled; otherwise <code>false</code>.
	 */
	public boolean isAutoRetaliateEnabled() {
		return methods.clientLocalStorage.getVarpValueAt(VarpIndices.AUTO_RETALIATE_ENABLED) == 0;
	}

	/**
	 * Gets the attack mode.
	 *
	 * @return The current fight mode setting.
	 */
	public int getFightMode() {
		return methods.clientLocalStorage.getVarpValueAt(VarpIndices.COMBAT_STYLE);
	}

	/**
	 * Sets the attack mode.
	 *
	 * @param fightMode The fight mode to set it to. From 0-3 corresponding to the 4
	 *                  attacking modes; Else if there is only 3 attacking modes then,
	 *                  from 0-2 corresponding to the 3 attacking modes
	 * @return <code>true</code> if the interface was clicked; otherwise
	 *         <code>false</code>.
	 * @see #getFightMode()
	 */
	public boolean setFightMode(int fightMode) {
		if (fightMode != getFightMode()) {
			methods.game.openTab(GameGUI.Tab.COMBAT);
			if (fightMode == 0) {
				return methods.interfaces.getComponent(GlobalWidgetInfo.COMBAT_STYLE_ONE).doClick();
			} else if (fightMode == 1) {
				return methods.interfaces.getComponent(GlobalWidgetInfo.COMBAT_STYLE_TWO).doClick();
			} else if (fightMode == 2) {
				return methods.interfaces.getComponent(GlobalWidgetInfo.COMBAT_STYLE_THREE).doClick();
			} else if (fightMode == 3) {
				return methods.interfaces.getComponent(GlobalWidgetInfo.COMBAT_STYLE_FOUR).doClick();
			}
		}
		return false;
	}

	/**
	 * Gets the current Wilderness Level.
	 *
	 * @return The current wilderness level otherwise, 0.
	 */
	public int getWildernessLevel() {
		RSWidget widget = methods.interfaces.getComponent(GlobalWidgetInfo.PVP_WILDERNESS_LEVEL);
		return (widget.isValid() && widget.isVisible())
				? Integer.parseInt(widget.getText().replace("Level: ", "").trim())
				: 0;
	}

	/**
	 * Gets the current player's life points.
	 *
	 * @return The current life points if the interface is valid; otherwise 0.
	 */
	public int getLifePoints() {
		try {
			return Integer.parseInt(methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_HEALTH_ORB_TEXT)
					.getText());
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	/**
	 * Returns whether we're poisoned.
	 *
	 * @return <code>true</code> if poisoned; otherwise <code>false</code>.
	 */
	public boolean isPoisoned() {
		return 0 < methods.clientLocalStorage.getVarpValueAt(VarpIndices.POISON)
				&& methods.clientLocalStorage.getVarpValueAt(VarpIndices.POISON) < 1000000;
	}

	/**
	 * Returns the damage we're taking from poison
	 *
	 * @return the poison damage if poisoned; otherwise 0;
	 */
	public double getPoisonDamage() {
		if (isPoisoned())
			return Math.ceil(methods.clientLocalStorage.getVarpValueAt(VarpIndices.POISON) / 5.0f);
		return 0;
	}

	/**
	 * Returns whether we're immune to poison.
	 *
	 * @return <code>true</code> if immune; otherwise <code>false</code>.
	 */
	public boolean isPoisonImmune() {
		return methods.clientLocalStorage.getVarpValueAt(VarpIndices.POISON) == -1;
	}

	/**
	 * Returns whether we're envenomed
	 *
	 * @return	<code>true</code> if the local player is envenomed; otherwise <code>false</code>
	 */
	public boolean isEnvenomed() {
		return methods.clientLocalStorage.getVarpValueAt(VarpIndices.POISON) >= 1000000;
	}

	/**
	 * Returns the damage we're taking from venom
	 *
	 * @return the venom damage if envenomed; otherwise 0;
	 */
	public int getVenomDamage() {
		if (isEnvenomed())
			return (methods.clientLocalStorage.getVarpValueAt(VarpIndices.POISON) - 999997) * 2;
		return 0;
	}

	/**
	 * Returns whether the special-attack option is enabled.
	 *
	 * @return <code>true</code> if special is enabled; otherwise <code>false</code>.
	 */
	public boolean isSpecialEnabled() {
		return methods.clientLocalStorage.getVarpValueAt(VarpIndices.SPECIAL_ATTACK_ENABLED) == 1;
	}

	/**
	 * Returns current special attack energy in percent.
	 *
	 * @return 0 - 100
	 */
	public int getSpecialAttackEnergy() {
		return methods.clientLocalStorage.getVarpValueAt(VarpIndices.SPECIAL_ATTACK_ENERGY);
	}

	/**
	 * Gets the special bar energy amount.
	 *
	 * @return The current spec energy.
	 */
	public int getSpecialBarEnergy() {
		try {
			return Integer.parseInt(methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_SPEC_ORB_TEXT)
					.getText()
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
			return Integer.parseInt(methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_PRAYER_ORB_TEXT)
					.getText()
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
	 * @return <code>true</code> if interacting; otherwise <code>false</code>.
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
	 * @return <code>true</code> if the Npc is dead or dying; otherwise
	 *         <code>false</code>.
	 */
	public boolean isDead(final RSNPC npc) {
		// getHPPercent() can return 0 when the Npc has a sliver of health left
		// getAnimation() confirms a death animation is playing (to prevent
		// false positives)
		// getInteracting() confirms because it will no longer interact if
		// dead/dying
		//TODO: Fix this
		return npc == null || !npc.isValid() || (npc.getHPPercent() == 0 && npc.getAnimation() != -1 && npc
				.getInteracting() == null);
	}
}
