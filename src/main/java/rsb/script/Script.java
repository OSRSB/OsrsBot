package rsb.script;

import lombok.extern.slf4j.Slf4j;
import rsb.methods.MethodContext;
import rsb.methods.Methods;
import rsb.util.Timer;
import rsb.botLauncher.RuneLite;


import rsb.plugin.AccountManager;
import rsb.internal.BreakHandler;
import rsb.event.EventMulticaster;
import rsb.event.listener.PaintListener;
import rsb.script.randoms.LoginBot;


import java.util.EventListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class Script extends Methods implements EventListener, Runnable {

	Set<Script> delegates = new HashSet<Script>();
	public MethodContext ctx;

	private volatile boolean running = false;
	private volatile boolean paused = false;
	private volatile boolean random = false;

	private int id = -1;
	private long lastNotice;

	/**
	 * Finalized to cause errors intentionally to avoid confusion
	 * (yea I know how to deal with these script writers ;)).
	 *
	 * @param map The arguments passed in from the description.
	 * @return <tt>true</tt> if the script can start.
	 * @deprecated Use {@link #onStart()} instead.
	 */
	@Deprecated
	public final boolean onStart(Map<String, String> map) {
		return true;
	}

	/**
	 * Called before loop() is first called, after this script has
	 * been initialized with all method providers. Override to
	 * perform any initialization or prevent script start.
	 *
	 * @return <tt>true</tt> if the script can start.
	 */
	public boolean onStart() {
		return true;
	}

	/**
	 * Called when a break is initiated, before the logout.
	 * Override it to implement in your script.
	 *
	 * @return <tt>true</tt> if a break can be initiated.
	 */
	public boolean onBreakStart() {
		return true;
	}

	/**
	 * Called when a break is initiated, before the logout.
	 * Override it to implement in your script.
	 */
	public void onBreakFinish() {

	}

	/**
	 * The main loop. Called if you return true from onStart, then continuously until
	 * a negative integer is returned or the script stopped externally. When this script
	 * is paused this method will not be called until the script is resumed. Avoid causing
	 * execution to pause using sleep() within this method in favor of returning the number
	 * of milliseconds to sleep. This ensures that pausing and anti-randoms perform normally.
	 *
	 * @return The number of milliseconds that the manager should sleep before
	 *         calling it again. Returning a negative number will deactivate the script.
	 */
	public abstract int loop();

	/**
	 * Override to perform any clean up on script stopScript.
	 */
	public void onFinish() {

	}

	/**
	 * Initializes this script with another script's
	 * context.
	 *
	 * @param script The context providing Script.
	 * @see #delegateTo(Script)
	 */
	public final void init(Script script) {
		init(script.ctx);
	}

	/**
	 * Initializes this script with a given context.
	 *
	 * @param ctx The MethodContext.
	 */
	public final void init(MethodContext ctx) {
		super.init(ctx);
		this.ctx = ctx;
	}

	/**
	 * Initializes the provided script with this script's
	 * method context and adds the delegate as a listener
	 * to the event manager, allowing it to receive client
	 * events. The script will be stored as a delegate of
	 * this script and removed from the event manager when
	 * this script is stopped. The onStart(), loop() and
	 * onFinish() methods are not automatically called on
	 * the delegate.
	 *
	 * @param script The script to delegate to.
	 */
	public final void delegateTo(Script script) {
		script.init(ctx);
		ctx.runeLite.getEventManager().addListener(script);
		delegates.add(script);
	}

	/**
	 * For internal use only. Deactivates this script if
	 * the appropriate id is provided.
	 *
	 * @param id The id from ScriptHandler.
	 */
	public final void deactivate(int id) {
		if (id != this.id) {
			throw new IllegalStateException("Invalid id!");
		}
		this.running = false;
	}

	/**
	 * For internal use only. Sets the pool id of this script.
	 *
	 * @param id The id from ScriptHandler.
	 */
	public final void setID(int id) {
		if (this.id != -1) {
			throw new IllegalStateException("Already added to pool!");
		}
		this.id = id;
	}

	/**
	 * Pauses/resumes this script.
	 *
	 * @param paused <tt>true</tt> to pause; <tt>false</tt> to resume.
	 */
	public final void setPaused(boolean paused) {
		if (running && !random) {
			if (paused) {
				blockEvents(true);
			} else {
				unblockEvents();
			}
		}
		this.paused = paused;
	}

	/**
	 * Returns whether or not this script is paused.
	 *
	 * @return <tt>true</tt> if paused; otherwise <tt>false</tt>.
	 */
	public final boolean isPaused() {
		return paused;
	}

	/**
	 * Returns whether or not this script has started and not stopped.
	 *
	 * @return <tt>true</tt> if running; otherwise <tt>false</tt>.
	 */
	public final boolean isRunning() {
		return running;
	}

	/**
	 * Returns whether or not the loop of this script is able to
	 * receive control (i.e. not paused, stopped or in random).
	 *
	 * @return <tt>true</tt> if active; otherwise <tt>false</tt>.
	 */
	public final boolean isActive() {
		return running && !paused && !random;
	}

	/**
	 * Stops the current script without logging out.
	 */
	public void stopScript() {
		stopScript(false);
	}

	/**
	 * Stops the current script; player can be logged out before
	 * the script is stopped.
	 *
	 * @param logout <tt>true</tt> if the player should be logged
	 *               out before the script is stopped.
	 */
	public void stopScript(boolean logout) {
		log.info("Script stopping...");
		if (logout) {
			if (bank.isOpen()) {
				bank.close();
			}
			if (game.isLoggedIn()) {
				game.logout();
			}
		}
		this.running = false;
	}

	public final void run() {
		boolean start = false;
		try {
			start = onStart();
		} catch (ThreadDeath ignored) {
			log.error("Thread died", ignored);
		} catch (Throwable ex) {
			log.error("Error starting script: ", ex);
		}
		if (start) {
			running = true;
			ctx.runeLite.getEventManager().addListener(this);
			log.info("Script started.");
			try {
				while (running) {
					if (!paused) {
						if (AccountManager.isTakingBreaks(account.getName())) {
							BreakHandler h = ctx.runeLite.getBreakHandler();
							if (h.isBreaking()) {
								if (System.currentTimeMillis() - lastNotice > 600000) {
									lastNotice = System.currentTimeMillis();
									log.info("Breaking for " + Timer.format(h.getBreakTime()));
								}
								if (game.isLoggedIn() && h.getBreakTime() > 60000) {
									game.logout();
								}
								try {
									sleep(5000);
								} catch (ThreadDeath td) {
									break;
								}
								continue;
							} else {
								h.tick();
							}
						}
						if (checkForRandoms()) {
							continue;
						}
						int timeOut = -1;
						try {
							timeOut = loop();
						} catch (ThreadDeath td) {
							break;
						} catch (Exception ex) {
							log.warn("Uncaught exception from script: ", ex);
						}
						if (timeOut == -1) {
							break;
						}
						try {
							sleep(timeOut);
						} catch (ThreadDeath td) {
							break;
						}
					} else {
						try {
							sleep(1000);
						} catch (ThreadDeath td) {
							break;
						}
					}
				}
				try {
					onFinish();
				} catch (ThreadDeath ignored) {
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			} catch (Throwable t) {
				log.error("Throwable: ", t);
				onFinish();
			}
			running = false;
			log.info("Script stopped.");
		} else {
			log.error("Failed to start up.");
		}
		mouse.moveOffScreen();
		for (Script s : delegates) {
			ctx.runeLite.getEventManager().removeListener(s);
		}
		delegates.clear();
		ctx.runeLite.getEventManager().removeListener(this);
		ctx.runeLite.getScriptHandler().stopScript(id);
		id = -1;
	}

	private boolean checkForRandoms() {
		if (ctx.runeLite.disableRandoms) {
			return false;
		}
		for (Random random : ctx.runeLite.getScriptHandler().getRandoms()) {
			if (random.isEnabled() && !(ctx.runeLite.disableAutoLogin && random instanceof LoginBot)) {
				if (random.activateCondition()) {
					this.random = true;
					blockEvents(false);
					random.run(this);
					unblockEvents();
					this.random = false;
					return true;
				}
			}
		}
		return false;
	}

	private void blockEvents(boolean paint) {
		for (Script s : delegates) {
			ctx.runeLite.getEventManager().removeListener(s);
			if (paint && s instanceof PaintListener) {
				ctx.runeLite.getEventManager().addListener(s, EventMulticaster.PAINT_EVENT);
			}
		}
		ctx.runeLite.getEventManager().removeListener(this);
		if (paint && this instanceof PaintListener) {
			ctx.runeLite.getEventManager().addListener(this, EventMulticaster.PAINT_EVENT);
		}
	}

	private void unblockEvents() {
		for (Script s : delegates) {
			ctx.runeLite.getEventManager().removeListener(s);
			ctx.runeLite.getEventManager().addListener(s);
		}
		ctx.runeLite.getEventManager().removeListener(this);
		ctx.runeLite.getEventManager().addListener(this);
	}

	public RuneLite getBot() {
		return ctx.runeLite;
	}
}
