/**
 * @Author: GigiaJ
 * The main application class that hosts all the bot instances
 */
package net.runelite.rsb.botLauncher;

import lombok.extern.slf4j.Slf4j;
import net.runelite.rsb.internal.globval.GlobalConfiguration;
import net.runelite.rsb.wrappers.common.CacheProvider;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
public class Application {

	static BotLiteInterface[] bots = new BotLiteInterface[]{};
	static ArgumentPreParser preParser;

	/**
	 * Parses the command-line arguments and then passes the parsed arguments in the form of the parser, optionSpecs,
	 * and options to a constructor to create an instance of the RuneLite(Bot) class
	 * 
	 * @param args			The command line arguments for the program
	 * @throws Throwable	Any error that might be thrown
	 */
	public static void main(final String[] args) throws Throwable {
		JnREPL.startRepl();
		preParser = new ArgumentPreParser(args);
		if (preParser.contains("--bot-runelite")) {
			addBot(preParser.contains("--headless"));
			checkForCacheAndLoad();
			CLIHandler.handleCLI();
		} else {
			net.runelite.client.RuneLite.main(args);
		}
	}

	/**
	 * Checks if the cache exists and if it does, loads it
	 * if not it creates a new cache and saves it
	 *
	 * @throws IOException If the file isn't found or is inaccessible then an IOException has occurred.
	 */
	private static void checkForCacheAndLoad() throws IOException {
		String gameCacheLocation = GlobalConfiguration.Paths.getRuneLiteGameCacheDirectory();
		String objectCacheLocation = GlobalConfiguration.Paths.getObjectsCacheDirectory();
		String itemCacheLocation = GlobalConfiguration.Paths.getItemsCacheDirectory();
		String npcCacheLocation = GlobalConfiguration.Paths.getNPCsCacheDirectory();
		String spriteCacheLocation = GlobalConfiguration.Paths.getSpritesCacheDirectory();
		//TODO Some sort of better validation here
		//Add a version check
		if (!new File(itemCacheLocation).exists() && new File(itemCacheLocation).getTotalSpace() < 100) {
			String[] itemArgs = {"--cache", gameCacheLocation,
					"--items", itemCacheLocation};
			String[] objectArgs = {"--cache", gameCacheLocation,
					"--objects", objectCacheLocation};
			String[] npcArgs = {"--cache", gameCacheLocation,
					"--npcs", npcCacheLocation};
			String[] spriteArgs = {"--cache", gameCacheLocation,
					"--sprites", spriteCacheLocation};

			net.runelite.cache.Cache.main(itemArgs);
			net.runelite.cache.Cache.main(objectArgs);
			net.runelite.cache.Cache.main(npcArgs);
			if (!new File(spriteCacheLocation).exists()) {
				new File(spriteCacheLocation).mkdir();
				net.runelite.cache.Cache.main(spriteArgs);
			}
		}
		else {
			CacheProvider.fillFileCache();
		}
	}

	public static void setBot(int index) {
		BotLiteInterface bot = getBots()[index];
	}

	/**
	 * Returns the Bot for any object loaded in its client. For internal use
	 * only (not useful for script writers).
	 *
	 * @param o Any object from within the client.
	 * @return The Bot for the client.
	 */
	public static BotLiteInterface getBot(Object o) {
		ClassLoader cl = o.getClass().getClassLoader();
		for (BotLiteInterface bot : bots) {
			if (cl == bot.getClass().getClassLoader()) {
				return bot;
			}
		}
		return null;
	}

	/**
	 * Adds a bot to the bot array
	 *
	 * @param headless To run the bot headless or not
	 */
	public static void addBot(boolean headless) {
		BotLiteInterface bot = null;

		try {
			if (headless) {
				preParser.add("--headless");
				BotClassLoader loader = new BotClassLoader("BotLoader" + bots.length + 1);
				Class<?> c;
				c = loader.loadClass("net.runelite.rsb.botLauncher.BotLite");
				bot = (BotLiteInterface) c.getConstructor().newInstance();
			} else {
				preParser.remove("--headless");
				bot = new BotLite();
			}
			bot.launch(preParser.asArgs());
		} catch (Exception e) {
			log.error("Error while starting bot", e);
		}

		BotLiteInterface[] update = new BotLiteInterface[bots.length + 1];
		System.arraycopy(bots, 0, update, 0, bots.length);
		update[bots.length] = bot;
		bots = update;
	}

	/**
	 * Retrieves all running bot instances
	 * @return	the bot instances
	 */
	public static BotLiteInterface[] getBots() {
		return bots;
	}

	/**
	 * A class to handle bot related arguments before passing them off to RuneLite
	 */
	private static class ArgumentPreParser extends ArrayList<String> {

		/**
		 * Creates a handler for the arguments before they're sent off to build the bot instance of RuneLite
		 * @param args			The command line arguments for the program
		 */
		public ArgumentPreParser(String[] args) {
			super(List.of(args));
		}

		/**
		 * Returns the program argument ArrayList as a built-in array
		 * @return	a built-in String array containing program arguments
		 */
		public String[] asArgs() {
			return this.toArray(new String[0]);
		}

		/**
		 * Modifies the contains function to remove elements upon checking if they exist within, but return the whether
		 * they did before-hand.
		 * As CLI args for the bot will be used once this will only pass to RuneLite arguments meant for it.
		 */
		@Override
		public boolean contains(Object o) {
			int index = indexOf(o);
			boolean within = index >= 0;
			if (within)
				this.remove(index);
			return within;
		}

	}

}
