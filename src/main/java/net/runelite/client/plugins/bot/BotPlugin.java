package net.runelite.client.plugins.bot;

import ch.qos.logback.classic.Level;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.bot.base.DebugPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.rsb.botLauncher.BotLite;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@PluginDescriptor(
        name = "Bot panel",
        description = "Bot panel"
)
@Slf4j
public class BotPlugin extends Plugin {

    @Inject
    private BotConfig config;

    @Inject
    private ClientToolbar clientToolbar;

    private NavigationButton navButton;

    private static ScriptPanel scriptPanel;

    private static AccountPanel accountPanel;

    private static DebugPanel debugPanel;

    public BotPlugin() {
    }

    @Provides
    BotConfig provideConfig(ConfigManager configManager) {
        return (BotConfig)configManager.getConfig(BotConfig.class);
    }
    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        if (configChanged.getGroup().equals("bot")) {
            switch(configChanged.getKey()) {
                case "debugLogLevel" ->
                    debugPanel.setLogLevel(Level.valueOf(configChanged.getNewValue()));
                case "debugDrawMouse" ->
                    debugPanel.setDrawMouse(configChanged.getNewValue() == "true");
                case "debugDrawMouseTrail" ->
                    debugPanel.setDrawMouseTrail(configChanged.getNewValue() == "true");
                case "debugEnableMouse" ->
                    debugPanel.setEnableMouse(configChanged.getNewValue() == "true");
                case "debugDrawBoundaries" ->
                    debugPanel.setDrawBoundaries(configChanged.getNewValue() == "true");
                case "debugDrawGround" ->
                    debugPanel.setDrawGround(configChanged.getNewValue() == "true");
                case "debugDrawInventory" ->
                        debugPanel.setDrawInventory(configChanged.getNewValue() == "true");
                case "debugDrawNpcs" ->
                    debugPanel.setDrawNPCs(configChanged.getNewValue() == "true");
                case "debugDrawObjects" ->
                        debugPanel.setDrawObjects(configChanged.getNewValue() == "true");
                case "debugDrawPlayers" ->
                    debugPanel.setDrawPlayers(configChanged.getNewValue() == "true");
                case "debugDrawWeb" ->
                    debugPanel.setDrawWeb(configChanged.getNewValue() == "true");
            }
        }
    }

    @Override
    protected void startUp() throws Exception
    {
        BotPanel panel = injector.getInstance(BotPanel.class);
        BotLite bot = injector.getInstance(BotLite.class);

        accountPanel = new AccountPanel(bot);
        scriptPanel = new ScriptPanel(bot);
        debugPanel = new DebugPanel(bot);

        panel.associateBot(accountPanel, scriptPanel, debugPanel);

        BufferedImage icon = imageToBufferedImage(BotPlugin.class.getResourceAsStream("rsb.png"));

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
        BotPlugin.scriptPanel = scriptPanel;
    }

    public static AccountPanel getAccountPanel() {
        return accountPanel;
    }

    public void setAccountPanel(AccountPanel accountPanel) {
        BotPlugin.accountPanel = accountPanel;
    }

    public static DebugPanel getDebugPanel() {
        return debugPanel;
    }

    public void setDebugPanel(DebugPanel debugPanel) {
        BotPlugin.debugPanel = debugPanel;
    }
}
