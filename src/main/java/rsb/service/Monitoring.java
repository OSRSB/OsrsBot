package rsb.service;


/**
 * @author Paris
 */
public class Monitoring {
	public static enum Event {
		Start, Finish, Random, Script, Hack
	}

	public static void RandomStarted(final String name) {
		RaiseEvent(Event.Random, new String[]{name});
	}

	public static void RandomFinished(final String name, final boolean passed) {
		RaiseEvent(Event.Random, new String[]{name, Boolean.toString(passed)});
	}

	public static void RaiseEvent(final Event type, String[] params) {
		//TODO Create the sending of an event.
	}
}
