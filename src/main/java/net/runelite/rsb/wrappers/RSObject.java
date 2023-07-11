package net.runelite.rsb.wrappers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.cache.definitions.ObjectDefinition;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.methods.MethodProvider;
import net.runelite.rsb.wrappers.common.CacheProvider;
import net.runelite.rsb.wrappers.common.Clickable07;
import net.runelite.rsb.wrappers.common.Positionable;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * A wrapper for a tile object which interprets the underlying tile objects type and furthermore
 * acts as a factory for the RSModel of the RSObject (refer to getModel for better explanation)
 * <p>
 * RSObject can represent any {@link Type types} game object
 */
@Slf4j
public class RSObject extends MethodProvider implements Clickable07, Positionable, CacheProvider<ObjectDefinition> {

	private static final Random random = new Random();

	private final TileObject obj;
	private final Type type;
	private final int plane;
	private final ObjectDefinition def;
	private final int id;

	/**
	 * Creates a new RSObject with the following parameters:
	 *
	 * @param ctx   The context in which the object exists (the singleton RuneLite)
	 * @param obj   The TileObject which this RSObject is associated with
	 * @param type  The type of game object corresponding to the enumerated {@link Type types}
	 * @param plane The plane that this object exists on
	 */
	public RSObject(final MethodContext ctx,
					final TileObject obj, final Type type,
					final int plane) {
		super(ctx);
		this.obj = obj;
		this.type = type;
		this.plane = plane;
		this.id = (obj != null) ? obj.getId() : -1;
		this.def = (id != -1) ? (ObjectDefinition) createDefinition(id) : null;
	}

	/**
	 * Gets the RSTile on which this object is centered. An RSObject may cover
	 * multiple tiles, in which case this will return the floored central tile.
	 *
	 * @return The central RSTile.
	 * @see #getArea()
	 */
	public RSTile getLocation() {
		return new RSTile(obj.getWorldLocation().getX(), obj.getWorldLocation().getY(), obj.getWorldLocation().getPlane());
	}

	/**
	 * Gets the area of tiles covered by this object.
	 *
	 * @return The RSArea containing all the tiles on which this object can be
	 * found.
	 */
	public RSArea getArea() {
		if (obj instanceof GameObject) {
			Point sceneMin = ((GameObject) obj).getSceneMinLocation();
			Point sceneMax = ((GameObject) obj).getSceneMaxLocation();
			WorldPoint worldMin = WorldPoint.fromScene(methods.client, sceneMin.getX(), sceneMin.getY(), methods.client.getPlane());
			WorldPoint worldMax = WorldPoint.fromScene(methods.client, sceneMax.getX(), sceneMax.getY(), methods.client.getPlane());

			return new RSArea(new RSTile(worldMin), new RSTile(worldMax));
		}
		RSTile loc = getLocation();
		return new RSArea(loc, loc, plane);
	}

	/**
	 * Gets the object definition of this object.
	 *
	 * @return The RSObjectDef if available, otherwise <code>null</code>.
	 */
	public ObjectDefinition getDef() {
		if (obj != null) {
			return def;
		}
		return null;
	}

	/**
	 * Gets the ID of this object.
	 *
	 * @return The ID.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the name of the object.
	 *
	 * @param object The object to look up.
	 * @return The object name if the definition is available; otherwise "".
	 */
	public String getName(final RSObject object) {
		return object.getName();
	}

	/**
	 * Returns the name of the object.
	 *
	 * @return The object name if the definition is available; otherwise "".
	 */
	public String getName() {
		ObjectDefinition objectDef = getDef();
		return objectDef != null ? objectDef.getName().replaceAll("<.*?>", "") : "";
	}

	/**
	 * Gets the Model of this object.
	 * Checks what kind of object it is and returns the model of the object based on that
	 *
	 * @return The RSModel, or <code>null</code> if unavailable.
	 */
	public RSModel getModel() {
		try {
			Model model;
			if (obj instanceof WallObject) {
				model = toModel(((WallObject) obj).getRenderable1());
				if (model != null && model.getVerticesX() != null)
					if (((WallObject) obj).getRenderable2() != null)
						return new RSWallObjectModel(methods, model, toModel(((WallObject) obj).getRenderable2()), obj);
					else {
						return new RSWallObjectModel(methods, model, obj);
					}
				return new RSWallObjectModel(methods, null, obj);
			} else if (obj instanceof GroundObject) {
				model = toModel(((GroundObject) obj).getRenderable());
				if (model != null && model.getVerticesX() != null)
					return new RSGroundObjectModel(methods, model, new RSTile(obj.getWorldLocation()).getTile(methods));
			} else if (obj instanceof DecorativeObject) {
				model = toModel(((DecorativeObject) obj).getRenderable());
				if (model != null && model.getVerticesX() != null)
					return new RSGroundObjectModel(methods, model, new RSTile(obj.getWorldLocation()).getTile(methods));
			} else if (obj instanceof ItemLayer) {
				return null;
			} else if (obj instanceof GameObject) {
				model = toModel(((GameObject) obj).getRenderable());
				if (model != null && model.getVerticesX() != null)
					return new RSObjectModel(methods, model, (GameObject) obj);
			}
		} catch (AbstractMethodError e) {
			log.debug("Error", e);
		}
		return null;
	}

	private Model toModel(Renderable r) {
		if (r instanceof Model) {
			return (Model) r;
		} else if (r != null) {
			return r.getModel();
		} else {
			return null;
		}
	}

	/**
	 * Determines whether this object is on the game screen.
	 *
	 * @return <code>true</code> if the object is on screen else <code>false</code>
	 */
	public boolean isOnScreen() {
		RSModel model = getModel();
		if (model == null) {
			return methods.calc.tileOnScreen(getLocation());
		} else {
			return methods.calc.pointOnScreen(model.getPoint());
		}
	}

	/**
	 * Returns this object's type.
	 *
	 * @return The type of the object.
	 */
	public Type getType() {
		return type;
	}


	/**
	 * @param action the object action
	 * @return <code>true</code> if the action is prevent, <code>false</code> if the object does not contain the
	 * desired action
	 * @deprecated this may be wrong as the object definitions can be incorrect. Use with caution
	 */
	@Deprecated
	public boolean hasAction(@NonNull final String action) {
		for (final String a : getDef().getActions()) {
			if (action.equalsIgnoreCase(a)) return true;
		}
		return false;
	}

	/**
	 * Performs the specified action on this object.
	 *
	 * @param action the menu item to search and click
	 * @return <code>true</code> if clicked, <code>false</code> if object does not contain the
	 * desired action
	 */
	public boolean doAction(final String action) {
		return doAction(action, getName());
	}

	/**
	 * Performs the specified action on this object.
	 *
	 * @param action the action of the menu item to search and click
	 * @param option the option of the menu item to search and click
	 * @return <code>true</code> if clicked, <code>false</code> if object does not contain the
	 * desired action
	 */
	public boolean doAction(final String action, final String option) {
		RSModel model = this.getModel();
		if (model != null) {
			return model.doAction(action, option);
		}
		return methods.tiles.doAction(getLocation(), action, option);
	}

	/**
	 * Left-clicks this object. It tries 3 times to move the mouse onto the object.
	 * Use {@link #doClick(boolean, int)} to specify number of attempts.
	 *
	 * @return <code>true</code> if clicked otherwise <code>false</code>
	 */
	public boolean doClick() {
		return doClick(true);
	}


	/**
	 * Clicks this object. It tries 3 times to move the mouse onto the object.
	 * Use {@link #doClick(boolean, int)} to specify number of attempts.
	 *
	 * @param leftClick <code>true</code> to left-click; <code>false</code> to right-click.
	 * @return <code>true</code> if clicked otherwise <code>false</code>
	 */
	public boolean doClick(boolean leftClick) {
		return doClick(leftClick, 3);
	}

	/**
	 * Clicks this object.
	 *
	 * @param leftClick <code>true</code> to left-click; <code>false</code> to right-click.
	 * @param attempts  number of mouse move attempts to get the mouse on the object.
	 *                  Set this higher if it is very important to successfully click.
	 * @return <code>true</code> if clicked otherwise <code>false</code>
	 */
	public boolean doClick(boolean leftClick, int attempts) {
		//attempt to find center of clickbox and check if on screen
		for (int i = 0; i < attempts; i++) {
			Point pointNearCenter = getRandomPointNearCenter();
			if (pointNearCenter == null) break;
			if (methods.calc.pointOnScreen(pointNearCenter)) {
				methods.mouse.move(pointNearCenter);
				if (isPointInClickBox(pointNearCenter)) {
					methods.mouse.click(pointNearCenter, leftClick);
					return true;
				}
			}
		}

		// if no clickbox present or the middle of the clickbox is not onscreen try the model
		RSModel model = this.getModel();
		if (model != null) {
			return model.doClick(leftClick);
		} else {
			Point p = methods.calc.tileToScreen(getLocation());
			if (methods.calc.pointOnScreen(p)) {
				methods.mouse.move(p);
				if (methods.calc.pointOnScreen(p)) {
					methods.mouse.click(leftClick);
					return true;
				} else {
					p = methods.calc.tileToScreen(getLocation());
					if (methods.calc.pointOnScreen(p)) {
						methods.mouse.move(p);
						methods.mouse.click(leftClick);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Moves the mouse over this object.
	 *
	 * @return true if the object was hovered over (or attempted to) otherwise false
	 */
	public boolean doHover() {
		Point middlePoint = getRandomPointNearCenter();
		if (middlePoint != null && methods.calc.pointOnScreen(middlePoint)) {
			methods.mouse.move(middlePoint);
			return true;
		}

		RSModel model = getModel();
		if (model != null) {
			model.hover();
			return true;
		} else {
			Point p = methods.calc.tileToScreen(getLocation());
			if (methods.calc.pointOnScreen(p)) {
				methods.mouse.move(p);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof RSObject) && ((RSObject) o).obj == obj;
	}

	@Override
	public int hashCode() {
		if (obj != null) {
			return obj.hashCode();
		}
		return 0;
	}

	/**
	 * Turns the camera towards the RSObject.
	 *
	 * @return <code>true</code> If RSObject is on screen after attempted to move camera angle.
	 */
	public boolean turnTo() {
		final RSObject o = methods.objects.getNearest(getID());
		if (o != null) {
			if (!o.isOnScreen()) {
				methods.camera.turnTo(o);
				return o.isOnScreen();
			}
		}
		return false;
	}

	/**
	 * Checks if the RSObject is clickable (interactive)
	 *
	 * @return <code>true</code> if the object is capable of being interacted with otherwise <code>false</code>
	 */
	public boolean isClickable() {
		return obj != null && getModel() != null;
	}

	/**
	 * Gets the TileObject associated with this RSObject
	 *
	 * @return the TileObject else null
	 */
	public TileObject getObj() {
		return obj;
	}

	private boolean isPointInClickBox(Point p) {
		Shape clickBox = getObj().getClickbox();
		return clickBox != null && clickBox.contains(p.getX(), p.getY());
	}

	private Point getRandomPointNearCenter() {
		Shape clickBox = getObj().getClickbox();
		if (clickBox == null) return null;

		Rectangle2D bounds = clickBox.getBounds2D();

		// compute the width and height
		double width = bounds.getWidth();
		double height = bounds.getHeight();

		// compute the middle point
		double midX = bounds.getCenterX();
		double midY = bounds.getCenterY();

		// get a point in 25% range around the middle point
		double minX = midX - width * 0.25 / 2;
		double maxX = midX + width * 0.25 / 2;
		double minY = midY - height * 0.25 / 2;
		double maxY = midY + height * 0.25 / 2;
		for (int i = 0; i < 5; i++) {
			double x = minX + (maxX - minX) * random.nextDouble();
			double y = minY + (maxY - minY) * random.nextDouble();

			java.awt.Point point = new java.awt.Point((int) x, (int) y);
			if (clickBox.contains(point)) return new Point(point.x, point.y);
		}
		return null;
	}

	/**
	 * The type of game object
	 * Game, Decorative, Ground, or Wall
	 */
	public enum Type {
		GAME(1), DECORATIVE(2), GROUND(4), WALL(8);

		private final int value;

		Type(int value) {
			this.value = value;
		}

		public int getBitValue() {
			return value;
		}

		public static Type getType(int value) {
			for (Type type : values()) {
				if (type.getBitValue() == value) {
					return type;
				}
			}
			return null;
		}

		/**
		 * Allows you to pass an array of enumerated types and retrieve the combined mask value
		 *
		 * @param types the type(s) to combine
		 * @return the mask value of the combined types
		 */
		public static int getMask(Type... types) {
			int sum = 0;
			for (Type type : types) {
				if (type != null) {
					sum += type.getBitValue();
				}
			}
			return sum;
		}
	}
}
