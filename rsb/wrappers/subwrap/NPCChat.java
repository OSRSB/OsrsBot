package net.runelite.client.rsb.wrappers.subwrap;

import net.runelite.client.rsb.methods.Interfaces;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.wrappers.RSWidget;

public class NPCChat extends Interfaces {
    public NPCChat(MethodContext ctx) {
        super(ctx);
    }

    public String getMessage() {
        return "";
    }

    public String getName() {
        return "";
    }

    public String[] getOptions() {
        return new String[]{};
    }

    public boolean selectOption(String option, boolean wait) {
        return false;
    }

}
