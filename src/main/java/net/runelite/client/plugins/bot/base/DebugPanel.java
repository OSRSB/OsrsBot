package net.runelite.client.plugins.bot.base;

import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.bot.BotConfig;
import net.runelite.rsb.botLauncher.BotLite;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DebugPanel extends JPanel {
    BotLite bot;
    String[] logLevels = {"DEBUG", "INFO", "WARN", "ERROR", "OFF"};

    JComboBox<String> logLevel = new JComboBox<>(logLevels);
    JCheckBox drawMouse = new JCheckBox("Draw Mouse");
    JCheckBox drawMouseTrail = new JCheckBox("Draw Mouse Trail");
    JCheckBox enableMouse = new JCheckBox("Enable Mouse");
    JCheckBox drawBoundaries = new JCheckBox("Draw Boundaries");
    JCheckBox drawGround = new JCheckBox("Draw Ground");
    JCheckBox drawInventory = new JCheckBox("Draw Inventory");
    JCheckBox drawNPCs = new JCheckBox("Draw NPCs");
    JCheckBox drawObjects = new JCheckBox("Draw Objects");
    JCheckBox drawPlayers = new JCheckBox("Draw Players");
    JCheckBox drawSettings = new JCheckBox("Draw Settings");
    JCheckBox drawWeb = new JCheckBox("Draw Web");

    public DebugPanel(BotLite bot) {
        super();
        this.bot = bot;
        init();
    }

    private void init() {
        logLevel.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugLogLevel", Level.valueOf(logLevel.getSelectedItem().toString())));
        drawMouse.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawMouse", drawMouse.isSelected()));
        drawMouseTrail.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawMouseTrail", drawMouseTrail.isSelected()));
        enableMouse.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugEnableMouse", enableMouse.isSelected()));
        drawBoundaries.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawBoundaries", drawBoundaries.isSelected()));
        drawGround.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawGround", drawGround.isSelected()));
        drawInventory.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawInventory", drawInventory.isSelected()));
        drawNPCs.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawNPCs", drawNPCs.isSelected()));
        drawObjects.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawObjects", drawObjects.isSelected()));
        drawPlayers.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawPlayers", drawPlayers.isSelected()));
        drawSettings.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawSettings", drawSettings.isSelected()));
        drawWeb.addActionListener(e -> bot.configManager.setConfiguration("bot", "debugDrawWeb", drawWeb.isSelected()));

        BotConfig config = bot.configManager.getConfig(BotConfig.class);
        if (config != null) {
            logLevel.setSelectedItem(config.debugLogLevel());
            setLogLevel(Level.valueOf(config.debugLogLevel()));
            drawMouse.setSelected(config.debugDrawMouse());
            setDrawMouse(config.debugDrawMouse());
            drawMouseTrail.setSelected(config.debugDrawMouseTrail());
            setDrawMouseTrail(config.debugDrawMouseTrail());
            enableMouse.setSelected(config.debugEnableMouse());
            setEnableMouse(config.debugEnableMouse());
            drawBoundaries.setSelected(config.debugDrawBoundaries());
            setDrawBoundaries(config.debugDrawBoundaries());
            drawGround.setSelected(config.debugDrawGround());
            setDrawGround(config.debugDrawGround());
            drawInventory.setSelected(config.debugDrawInventory());
            setDrawInventory(config.debugDrawInventory());
            drawNPCs.setSelected(config.debugDrawNPCs());
            setDrawNPCs(config.debugDrawNPCs());
            drawObjects.setSelected(config.debugDrawObjects());
            setDrawObjects(config.debugDrawObjects());
            drawPlayers.setSelected(config.debugDrawPlayers());
            setDrawPlayers(config.debugDrawPlayers());
            drawSettings.setSelected(config.debugDrawSettings());
            setDrawSettings(config.debugDrawSettings());
            drawWeb.setSelected(config.debugDrawWeb());
            setDrawWeb(config.debugDrawWeb());
        }

        drawBoundaries.setEnabled(false);
        drawGround.setEnabled(false);
        drawInventory.setEnabled(false);
        drawNPCs.setEnabled(false);
        drawObjects.setEnabled(false);
        drawPlayers.setEnabled(false);

        drawWeb.setEnabled(false);

        this.add(drawMouse);
        this.add(drawMouseTrail);
        this.add(enableMouse);
        this.add(drawBoundaries);
        this.add(drawGround);
        this.add(drawInventory);
        this.add(drawNPCs);
        this.add(drawObjects);
        this.add(drawPlayers);
        this.add(drawSettings);
        this.add(drawWeb);
        this.add(logLevel);
    }

    public void setLogLevel(Level level) {
        bot.getScriptHandler().getDebugSettingsListener().setLogLevel(level);
    }

    public void setDrawMouse(boolean drawMouse) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawMouse(drawMouse);
    }

    public void setDrawMouseTrail(boolean drawMouseTrail) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawMouseTrail(drawMouseTrail);
    }

    public void setEnableMouse(boolean enableMouse) {
        bot.getScriptHandler().getDebugSettingsListener().setEnableMouse(enableMouse);
    }

    public void setDrawBoundaries(boolean drawBoundaries) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawBoundaries(drawBoundaries);
    }

    public void setDrawGround(boolean drawGround) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawGround(drawGround);
    }

    public void setDrawInventory(boolean drawInventory) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawInventory(drawInventory);
    }

    public void setDrawNPCs(boolean drawNPCs) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawNPCs(drawNPCs);
    }

    public void setDrawObjects(boolean drawObjects) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawObjects(drawObjects);
    }

    public void setDrawPlayers(boolean drawPlayers) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawPlayers(drawPlayers);
    }

    public void setDrawSettings(boolean drawSettings) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawSettings(drawSettings);
    }

    public void setDrawWeb(boolean drawWeb) {
        bot.getScriptHandler().getDebugSettingsListener().setDrawWeb(drawWeb);
    }
}
