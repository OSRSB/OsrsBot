package net.runelite.client.modified;

import com.google.common.base.Strings;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuntimeConfig;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class RuntimeConfigLoader extends net.runelite.client.RuntimeConfigLoader implements Supplier<RuntimeConfig>
{
    private final OkHttpClient okHttpClient;
    private final CompletableFuture<RuntimeConfig> configFuture;

    public RuntimeConfigLoader(OkHttpClient okHttpClient)
    {
        super(okHttpClient);
        this.okHttpClient = okHttpClient;
        configFuture = fetch();
    }

    @Override
    public RuntimeConfig get()
    {
        try
        {
            return configFuture.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            log.error("error fetching runtime config", e);
            return null;
        }
    }

    private CompletableFuture<RuntimeConfig> fetch()
    {
        CompletableFuture<RuntimeConfig> future = new CompletableFuture<>();

        String prop = System.getProperty("runelite.rtconf");
        if (!Strings.isNullOrEmpty(prop))
        {
            try
            {
                log.info("Using local runtime config");

                String strConf = new String(Files.readAllBytes(Paths.get(prop)), StandardCharsets.UTF_8);
                RuntimeConfig conf = RuneLiteAPI.GSON.fromJson(strConf, RuntimeConfig.class);
                future.complete(conf);
                return future;
            }
            catch (IOException e)
            {
                throw new RuntimeException("failed to load override runtime config", e);
            }
        }

        Request request = new Request.Builder()
                .url(BotProperties.getRuneLiteConfig())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response)
            {
                try // NOPMD: UseTryWithResources
                {
                    RuntimeConfig config = RuneLiteAPI.GSON.fromJson(response.body().charStream(), RuntimeConfig.class);
                    future.complete(config);
                }
                catch (Throwable ex)
                {
                    future.completeExceptionally(ex);
                }
                finally
                {
                    response.close();
                }
            }
        });
        return future;
    }
}