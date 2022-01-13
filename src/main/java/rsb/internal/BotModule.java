package rsb.internal;

import com.google.inject.name.Names;
import net.runelite.api.hooks.Callbacks;
import net.runelite.client.RuneLite;
import net.runelite.client.RuneLiteModule;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.account.SessionManager;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.game.ItemManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.task.Scheduler;
import net.runelite.client.util.DeferredEventBus;
import net.runelite.client.util.ExecutorServiceExceptionLogger;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.applet.Applet;
import java.io.File;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class BotModule extends RuneLiteModule {

    private final OkHttpClient okHttpClient;
    private final Supplier<Applet> clientLoader;
    private final boolean developerMode;
    private final boolean safeMode;
    private final File sessionfile;
    private final File config;


    public BotModule(OkHttpClient okHttpClient, Supplier<Applet> clientLoader, boolean developerMode, boolean safeMode, File sessionfile, File config) {
        super(okHttpClient, clientLoader, developerMode, safeMode, sessionfile, config);
        this.okHttpClient = okHttpClient;
        this.clientLoader = clientLoader;
        this.developerMode = developerMode;
        this.safeMode = safeMode;
        this.sessionfile = sessionfile;
        this.config = config;
    }

    @Override
    protected void configure()
    {
        Properties properties = BotProperties.getProperties();
        for (String key : properties.stringPropertyNames())
        {
            String value = properties.getProperty(key);
            bindConstant().annotatedWith(Names.named(key)).to(value);
        }
        bindConstant().annotatedWith(Names.named("developerMode")).to(developerMode);
        bindConstant().annotatedWith(Names.named("safeMode")).to(safeMode);
        bind(File.class).annotatedWith(Names.named("sessionfile")).toInstance(sessionfile);
        bind(File.class).annotatedWith(Names.named("config")).toInstance(config);
        bind(ScheduledExecutorService.class).toInstance(new ExecutorServiceExceptionLogger(Executors.newSingleThreadScheduledExecutor()));
        bind(OkHttpClient.class).toInstance(okHttpClient);
        bind(MenuManager.class);
        bind(ChatMessageManager.class);
        bind(ItemManager.class);
        bind(Scheduler.class);
        bind(PluginManager.class);
        bind(SessionManager.class);

        bind(Callbacks.class).to(NewHooks.class);

        bind(EventBus.class)
                .toInstance(new EventBus());

        bind(EventBus.class)
                .annotatedWith(Names.named("Deferred EventBus"))
                .to(DeferredEventBus.class);

        bind(Logger.class)
                .annotatedWith(Names.named("Core Logger"))
                .toInstance(LoggerFactory.getLogger(RuneLite.class));
    }
}
