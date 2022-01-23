package rsb.wrappers;

import rsb.methods.MethodContext;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * A path consisting of a list of tile waypoints.
 *
 * @author GigiaJ
 */
public class RSTilePath extends RSPath {

	protected RSTile[] tiles;
	protected RSTile[] orig;

	private boolean end;

	public RSTilePath(MethodContext ctx, RSTile[] tiles) {
		super(ctx);
		this.orig = tiles;
		this.tiles = Arrays.copyOf(tiles, tiles.length);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean traverse(EnumSet<TraversalOption> options) {
		RSTile next = getNext();
		if (next == null) {
			return false;
		}
		if (next.equals(getEnd())) {
			if (methods.calc.distanceTo(next) <= 1 || (end && methods.players.getMyPlayer().isLocalPlayerMoving()) || next.equals(
					methods.walking.getDestination())) {
				return false;
			}
			end = true;
		} else {
			end = false;
		}
		if (options != null && options.contains(
				TraversalOption.HANDLE_RUN) && !methods.walking.isRunEnabled() && methods.walking.getEnergy() > 50) {
			methods.walking.setRun(true);
			sleep(300);
		}
		if (options != null && options.contains(TraversalOption.SPACE_ACTIONS)) {
			RSTile dest = methods.walking.getDestination();
			if (dest != null && methods.players.getMyPlayer().isLocalPlayerMoving() &&
					methods.calc.distanceTo(dest) > 5 &&
					methods.calc.distanceBetween(next, dest) < 7) {
				return true;
			}
		}
		return methods.walking.walkTileMM(next, 0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		return tiles.length > 0 && getNext() != null &&
				!methods.players.getMyPlayer().getLocation().equals(getEnd());
	}

	/**
	 * {@inheritDoc}
	 */
	public RSTile getNext() {
		for (int i = tiles.length - 1; i >= 0; --i) {
			if (methods.calc.tileOnMap(tiles[i])) {
				return tiles[i];
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public RSTile getStart() {
		return tiles[0];
	}

	/**
	 * {@inheritDoc}
	 */
	public RSTile getEnd() {
		return tiles[tiles.length - 1];
	}

	/**
	 * Randomize this path. The original path is stored so
	 * this method may be called multiple times without the
	 * waypoints drifting far from their original locations.
	 *
	 * @param maxX The max deviation on the X axis
	 * @param maxY The max deviation on the Y axis
	 * @return This path.
	 */
	public RSTilePath randomize(int maxX, int maxY) {
		for (int i = 0; i < tiles.length; ++i) {
			tiles[i] = orig[i].randomize(maxX, maxY);
		}
		return this;
	}

	/**
	 * Reverses this path.
	 *
	 * @return This path.
	 */
	public RSTilePath reverse() {
		RSTile[] reversed = new RSTile[tiles.length];
		for (int i = 0; i < orig.length; ++i) {
			reversed[i] = orig[tiles.length - 1 - i];
		}
		orig = reversed;
		reversed = new RSTile[tiles.length];
		for (int i = 0; i < tiles.length; ++i) {
			reversed[i] = tiles[tiles.length - 1 - i];
		}
		tiles = reversed;
		return this;
	}

	/**
	 * Returns an array containing all of the vertices in this path.
	 *
	 * @return an array containing all of the vertices in this path.
	 */
	public RSTile[] toArray() {
		RSTile[] a = new RSTile[tiles.length];
		System.arraycopy(tiles, 0, a, 0, tiles.length);
		return a;
	}

}