package rsb.methods;

import net.runelite.api.Skill;
import rsb.internal.globval.GlobalWidgetId;
import rsb.util.SkillTracker;

import java.lang.reflect.Field;

/**
 * This class is for all the skill calculations.
 *
 * Example usage: skills.getRealLevel(Skills.ATTACK);
 */
public class Skills extends MethodProvider {

	Skills(final MethodContext ctx) {
		super(ctx);
	}


	public static Skill getSkill(final int index) {
		if (index > 0 && index < Skill.values().length)
			return Skill.values()[index];
		return null;
	}

	/**
	 * Gets the index of the skill with a given name. This is not case
	 * sensitive.
	 *
	 * @param statName The skill's name.
	 * @return The index of the specified skill; otherwise -1.
	 */
	public static int getIndex(final String statName) {
		for (GlobalWidgetId.Skill skill : GlobalWidgetId.Skill.values()) {
			if (skill.name().contains(statName.toUpperCase())) {
				return Skill.valueOf(statName.toUpperCase()).ordinal();
			}
		}
		return -1;
	}

	/**
	 * Gets the level at the given experience.
	 *
	 * @param exp The experience.
	 * @return The level based on the experience given.
	 */
	public static int getLevelAt(final int exp) {
		for (int i = Skills.XP_TABLE.length - 1; i > 0; i--) {
			if (exp > Skills.XP_TABLE[i]) {
				return i;
			}
		}
		return 1;
	}


	/**
	 * Gets the current experience for the given skill.
	 *
	 * @param index The index of the skill.
	 * @return -1 if the skill is unavailable
	 */
	public int getCurrentExp(final int index) {
		final int[] skills = methods.client.getSkillExperiences();

		if (index > skills.length - 1) {
			return -1;
		}

		return methods.client.getSkillExperiences()[index];
	}

	/**
	 * Gets the effective level of the given skill (accounting for temporary
	 * boosts and reductions).
	 *
	 * @param index The index of the skill.
	 * @return The current level of the given Skill.
	 */
	public int getCurrentLevel(final int index) {
		return methods.client.getRealSkillLevel(getSkill(index));
	}

	/**
	 * Gets the player's current level in a skill based on their experience in
	 * that skill.
	 *
	 * @param index The index of the skill.
	 * @return The real level of the skill.
	 * @see #getRealLevel(int)
	 */
	public int getRealLevel(final int index) {
		return Skills.getLevelAt(getCurrentExp(index));
	}

	/**
	 * Gets the percentage to the next level in a given skill.
	 *
	 * @param index The index of the skill.
	 * @return The percent to the next level of the provided skill or 0 if level
	 *         of skill is 99.
	 */
	public int getPercentToNextLevel(final int index) {
		final int lvl = getRealLevel(index);
		if (lvl == 99) {
			return 0;
		}
		final int xpTotal = Skills.XP_TABLE[lvl + 1] - Skills.XP_TABLE[lvl];
		if (xpTotal == 0) {
			return 0;
		}
		final int xpDone = getCurrentExp(index) - Skills.XP_TABLE[lvl];
		return 100 * xpDone / xpTotal;
	}

	/**
	 * Gets the maximum level of a given skill.
	 *
	 * @param index The index of the skill.
	 * @return The max level of the skill.
	 */
	public int getMaxLevel(final int index) {
		return 99;
		//return methods.client.getSkillLevelMaxes()[index];
	}

	/**
	 * Gets the maximum experience of a given skill.
	 *
	 * @param index The index of the skill.
	 * @return The max experience of the skill.
	 */
	public int getMaxExp(final int index) {
		return 200000000;
		//return methods.client.getSkillExperiencesMax()[index];
	}

	/**
	 * Gets the experience remaining until reaching the next level in a given
	 * skill.
	 *
	 * @param index The index of the skill.
	 * @return The experience to the next level of the skill.
	 */
	public int getExpToNextLevel(final int index) {
		final int lvl = getRealLevel(index);
		if (lvl == 99) {
			return 0;
		}
		return Skills.XP_TABLE[lvl + 1] - getCurrentExp(index);
	}

	/**
	 * Moves the mouse over a given component in the stats tab.
	 *
	 * @param component The component index.
	 * @return <tt>true</tt> if the mouse was moved over the given component
	 *         index.
	 */
	public boolean doHover(int component) {
		methods.game.openTab(GameGUI.Tab.STATS);
		sleep(random(10, 100));
		return methods.interfaces.getComponent(GlobalWidgetId.INTERFACE_STATS, component)
				.doHover();
	}

	/**
	 * Creates a new SkillTracker.
	 *
	 * @param skills Skills to track.
	 * @return New instance of SkillTracker.
	 * @see SkillTracker
	 */
	@Deprecated
	public SkillTracker createTracker(final int... skills) {
		return new SkillTracker(methods.runeLite, skills);
	}


	/**
	 * A table containing the experiences that begin each level.
	 */
	public static final int[] XP_TABLE = {0, 0, 83, 174, 276, 388, 512, 650,
			801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523,
			3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031,
			13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408,
			33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
			83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636,
			184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599,
			407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
			899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
			1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
			3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253,
			7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431,
			14391160, 15889109, 17542976, 19368992, 21385073, 23611006,
			26068632, 28782069, 31777943, 35085654, 38737661, 42769801,
			47221641, 52136869, 57563718, 63555443, 70170840, 77474828,
			85539082, 94442737, 104273167};
}
