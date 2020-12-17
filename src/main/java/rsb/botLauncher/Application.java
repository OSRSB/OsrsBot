package rsb.botLauncher;

import javassist.*;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.util.EnumConverter;
import joptsimple.ValueConverter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.rs.ClientUpdateCheckMode;
import rsb.internal.LogOutputStream;
import rsb.internal.SystemConsoleHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

	static RuneLite[] bots = new RuneLite[]{};

	public static void main(final String[] args) throws Throwable {
		final OptionParser parser = new OptionParser();
		parser.accepts("developer-mode", "Enable developer tools");
		parser.accepts("debug", "Show extra debugging output");
		parser.accepts("bot", "Starts the client in bot mode");
		parser.accepts("runelite", "Starts the client in bot mode");
		parser.accepts("bot-runelite", "Starts the client in RuneLite mode");

		final ArgumentAcceptingOptionSpec<ClientUpdateCheckMode> updateMode = parser
				.accepts("rs", "Select client type")
				.withRequiredArg()
				.ofType(ClientUpdateCheckMode.class)
				.defaultsTo(ClientUpdateCheckMode.AUTO)
				.withValuesConvertedBy(new EnumConverter<ClientUpdateCheckMode>(ClientUpdateCheckMode.class) {
					@Override
					public ClientUpdateCheckMode convert(String v) {
						return super.convert(v.toUpperCase());
					}
				});

		parser.accepts("help", "Show this text").forHelp();
		OptionSet options = parser.parse(args);
		if (options.has("bot") && !options.has("bot-runelite") && !options.has("runelite")) {


		} else if (options.has("bot-runelite") && !options.has("runelite")) {
			RuneLite bot = new RuneLite();
			bot.launch(args);
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

	public static RuneLite[] getBots() {
		return bots;
	}


	public static String test() {
		return "Test String";
	}
}
