package net.runelite.rsb.internal.listener;

import net.runelite.rsb.internal.PassiveScriptHandler;
import net.runelite.rsb.script.PassiveScript;

public interface PassiveScriptListener {

	public void scriptStarted(PassiveScriptHandler handler, PassiveScript script);

	public void scriptStopped(PassiveScriptHandler handler, PassiveScript script);
}
