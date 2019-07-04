package net.runelite.client.rsb.methods;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuEntry;
import net.runelite.client.rsb.wrappers.RSItem;


import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Context menu related operations.
 */
@Slf4j
public class Menu extends MethodProvider {

    private static final Pattern HTML_TAG = Pattern
            .compile("(^[^<]+>|<[^>]+>|<[^>]+$)");


    private Method menuX;
    private Method menuY;

    final int TOP_OF_MENU_BAR = 18;
    final int MENU_ENTRY_LENGTH = 15;
    final int ESTIMATED_FONT_SIZE = 7;
    final int CANVAS_LENGTH = 503;

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
    public boolean doAction(final String action, final String target) {
        final int idx = getIndex(action, target);
        if (!isOpen()) {
            if (idx == -1) {
                return false;
            }
            if (idx == getEntries().length - 1) {
                methods.mouse.click(true);
                return true;
            }
            methods.mouse.click(false);
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
        ItemComposition itemDef = item.getDefinition();
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
            return clickMain(methods.client.getMenuEntries(), i);
        }
        return false;
    }

    private boolean clickMain(MenuEntry[] entries, final int i) {
        //Indexes in the menu entries are reversed in game so we need to accommodate for this
        int inGameIndexPosition = entries.length - i;
        String item = (entries[i].getOption() + " " + entries[i].getTarget().replaceAll("<.*?>", ""));
        Point menu = getLocation();
        //Lower bounds of random are just low numbers and could be any other low number really
        int xOff = random(4, item.length() * ESTIMATED_FONT_SIZE);
        int yOff = TOP_OF_MENU_BAR + (((MENU_ENTRY_LENGTH * (inGameIndexPosition - 1)) + random(1, (MENU_ENTRY_LENGTH - 2))));
        methods.mouse.move(menu.x + xOff, menu.y + yOff, 2, 2);
        if (isOpen()) {
            methods.mouse.click(true);
            return true;
        }
        return false;
    }


    /**
     * Returns the index in the menu for a given action. Starts at 0.
     *
     * @param action The action that you want the index of.
     * @return The index of the given target in the context menu; otherwise -1.
     */
    public int getIndex(String action) {
        action = action.toLowerCase();
        MenuEntry[] entries = getEntries();
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
    public int getIndex(String action, String target) {
        if (target == null) {
            return getIndex(action);
        }
        action = action.toLowerCase();
        target = target.toLowerCase();
        String[] actions = getActions();
        String[] targets = getTargets();
        /* Throw exception if lenghts unequal? */
        for (int i = 0; i < Math.min(actions.length, targets.length); i++) {
            if (actions[i].toLowerCase().contains(action) &&
                    targets[i].toLowerCase().contains(target)) {
                return i;
            }
        }
        return -1;
    }

    public MenuEntry[] getEntries() {
        return methods.client.getMenuEntries();
    }

    public String[] getActions() {
        String[] actions = new String[getEntries().length];
        for (int i = 0; i < getSize(); i++) {
            actions[i] = getEntries()[i].getOption();
        }
        return actions;
    }

    public String[] getTargets() {
        String[] targets = new String[getEntries().length];
        for (int i = 0; i < getSize(); i++) {
            targets[i] = getEntries()[i].getTarget();
        }
        return targets;
    }

    /**
     * Returns the menu's location.
     *
     * @return The screen space point if the menu is open; otherwise null.
     */
    public Point getLocation() {
        if (isOpen()) {
            return new Point(this.getMenuX(), this.getMenuY());
        }
        return null;
    }

    /**
     * Returns the menu's item count.
     *
     * @return The menu size.
     */
    public int getSize() {
        return getEntries().length;
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
     * Assigns the respective methods in this class by going through the
     * injected client's methods and finding the method so we can assign
     * it to a variable for later use
     */
    public void assignMethods() {
        /**
         * The names inside the client for the respective method
         */
        final String GET_X_METHOD = "nx";
        final String GET_Y_METHOD = "et";
        Class<?> clientClass = methods.runeLite.getInjector().getInstance(Client.class).getClass();
        for (Method i : clientClass.getDeclaredMethods()) {
            i.setAccessible(true);
            if (i.getReturnType().toString().equalsIgnoreCase("int")) {
                if (i.getParameterCount() == 0) {
                    if (i.getModifiers() == 1) {
                        if (i.getName() == GET_X_METHOD) {
                            menuX = i;
                        }
                        if (i.getName() == GET_Y_METHOD) {
                            menuY = i;
                        }
                    }
                }

            }
        }
    }

    /**
     * Gets the top left corner X of the menu on screen from invoking the method used in the injected client
     *
     * @return the menu x
     */
    public int getMenuX() {
        Class<?> clientClass = methods.runeLite.getInjector().getInstance(Client.class).getClass();
        try {
            return (int) menuX.invoke(clientClass.newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Gets the top left corner Y of the menu on screen
     *
     * @return the menu y
     */
    public int getMenuY() {
        int numberOfEntries = getEntries().length;
        int mousePositionY = methods.virtualMouse.getClientPressY();
        int menuHeight = MENU_ENTRY_LENGTH * numberOfEntries + TOP_OF_MENU_BAR;
        int offset = CANVAS_LENGTH - (mousePositionY + menuHeight);
        if (numberOfEntries <= 32)
            return mousePositionY +  ((offset < 0) ? offset : 0);
        return 0;
    }


}
