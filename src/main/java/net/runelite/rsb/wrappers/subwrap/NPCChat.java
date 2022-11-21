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

    public boolean containsOption(String option){
        try {
            return Arrays.stream(getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents()).anyMatch(o->o.getText().contains(option));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean selectOption(String option, boolean wait) {

        final RSWidget iface = getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER);
        if(iface.getComponents()!=null){
            for (RSWidget component : iface.getComponents()) {
                if(component.getText().contains(option)){
                    return component.doClick();
                }
            }
        }

        return false;
    }
}
