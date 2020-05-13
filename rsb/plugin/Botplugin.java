package net.runelite.client.rsb.plugin;


import java.awt.image.BufferedImage;
import javax.inject.Inject;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.account.AccountPlugin;
import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
        name = "Bot panel",
        description = "Bot panel",
        loadWhenOutdated = true
)
@Slf4j
public class Botplugin extends Plugin
{
    public Botplugin(Injector injector) {
        super.injector = injector;
    }
    @Inject
    private ClientToolbar clientToolbar;

    private NavigationButton navButton;

    @Override
    protected void startUp() throws Exception
    {
        BotPanel panel = injector.getInstance(BotPanel.class);
        RuneLite bot = injector.getInstance(RuneLite.class);
        panel.associateBot(new BasePanel(bot), new ScriptPanel(bot));

        final BufferedImage icon = ImageUtil.getResourceStreamFromClass(AccountPlugin.class, "login_icon.png");

        navButton = NavigationButton.builder()
                .tooltip("Bot Interface")
                .icon(icon)
                .priority(10)
                .panel(panel)
                .build();
        clientToolbar = injector.getInstance(ClientToolbar.class);
        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown()
    {
        clientToolbar.removeNavigation(navButton);
    }
}
