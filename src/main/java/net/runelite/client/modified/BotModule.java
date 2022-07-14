package net.runelite.client.modified;

import com.google.common.base.Strings;
import com.google.common.math.DoubleMath;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.binder.ConstantBindingBuilder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import java.applet.Applet;
import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

import net.runelite.api.Client;
import net.runelite.api.hooks.Callbacks;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.account.SessionManager;
import net.runelite.client.callback.Hooks;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ChatColorConfig;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.game.ItemManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.task.Scheduler;
import net.runelite.client.util.DeferredEventBus;
import net.runelite.client.util.ExecutorServiceExceptionLogger;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import javax.annotation.Nullable;


@SuppressWarnings("removal")
public class BotModule extends AbstractModule {

    private final OkHttpClient okHttpClient;
    private final Supplier<Applet> clientLoader;
    private final RuntimeConfigLoader configSupplier;
    private final boolean developerMode;
    private final boolean safeMode;
    private final File sessionfile;
    private final File config;


    public BotModule(OkHttpClient okHttpClient, Supplier<Applet> clientLoader, RuntimeConfigLoader configSupplier, boolean developerMode, boolean safeMode, File sessionfile, File config) {
        this.okHttpClient = okHttpClient;
        this.clientLoader = clientLoader;
        this.configSupplier = configSupplier;
        this.developerMode = developerMode;
        this.safeMode = safeMode;
        this.sessionfile = sessionfile;
        this.config = config;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure()
    {
        // bind properties
        Properties properties = BotProperties.getProperties();
        for (String key : properties.stringPropertyNames())
        {
            String value = properties.getProperty(key);
            bindConstant().annotatedWith(Names.named(key)).to(value);
        }

        // bind runtime config
        RuntimeConfig runtimeConfig = configSupplier.get();
        if (runtimeConfig != null && runtimeConfig.getProps() != null)
        {
            for (Map.Entry<String, ?> entry : runtimeConfig.getProps().entrySet())
            {
                if (entry.getValue() instanceof String)
                {
                    ConstantBindingBuilder binder = bindConstant().annotatedWith(Names.named(entry.getKey()));
                    binder.to((String) entry.getValue());
                }
                else if (entry.getValue() instanceof Double)
                {
                    ConstantBindingBuilder binder = bindConstant().annotatedWith(Names.named(entry.getKey()));
                    if (DoubleMath.isMathematicalInteger((double) entry.getValue()))
                    {
                        binder.to((int) (double) entry.getValue());
                    }
                    else
                    {
                        binder.to((double) entry.getValue());
                    }
                }
            }
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

            bind(Gson.class).toInstance(RuneLiteAPI.GSON);

            bind(Callbacks.class).to(Hooks.class);

            bind(EventBus.class)
                    .toInstance(new EventBus());

            bind(EventBus.class)
                    .annotatedWith(Names.named("Deferred EventBus"))
                    .to(DeferredEventBus.class);
    }

    @Provides
    @Singleton
    Applet provideApplet()
    {
        return clientLoader.get();
    }

    @Provides
    @Singleton
    Client provideClient(@Nullable Applet applet)
    {
        return applet instanceof Client ? (Client) applet : null;
    }

    @Provides
    @Singleton
    RuntimeConfig provideRuntimeConfig()
    {
        return configSupplier.get();
    }

    @Provides
    @Singleton
    RuneLiteConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(RuneLiteConfig.class);
    }

    @Provides
    @Singleton
    ChatColorConfig provideChatColorConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ChatColorConfig.class);
    }

    @Provides
    @Named("runelite.api.base")
    HttpUrl provideApiBase(@Named("runelite.api.base") String s)
    {
        final String prop = System.getProperty("runelite.http-service.url");
        return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
    }

    @Provides
    @Named("runelite.session")
    HttpUrl provideSession(@Named("runelite.session") String s)
    {
        final String prop = System.getProperty("runelite.session.url");
        return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
    }

    @Provides
    @Named("runelite.static.base")
    HttpUrl provideStaticBase(@Named("runelite.static.base") String s)
    {
        final String prop = System.getProperty("runelite.static.url");
        return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
    }

    @Provides
    @Named("runelite.ws")
    HttpUrl provideWs(@Named("runelite.ws") String s)
    {
        final String prop = System.getProperty("runelite.ws.url");
        return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
    }
}
