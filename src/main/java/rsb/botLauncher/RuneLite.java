/**
 * Author:GigiaJ
 *
 * A modified RuneLite class that enables the utilization of the RSB API
 *
 */
package rsb.botLauncher;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;

import joptsimple.*;
import joptsimple.util.EnumConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.client.ClientSessionManager;
import net.runelite.client.account.SessionManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.discord.DiscordService;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.rs.ClientLoader;
import net.runelite.client.rs.ClientUpdateCheckMode;
import net.runelite.client.ui.overlay.WidgetOverlay;
import okhttp3.Request;
import okhttp3.Response;
import rsb.event.EventManager;
import rsb.event.events.PaintEvent;
import rsb.event.events.TextPaintEvent;
import rsb.internal.*;
import rsb.plugin.AccountManager;
import rsb.methods.*;
import rsb.internal.input.Canvas;
import rsb.plugin.Botplugin;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.ui.SplashScreen;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.tooltip.TooltipOverlay;
import net.runelite.client.ui.overlay.worldmap.WorldMapOverlay;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.Cache;
import okhttp3.OkHttpClient;


@Slf4j
@Singleton
@SuppressWarnings("removal")
public class RuneLite extends net.runelite.client.RuneLite {
    public static final File RUNELITE_DIR = new File(System.getProperty("user.home"), ".runelite");
    public static final File CACHE_DIR = new File(RUNELITE_DIR, "cache");
    public static final File PLUGINS_DIR = new File(RUNELITE_DIR, "plugins");
    public static final File PROFILES_DIR = new File(RUNELITE_DIR, "profiles");
    public static final File SCREENSHOT_DIR = new File(RUNELITE_DIR, "screenshots");
    public static final File LOGS_DIR = new File(RUNELITE_DIR, "logs");
    public static final File DEFAULT_SESSION_FILE = new File(RUNELITE_DIR, "session");
    public static final File DEFAULT_CONFIG_FILE = new File(RUNELITE_DIR, "settings.properties");

    private static final int MAX_OKHTTP_CACHE_SIZE = 20 * 1024 * 1024; // 20mb
    public static String USER_AGENT = "RuneLite/" + BotProperties.getVersion() + "-" + BotProperties.getCommit() + (BotProperties.isDirty() ? "+" : "");


    @Getter
    private static Injector injector;

    @Inject
    public ClientUI clientUI;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private ExternalPluginManager externalPluginManager;

    @Inject
    private EventBus eventBus;

    @Inject
    private ConfigManager configManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private DiscordService discordService;

    @Inject
    private ClientSessionManager clientSessionManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private Provider<TooltipOverlay> tooltipOverlay;

    @Inject
    private Provider<WorldMapOverlay> worldMapOverlay;


    @Inject
    @Nullable
    private Client client;

    @Inject
    @Nullable
    private Applet applet;

    private String account;
    private MethodContext methods;
    private Component panel;
    private PaintEvent paintEvent;
    private TextPaintEvent textPaintEvent;
    private EventManager eventManager;
    private BufferedImage backBuffer;
    private Image image;
    private InputManager im;
    private ScriptHandler sh;
    private PassiveScriptHandler psh;
    private BreakHandler bh;
    private Map<String, EventListener> listeners;
    private boolean kill_passive = false;
    private Canvas canvas;
    private Botplugin botplugin;

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

    /**
     * Set the canvas to the opposite state
     */
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

    public Applet getApplet() {return applet = injector.getInstance(Applet.class);}

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
            log.debug("Failed to instantiate listener", ignored);
        }
        return null;
    }

    public boolean hasListener(Class<?> clazz) {
        return clazz != null && listeners.get(clazz.getName()) != null;
    }

    public Image getImage() {
        return image;
    }

    public BufferedImage getBackBuffer() {
        return backBuffer;
    }

    public Component getPanel() {
        return this.panel;
    }

    public void setPanel(Component c) {
        this.panel = c;
    }

    /**
     * Sets an account for the RuneLite (Bot) instance
     * @param name  The name of the account
     * @return  If the account existed already
     */
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

    /**
     * Gets the canvas object while checking to make sure we don't do this before it has actually
     * loaded
     * @return  The Canvas if the client is loaded otherwise null
     */
    public Canvas getCanvas() {
        if (client == null) {
            return null;
        }
        if (client.getCanvas() == null) {
            return null;
        }
        if (canvas == null) {
            canvas = new Canvas(client.getCanvas());
            return canvas;
        }
        return canvas;
    }

    /**
     * Grabs the graphics visible on the canvas from the main buffer using the associated provider
     * @param mainBufferProvider    An object that provides the main buffer (canvas info) for this client instance
     * @return  The graphics of the Canvas
     */
    public Graphics getBufferGraphics(MainBufferProvider mainBufferProvider) {
        image = mainBufferProvider.getImage();
        Graphics back = mainBufferProvider.getImage().getGraphics();
        paintEvent.graphics = back;
        textPaintEvent.graphics = back;
        textPaintEvent.idx = 0;
        eventManager.processEvent(paintEvent);
        eventManager.processEvent(textPaintEvent);
        back.dispose();
        back.drawImage(backBuffer, 0, 0, null);
        return back;
    }

    public Applet getLoader() {
        return (Applet) this.getClient();
    }

    public RuneLite() {
    }


    /**
     * Launches a single instance of RuneLite
     *
     * @param parser        The command-line parser for the program
     * @param optionSpecs   The option specs for the program
     * @param options       The option set for the program
     * @throws Exception Any exception the client or RuneLite might throw
     */
    public void launch(OptionParser parser, ArgumentAcceptingOptionSpec<?>[] optionSpecs, OptionSet options) throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        handleOptions(parser, optionSpecs, options);
        setDefaultUncaughtExceptionHandler();
        initializeClient(optionSpecs, options);
    }

    /**
     * Our set of options corresponding to the command-line arguments that should be parsed.
     * The values assigned are their positions within the relating ArgumentAcceptingOptionSpec array
     */
    enum Options {
        SESSION_FILE(0),CONFIG_FILE(1), UPDATE_MODE(2), PROXY_INFO(3);

        private int index;

        Options(int arrayIndex) {
            this.index = arrayIndex;
        }

        public int getIndex() {
            return index;
        }
    }

    /**
     * Handles the command-line arguments using the OptionParser passed through and assigns our option specs
     * accordingly and then returns them for use
     * @param parser    The parser to use for handling the command-line arguments
     * @return          The ArgumentAcceptingOptionSpec array (the fields for our options)
     */
    public static ArgumentAcceptingOptionSpec<?>[] handleParsing(OptionParser parser) {

        parser.accepts("bot-runelite", "Starts the client in Bot RuneLite mode");
        parser.accepts("sub-bot", "Starts a sub-bot client without the additional features of RuneLite");
        parser.accepts("runelite", "Starts the client in RuneLite mode");
        parser.accepts("developer-mode", "Enable developer tools");
        parser.accepts("ea", "Enable assertions");
        parser.accepts("debug", "Show extra debugging output");
        parser.accepts("insecure-skip-tls-verification", "Disables TLS verification");
        parser.accepts("jav_config", "jav_config url")
                .withRequiredArg()
                .defaultsTo(BotProperties.getJavConfig());
        parser.accepts("help", "Show this text").forHelp();

        final ArgumentAcceptingOptionSpec<File> sessionfile = parser.accepts("sessionfile", "Use a specified session file")
                .withRequiredArg()
                .withValuesConvertedBy(new ConfigFileConverter())
                .defaultsTo(DEFAULT_SESSION_FILE);

        final ArgumentAcceptingOptionSpec<File> configfile = parser.accepts("config", "Use a specified config file")
                .withRequiredArg()
                .withValuesConvertedBy(new ConfigFileConverter())
                .defaultsTo(DEFAULT_CONFIG_FILE);

        final ArgumentAcceptingOptionSpec<ClientUpdateCheckMode> updateMode = parser
                .accepts("rs", "Select client type")
                .withRequiredArg()
                .ofType(ClientUpdateCheckMode.class)
                .defaultsTo(ClientUpdateCheckMode.AUTO)
                .withValuesConvertedBy(new EnumConverter<>(ClientUpdateCheckMode.class) {
                    @Override
                    public ClientUpdateCheckMode convert(String v) {
                        return super.convert(v.toUpperCase());
                    }
                });

        final ArgumentAcceptingOptionSpec<String> proxyInfo = parser
                .accepts("proxy", "Designates a proxy ip address to be used to make the bot server connections")
                .withRequiredArg().ofType(String.class);

        return (ArgumentAcceptingOptionSpec<?>[]) new ArgumentAcceptingOptionSpec[]{sessionfile, configfile, updateMode, proxyInfo};
    }

    /**
     * @param parser        The parser responsible for reading the command-line arguments
     * @param optionSpecs   The associated fields to the corresponding options
     * @param options       The actual set of options required for initialization
     * @throws IOException  Any input/output exception
     */
    public static void handleOptions(OptionParser parser, ArgumentAcceptingOptionSpec<?>[] optionSpecs, OptionSet options) throws IOException {

        if (options.has("help")) {
            parser.printHelpOn(System.out);
            System.exit(0);
        }

        if (options.has("proxy")) {
            String[] proxy = options.valueOf(optionSpecs[Options.PROXY_INFO.getIndex()].ofType(String.class)).split(":");

            if (proxy.length >= 2) {
                System.setProperty("socksProxyHost", proxy[0]);
                System.setProperty("socksProxyPort", proxy[1]);
            }

            if (proxy.length >= 4) {
                System.setProperty("java.net.socks.username", proxy[2]);
                System.setProperty("java.net.socks.password", proxy[3]);

                final String user = proxy[2];
                final char[] pass = proxy[3].toCharArray();

                Authenticator.setDefault(new Authenticator() {
                    private PasswordAuthentication auth = new PasswordAuthentication(user, pass);

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return auth;
                    }
                });
            }
        }
    }

    /**
     * Creates a thread to handle uncaught exceptions created by RuneLite
     */
    public static void setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
        {
            log.error("Uncaught exception:", throwable);
            if (throwable instanceof AbstractMethodError)
            {
                log.error("Classes are out of date; Build with maven again.");
            }
        });
    }

    /**
     * Initializes the RuneLite processes after having parsed and handled the command line arguments
     * @param optionSpecs   The associated fields to the corresponding options
     * @param options       The actual set of options required for initialization
     */
    public void initializeClient(ArgumentAcceptingOptionSpec<?>[] optionSpecs, OptionSet options) {
        final OkHttpClient okHttpClient = buildHttpClient(options.has("insecure-skip-tls-verification"));
        RuneLiteAPI.CLIENT = okHttpClient;

        //SplashScreen.init();
        //SplashScreen.stage(0, "Retrieving client", "");

        try
        {
            final RuntimeConfigLoader runtimeConfigLoader = new RuntimeConfigLoader(okHttpClient);
            final ClientLoader clientLoader = new ClientLoader(okHttpClient,
                    options.valueOf(optionSpecs[Options.UPDATE_MODE.getIndex()].ofType(ClientUpdateCheckMode.class)),
                    runtimeConfigLoader,
                    (String) options.valueOf("jav_config"));

            new Thread(() ->
            {
                clientLoader.get();
                ClassPreloader.preload();
            }, "Preloader").start();


            PROFILES_DIR.mkdirs();

            final long start = System.currentTimeMillis();

            injector = Guice.createInjector(new BotModule(
                    okHttpClient,
                    clientLoader,
                    runtimeConfigLoader,
                    options.has("developer-mode"),
                    false,
                    options.valueOf(optionSpecs[Options.SESSION_FILE.getIndex()].ofType(File.class)),
                    options.valueOf(optionSpecs[Options.CONFIG_FILE.getIndex()].ofType(File.class)
                    )));

            setInjector(injector);

            injector.getInstance(RuneLite.class).init(false);

            final long end = System.currentTimeMillis();
            final RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
            final long uptime = rb.getUptime();
            log.info("Client initialization took {}ms. Uptime: {}ms", end - start, uptime);
        }
        catch (Exception e)
        {
            log.warn("Failure during startup", e);
            SwingUtilities.invokeLater(() ->
                    new FatalErrorDialog("RuneLite has encountered an unexpected error during startup.")
                            .open());
        }
    }

    /**
     * Returns the size of the panel that clients should be drawn into. For
     * internal use.
     *
     * @return The client panel size.
     */
    public Dimension getPanelSize() {
        for (RuneLite bot : Application.getBots()) {
            if (bot != null) {
                if (bot.getClient().getClass().getClassLoader() == this.getClient().getClass().getClassLoader()) {
                    return bot.getPanel().getSize();
                }
            }
        }
        return null;
    }


    /**
     * The actual method associated with initializing the client-related data. Such as creating the client sizing and
     * binding the plethora of handlers, listeners, and managers to this particular RuneLite instance
     * (outside the injector binding)
     *
     * @param  startClientBare  Whether to launch the client without any additional initialization settings or not
     * @throws Exception        Any exception the client, bot, or RuneLite might throw.
     */
    public void init(boolean startClientBare) throws Exception {
        im = new InputManager(this);
        Executors.newSingleThreadScheduledExecutor().submit(() -> {
            while(this.getClient() == null){}
            if (getPanelSize() != null) {
                final Dimension size = getPanelSize();
                backBuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            }
        });
        psh = new PassiveScriptHandler(this);
        eventManager = new EventManager();
        sh = new ScriptHandler(this);
        bh = new BreakHandler(this);
        paintEvent = new PaintEvent();
        textPaintEvent = new TextPaintEvent();
        listeners = new TreeMap<>();
        Botplugin botplugin = new Botplugin(injector);
        pluginManager.add(botplugin);
        setMethodContext();

        eventManager.start();

        if (!startClientBare) {
            this.start();
        }
        else {
            this.bareStart();
        }
    }

    /**
     * Test purposes only.
     */
    public void start() throws Exception {
        // Load RuneLite or Vanilla client
        final boolean isOutdated = client == null;

        if (!isOutdated)
        {
            // Inject members into client
            injector.injectMembers(client);
        }

        // Start the applet
        if (applet != null)
        {

            // Client size must be set prior to init
            applet.setSize(Constants.GAME_FIXED_SIZE);

            // Change user.home so the client places jagexcache in the .runelite directory
            String oldHome = System.setProperty("user.home", RUNELITE_DIR.getAbsolutePath());
            try
            {
                applet.init();
            }
            finally
            {
                System.setProperty("user.home", oldHome);
            }

            applet.start();
        }

        SplashScreen.stage(.57, null, "Loading configuration");

        // Load user configuration
        configManager.load();

        // Load the session, including saved configuration
        sessionManager.loadSession();

        // Tell the plugin manager if client is outdated or not
        pluginManager.setOutdated(isOutdated);

        // Load the plugins, but does not start them yet.
        // This will initialize configuration
        pluginManager.loadCorePlugins();
        
        ArrayList<Plugin> pluginsToRemove = new ArrayList<Plugin>();
        
        for (Plugin plugin : pluginManager.getPlugins()) {
        
            if (plugin.getName().equals("Music") || plugin.getName().equals("World Hopper")) {
                pluginsToRemove.add(plugin);
            }

        }
        for (Plugin plugin : pluginsToRemove) {
            pluginManager.remove(plugin);
        }

        externalPluginManager.loadExternalPlugins();

        SplashScreen.stage(.70, null, "Finalizing configuration");

        // Plugins have provided their config, so set default config
        // to main settings
        pluginManager.loadDefaultPluginConfiguration(null);

        // Start client session
        clientSessionManager.start();
        eventBus.register(clientSessionManager);

        SplashScreen.stage(.75, null, "Starting core interface");

        // Initialize UI
        clientUI.init();

        // Initialize Discord service
        discordService.init();

        // Register event listeners
        eventBus.register(clientUI);
        eventBus.register(pluginManager);
        eventBus.register(externalPluginManager);
        eventBus.register(overlayManager);
        eventBus.register(configManager);
        eventBus.register(discordService);

        if (!isOutdated)
        {
            // Add core overlays
            WidgetOverlay.createOverlays(overlayManager, client).forEach(overlayManager::add);
            overlayManager.add(worldMapOverlay.get());
            overlayManager.add(tooltipOverlay.get());
        }

        // Start plugins
        pluginManager.startPlugins();

        SplashScreen.stop();

        clientUI.show();
    }

    /**
     * Launches a client with the bare minimum of settings needed. This is used for the sub-bots and do not use
     * the additional initializations that the standard RuneLite start method does.
     * @throws Exception    Any exception the client, bot, or RuneLite might throw.
     */
    public void bareStart() throws Exception {
        // Load RuneLite or Vanilla client
        final boolean isOutdated = client == null;

        if (!isOutdated)
        {
            // Inject members into client
            injector.injectMembers(client);
        }

        // Start the applet
        if (applet != null)
        {
            // Client size must be set prior to init
            applet.setSize(Constants.GAME_FIXED_SIZE);

            // Change user.home so the client places jagexcache in the .runelite directory
            String oldHome = System.setProperty("user.home", RUNELITE_DIR.getAbsolutePath());
            try
            {
                applet.init();
            }
            finally
            {
                System.setProperty("user.home", oldHome);
            }

            applet.start();
        }

        // Load user configuration
        configManager.load();

        // Load the session, including saved configuration
        sessionManager.loadSession();


        // Load the plugins, but does not start them yet.
        // This will initialize configuration

        // Plugins have provided their config, so set default config
        // to main settings

        // Start client session
        clientSessionManager.start();
        eventBus.register(clientSessionManager);

        // Register event listeners
        eventBus.register(clientUI);
        eventBus.register(pluginManager);
        eventBus.register(externalPluginManager);
        eventBus.register(overlayManager);
        eventBus.register(configManager);

        if (!isOutdated)
        {
            // Add core overlays
            WidgetOverlay.createOverlays(overlayManager, client).forEach(overlayManager::add);
            overlayManager.add(worldMapOverlay.get());
            overlayManager.add(tooltipOverlay.get());
        }

    }

    /**
     * Assigns this instance of the RuneLite (Bot) a method context for calling bot api methods
     * as well as assigns bank constants here.
     */
    public void setMethodContext() {
        methods = new MethodContext(this);
        methods.bank.assignConstants();
    }

    /**
     * Stops and shuts down the current bot instance
     */
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

    /**
     * RuneLite method
     * Converts config files paths to whatever directory needed
     */
    private static class ConfigFileConverter implements ValueConverter<File>
    {
        @Override
        public File convert(String fileName)
        {
            final File file;

            if (Paths.get(fileName).isAbsolute()
                    || fileName.startsWith("./")
                    || fileName.startsWith(".\\"))
            {
                file = new File(fileName);
            }
            else
            {
                file = new File(net.runelite.client.RuneLite.RUNELITE_DIR, fileName);
            }

            if (file.exists() && (!file.isFile() || !file.canWrite()))
            {
                throw new ValueConversionException(String.format("File %s is not accessible", file.getAbsolutePath()));
            }

            return file;
        }

        @Override
        public Class<? extends File> valueType()
        {
            return File.class;
        }

        @Override
        public String valuePattern()
        {
            return null;
        }
    }

    /**
     * RuneLite method
     * Establishes the HTTPClient (Request, Response, Caching, etc)
     * @param insecureSkipTlsVerification   determine whether to skip TLSVerification
     * @return  the built OkHttpClient
     */
    static OkHttpClient buildHttpClient(boolean insecureSkipTlsVerification)
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .pingInterval(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(chain ->
                {
                    Request userAgentRequest = chain.request()
                            .newBuilder()
                            .header("User-Agent", USER_AGENT)
                            .build();
                    return chain.proceed(userAgentRequest);
                })
                // Setup cache
                .cache(new Cache(new File(CACHE_DIR, "okhttp"), MAX_OKHTTP_CACHE_SIZE))
                .addNetworkInterceptor(chain ->
                {
                    // This has to be a network interceptor so it gets hit before the cache tries to store stuff
                    Response res = chain.proceed(chain.request());
                    if (res.code() >= 400 && "GET".equals(res.request().method()))
                    {
                        // if the request 404'd we don't want to cache it because its probably temporary
                        res = res.newBuilder()
                                .header("Cache-Control", "no-store")
                                .build();
                    }
                    return res;
                });

        if (insecureSkipTlsVerification || BotProperties.isInsecureSkipTlsVerification())
        {
            setupInsecureTrustManager(builder);
        }

        return builder.build();
    }

    /**
     * RuneLite method
     * Uncertain behavior
     * Looks to verify client trust?
     * #todo Might need to investigate further
     * @param okHttpClientBuilder   The OkHttpClientBuilder object that handles creating an OkHttpClient
     */
    private static void setupInsecureTrustManager(OkHttpClient.Builder okHttpClientBuilder)
    {
        try
        {
            X509TrustManager trustManager = new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return new X509Certificate[0];
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            okHttpClientBuilder.sslSocketFactory(sc.getSocketFactory(), trustManager);
        }
        catch (NoSuchAlgorithmException | KeyManagementException ex)
        {
            log.warn("unable to setup insecure trust manager", ex);
        }
    }
}
