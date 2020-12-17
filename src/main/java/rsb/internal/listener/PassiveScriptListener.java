package rsb.internal.listener;

import rsb.internal.PassiveScriptHandler;
import rsb.script.PassiveScript;

public interface PassiveScriptListener {

	public void scriptStarted(PassiveScriptHandler handler, PassiveScript script);

	public void scriptStopped(PassiveScriptHandler handler, PassiveScript script);
}
