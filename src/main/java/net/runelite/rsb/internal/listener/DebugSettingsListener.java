package net.runelite.rsb.internal.listener;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.EventManager;
import net.runelite.rsb.event.impl.*;
import net.runelite.rsb.internal.ScriptHandler;
import net.runelite.rsb.internal.input.MouseInputBlocker;
import net.runelite.rsb.internal.input.MouseMotionBlocker;
import net.runelite.rsb.script.Script;

public class DebugSettingsListener implements ScriptListener {
    BotLite bot;

    DrawMouse drawMouse = null;
    DrawMouseTrail drawMouseTrail = null;
    MouseInputBlocker mouseInputBlocker = null;
    MouseMotionBlocker mouseMotionBlocker = null;
    DrawBoundaries drawBoundaries = null;
    DrawGround drawGround = null;
    DrawInventory drawInventory = null;
    DrawNPCs drawNPCs = null;
    DrawObjects drawObjects = null;
    DrawPlayers drawPlayers = null;
    DrawSettings drawSettings = null;
    DrawWeb drawWeb = null;

    public DebugSettingsListener(BotLite bot) {
        this.bot = bot;
        mouseInputBlocker = new MouseInputBlocker(bot);
        mouseMotionBlocker = new MouseMotionBlocker(bot);
    }

    public void enableDebugs() {
        ScriptHandler scriptHandler = bot.getScriptHandler();
        EventManager eventManager = bot.getEventManager();

        if (drawMouse == null && scriptHandler.isDrawMouse()) {
            eventManager.addListener(drawMouse = new DrawMouse(bot));
        }
        if (drawMouseTrail == null && scriptHandler.isDrawMouseTrail()) {
            eventManager.addListener(drawMouseTrail = new DrawMouseTrail(bot));
        }
        if (mouseInputBlocker != null) {
            mouseInputBlocker.setInput(scriptHandler.isEnableMouse());
        }
        if (mouseMotionBlocker != null) {
            mouseMotionBlocker.setInput(scriptHandler.isEnableMouse());
        }
        if (drawBoundaries == null && scriptHandler.isDrawBoundaries()) {
            eventManager.addListener(drawBoundaries = new DrawBoundaries(bot));
        }
        if (drawGround == null && scriptHandler.isDrawGround()) {
            eventManager.addListener(drawGround = new DrawGround(bot));
        }
        if (drawInventory == null && scriptHandler.isDrawInventory()) {
            eventManager.addListener(drawInventory = new DrawInventory(bot));
        }
        if (drawNPCs == null && scriptHandler.isDrawNPCs()) {
            eventManager.addListener(drawNPCs = new DrawNPCs(bot));
        }
        if (drawObjects == null && scriptHandler.isDrawObjects()) {
            eventManager.addListener(drawObjects = new DrawObjects(bot));
        }
        if (drawPlayers == null && scriptHandler.isDrawPlayers()) {
            eventManager.addListener(drawPlayers = new DrawPlayers(bot));
        }
        if (drawSettings == null && scriptHandler.isDrawSettings()) {
            eventManager.addListener(drawSettings = new DrawSettings(bot));
        }
        if (drawWeb == null && scriptHandler.isDrawWeb()) {
            eventManager.addListener(drawWeb = new DrawWeb(bot));
        }
    }

    public void disableDebugs() {
        ScriptHandler scriptHandler = bot.getScriptHandler();
        EventManager eventManager = bot.getEventManager();

        if (drawMouse != null) {
            eventManager.removeListener(drawMouse);
            drawMouse = null;
        }
        if (drawBoundaries != null) {
            eventManager.removeListener(drawBoundaries);
            drawBoundaries = null;
        }
        if (mouseInputBlocker != null) {
            mouseInputBlocker.setInput(!scriptHandler.isEnableMouse());
        }
        if (mouseMotionBlocker != null) {
            mouseMotionBlocker.setInput(!scriptHandler.isEnableMouse());
        }
        if (drawGround != null) {
            eventManager.removeListener(drawGround);
            drawGround = null;
        }
        if (drawInventory != null) {
            eventManager.removeListener(drawInventory);
            drawInventory = null;
        }
        if (drawNPCs != null) {
            eventManager.removeListener(drawNPCs);
            drawNPCs = null;
        }
        if (drawObjects != null) {
            eventManager.removeListener(drawObjects);
            drawObjects = null;
        }
        if (drawPlayers != null) {
            eventManager.removeListener(drawPlayers);
            drawPlayers = null;
        }
        if (drawSettings != null) {
            eventManager.removeListener(drawSettings);
            drawSettings = null;
        }
        if (drawWeb != null) {
            eventManager.removeListener(drawWeb);
            drawWeb = null;
        }
    }
    @Override
    public void scriptStarted(ScriptHandler handler, Script script) {
        enableDebugs();
    }

    @Override
    public void scriptStopped(ScriptHandler handler, Script script) {
        if (!bot.getScriptHandler().hasScriptsRunning()) {
            disableDebugs();
        }
    }

    @Override
    public void scriptResumed(ScriptHandler handler, Script script) {
        enableDebugs();
    }

    @Override
    public void scriptPaused(ScriptHandler handler, Script script) {
        if (!bot.getScriptHandler().hasScriptsRunning()) {
            disableDebugs();
        }
    }

    @Override
    public void inputChanged(BotLite bot, int mask) {

    }
}
