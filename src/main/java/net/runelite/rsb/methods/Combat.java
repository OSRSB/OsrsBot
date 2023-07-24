package net.runelite.rsb.methods;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.http.api.item.ItemEquipmentStats;
import net.runelite.http.api.item.ItemStats;
import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.internal.globval.VarpIndices;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.VarpValues;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.internal.globval.enums.Prayers;
import net.runelite.rsb.internal.globval.enums.Spell;
import net.runelite.rsb.wrappers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static net.runelite.rsb.botLauncher.Application.getBot;

/**
 * Combat related operations.
 */
@Slf4j
public class Combat extends MethodProvider {

	public Combat(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Eats at the desired HP %.
	 *
	 * @param percent The health percentage to eat to; eg.10%-90%
	 * @param foods   Optional: Array of foods we can eat,
	 *                if no array supplied will eat edible stuff in inventory.
	 * @return <code>true</code> once we ate to the health % (percent); otherwise
	 * <code>false</code>.
	 */
	public boolean eatUntilHP(final int percent, final int... foods) {
		if (foods == null || foods.length == 0) {
			return eatEdibleUntilHP(percent);
		}
		return eatFoodsUntilHP(percent, foods);
	}

	/**
	 * Eats at the desired HP %.
	 *
	 * @param percent The health percentage to eat to; eg.10%-90%
	 * @param foods   Array of foods we can eat.
	 * @return <code>true</code> once we ate to the health % (percent); otherwise
	 * <code>false</code>.
	 */
	public boolean eatFoodsUntilHP(final int percent, final int... foods) {
		int firstPercent = getHealth();
		for (int food : foods) {
			if (!methods.inventory.contains(food)) {
				continue;
			}
			if (!methods.inventory.open()) {
				return false;
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
	 * Eats at the desired HP %.
	 *
	 * @param percent The health percentage to eat to; eg.10%-90%
	 * @return <code>true</code> once we ate to the health % (percent); otherwise
	 * <code>false</code>.
	 */
	public boolean eatEdibleUntilHP(final int percent) {
		int firstPercent = getHealth();
		RSItem[] edibleItems = methods.inventory.getAllWithAction("Eat");
		if (edibleItems == null || edibleItems.length == 0) {
			return false;
		}
		if (!methods.inventory.open()) {
			return false;
		}
		for (RSItem edibleItem : edibleItems) {
			if (edibleItem.doAction("Eat")) {
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
		final RSWidget autoRetaliate = methods.interfaces.getComponent(GlobalWidgetInfo.COMBAT_AUTO_RETALIATE);
		if (isAutoRetaliateEnabled() != enable) {
			if (methods.game.getCurrentTab() != InterfaceTab.COMBAT) {
				methods.game.openTab(InterfaceTab.COMBAT);
			}
			if (methods.game.getCurrentTab() == InterfaceTab.COMBAT && autoRetaliate != null) {
				autoRetaliate.doClick();
			}
		}
	}

	/**
	 * Returns whether the auto-retaliate option is enabled.
	 *
	 * @return <code>true</code> if retaliate is enabled; otherwise <code>false</code>.
	 */
	public boolean isAutoRetaliateEnabled() {
		return methods.clientLocalStorage.getVarpValueAt(VarpIndices.TOGGLE_AUTO_RETALIATE)
				== VarpValues.AUTO_RETALIATE_ENABLED.getValue();
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
	 * <code>false</code>.
	 * @see #getFightMode()
	 */
	public boolean setFightMode(int fightMode) {
		if (fightMode != getFightMode()) {
			methods.game.openTab(InterfaceTab.COMBAT);
			if (fightMode == VarpValues.COMBAT_STYLE_FIRST.getValue()) {
				return methods.interfaces.getComponent(GlobalWidgetInfo.COMBAT_STYLE_ONE).doClick();
			} else if (fightMode == VarpValues.COMBAT_STYLE_SECOND.getValue()) {
				return methods.interfaces.getComponent(GlobalWidgetInfo.COMBAT_STYLE_TWO).doClick();
			} else if (fightMode == VarpValues.COMBAT_STYLE_THIRD.getValue()) {
				return methods.interfaces.getComponent(GlobalWidgetInfo.COMBAT_STYLE_THREE).doClick();
			} else if (fightMode == VarpValues.COMBAT_STYLE_FOURTH.getValue()) {
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
		if (!widget.isValid() || !widget.isVisible()) return 0;
		String levelText = widget.getText().replace("Level: ", "").trim();
		if (levelText.contains("<")) {
			levelText = levelText.substring(0, levelText.indexOf("<"));
		}
		// In Clan Wars the level is 0
		if (levelText.equals("--")) return 0;
		return Integer.parseInt(levelText);
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
	 * @return    <code>true</code> if the local player is envenomed; otherwise <code>false</code>
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
	 * Returns whether the special attack option is enabled.
	 *
	 * @return <code>true</code> if special attack is enabled; otherwise <code>false</code>.
	 */
	public boolean isSpecialAttackEnabled() {
		return methods.clientLocalStorage.getVarpValueAt(VarpIndices.TOGGLE_SPECIAL_ATTACK)
				== VarpValues.SPECIAL_ATTACK_ENABLED.getValue();
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
			return Integer.parseInt(methods.interfaces.getComponent(GlobalWidgetInfo.MINIMAP_QUICK_PRAYER_ORB_TEXT)
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

	public boolean isHPZero(final RSNPC npc) {
		return npc.getHPPercent() == 0;
	}

	public boolean isAlive(final RSNPC npc) {
		return npc.getHPPercent() > 0 || (isHPZero(npc) && isFinishable(npc));
	}

	/**
	 * Checks whether the desired Npc is dead.
	 *
	 * @param npc The RSNPC to check.
	 * @return <code>true</code> if the Npc is dead or dying; otherwise
	 * <code>false</code>.
	 */
	//TODO: this will need investigation as there will always be special cases
	public boolean isDead(final RSNPC npc) {
		return isHPZero(npc) && !isFinishable(npc);
	}

	public boolean isBelowHP(final RSNPC npc, final double healthPercentage) {
		return npc.getHPPercent() < healthPercentage;
	}

	public boolean isBelowOrAtHP(final RSNPC npc, final double healthPercentage) {
		return npc.getHPPercent() <= healthPercentage;
	}

	public boolean isAboveHP(final RSNPC npc, final double healthPercentage) {
		return npc.getHPPercent() > healthPercentage;
	}

	public boolean isAboveOrAtHP(final RSNPC npc, final double healthPercentage) {
		return npc.getHPPercent() >= healthPercentage;
	}

	public boolean isLowHealth(final RSNPC npc) {
		return isAboveHP(npc, 0) && isBelowHP(npc, 15);
	}

	public boolean isFinishable(final RSNPC npc) {
		String mobName = npc.getName();
		if (mobName == null || mobName.equals(""))
			return false;
		String[] finishableMobs = {
				"Rockslug", "Desert Lizard", "Small Lizard", "Lizard", "Mutated Zygomite", "Ancient Zygomite", "Gargoyle"
		};
		return Arrays.asList(finishableMobs).contains(mobName);
	}

	public double getFinishableHP(double maxHP, double finishHP) {
		return Math.floor(finishHP / (maxHP / 100));
	}

	public boolean canBeFinished(final RSNPC npc) {
		String mobName = npc.getName();
		if (mobName == null || mobName.equals(""))
			return false;
		switch (mobName) {
			case "Ancient Zygomite" -> isBelowOrAtHP(npc, getFinishableHP(150, 7));
			case "Gargoyle" -> isBelowOrAtHP(npc, getFinishableHP(105, 8));
			case "Zygomite" -> isBelowOrAtHP(npc, getFinishableHP(75, 7));
			case "Lizard" -> isBelowOrAtHP(npc, getFinishableHP(40, 4));
			case "Rockslug" -> isBelowOrAtHP(npc, getFinishableHP(27, 5));
			case "Desert Lizard" -> isBelowOrAtHP(npc, getFinishableHP(25, 4));
			case "Small Lizard" -> isBelowOrAtHP(npc, getFinishableHP(15, 4));
			default -> throw new IllegalStateException("Unexpected value: " + mobName);
		}
		return false;
	}

	public int calculateMaxHit(Spell spell) {
		return DPSCalculator.calculateMaxHit(spell);
	}
	public int calculateMaxHit() {
		return DPSCalculator.calculateMaxHit();
	}

	public class DPSCalculator {
		public enum EquipmentStatTypes {
			MELEE_STR,
			RANGED_STR,
			MAGIC_DMG,
			SLASH_ATT,
			STAB_ATT,
			CRUSH_ATT,
			RANGE_ATT,
			MAGIC_ATT,
			SLASH_DEF,
			STAB_DEF,
			CRUSH_DEF,
			RANGE_DEF,
			MAGIC_DEF
		}

		public enum AttackStyle {
			ACCURATE("Accurate", Skill.ATTACK),
			AGGRESSIVE("Aggressive", Skill.STRENGTH),
			DEFENSIVE("Defensive", Skill.DEFENCE),
			CONTROLLED("Controlled", Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE),
			RANGING("Ranging", Skill.RANGED),
			LONGRANGE("Longrange", Skill.RANGED, Skill.DEFENCE),
			CASTING("Casting", Skill.MAGIC),
			DEFENSIVE_CASTING("Defensive Casting", Skill.MAGIC, Skill.DEFENCE),
			OTHER("Other");

			@Getter
			private final String name;
			@Getter
			private final Skill[] skills;

			AttackStyle(String name, Skill... skills) {
				this.name = name;
				this.skills = skills;
			}
		}

		public enum WeaponType {
			TYPE_0(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
			TYPE_1(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
			TYPE_2(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
			TYPE_3(AttackStyle.RANGING, AttackStyle.RANGING, null, AttackStyle.LONGRANGE),
			TYPE_4(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
			TYPE_5(AttackStyle.RANGING, AttackStyle.RANGING, null, AttackStyle.LONGRANGE),
			TYPE_6(AttackStyle.AGGRESSIVE, AttackStyle.RANGING, AttackStyle.CASTING, null),
			TYPE_7(AttackStyle.RANGING, AttackStyle.RANGING, null, AttackStyle.LONGRANGE),
			TYPE_8(AttackStyle.OTHER, AttackStyle.AGGRESSIVE, null, null),
			TYPE_9(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
			TYPE_10(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
			TYPE_11(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
			TYPE_12(AttackStyle.CONTROLLED, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
			TYPE_13(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
			TYPE_14(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
			TYPE_15(AttackStyle.CONTROLLED, AttackStyle.CONTROLLED, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
			TYPE_16(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
			TYPE_17(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
			TYPE_18(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE, AttackStyle.CASTING, AttackStyle.DEFENSIVE_CASTING),
			TYPE_19(AttackStyle.RANGING, AttackStyle.RANGING, null, AttackStyle.LONGRANGE),
			TYPE_20(AttackStyle.ACCURATE, AttackStyle.CONTROLLED, null, AttackStyle.DEFENSIVE),
			TYPE_21(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE, AttackStyle.CASTING, AttackStyle.DEFENSIVE_CASTING),
			TYPE_22(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE),
			TYPE_23(AttackStyle.CASTING, AttackStyle.CASTING, null, AttackStyle.DEFENSIVE_CASTING),
			TYPE_24(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.CONTROLLED, AttackStyle.DEFENSIVE),
			TYPE_25(AttackStyle.CONTROLLED, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE),
			TYPE_26(AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, null, AttackStyle.AGGRESSIVE),
			TYPE_27(AttackStyle.ACCURATE, null, null, AttackStyle.OTHER),
			TYPE_28(AttackStyle.ACCURATE, AttackStyle.ACCURATE, null, AttackStyle.LONGRANGE),
			TYPE_29(AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE);

			@Getter
			private final AttackStyle[] attackStyles;

			private static final Map<Integer, WeaponType> weaponTypes;

			static {
				ImmutableMap.Builder<Integer, WeaponType> builder = new ImmutableMap.Builder<>();

				for (WeaponType weaponType : values()) {
					builder.put(weaponType.ordinal(), weaponType);
				}

				weaponTypes = builder.build();
			}

			WeaponType(AttackStyle... attackStyles) {
				Preconditions.checkArgument(attackStyles.length == 4 || attackStyles.length == 6,
						"WeaponType " + this + " does not have exactly 4 or 6 attack style arguments");
				this.attackStyles = attackStyles;
			}

			public static WeaponType getWeaponType(int id) {
				return weaponTypes.get(id);
			}
		}

		static public int getEquipmentBonus(EquipmentStatTypes type) {
			ItemContainer c = methods.client.getItemContainer(InventoryID.EQUIPMENT);
			if (c == null) return -1;

			if (!(getBot(methods) instanceof BotLite)) {
				log.warn("Cannot use DPSCalculator with non-BotLite client");
				return -1;
			}

			int bonus = 0;
			final Item[] items = c.getItems();
			for (Item item : items) {
				if (item == null) continue;
				ItemStats stats = ((BotLite) getBot(methods)).getItemManager().getItemStats(item.getId(), false);
				if (stats == null) continue;
				ItemEquipmentStats eqStats = stats.getEquipment();
				if (eqStats == null) continue;
				switch (type) {
					case MELEE_STR:
						bonus += eqStats.getStr();
						break;
					case RANGED_STR:
						bonus += eqStats.getRstr();
						break;
					case MAGIC_DMG:
						bonus += eqStats.getMdmg();
						break;
					case SLASH_ATT:
						bonus += eqStats.getAslash();
						break;
					case STAB_ATT:
						bonus += eqStats.getAstab();
						break;
					case CRUSH_ATT:
						bonus += eqStats.getAcrush();
						break;
					case RANGE_ATT:
						bonus += eqStats.getArange();
						break;
					case MAGIC_ATT:
						bonus += eqStats.getAmagic();
						break;
					case SLASH_DEF:
						bonus += eqStats.getDslash();
						break;
					case STAB_DEF:
						bonus += eqStats.getDstab();
						break;
					case CRUSH_DEF:
						bonus += eqStats.getDcrush();
						break;
					case RANGE_DEF:
						bonus += eqStats.getDrange();
						break;
					case MAGIC_DEF:
						bonus += eqStats.getDmagic();
						break;
				}
			}
			return bonus;
		}

		static public AttackStyle getAttackStyle() {
			final int equippedWeaponTypeVarbit = methods.client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);
			final int currentAttackStyleVarbit = methods.client.getVarpValue(VarPlayer.ATTACK_STYLE);
			final int currentEquippedWeaponTypeVarbit = methods.client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);
			final int currentCastingModeVarbit = methods.client.getVarbitValue(Varbits.DEFENSIVE_CASTING_MODE);
			AttackStyle[] attackStyles = WeaponType.getWeaponType(currentEquippedWeaponTypeVarbit).getAttackStyles();
			if (currentAttackStyleVarbit < attackStyles.length) {
				AttackStyle attackStyle = attackStyles[currentAttackStyleVarbit];
				if (attackStyle == null) {
					attackStyle = AttackStyle.OTHER;
				} else if ((attackStyle == AttackStyle.CASTING) && (currentCastingModeVarbit == 1)) {
					attackStyle = AttackStyle.DEFENSIVE_CASTING;
				}
				return attackStyle;
			}
			return null;
		}

		static public double getVoidBonus(AttackStyle attackStyle) {
			return 1.0;
		}

		static final Map<Prayers, Double> prayerStrengthBonus = Map.of(
				Prayers.BURST_OF_STRENGTH, 1.05,
				Prayers.SUPERHUMAN_STRENGTH, 1.10,
				Prayers.ULTIMATE_STRENGTH, 1.15,
				Prayers.CHIVALRY, 1.18,
				Prayers.PIETY, 1.23);
		static final Map<Prayers, Double> prayerRangedBonus = Map.of(
				Prayers.SHARP_EYE, 1.05,
				Prayers.HAWK_EYE, 1.10,
				Prayers.EAGLE_EYE, 1.15,
				Prayers.RIGOUR, 1.23);
		static final Map<Prayers, Double> prayerMagicBonus = Map.of(
				Prayers.MYSTIC_WILL, 1.05,
				Prayers.MYSTIC_LORE, 1.10,
				Prayers.MYSTIC_MIGHT, 1.15,
				Prayers.AUGURY, 1.23);

		static public double getPrayerBonus(AttackStyle attackStyle) {
			switch (attackStyle) {
				case ACCURATE:
				case AGGRESSIVE:
				case CONTROLLED:
				case DEFENSIVE:
					for (Prayers prayer : prayerStrengthBonus.keySet()) {
						if (methods.prayer.isPrayerOn(prayer)) {
							return prayerStrengthBonus.get(prayer);
						}
					}
					break;
				case RANGING:
				case LONGRANGE:
					for (Prayers prayer : prayerRangedBonus.keySet()) {
						if (methods.prayer.isPrayerOn(prayer)) {
							return prayerRangedBonus.get(prayer);
						}
					}
					break;
				/*
				case CASTING:
				case DEFENSIVE_CASTING:
				case OTHER:
					for (Prayers prayer : prayerMagicBonus.keySet()) {
						if (methods.prayer.isPrayerOn(prayer)) {
							return prayerMagicBonus.get(prayer);
						}
					}
					break;
				 */
			}
			return 1.0;
		}

		static public int getEffectiveStrength(AttackStyle attackStyle) {
			int currentMeleeLevel = methods.skills.getCurrentLevel(Skill.STRENGTH);
			int currentRangedLevel = methods.skills.getCurrentLevel(Skill.RANGED);
			switch (attackStyle) {
				case AGGRESSIVE:
					return (int) Math.floor((currentMeleeLevel * getPrayerBonus(attackStyle) + 11) * getVoidBonus(attackStyle));
				case CONTROLLED:
					return (int) Math.floor((currentMeleeLevel * getPrayerBonus(attackStyle) + 9) * getVoidBonus(attackStyle));
				case ACCURATE:
				case DEFENSIVE:
					return (int) Math.floor((currentMeleeLevel * getPrayerBonus(attackStyle) + 8) * getVoidBonus(attackStyle));
				case RANGING:
					return (int) Math.floor((currentRangedLevel * getPrayerBonus(attackStyle) + 11) * getVoidBonus(attackStyle));
				case LONGRANGE:
					return (int) Math.floor((currentRangedLevel * getPrayerBonus(attackStyle) + 8) * getVoidBonus(attackStyle));
			}
			return 0;

		}

		/**
		 * Calculates the max hit of the player
		 * Currently doesn't support staves like Iban's
		 * Doesn't account for equipment % bonuses including void
		 * Doesn't account for special attacks
		 * Should probably somehow get autocast spell instead of having to specify
		 * Does account for potions, prayer and style bonus
		 *
		 * @param spell The spell to calculate the max hit for
		 * @return The max hit of the player
		 */
		static public int calculateMaxHit(Spell spell) {
			AttackStyle attackStyle = getAttackStyle();

			if (spell != null) {
				return spell.getBaseHit();
			}
			switch (attackStyle) {
				case ACCURATE:
				case AGGRESSIVE:
				case CONTROLLED:
				case DEFENSIVE:
					return (int) Math.floor(0.5 + (getEffectiveStrength(attackStyle) * (getEquipmentBonus(EquipmentStatTypes.MELEE_STR) + 64)) / 640);
				case RANGING:
				case LONGRANGE:
					return (int) Math.floor(0.5 + (getEffectiveStrength(attackStyle) * (getEquipmentBonus(EquipmentStatTypes.RANGED_STR) + 64)) / 640);
			}
			return -1;
		}

		static public int calculateMaxHit() {
			return calculateMaxHit(null);
		}
	}
}
