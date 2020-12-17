package rsb.event.listener;

import java.awt.*;
import java.util.EventListener;

public interface TextPaintListener extends EventListener {
	/**
	 * Argument is the line number to start drawing from.
	 * <p/>
	 * Returns the line number + number of lines actually drawn.
	 */
	public int drawLine(Graphics render, int idx);
}
