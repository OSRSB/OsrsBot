package rsb.walker.dax_api.shared.helpers;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;
import rsb.internal.wrappers.Filter;
import rsb.methods.Calculations;
import rsb.methods.Web;
import rsb.walker.dax_api.Filters;
import rsb.wrappers.RSGroundItem;
import rsb.wrappers.RSItem;
import rsb.wrappers.subwrap.ChooseOption;
import rsb.wrappers.subwrap.RSMenuNode;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class RSItemHelper {

    public static boolean click(String itemNameRegex, String itemAction){
        return click(new Filter<RSItem>() {
            @Override
            public boolean test(RSItem item) {
                return getItemName(item).matches(itemNameRegex) && Arrays.stream(getItemActions(item)).anyMatch(s -> s.equals(itemAction));
            }
        }, itemAction);
    }

    public static boolean clickMatch(RSItem item, String regex){
        return item.doAction((Filter<RSMenuNode>) rsMenuNode -> {
            String action = rsMenuNode.getAction();
            return action != null && action.matches(regex);
        });
    }

    public static boolean click(int itemID){
        return click(itemID, null);
    }

    public static boolean click(int itemID, String action){
        return click(Filters.Items.idEquals(itemID), action, true);
    }

    public static boolean click(Filter<RSItem> filter, String action){
        return click(filter, action, true);
    }

    /**
     *
     * @param filter filter for items
     * @param action action to click
     * @param one click only one item.
     * @return
     */
    public static boolean click(Filter<RSItem> filter, String action, boolean one){
        if (action == null){
            action = "";
        }
        List<RSItem> list = Arrays.stream(Web.methods.inventory.find(filter)).collect(Collectors.toList());
        if (one) {
            RSItem closest = getClosestToMouse(list);
            return closest != null && closest.doAction(action);
        }
        boolean value = false;
        while (!list.isEmpty()){
            RSItem item = getClosestToMouse(list);
            if (item != null) {
                list.remove(item);
                if (item.doAction(action)){
                    value = true;
                }
            }
        }
        return value;
    }

    public static boolean click(RSItem item, String action){
        if (Web.methods.bank.isOpen()){
            Web.methods.bank.close();
        }
        return action != null ? item.doAction(action) : item.doClick();
    }

    public static boolean use(int itemID){
        String name = Web.methods.inventory.getSelectedItemName();
        ItemComposition rsItemDefinition = Web.methods.runeLite.getItemManager().getItemComposition(itemID);
        String itemName;
        if (Web.methods.inventory.isItemSelected() && name != null && (itemName = rsItemDefinition.getName()) != null && name.equals(itemName)){
            return true;
        } else if (Web.methods.inventory.isItemSelected()){
            Web.methods.mouse.click(false);
            Web.methods.chooseOption.select("Cancel");
        }
        return RSItemHelper.click(itemID, "Use");
    }

    public static RSItem getClosestToMouse(List<RSItem> rsItems){
        Point mouse = Calculations.convertRLPointToAWTPoint(Web.methods.mouse.getLocation());
        rsItems.sort(Comparator.comparingInt(o -> (int) getCenter(o.getItem().getArea()).distance(mouse)));
        return rsItems.size() > 0 ? rsItems.get(0) : null;
    }

    private static Point getCenter(Rectangle rectangle){
        return new Point(rectangle.x + rectangle.width/2, rectangle.y + rectangle.height/2);
    }


    public static RSItem getItem(Filter<RSItem> filter){
        return getClosestToMouse(Arrays.stream(Web.methods.inventory.find(filter)).collect(Collectors.toList()));
    }

    public static boolean isNoted(RSItem item) {
        return item != null && isNoted(item.getID());
    }

    public static boolean isNoted(int id) {
        ItemComposition definition = Web.methods.runeLite.getItemManager().getItemComposition(id);
        return definition.getNote() == 799;
    }


    public static String[] getItemActions(RSGroundItem rsGroundItem){
        return rsGroundItem.getItem().getGroundActions();
    }

    public static String[] getItemActions(RSItem rsItem){
        return getItemActions(rsItem.getDefinition());
    }


    public static String getItemName(int id){
        return getItemName(Web.methods.runeLite.getItemManager().getItemComposition(id));
    }

    public static String getItemName(RSGroundItem rsGroundItem){
        return getItemName(rsGroundItem.getItem().getDefinition());
    }

    public static String getItemName(RSItem rsItem){
        return getItemName(rsItem.getDefinition());
    }


    public static boolean isStackable(int id) {
        ItemComposition definition = Web.methods.runeLite.getItemManager().getItemComposition(id);
        return definition != null && definition.isStackable();
    }


    public static boolean isStackable(RSItem rsItem) {
        ItemComposition definition = rsItem.getDefinition();
        return definition != null && definition.isStackable();
    }

    private static String[] getItemActions(ItemComposition rsItemDefinition){
        if (rsItemDefinition == null){
            return new String[0];
        }
        String[] actions = rsItemDefinition.getInventoryActions();
        return actions != null ? actions : new String[0];
    }

    private static String getItemName(ItemComposition definition){
        String name = definition.getName();
        return name != null ? name : "null";
    }

    public static int distanceToMouse(RSItem item){
        Rectangle rectangle = item.getItem().getArea();
        if (rectangle == null){
            return Integer.MAX_VALUE;
        }
        return (int) Web.methods.mouse.getLocation().distanceTo(new net.runelite.api.Point(rectangle.x + rectangle.width, rectangle.y + rectangle.height));
    }


}
