package rsb.wrappers;

import net.runelite.api.CollisionData;
import net.runelite.api.Tile;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import rsb.methods.MethodContext;

import java.util.*;

/**
 * @author GigiaJ
 */
public class RSLocalPath extends RSPath {

	public static final int WALL_NORTH_WEST = 0x1;
	public static final int WALL_NORTH = 0x2;
	public static final int WALL_NORTH_EAST = 0x4;
	public static final int WALL_EAST = 0x8;
	public static final int WALL_SOUTH_EAST = 0x10;
	public static final int WALL_SOUTH = 0x20;
	public static final int WALL_SOUTH_WEST = 0x40;
	public static final int WALL_WEST = 0x80;
	public static final int BLOCKED = 0x100;

	protected final RSTile end;
	protected RSTile base;
	protected int[][] flags;
	protected int offX, offY;

	private RSTilePath tilePath;

	public RSLocalPath(MethodContext ctx, RSTile end) {
		super(ctx);
		this.end = end;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean traverse(EnumSet<TraversalOption> options) {
		return getNext() != null && tilePath.traverse(options);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		return getNext() != null && !methods.players.getMyPlayer().getLocation().equals(getEnd());
	}

	/**
	 * {@inheritDoc}
	 */
	public RSTile getNext() {
		if (!methods.game.getMapBase().equals(base)) {
			int[][] flags = methods.walking.getCollisionFlags(methods.game.getPlane());
			if (flags != null) {
				base = methods.game.getMapBase();
				RSTile start = methods.players.getMyPlayer().getLocation();
				RSTile[] tiles = findPath(start, end);
				if (tiles == null) {
					base = null;
					return null;
				}
				tilePath = methods.walking.newTilePath(tiles);
			}
		}
		return tilePath.getNext();
	}

	/**
	 * {@inheritDoc}
	 */
	public RSTile getStart() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public RSTile getEnd() {
		return end;
	}

	/**
	 * Returns the calculated RSTilePath that is currently
	 * providing data to this RSLocalPath.
	 *
	 * @return The current RSTile path; or <code>null</code>.
	 */
	public RSTilePath getCurrentTilePath() {
		return tilePath;
	}

	protected class Node {

		public final int x;
		public final int y;
		public Node prev;
		public double g, f;
		public boolean border;

		public Node(int x, int y, boolean border) {
			this.border = border;
			this.x = x;
			this.y = y;
			g = f = 0;
		}

		public Node(int x, int y) {
			this.x = x;
			this.y = y;
			g = f = 0;
		}

		@Override
		public int hashCode() {
			return (x << 4) | y;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Node) {
				Node n = (Node) o;
				return x == n.x && y == n.y;
			}
			return false;
		}

		@Override
		public String toString() {
			return "(" + x + "," + y + ")";
		}

		public RSTile toRSTile(RSTile baseTile) {
			return new RSTile(x + baseTile.getWorldLocation().getX(), y + baseTile.getWorldLocation().getY(), baseTile.getWorldLocation().getPlane());
		}

	}

	protected RSTile[] findPath(RSTile start, RSTile end) {
		return findPath(start, end, false);
	}

	private RSTile[] findPath(RSTile start, RSTile end, boolean remote) {
		int base_x = base.getWorldLocation().getX(), base_y = base.getWorldLocation().getY();
		int curr_x = start.getWorldLocation().getX() - base_x, curr_y = start.getWorldLocation().getY() - base_y;
		int dest_x = end.getWorldLocation().getX() - base_x, dest_y = end.getWorldLocation().getY() - base_y;

		// load client data
		int plane = methods.game.getPlane();
		flags = methods.walking.getCollisionFlags(plane);

		// loaded region only
		if (flags == null || curr_x < 0 || curr_y < 0 || curr_x >= flags.length || curr_y >= flags.length) {
			return null;
		} else if (dest_x < 0 || dest_y < 0 || dest_x >= flags.length || dest_y >= flags.length) {
			remote = true;
			if (dest_x < 0) {
				dest_x = 0;
			} else if (dest_x >= flags.length) {
				dest_x = flags.length - 1;
			}
			if (dest_y < 0) {
				dest_y = 0;
			} else if (dest_y >= flags.length) {
				dest_y = flags.length - 1;
			}
		}

		// structs
		HashSet<Node> open = new HashSet<Node>();
		HashSet<Node> closed = new HashSet<Node>();
		Node curr = new Node(curr_x, curr_y);
		Node dest = new Node(dest_x, dest_y);

		curr.f = heuristic(curr, dest);
		open.add(curr);

		// search
		while (!open.isEmpty()) {
			curr = lowest_f(open);
			if (curr.equals(dest)) {
				// reconstruct from pred tree
				return path(curr, base);
			}
			open.remove(curr);
			closed.add(curr);
			for (Node next : successors(curr)) {
				if (!closed.contains(next)) {
					double t = curr.g + dist(curr, next);
					boolean use_t = false;
					if (!open.contains(next)) {
						open.add(next);
						use_t = true;
					} else if (t < next.g) {
						use_t = true;
					}
					if (use_t) {
						next.prev = curr;
						next.g = t;
						next.f = t + heuristic(next, dest);
					}
				}
			}
		}

		// no path
		if (!remote || methods.calc.distanceTo(end) < 10) {
			return null;
		}
		return findPath(start, pull(end));

	}

	private RSTile pull(RSTile tile) {
		RSTile p = methods.players.getMyPlayer().getLocation();
		int x = tile.getWorldLocation().getX(), y = tile.getWorldLocation().getY();
		if (p.getWorldLocation().getX() < x) {
			x -= 2;
		} else if (p.getWorldLocation().getX() > x) {
			x += 2;
		}
		if (p.getWorldLocation().getY() < y) {
			y -= 2;
		} else if (p.getWorldLocation().getY() > y) {
			y += 2;
		}
		return new RSTile(x, y, p.getWorldLocation().getPlane());
	}

	private double heuristic(Node start, Node end) {
		double dx = start.x - end.x;
		double dy = start.y - end.y;
		if (dx < 0) {
			dx = -dx;
		}
		if (dy < 0) {
			dy = -dy;
		}
		return dx < dy ? dy : dx;
		//double diagonal = dx > dy ? dy : dx;
		//double manhattan = dx + dy;
		//return 1.41421356 * diagonal + (manhattan - 2 * diagonal);
	}

	private double dist(Node start, Node end) {
		if (start.x != end.x && start.y != end.y) {
			return 1.41421356;
		} else {
			return 1.0;
		}
	}

	private Node lowest_f(Set<Node> open) {
		Node best = null;
		for (Node t : open) {
			if (best == null || t.f < best.f) {
				best = t;
			}
		}
		return best;
	}

	private RSTile[] path(Node end, RSTile baseTile) {
		LinkedList<RSTile> path = new LinkedList<RSTile>();
		Node p = end;
		while (p != null) {
			path.addFirst(p.toRSTile(baseTile));
			p = p.prev;
		}
		return path.toArray(new RSTile[path.size()]);
	}

	private List<Node> successors(Node t) {
		LinkedList<Node> tiles = new LinkedList<Node>();
		int x = t.x, y = t.y;
		int f_x = x - offX, f_y = y - offY;
		int here = flags[f_x][f_y];
		int upper = flags.length - 1;
		if (f_y > 0 && (here & WALL_SOUTH) == 0 && (flags[f_x][f_y - 1] & BLOCKED) == 0) {
			tiles.add(new Node(x, y - 1));
		}
		if (f_x > 0 && (here & WALL_WEST) == 0 && (flags[f_x - 1][f_y] & BLOCKED) == 0) {
			tiles.add(new Node(x - 1, y));
		}
		if (f_y < upper && (here & WALL_NORTH) == 0 && (flags[f_x][f_y + 1] & BLOCKED) == 0) {
			tiles.add(new Node(x, y + 1));
		}
		if (f_x < upper && (here & WALL_EAST) == 0 && (flags[f_x + 1][f_y] & BLOCKED) == 0) {
			tiles.add(new Node(x + 1, y));
		}
		if (f_x > 0 && f_y > 0 && (here & (WALL_SOUTH_WEST | WALL_SOUTH | WALL_WEST)) == 0
				&& (flags[f_x - 1][f_y - 1] & BLOCKED) == 0
				&& (flags[f_x][f_y - 1] & (BLOCKED | WALL_WEST)) == 0
				&& (flags[f_x - 1][f_y] & (BLOCKED | WALL_SOUTH)) == 0) {
			tiles.add(new Node(x - 1, y - 1));
		}
		if (f_x > 0 && f_y < upper && (here & (WALL_NORTH_WEST | WALL_NORTH | WALL_WEST)) == 0
				&& (flags[f_x - 1][f_y + 1] & BLOCKED) == 0
				&& (flags[f_x][f_y + 1] & (BLOCKED | WALL_WEST)) == 0
				&& (flags[f_x - 1][f_y] & (BLOCKED | WALL_NORTH)) == 0) {
			tiles.add(new Node(x - 1, y + 1));
		}
		if (f_x < upper && f_y > 0 && (here & (WALL_SOUTH_EAST | WALL_SOUTH | WALL_EAST)) == 0
				&& (flags[f_x + 1][f_y - 1] & BLOCKED) == 0
				&& (flags[f_x][f_y - 1] & (BLOCKED | WALL_EAST)) == 0
				&& (flags[f_x + 1][f_y] & (BLOCKED | WALL_SOUTH)) == 0) {
			tiles.add(new Node(x + 1, y - 1));
		}
		if (f_x > 0 && f_y < upper && (here & (WALL_NORTH_EAST | WALL_NORTH | WALL_EAST)) == 0
				&& (flags[f_x + 1][f_y + 1] & BLOCKED) == 0
				&& (flags[f_x][f_y + 1] & (BLOCKED | WALL_EAST)) == 0
				&& (flags[f_x + 1][f_y] & (BLOCKED | WALL_NORTH)) == 0) {
			tiles.add(new Node(x + 1, y + 1));
		}
		return tiles;
	}

}
