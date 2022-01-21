package rsb.methods;

import net.runelite.api.GameState;
import net.runelite.api.widgets.WidgetInfo;
import rsb.script.Random;
import rsb.script.randoms.*;
import rsb.wrappers.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;



/**
 * Game state and GUI operations.
 */
public class Game extends MethodProvider {

	/**
	 * Different Types of Chat Modes
	 *
	 */
	public enum ChatMode {
		VIEW, ON, FRIENDS, OFF, HIDE, AUTOCHAT, STANDARD, CLEAR, SWITCH
	}
	
	public static int TAB_ATTACK;
	public static int TAB_STATS;
	public static int TAB_QUESTS;
	public static int TAB_INVENTORY;
	public static int TAB_EQUIPMENT;
	public static int TAB_PRAYER;
	public static int TAB_MAGIC;
	public static int TAB_FRIENDS;
	public static int TAB_CLAN_CHAT;
	public static int TAB_OPTIONS;
	public static int TAB_MUSIC;
	public static int TAB_LOGOUT;

	public static int[] TABS =
	{
		TAB_ATTACK = WidgetInfo.FIXED_VIEWPORT_COMBAT_TAB.getChildId(),
		TAB_STATS = WidgetInfo.FIXED_VIEWPORT_STATS_TAB.getChildId(),
		TAB_QUESTS = WidgetInfo.FIXED_VIEWPORT_QUESTS_TAB.getChildId(),
		TAB_INVENTORY = WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB.getChildId(),
		TAB_EQUIPMENT = WidgetInfo.FIXED_VIEWPORT_EQUIPMENT_TAB.getChildId(),
		TAB_PRAYER = WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB.getChildId(),
		TAB_MAGIC = WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB.getChildId(),
		TAB_FRIENDS = WidgetInfo.FIXED_VIEWPORT_FRIENDS_TAB.getChildId(),
		TAB_OPTIONS = WidgetInfo.FIXED_VIEWPORT_OPTIONS_TAB.getChildId(),
		TAB_MUSIC = WidgetInfo.FIXED_VIEWPORT_MUSIC_TAB.getChildId(),
		TAB_LOGOUT = WidgetInfo.FIXED_VIEWPORT_LOGOUT_TAB.getChildId()
	};

	public static final String[] TAB_NAMES = new String[]{"Combat Styles", "Stats", "Quest Journals", "Inventory",
			"Worn Equipment", "Prayer List", "Magic Spellbook",
			"Friends List", "Clan Chat", "Options",
			"Music Player", "Exit"};
	
	public static final int INDEX_LOGIN_SCREEN = 3;
	public static final int INDEX_LOBBY_SCREEN = 7;
	public static final int[] INDEX_LOGGED_IN = {10, 11};
	public static final int INDEX_FIXED = 746;

	public static final int[] TAB_FUNCTION_KEYS = {KeyEvent.VK_F5, // Attack
			0, // Achievements
			0, // Stats
			0, // Quests
			KeyEvent.VK_F1, // Inventory
			KeyEvent.VK_F2, // Equipment
			KeyEvent.VK_F3, // Prayer
			KeyEvent.VK_F4, // Magic
			0, // Summoning
			0, // Friends
			0, // Ignore
			0, // Clan
			0, // Options
			0, // Controls
			0, // Music
			0, // Notes
			0, // Logout
	};


	public static final int CHAT_OPTION = WidgetInfo.CHATBOX.getGroupId();

	public static final int INTERFACE_CHAT_BOX = 137;
	public static final int INTERFACE_GAME_SCREEN = 548;
	public static final int INTERFACE_LEVEL_UP = 740;
	public static final int INTERFACE_LOGOUT_BUTTON_FIXED = 181;
	public static final int INTERFACE_LOGOUT_BUTTON_RESIZED = 172;
	public static final int INTERFACE_WELCOME_SCREEN = 907;
	public static final int INTERFACE_WELCOME_SCREEN_CHILD = 150;
	public static final int INTERFACE_WELCOME_SCREEN_PLAY = 18;



	public static final int CHATBOX_TALK_INTERFACE = 561;

	Game(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Set the specified chat mode
	 *
	 * @param chatOption one of CHAT_OPTION_
	 * @param mode       one of ChatMode
	 * @return <tt>true</tt> if item was clicked correctly; otherwise
	 *         <tt>false</tt>
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
		final int CHAT_MESSAGES = 58;
		RSWidget messages = methods.interfaces.get(CHAT_OPTION, CHAT_MESSAGES);
			if (!messages.getDynamicComponent(0).getText().isEmpty()) {
				if (messages.getDynamicComponent(1).isVisible() && !messages.getDynamicComponent(1).getText().isEmpty())
					return messages.getDynamicComponent(1).getText();
				return messages.getDynamicComponent(0).getText();
			}
		return "";
	}

	/**
	 * Opens the specified tab at the specified index.
	 *
	 * @param tab The tab to open.
	 * @return <tt>true</tt> if tab successfully selected; otherwise
	 *         <tt>false</tt>.
	 * @see #openTab(int tab, boolean functionKey)
	 */
	public boolean openTab(int tab) {
		return openTab(tab, false);
	}

	/**
	 * Opens the specified tab at the specified index.
	 *
	 * @param tab         The tab to open, functionKey if wanting to use function keys
	 *                    to switch.
	 * @param functionKey Use a function key for fast switching?
	 * @return <tt>true</tt> if tab successfully selected; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean open(final int tab, final boolean functionKey) {
		/*
				   * Only attempts by fn key if there is a valid hotkey available Returns
				   * faster when the new tab has been selected
				   */
		if (tab == getCurrentTab()) {
			return true;
		}
		if (functionKey && tab < TAB_FUNCTION_KEYS.length && TAB_FUNCTION_KEYS[tab] != 0) {
			methods.keyboard.pressKey((char) TAB_FUNCTION_KEYS[tab]);
			sleep(random(80, 200));
			methods.keyboard.releaseKey((char) TAB_FUNCTION_KEYS[tab]);
		} else {
			net.runelite.api.widgets.Widget iTab = methods.gui.getTab(tab);
			if (iTab == null) {
				return false;
			}
			methods.interfaces.getComponent(WidgetInfo.TO_GROUP(iTab.getParent().getId()), WidgetInfo.TO_CHILD(iTab.getId())).doClick();
		}
		for (int i = 0; i < 4; i++) {
			if (tab == getCurrentTab()) {
				break;
			}
			sleep(random(100, 150));
		}
		return tab == getCurrentTab();
	}

	/**
	 * Opens the specified tab at the specified index.
	 *
	 * @param tab The tab to open, functionKey if wanting to use function keys
	 *            to switch.
	 * @return <tt>true</tt> if tab successfully selected; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean openTab(int tab, boolean functionKey) {
		// Check current tab
		if (tab == getCurrentTab()) {
			return true;
		}

		if (functionKey) {
			if (tab >= TAB_FUNCTION_KEYS.length || TAB_FUNCTION_KEYS[tab] == 0) {
				return false;// no function key for specified tab
			}

			methods.keyboard.pressKey((char) TAB_FUNCTION_KEYS[tab]);
			sleep(random(80, 200));
			methods.keyboard.releaseKey((char) TAB_FUNCTION_KEYS[tab]);
		} else {
			net.runelite.api.widgets.Widget iTab = methods.gui.getTab(tab);
			if (iTab == null) {
				return false;
			}
			methods.interfaces.getComponent(WidgetInfo.TO_GROUP(iTab.getParent().getId()), WidgetInfo.TO_CHILD(iTab.getId())).doClick();
		}

		sleep(random(400, 600));
		return tab == getCurrentTab();
	}

	/**
	 * Closes the currently open tab if in resizable mode.
	 */
	public void closeTab() {
		int tab = getCurrentTab();
		if (isFixed() || tab == TAB_LOGOUT) {
			return;
		}
		net.runelite.api.widgets.Widget iTab = methods.gui.getTab(tab);
		if (iTab != null) {
			methods.interfaces.getComponent(WidgetInfo.TO_GROUP(iTab.getParent().getId()), WidgetInfo.TO_CHILD(iTab.getId())).doClick();
		}
	}

	/**
	 * Click chat button.
	 *
	 * @param button Which button? One of CHAT_OPTION
	 * @param left   Left or right button? Left = true. Right = false.
	 * @return <tt>true</tt> if it was clicked.
	 */
	public boolean mouseChatButton(int button, boolean left) {
		RSWidget chatButton = methods.interfaces.get(CHAT_OPTION).getComponent(button);
		return chatButton.isValid() && chatButton.doClick(left);
	}

	/**
	 * Gets the currently open tab.
	 *
	 * @return The currently open tab or the logout tab by default.
	 */
	public int getCurrentTab() {
		for (int i : TABS) {
				net.runelite.api.widgets.Widget tab = methods.gui.getTab(i);
				if (tab == null) {
					continue;
				}

				if (tab.getSpriteId() != -1) {
					return i;
				}
			}
		return -1; // no selected ones. (never happens, always return TAB_LOGOUT
	}

	/**
	 * Gets the current run energy.
	 * <p/>
	 * Deprecated : use walking.getEnergy()
	 *
	 * @return An <tt>int</tt> representation of the players current energy.
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
	 */
	public Boolean inRandom() {

		for (Random random : methods.runeLite.getScriptHandler().getRandoms()) {
			if (random.getClass().equals(new LoginBot()))
					//|| random.getClass().equals(new BankPins())
					//|| random.getClass().equals(new TeleotherCloser())
					//|| random.getClass().equals(new CloseAllInterface())
					//|| random.getClass().equals(new ImprovedRewardsBox())) {
			{
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
	 * @return <tt>RSWidgetChild</tt> of the current valid talk interface;
	 *         otherwise null.
	 * @see #INTERFACE_TALKS
	 */
	public RSWidget getTalkInterface() {
		for (RSWidget component : methods.interfaces.getComponent(CHAT_OPTION, CHATBOX_TALK_INTERFACE).getComponents()) {
			if (component.isValid() && component.isVisible())
				return component;
		}
		return null;
	}

	public int getCrosshairState() {
		return 0;
	}

	/**
	 * Checks whether or not the logout tab is selected.
	 *
	 * @return <tt>true</tt> if on the logout tab.
	 */
	public boolean isOnLogoutTab() {
		return getCurrentTab() == TAB_LOGOUT;
	}

	/**
	 * Closes the bank if it is open and logs out.
	 *
	 * @return <tt>true</tt> if the player was logged out.
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
			int currentTab = methods.game.getCurrentTab();
			int randomTab = random(1, 6);
			while (randomTab == currentTab) {
				randomTab = random(1, 6);
			}
			methods.game.openTab(randomTab);
			sleep(random(400, 800));
		}
		if (methods.inventory.isItemSelected()) {
			return false;
		}

		if (!isOnLogoutTab()) {
			openTab(TAB_LOGOUT);
			sleep(random(300, 600));
		}


		//It exists in Widget ID but is 6 instead of the correct value of 8
		methods.interfaces.getComponent(WidgetInfo.LOGOUT_BUTTON.getGroupId(), 8).doClick();
		// Final logout button in the logout tab
		sleep(random(1500, 2000));
		return !isLoggedIn();
	}

	/**
	 * Runs the LoginBot random.
	 *
	 * @return <tt>true</tt> if random was run; otherwise <tt>false</tt>.
	 */
	public boolean login() {
		return new LoginBot().activateCondition();
	}

	/**
	 * Determines whether or no the client is currently in the fixed display
	 * mode.
	 *
	 * @return <tt>true</tt> if in fixed mode; otherwise <tt>false</tt>.
	 */
	public boolean isFixed() {
		return true;//!methods.client.isResized();
	}

	/**
	 * Determines whether or not the client is currently logged in to an
	 * account.
	 *
	 * @return <tt>true</tt> if logged in; otherwise <tt>false</tt>.
	 */
	public boolean isLoggedIn() {
		return methods.client.getLocalPlayer() != null;
	}

	/**
	 * Determines whether or not the client is showing the login screen.
	 *
	 * @return <tt>true</tt> if the client is showing the login screen;
	 *         otherwise <tt>false</tt>.
	 */
	public boolean isLoginScreen() {
		return methods.client.getLocalPlayer() == null;
	}

	/**
	 * Determines whether or not the welcome screen is open.
	 *
	 * @return <tt>true</tt> if the client is showing the welcome screen;
	 *         otherwise <tt>false</tt>.
	 */
	public boolean isWelcomeScreen() {
		return methods.interfaces.get(WidgetInfo.LOGIN_CLICK_TO_PLAY_SCREEN.getGroupId())
				.getComponent(WidgetInfo.LOGIN_CLICK_TO_PLAY_SCREEN_MESSAGE_OF_THE_DAY.getChildId()).getAbsoluteY() > 2;
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