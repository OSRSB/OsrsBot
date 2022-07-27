/**
 * Author:GigiaJ
 *
 * A modified RuneLite class that enables the utilization of the RSB API
 *
 */
package net.runelite.client.modified;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
import net.runelite.client.RuntimeConfig;
import net.runelite.client.account.SessionManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.discord.DiscordService;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.rs.ClientLoader;
import net.runelite.client.rs.ClientUpdateCheckMode;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetOverlay;
import net.runelite.rsb.botLauncher.BotLite;
import okhttp3.Request;
import okhttp3.Response;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.FatalErrorDialog;
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
    public static Injector injector;

    @Inject
    public ClientUI clientUI;

    @Inject
    public PluginManager pluginManager;

    @Inject
    public ExternalPluginManager externalPluginManager;

    @Inject
    public EventBus eventBus;

    @Inject
    public ConfigManager configManager;

    @Inject
    public SessionManager sessionManager;

    @Inject
    public DiscordService discordService;

    @Inject
    public ClientSessionManager clientSessionManager;

    @Inject
    public OverlayManager overlayManager;

    @Inject
    public Provider<TooltipOverlay> tooltipOverlay;

    @Inject
    public Provider<WorldMapOverlay> worldMapOverlay;


    @Inject
    @Nullable
    public Client client;

    @Inject
    @Nullable
    public Applet applet;

    @Inject
    @Nullable
    private RuntimeConfig runtimeConfig;

    /**
     * Launches a single instance of RuneLite
     *
     * @param args      The args for the program (plus any extras added on)
     * @throws Exception Any exception the client or RuneLite might throw
     */
    public static void launch(String[] args) throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        OptionParser parser = new OptionParser();
        ArgumentAcceptingOptionSpec<?>[] optionSpecs = handleParsing(parser);
        OptionSet options = parser.parse(args);
        handleOptions(parser, optionSpecs, options);
        setDefaultUncaughtExceptionHandler();
        initializeClient(optionSpecs, options);
    }

    /**
     * Handles the command-line arguments using the OptionParser passed through and assigns our option specs
     * accordingly and then returns them for use
     * @param parser    The parser to use for handling the command-line arguments
     * @return          The ArgumentAcceptingOptionSpec array (the fields for our options)
     */
    public static ArgumentAcceptingOptionSpec<?>[] handleParsing(OptionParser parser) {
        parser.accepts("bot-runelite", "Starts the client in Bot RuneLite mode");
        parser.accepts("headless", "Starts a client without the additional features of RuneLite");
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
    public static void initializeClient(ArgumentAcceptingOptionSpec<?>[] optionSpecs, OptionSet options) {
        final OkHttpClient okHttpClient = buildHttpClient(options.has("insecure-skip-tls-verification"));
        RuneLiteAPI.CLIENT = okHttpClient;

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
                preload();
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
            injector.getInstance(BotLite.class).init(options.has("headless"));

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
     * Launches a client with the bare minimum of settings needed. This is used for the sub-bots and do not use
     * the additional initializations that the standard RuneLite start method does.
     * @throws Exception    Any exception the client, bot, or RuneLite might throw.
     */
    public void bareStart() throws Exception {
        // Load RuneLite or Vanilla client
        final boolean isOutdated = client == null;

        setupSystemProps();

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

            System.setProperty("jagex.disableBouncyCastle", "true");
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
     * Loads some slow to initialize classes (hopefully) before they are needed to streamline client startup
     */
    static void preload()
    {
        // This needs to enumerate the system fonts for some reason, and that takes a while
        FontManager.getRunescapeSmallFont();

        // This needs to load a timezone database that is mildly large
        ZoneId.of("Europe/London");

        // This just needs to call 20 different DateTimeFormatter constructors, which are slow
        Object unused = DateTimeFormatter.BASIC_ISO_DATE;
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

    private void setupSystemProps()
    {
        if (runtimeConfig == null || runtimeConfig.getSysProps() == null)
        {
            return;
        }

        for (Map.Entry<String, String> entry : runtimeConfig.getSysProps().entrySet())
        {
            String key = entry.getKey(), value = entry.getValue();
            log.debug("Setting property {}={}", key, value);
            System.setProperty(key, value);
        }
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

}
