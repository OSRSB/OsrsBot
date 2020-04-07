package net.runelite.client.rsb.wrappers;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.methods.MethodProvider;
//import java.awt.Point;

public abstract class RSCharacter extends MethodProvider {

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
    public boolean doAction(final String action, final String... option) {
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
        return getAccessor().getSpotAnimation();
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

    public RSTile getLocation() {
        Actor c = getAccessor();
        if (c == null) {
            return null;
        }
        return new RSTile(c.getWorldLocation());
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

    /**
     * Hovers this Player/NPC
     */
    public void hover() {
        this.getModel().hover();
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
}