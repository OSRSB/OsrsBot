package rsb.internal.listener;

import rsb.botLauncher.RuneLite;
import rsb.script.Script;
import rsb.internal.ScriptHandler;

/**
 * @author GigiaJ
 */
public interface ScriptListener {

	public void scriptStarted(ScriptHandler handler, Script script);

	public void scriptStopped(ScriptHandler handler, Script script);

	public void scriptResumed(ScriptHandler handler, Script script);

	public void scriptPaused(ScriptHandler handler, Script script);

	public void inputChanged(RuneLite bot, int mask);

}
