/**
 * @Author: GigiaJ
 * The main application class that hosts all the bot instances
 */
package rsb.botLauncher;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

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
