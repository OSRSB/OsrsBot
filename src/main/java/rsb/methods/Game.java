package rsb.methods;

import net.runelite.api.GameState;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import rsb.script.Random;
import rsb.script.randoms.*;
import rsb.internal.globval.GlobalWidgetId;
import rsb.wrappers.*;

import java.awt.*;
import java.awt.event.KeyEvent;
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
		RSWidget messages = methods.interfaces.getComponent(WidgetInfo.CHATBOX_MESSAGES);
			if (!messages.getDynamicComponent(GlobalWidgetId.DYNAMIC_CHAT_BOX_FIRST_MESSAGE).getText().isEmpty()) {
				if (messages.getDynamicComponent(GlobalWidgetId.DYNAMIC_CHAT_BOX_LATEST_MESSAGE).isVisible()
						&& !messages.getDynamicComponent(GlobalWidgetId.DYNAMIC_CHAT_BOX_LATEST_MESSAGE).getText().isEmpty())
					return messages.getDynamicComponent(GlobalWidgetId.DYNAMIC_CHAT_BOX_LATEST_MESSAGE).getText();
				return messages.getDynamicComponent(GlobalWidgetId.DYNAMIC_CHAT_BOX_FIRST_MESSAGE).getText();
			}
		return "";
	}

	/**
	 * Opens the specified tab at the specified index.
	 *
	 * @param tab The tab to open.
	 * @return <tt>true</tt> if tab successfully selected; otherwise
	 *         <tt>false</tt>.
	 * @see #openTab(GameGUI.Tab tab, boolean functionKey)
	 */
	public boolean openTab(GameGUI.Tab tab) {
		return openTab(tab, false);
	}

	/**
	 * Opens the specified tab at the specified index.
	 *
	 * @param tab 				The tab to open
	 * @param functionKey       If wanting to use function keys to switch.
	 * @return <tt>true</tt> if tab successfully selected; otherwise
	 *         <tt>false</tt>.
	 */
	public boolean openTab(GameGUI.Tab tab, boolean functionKey) {
		// Check current tab
		if (tab == getCurrentTab()) {
			return true;
		}

		if (functionKey) {
			if (tab.getFunctionKey() == 0) {
				return false;// no function key for specified tab
			}

			methods.keyboard.pressKey((char) tab.getFunctionKey());
			sleep(random(80, 200));
			methods.keyboard.releaseKey((char) tab.getFunctionKey());
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
		GameGUI.Tab tab = getCurrentTab();
		if (methods.gui.isFixed() || tab == GameGUI.Tab.LOGOUT) {
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
	@SuppressWarnings("JavadocReference")
	public boolean mouseChatButton(int button, boolean left) {
		RSWidget chatButton = methods.interfaces.get(WidgetID.CHATBOX_GROUP_ID).getComponent(button);
		return chatButton.isValid() && chatButton.doClick(left);
	}

	/**
	 * Gets the currently open tab.
	 *
	 * @return The currently open tab or the logout tab by default.
	 */
	public GameGUI.Tab getCurrentTab() {
		for (GameGUI.Tab i : GameGUI.Tab.values()) {
				net.runelite.api.widgets.Widget tab = methods.gui.getTab(i);
				if (tab == null) {
					continue;
				}

				if (tab.getSpriteId() != -1) {
					return i;
				}
			}
		return GameGUI.Tab.LOGOUT; // no selected ones. (never happens, always return TAB_LOGOUT
	}

	/**
	 * Gets the current run energy.
	 *
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
	 */
	public RSWidget getTalkInterface() {
		for (RSWidget component : methods.interfaces.getComponent(WidgetInfo.CHATBOX_FULL_INPUT).getComponents()) {
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
	 * @return <tt>true</tt> if on the logout tab.
	 */
	public boolean isOnLogoutTab() {
		return getCurrentTab() == GameGUI.Tab.LOGOUT;
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
			GameGUI.Tab currentTab = methods.game.getCurrentTab();
			GameGUI.Tab randomTab = GameGUI.Tab.values()[random(1, 6)];
			while (randomTab == currentTab) {
				randomTab = GameGUI.Tab.values()[random(1, 6)];
			}
			methods.game.openTab(randomTab);
			sleep(random(400, 800));
		}
		if (methods.inventory.isItemSelected()) {
			return false;
		}

		if (!isOnLogoutTab()) {
			openTab(GameGUI.Tab.LOGOUT);
			sleep(random(300, 600));
		}

		methods.interfaces.getComponent(WidgetInfo.LOGOUT_BUTTON).doClick();
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