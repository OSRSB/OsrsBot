/**
 *
 */
package net.runelite.client.rsb.event.listener;

import net.runelite.client.rsb.event.events.CharacterMovedEvent;

import java.util.EventListener;

/**
 * @author Qauters
 */
public interface CharacterMovedListener extends EventListener {
	public void characterMoved(CharacterMovedEvent e);
}
