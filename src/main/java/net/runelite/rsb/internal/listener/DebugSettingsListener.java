package net.runelite.rsb.internal.listener;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.EventManager;
import net.runelite.rsb.event.impl.*;
import net.runelite.rsb.internal.ScriptHandler;
import net.runelite.rsb.script.Script;

public class DebugSettingsListener implements ScriptListener {
    BotLite bot;

    DrawMouse drawMouse = null;
    DrawBoundaries drawBoundaries = null;
    DrawGround drawGround = null;
    DrawSettings drawSettings = null;
    DrawWeb drawWeb = null;

    public DebugSettingsListener(BotLite bot) {
        this.bot = bot;
    }

    public void enableDebugs() {
        ScriptHandler scriptHandler = bot.getScriptHandler();
        EventManager eventManager = bot.getEventManager();

        if (drawMouse == null && scriptHandler.isDrawMouse()) {
            eventManager.addListener(drawMouse = new DrawMouse(bot));
        }
        if (drawBoundaries == null && scriptHandler.isDrawBoundaries()) {
            eventManager.addListener(drawBoundaries = new DrawBoundaries(bot));
        }
        if (drawGround == null && scriptHandler.isDrawGround()) {
            eventManager.addListener(drawGround = new DrawGround(bot));
        }
        if (drawSettings == null && scriptHandler.isDrawSettings()) {
            eventManager.addListener(drawSettings = new DrawSettings(bot));
        }
        if (drawWeb == null && scriptHandler.isDrawWeb()) {
            eventManager.addListener(drawWeb = new DrawWeb(bot));
        }
    }

    public void disableDebugs() {
        System.out.println("DebugSettingsListener.disableDebugs");
        EventManager eventManager = bot.getEventManager();

        if (drawMouse != null) {
            eventManager.removeListener(drawMouse);
            drawMouse = null;
        }
        if (drawBoundaries != null) {
            eventManager.removeListener(drawBoundaries);
            drawBoundaries = null;
        }
        if (drawGround != null) {
            eventManager.removeListener(drawGround);
            drawGround = null;
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
