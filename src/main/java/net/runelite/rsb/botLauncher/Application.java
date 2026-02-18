/**
 * @Author: GigiaJ
 * The main application class that hosts all the bot instances
 */
package net.runelite.rsb.botLauncher;

import lombok.extern.slf4j.Slf4j;
import net.runelite.rsb.internal.globval.GlobalConfiguration;
import net.runelite.rsb.wrappers.common.CacheProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map;

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
		preParser = new ArgumentPreParser(args);
		if (preParser.contains("--bot-runelite")) {
			loadJagexCredentials();
			String scriptName = preParser.consumeValue("--script");
			addBot(preParser.contains("--headless"));
			if (scriptName != null) {
				autoStartScript(scriptName);
			}
			CLIHandler.handleCLI();
		} else {
			net.runelite.client.RuneLite.main(args);
		}
	}

	/**
	 * Auto-starts a script after the player is logged in.
	 * Polls game state in a background thread, waiting for LOGGED_IN before launching.
	 *
	 * @param scriptName Script name with spaces removed (e.g. "PixelBot-Woodcutting")
	 */
	private static void autoStartScript(String scriptName) {
		new Thread(() -> {
			log.info("Auto-start queued for script: {} — log in within 45 seconds", scriptName);
			try {
				// Wait for client to initialize and user to log in
				Thread.sleep(45000);
				log.info("Starting script: {}", scriptName);
				BotLiteInterface bot = getBots()[0];
				bot.runScript("default", scriptName);
				log.info("Auto-started script: {}", scriptName);
			} catch (Exception e) {
				log.error("Failed to auto-start script: {}", scriptName, e);
			}
		}, "ScriptAutoStart").start();
	}

	/**
	 * Loads Jagex Launcher credentials from ~/.runelite/credentials.properties
	 * and injects them as real environment variables so the game client
	 * can authenticate via System.getenv("JX_ACCESS_TOKEN") etc.
	 *
	 * The launcher scripts (launch-bot.bat/sh) also set these as env vars
	 * before starting Java. This method is a fallback for direct JAR launch.
	 *
	 * To generate credentials.properties:
	 * 1. Open "RuneLite (configure)" and add --insecure-write-credentials to client arguments
	 * 2. Launch RuneLite via Jagex Launcher and log in
	 * 3. credentials.properties will be created in ~/.runelite/
	 */
	private static void loadJagexCredentials() {
		File credFile = new File(System.getProperty("user.home"), ".runelite/credentials.properties");
		if (!credFile.exists()) {
			log.info("No credentials.properties found at {}. See launch-bot.bat for setup instructions.", credFile.getAbsolutePath());
			return;
		}
		try (FileInputStream fis = new FileInputStream(credFile)) {
			Properties creds = new Properties();
			creds.load(fis);
			int envSet = 0;
			for (Map.Entry<Object, Object> entry : creds.entrySet()) {
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				// Set as system property (accessible via System.getProperty)
				System.setProperty(key, value);
				// Also inject as environment variable (accessible via System.getenv)
				if (setEnvironmentVariable(key, value)) {
					envSet++;
				}
				log.info("Loaded Jagex credential: {}", key);
			}
			log.info("Loaded {} credentials from {} ({} set as env vars)", creds.size(), credFile.getAbsolutePath(), envSet);
		} catch (IOException e) {
			log.warn("Failed to load credentials.properties", e);
		}
	}

	/**
	 * Injects a key-value pair into the current process's environment variables
	 * via reflection. This is needed because System.getenv() returns an unmodifiable
	 * map, but the RuneLite client reads JX_* tokens from environment variables.
	 *
	 * @return true if successfully set, false if reflection failed
	 */
	@SuppressWarnings("unchecked")
	private static boolean setEnvironmentVariable(String key, String value) {
		// Skip if already set (e.g. by launcher script)
		if (value.equals(System.getenv(key))) {
			return true;
		}
		try {
			// On Windows, ProcessEnvironment has a theCaseInsensitiveEnvironment field
			Class<?> processEnvClass = Class.forName("java.lang.ProcessEnvironment");
			try {
				Field theEnvironmentField = processEnvClass.getDeclaredField("theEnvironment");
				theEnvironmentField.setAccessible(true);
				Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
				env.put(key, value);
			} catch (NoSuchFieldException e) {
				// Fallback: try the unmodifiable map's backing field
				Map<String, String> env = System.getenv();
				Field field = env.getClass().getDeclaredField("m");
				field.setAccessible(true);
				((Map<String, String>) field.get(env)).put(key, value);
			}
			return true;
		} catch (Exception e) {
			log.debug("Could not set env var {} via reflection (expected on some JDKs). Use launch-bot.bat/sh instead.", key);
			return false;
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
		//TODO Checking if the directories are empty or missing files. Do a diff against the expected cache.
		//Add a version check

		if ((!new File(itemCacheLocation).exists()) || new File(itemCacheLocation).getTotalSpace() < 100) {
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

		/**
		 * Consumes a key-value argument pair (e.g. --script PixelBot-Woodcutting).
		 * Removes both the key and value from the list and returns the value.
		 *
		 * @param key The argument key (e.g. "--script")
		 * @return The value following the key, or null if not found
		 */
		public String consumeValue(String key) {
			int index = indexOf(key);
			if (index >= 0 && index + 1 < size()) {
				remove(index); // remove key
				return remove(index); // remove and return value (now at same index)
			} else if (index >= 0) {
				remove(index); // remove orphan key
			}
			return null;
		}

	}

}
