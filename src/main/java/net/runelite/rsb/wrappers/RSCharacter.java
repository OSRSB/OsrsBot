package net.runelite.rsb.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.cache.definitions.NpcDefinition;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.methods.MethodProvider;
import net.runelite.rsb.util.OutputObjectComparer;
import net.runelite.rsb.wrappers.common.CacheProvider;
import net.runelite.rsb.wrappers.common.ClickBox;
import net.runelite.rsb.wrappers.common.Clickable07;
import net.runelite.rsb.wrappers.common.Positionable;
import net.runelite.rsb.wrappers.RSTile;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;

@Slf4j
public abstract class RSCharacter extends MethodProvider implements Clickable07, Positionable {
    private static ArrayList<Field> pathFields = new ArrayList<>();
    private static int pathXIndex = -1;
    private static int pathYIndex = -1;

    private final ClickBox clickBox = new ClickBox(this);

    public RSCharacter(MethodContext ctx) {
        super(ctx);
    }

    /**
     * Retrieves a reference to the client accessor. For internal use. The
     * reference should be stored in a SoftReference by subclasses to allow for
     * garbage collection when appropriate.
     *
     * @return The client accessor.
     */
    protected abstract Actor getAccessor();
    protected abstract Actor getInteracting();

    public int[] getPathX() {
        if (pathFields.isEmpty()) {
            setPathFields();
        }
        try {
            return (int[]) pathFields.get(pathXIndex).get(getAccessor());
        } catch (IllegalAccessException e) {
            log.warn("Path field failed getting a value", e.getCause());
        }
        return null;
    }

    public int[] getPathY() {
        if (pathFields.isEmpty()) {
            setPathFields();
        }
        try {
            return (int[]) pathFields.get(pathYIndex).get(getAccessor());
        } catch (IllegalAccessException e) {
            log.warn("Path field failed getting a value", e.getCause());
        }
        return null;
    }

    private void setPathFields() {
        Player actor = (Player) this.getAccessor();
        for (Field field : actor.getClass().getSuperclass().getDeclaredFields()) {
            if (field.getType().getTypeName().equals("int[]")) {
                field.setAccessible(true);
                try {
                    if (OutputObjectComparer.unpack(field.get(actor)).length == 10) {
                        pathFields.add(field);
                    }
                } catch (IllegalAccessException e) {
                    log.error("Accessed reflected object incorrectly", e.getCause());
                }
            }
        }
        pathFields.sort(Comparator.comparing(Field::getName));
            try {
            if (methods.client.getLocalPlayer() != null)
                //Attempt to accurately decide which set is which (Hopefully this is never relied on)
                pathXIndex = (((int[]) pathFields.get(0).get(methods.client.getLocalPlayer()))[0] == methods.client.getLocalPlayer().getLocalLocation().getSceneX()) ? 0 : 1;
            pathYIndex = (pathXIndex == 0) ? 1 : 0;
        } catch (IllegalAccessException e) {
            log.error("Accessed reflected object incorrectly", e.getCause());
        }
    }

    /**
     * Performs an action on a humanoid character (tall and skinny).
     *
     * @param action The action of the menu entry to be clicked (if available).
     * @return <code>true</code> if the option was found; otherwise <code>false</code>.
     */
    public boolean doAction(final String action) {
        return doAction(action, getName());
    }

    /**
     * Performs an action on a humanoid character (tall and skinny).
     *
     * @param action The action of the menu entry to be clicked (if available).
     * @param option The option of the menu entry to be clicked (if available).
     * @return <code>true</code> if the option was found; otherwise <code>false</code>.
     */
    public boolean doAction(final String action, final String option) {
        return getClickBox().doAction(action, option);
    }

    public RSModel getModel() {
        Actor actor = getAccessor();
        if (actor != null) {
            Model model = actor.getModel();
            if (model != null) {
                return new RSCharacterModel(methods, model, actor);
            }
        }
        return null;
    }

    public int getAnimation() {
        return getAccessor().getAnimation();
    }

    public int getGraphic() {
        return getAccessor().getGraphic();
    }

    public int getHeight() {
        return getAccessor().getLogicalHeight();
    }

    /**
     * @return The % of HP
     */
    public int getHPPercent() {
        double healthRatio = getAccessor().getHealthRatio();
        double healthScale = getAccessor().getHealthScale();
        if (healthRatio == -1) return -1;
        return (int)(healthRatio / healthScale * 100);
    }

    public RSTile getLocation() {
        Actor actor = getAccessor();
        if (actor == null) { return null; }
        return new RSTile(actor.getWorldLocation());
    }

    public String getMessage() {
        return getAccessor().getOverheadText();
    }

    /**
     * Gets the minimap location, of the character. Note: This does work when
     * it's walking!
     *
     * @return The location of the character on the minimap.
     */
    public Point getMinimapLocation() {
        Actor actor = getAccessor();
        int actorX = methods.client.getBaseX() + (actor.getLocalLocation().getX() / 32 - 2) / 4;
        int actorY = methods.client.getBaseY() + (actor.getLocalLocation().getY() / 32 - 2) / 4;
        return methods.calc.worldToMinimap(actorX, actorY);
    }

    public String getName() {
        return null; // should be overridden, obviously
    }

    public int getLevel() {
        return -1; // should be overridden too
    }

    public int getOrientation() {
        return (int) (270 - (getAccessor().getOrientation() & 0x3fff) / 45.51) % 360;
    }

    public Point getScreenLocation() {
        Actor actor = getAccessor();
        RSModel model = getModel();
        if (model == null) {
            return methods.calc.groundToScreen(
                    actor.getLocalLocation().getX(),
                    actor.getLocalLocation().getY(),
                    actor.getLogicalHeight() / 2);
        } else {
            return model.getPoint();
        }
    }

    public boolean isBeingAttacked() {
        if (methods.game.isLoggedIn()) {
            if (getAccessor().getInteracting() != null) {
                return getAccessor().getHealthRatio() > 0;
            }
        }
        return false;
    }

    public boolean isAttacking() {
        if (methods.game.isLoggedIn()) {
            if (getAccessor().getInteracting() != null) {
                return getAccessor().getInteracting().getHealthRatio() > 0;
            }
        }
        return false;
    }

    // TODO: this is far from enough to decide
    public boolean isInCombat() {
        return isAttacking() || isBeingAttacked();
    }

    public boolean isInteractingWithLocalPlayer() {
        Player localPlayer = methods.client.getLocalPlayer();
        if (localPlayer == null) return false;
        return getAccessor() == localPlayer.getInteracting();
    }

    /**
    * Checks to determine whether the character is in the idle animation or not
    *
    * @return true if the character is in the idle animation otherwise false
    */
    public boolean isIdle() {
        return getAnimation() == -1;
    }

     // TODO: public boolean isMoving()

    public boolean isOnScreen() {
        RSModel model = getModel();
        if (model == null) {
            return methods.calc.tileOnScreen(getLocation());
        } else {
            return methods.calc.pointOnScreen(model.getPoint());
        }
    }

    public boolean isValid() {
        return getAccessor() != null;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(getAccessor());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RSCharacter character) {
            return character.getAccessor() == getAccessor();
        }
        return false;
    }

    /*
    @Override
    public String toString() {
        final RSCharacter inter = getInteracting();
        return "[anim="
                + getAnimation()
                + ",msg="
                + getMessage()
                + ",interact="
                + (inter == null ? "null" : inter.isValid() ? inter
                .getMessage() : "Invalid") + "]";
    }*/

    /**
     * Turns towards the RSCharacter.
     * @return <code>true</code> - If RSCharacter is on screen after attempting to move camera angle.
     */
    public boolean turnTo() {
        if (!this.isOnScreen()) {
            methods.camera.turnTo(this);
            return this.isOnScreen();
        }
        return false;
    }

    /**
     * Hovers this Player/NPC
     */
    public boolean doHover() {
        return getClickBox().doHover();
    }

    public boolean doClick() {
        return doClick(true);
    }

    public boolean doClick(boolean leftClick) {
        return getClickBox().doClick(leftClick);
    }

    public boolean isClickable() {
        RSModel model = getModel();
        if (model == null) {
            return false;
        }
        return true;
        //return model.getModel().isClickable();
    }

    public Shape getClickShape() {
        return getAccessor().getConvexHull();
    }
    public ClickBox getClickBox() {
        return clickBox;
    }

    public DIRECTION getDirectionFacing() {
        int angle = this.getAccessor().getOrientation();
        {
            int round = angle >>> 9;
            int up = angle & 128;
            if (up != 0) {
                // round up
                ++round;
            }
            return switch (round & 7) {
                case 0 -> DIRECTION.S;
                case 1 -> DIRECTION.SW;
                case 2 -> DIRECTION.W;
                case 3 -> DIRECTION.NW;
                case 4 -> DIRECTION.N;
                case 5 -> DIRECTION.NE;
                case 6 -> DIRECTION.E;
                case 7 -> DIRECTION.SE;
                default -> throw new IllegalStateException();
            };
        }
    }

    public enum DIRECTION {
        N, S, E, W, NE, NW, SE, SW;
    }
}
