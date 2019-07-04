package net.runelite.client.rsb.wrappers;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a shape made of RSTiles.
 *
 * @author SpeedWing
 */
public class RSArea {

	private final Polygon area;
	private final int plane;

	/**
	 * @param tiles An Array containing of <b>RSTiles</b> forming a polygon shape.
	 * @param plane The plane of the <b>RSArea</b>.
	 */
	public RSArea(RSTile[] tiles, int plane) {
		this.area = tileArrayToPolygon(tiles);
		this.plane = plane;
	}

	/**
	 * @param tiles An Array containing of <b>RSTiles</b> forming a polygon shape.
	 */
	public RSArea(RSTile[] tiles) {
		this(tiles, 0);
	}

	/**
	 * @param sw    The <i>South West</i> <b>RSTile</b> of the <b>RSArea</b>
	 * @param ne    The <i>North East</i> <b>RSTile</b> of the <b>RSArea</b>
	 * @param plane The plane of the <b>RSArea</b>.
	 */
	public RSArea(RSTile sw, RSTile ne, int plane) {
		this(new RSTile[]{sw, new RSTile(ne.getWorldLocation().getX() + 1, sw.getWorldLocation().getY()),
				new RSTile(ne.getWorldLocation().getX() + 1, ne.getWorldLocation().getY() + 1),
				new RSTile(sw.getWorldLocation().getX(), ne.getWorldLocation().getY() + 1)}, plane);
	}

	/**
	 * @param sw The <i>South West</i> <b>RSTile</b> of the <b>RSArea</b>
	 * @param ne The <i>North East</i> <b>RSTile</b> of the <b>RSArea</b>
	 */
	public RSArea(RSTile sw, RSTile ne) {
		this(sw, ne, 0);
	}

	/**
	 * @param swX The X axle of the <i>South West</i> <b>RSTile</b> of the
	 *            <b>RSArea</b>
	 * @param swY The Y axle of the <i>South West</i> <b>RSTile</b> of the
	 *            <b>RSArea</b>
	 * @param neX The X axle of the <i>North East</i> <b>RSTile</b> of the
	 *            <b>RSArea</b>
	 * @param neY The Y axle of the <i>North East</i> <b>RSTile</b> of the
	 *            <b>RSArea</b>
	 */
	public RSArea(int swX, int swY, int neX, int neY) {
		this(new RSTile(swX, swY), new RSTile(neX, neY), 0);
	}

	/**
	 * @param x The x location of the <b>RSTile</b> that will be checked.
	 * @param y The y location of the <b>RSTile</b> that will be checked.
	 * @return True if the <b>RSArea</b> contains the given <b>RSTile</b>.
	 */
	public boolean contains(int x, int y) {
		return this.contains(new RSTile(x, y));
	}

	/**
	 * @param plane The plane to check.
	 * @param tiles The <b>RSTile(s)</b> that will be checked.
	 * @return True if the <b>RSArea</b> contains the given <b>RSTile(s)</b>.
	 */
	public boolean contains(int plane, RSTile... tiles) {
		return this.plane == plane && this.contains(tiles);
	}

	/**
	 * @param tiles The <b>RSTile(s)</b> that will be checked.
	 * @return True if the <b>RSArea</b> contains the given <b>RSTile(s)</b>.
	 */
	public boolean contains(RSTile... tiles) {
		RSTile[] areaTiles = this.getTileArray();
		for (RSTile check : tiles) {
			for (RSTile space : areaTiles) {
				if (check.equals(space)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return The central <b>RSTile</b> of the <b>RSArea</b>.
	 */
	public RSTile getCentralTile() {
		if (area.npoints < 1) {
			return null;
		}
		int totalX = 0, totalY = 0;
		for (int i = 0; i < area.npoints; i++) {
			totalX += area.xpoints[i];
			totalY += area.ypoints[i];
		}
		return new RSTile(Math.round(totalX / area.npoints),
				Math.round(totalY / area.npoints));
	}

	/**
	 * @param base The base tile to measure the closest tile off of.
	 * @return The nearest <b>RSTile</b> in the <b>RSArea</b>
	 *         to the given <b>RSTile</b>.
	 */
	public RSTile getNearestTile(RSTile base) {
		RSTile[] tiles = this.getTileArray();
		RSTile cur = null;
		double dist = -1;
		for (RSTile tile : tiles) {
			double distTmp = distanceBetween(tile, base);
			if (cur == null) {
				dist = distTmp;
				cur = tile;
			} else if (distTmp < dist) {
				cur = tile;
				dist = distTmp;
			}
		}
		return cur;
	}

	/**
	 * @return The <b>RSTiles</b> the <b>RSArea</b> contains.
	 */
	public RSTile[] getTileArray() {
		ArrayList<RSTile> list = new ArrayList<RSTile>();
		for (int x = this.getX(); x <= (this.getX() + this.getWidth()); x++) {
			for (int y = this.getY(); y <= (this.getY() + this.getHeight()); y++) {
				if (this.area.contains(x, y)) {
					list.add(new RSTile(x, y));
				}
			}
		}
		RSTile[] tiles = new RSTile[list.size()];
		for (int i = 0; i < list.size(); i++) {
			tiles[i] = list.get(i);
		}
		return tiles;
	}

	/**
	 * @return The <b>RSTiles</b> the <b>RSArea</b> contains.
	 */
	public RSTile[][] getTiles() {
		RSTile[][] tiles = new RSTile[this.getWidth()][this.getHeight()];
		for (int i = 0; i < this.getWidth(); ++i) {
			for (int j = 0; j < this.getHeight(); ++j) {
				if (this.area.contains(this.getX() + i, this.getY() + j)) {
					tiles[i][j] = new RSTile(this.getX() + i, this.getY() + j);
				}
			}
		}
		return tiles;
	}

	/**
	 * @return The distance between the the <b>RSTile</b> that's most
	 *         <i>East</i> and the <b>RSTile</b> that's most <i>West</i>.
	 */
	public int getWidth() {
		return this.area.getBounds().width;
	}

	/**
	 * @return The distance between the the <b>RSTile</b> that's most
	 *         <i>South</i> and the <b>RSTile</b> that's most <i>North</i>.
	 */
	public int getHeight() {
		return this.area.getBounds().height;
	}

	/**
	 * @return The X axle of the <b>RSTile</b> that's most <i>West</i>.
	 */
	public int getX() {
		return this.area.getBounds().x;
	}

	/**
	 * @return The Y axle of the <b>RSTile</b> that's most <i>South</i>.
	 */
	public int getY() {
		return this.area.getBounds().y;
	}

	/**
	 * @return The plane of the <b>RSArea</b>.
	 */
	public int getPlane() {
		return plane;
	}

	/**
	 * @return The bounding box of the <b>RSArea</b>.
	 */
	public Rectangle getBounds() {
		return new Rectangle(this.area.getBounds().x + 1,
				this.area.getBounds().y + 1, this.getWidth(), this.getHeight());
	}

	/**
	 * Converts an shape made of <b>RSTile</b> to a polygon.
	 *
	 * @param tiles The <b>RSTile</b> of the Polygon.
	 * @return The Polygon of the <b>RSTile</b>.
	 */
	private Polygon tileArrayToPolygon(RSTile[] tiles) {
		Polygon poly = new Polygon();
		for (RSTile t : tiles) {
			poly.addPoint(t.getWorldLocation().getX(), t.getWorldLocation().getY());
		}
		return poly;
	}

	/**
	 * @param curr first <b>RSTile</b>
	 * @param dest second <b>RSTile</b>
	 * @return the distance between the first and the second rstile
	 */
	private double distanceBetween(RSTile curr, RSTile dest) {
		return Math.sqrt((curr.getWorldLocation().getX() - dest.getWorldLocation().getX())
				* (curr.getWorldLocation().getX() - dest.getWorldLocation().getX()) + (curr.getWorldLocation().getY() - dest.getWorldLocation().getY())
				* (curr.getWorldLocation().getY() - dest.getWorldLocation().getY()));
	}

}