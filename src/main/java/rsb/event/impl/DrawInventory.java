package rsb.event.impl;

import rsb.botLauncher.RuneLite;
import rsb.event.listener.PaintListener;
import rsb.methods.Game;
import rsb.methods.GameGUI;
import rsb.methods.MethodContext;
import rsb.wrappers.RSItem;

import java.awt.*;

public class DrawInventory implements PaintListener {

	private final MethodContext ctx;

	public DrawInventory(RuneLite bot) {
		ctx = bot.getMethodContext();
	}

	public void onRepaint(final Graphics render) {
		if (!ctx.game.isLoggedIn()) {
			return;
		}

		if (ctx.game.getCurrentTab() != GameGUI.Tab.INVENTORY) {
			return;
		}

		render.setColor(Color.WHITE);
		final RSItem[] inventoryItems = ctx.inventory.getItems();

		for (RSItem inventoryItem : inventoryItems) {
			if (inventoryItem.getID() != -1) {
				final Point location = new Point (inventoryItem.getItem().getItemLocation().getX(), inventoryItem.getItem().getItemLocation().getY() + 20);
				render.drawString("" + inventoryItem.getID(), location.x, location.y);
			}
		}
	}
}
