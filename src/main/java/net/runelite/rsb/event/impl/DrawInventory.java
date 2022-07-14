package net.runelite.rsb.event.impl;

import net.runelite.rsb.botLauncher.BotLite;
import net.runelite.rsb.event.listener.PaintListener;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.wrappers.RSItem;

import java.awt.*;

public class DrawInventory implements PaintListener {

	private final MethodContext ctx;

	public DrawInventory(BotLite bot) {
		ctx = bot.getMethodContext();
	}

	public void onRepaint(final Graphics render) {
		if (!ctx.game.isLoggedIn()) {
			return;
		}

		if (ctx.game.getCurrentTab() != InterfaceTab.INVENTORY) {
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
