package rsb.wrappers.common;

import net.runelite.api.Point;

public interface Clickable {

    boolean doAction(String action);

    boolean doAction(String action, String option);

    boolean doClick();

    boolean doClick(boolean leftClick);

    boolean doHover();


}
