package net.runelite.rsb.event.listener;

import net.runelite.rsb.event.events.MessageEvent;

import java.util.EventListener;

public interface MessageListener extends EventListener {
	abstract void messageReceived(MessageEvent e);
}
