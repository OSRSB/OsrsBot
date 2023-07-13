package net.runelite.rsb.internal.listener;

import ch.qos.logback.classic.Level;
import lombok.Getter;
import lombok.Setter;
import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.EventManager;
import net.runelite.rsb.event.impl.*;
import net.runelite.rsb.internal.ScriptHandler;
import net.runelite.rsb.internal.input.MouseInputBlocker;
import net.runelite.rsb.internal.input.MouseMotionBlocker;
import net.runelite.rsb.script.Script;

public class DebugSettingsListener implements ScriptListener {
    BotLite bot;

    DrawMouse drawMouseListener = null;
    DrawMouseTrail drawMouseTrailListener = null;
    MouseInputBlocker mouseInputBlockerListener = null;
    MouseMotionBlocker mouseMotionBlockerListener = null;
    DrawBoundaries drawBoundariesListener = null;
    DrawGround drawGroundListener = null;
    DrawInventory drawInventoryListener = null;
    DrawNPCs drawNPCsListener = null;
    DrawObjects drawObjectsListener = null;
    DrawPlayers drawPlayersListener = null;
    DrawSettings drawSettingsListener = null;
    DrawWeb drawWebListener = null;

    @Getter
    private boolean drawMouse = false;
    @Getter
    private boolean drawMouseTrail = false;
    @Getter
    private boolean enableMouse = false;
    @Getter
    private boolean drawBoundaries = false;
    @Getter
    private boolean drawGround = false;
    @Getter
    private boolean drawInventory = false;
    @Getter
    private boolean drawNPCs = false;
    @Getter
    private boolean drawObjects = false;
    @Getter
    private boolean drawPlayers = false;
    @Getter
    private boolean drawSettings = false;
    @Getter
    private boolean drawWeb = false;
    @Getter
    Level logLevel = Level.INFO;
    private ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

    public DebugSettingsListener(BotLite bot) {
        this.bot = bot;
        mouseInputBlockerListener = new MouseInputBlocker(bot);
        mouseMotionBlockerListener = new MouseMotionBlocker(bot);
    }

    public void enableDebugs() {
        ScriptHandler scriptHandler = bot.getScriptHandler();
        EventManager eventManager = bot.getEventManager();

        if (drawMouseListener == null && drawMouse) {
            eventManager.addListener(drawMouseListener = new DrawMouse(bot));
        }
        if (drawMouseTrailListener == null && drawMouseTrail) {
            eventManager.addListener(drawMouseTrailListener = new DrawMouseTrail(bot));
        }

        if (mouseInputBlockerListener != null) {
            mouseInputBlockerListener.setInput(enableMouse);
        }
        if (mouseMotionBlockerListener != null) {
            mouseMotionBlockerListener.setInput(enableMouse);
        }
    }

    public void disableDebugs() {
        ScriptHandler scriptHandler = bot.getScriptHandler();
        EventManager eventManager = bot.getEventManager();

        if (drawMouseListener != null) {
            eventManager.removeListener(drawMouseListener);
            drawMouseListener = null;
        }
        if (drawBoundariesListener != null) {
            eventManager.removeListener(drawBoundariesListener);
            drawBoundariesListener = null;
        }

        if (mouseInputBlockerListener != null) {
            mouseInputBlockerListener.setInput(true);
        }
        if (mouseMotionBlockerListener != null) {
            mouseMotionBlockerListener.setInput(true);
        }
    }

    public void updateDebugs() {
        updateDebugs(false);
    }

    public void updateDebugs(boolean scriptStarting) {
        ScriptHandler scriptHandler = bot.getScriptHandler();
        EventManager eventManager = bot.getEventManager();

        if (root.getLevel() != logLevel) {
            root.setLevel(logLevel);
        }

        if (bot.getScriptHandler().hasScriptsRunning() || scriptStarting) {
            enableDebugs();
        } else {
            disableDebugs();
        }

        if (drawBoundariesListener == null && drawBoundaries) {
            eventManager.addListener(drawBoundariesListener = new DrawBoundaries(bot));
        }
        if (drawBoundariesListener != null && !drawBoundaries) {
            eventManager.removeListener(drawBoundariesListener);
            drawBoundariesListener = null;
        }
        if (drawGroundListener == null && drawGround) {
            eventManager.addListener(drawGroundListener = new DrawGround(bot));
        }
        if (drawGroundListener != null && !drawGround) {
            eventManager.removeListener(drawGroundListener);
            drawGroundListener = null;
        }
        if (drawInventoryListener == null && drawInventory) {
            eventManager.addListener(drawInventoryListener = new DrawInventory(bot));
        }
        if (drawInventoryListener != null && !drawInventory) {
            eventManager.removeListener(drawInventoryListener);
            drawInventoryListener = null;
        }
        if (drawNPCsListener == null && drawNPCs) {
            eventManager.addListener(drawNPCsListener = new DrawNPCs(bot));
        }
        if (drawNPCsListener != null && !drawNPCs) {
            eventManager.removeListener(drawNPCsListener);
            drawNPCsListener = null;
        }
        if (drawObjectsListener == null && drawObjects) {
            eventManager.addListener(drawObjectsListener = new DrawObjects(bot));
        }
        if (drawObjectsListener != null && !drawObjects) {
            eventManager.removeListener(drawObjectsListener);
            drawObjectsListener = null;
        }
        if (drawPlayersListener == null && drawPlayers) {
            eventManager.addListener(drawPlayersListener = new DrawPlayers(bot));
        }
        if (drawPlayersListener != null && !drawPlayers) {
            eventManager.removeListener(drawPlayersListener);
            drawPlayersListener = null;
        }
        if (drawSettingsListener == null && drawSettings) {
            eventManager.addListener(drawSettingsListener = new DrawSettings(bot));
        }
        if (drawSettingsListener != null && !drawSettings) {
            eventManager.removeListener(drawSettingsListener);
            drawSettingsListener = null;
        }
        if (drawWebListener == null && drawWeb) {
            eventManager.addListener(drawWebListener = new DrawWeb(bot));
        }
        if (drawWebListener != null && !drawWeb) {
            eventManager.removeListener(drawWebListener);
            drawWebListener = null;
        }
    }
    @Override
    public void scriptStarted(ScriptHandler handler, Script script) {
        updateDebugs(true);
    }

    @Override
    public void scriptStopped(ScriptHandler handler, Script script) {
        updateDebugs();
    }

    @Override
    public void scriptResumed(ScriptHandler handler, Script script) {
        updateDebugs(true);
    }

    @Override
    public void scriptPaused(ScriptHandler handler, Script script) {
        updateDebugs();
    }

    @Override
    public void inputChanged(BotLite bot, int mask) {

    }

    public void setDrawMouse(boolean drawMouse) {
        this.drawMouse = drawMouse;
        updateDebugs();
    }

    public void setDrawMouseTrail(boolean drawMouseTrail) {
        this.drawMouseTrail = drawMouseTrail;
        updateDebugs();
    }

    public void setEnableMouse(boolean enableMouse) {
        this.enableMouse = enableMouse;
        updateDebugs();
    }

    public void setDrawBoundaries(boolean drawBoundaries) {
        this.drawBoundaries = drawBoundaries;
        updateDebugs();
    }

    public void setDrawGround(boolean drawGround) {
        this.drawGround = drawGround;
        updateDebugs();
    }

    public void setDrawInventory(boolean drawInventory) {
        this.drawInventory = drawInventory;
        updateDebugs();
    }

    public void setDrawNPCs(boolean drawNPCs) {
        this.drawNPCs = drawNPCs;
        updateDebugs();
    }

    public void setDrawObjects(boolean drawObjects) {
        this.drawObjects = drawObjects;
        updateDebugs();
    }

    public void setDrawPlayers(boolean drawPlayers) {
        this.drawPlayers = drawPlayers;
        updateDebugs();
    }

    public void setDrawSettings(boolean drawSettings) {
        this.drawSettings = drawSettings;
        updateDebugs();
    }

    public void setDrawWeb(boolean drawWeb) {
        this.drawWeb = drawWeb;
        updateDebugs();
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
        updateDebugs();
    }
}
