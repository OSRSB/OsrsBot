package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.internal.input.VirtualMouse;
import rsb.event.listener.TextPaintListener;
import rsb.methods.MethodContext;
import rsb.util.StringUtil;

import java.awt.*;

public class TMousePosition implements TextPaintListener {

	private final MethodContext ctx;

	public TMousePosition(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	public int drawLine(final Graphics render, int idx) {
		final VirtualMouse mouse = ctx.virtualMouse;
		if (mouse != null) {
			final int mouse_x = mouse.getClientX();
			final int mouse_y = mouse.getClientY();
			String off = mouse.isClientPresent() ? "" : " (off)";
			StringUtil.drawLine(render, idx++, "Mouse Position: (" + mouse_x + "," + mouse_y + ")" + off);
		}

		return idx;
	}
}
