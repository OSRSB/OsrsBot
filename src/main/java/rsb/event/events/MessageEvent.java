package rsb.event.events;

import rsb.event.EventMulticaster;
import rsb.event.listener.MessageListener;

import java.util.EventListener;

/**
 * A message event.
 *
 * @author GigiaJ
 */
public class MessageEvent extends RSEvent {

	public static final int MESSAGE_SERVER = 0;
	public static final int MESSAGE_CHAT = 2;
	public static final int MESSAGE_PRIVATE_IN = 3;
	public static final int MESSAGE_PRIVATE_INFO = 5;
	public static final int MESSAGE_PRIVATE_OUT = 6;
	public static final int MESSAGE_CLAN_CHAT = 9;
	public static final int MESSAGE_CLIENT = 11;
	public static final int MESSAGE_EXAMINE_NPC = 28;
	public static final int MESSAGE_EXAMINE_OBJECT = 29;
	public static final int MESSAGE_TRADE_REQ = 100;
	public static final int MESSAGE_ASSIST_REQ = 102;
	public static final int MESSAGE_TRADE_INFO = 103;
	public static final int MESSAGE_ASSIST_INFO = 104;
	public static final int MESSAGE_ACTION = 109;

	private static final long serialVersionUID = -8416382326776831211L;

	private final String sender;
	private final int id;
	private final String message;

	public MessageEvent(String sender, int id, String message) {
		this.sender = sender;
		this.id = id;
		this.message = message;
	}

	@Override
	public void dispatch(final EventListener el) {
		((MessageListener) el).messageReceived(this);
	}

	@Override
	public long getMask() {
		return EventMulticaster.MESSAGE_EVENT;
	}

	public String getSender() {
		return sender;
	}

	public int getID() {
		return id;
	}

	public String getMessage() {
		return message;
	}

}
