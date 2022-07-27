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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@Slf4j
public class Application {

	static BotLiteInterface[] bots = new BotLiteInterface[]{};
	static String[] programArgs = new String[]{};

	/**
	 * Parses the command-line arguments and then passes the parsed arguments in the form of the parser, optionSpecs,
	 * and options to a constructor to create an instance of the RuneLite(Bot) class
	 * 
	 * @param args			The command line arguments for the program
	 * @throws Throwable	Any error that might be thrown
	 */
	public static void main(final String[] args) throws Throwable {
		ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
		if (argList.contains("--bot-runelite")) {
			argList.remove("--bot-runelite");
			programArgs = argList.toArray(new String[]{});
			addBot(argList.contains("--headless"));
			checkForCacheAndLoad();
				new Thread(() -> {
					Scanner input = new Scanner(System.in);
					while(input.hasNext()) {
						String[] command = input.nextLine().split(" ");
						System.out.println(Arrays.toString(command));
						if (command[0].equalsIgnoreCase("runScript")) {
							BotLiteInterface botInterface = Application.getBots()[Integer.parseInt(command[1])];
							botInterface.runScript(command[2], command[3]);

						}
						if (command[0].equalsIgnoreCase("addBot")) {
							addBot(true);
						}
						if (command[0].equalsIgnoreCase("checkState")) {
							for (BotLiteInterface botInstance : bots) {
								System.out.println(botInstance.getClass().getClassLoader());
							}
						}
					}
				}).start();
		} else {
			net.runelite.client.RuneLite.main(args);
		}
	}

	/**
	 * Checks if the cache exists and if it does, loads it
	 * if not it creates a new cache and saves it
	 *
	 * @throws IOException
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
		BotLiteInterface bot;
		String[] args = new String[programArgs.length + 1];
		if (headless) {
			System.arraycopy(programArgs, 0, args, 0, programArgs.length);
			args[programArgs.length] = "--headless";
		}
		else {
			args = programArgs;
		}
		BotClassLoader loader = new BotClassLoader("BotLoader" + bots.length);
		// Class<?> c;
		try {
			// c = loader.loadClass("net.runelite.client.modified.RuneLite");
			// bot = (BotLiteInterface) c.getConstructor().newInstance();
			BotLite.launch(args);
			BotLiteInterface[] update = new BotLiteInterface[bots.length + 1];
			for (int i = 0; i < bots.length; i++) {
				update[i] = bots[i];
			}
			update[bots.length] = BotLite.getInjector().getInstance(BotLite.class);
			bots = update;
		} catch (Exception e) {
			log.error("Error while starting bot", e);
		}
	}

	/**
	 * Retrieves all running bot instances
	 * @return	the bot instances
	 */
	public static BotLiteInterface[] getBots() {
		return bots;
	}

}
