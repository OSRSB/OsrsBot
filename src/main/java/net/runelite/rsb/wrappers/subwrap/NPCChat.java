package net.runelite.rsb.wrappers.subwrap;

import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.methods.Interfaces;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.*;
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

    private static final GlobalWidgetInfo[] widgetInfos = {
            GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER,
            GlobalWidgetInfo.DIALOG_NPC_CONTINUE,
            GlobalWidgetInfo.DIALOG_PLAYER_CONTINUE,
            GlobalWidgetInfo.DIALOG_QUEST_CONTINUE,
            GlobalWidgetInfo.DIALOG_LEVEL_UP_CONTINUE,
            GlobalWidgetInfo.DIALOG_UNKNOWN_CONTAINER
    };
    public Stream<RSWidget> getStream() {
        Stream<RSWidget> stream = Stream.empty();
        for (GlobalWidgetInfo info : widgetInfos) {
            if (getComponent(info) != null && getComponent(info).getId() != -1){
                if (info == GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER) {
                    RSWidget[] widgets = getComponent(info).getComponents();
                    stream = Stream.concat(stream, Stream.of(widgets));
                }
                else {
                    stream = Stream.concat(stream, Stream.of(getComponent(info)));
                }
            }
        }
        return stream;
    }

    public boolean isOpen() {
        return canContinue() || getOptions().length > 0 || getStream().anyMatch(widget -> widget.isVisible());
    }

    public boolean isLoading() {
        return getStream().anyMatch(widget -> widget.containsText("Please wait..") && widget.isVisible());
    }

    /**
     * Attempts to find all menu options from the current npc chat dialog
     *
     * @return array of menu options, null otherwise
     */
    public String[] getOptions() {
        try {
            return getStream()
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
            return getStream()
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
            getStream()
                    .filter(widget -> widget.getText().contains(option))
                    .findFirst()
                    .ifPresentOrElse(RSWidget::doClick, IllegalArgumentException::new);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
    public boolean canContinue() {
        return  getStream()
                .map(RSWidget::getText)
                .anyMatch(menuString -> menuString.contains("Click here to continue"));
    }
    public boolean selectContinue() {
        methods.keyboard.sendText(" ", false);
        return true;
    }
}
