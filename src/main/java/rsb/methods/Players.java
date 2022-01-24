package rsb.methods;

import net.runelite.api.Player;
import rsb.internal.wrappers.Filter;
import rsb.wrappers.RSPlayer;

import java.util.HashSet;
import java.util.Set;

/**
 * Player related operations.
 */
public class Players extends MethodProvider {

	Players(final MethodContext ctx) {
		super(ctx);
	}

	/**
	 * A filter that accepts all matches.
	 */
	public static final Filter<RSPlayer> ALL_FILTER = new Filter<RSPlayer>() {
		public boolean test(RSPlayer player) {
			return true;
		}
	};

	/**
	 * Returns an <tt>RSPlayer</tt> object representing the current player.
	 *
	 * @return An <tt>RSPlayer</tt> object representing the player.
	 */
	public RSPlayer getMyPlayer() {
		return new RSPlayer(methods, methods.client.getLocalPlayer());
	}

	/**
	 * Returns an array of all valid <tt>RSPlayer</tt>s.
	 *
	 * @return All valid RSPlayers.
	 */
	public RSPlayer[] getAll() {
		return getAll(Players.ALL_FILTER);
	}

	/**
	 * Returns an array of all valid <tt>RSPlayer</tt>s.
	 *
	 * @param filter Filters out unwanted matches.
	 * @return All valid RSPlayers.
	 */
	public RSPlayer[] getAll(final Filter<RSPlayer> filter) {
		Player[] playerArray = methods.client.getCachedPlayers();
		Set<RSPlayer> players = new HashSet<RSPlayer>();
		for (Player player : playerArray) {
			if (player != null) {
				RSPlayer rsPlayer = new RSPlayer(methods, player);
				if (filter.test(rsPlayer)) {
					players.add(rsPlayer);
				}
			}
		}
		return players.toArray(new RSPlayer[players.size()]);
	}

	/**
	 * Returns the <tt>RSPlayer</tt> that is nearest, out of all of the Players
	 * accepted by the provided filter.
	 *
	 * @param filter Filters unwanted matches.
	 * @return An <tt>RSPlayer</tt> object representing the nearest player that
	 *         was accepted by the provided Filter; or null if there are no
	 *         matching players in the current region.
	 */
	public RSPlayer getNearest(final Filter<RSPlayer> filter) {
		int min = 20;
		RSPlayer closest = null;
		Player[] players = methods.client.getCachedPlayers();
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			RSPlayer rsPlayer = new RSPlayer(methods, player);
			if (filter.test(rsPlayer)) {
				int distance = methods.calc.distanceTo(rsPlayer);
				if (distance < min) {
					min = distance;
					closest = rsPlayer;
				}
			}
		}
		return closest;
	}

	/**
	 * Returns the <tt>RSPlayer</tt> that is nearest, out of all of the Players
	 * with the provided name.
	 *
	 * @param name The name of the <tt>RSPlayer</tt> that you are searching for.
	 * @return An <tt>RSPlayer</tt> object representing the nearest player with
	 *         the provided name; or null if there are no matching players in
	 *         the current region.
	 */
	public RSPlayer getNearest(final String name) {
		return getNearest(new Filter<RSPlayer>() {
			public boolean test(RSPlayer player) {
				return player.getName().equals(name);
			}
		});
	}

	/**
	 * Returns the <tt>RSPlayer</tt> that is nearest, out of all of the Players
	 * with the provided combat level.
	 *
	 * @param level The combat level of the <tt>RSPlayer</tt> that you are
	 *              searching for.
	 * @return An <tt>RSPlayer</tt> object representing the nearest player with
	 *         the provided combat level; or null if there are no matching
	 *         players in the current region.
	 */
	public RSPlayer getNearest(final int level) {
		return getNearest(new Filter<RSPlayer>() {
			public boolean test(RSPlayer player) {
				return player.getCombatLevel() == level;
			}
		});
	}

}
