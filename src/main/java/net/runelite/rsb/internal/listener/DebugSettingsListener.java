package net.runelite.rsb.internal.listener;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.EventManager;
import net.runelite.rsb.event.impl.DrawMouse;
import net.runelite.rsb.internal.ScriptHandler;
import net.runelite.rsb.script.Script;

public class DebugSettingsListener implements ScriptListener {
    BotLite bot;

    DrawMouse drawMouse = null;

    public DebugSettingsListener(BotLite bot) {
        this.bot = bot;
    }

    public void enableDebugs() {
        ScriptHandler scriptHandler = bot.getScriptHandler();
        EventManager eventManager = bot.getEventManager();

        if (drawMouse == null && scriptHandler.isDrawMouse()) {
            eventManager.addListener(drawMouse = new DrawMouse(bot));
        }
    }

    public void disableDebugs() {
        System.out.println("DebugSettingsListener.disableDebugs");
        EventManager eventManager = bot.getEventManager();

        if (drawMouse != null) {
            eventManager.removeListener(drawMouse);
            drawMouse = null;
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
