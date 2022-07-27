package net.runelite.rsb.internal.wrappers;

import net.runelite.rsb.wrappers.RSTile;

import java.util.ArrayList;
import java.util.List;

public class TileFlags {
	public interface Keys {
		int TILE_CLEAR = 0;
		int TILE_WATER = 1280;
		int WALL_NORTH_WEST = 1;
		int WALL_NORTH = 2;
		int WALL_NORTH_EAST = 4;
		int WALL_EAST = 8;
		int WALL_SOUTH_EAST = 10;
		int WALL_SOUTH = 20;
		int WALL_SOUTH_WEST = 40;
		int WALL_WEST = 80;
		int BLOCKED = 100;
	}

	public interface Flags {
		int WALL_NORTH_WEST = 0x1;
		int WALL_NORTH = 0x2;
		int WALL_NORTH_EAST = 0x4;
		int WALL_EAST = 0x8;
		int WALL_SOUTH_EAST = 0x10;
		int WALL_SOUTH = 0x20;
		int WALL_SOUTH_WEST = 0x40;
		int WALL_WEST = 0x80;
		int BLOCKED = 0x100;
		int WATER = 0x1280100;
	}

	private RSTile tile;
	private List<Integer> keys = new ArrayList<>();

	public TileFlags(RSTile tile) {
		this.tile = tile;
	}

	public RSTile getTile() {
		return tile;
	}

	public boolean isQuestionable() {
		return keys.size() > 0 && !keys.contains(Keys.TILE_CLEAR);
	}

	public boolean isWalkable() {
		return !keys.contains(Keys.TILE_WATER) && !keys.contains(Keys.BLOCKED);
	}

	public boolean isWater() {
		return keys.contains(Keys.TILE_WATER);
	}

	public void addKey(final int key) {
		keys.add(key);
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		for (int flag : keys) {
			strBuilder.append(flag).append("=");
		}
		return tile.getWorldLocation().getX() + "," + tile.getWorldLocation().getY() + "," + tile.getWorldLocation().getPlane() + "tile=data" + strBuilder;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TileFlags tileFlags) {
			return getTile().equals(tileFlags.getTile()) && flagsEqual(tileFlags, this);
		}
		return false;
	}

	private static boolean flagsEqual(final TileFlags tileFlag1, final TileFlags tileFlag2) {
		if (tileFlag1.keys.size() == tileFlag2.keys.size()) {
			for (int flag : tileFlag1.keys) {
				if (!tileFlag2.keys.contains(flag)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}