package rsb.event.events;

import rsb.event.EventMulticaster;
import rsb.event.listener.TextPaintListener;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.EventListener;

/**
 * An event that specifies a line index and graphics
 * object on which a TextPaintListener should paint a
 * line of text.
 */
public class TextPaintEvent extends RSEvent {

	private static final long serialVersionUID = 6634362568916377937L;

	public Graphics graphics;
	public int idx;

	@Override
	public void dispatch(final EventListener el) {
		final Graphics2D g2d = (Graphics2D) graphics;

		// Backup settings
		// Which is needed, otherwise if some script does a transform without
		// cleaning
		// every other graphics call will use the same transformations.
		final Color s_background = g2d.getBackground();
		final Shape s_clip = g2d.getClip();
		final Color s_color = g2d.getColor();
		final Composite s_composite = g2d.getComposite();
		final Font s_font = g2d.getFont();
		final Paint s_paint = g2d.getPaint();
		final RenderingHints s_renderingHints = g2d.getRenderingHints();
		final Stroke s_stroke = g2d.getStroke();
		final AffineTransform s_transform = g2d.getTransform();

		// Dispatch the event
		idx = ((TextPaintListener) el).drawLine(graphics, idx);

		// Restore settings
		g2d.setBackground(s_background);
		g2d.setClip(s_clip);
		g2d.setColor(s_color);
		g2d.setComposite(s_composite);
		g2d.setFont(s_font);
		g2d.setPaint(s_paint);
		g2d.setRenderingHints(s_renderingHints);
		g2d.setStroke(s_stroke);
		g2d.setTransform(s_transform);
	}

	@Override
	public long getMask() {
		return EventMulticaster.TEXT_PAINT_EVENT;
	}

}
