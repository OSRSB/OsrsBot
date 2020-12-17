package rsb.event.listener;

import rsb.event.events.MessageEvent;

import java.util.EventListener;

public interface MessageListener extends EventListener {
	abstract void messageReceived(MessageEvent e);
}
