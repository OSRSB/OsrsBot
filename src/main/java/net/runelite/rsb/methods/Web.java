package net.runelite.rsb.methods;

import net.runelite.rsb.internal.wrappers.TileFlags;
import net.runelite.rsb.wrappers.RSTile;

import java.util.HashMap;

public class Web extends MethodProvider {
	public static final HashMap<RSTile, TileFlags> map = new HashMap<>();
	public static boolean loaded = false;

	Web(final MethodContext ctx) {
		super(ctx);
	}
}
