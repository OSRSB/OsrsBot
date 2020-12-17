package rsb.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import rsb.methods.MethodContext;
import rsb.methods.MethodProvider;
import rsb.util.OutputObjectComparer;
import rsb.wrappers.common.Clickable07;
import rsb.wrappers.common.Positionable;
import rsb.wrappers.subwrap.WalkerTile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import static net.runelite.api.coords.Direction.*;
import static net.runelite.api.coords.Direction.EAST;
//import java.awt.Point;

@Slf4j
public abstract class RSCharacter extends MethodProvider implements Clickable07, Positionable {

    private static ArrayList<Field> pathFields = new ArrayList<>();
    private static int pathXIndex = -1;
    private static int pathYIndex = -1;

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
     * @return <tt>true</tt> if the option was found; otherwise <tt>false</tt>.
     */
    public boolean doAction(final String action) {
        return doAction(action, null);
    }

    /**
     * Performs an action on a humanoid character (tall and skinny).
     *
     * @param action The action of the menu entry to be clicked (if available).
     * @param option The option of the menu entry to be clicked (if available).
     * @return <tt>true</tt> if the option was found; otherwise <tt>false</tt>.
     */
    public boolean doAction(final String action, final String option) {
        RSModel model = this.getModel();
        return model != null && this.isValid() && this.getModel().doAction(action, option);
    }

    public RSModel getModel() {
        Actor c = getAccessor();
        if (c != null) {
            Model model = c.getModel();
            if (model != null) {
                return new RSCharacterModel(methods, model, c);
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
        return isInCombat() ? getAccessor().getHealthRatio() * 100 / 255 : 100;
    }

    public WalkerTile getLocation() {
        Actor c = getAccessor();
        if (c == null) {
            return null;
        }
        return new WalkerTile(c.getWorldLocation());
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
        Actor c = getAccessor();
        int cX = methods.client.getBaseX() + (c.getLocalLocation().getX() / 32 - 2) / 4;
        int cY = methods.client.getBaseY() + (c.getLocalLocation().getY() / 32 - 2) / 4;
        return methods.calc.worldToMinimap(cX, cY);
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
        Actor c = getAccessor();
        RSModel model = getModel();
        if (model == null) {
            return methods.calc.groundToScreen(c.getLocalLocation().getX(), c.getLocalLocation().getY(),
                    c.getLogicalHeight() / 2);
        } else {
            return model.getPoint();
        }
    }




    public boolean isBeingAttacked() {
        if (methods.game.isLoggedIn()) {
            if (getAccessor().getInteracting() != null) {
                if (getAccessor().getHealthRatio() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAttacking() {
        if (methods.game.isLoggedIn()) {
            if (getAccessor().getInteracting() != null) {
                if (getAccessor().getInteracting().getHealthRatio() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCombat() {
        if (isAttacking() || isBeingAttacked()) {
            return true;
        }
        return false;
    }

    public boolean isInteractingWithLocalPlayer() {
        return getAccessor() == methods.client.getLocalPlayer().getInteracting();
    }

    public boolean isAnimating(){
        return (getAnimation() != -1);
    }

    /*
    public boolean isMoving() {

    }
*/

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
        if (obj instanceof RSCharacter) {
            RSCharacter cha = (RSCharacter) obj;
            return cha.getAccessor() == getAccessor();
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
    }
*/

    /**
     * Turns towards the RSCharacter.
     * @return <tt>true</tt> - If RSCharacter is on screen after attempting to move camera angle.
     */
    public boolean turnTo() {
        RSCharacter character = this;
        if(character != null) {
            if(!character.isOnScreen()) {
                methods.camera.turnTo(character);
                return character.isOnScreen();
            }
        }
        return false;
    }


    /**
     * Hovers this Player/NPC
     */
    public boolean doHover() {
        RSModel model = getModel();
        if (model == null) {
            return false;
        }
        this.getModel().hover();
        return true;
    }

    public boolean doClick() {
        RSModel model = getModel();
        if (model == null) {
            return false;
        }
        this.getModel().doClick(true);
        return true;
    }

    public boolean doClick(boolean leftClick) {
        RSModel model = getModel();
        if (model == null) {
            return false;
        }
        this.getModel().doClick(leftClick);
        return true;
    }

    public boolean isClickable() {
        RSModel model = getModel();
        if (model == null) {
            return false;
        }
        return model.getModel().isClickable();
    }

    public DIRECTION getDirectionFacing() {
        int angle = this.getAccessor().getOrientation();
        {
            int round = angle >>> 9;
            int up = angle & 128;
            if (up != 0)
            {
                // round up
                ++round;
            }
            switch (round & 7)
            {
                case 0:
                    return DIRECTION.S;
                case 1:
                    return DIRECTION.SW;
                case 2:
                    return DIRECTION.W;
                case 3:
                    return DIRECTION.NW;
                case 4:
                    return DIRECTION.N;
                case 5:
                    return DIRECTION.NE;
                case 6:
                    return DIRECTION.E;
                case 7:
                    return DIRECTION.SE;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    public enum DIRECTION {
        N, S, E, W, NE, NW, SE, SW;
    }


}