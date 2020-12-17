package rsb.internal.input;

import net.runelite.api.MainBufferProvider;
import rsb.botLauncher.Application;
import rsb.botLauncher.RuneLite;
//import net.runelite.client.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.image.*;
import java.util.Hashtable;

public class Canvas extends java.awt.Canvas {
	public static final int GRAPHICS_DELAY = 6;
	public static final int SLOW_GRAPHICS_DELAY = 50;
	public static final int DISABLE_GRAPHICS_DELAY = 100;

	private static final long serialVersionUID = -2276037172265300477L;

	private RuneLite bot;
	private boolean toshi;

	private boolean visible;
	private boolean focused;


	public Canvas(GraphicsConfiguration c) {
		super(c);
	}

	public Canvas(java.awt.Canvas c) {
		this(c.getGraphicsConfiguration());
		this.setBounds(c.getBounds());
		this.setSize(c.getSize());
		//This may need more variables set in the future
	}

	public final Graphics getGraphics(RuneLite bot, MainBufferProvider mainBufferProvider) {
		if (bot == null) {
			if (toshi) {
				return super.getGraphics();
			} else {
				bot = Application.getBot(this);
				toshi = true;
			}
		}
		return bot.getBufferGraphics(mainBufferProvider);
	}

	@Override
	public final boolean hasFocus() {
		return focused;
	}

	@Override
	public final boolean isValid() {
		return visible;
	}

	@Override
	public final boolean isVisible() {
		return visible;
	}

	@Override
	public final boolean isDisplayable() {
		return true;
	}

	/*
	@Override
	public final Dimension getSize() {
		if (bot != null) {
			return bot.getLoader().getSize();
		}
		return Application.getPanelSize();
	}


	 */
	@Override
	public final void setVisible(boolean visible) {
		super.setVisible(visible);
		this.visible = visible;
	}

	public final void setFocused(boolean focused) {
		if (focused && !this.focused) {
			// null opposite; permanent gain, as expected when entire Applet
			// regains focus
			super.processEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED, false, null));
		} else if (this.focused) {
			// null opposite; temporary loss, as expected when entire Applet
			// loses focus
			super.processEvent(new FocusEvent(this, FocusEvent.FOCUS_LOST, true, null));
		}
		this.focused = focused;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Image createImage(int width, int height) {
		// Prevents NullPointerException when opening world map.
		// This is caused by the character loader, which creates
		// character sprites using this method (which will return
		// null as long as this canvas is not really displayed).
		int[] pixels = new int[height * width];
		DataBufferInt databufferint = new DataBufferInt(pixels, pixels.length);
		DirectColorModel directcolormodel = new DirectColorModel(32, 0xff0000, 0xff00, 255);
		WritableRaster writableraster = Raster.createWritableRaster(directcolormodel.createCompatibleSampleModel(width, height), databufferint, null);
		return new BufferedImage(directcolormodel, writableraster, false, new Hashtable());
	}

	@Override
	protected final void processEvent(AWTEvent e) {
		if (!(e instanceof FocusEvent)) {
			super.processEvent(e);
		}
	}

	private void init() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setFocused(true);
			}
		});
	}

	@Override
	public Point getMousePosition(){
		return new Point(40,500);
	}
}