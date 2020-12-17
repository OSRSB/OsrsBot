package rsb.script;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;

import rsb.event.listener.PaintListener;
import rsb.methods.MethodContext;
import net.runelite.api.Point;
import rsb.methods.Methods;
import rsb.service.Monitoring;

public abstract class Random extends Methods implements PaintListener {

	protected String name;

	private volatile boolean enabled = true;

	public int i = 50;

	public boolean up = false;

	private Script script;

	private final long timeout = random(240, 300);

//	private Color[] fadeArray = {Color.red, Color.white, Color.green, new Color(128, 0, 128), Color.getY()ellow,
//	                             Color.black, Color.orange, Color.pink};
//
//	private int currentIndex = 0;

	/**
	 * Detects whether or not this anti-random should
	 * activate.
	 *
	 * @return <tt>true</tt> if the current script
	 *         should be paused and control passed to this
	 *         anti-random's loop.
	 */
	public abstract boolean activateCondition();

	public abstract int loop();


	/**
	 * Called after the method providers for this Random
	 * become available for use in initialization.
	 */
	public void onStart() {

	}

	public void onFinish() {

	}

	/**
	 * Override to provide a time limit in seconds for
	 * this anti-random to complete.
	 *
	 * @return The number of seconds after activateCondition
	 *         returns <tt>true</tt> before the anti-random should be
	 *         detected as having failed. If this time is reached
	 *         the random and running script will be stopped.
	 */
	public long getTimeout() {
		return timeout;
	}

	@Override
	public final void init(MethodContext ctx) {
		super.init(ctx);
		onStart();
	}

	public final boolean isActive() {
		return script != null;
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Stops the current script; player can be logged out before
	 * the script is stopped.
	 *
	 * @param logout <tt>true</tt> if the player should be logged
	 *               out before the script is stopped.
	 */
	protected void stopScript(boolean logout) {
		script.stopScript(logout);
	}

	public final void run(Script ctx) {
		script = ctx;
		name = getClass().getAnnotation(ScriptManifest.class).name();
		ctx.ctx.runeLite.getEventManager().removeListener(ctx);
		for (Script s : ctx.delegates) {
			ctx.ctx.runeLite.getEventManager().removeListener(s);
		}
		ctx.ctx.runeLite.getEventManager().addListener(this);
		log("Random event started: " + name);
		Monitoring.RandomStarted(name);
		long timeout = getTimeout();
		if (timeout > 0) {
			timeout *= 1000;
			timeout += System.currentTimeMillis();
		}
		while (ctx.isRunning()) {
			try {
				int wait = loop();
				if (wait == -1) {
					break;
				} else if (timeout > 0 && System.currentTimeMillis() >= timeout) {
					log.warning("Time limit reached for " + name + ".");
					Monitoring.RandomFinished(name, false);
					ctx.stopScript();
				} else {
					sleep(wait);
				}
			} catch (Exception ex) {
				log.log(Level.SEVERE, "Uncaught exception: ", ex);
				break;
			}
		}
		script = null;
		onFinish();
		log("Random event finished: " + name);
		Monitoring.RandomFinished(name, true);
		ctx.ctx.runeLite.getEventManager().removeListener(this);
		sleep(1000);
		ctx.ctx.runeLite.getEventManager().addListener(ctx);
		for (Script s : ctx.delegates) {
			ctx.ctx.runeLite.getEventManager().addListener(s);
		}
	}

	public final void onRepaint(Graphics g) {
		Point p = mouse.getLocation();
		int w = game.getWidth(), h = game.getHeight();
		if (i >= 70 && !up) {
			i--;
		} else {
			i++;
			up = i < 130;
		}
		g.setColor(new Color(0, 255, 0, i));
		g.fillRect(0, 0, p.getX() - 1, p.getY() - 1);
		g.fillRect(p.getX() + 1, 0, w - (p.getX() + 1), p.getY() - 1);
		g.fillRect(0, p.getY() + 1, p.getX() - 1, h - (p.getY() - 1));
		g.fillRect(p.getX() + 1, p.getY() + 1, w - (p.getX() + 1), h - (p.getY() - 1));
		g.setColor(Color.RED);
		g.drawString("Random Active: " + name, 540, 20);
		g.drawString("Mouse Position: ", 540, 40);
		g.drawString("X: " + mouse.getLocation().getX(), 540, 60);
		g.drawString("Y: " + mouse.getLocation().getY(), 540, 80);
	}
}
