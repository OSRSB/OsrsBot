/**
 *
 */
package net.runelite.rsb.event.listener;

import net.runelite.rsb.event.events.CharacterMovedEvent;

import java.util.EventListener;

/**
 * @author GigiaJ
 */
public interface CharacterMovedListener extends EventListener {
	public void characterMoved(CharacterMovedEvent e);
}
