package net.runelite.rsb.wrappers.common;

import net.runelite.rsb.wrappers.RSTile;

/**
 * An interface for position related information regarding sub-classes
 * (Tile position, position of animation, and adjusting the camera to the object)
 */
public interface Positionable {

    /**
     * Gets the tile position of the entities animation (where it *appears* to be)
     * @return the animable tile position of the entity if it exists, else null
     */
    //RSTile getAnimablePosition();

    /**
     * Gets the tile position of the entity as provided by the client
     * @return the tile position of the entity if it exists, else null
     */
    RSTile getLocation();

    /**
     * Turns the camera to face the entity
     * @return <code>True</code> if the camera has been turned to face the entity
     */
    boolean turnTo();

}
