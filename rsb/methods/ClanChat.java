package net.runelite.client.rsb.methods;


import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.rsb.wrappers.RSWidget;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Friend chat related operations.
 *
 * @author Debauchery
 */
public class ClanChat extends MethodProvider {

	ClanChat(final MethodContext ctx) {
		super(ctx);
	}

	private static final Pattern HTML_TAG = Pattern.compile("(^[^<]+>|<[^>]+>|<[^>]+$)");
	public static final int INTERFACE_FRIEND_CHAT = 7;
	public static final int INTERFACE_FRIEND_CHAT_JOIN_BUTTON = 22;
	public static final int INTERFACE_FRIEND_CHAT_OWNER = WidgetInfo.CLAN_CHAT_OWNER.getChildId();
	public static final int INTERFACE_FRIEND_CHAT_NAME = WidgetInfo.CLAN_CHAT_NAME.getChildId();
	public static final int INTERFACE_FRIEND_CHAT_USERS_LIST = WidgetInfo.CLAN_CHAT_LIST.getChildId();
	public static final int INTERFACE_JOIN_FRIEND_CHAT = WidgetInfo.CHATBOX.getGroupId();
	public static final int INTERFACE_JOIN_FRIEND_CHAT_LAST_CHANNEL = WidgetInfo.CHATBOX_CONTAINER.getChildId();

	private String lastCachedChannel = null;

	/**
	 * Joins the given channel.
	 * If we are already in a channel, it will leave it.
	 *
	 * @param channel The channel to join
	 * @return <tt>true</tt> if successful; otherwise <tt>false</tt>
	 */
	public boolean join(String channel) {
		methods.game.openTab(Game.TAB_CLAN_CHAT);
		if (isInChannel()) {
			if (getChannelName() == channel) {
				return true;
			}
			if (!leave()) {
				return false;
			}
		}
		methods.interfaces.getComponent(INTERFACE_FRIEND_CHAT, INTERFACE_FRIEND_CHAT_JOIN_BUTTON).doClick();
		sleep(random(500, 800));
		if (methods.interfaces.get(INTERFACE_JOIN_FRIEND_CHAT).isValid()) {
			String lastChatCompText = methods.interfaces.getComponent(INTERFACE_JOIN_FRIEND_CHAT,
					INTERFACE_JOIN_FRIEND_CHAT_LAST_CHANNEL).getComponent(0).getText();
			lastCachedChannel = lastChatCompText.substring(lastChatCompText.indexOf(": ") + 2);
			methods.keyboard.sendText(channel, true);
			sleep(random(1550, 1800));
			if (isInChannel()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Joins the given channel.
	 * If we are already in a channel, it will leave it.
	 *
	 * @return <tt>true</tt> if successful; otherwise <tt>false</tt>
	 */
	public boolean joinLastChannel() {
		methods.game.openTab(Game.TAB_CLAN_CHAT);
		if (isInChannel()) {
			return true;
		}
		methods.interfaces.getComponent(INTERFACE_FRIEND_CHAT, INTERFACE_FRIEND_CHAT_JOIN_BUTTON).doClick();
		sleep(random(500, 800));
		if (methods.interfaces.get(INTERFACE_JOIN_FRIEND_CHAT).isValid()) {
			String lastChatCompText = methods.interfaces.getComponent(INTERFACE_JOIN_FRIEND_CHAT,
					INTERFACE_JOIN_FRIEND_CHAT_LAST_CHANNEL).getComponent(0).getText();
			lastCachedChannel = lastChatCompText.substring(lastChatCompText.indexOf(": ") + 2);
			methods.interfaces.getComponent(INTERFACE_JOIN_FRIEND_CHAT,
					INTERFACE_JOIN_FRIEND_CHAT_LAST_CHANNEL).doClick();
			sleep(random(1550, 1800));
			if (isInChannel()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Leaves the current channel.
	 *
	 * @return <tt>true</tt> if successful; otherwise <tt>false</tt>
	 */
	public boolean leave() {
		methods.game.openTab(Game.TAB_CLAN_CHAT);
		if (isInChannel()) {
			lastCachedChannel = getChannelOwner();
			methods.interfaces.getComponent(
					INTERFACE_FRIEND_CHAT, INTERFACE_FRIEND_CHAT_JOIN_BUTTON).doClick();
			sleep(random(650, 900));
			return isInChannel();
		}
		return true;
	}

	/**
	 * Returns whether or not we're in a channel.
	 *
	 * @return <tt>true</tt> if in a channel; otherwise <tt>false</tt>
	 */
	public boolean isInChannel() {
		methods.game.openTab(Game.TAB_CLAN_CHAT);
		if (getChannelName() != null) {
			lastCachedChannel = getChannelName();
		}
		return methods.interfaces.getComponent(INTERFACE_FRIEND_CHAT, INTERFACE_FRIEND_CHAT_JOIN_BUTTON).containsAction(
				"Leave chat");
	}

	/**
	 * Gets the users in the channel.
	 *
	 * @return The users in the channel or null if unavailable
	 */
	public String[] getChannelUsers() {
		ArrayList<String> tempList = new ArrayList<String>();
		if (methods.game.getCurrentTab() != Game.TAB_CLAN_CHAT) {
			methods.game.openTab(Game.TAB_CLAN_CHAT);
		}
		if (methods.game.getCurrentTab() == Game.TAB_CLAN_CHAT) {
			if (getChannelName() != null) {
				lastCachedChannel = getChannelName();
			}
			if (methods.interfaces.getComponent(INTERFACE_FRIEND_CHAT, INTERFACE_FRIEND_CHAT_USERS_LIST) != null) {
				for (RSWidget comp : methods.interfaces.getComponent(INTERFACE_FRIEND_CHAT,
						INTERFACE_FRIEND_CHAT_USERS_LIST).getComponents()) {
					if (comp.getText() != null) {
						tempList.add(comp.getText().trim());
					} else {
						break;
					}
				}
			}
		}
		{
			String[] temp = new String[tempList.size()];
			tempList.toArray(temp);
			return temp;
		}
	}

	/**
	 * Gets the name of the channel.
	 *
	 * @return The name of the channel or null if none
	 */
	public String getChannelName() {
		try {
			methods.game.openTab(Game.TAB_CLAN_CHAT);
			String name = stripFormatting(methods.interfaces.getComponent(
					INTERFACE_FRIEND_CHAT, INTERFACE_FRIEND_CHAT_NAME).getText());
			return name;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gets the owner of the channel.
	 *
	 * @return The owner of the channel or null if none
	 */
	public String getChannelOwner() {
		try {
			methods.game.openTab(Game.TAB_CLAN_CHAT);
			if (getChannelName() != null) {
				lastCachedChannel = getChannelName();
			}
			String name = stripFormatting(methods.interfaces.getComponent(
					INTERFACE_FRIEND_CHAT, INTERFACE_FRIEND_CHAT_OWNER).getText());
			return name;
		} catch (Exception e) {
			return null;
		}
	}

	public String getLastCachedChannel() {
		return lastCachedChannel;
	}

	/**
	 * Strips HTML tags.
	 *
	 * @param input The string you want to parse.
	 * @return The parsed {@code String}.
	 */
	private String stripFormatting(String input) {
		return HTML_TAG.matcher(input).replaceAll("");
	}
}
