package rsb.internal.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public abstract class Focus implements FocusListener {
	public abstract void _focusGained(FocusEvent e);

	public abstract void _focusLost(FocusEvent e);

	public final void focusGained(final FocusEvent e) {
		_focusGained(e);
	}

	public final void focusLost(final FocusEvent e) {
		_focusLost(e);
	}
}
