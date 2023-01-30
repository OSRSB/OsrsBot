package net.runelite.rsb.wrappers.subwrap;

import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.methods.Interfaces;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.function.Predicate;
import java.util.stream.Stream;

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

    /**
     * Attempts to find all menu options from the current npc chat dialog
     *
     * @return array of menu options, null otherwise
     */
    public String[] getOptions() {
        try {
            return Stream.of(getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents())
                    .map(RSWidget::getText)
                    .filter(Predicate.not(String::isEmpty))
                    .toArray(String[]::new);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Determines a {@code string} is found in npc chat dialog menu
     *
     * @param option the menu option to search for
     * @return true if found, false otherwise
     */
    public boolean containsOption(String option) {
        try {
            return Stream.of(getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents())
                    .map(RSWidget::getText)
                    .anyMatch(menuString -> menuString.contains(option));
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Clicks npc chat dialog menu option (if found)
     *
     * @param option the menu option to click
     * @param wait   not implemented. yet.
     * @return true if successful, false otherwise
     */
    public boolean selectOption(String option, boolean wait) {

        try {
            Stream.of(getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents())
                    .filter(widget -> widget.getText().contains(option))
                    .findFirst()
                    .ifPresentOrElse(RSWidget::doClick, IllegalArgumentException::new);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
