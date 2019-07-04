package net.runelite.client.rsb.internal.listener;

import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.script.Script;
import net.runelite.client.rsb.internal.ScriptHandler;

/**
 * @author Jacmob
 */
public interface ScriptListener {

	public void scriptStarted(ScriptHandler handler, Script script);

	public void scriptStopped(ScriptHandler handler, Script script);

	public void scriptResumed(ScriptHandler handler, Script script);

	public void scriptPaused(ScriptHandler handler, Script script);

	public void inputChanged(RuneLite bot, int mask);

}
