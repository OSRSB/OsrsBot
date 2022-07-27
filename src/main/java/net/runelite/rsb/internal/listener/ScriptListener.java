package net.runelite.rsb.internal.listener;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.script.Script;
import net.runelite.rsb.internal.ScriptHandler;

/**
 * @author GigiaJ
 */
public interface ScriptListener {

	public void scriptStarted(ScriptHandler handler, Script script);

	public void scriptStopped(ScriptHandler handler, Script script);

	public void scriptResumed(ScriptHandler handler, Script script);

	public void scriptPaused(ScriptHandler handler, Script script);

	public void inputChanged(BotLite bot, int mask);

}
