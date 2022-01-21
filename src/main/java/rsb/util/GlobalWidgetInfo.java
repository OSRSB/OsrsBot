package rsb.util;

import net.runelite.api.widgets.WidgetInfo;

import static net.runelite.api.widgets.WidgetInfo.*;
import static rsb.util.GlobalWidgetId.*;
import static rsb.util.GlobalWidgetId.EquipmentSlotId.*;

/**
 * The list of widget info in the form of (parent, child)
 * The [Group] at the start and end denotes a segment
 * Though it is not indicative of the underlying parent interface
 * Rather just for grouping and potentially future updating purposes
 */
public enum GlobalWidgetInfo {
    /**
     * [BANK] Widget Info
     */
    BANK_DYNAMIC_COMPONENTS(INTERFACE_BANK, INTERFACE_BANK_DYNAMIC_COMPONENTS),
    BANK_ITEM_COUNT(INTERFACE_BANK, INTERFACE_BANK_ITEM_COUNT),
    //6
    //7
    BANK_ITEM_MAX(INTERFACE_BANK, INTERFACE_BANK_ITEM_MAX),
    //9
    BANK_TAB(INTERFACE_BANK, INTERFACE_BANK_TAB),
    //11
    BANK_INVENTORY(INTERFACE_BANK, INTERFACE_BANK_INVENTORY),
    BANK_SCROLLBAR(INTERFACE_BANK, INTERFACE_BANK_SCROLLBAR),
    BANK_BUTTON_SWAP(INTERFACE_BANK, INTERFACE_BANK_BUTTON_SWAP),
    //14
    //15
    //16
    BANK_BUTTON_INSERT(INTERFACE_BANK, INTERFACE_BANK_BUTTON_INSERT),
    BANK_BUTTON_ITEM(INTERFACE_BANK, INTERFACE_BANK_BUTTON_ITEM),
    BANK_BUTTON_NOTE(INTERFACE_BANK, INTERFACE_BANK_BUTTON_NOTE),
    BANK_BUTTON_SEARCH(INTERFACE_BANK, INTERFACE_BANK_BUTTON_SEARCH),
    BANK_BUTTON_DEPOSIT_CARRIED_ITEMS(INTERFACE_BANK, INTERFACE_BANK_BUTTON_DEPOSIT_CARRIED_ITEMS),
    BANK_BUTTON_DEPOSIT_WORN_ITEMS(INTERFACE_BANK, INTERFACE_BANK_BUTTON_DEPOSIT_WORN_ITEMS),
    BANK_SEARCH_INPUT(CHATBOX_FULL_INPUT),
    //[BANK]

    /**
     * [DEPOSIT BOX] Widget Info
     */
    DEPOSIT_DYNAMIC_COMPONENTS(INTERFACE_DEPOSIT_BOX, INTERFACE_DEPOSIT_DYNAMIC_COMPONENTS),
    DEPOSIT_BUTTON_DEPOSIT_WORN_ITEMS(INTERFACE_DEPOSIT_BOX, INTERFACE_DEPOSIT_BUTTON_DEPOSIT_WORN_ITEMS),
    DEPOSIT_BUTTON_DEPOSIT_CARRIED_ITEMS(INTERFACE_DEPOSIT_BOX, INTERFACE_DEPOSIT_BUTTON_DEPOSIT_CARRIED_ITEMS),
    DEPOSIT_BUTTON_DEPOSIT_LOOT(INTERFACE_DEPOSIT_BOX, INTERFACE_DEPOSIT_BUTTON_DEPOSIT_LOOT),
    //[DEPOSIT BOX]

    /**
     * [COMBAT] Widget Info
     */
    COMBAT_DEFENSIVE_CAST_SPELL(INTERFACE_COMBAT, INTERFACE_COMBAT_DEFENSIVE_CAST_SPELL),
    //23
    //24
    //25
    //26
    COMBAT_AUTO_CAST_SPELL(INTERFACE_COMBAT, INTERFACE_COMBAT_AUTO_CAST_SPELL),
    //28
    //29
    COMBAT_AUTO_RETALIATE(INTERFACE_COMBAT, INTERFACE_COMBAT_AUTO_RETALIATE),
    //[COMBAT]

    /**
     * [EQUIPMENT] Widget Info
     */
    EQUIPMENT_ITEM_SLOTS(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_ITEM_SLOTS),
    //12
    //13
    EQUIPMENT_HELMET(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_HELMET),
    EQUIPMENT_CAPE(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_CAPE),
    EQUIPMENT_NECK(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_NECK),
    EQUIPMENT_WEAPON(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_WEAPON),
    EQUIPMENT_BODY(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_BODY),
    EQUIPMENT_SHIELD(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_SHIELD),
    EQUIPMENT_LEGS(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_LEGS),
    EQUIPMENT_HANDS(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_HANDS),
    EQUIPMENT_FEET(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_FEET),
    EQUIPMENT_RING(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_RING),
    EQUIPMENT_AMMO(INTERFACE_EQUIPMENT, INTERFACE_EQUIPMENT_AMMO),
    //[EQUIPMENT]

    /**
     * [TRADE] Widget Info
     */
    TRADE_MAIN_SCREEN__INVENTORY_ITEMS_CONTAINER(INTERFACE_TRADE_MAIN, INTERFACE_TRADE_MAIN_INV_SLOTS),
    TRADE_MAIN_SCREEN_ACCEPT(INTERFACE_TRADE_MAIN, INTERFACE_TRADE_MAIN_ACCEPT),
    TRADE_MAIN_SCREEN_DECLINE(INTERFACE_TRADE_MAIN, INTERFACE_TRADE_MAIN_DECLINE),
    //...
    TRADE_MAIN_SCREEN_PERSONAL(INTERFACE_TRADE_MAIN, INTERFACE_TRADE_MAIN_PERSONAL),
    //26
    //27
    TRADE_MAIN_SCREEN_PARTNER(INTERFACE_TRADE_MAIN, INTERFACE_TRADE_MAIN_PARTNER),
    //29
    //30
    //31
    TRADE_MAIN_SCREEN_MAIN_NAME(INTERFACE_TRADE_MAIN, INTERFACE_TRADE_MAIN_NAME),

    TRADE_SECOND_SCREEN_ACCEPT(INTERFACE_TRADE_SECOND, INTERFACE_TRADE_SECOND_ACCEPT),
    TRADE_SECOND_SCREEN_DECLINE(INTERFACE_TRADE_SECOND, INTERFACE_TRADE_SECOND_DECLINE),
    TRADE_SECOND_SCREEN_PERSONAL(INTERFACE_TRADE_SECOND, INTERFACE_TRADE_SECOND_PERSONAL),
    //26
    //27
    TRADE_SECOND_SCREEN_PARTNER(INTERFACE_TRADE_SECOND, INTERFACE_TRADE_SECOND_PARTNER),
    //29
    //30
    //31
    TRADE_SECOND_SCREEN_SECOND_NAME(INTERFACE_TRADE_SECOND, INTERFACE_TRADE_SECOND_NAME),
    //[TRADE]

    /**
     * [STORE] Widget Info
     */
    STORE_DYNAMIC_COMPONENTS(INTERFACE_STORE, INTERFACE_STORE_DYNAMIC_COMPONENTS),
    STORE_INVENTORY(INTERFACE_STORE, INTERFACE_STORE_ITEMS)
    //[STORE]

    /**
     * [SKILL] Widget Info
     */

    ;

    private final int groupId;
    private final int childId;

    GlobalWidgetInfo(int groupId, int childId)
    {
        this.groupId = groupId;
        this.childId = childId;
    }

    GlobalWidgetInfo(WidgetInfo widgetInfo) {
        this.groupId = widgetInfo.getGroupId();
        this.childId = widgetInfo.getChildId();
    }

    /**
     * Gets the ID of the group-child pairing.
     *
     * @return the ID
     */
    public int getId()
    {
        return groupId << 16 | childId;
    }

    /**
     * Gets the group ID of the pair.
     *
     * @return the group ID
     */
    public int getGroupId()
    {
        return groupId;
    }

    /**
     * Gets the ID of the child in the group.
     *
     * @return the child ID
     */
    public int getChildId()
    {
        return childId;
    }

    /**
     * Gets the packed widget ID.
     *
     * @return the packed ID
     */
    public int getPackedId()
    {
        return groupId << 16 | childId;
    }

    /**
     * Utility method that converts an ID returned by {@link #getId()} back
     * to its group ID.
     *
     * @param id passed group-child ID
     * @return the group ID
     */
    public static int TO_GROUP(int id)
    {
        return id >>> 16;
    }

    /**
     * Utility method that converts an ID returned by {@link #getId()} back
     * to its child ID.
     *
     * @param id passed group-child ID
     * @return the child ID
     */
    public static int TO_CHILD(int id)
    {
        return id & 0xFFFF;
    }

    /**
     * Packs the group and child IDs into a single integer.
     *
     * @param groupId the group ID
     * @param childId the child ID
     * @return the packed ID
     */
    public static int PACK(int groupId, int childId)
    {
        return groupId << 16 | childId;
    }

}
