/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.rsb.botLauncher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Constructor;
import java.util.*;
import javax.inject.Provider;
import javax.inject.Singleton;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.util.EnumConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ClientSessionManager;
import net.runelite.client.RuneLiteModule;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.account.SessionManager;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.CommandManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.discord.DiscordService;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.game.ClanManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.LootManager;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.rs.ClientUpdateCheckMode;
import net.runelite.client.rsb.event.EventManager;
import net.runelite.client.rsb.event.events.PaintEvent;
import net.runelite.client.rsb.event.events.TextPaintEvent;
import net.runelite.client.rsb.gui.AccountManager;
import net.runelite.client.rsb.internal.BreakHandler;
import net.runelite.client.rsb.internal.PassiveScriptHandler;
import net.runelite.client.rsb.internal.ScriptHandler;
import net.runelite.client.rsb.methods.*;
import net.runelite.client.rsb.internal.InputManager;
import net.runelite.client.rsb.util.OutputObjectComparer;
import net.runelite.client.rsb.wrappers.RSNPC;
import net.runelite.client.rsb.wrappers.RSPlayer;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayRenderer;
import net.runelite.client.ui.overlay.WidgetOverlay;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxOverlay;
import net.runelite.client.ui.overlay.tooltip.TooltipOverlay;
import net.runelite.client.ui.overlay.worldmap.WorldMapOverlay;
import net.runelite.client.ws.PartyService;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;

@Singleton
@Slf4j
public class RuneLite extends net.runelite.client.RuneLite {
    public static final File RUNELITE_DIR = new File(System.getProperty("user.home"), ".runelite");
    public static final File PROFILES_DIR = new File(RUNELITE_DIR, "profiles");
    public static final File SCREENSHOT_DIR = new File(RUNELITE_DIR, "screenshots");

    @Getter
    private static Injector injector;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private EventBus eventBus;

    @Inject
    private ConfigManager configManager;

    @Inject
    private DrawManager drawManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private DiscordService discordService;

    @Inject
    private ClientSessionManager clientSessionManager;

    @Inject
    private ClientUI clientUI;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private PartyService partyService;

    @Inject
    private Provider<ItemManager> itemManager;

    @Inject
    private Provider<OverlayRenderer> overlayRenderer;

    @Inject
    private Provider<ClanManager> clanManager;

    @Inject
    private Provider<ChatMessageManager> chatMessageManager;

    @Inject
    private Provider<MenuManager> menuManager;

    @Inject
    private Provider<CommandManager> commandManager;

    @Inject
    private Provider<InfoBoxOverlay> infoBoxOverlay;

    @Inject
    private Provider<TooltipOverlay> tooltipOverlay;

    @Inject
    private Provider<WorldMapOverlay> worldMapOverlay;

    @Inject
    private Provider<LootManager> lootManager;

    @Inject
    private Provider<ChatboxPanelManager> chatboxPanelManager;

    private Client client;

    private String account;
    private MethodContext methods;
    private Component panel;
    private PaintEvent paintEvent;
    private TextPaintEvent textPaintEvent;
    private EventManager eventManager;
    private BufferedImage backBuffer;
    private BufferedImage image;
    private final InputManager im;
    private ScriptHandler sh;
    private PassiveScriptHandler psh;
    private BreakHandler bh;
    private Map<String, EventListener> listeners;
    private boolean kill_passive = false;

    /**
     * Defines what types of input are enabled when overrideInput is false.
     * Defaults to 'keyboard only' whenever a script is started.
     */
    public volatile int inputFlags = Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE;

    /**
     * Whether or not user input is allowed despite a script's preference.
     */
    public volatile boolean overrideInput = false;

    /**
     * Whether or not all anti-randoms are enabled.
     */
    public volatile boolean disableRandoms = false;

    /**
     * Whether or not the login screen anti-random is enabled.
     */
    public volatile boolean disableAutoLogin = false;

    /**
     * Whether or not rendering is enabled.
     */
    public volatile boolean disableRendering = false;

    /**
     * Whether or not the canvas is enabled.
     */
    public volatile boolean disableCanvas = false;


    public void changeCanvasState() {
        if (disableCanvas) {
            getLoader().setVisible(false);
            return;
        }
        getLoader().setVisible(true);
    }

    public String getAccountName() {
        return account;
    }

    public Client getClient() {
        return client = injector.getInstance(Client.class);
    }

    public ItemManager getItemManager() { return injector.getInstance(ItemManager.class);}

    public MethodContext getMethodContext() {
        return methods;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public InputManager getInputManager() {
        return im;
    }

    public BreakHandler getBreakHandler() {
        return bh;
    }

    public ScriptHandler getScriptHandler() {
        return sh;
    }

    public PassiveScriptHandler getPassiveScriptHandler() {
        return psh;
    }

    public void addListener(Class<?> clazz) {
        EventListener el = instantiateListener(clazz);
        listeners.put(clazz.getName(), el);
        eventManager.addListener(el);
    }

    public void removeListener(Class<?> clazz) {
        EventListener el = listeners.get(clazz.getName());
        listeners.remove(clazz.getName());
        eventManager.removeListener(el);
    }

    private EventListener instantiateListener(Class<?> clazz) {
        try {
            EventListener listener;
            try {
                Constructor<?> constructor = clazz.getConstructor(RuneLite.class);
                listener = (EventListener) constructor.newInstance(this);
            } catch (Exception e) {
                listener = clazz.asSubclass(EventListener.class).newInstance();
            }
            return listener;
        } catch (Exception ignored) {
        }
        return null;
    }

    public boolean hasListener(Class<?> clazz) {
        return clazz != null && listeners.get(clazz.getName()) != null;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Component getPanel() {
        return this.panel;
    }

    public void setPanel(Component c) {
        this.panel = c;
    }

    public boolean setAccount(final String name) {
        boolean exist = false;
        for (String s : AccountManager.getAccountNames()) {
            if (s.toLowerCase().equals(name.toLowerCase())) {
                exist = true;
            }
        }
        if (exist) {
            account = name;
            return true;
        }
        account = null;
        return false;
    }


    public ClientUI getClientUI() {
        if (client == null) {
            return null;
        }
        return clientUI;
    }

    public Canvas getCanvas() {
        if (client == null) {
            return null;
        }
        return client.getCanvas();
    }

    public Applet getLoader() {
        return (Applet) client;
    }

    public RuneLite() {
        im = new InputManager(this);
        if (Application.getPanelSize() != null) {
            final Dimension size = Application.getPanelSize();
            backBuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        }
        psh = new PassiveScriptHandler(this);
        eventManager = new EventManager();
        sh = new ScriptHandler(this);
        bh = new BreakHandler(this);
        paintEvent = new PaintEvent();
        textPaintEvent = new TextPaintEvent();
        listeners = new TreeMap<>();
    }

    public static void launch(String[] args) throws Exception {
        Locale.setDefault(Locale.ENGLISH);

        final OptionParser parser = new OptionParser();
        parser.accepts("developer-mode", "Enable developer tools");
        parser.accepts("debug", "Show extra debugging output");
        parser.accepts("bot", "Starts the client in bot mode");
        parser.accepts("bot-runelite", "Starts the client in Bot RuneLite mode");

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

        if (options.has("help")) {
            parser.printHelpOn(System.out);
            System.exit(0);
        }

        final boolean developerMode = options.has("developer-mode") && RuneLiteProperties.getLauncherVersion() == null;

        PROFILES_DIR.mkdirs();

        if (options.has("debug")) {
            final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            logger.setLevel(Level.DEBUG);
        }

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
        {
            log.error("Uncaught exception:", throwable);
            if (throwable instanceof AbstractMethodError) {
                log.error("Classes are out of date; Build with maven again.");
            }
        });

        final long start = System.currentTimeMillis();

        injector = Guice.createInjector(new RuneLiteModule(
                options.valueOf(updateMode),
                developerMode));

        if (options.has("bot-runelite")) {
            setInjector();
            injector.getInstance(RuneLite.class).start();
        }
        else {
            injector.getInstance(RuneLite.class).init();
        }

        final long end = System.currentTimeMillis();
        final RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        final long uptime = rb.getUptime();
        log.info("Client initialization took {}ms. Uptime: {}ms", end - start, uptime);
    }

    public void init() {
        // Load RuneLite or Vanilla client
        final boolean isOutdated = client == null;

        if (!isOutdated) {
            // Inject members into client
            injector.injectMembers(client);

        }
            eventManager.start();
            synchronized (this) {
                if (this.client != null) {
                    this.notify();

                }

        }
    }

    public void setMethodContext() {
        methods = new MethodContext(this);
        methods.bank.assignConstants();
        methods.menu.assignMethods();
    }

    public void shutdown() {
        getLoader().stop();
        getLoader().setVisible(false);
        eventManager.killThread(false);
        sh.stopScript();
        psh.stopScript();
        kill_passive = true;
    }

    public static void setInjector() {
        setInjector(RuneLite.injector);
    }

}
