package net.runelite.client.rsb.methods;

import net.runelite.api.ItemDefinition;
import net.runelite.api.MenuEntry;
import net.runelite.client.rsb.wrappers.RSItem;
import net.runelite.client.ui.FontManager;


import java.awt.*;
import java.util.regex.Pattern;

/**
 * Context menu related operations.
 */
public class Menu extends MethodProvider {

    private static final Pattern HTML_TAG = Pattern
            .compile("(^[^<]+>|<[^>]+>|<[^>]+$)");

    static final int TOP_OF_MENU_BAR = 18;
    static final int MENU_ENTRY_LENGTH = 15;
    static final int MENU_SIDE_BORDER = 7;
    static final int MAX_DISPLAYABLE_ENTRIES = 32;

    Menu(final MethodContext ctx) {
        super(ctx);
    }

    /**
     * Clicks the menu target. Will left-click if the menu item is the first,
     * otherwise open menu and click the target.
     *
     * @param action The action (or action substring) to click.
     * @return <tt>true</tt> if the menu item was clicked; otherwise
     * <tt>false</tt>.
     */
    public boolean doAction(String action) {
        return doAction(action, null);
    }

    /**
     * Clicks the menu target. Will left-click if the menu item is the first,
     * otherwise open menu and click the target.
     *
     * @param action The action (or action substring) to click.
     * @param target The target (or target substring) of the action to click.
     * @return <tt>true</tt> if the menu item was clicked; otherwise
     * <tt>false</tt>.
     */
    public boolean doAction(final String action, final String... target) {
        int idx = getIndex(action, target);
        if (!isOpen()) {
            if (idx == -1) {
                return false;
            }
            if (idx > MAX_DISPLAYABLE_ENTRIES) {
                return false;
            }
            if (idx == 0) {
                methods.mouse.click(true);
                return true;
            }
            methods.mouse.click(false);
            sleep(random(50,90));
            idx = getIndex(action, target);
            return clickIndex(idx);
        } else if (idx == -1) {
            while (isOpen()) {
                methods.mouse.moveRandomly(750);
                sleep(random(100, 500));
            }
            return false;
        }
        return clickIndex(idx);
    }


    /**
     * Determines if the item contains the desired action.
     *
     * @param item   The item to check.
     * @param action The item menu action to check.
     * @return <tt>true</tt> if the item has the action; otherwise
     * <tt>false</tt>.
     */
    public boolean itemHasAction(final RSItem item, final String action) {
        // Used to determine if an item is droppable/destroyable
        if (item == null) {
            return false;
        }
        ItemDefinition itemDef = item.getDefinition();
        if (itemDef != null) {
            for (String a : itemDef.getInventoryActions()) {
                if (a.equalsIgnoreCase(action)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Left clicks at the given index.
     *
     * @param i The index of the item.
     * @return <tt>true</tt> if the mouse was clicked; otherwise <tt>false</tt>.
     */
    public boolean clickIndex(final int i) {
        if (!isOpen()) {
            return false;
        }
        MenuEntry[] entries = getEntries();
        if (entries.length <= i) {
            return false;
        }
        if (!isCollapsed()) {
            return clickMain(i);
        }
        return false;
    }

    private boolean clickMain(final int i) {
        MenuEntry[] entries = getEntries();
        String item = (entries[i].getOption() + " " + entries[i].getTarget().replaceAll("<.*?>", ""));
        Point menu = getLocation();
        FontMetrics fm = methods.runeLite.getLoader().getGraphics().getFontMetrics(FontManager.getRunescapeBoldFont());
        int xOff = random(1, (fm.stringWidth(item) + MENU_SIDE_BORDER) - 1);
        int yOff = TOP_OF_MENU_BAR + (((MENU_ENTRY_LENGTH * i) + random(1, MENU_ENTRY_LENGTH - 1)));
        methods.mouse.move(menu.x + xOff, menu.y + yOff);
        sleep(random(100, 200));
        if (isOpen()) {
            methods.mouse.click(true);
            return true;
        }
        return false;
    }

    public Point getLocation() {
        return new Point(calculateX(), calculateY());
    }

    /**
     * Checks whether or not the menu is collapsed.
     *
     * @return <tt>true</tt> if the menu is collapsed; otherwise <tt>false</tt>.
     */
    public boolean isCollapsed() {
        return !methods.client.isMenuOpen();
    }

    /**
     * Checks whether or not the menu is open.
     *
     * @return <tt>true</tt> if the menu is open; otherwise <tt>false</tt>.
     */
    public boolean isOpen() {
        return methods.client.isMenuOpen();
    }

    /**
     * Strips HTML tags.
     *
     * @param input The string you want to parse.
     * @return The parsed {@code String}.
     */
    private String stripFormatting(String input) {
        return HTML_TAG.matcher(input).replaceAll("");
    }

        /**
         * Calculates the width of the menu
         *
         * @return the menu width
         */
        private int calculateWidth() {
            MenuEntry[] entries = getEntries();
            final int MIN_MENU_WIDTH = 102;
            FontMetrics fm = methods.runeLite.getLoader().getGraphics().getFontMetrics(FontManager.getRunescapeBoldFont());
            int longestEntry = 0;
            for (MenuEntry entry : entries) {
                int entryLength = fm.stringWidth(entry.getOption() + " " + entry.getTarget().replaceAll("<.*?>", ""));
                if (entryLength > longestEntry) {
                    longestEntry = entryLength;
                }
            }
            return (longestEntry + MENU_SIDE_BORDER < MIN_MENU_WIDTH) ? MIN_MENU_WIDTH : longestEntry + MENU_SIDE_BORDER;
        }

        /**
         * Calculates the height of the menu
         *
         * @return the menu height
         */
        private int calculateHeight() {
            MenuEntry[] entries = getEntries();
            int numberOfEntries = entries.length;
            return MENU_ENTRY_LENGTH * numberOfEntries + TOP_OF_MENU_BAR;
        }


        /**
         * Calculates the top left corner X of the menu
         *
         * @return the menu x
         */
        private int calculateX() {
            if (isOpen()) {
                final int MIN_MENU_WIDTH = 102;
                int width = calculateWidth();
                return (width + MENU_SIDE_BORDER < MIN_MENU_WIDTH) ? (methods.virtualMouse.getClientPressX() - (MIN_MENU_WIDTH / 2)) : (methods.virtualMouse.getClientPressX() - (width / 2));
            }
            return -1;
        }

        /**
         * Calculates the top left corner Y of the menu
         *
         * @return the menu y
         */
        private int calculateY() {
            if (isOpen()) {
                final int CANVAS_LENGTH = 503;
                MenuEntry[] entries = getEntries();
                int offset = CANVAS_LENGTH - (methods.virtualMouse.getClientPressY() + calculateHeight());
                return methods.virtualMouse.getClientPressY() + ((offset < 0 && entries.length <= MAX_DISPLAYABLE_ENTRIES) ? offset : 0);
            }
            return -1;
        }

        public MenuEntry[] getEntries() {
            MenuEntry[] entries = methods.client.getMenuEntries();
            MenuEntry[] reversed = new MenuEntry[entries.length];
            for (int i = entries.length - 1, x = 0; i >= 0; i--, x++)
                reversed[i] = entries[x];
            return reversed;
        }

        public String[] getActions() {
            MenuEntry[] entries = getEntries();
            String[] actions = new String[entries.length];
            for (int i = 0; i < entries.length; i++) {
                actions[i] = entries[i].getOption();
            }
            return actions;
        }

        public String[] getTargets() {
            MenuEntry[] entries = getEntries();
            String[] targets = new String[entries.length];
            for (int i = 0; i < entries.length; i++) {
                targets[i] = entries[i].getTarget();
            }
            return targets;
        }


        /**
         * Returns the index in the menu for a given action. Starts at 0.
         *
         * @param action The action that you want the index of.
         * @return The index of the given target in the context menu; otherwise -1.
         */
        public int getIndex(String action) {
            MenuEntry[] entries = getEntries();
            action = action.toLowerCase();
            for (int i = 0; i < entries.length; i++) {
                if (entries[i].getOption().toLowerCase().contains(action)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * Returns the index in the menu for a given action with a given target.
         * Starts at 0.
         *
         * @param action The action of the menu entry of which you want the index.
         * @param target The target of the menu entry of which you want the index.
         *               If target is null, operates like getIndex(String action).
         * @return The index of the given target in the context menu; otherwise -1.
         */
        public int getIndex(String action, String... target) {
            if (target == null) {
                return getIndex(action);
            }
            action = action.toLowerCase();
            String[] actions = getActions();
            String[] targets = getTargets();
            /* Throw exception if lenghts unequal? */
            for (int i = 0; i < Math.min(actions.length, targets.length); i++) {
                if (actions[i].toLowerCase().contains(action)) {
                    boolean targetMatch = false;
                    for (String targetPart : target) {
                        if (targets[i].toLowerCase().contains(targetPart.toLowerCase())) {
                            targetMatch = true;
                        } else {
                            targetMatch = false;
                        }
                    }
                    if (targetMatch)
                        return i;
                }
            }
            return -1;
        }


        /**
         * Checks whether or not a given action (or action substring) is present in
         * the menu.
         *
         * @param action The action or action substring.
         * @return <tt>true</tt> if present, otherwise <tt>false</tt>.
         */
        public boolean contains(final String action) {
            return getIndex(action) != -1;
        }

        /**
         * Checks whether or not a given action with given target is present
         * in the menu.
         *
         * @param action The action or action substring.
         * @param target The target or target substring.
         * @return <tt>true</tt> if present, otherwise <tt>false</tt>.
         */
        public boolean contains(final String action, final String target) {
            return getIndex(action, target) != -1;
        }
        
}