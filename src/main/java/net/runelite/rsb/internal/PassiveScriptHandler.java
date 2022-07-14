package net.runelite.rsb.internal;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.client.modified.RuneLite;
import net.runelite.rsb.internal.listener.PassiveScriptListener;
import net.runelite.rsb.script.PassiveScript;
import net.runelite.rsb.script.PassiveScriptManifest;

import java.util.*;

public class PassiveScriptHandler {
	private final HashMap<Integer, PassiveScript> scripts = new HashMap<>();
	private final HashMap<Integer, Thread> scriptThreads = new HashMap<>();

	private final Set<PassiveScriptListener> listeners = Collections.synchronizedSet(new HashSet<>());

	private final BotLite bot;

	public PassiveScriptHandler(BotLite bot) {
		this.bot = bot;
	}

	public void addScriptListener(PassiveScriptListener l) {
		listeners.add(l);
	}

	public void removeScriptListener(PassiveScriptListener l) {
		listeners.remove(l);
	}

	private void addScriptToPool(PassiveScript ss, Thread t) {
		for (int off = 0; off < scripts.size(); ++off) {
			if (!scripts.containsKey(off)) {
				scripts.put(off, ss);
				ss.setID(off);
				scriptThreads.put(off, t);
				return;
			}
		}
		ss.setID(scripts.size());
		scripts.put(scripts.size(), ss);
		scriptThreads.put(scriptThreads.size(), t);
	}

	public RuneLite getBot() {
		return bot;
	}

	public Map<Integer, PassiveScript> getRunningScripts() {
		return Collections.unmodifiableMap(scripts);
	}

	public void stopScript(int id) {
		PassiveScript script = scripts.get(id);
		if (script != null) {
			script.deactivate(id);
			scripts.remove(id);
			scriptThreads.remove(id);
			for (PassiveScriptListener l : listeners) {
				l.scriptStopped(this, script);
			}
		}
	}

	public void runScript(PassiveScript script) {
		script.init(bot.getMethodContext());
		for (PassiveScriptListener l : listeners) {
			l.scriptStarted(this, script);
		}
		PassiveScriptManifest prop = script.getClass().getAnnotation(PassiveScriptManifest.class);
		Thread t = new Thread(script, "PassiveScript-" + prop.name());
		addScriptToPool(script, t);
		t.start();
	}

	public void stopAllScripts() {
		Set<Integer> theSet = scripts.keySet();
		int[] arr = new int[theSet.size()];
		int c = 0;
		for (int i : theSet) {
			arr[c] = i;
			c++;
		}
		for (int id : arr) {
			stopScript(id);
		}
	}

	public void stopScript() {
		Thread curThread = Thread.currentThread();
		for (int i = 0; i < scripts.size(); i++) {
			PassiveScript script = scripts.get(i);
			if (script != null && script.isRunning()) {
				if (scriptThreads.get(i) == curThread) {
					stopScript(i);
				}
			}
		}
		if (curThread == null) {
			throw new ThreadDeath();
		}
	}
}
