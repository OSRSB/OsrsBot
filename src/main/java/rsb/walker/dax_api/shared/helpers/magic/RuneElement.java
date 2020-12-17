package rsb.walker.dax_api.shared.helpers.magic;


import net.runelite.api.ItemComposition;
import rsb.internal.wrappers.Filter;
import rsb.methods.Web;
import rsb.wrappers.RSItem;

import java.util.Arrays;

public enum RuneElement {

    AIR("Air", "Smoke", "Mist", "Dust"),
    EARTH("Earth", "Lava", "Mud", "Dust"),
    FIRE("Fire", "Lava", "Smoke", "Steam"),
    WATER("Water", "Mud", "Steam", "Mist"),
    LAW("Law"),
    NATURE("Nature"),
    SOUL("Soul");

    private String[] alternativeNames;

    RuneElement(String... alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public String[] getAlternativeNames() {
        return alternativeNames;
    }

    public int getCount() {
        if (haveStaff()) {
            return Integer.MAX_VALUE;
        }
        RSItem[] items = Web.methods.inventory.find(new Filter<RSItem>() {
            @Override
            public boolean test(RSItem rsItem) {
                String name = getItemName(rsItem).toLowerCase();

                if (!name.contains("rune")) {
                    return false;
                }

                for (String alternativeName : alternativeNames) {
                    if (name.startsWith(alternativeName.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        });
        return Arrays.stream(items).mapToInt(RSItem::getStackSize).sum() + RunePouch.getQuantity(this);
    }

    private boolean haveStaff() {
        return Web.methods.equipment.find(new Filter<RSItem>() {
            @Override
            public boolean test(RSItem rsItem) {
                String name = getItemName(rsItem).toLowerCase();
                if (!name.contains("staff")) {
                    return false;
                }
                for (String alternativeName : alternativeNames) {
                    if (name.contains(alternativeName.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        }).length > 0;
    }

    /**
     * @param item
     * @return item name. Never null. "null" if no name.
     */
    private static String getItemName(RSItem item) {
        ItemComposition definition = item.getDefinition();
        String name;
        return definition == null || (name = definition.getName()) == null ? "null" : name;
    }


}
