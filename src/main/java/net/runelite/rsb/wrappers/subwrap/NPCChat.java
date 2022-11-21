package net.runelite.rsb.wrappers.subwrap;

import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.methods.Interfaces;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.Arrays;

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
        try {
            return Arrays.stream(getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents()).map(RSWidget::getText).toArray(String[]::new);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean selectOption(String option, boolean wait) {
        return false;
    }
}
