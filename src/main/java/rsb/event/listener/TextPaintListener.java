package rsb.event.listener;

import java.awt.*;
import java.util.EventListener;

public interface TextPaintListener extends EventListener {
	/**
	 *	Draws at the line at the index
	 * @param render	the graphic renderer for the bot singleton
	 * @param i			the index for the line to draw from
	 * @return  the line number + number of lines actually drawn.
	 */
	public int drawLine(Graphics render, int i);
}
