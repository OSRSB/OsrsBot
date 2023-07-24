package net.runelite.rsb.methods;

/**
 * Provides access to game local storage which holds VarPlayers, VarBits and VarClients
 */
public class ClientLocalStorage extends MethodProvider {

	ClientLocalStorage(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Gets the values of loaded VarPlayer as integer array.
	 *
	 * @return An <code>int</code> array representing all loaded VarPlayer values;
	 * otherwise <code>new int[0]</code>.
	 */
	public int[] getLoadedVarpValues() {
		int[] varpValuesArray = methods.client.getVarps();
		if (varpValuesArray == null) {
			return new int[0];
		}
		return varpValuesArray.clone(); // NEVER return pointer
	}

	/**
	 * Gets the value of VarPlayer at a given index.
	 *
	 * @param varpIndex The VarPlayer index to return the value of.
	 * @return <code>int</code> representing the value of the given VarPlayer index;
	 * otherwise <code>-1</code>.
	 */
	public int getVarpValueAt(final int varpIndex) {
		int[] varpValuesArray = getLoadedVarpValues();
		if (varpIndex < varpValuesArray.length) {
			return varpValuesArray[varpIndex];
		}
		return -1;
	}
}
