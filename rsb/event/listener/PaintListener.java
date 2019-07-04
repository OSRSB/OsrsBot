package net.runelite.client.rsb.event.listener;

import java.awt.*;
import java.util.EventListener;

public interface PaintListener extends EventListener {
	public void onRepaint(Graphics render);
}
