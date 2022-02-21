package rsb.event.events;

import lombok.extern.java.Log;
import rsb.event.EventMulticaster;
import rsb.event.listener.PaintListener;
import rsb.methods.Players;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.EventListener;

/**
 * A paint update event.
 */
public class PaintEvent extends RSEvent {

	private static final long serialVersionUID = -7404828108740551228L;

	public Graphics graphics;

	@Override
	public void dispatch(final EventListener el) {
		final Graphics2D g2d = (Graphics2D) graphics;

		// Store settings
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
		((PaintListener) el).onRepaint(graphics);

		// Draw bot's mouse
		if (Players.methods.mouse.isPressed()) {
			graphics.setColor(new Color(255, 255, 0, 175));
			graphics.fillOval(Players.methods.mouse.getLocation().getX(), Players.methods.mouse.getLocation().getY(), 7, 7);
			graphics.setColor(new Color(0, 255, 255, 175));
			graphics.drawOval(Players.methods.mouse.getLocation().getX(), Players.methods.mouse.getLocation().getY(), 7, 7);
		} else {
			graphics.setColor(new Color(0, 255, 255, 175));
			graphics.fillOval(Players.methods.mouse.getLocation().getX(), Players.methods.mouse.getLocation().getY(), 7, 7);
			graphics.setColor(new Color(255, 255, 0, 175));
			graphics.drawOval(Players.methods.mouse.getLocation().getX(), Players.methods.mouse.getLocation().getY(), 7, 7);
		}


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
		return EventMulticaster.PAINT_EVENT;
	}

}
