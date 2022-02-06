package rsb.event;

import rsb.event.events.RSEvent;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class EventManager implements Runnable {

	public static class KillEvent extends RSEvent {
		private static final long serialVersionUID = 3426050317048250049L;

		@Override
		public void dispatch(final EventListener el) {
		}

		@Override
		public long getMask() {
			return -1;
		}
	}

	private final Logger log = Logger.getLogger(EventManager.class.getName());

	private final EventMulticaster multicaster = new EventMulticaster();
	private final Map<Integer, EventObject> queue = new HashMap<Integer, EventObject>();

	private final Object threadLock = new Object();

	private Thread eventThread;

	/**
	 * Adds the event to the queue for the EventManager to process.
	 *
	 * Events are processed with the default mask.
	 *
	 * @param e The event object to dispatch.
	 */
	public void dispatchEvent(EventObject e) {
		synchronized (queue) {
			boolean added = false;
			for (int off = 0; off < queue.size(); ++off) {
				if (!queue.containsKey(off)) {
					queue.put(off, e);
					added = true;
					break;
				}
			}
			if (!added) {
				queue.put(queue.size(), e);
			}
			queue.notifyAll();
		}
	}

	/**
	 * Dispatches the given event. Calling this avoids the use
	 * of the event queue.
	 *
	 * @param event The event to fire.
	 */
	public void processEvent(EventObject event) {
		multicaster.fireEvent(event);
	}

	/**
	 * Is this thread the event thread?
	 *
	 * @return <tt>true</tt> if the thread is an event thread; otherwise <tt>false</tt>.
	 */
	public boolean isEventThread() {
		synchronized (threadLock) {
			return Thread.currentThread() == eventThread;
		}
	}

	/**
	 * Is the event thread alive?
	 *
	 * @return <tt>true</tt> if the thread is alive; otherwise <tt>false</tt>.
	 */
	public boolean isEventThreadAlive() {
		synchronized (threadLock) {
			return eventThread != null;
		}
	}

	/**
	 * Kills the event manager thread.
	 *
	 * @param wait <tt>true</tt> to wait for the kill
	 *             event to be processed before returning; otherwise
	 *             <tt>false</tt> to submit the kill event and return
	 *             immediately.
	 */
	public void killThread(boolean wait) {
		EventObject event = new KillEvent();
		synchronized (event) {
			dispatchEvent(event);
			if (wait) {
				try {
					event.wait();
				} catch (InterruptedException e) {
					log.info("wait for kill event interrupted!");
				}
			}
		}
	}

	/**
	 * Registers a listener.
	 *
	 * @param listener the listener to add.
	 */
	public void addListener(EventListener listener) {
		multicaster.addListener(listener);
	}

	/**
	 * Registers a listener.
	 *
	 * @param listener the listener to add.
	 * @param mask     the event type mask.
	 */
	public void addListener(EventListener listener, long mask) {
		multicaster.addListener(listener, mask);
	}

	/**
	 * Removes a listener.
	 *
	 * @param listener the listener to remove.
	 */
	public void removeListener(EventListener listener) {
		multicaster.removeListener(listener);
	}

	/**
	 * The thread entry point.
	 */
	public void run() {
		if (!isEventThread()) {
			throw new IllegalThreadStateException();
		}
		while (true) {
			try {
				EventObject event = null;
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (final Exception e) {
							log.info("Event Queue: " + e.toString());
						}
					}
					int emptySpots = 0;
					for (int off = 0; off < queue.size() + emptySpots; ++off) {
						if (!queue.containsKey(off)) {
							emptySpots++;
							continue;
						}
						event = queue.remove(off);
						break;
					}
				}
				if (event instanceof KillEvent) {
					eventThread = null;
					synchronized (event) {
						event.notifyAll();
					}
					return;
				}
				try {
					processEvent(event);
				} catch (final ThreadDeath td) {
					eventThread = null;
					event.notifyAll();
					return;
				} catch (final Throwable e) {
					e.printStackTrace();
				}
				synchronized (event) {
					event.notifyAll();
				}
			} catch (final Exception e) {
				log.info("Event Queue: " + e.toString());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Spawns a daemon event thread. Only one can be created unless it is
	 * killed.
	 */
	public void start() {
		synchronized (threadLock) {
			if (eventThread != null) {
				throw new IllegalThreadStateException();
			}
			eventThread = new Thread(this, "EventQueue-" + hashCode());
			eventThread.setDaemon(true);
			eventThread.start();
		}
	}
}
