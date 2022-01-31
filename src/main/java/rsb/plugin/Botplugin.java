package rsb.plugin;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import rsb.botLauncher.RuneLite;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.ClientToolbar;
import rsb.internal.globval.GlobalConfiguration;

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

    private static ScriptPanel scriptPanel;

    private static AccountPanel accountPanel;


    @Override
    protected void startUp() throws Exception
    {
        BotPanel panel = injector.getInstance(BotPanel.class);
        RuneLite bot = injector.getInstance(RuneLite.class);

        accountPanel = new AccountPanel(bot);
        scriptPanel = new ScriptPanel(bot);

        panel.associateBot(accountPanel, scriptPanel);

        BufferedImage icon = imageToBufferedImage(Botplugin.class.getResourceAsStream("rsb.png"));

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

    public static BufferedImage imageToBufferedImage(InputStream is) throws IOException {
        Image im = ImageIO.read(is);
        BufferedImage bi = new BufferedImage
                (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

    public static ScriptPanel getScriptPanel() {
        return scriptPanel;
    }

    public static void setScriptPanel(ScriptPanel scriptPanel) {
        Botplugin.scriptPanel = scriptPanel;
    }

    public static AccountPanel getAccountPanel() {
        return accountPanel;
    }

    public void setAccountPanel(AccountPanel accountPanel) {
        Botplugin.accountPanel = accountPanel;
    }
}
