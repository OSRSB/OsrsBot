package rsb.util;

import net.runelite.api.Skill;
import rsb.botLauncher.RuneLite;
import rsb.methods.MethodContext;

import java.util.Arrays;


/**
 * A class for tracking experience gains in specified skills.
 *
 * The three arrays (skills, startExp, currentExp) are constantly congruent, and
 * updated as such.
 *
 * @author GigiaJ
 * @version 1.0
 */
@Deprecated
public class SkillTracker {
	public int[] skills;
	public int[] startExp, currentExp;

	private final RuneLite bot;
	private final MethodContext context;

	public transient int firstIndex;
	public transient int lastIndex;
	public long start;
	public boolean started;

	public SkillTracker(RuneLite bot, int... skills) {
		this.bot = bot;
		context = bot.getMethodContext();
		firstIndex = 0;
		int size = skills.length;
		if (size < 0) {
			throw new IllegalArgumentException();
		}
		this.skills = skills;
		for (int i = 0; i < size; i++) {
			startExp[i] = context.skills.getCurrentExp(skills[i]);
			currentExp[i] = context.skills.getCurrentExp(skills[i]);
		}
		lastIndex = size;
		start();
	}

	/**
	 * Updates all the skills within the current array and starts the time at the currentTimeMillis.
	 */
	public void start() {
		if (started) {
			return;
		}
		updateAll();
		start = System.currentTimeMillis();
		started = true;
	}

	/**
	 * Determines if the current SkillTracker is started.
	 *
	 * @return <tt>true</tt> if the tracker is started; otherwise <tt>false</tt>
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * Calculates the current runtime.
	 * @return the runtime of the tracker
	 */
	public long getRuntime() {
		return (long) (System.currentTimeMillis() - start);
	}

	/**
	 * Updates all skills within the skills array.
	 */
	public void updateAll() {
		int size = skills.length;
		for (int i = 0; i < size; i++) {
			currentExp[i] = context.skills.getCurrentExp(skills[i]);
		}
	}

	/**
	 * Updates a single skill dependent on the index of the skill.
	 * @param index	the index of the skill to update
	 */
	public void update(final int index) {
		int size = skills.length;
		if (index >= 0 && index <= size) {
			currentExp[getArrayIndex(index, skills)] = context.skills
					.getCurrentExp(index);
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Removes an item using index (of item in array). Recommended to use
	 * removeItem to remove via skill index.
	 *
	 * @param index	the skill to remove from the array
	 */
	public void remove(final int index) {
		int size = lastIndex - firstIndex;
		if (0 <= index && index < size) {
			if (index == size - 1) {
				int l = --lastIndex;
				skills[l] = 0;
				currentExp[l] = 0;
				startExp[l] = 0;
			} else if (index == 0) {
				int f = firstIndex++;
				skills[f] = 0;
				currentExp[f] = 0;
				startExp[f] = 0;
			} else {
				int trueIndex = firstIndex + index;
				if (index < size / 2) {
					System.arraycopy(skills, firstIndex, skills,
							firstIndex + 1, index);
					System.arraycopy(currentExp, firstIndex, currentExp,
							firstIndex + 1, index);
					System.arraycopy(startExp, firstIndex, startExp,
							firstIndex + 1, index);
				} else {
					System.arraycopy(skills, trueIndex + 1, skills, trueIndex,
							size - index - 1);
					System.arraycopy(currentExp, trueIndex + 1, currentExp,
							trueIndex, size - index - 1);
					System.arraycopy(startExp, trueIndex + 1, startExp,
							trueIndex, size - index - 1);
					--lastIndex;
				}
			}
			if (firstIndex == lastIndex) {
				firstIndex = lastIndex = 0;
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Removes an item from the current array.
	 *
	 * @param index Of the skill to remove.
	 * @return <tt>true</tt> If removed.
	 */
	public boolean removeSkill(final int index) {
		int realIndex = getArrayIndex(index, skills);
		if (realIndex >= 0) {
			remove(index);
			return true;
		}
		return false;
	}

	/**
	 * Adds a skill into the skills array and updates it. Start experience will
	 * be calculated during the the exact time the item is added into the array.
	 *
	 * @param skill The skill to add.
	 */
	public void add(final int skill) {
		if (lastIndex == skills.length) {
			growAtEnd(1);
		}
		int l = lastIndex++;
		skills[l] = skill;
		currentExp[l] = 0;
		startExp[l] = context.skills.getCurrentExp(skill);
		update(skill);
	}

	/**
	 * Determines if an array contains the specified index.
	 * @param index the index to check
	 * @param array	the array to check
	 * @return <tt>true</tt> if the array contains the index; otherwise <tt>false</tt>
	 */
	public boolean arrayContains(final int index, final int[] array) {
		for (final int i : array) {
			if (index == i) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines the true index for an item within the specified array.
	 * @param secondaryIndex 	the inner index in the array to check
	 * @param array				the array to check
	 * @return the true index
	 */
	public int getArrayIndex(final int secondaryIndex, final int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == secondaryIndex) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Expands the current array (skillsList) by the specified growth.
	 * @param growth	the integer amount to increase the array size by
	 */
	public void growAtEnd(final int growth) {
		int size = lastIndex - firstIndex;
		if (firstIndex >= growth - (skills.length - lastIndex)) {
			int newLast = lastIndex - firstIndex;
			if (size > 0) {
				System.arraycopy(skills, firstIndex, skills, 0, size);
				int start = newLast < firstIndex ? firstIndex : newLast;
				Arrays.fill(skills, start, skills.length, 0);
			}
			firstIndex = 0;
			lastIndex = newLast;
		} else {
			int increment = size / 2;
			if (growth > increment) {
				increment = growth;
			}
			if (increment < 12) {
				increment = 12;
			}
			int[] newArray = new int[size + increment];
			if (size > 0) {
				System.arraycopy(skills, firstIndex, newArray, 0, size);
				firstIndex = 0;
				lastIndex = size;
			}
			skills = newArray;
		}
	}

	/**
	 * Returns the main value of the skills array.
	 * @return the skills array
	 */
	public int[] toArray() {
		return skills;
	}

	/**
	 * Calculates the true gains of the skills being tracked. (Updates current
	 * experience)
	 * @return an array containing the calculated gains of the skills tracked
	 */
	public int[] getGains() {
		int size = skills.length;
		int[] gains = new int[size];
		for (int i = 0; i < size; i++) {
			update(skills[i]);
			gains[i] = currentExp[i] - startExp[i];
		}
		return gains;
	}

	/**
	 * Calculates the experience gained per hour. (Updates current experience)
	 * @return the calculated hourly gains
	 */
	public int[] getHourlyGains() {
		int size = skills.length;
		int[] gains = new int[size];
		for (int i = 0; i < size; i++) {
			update(skills[i]);
			long time = getRuntime();
			gains[i] = (time == 0L) ? 0 : (int) (3600L * (currentExp[i] - startExp[i]) / time);
		}
		return gains;
	}

	/**
	 * Retrieves the name of the indicated skill.
	 * @param skill the index to check for
	 * @return the name of the skill at a specified index
	 */
	public String getName(final int skill) {
		final int index = getArrayIndex(skill, skills);
		return Skill.values()[index].getName();
	}

	/**
	 * Calculates number of seconds until level gain
	 * @return the calculated value estimate for gaining a level
	 */
	public int[] getSecTNL() {
		final int[] gains = getHourlyGains();
		final int size = skills.length;
		int[] secTNL = new int[size];
		for (int i = 0; i < size; i++) {
			long xpTNL = context.skills.getExpToNextLevel(skills[i]);
			secTNL[i] = (gains[i] == 0) ? 0 : (int) ((3600L * xpTNL) / (long) gains[i]);
		}
		return secTNL;
	}

	/**
	 * Converts a number of seconds into a string of form hh:mm:ss
	 * @param sec the seconds to convert into the HMS format
	 * @return the hour minutes and seconds representation of the parameter
	 */
	public static String SecToHMS(int sec) {
		int min = sec / 60;
		sec %= 60;
		int hour = min / 60;
		min %= 60;
		return "" + hour + ":" + min + ":" + sec;
	}
}
