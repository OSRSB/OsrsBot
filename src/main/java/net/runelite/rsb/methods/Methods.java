package net.runelite.rsb.methods;

import net.runelite.rsb.wrappers.*;
import net.runelite.rsb.wrappers.subwrap.ChooseOption;
import net.runelite.rsb.wrappers.subwrap.NPCChat;

import java.awt.*;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides access to methods that can be used by RSBot scripts.
 */
public class Methods {
	public MethodContext ctx;

	/**
	 * The logger instance
	 */
	protected final Logger log = Logger.getLogger(getClass().getName());
	/**
	 * The instance of {@link java.util.Random} for random number generation.
	 */
	protected static final java.util.Random random = new java.util.Random();
	/**
	 * The singleton of Skills
	 */
	protected Skills skills;
	/**
	 * The singleton of ClientLocalStorage
	 */
	protected ClientLocalStorage clientLocalStorage;
	/**
	 * The singleton of Magic
	 */
	protected Magic magic;
	/**
	 * The singleton of Bank
	 */
	protected Bank bank;
	/**
	 * The singleton of Players
	 */
	protected Players players;
	/**
	 * The singleton of Store
	 */
	protected Store store;
	/**
	 * The singleton of Camera
	 */
	protected Camera camera;
	/**
	 * The singleton of NPCs
	 */
	protected NPCs npcs;
	/**
	 * The singleton of GameScreen
	 */
	protected Game game;
	/**
	 * The singleton of Combat
	 */
	protected Combat combat;
	/**
	 * The singleton of Interfaces
	 */
	protected Interfaces interfaces;
	/**
	 * The singleton of Mouse
	 */
	protected Mouse mouse;
	/**
	 * The singleton of Keyboard
	 */
	protected Keyboard keyboard;
	/**
	 * The singleton of Menu
	 */
	protected Menu menu;
	/**
	 * The singleton of Tiles
	 */
	protected Tiles tiles;
	/**
	 * The singleton of Objects
	 */
	protected Objects objects;
	/**
	 * The singleton of Walking
	 */
	protected Walking walking;
	/**
	 * The singleton of Calculations
	 */
	protected Calculations calc;
	/**
	 * The singleton of Inventory
	 */
	protected Inventory inventory;
	/**
	 * The singleton of Equipment
	 */
	protected Equipment equipment;
	/**
	 * The singleton of GroundItems
	 */
	protected GroundItems groundItems;
	/**
	 * The singleton of Account
	 */
	protected Account account;
	/**
	 * The singleton of Environment
	 */
	protected Environment env;
	/**
	 * The singleton of Prayer
	 */
	protected Prayer prayer;
	/**
	 * The singleton of Trade
	 */
	protected Trade trade;
	/**
	 * The singleton of Web
	 */
	protected Web web;
	/**
	 * The singleton of GrandExchange
	 */
	protected GrandExchange grandExchange;
	/**
	 * The singleton of WorldHopper
	 */
	protected WorldHopper worldHopper;
	/**
	 * The singleton of ChooseOption
	 */
	protected ChooseOption chooseOption;
	/**
	 *  The singleton of NPCChat
	 */
	protected NPCChat npcChat;

	/**
	 * For internal use only: initializes the method providers.
	 *
	 * @param ctx The MethodContext.
	 */
	public void init(MethodContext ctx) {
		this.ctx = ctx;
		this.skills = ctx.skills;
		this.clientLocalStorage = ctx.clientLocalStorage;
		this.magic = ctx.magic;
		this.bank = ctx.bank;
		this.players = ctx.players;
		this.store = ctx.store;
		this.camera = ctx.camera;
		this.npcs = ctx.npcs;
		this.game = ctx.game;
		this.grandExchange = ctx.grandExchange;
		this.combat = ctx.combat;
		this.interfaces = ctx.interfaces;
		this.mouse = ctx.mouse;
		this.keyboard = ctx.keyboard;
		this.menu = ctx.menu;
		this.tiles = ctx.tiles;
		this.objects = ctx.objects;
		this.walking = ctx.walking;
		this.calc = ctx.calc;
		this.inventory = ctx.inventory;
		this.equipment = ctx.equipment;
		this.groundItems = ctx.groundItems;
		this.account = ctx.account;
		this.env = ctx.env;
		this.prayer = ctx.prayer;
		this.web = ctx.web;
		this.trade = ctx.trade;
		this.worldHopper = ctx.worldHopper;
		this.chooseOption = ctx.chooseOption;
		this.npcChat = ctx.npcChat;
	}

	/**
	 * Returns the current client's local player.
	 *
	 * @return The current client's <code>RSPlayer</code>.
	 * @see Players#getMyPlayer()
	 */
	public RSPlayer getMyPlayer() {
		return players.getMyPlayer();
	}

	/**
	 * Returns a random integer with min as the inclusive lower bound and max as
	 * the exclusive upper bound.
	 *
	 * @param min The inclusive lower bound.
	 * @param max The exclusive upper bound.
	 * @return Random integer min (less than or equal) to n (less than) max.
	 */
	public static int random(int min, int max) {
		int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random.nextInt(n));
	}

	/**
	 * Checks for the existence of a NPC.
	 *
	 * @param npc The NPC to check for.
	 * @return <code>true</code> if found.
	 */
	public boolean verify(RSNPC npc) {
		return npc != null;
	}

	/**
	 * Checks for the existence of a RSObject.
	 *
	 * @param o The RSObject to check for.
	 * @return <code>true</code> if found.
	 */
	public boolean verify(RSObject o) {
		return o != null;
	}

	/**
	 * Checks for the existence of a RSTile.
	 *
	 * @param t The RSTile to check for.
	 * @return <code>true</code> if found.
	 */
	public boolean verify(RSTile t) {
		return t != null;
	}

	/**
	 * Checks for the existence of a RSGroundItem.
	 *
	 * @param i The RSGroundItem to check for.
	 * @return <code>true</code> if found.
	 */
	public boolean verify(RSGroundItem i) {
		return i != null;
	}

	/**
	 * Returns a random double with min as the inclusive lower bound and max as
	 * the exclusive upper bound.
	 *
	 * @param min The inclusive lower bound.
	 * @param max The exclusive upper bound.
	 * @return Random double min (less than or equal) to n (less than) max.
	 */
	public static double random(double min, double max) {
		return Math.min(min, max) + random.nextDouble() * Math.abs(max - min);
	}

	/**
	 * Pauses execution for a random amount of time between two values.
	 *
	 * @param minSleep The minimum time to sleep.
	 * @param maxSleep The maximum time to sleep.
	 * @see #sleep(int)
	 * @see #random(int, int)
	 */
	public static boolean sleep(int minSleep, int maxSleep) {
		return sleep(random(minSleep, maxSleep));
	}

	/**
	 * Pauses execution for a given number of milliseconds.
	 *
	 * @param toSleep The time to sleep in milliseconds.
	 */
	public static boolean sleep(int toSleep) {
		try {
			long start = System.currentTimeMillis();
			Thread.sleep(toSleep);

			// Guarantee minimum sleep
			long now;
			while (start + toSleep > (now = System.currentTimeMillis())) {
				Thread.sleep(start + toSleep - now);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Pauses execution for a random amount of time between two values or when supplier returns true.
	 *
	 * @param minSleep The minimum time to sleep.
	 * @param maxSleep The maximum time to sleep.
	 * @param stopCondition a function which returns true when sleep should be aborted
	 * @see #sleep(int)
	 * @see #random(int, int)
	 */
	public static boolean sleep(int minSleep, int maxSleep, BooleanSupplier stopCondition) {
		return sleep(random(minSleep, maxSleep), stopCondition);
	}

	/**
	 * Pauses execution for a given number of milliseconds or when supplier returns true.
	 *
	 * @param toSleep The time to sleep in milliseconds.
	 * @param stopCondition a function which returns true when sleep should be aborted
	 */
	public static boolean sleep(int toSleep, BooleanSupplier stopCondition) {
		long start = System.currentTimeMillis();
		try {
			while (start + toSleep > System.currentTimeMillis()) {
				if (stopCondition.getAsBoolean()) {
					return true;
				}
				Thread.sleep(random.nextLong(15, 55));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	/**
	 * Prints to the RSBot log.
	 *
	 * @param message Object to log.
	 */
	public void log(Object message) {
		log.info(message.toString());
	}

	/**
	 * Prints to the RSBot log with a font color
	 *
	 * @param color   The color of the font
	 * @param message Object to log
	 */
	public void log(Color color, Object message) {
		Object[] parameters = {color};
		log.log(Level.INFO, message.toString(), parameters);
	}
}
