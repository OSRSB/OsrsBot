package rsb.methods;

import rsb.plugin.AccountManager;

import java.util.logging.Logger;

/**
 * Selected account information.
 */
public class Account extends MethodProvider {

	Logger log = Logger.getLogger(getClass().getName());

	public Account(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * The account name.
	 *
	 * @return The currently selected account's name.
	 */
	public String getName() {
		return methods.runeLite.getAccountName();
	}

	/**
	 * The account password.
	 *
	 * @return The currently selected account's password.
	 */
	public String getPassword() {
		return AccountManager.getPassword(getName());
	}

	/**
	 * The account pin.
	 *
	 * @return The currently selected account's pin.
	 */
	public String getPin() {
		return AccountManager.getPin(getName());
	}

	/**
	 * The account reward.
	 *
	 * @return The currently selected account's reward.
	 */
	public String getReward() {
		return AccountManager.getReward(getName());
	}

}
