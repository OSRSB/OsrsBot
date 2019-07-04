package net.runelite.client.rsb.internal.listener;

import net.runelite.client.rsb.internal.PassiveScriptHandler;
import net.runelite.client.rsb.script.PassiveScript;

public interface PassiveScriptListener {

	public void scriptStarted(PassiveScriptHandler handler, PassiveScript script);

	public void scriptStopped(PassiveScriptHandler handler, PassiveScript script);
}
