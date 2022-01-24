package rsb.wrappers;

import rsb.methods.MethodContext;
import rsb.methods.MethodProvider;

import java.util.EnumSet;

/**
 * Represents a path to walk along in game.
 *
 * @author GigiaJ
 */
public abstract class RSPath extends MethodProvider {

	/**
	 * Defines the path traversal options.
	 */
	public static enum TraversalOption {
		HANDLE_RUN, SPACE_ACTIONS
	}

	public RSPath(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Takes a step along this path if appropriate.
	 * If the path cannot be traversed due to the player
	 * being too far from its vertices or already at the
	 * end vertex, <tt>false</tt> will be returned. In all
	 * other cases, <tt>true</tt> will be returned, but
	 * an action will not necessarily be performed (based
	 * on the given options).
	 *
	 * @param options Walking style options.
	 * @return <tt>true</tt> if this path is currently
	 *         valid for the player; otherwise <tt>false</tt>.
	 */
	public abstract boolean traverse(EnumSet<TraversalOption> options);

	/**
	 * Takes a step along this path if appropriate.
	 * Specifies only TraversalOption.SPACE_ACTIONS.
	 *
	 * @return <tt>true</tt> if this path is currently
	 *         valid for the player; otherwise <tt>false</tt>.
	 * @see #traverse(EnumSet)
	 */
	public boolean traverse() {
		return traverse(EnumSet.of(
				TraversalOption.HANDLE_RUN,
				TraversalOption.SPACE_ACTIONS));
	}

	/**
	 * Checks whether or not this path can be traversed
	 * by the player. This will be the case provided
	 * that the player near to one of its vertices, but
	 * not already standing on the end vertex.
	 *
	 * @return <tt>true</tt> if the player can walk
	 *         along this path; otherwise <tt>false</tt>.
	 */
	public abstract boolean isValid();

	/**
	 * Gets the next immediately available vertex
	 * in this path.
	 *
	 * @return The next walkable <code>RSTile</code>.
	 */
	public abstract RSTile getNext();

	/**
	 * Gets the start tile of this path.
	 *
	 * @return The start <code>RSTile</code>.
	 */
	public abstract RSTile getStart();

	/**
	 * Gets the end tile of this path.
	 *
	 * @return The end <code>RSTile</code>.
	 */
	public abstract RSTile getEnd();

}
