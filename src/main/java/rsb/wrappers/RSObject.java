package rsb.wrappers;

import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.Angle;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.callback.ClientThread;
import rsb.methods.MethodContext;
import rsb.methods.MethodProvider;
import rsb.util.OutputObjectComparer;
import rsb.util.Parameters;
import rsb.wrappers.common.Clickable07;
import rsb.wrappers.common.Positionable;
import rsb.wrappers.subwrap.WalkerTile;
import org.apache.commons.lang3.ObjectUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.lang.reflect.Field;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * A wrapper for a tile object which interprets the underlying tile objects type and furthermore
 * acts as a factory for the RSModel of the RSObject (refer to getModel for better explanation)
 *
 * RSObject can represent any {@link Type types} game object
 */
@Slf4j
public class RSObject extends MethodProvider implements Clickable07, Positionable {
	private final TileObject obj;
	private final Type type;
	private final int plane;
	private final ObjectComposition def = null;

	/**
	 * Creates a new RSObject with the following parameters:
	 * @param ctx	The context in which the object exists (the singleton RuneLite)
	 * @param obj	The TileObject which this RSObject is associated with
	 * @param type	The type of game object corresponding to the enumerated {@link Type types}
	 * @param plane	The plane that this object exists on
	 */
	public RSObject(final MethodContext ctx,
					final TileObject obj, final Type type,
					final int plane) {
		super(ctx);
		this.obj = obj;
		this.type = type;
		this.plane = plane;
	}

	/**
	 * Gets the RSTile on which this object is centered. An RSObject may cover
	 * multiple tiles, in which case this will return the floored central tile.
	 *
	 * @return The central RSTile.
	 * @see #getArea()
	 */
	public WalkerTile getLocation() {
		return new WalkerTile(obj.getWorldLocation().getX(), obj.getWorldLocation().getY(), obj.getWorldLocation().getPlane());
	}

	/**
	 * Gets the area of tiles covered by this object.
	 *
	 * @return The RSArea containing all the tiles on which this object can be
	 *         found.
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
	public ObjectComposition getDef() {
		int id;
		return ((id = getID()) != -1) ? methods.client.getObjectDefinition(id) : def;
	}

	/**
	 * Gets the client thread provider from the clientUI to allow passing runnables to it
	 */
	public void setClientThreadProvider() {
		for (Field field : methods.runeLite.clientUI.getClass().getDeclaredFields()) {
			if (field.getName().equals("clientThreadProvider")) {
				try {
					field.setAccessible(true);
					methods.clientThreadProvider = (Provider<ClientThread>) field.get(methods.runeLite.clientUI);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Gets the ID of this object.
	 *
	 * @return The ID.
	 */
	public int getID() {
		if (obj != null) {
			return obj.getId();
		}
		return -1;
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
		ObjectComposition objectDef = getDef();
		return objectDef != null ? objectDef.getName() : "";
	}

	/**
	 * Gets the Model of this object.
	 * Checks what kind of object it is and returns the model of the object based on that
	 *
	 * @return The RSModel, or <tt>null</tt> if unavailable.
	 */
	public RSModel getModel() {
		try {
			Model model;
			if (obj instanceof WallObject) {
				model = (Model) ((WallObject) obj).getRenderable1();
				if (model != null && model.getVerticesX() != null)
					if (((WallObject) obj).getRenderable2() != null)
					return new RSWallObjectModel(methods, model, (Model) ((WallObject) obj).getRenderable2(), obj);
					else {
						return new RSWallObjectModel(methods, model, obj);
					}
				return new RSWallObjectModel(methods, null, obj);
			} else if (obj instanceof GroundObject) {
				model = (Model) ((GroundObject) obj).getRenderable();
				if (model != null && model.getVerticesX() != null)
					return new RSGroundObjectModel(methods, model, new RSTile(obj.getWorldLocation()).getTile(methods));
			} else if (obj instanceof DecorativeObject) {
				model = (Model) ((DecorativeObject) obj).getRenderable();
				if (model != null && model.getVerticesX() != null)
					return new RSGroundObjectModel(methods, model, new RSTile(obj.getWorldLocation()).getTile(methods));
			} else if (obj instanceof ItemLayer) {
				//model = new RSModel(methods, ((GameObject) obj).getRenderable().getModel());
			} else if (obj instanceof GameObject) {
				model =  (Model) ((GameObject) obj).getRenderable();
				if (model != null && model.getVerticesX() != null)
					return new RSObjectModel(methods, model, (GameObject) obj);
			}
		} catch (AbstractMethodError ignored) {
			log.debug("Error", ignored);
		}
		return null;


	}


	/**
	 * Determines whether this object is on the game screen.
	 *
	 * @return <tt>true</tt> if the object is on screen else <tt>false</tt>
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
	 * Performs the specified action on this object.
	 *
	 * @param action the menu item to search and click
	 * @return 		 <tt>true</tt> if clicked, <tt>false</tt> if object does not contain the
	 *         		 desired action
	 */
	public boolean doAction(final String action) {
		return doAction(action, null);
	}

	/**
	 * Performs the specified action on this object.
	 *
	 * @param action the action of the menu item to search and click
	 * @param option the option of the menu item to search and click
	 * @return 		 <tt>true</tt> if clicked, <tt>false</tt> if object does not contain the
	 *         		 desired action
	 */
	public boolean doAction(final String action, final String option) {
		RSModel model = this.getModel();
		if (model != null) {
			return model.doAction(action, option);
		}
		return methods.tiles.doAction(getLocation(), action, option);
	}

	/**
	 * Left-clicks this object.
	 *
	 * @return <tt>true</tt> if clicked otherwise <tt>false</tt>
	 */
	public boolean doClick() {
		return doClick(true);
	}

	/**
	 * Clicks this object.
	 *
	 * @param leftClick <tt>true</tt> to left-click; <tt>false</tt> to right-click.
	 * @return <tt>true</tt> if clicked otherwise <tt>false</tt>
	 */
	public boolean doClick(boolean leftClick) {
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
			return false;
		}
	}

	/**
	 * Moves the mouse over this object.
	 * @return true if the object was hovered over (or attempted to) otherwise false
	 */
	public boolean doHover() {
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
	 * @return <tt>true</tt> If RSObject is on screen after attempted to move camera angle.
	 */
	public boolean turnTo() {
		final RSObject o = methods.objects.getNearest(getID());
		if(o != null) {
			if(!o.isOnScreen()) {
				methods.camera.turnTo(o);
				return o.isOnScreen();
			}
		}
		return false;
	}

	/**
	 * Checks if the RSObject is clickable (interactive)
	 * @return	<tt>true</tt> if the object is capable of being interacted with otherwise <tt>false</tt>
	 */
	public boolean isClickable() {
		if (obj == null) {
			return false;
		}
		RSModel model = getModel();
		if (model == null) {
			return false;
		}
		return true;
		//return model.getModel().isClickable();
	}

	/**
	 * Gets the TileObject associated with this RSObject
	 * @return the TileObject else null
	 */
	public TileObject getObj() {
		return obj;
	}


	/**
	 * The type of game object
	 * Game, Decorative, Ground, or Wall
	 */
	public enum Type {
		GAME, DECORATIVE, GROUND, WALL
	}
}
