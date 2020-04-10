package net.runelite.client.rsb.wrappers;

import net.runelite.api.GroundObject;
import net.runelite.api.Model;
import net.runelite.api.Tile;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.methods.MethodProvider;

/**
 * Represents an item on a tile.
 *
 * @author Jacmob
 */
public class RSGroundItem extends MethodProvider {

	private final RSItem groundItem;
	private final RSTile location;

	public RSGroundItem(final MethodContext ctx, final RSTile location, final RSItem groundItem) {
		super(ctx);
		this.location = location;
		this.groundItem = groundItem;
	}

	/**
	 * Gets the top model on the tile of this ground item.
	 *
	 * @return The top model on the tile of this ground item.
	 */
	public RSModel getModel() {
		Tile tile = location.getTile(methods);

		if (tile != null) {
			if (!tile.getGroundItems().isEmpty()) {
				//if (obj != null) {
				for (int i = 0; i < tile.getGroundItems().size(); i++) {
					if (!tile.getGroundItems().isEmpty()) {
						return (tile.getItemLayer().getTop() != null) ?
								new RSGroundObjectModel(methods,  tile.getItemLayer().getTop().getModel(), tile):
								new RSGroundObjectModel(methods, tile.getGroundItems().get(i).getModel(), tile);
					}
				}
				/*
				Model model = tile.getGroundItems().get(0).getModel();
				if (model != null) {
					return new RSModel(methods, model);
				}

				 */
			}
		}
		return null;
	}

	/**
	 * Performs the given action on this RSGroundItem.
	 *
	 * @param action The menu action to click.
	 * @return <tt>true</tt> if the action was clicked; otherwise <tt>false</tt>.
	 */
	public boolean doAction(final String action) {
		return doAction(action, null);
	}

	/**
	 * Performs the given action on this RSGroundItem.
	 *
	 * @param action The menu action to click.
	 * @param option The option of the menu action to click.
	 * @return <tt>true</tt> if the action was clicked; otherwise <tt>false</tt>.
	 */
	public boolean doAction(final String action, final String option) {
		RSModel model = getModel();
		if (model != null) {
			return model.doAction(action, option);
		}
		return methods.tiles.doAction(getLocation(), random(0.45, 0.55), random(0.45, 0.55), 0,
				action, option);
	}

	public RSItem getItem() {
		return groundItem;
	}

	public RSTile getLocation() {
		return location;
	}

	public boolean isOnScreen() {
		RSModel model = getModel();
		if (model == null) {
			return methods.calc.tileOnScreen(location);
		} else {
			return methods.calc.pointOnScreen(model.getPoint());
		}
	}

}
