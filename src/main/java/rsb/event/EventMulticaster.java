package rsb.event;

import rsb.event.events.RSEvent;
import rsb.event.listener.PaintListener;
import rsb.event.listener.TextPaintListener;
import rsb.event.listener.MessageListener;
import rsb.event.listener.CharacterMovedListener;

import java.awt.event.*;
import java.util.*;

public class EventMulticaster implements EventListener {

	public static final long FOCUS_EVENT = 0x10;
	public static final long KEY_EVENT = 0x08;

	public static final long MOUSE_EVENT = 0x01;
	public static final long MOUSE_MOTION_EVENT = 0x02;
	public static final long MOUSE_WHEEL_EVENT = 0x04;

	public static final long CHARACTER_MOVED_EVENT = 0x400;
	public static final long SERVER_MESSAGE_EVENT = 0x600;
	public static final long MESSAGE_EVENT = 0x800;
	public static final long PAINT_EVENT = 0x1000;
	public static final long TEXT_PAINT_EVENT = 0x2000;

	private static final Object treeLock = new Object();

	/**
	 * Gets the default mask for an event listener.
	 * @param el an EventListener
	 * @return the integer value representing the mask for this object
	 */
	@SuppressWarnings("deprecation")
	public static long getDefaultMask(EventListener el) {
		if (el instanceof EventMulticaster) {
			EventMulticaster em = (EventMulticaster) el;
			return em.enabledMask;
		}
		int mask = 0;
		if (el instanceof MouseListener) {
			mask |= EventMulticaster.MOUSE_EVENT;
		}
		if (el instanceof MouseMotionListener) {
			mask |= EventMulticaster.MOUSE_MOTION_EVENT;
		}
		if (el instanceof MouseWheelListener) {
			mask |= EventMulticaster.MOUSE_WHEEL_EVENT;
		}
		if (el instanceof KeyListener) {
			mask |= EventMulticaster.KEY_EVENT;
		}
		if (el instanceof FocusListener) {
			mask |= EventMulticaster.FOCUS_EVENT;
		}

		if (el instanceof CharacterMovedListener) {
			mask |= EventMulticaster.CHARACTER_MOVED_EVENT;
		}
		if (el instanceof MessageListener) {
			mask |= EventMulticaster.MESSAGE_EVENT;
		}
		if (el instanceof PaintListener) {
			mask |= EventMulticaster.PAINT_EVENT;
		}
		if (el instanceof TextPaintListener) {
			mask |= EventMulticaster.TEXT_PAINT_EVENT;
		}

		return mask;
	}

	/**
	 * Gets the default mask for an event.
	 * @param e an EventObject
	 * @return the integer value representing the mask for this object
	 */
	public static long getDefaultMask(EventObject e) {
		long mask = 0;
		if (e instanceof MouseEvent) {
			final MouseEvent me = (MouseEvent) e;
			switch (me.getID()) {
				case MouseEvent.MOUSE_PRESSED:
				case MouseEvent.MOUSE_RELEASED:
				case MouseEvent.MOUSE_CLICKED:
				case MouseEvent.MOUSE_ENTERED:
				case MouseEvent.MOUSE_EXITED:
					mask |= EventMulticaster.MOUSE_EVENT;
					break;

				case MouseEvent.MOUSE_MOVED:
				case MouseEvent.MOUSE_DRAGGED:
					mask |= EventMulticaster.MOUSE_MOTION_EVENT;
					break;

				case MouseEvent.MOUSE_WHEEL:
					mask |= EventMulticaster.MOUSE_WHEEL_EVENT;
					break;
			}
		} else if (e instanceof FocusEvent) {
			final FocusEvent fe = (FocusEvent) e;
			switch (fe.getID()) {
				case FocusEvent.FOCUS_GAINED:
				case FocusEvent.FOCUS_LOST:
					mask |= EventMulticaster.FOCUS_EVENT;
					break;
			}
		} else if (e instanceof KeyEvent) {
			final KeyEvent ke = (KeyEvent) e;
			switch (ke.getID()) {
				case KeyEvent.KEY_TYPED:
				case KeyEvent.KEY_PRESSED:
				case KeyEvent.KEY_RELEASED:
					mask |= EventMulticaster.KEY_EVENT;
					break;
			}
		} else if (e instanceof RSEvent) {
			final RSEvent rse = (RSEvent) e;
			mask |= rse.getMask();
		}
		return mask;
	}

	private long enabledMask;
	private final List<Long> listenerMasks = new ArrayList<Long>();

	private final List<EventListener> listeners = new ArrayList<EventListener>(5);

	private EventMulticaster parent;

	/**
	 * Adds the listener to the tree with a default mask.
	 * @param el an EventListener to add
	 */
	public void addListener(EventListener el) {
		long mask;
		if (el instanceof EventMulticaster) {
			final EventMulticaster em = (EventMulticaster) el;
			mask = em.enabledMask;
		} else {
			mask = EventMulticaster.getDefaultMask(el);
		}
		addListener(el, mask);
	}

	/**
	 * Adds the listener with the specified mask. If it is an EventMulticaster the
	 * specified mask will be ignored.
	 * @param el 	an EventListener to add
	 * @param mask	a mask to assign to the EventListener
	 */
	public void addListener(EventListener el, long mask) {
		synchronized (EventMulticaster.treeLock) {
			if (listeners.contains(el)) {
				return;
			}

			if (el instanceof EventMulticaster) {
				final EventMulticaster em = (EventMulticaster) el;
				addMulticaster(em);
				mask = em.enabledMask;
			} else {
				listeners.add(el);
			}
			listenerMasks.add(mask);
			// log.info(("Added mask: " + mask + " " +
			// listenerMasks.get(listenerMasks.size()-1));
			cleanMasks();
		}
	}

	/**
	 * Ensures the multicaster tree is clean and adds it.
	 * <p/>
	 * Has to hold tree lock.
	 */
	private void addMulticaster(EventMulticaster em) {
		if (em.parent != null) {
			throw new IllegalArgumentException("adding multicaster to multiple multicasters");
		}
		for (EventMulticaster cur = this; cur != null; cur = cur.parent) {
			if (cur == em) {
				throw new IllegalArgumentException("adding multicaster's parent to itself");
			}
		}
		listeners.add(em);
		em.parent = this;
	}

	/**
	 * Walks up the tree as necessary reseting the masks to the minimum.
	 * <p/>
	 * Has to hold TreeLock.
	 */
	private void cleanMasks() {
		for (EventMulticaster cur = this; cur != null; cur = cur.parent) {
			int mask = 0;
			final int len = cur.listeners.size();
			for (int i = 0; i < len; i++) {
				final EventListener el = cur.listeners.get(i);
				long m = cur.listenerMasks.get(i);
				if (el instanceof EventMulticaster) {
					final EventMulticaster em = (EventMulticaster) el;
					if (em.enabledMask != m) {
						m = em.enabledMask;
						cur.listenerMasks.set(i, m);
					}
				}
				mask |= m;
			}
			if (mask == cur.enabledMask) {
				break;
			}
			cur.enabledMask = mask;
		}
	}

	/**
	 * Fires an event to all applicable listeners.
	 * @param e an EventObject to trigger
	 */
	public void fireEvent(EventObject e) {
		fireEvent(e, EventMulticaster.getDefaultMask(e));
	}

	/**
	 * Fires an event to all listeners, restricted by the mask.
	 * @param e 	an EventObject to trigger
	 * @param mask	a mask to act as a filter to prevent unwanted firing
	 */
	public void fireEvent(EventObject e, long mask) {
		synchronized (EventMulticaster.treeLock) {
			final int len = listeners.size();
			for (int i = 0; i < len; i++) {
				long m = listenerMasks.get(i);
				if (m != 12288 && (m & mask) == 0) {
					continue;
				}
				EventListener el = listeners.get(i);
				if (e instanceof MouseEvent) {
					MouseEvent me = (MouseEvent) e;
					switch (me.getID()) {
						case MouseEvent.MOUSE_PRESSED:
							((MouseListener) el).mousePressed(me);
							break;
						case MouseEvent.MOUSE_RELEASED:
							((MouseListener) el).mouseReleased(me);
							break;
						case MouseEvent.MOUSE_CLICKED:
							((MouseListener) el).mouseClicked(me);
							break;
						case MouseEvent.MOUSE_ENTERED:
							((MouseListener) el).mouseEntered(me);
							break;
						case MouseEvent.MOUSE_EXITED:
							((MouseListener) el).mouseExited(me);
							break;
						case MouseEvent.MOUSE_MOVED:
							((MouseMotionListener) el).mouseMoved(me);
							break;
						case MouseEvent.MOUSE_DRAGGED:
							((MouseMotionListener) el).mouseDragged(me);
							break;
						case MouseEvent.MOUSE_WHEEL:
							((MouseWheelListener) el).mouseWheelMoved((MouseWheelEvent) me);
							break;
					}
				} else if (e instanceof FocusEvent) {
					FocusEvent fe = (FocusEvent) e;
					switch (fe.getID()) {
						case FocusEvent.FOCUS_GAINED:
							((FocusListener) el).focusGained(fe);
							break;
						case FocusEvent.FOCUS_LOST:
							((FocusListener) el).focusLost(fe);
							break;
					}
				} else if (e instanceof KeyEvent) {
					KeyEvent ke = (KeyEvent) e;
					switch (ke.getID()) {
						case KeyEvent.KEY_TYPED:
							((KeyListener) el).keyTyped(ke);
							break;
						case KeyEvent.KEY_PRESSED:
							((KeyListener) el).keyPressed(ke);
							break;
						case KeyEvent.KEY_RELEASED:
							((KeyListener) el).keyReleased(ke);
							break;
					}
				} else if (e instanceof RSEvent) {
					RSEvent rse = (RSEvent) e;
					rse.dispatch(el);
				}
			}
		}
	}

	/**
	 * Gets the masks enabled for this multicaster.
	 * @return the mask for the multicaster
	 */
	public long getEnabledMask() {
		return enabledMask;
	}

	/**
	 * Returns an unmodifiable list of the backing list of listeners.
	 * @return the listeners on this multicaster
	 */
	public List<EventListener> getListeners() {
		return Collections.unmodifiableList(listeners);
	}

	/**
	 * Returns whether the mask is enabled on this multicaster.
	 * @param mask	the mask to check for on the multicaster
	 * @return 	<tt>true</tt> if the mask is found; otherwise <tt>false</tt>
	 */
	public final boolean isEnabled(long mask) {
		return (enabledMask & mask) != 0;
	}

	/**
	 * Removes a listener. Cleans up the masks.
	 * @param el an EventListener to remove
	 */
	public void removeListener(EventListener el) {
		synchronized (EventMulticaster.treeLock) {
			final int idx = listeners.indexOf(el);
			if (idx == -1) {
				return;
			}
			el = listeners.remove(idx);
			if (el instanceof EventMulticaster) {
				final EventMulticaster em = (EventMulticaster) el;
				em.parent = null;
			}
			listenerMasks.remove(idx);
			cleanMasks();
		}
	}
}
