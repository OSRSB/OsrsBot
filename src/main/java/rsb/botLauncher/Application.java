/**
 * @Author: GigiaJ
 * The main application class that hosts all the bot instances
 */
package rsb.botLauncher;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.extern.slf4j.Slf4j;
import rsb.internal.globval.GlobalConfiguration;
import rsb.plugin.Botplugin;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Application {

	static RuneLite[] bots = new RuneLite[]{};

	/**
	 * Parses the command-line arguments and then passes the parsed arguments in the form of the parser, optionSpecs,
	 * and options to a constructor to create an instance of the RuneLite(Bot) class
	 * 
	 * @param args			The command line arguments for the program
	 * @throws Throwable	Any error that might be thrown
	 */
	public static void main(final String[] args) throws Throwable {
		final OptionParser parser = new OptionParser();
		ArgumentAcceptingOptionSpec<?>[] optionSpecs = RuneLite.handleParsing(parser);
		OptionSet options = parser.parse(args);

		checkForCacheAndLoad();

		if (!options.has("bot") || options.has("bot-runelite") || options.has("runelite")) {
			if (options.has("bot-runelite") && !options.has("runelite")) {
				RuneLite bot = new RuneLite();
				bot.launch(parser, optionSpecs, options);
				addBot(bot);
			} else {
				String[] nonBotArgs = new String[args.length];
				int o = 0;
				for (int i = 0; i < args.length; i++) {
					if (!args[i].equals("--runelite")) {
						nonBotArgs[o] = args[i];
						o++;
					}
					if (nonBotArgs[i] == null) {
						nonBotArgs[i] = "";
					}
				}
				net.runelite.client.RuneLite.main(nonBotArgs);
			}
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


		String[] itemArgs = {"--cache", gameCacheLocation,
				"--items", itemCacheLocation};
		String[] objectArgs = {"--cache", gameCacheLocation,
				"--objects", objectCacheLocation};
		String[] npcArgs = {"--cache", gameCacheLocation,
				"--npcs", npcCacheLocation};
		String[] spriteArgs = {"--cache", gameCacheLocation,
				"--sprites", spriteCacheLocation};

		//TODO Some sort of better validation here
		if (!new File(itemCacheLocation).exists() && new File(itemCacheLocation).getTotalSpace() < 100) {
			net.runelite.cache.Cache.main(itemArgs);
			net.runelite.cache.Cache.main(objectArgs);
			net.runelite.cache.Cache.main(npcArgs);
			if (!new File(spriteCacheLocation).exists()) {
				new File(spriteCacheLocation).mkdir();
				net.runelite.cache.Cache.main(spriteArgs);
			}
		}
	}

	public static void setBot(int index) {
		RuneLite bot = getBots()[index];
		try {
			bot.init();
			bot.bareStart();
		} catch (Throwable e) {
			log.error("Error while starting bot", e);
		}

	}

	/**
	 * Returns the Bot for any object loaded in its client. For internal use
	 * only (not useful for script writers).
	 *
	 * @param o Any object from within the client.
	 * @return The Bot for the client.
	 */
	public static RuneLite getBot(Object o) {
		ClassLoader cl = o.getClass().getClassLoader();
		for (RuneLite bot : bots) {
			if (cl == bot.getClass().getClassLoader()) {
				return bot;
			}
		}
		return null;
	}

	/**
	 * Adds a bot to the bot array
	 *
	 * @param bot the bot to be added to the array
	 */
	public static void addBot(RuneLite bot) {
		RuneLite[] update = new RuneLite[bots.length + 1];
		for (int i = 0; i < bots.length; i++) {
			update[i] = bots[i];
		}
		update[bots.length] = bot;
		bots = update;
	}

	/**
	 * Retrieves all running bot instances
	 * @return	the bot instances
	 */
	public static RuneLite[] getBots() {
		return bots;
	}

}
