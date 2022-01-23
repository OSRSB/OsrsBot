package rsb.methods;

import rsb.internal.globval.GlobalWidgetId;
import rsb.internal.globval.GlobalWidgetInfo;
import rsb.wrappers.RSPlayer;
import rsb.wrappers.RSWidget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Trade handling.
 *
 * @author GigiaJ
 */
@SuppressWarnings("unused")
public class Trade extends MethodProvider {
	public static final int TRADE_TYPE_MAIN = 0;
	public static final int TRADE_TYPE_SECONDARY = 1;
	public static final int TRADE_TYPE_NONE = 2;

	Trade(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Are we in the first stage of a trade?
	 *
	 * @return <tt>true</tt> if in first stage.
	 */
	public boolean inTradeMain() {
		RSWidget tradeInterface = methods.interfaces.get(GlobalWidgetId.INTERFACE_TRADE_MAIN);
		return tradeInterface != null && tradeInterface.isValid();
	}

	/**
	 * Are we in the second stage of a trade?
	 *
	 * @return <tt>true</tt> if in second stage.
	 */
	public boolean inTradeSecond() {
		RSWidget tradeInterface = methods.interfaces.get(GlobalWidgetId.INTERFACE_TRADE_SECOND);
		return tradeInterface != null && tradeInterface.isValid();
	}

	/**
	 * Checks if you're in a trade.
	 *
	 * @return <tt>true</tt> if you're trading; otherwise <tt>false</tt>.
	 */
	public boolean inTrade() {
		return inTradeMain() || inTradeSecond();
	}

	/**
	 * Trades a player.
	 *
	 * @param playerName The player's name.
	 * @param tradeWait  Timeout to wait for the trade.
	 * @return <tt>true</tt> if traded.
	 */
	public boolean tradePlayer(final String playerName, final int tradeWait) {
		if (!inTrade()) {
			RSPlayer targetPlayer = methods.players.getNearest(playerName);
			if (targetPlayer != null) {
				return targetPlayer.doAction("Trade with", targetPlayer.getName()) && waitForTrade(TRADE_TYPE_MAIN, tradeWait);
			} else {
				return false;
			}
		} else {
			return isTradingWith(playerName);
		}
	}

	/**
	 * Trades a player.
	 *
	 * @param playerName The player's name.
	 * @return <tt>true</tt> if traded.
	 */
	public boolean tradePlayer(final String playerName) {
		return tradePlayer(playerName, 15000);
	}

	/**
	 * Trades a player.
	 *
	 * @param targetPlayer The player you wish to trade.
	 * @param tradeWait    The time out for the trade.
	 * @return <tt>true</tt> if traded.
	 */
	public boolean tradePlayer(final RSPlayer targetPlayer, final int tradeWait) {
		if (!inTrade()) {
			if (targetPlayer != null) {
				return targetPlayer.doAction("Trade with", targetPlayer.getName()) && waitForTrade(TRADE_TYPE_MAIN, tradeWait);
			} else {
				return false;
			}
		} else {
			return isTradingWith(targetPlayer.getName());
		}
	}

	/**
	 * Trades a player.
	 *
	 * @param targetPlayer The desired player.
	 * @return <tt>true</tt> if traded.
	 */
	public boolean tradePlayer(final RSPlayer targetPlayer) {
		return tradePlayer(targetPlayer, 15000);
	}

	/**
	 * Accepts a trade
	 *
	 * @return <tt>true</tt> on accept.
	 */
	public boolean acceptTrade() {
		if (inTradeMain()) {
			return methods.interfaces.getComponent(GlobalWidgetInfo.TRADE_MAIN_SCREEN_ACCEPT).doAction(
					"Accept");
		} else {
			return inTradeSecond() && methods.interfaces.getComponent(GlobalWidgetInfo.TRADE_SECOND_SCREEN_ACCEPT).doAction("Accept");
		}
	}

	/**
	 * Declines a trade
	 *
	 * @return <tt>true</tt> on decline
	 */
	public boolean declineTrade() {
		if (inTradeMain()) {
			return methods.interfaces.getComponent(GlobalWidgetInfo.TRADE_MAIN_SCREEN_DECLINE).doAction(
					"Decline");
		} else {
			return inTradeSecond() && methods.interfaces.getComponent(GlobalWidgetInfo.TRADE_SECOND_SCREEN_DECLINE).doAction("Decline");
		}
	}

	/**
	 * Waits for trade type to be true.
	 *
	 * @param tradeType The trade type.
	 * @param timeOut   Time out of waiting.
	 * @return <tt>true</tt> if true, otherwise false.
	 */
	public boolean waitForTrade(final int tradeType, final long timeOut) {
		long timeCounter = System.currentTimeMillis() + timeOut;
		while (timeCounter - System.currentTimeMillis() > 0) {
			switch (tradeType) {
				case TRADE_TYPE_MAIN:
					if (inTradeMain()) {
						return true;
					}
					break;
				case TRADE_TYPE_SECONDARY:
					if (inTradeSecond()) {
						return true;
					}
					break;
				case TRADE_TYPE_NONE:
					if (!inTrade()) {
						return true;
					}
					break;
			}
			sleep(5);
		}
		return false;
	}

	/**
	 * Gets who you're trading with.
	 *
	 * @return The person's name you're trading with.
	 */
	private String getTradingWith() {
		if (inTradeMain()) {
			String name = methods.interfaces.getComponent(GlobalWidgetInfo.TRADE_MAIN_SCREEN_MAIN_NAME).getText();
			return name.substring(name.indexOf(": ") + 2);
		} else if (inTradeSecond()) {
			return methods.interfaces.getComponent(GlobalWidgetInfo.TRADE_SECOND_SCREEN_SECOND_NAME).getText();
		}
		return null;
	}

	/**
	 * Checks if you're trading with someone.
	 *
	 * @param name The person's name.
	 * @return <tt>true</tt> if true; otherwise <tt>false</tt>.
	 */
	private boolean isTradingWith(String name) {
		return getTradingWith().equals(name);
	}

	/**
	 * Returns the total number of items offered by another player
	 *
	 * @return The number of items offered.
	 */
	private int getNumberOfItemsOffered() {
		int number = 0;
		for (int i = 0; i < 28; i++) {
			if (methods.interfaces.getComponent(GlobalWidgetInfo.TRADE_MAIN_SCREEN_PARTNER).getComponent(i).getStackSize() != 0) {
				++number;
			}
		}
		return number;
	}

	/**
	 * Returns the total number of free slots the other player has
	 *  ElevenGolden has 10 free inventory slots.
	 * @return The number of free slots.
	 */
	private int getFreeSlots() {
		if (inTradeMain()) {
			String text = methods.interfaces.getComponent(GlobalWidgetInfo.TRADE_MAIN_SCREEN_PARTNER_FREE_SLOTS).getText();
			//
			Matcher matcher = Pattern.compile("(has (.*\\d) free inventory slot)").matcher(text);
			while (matcher.find())
				text = matcher.group(2);
			text = text.trim();
			try {
				return Integer.parseInt(text);
			} catch (Exception e) {
			}
		}
		return 0;
	}
}
