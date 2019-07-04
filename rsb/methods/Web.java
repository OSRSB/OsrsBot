package net.runelite.client.rsb.methods;

import net.runelite.client.rsb.internal.wrappers.TileFlags;
import net.runelite.client.rsb.wrappers.RSTile;

import java.util.HashMap;

public class Web extends MethodProvider {
	public static final HashMap<RSTile, TileFlags> map = new HashMap<RSTile, TileFlags>();
	public static boolean loaded = false;

	Web(final MethodContext ctx) {
		super(ctx);
	}
}
