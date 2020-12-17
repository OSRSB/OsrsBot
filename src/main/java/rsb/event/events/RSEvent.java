package rsb.event.events;

import java.util.EventListener;
import java.util.EventObject;

public abstract class RSEvent extends EventObject {

	private static final long serialVersionUID = 6977096569226837605L;

	private static final Object SOURCE = new Object();

	public RSEvent() {
		super(RSEvent.SOURCE);
	}

	public abstract void dispatch(EventListener el);

	public abstract long getMask();

}
