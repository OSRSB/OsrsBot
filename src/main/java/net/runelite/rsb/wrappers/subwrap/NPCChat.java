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

    public boolean isLoading() {
        try {
            if (canContinue()) {
                return getContinueComponent().getText().contains("Please wait...");
            }
            else if (hasOptions()) {
                return Stream.of(getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents())
                        .map(RSWidget::getText)
                        .anyMatch(str -> str.contains("Please wait..."));
            }
            else {
                return false;
            }
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isOpen() {
        return canContinue() || hasOptions() || isLoading() || isCutsceneActive();
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

    public boolean hasOptions() {
        String[] options = getOptions();
        return options != null && options.length > 0;
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
     * @param hotKey presses the option number on keyboard
     * @param wait   not implemented. yet.
     * @return true if successful, false otherwise
     */
    public boolean selectOption(String option, boolean hotKey, boolean wait) {
        try {
            if (hotKey) {
                RSWidget[] widgets = getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents();
                for (int i = 1; i < widgets.length; i++) {
                    if (widgets[i].containsText(option)) {
                        methods.keyboard.sendText(String.valueOf(i), false);
                        return true;
                    }
                }
            }
            else {
                Stream.of(getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents())
                        .filter(widget -> widget.getText().contains(option))
                        .findFirst()
                        .ifPresentOrElse(RSWidget::doClick, IllegalArgumentException::new);
                return true;
            }
        } catch (Exception ignored) {
            return false;
        }
        return false;
    }

    /**
     * Clicks npc chat dialog menu option (if found)
     *
     * @param option the index of the menu option to click
     * @param hotKey      presses the option number on keyboard
     * @param wait        not implemented. yet.
     * @return true if successful, false otherwise
     */
    public boolean selectOption(int option, boolean hotKey, boolean wait) {
        try {
            if (hotKey) {
                RSWidget[] widgets = getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents();
                if (option >= 0 && option < widgets.length) {
                    methods.keyboard.sendText(String.valueOf((option == 0) ? 1 : option), false);
                    return true;
                }
            } else {
                Stream.of(getComponent(GlobalWidgetInfo.DIALOG_DYNAMIC_CONTAINER).getComponents())
                        .filter(widget -> {
                            String optionText = getOptions()[option == 0 ? 1 : option];
                            return option < getOptions().length && widget.getText().contains(optionText);
                        })
                        .findFirst()
                        .ifPresentOrElse(RSWidget::doClick, IllegalArgumentException::new);
                return true;
            }
        } catch (Exception ignored) {
            return false;
        }
        return false;
    }

    /**
     * Clicks npc chat dialog menu option (if found)
     *
     * @param option the menu option to click
     * @param wait   not implemented. yet.
     * @return true if successful, false otherwise
     */
    public boolean selectOption(String option, boolean wait) {
        return selectOption(option, false, wait);
    }
}
