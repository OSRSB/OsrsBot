package rsb.methods;

import net.runelite.api.VarPlayer;

/**
 * Provides access to game settings.
 */
public class Settings extends MethodProvider {

	Settings(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Gets the settings array.
	 *
	 * @return An <tt>int</tt> array representing all of the settings values;
	 *         otherwise <tt>new int[0]</tt>.
	 */
	public int[] getSettingArray() {
		int[] settingArray = methods.client.getVarps();
		if (settingArray == null) {
			return new int[0];
		}
		return settingArray.clone(); // NEVER return pointer
	}

	/**
	 * Gets the setting at a given index.
	 *
	 * @param setting The setting index to return the value of.
	 * @return <tt>int</tt> representing the setting of the given setting id;
	 *         otherwise <tt>-1</tt>.
	 */
	public int getSetting(final int setting) {
		int[] settings = getSettingArray();
		if (setting < settings.length) {
			return settings[setting];
		}
		return -1;
	}

}
