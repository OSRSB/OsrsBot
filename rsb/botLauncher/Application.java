package net.runelite.client.rsb.botLauncher;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.util.EnumConverter;
import net.runelite.client.rs.ClientUpdateCheckMode;
import net.runelite.client.rsb.gui.BotGUI;
import net.runelite.client.rsb.internal.LogOutputStream;
import net.runelite.client.rsb.internal.SystemConsoleHandler;
import net.runelite.client.rsb.util.Extractor;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.SwingUtil;

import java.awt.*;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
	private static BotGUI gui;

	public static void main(final String[] args) throws Exception {


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
			bootstrap();
			new Extractor(args).run();
			gui = new BotGUI(args);
			gui.setFont(FontManager.getRunescapeFont());
			gui.setVisible(true);
			gui.addBot(args);
		}
		else if (options.has("bot-runelite") && !options.has("runelite")) {
			RuneLite bot = new RuneLite();
			bot.launch(args);
			bot.setMethodContext();
			RuneLiteTestFeatures.init(bot);
		}
		else {
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
		return gui.getBot(o);
	}

	/**
	 * Returns the size of the panel that clients should be drawn into. For
	 * internal use.
	 *
	 * @return The client panel size.
	 */
	public static Dimension getPanelSize() {
		if (gui != null)
			return gui.getPanel().getSize();
		return null;
	}

	private static void bootstrap() {
		Logger.getLogger("").setLevel(Level.ALL);
		Logger.getLogger("").addHandler(new SystemConsoleHandler());
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			private final Logger log = Logger.getLogger("EXCEPTION");

			public void uncaughtException(final Thread t, final Throwable e) {
				log.logp(Level.SEVERE, "EXCEPTION", "", "Unhandled exception in thread " + t.getName() + ": ", e);
			}
		});
		System.setErr(new PrintStream(new LogOutputStream(Logger.getLogger("STDERR"), Level.SEVERE), true));
	}
}
