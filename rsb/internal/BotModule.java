package net.runelite.client.rsb.internal;

import com.google.inject.name.Names;
import net.runelite.api.hooks.Callbacks;
import net.runelite.client.RuneLite;
import net.runelite.client.RuneLiteModule;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class BotModule extends RuneLiteModule {

    private Supplier<Applet> clientLoader;
    private boolean developerMode;
    private File sessionfile;
    private File config;
    private static final int MAX_OKHTTP_CACHE_SIZE = 20 * 1024 * 1024; // 20mb


    public BotModule(Supplier<Applet> clientLoader, File config) {
        super(clientLoader, config);
        this.clientLoader = clientLoader;
        this.config = config;
    }


    @Override
    protected void configure()
    {
        bindConstant().annotatedWith(Names.named("developerMode")).to(developerMode);
        bind(File.class).annotatedWith(Names.named("sessionfile")).toInstance(sessionfile);
        bind(File.class).annotatedWith(Names.named("config")).toInstance(config);
        bind(ScheduledExecutorService.class).toInstance(new ExecutorServiceExceptionLogger(Executors.newSingleThreadScheduledExecutor()));
        bind(OkHttpClient.class).toInstance(RuneLiteAPI.CLIENT.newBuilder()
                .cache(new Cache(new File(RuneLite.CACHE_DIR, "okhttp"), MAX_OKHTTP_CACHE_SIZE))
                .build());
        bind(MenuManager.class);
        bind(ChatMessageManager.class);
        bind(ItemManager.class);
        bind(Scheduler.class);
        bind(PluginManager.class);
        bind(SessionManager.class);

        bind(Callbacks.class).to(BotHooks.class);

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
