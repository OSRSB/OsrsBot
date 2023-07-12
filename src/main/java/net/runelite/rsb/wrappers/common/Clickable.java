package net.runelite.rsb.wrappers.common;

import java.awt.*;

public interface Clickable {
    boolean doAction(String action);

    boolean doAction(String action, String option);

    boolean doClick();

    boolean doClick(boolean leftClick);

    boolean doHover();

    Shape getClickShape();

    ClickBox getClickBox();
}