package net.runelite.client.rsb.wrappers;

import net.runelite.api.*;
import net.runelite.client.rsb.methods.MethodContext;

import java.lang.ref.SoftReference;

public class RSNPC extends RSCharacter {
    private final SoftReference<NPC> npc;
    //private final NPC npc;


    public RSNPC(final MethodContext ctx, final NPC npc) {
        super(ctx);
        this.npc =  new SoftReference<>(npc);
    }
/*
    public RSNPC(final MethodContext ctx, final RSCharacter npc) {
        super(ctx);
        this.npc =
    }
*/
    @Override
    public Actor getAccessor() {
        return npc.get();
    }

    @Override
    public Actor getInteracting() {
        return getAccessor().getInteracting();
    }


    public String[] getActions() {
        NPCDefinition def = getDefInternal();

        if (def != null) {
            return def.getActions();
        }
        return new String[0];
    }

    public int getID() {
        NPCDefinition def = getDefInternal();
        if (def != null) {
            return def.getId();
        }
        return -1;
    }

    @Override
    public String getName() {
        NPCDefinition def = getDefInternal();
        if (def != null) {
            return def.getName();
        }
        return "";
    }

    @Override
    public int getLevel() {
        NPC c = npc.get();
        if (c == null) {
            return -1;
        } else {
            return c.getCombatLevel();
        }
    }

    /**
     * @return <tt>true</tt> if RSNPC is interacting with RSPlayer; otherwise
     *         <tt>false</tt>.
     */
    @Override
    public boolean isInteractingWithLocalPlayer() {
       // RSNPC npc = methods.npcs.getNearest(getID())
        RSNPC npc = this;
        return npc.getInteracting() != null && npc.getInteracting().equals(
                methods.players.getMyPlayer());
    }


/*
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (final String act : getActions()) {
            sb.append(act);
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return "NPC[" + getName() + "],actions=[" + sb.toString() + "]"
                + super.toString();
    }
*/


    NPCDefinition getDefInternal() {
        NPC c = npc.get();
        if (c == null) {
            return null;
        } else {
            return c.getDefinition();
        }
    }

    /**
     * Turns towards the RSNPC.
     * @author LastCoder
     * @return <tt>true</tt> - If RSNPC is on screen after attempting to move camera angle.
     */
    public boolean turnTo() {
        //final RSNPC n = methods.npcs.getNearest(getID());
        RSNPC n = this;
        if(n != null) {
            if(!n.isOnScreen()) {
                methods.camera.turnTo(n);
                return n.isOnScreen();
            }
        }
        return false;
    }

}
