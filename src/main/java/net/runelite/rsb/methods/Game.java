package net.runelite.rsb.methods;

import net.runelite.api.GameState;
import net.runelite.api.widgets.WidgetID;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.VarcIntIndices;
import net.runelite.rsb.internal.globval.VarcIntValues;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.internal.globval.enums.ViewportLayout;
import net.runelite.rsb.script.Random;
import net.runelite.rsb.script.randoms.*;
import net.runelite.rsb.wrappers.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Game state and GUI operations.
 */
public class Game extends MethodProvider {

	Game(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Different Types of Chat Modes
	 *
	 */
	public enum ChatMode {
		VIEW, ON, FRIENDS, OFF, HIDE, AUTOCHAT, STANDARD, CLEAR, SWITCH
	}

	/**
	 * Set the specified chat mode
	 *
	 * @param chatOption one of CHAT_OPTION_
	 * @param mode       one of ChatMode
	 * @return <code>true</code> if item was clicked correctly; otherwise
	 *         <code>false</code>
	 */
	public boolean setChatOption(int chatOption, ChatMode mode) {
		mouseChatButton(chatOption, false);
		return methods.menu.doAction(mode.toString());
	}

	/**
	 * Access the last message spoken by a player.
	 *
	 * @return The last message spoken by a player or "" if none
	 */
	public String getLastMessage() {
		RSWidget messages = methods.interfaces.getComponent(GlobalWidgetInfo.CHATBOX_MESSAGES);
			if (!messages.getDynamicComponent(WidgetIndices.ChatBox.FIRST_MESSAGE_LABEL).getText().isEmpty()) {
				if (messages.getDynamicComponent(WidgetIndices.ChatBox.LAST_MESSAGE_LABEL).isVisible()
						&& !messages.getDynamicComponent(WidgetIndices.ChatBox.LAST_MESSAGE_LABEL).getText().isEmpty())
					return messages.getDynamicComponent(WidgetIndices.ChatBox.LAST_MESSAGE_LABEL).getText();
				return messages.getDynamicComponent(WidgetIndices.ChatBox.FIRST_MESSAGE_LABEL).getText();
			}
		return "";
	}

	/**
	 * Opens the specified tab at the specified index.
	 *
	 * @param tab The tab to open.
	 * @return <code>true</code> if tab successfully selected; otherwise
	 *         <code>false</code>.
	 * @see #openTab(InterfaceTab tab, boolean functionKey)
	 */
	public boolean openTab(InterfaceTab tab) {
		return openTab(tab, false);
	}

	/**
	 * Opens the specified tab at the specified index.
	 *
	 * @param interfaceTab The tab to open
	 * @param useHotkey If wanting to use hotkeys to switch.
	 * @return <code>true</code> if tab successfully selected; otherwise
	 *         <code>false</code>.
	 */
	public boolean openTab(InterfaceTab interfaceTab, boolean useHotkey) {
		if (interfaceTab == getCurrentTab()) { return true; }
		if (useHotkey) {
			if (interfaceTab.getHotkey() == 0) { return false; } // no hotkey for specified tab
			methods.keyboard.pressKey((char) interfaceTab.getHotkey());
			sleep(random(80, 200));
			methods.keyboard.releaseKey((char) interfaceTab.getHotkey());
		} else {
			net.runelite.api.widgets.Widget tabWidget = methods.gui.getTab(interfaceTab);
			if (tabWidget == null) { return false; }
			methods.interfaces.getComponent(
					GlobalWidgetInfo.TO_GROUP(tabWidget.getParent().getId()),
					GlobalWidgetInfo.TO_CHILD(tabWidget.getId())).doClick();
		}
		sleep(random(400, 600));
		return interfaceTab == getCurrentTab();
	}

	/**
	 * Closes the currently open tab if in resizable mode.
	 */
	public void closeTab() {
		InterfaceTab interfaceTab = getCurrentTab();
		if (methods.gui.getViewportLayout() == ViewportLayout.FIXED_CLASSIC || interfaceTab == InterfaceTab.LOGOUT) {
			return;
		}
		net.runelite.api.widgets.Widget tabWidget = methods.gui.getTab(interfaceTab);
		if (tabWidget != null) {
			methods.interfaces.getComponent(
					GlobalWidgetInfo.TO_GROUP(tabWidget.getParent().getId()),
					GlobalWidgetInfo.TO_CHILD(tabWidget.getId())).doClick();
		}
	}

	/**
	 * Click chat button.
	 *
	 * @param button Which button? One of CHAT_OPTION
	 * @param left   Left or right button? Left = true. Right = false.
	 * @return <code>true</code> if it was clicked.
	 */
	public boolean mouseChatButton(int button, boolean left) {
		RSWidget chatButton = methods.interfaces.get(WidgetID.CHATBOX_GROUP_ID).getComponent(button);
		return chatButton.isValid() && chatButton.doClick(left);
	}

	/**
	 * Gets the currently open tab.
	 *
	 * @return The currently open interfaceTab if tab recognized else null;
	 */
	public InterfaceTab getCurrentTab() {
		int varcIntValue = methods.client.getVarcIntValue(VarcIntIndices.CURRENT_INTERFACE_TAB);
		return switch (VarcIntValues.valueOf(varcIntValue)) {
			case TAB_COMBAT_OPTIONS -> InterfaceTab.COMBAT;
			case TAB_SKILLS -> InterfaceTab.SKILLS;
			case TAB_QUEST_LIST -> InterfaceTab.QUESTS;
			case TAB_INVENTORY -> InterfaceTab.INVENTORY;
			case TAB_WORN_EQUIPMENT -> InterfaceTab.EQUIPMENT;
			case TAB_PRAYER -> InterfaceTab.PRAYER;
			case TAB_SPELLBOOK -> InterfaceTab.MAGIC;
			case TAB_FRIEND_LIST -> InterfaceTab.FRIENDS;
			case TAB_LOGOUT -> InterfaceTab.LOGOUT;
			case TAB_SETTINGS -> InterfaceTab.SETTINGS;
			case TAB_MUSIC -> InterfaceTab.MUSIC;
			case TAB_CHAT_CHANNEL -> InterfaceTab.CHAT;
			case TAB_ACC_MANAGEMENT -> InterfaceTab.ACC_MAN;
			case TAB_EMOTES -> InterfaceTab.EMOTES;
			default -> throw new IllegalStateException("Unexpected value: " + VarcIntValues.valueOf(varcIntValue));
		};
	}

	/**
	 * Gets the current run energy.
	 *
	 * Deprecated : use walking.getEnergy()
	 *
	 * @return An <code>int</code> representation of the players current energy.
	 */
	@Deprecated
	public int getEnergy() {
		return methods.walking.getEnergy();
	}

	/**
	 * Excludes Loginbot, BankPin, TeleotherCloser, CloseAllInterface,
	 * ImprovedRewardsBox
	 *
	 * @return True if player is in a random
	 * TODO: this feels broken
	 */
	public Boolean inRandom() {
		for (Random random : methods.runeLite.getScriptHandler().getRandoms()) {
			if (random.getClass().equals(new LoginBot())) {
					//|| random.getClass().equals(new BankPins())
					//|| random.getClass().equals(new TeleotherCloser())
					//|| random.getClass().equals(new CloseAllInterface())
					//|| random.getClass().equals(new ImprovedRewardsBox())) {
				continue;
			} else {
				if (random.activateCondition()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the valid chat component.
	 *
	 * @return <code>RSWidgetChild</code> of the current valid talk interface;
	 *         otherwise null.
	 */
	public RSWidget getTalkInterface() {
		for (RSWidget component : methods.interfaces.getComponent(GlobalWidgetInfo.CHATBOX_FULL_INPUT).getComponents()) {
			if (component.isValid() && component.isVisible())
				return component;
		}
		return null;
	}

	/**
	 * TODO: This is non-functional
	 * @return	-1 as the method is non-functional
	 */
	public int getCrosshairState() {
		return -1;
	}

	/**
	 * Checks whether or not the logout tab is selected.
	 *
	 * @return <code>true</code> if on the logout tab.
	 */
	public boolean isOnLogoutTab() {
		return getCurrentTab() == InterfaceTab.LOGOUT;
	}

	/**
	 * Closes the bank if it is open and logs out.
	 *
	 * @return <code>true</code> if the player was logged out.
	 */
	public boolean logout() {
		if (methods.bank.isOpen()) {
			methods.bank.close();
			sleep(random(200, 400));
		}
		if (methods.bank.isOpen()) {
			return false;
		}
		if (methods.inventory.isItemSelected()) {
			InterfaceTab currentTab = methods.game.getCurrentTab();
			InterfaceTab randomTab = InterfaceTab.values()[random(1, 6)];
			while (randomTab == currentTab) {
				randomTab = InterfaceTab.values()[random(1, 6)];
			}
			methods.game.openTab(randomTab);
			sleep(random(400, 800));
		}
		if (methods.inventory.isItemSelected()) {
			return false;
		}

		if (!isOnLogoutTab()) {
			openTab(InterfaceTab.LOGOUT);
			sleep(random(300, 600));
		}

		methods.interfaces.getComponent(GlobalWidgetInfo.LOGOUT_BUTTON).doClick();
		// Final logout button in the logout tab
		sleep(random(1500, 2000));
		return !isLoggedIn();
	}

	/**
	 * Runs the LoginBot random.
	 *
	 * @return <code>true</code> if random was run; otherwise <code>false</code>.
	 */
	public boolean login() {
		return new LoginBot().activateCondition();
	}


	/**
	 * Determines whether or not the client is currently logged in to an
	 * account.
	 *
	 * @return <code>true</code> if logged in; otherwise <code>false</code>.
	 */
	public boolean isLoggedIn() {
		return methods.client.getLocalPlayer() != null;
	}

	/**
	 * Determines whether or not the client is showing the login screen.
	 *
	 * @return <code>true</code> if the client is showing the login screen;
	 *         otherwise <code>false</code>.
	 */
	public boolean isLoginScreen() {
		return methods.client.getLocalPlayer() == null;
	}

	/**
	 * Determines whether or not the welcome screen is open.
	 *
	 * @return <code>true</code> if the client is showing the welcome screen;
	 *         otherwise <code>false</code>.
	 */
	public boolean isWelcomeScreen() {
		return methods.interfaces.getComponent(GlobalWidgetInfo.LOGIN_MOTW_TEXT)
				.getAbsoluteY() > 2;
	}

	/**
	 * Gets the game state.
	 *
	 * @return The game state.
	 */
	public GameState getClientState() {
		return methods.client.getGameState();
	}

	/**
	 * Gets the plane we are currently on. Typically 0 (ground level), but will
	 * increase when going up ladders. You cannot be on a negative plane. Most
	 * dungeons/basements are on plane 0 elsewhere on the world map.
	 *
	 * @return The current plane.
	 */
	public int getPlane() {
		return methods.client.getPlane();
	}

	/**
	 * Gets the x coordinate of the loaded map area (far west).
	 *
	 * @return The region base x.
	 */
	public int getBaseX() {
		return methods.client.getBaseX();
	}

	/**
	 * Gets the y coordinate of the loaded map area (far south).
	 *
	 * @return The region base y.
	 */
	public int getBaseY() {
		return methods.client.getBaseY();
	}

	/**
	 * Gets the (x, y) coordinate pair of the south-western tile at the base of
	 * the loaded map area.
	 *
	 * @return The region base tile.
	 */
	public RSTile getMapBase() {
		return new RSTile(methods.client.getBaseX(), methods.client.getBaseY(), methods.client.getPlane());
	}

	/**
	 * Gets the flags relating to the tiles in the scene
	 *
	 * @return the flags for all the tiles in the current scene
	 */
	public byte[][][] getSceneFlags() {
		return methods.client.getTileSettings();
	}

	/**
	 * Gets the canvas height.
	 *
	 * @return The canvas' width.
	 */
	public int getWidth() {
		return methods.runeLite.getCanvas().getWidth();
	}

	/**
	 * Gets the canvas height.
	 *
	 * @return The canvas' height.
	 */
	public int getHeight() {
		return methods.runeLite.getCanvas().getHeight();
	}

	/**
	 * Gets a color corresponding to x and y co ordinates from the current game screen.
	 *
	 * @param x: The x co ordinate at which to get the color.
	 * @param y: The y co ordinate at which to get the color.
	 * @return Color
	 * @see java.awt.color
	 */
	public Color getColorAtPoint(int x, int y) {
		BufferedImage image = methods.env.takeScreenshot(false);
		return new Color(image.getRGB(x, y));
	}
}