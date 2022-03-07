package rsb.internal.globval;

import net.runelite.api.widgets.WidgetInfo;

import static rsb.internal.globval.GlobalWidgetId.*;

/**
 * The list of widget info in the form of (parent, child)
 * The [Group] at the start and end denotes a segment
 * Though it is not indicative of the underlying parent interface
 * Rather just for grouping and potentially future updating purposes
 */
public enum GlobalWidgetInfo {
    /** [LOGIN] Widget Info */
    LOGIN_MOTW(INTERFACE_LOGIN_SCREEN, INTERFACE_LOGIN_SCREEN_MOTW),
    //[LOGIN]

    /** [MISC] Widget Info */
    WORLD_MAP_VIEW(WidgetInfo.WORLD_MAP_VIEW),
    LOGOUT_BUTTON(WidgetInfo.LOGOUT_BUTTON),
    LOGIN_CLICK_TO_PLAY_SCREEN_MESSAGE_OF_THE_DAY(WidgetInfo.LOGIN_CLICK_TO_PLAY_SCREEN_MESSAGE_OF_THE_DAY),

    //[MISC]

    /**
     * [CHAT] Widget Info
     */
    CHATBOX_MESSAGES(WidgetInfo.CHATBOX_MESSAGES),
    CHATBOX_FULL_INPUT(WidgetInfo.CHATBOX_FULL_INPUT),
    DIALOG_NPC_TEXT(WidgetInfo.DIALOG_NPC_TEXT),
    //[CHAT

    /**
     * [WILDERNESS] Widget Info
     */
    PVP_WILDERNESS_LEVEL(WidgetInfo.PVP_WILDERNESS_LEVEL),
    //[WILDERNESS]

    /**
     * [INVENTORY] Widget Info
     */
    INVENTORY_ITEMS_CONTAINER(WidgetInfo.INVENTORY),
    INVENTORY_DESTROY_ITEM(WidgetInfo.DESTROY_ITEM),
    INVENTORY_DESTROY_ITEM_YES(WidgetInfo.DESTROY_ITEM_YES),
    //[INVENTORY]

    /**
     * [BANK] Widget Info
     */
    BANK_DYNAMIC_COMPONENTS(INTERFACE_BANK, INTERFACE_BANK_DYNAMIC_COMPONENTS),
    BANK_INVENTORY_ITEMS_CONTAINER(INTERFACE_BANK, INTERFACE_BANK_INVENTORY),
    BANK_ITEM_COUNT(INTERFACE_BANK, INTERFACE_BANK_ITEM_SLOTS_USED),
    BANK_LINE_SEPARATOR(INTERFACE_BANK, INTERFACE_BANK_SEPARATOR_LINE),
    //7
    BANK_ITEM_MAX(INTERFACE_BANK, INTERFACE_BANK_ITEM_SLOTS_MAX),
    //9
    BANK_TAB(INTERFACE_BANK, INTERFACE_BANK_TABS),
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
    DEPOSIT_DYNAMIC_COMPONENTS(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.DYNAMIC_CONTAINER),
    DEPOSIT_ITEMS_CONTAINER(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.ITEMS_DYNAMIC_CONTAINER),
    DEPOSIT_BUTTON_DEPOSIT_INVENTORY_ITEMS(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.BUTTON_DEPOSIT_INVENTORY_SPRITE),
    DEPOSIT_BUTTON_DEPOSIT_WORN_ITEMS(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.BUTTON_DEPOSIT_WORN_ITEMS_SPRITE),
    DEPOSIT_BUTTON_DEPOSIT_LOOT(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.BUTTON_DEPOSIT_LOOT_SPRITE),
    //[DEPOSIT BOX]

    /**
     * [COMBAT] Widget Info
     */
    COMBAT_STYLE_ONE(WidgetIndices.CombatOptionsTab.GROUP_INDEX, WidgetIndices.CombatOptionsTab.WEAPON_STYLE_ONE_CONTAINER),
    COMBAT_STYLE_TWO(WidgetIndices.CombatOptionsTab.GROUP_INDEX, WidgetIndices.CombatOptionsTab.WEAPON_STYLE_TWO_CONTAINER),
    COMBAT_STYLE_THREE(WidgetIndices.CombatOptionsTab.GROUP_INDEX, WidgetIndices.CombatOptionsTab.WEAPON_STYLE_THREE_CONTAINER),
    COMBAT_STYLE_FOUR(WidgetIndices.CombatOptionsTab.GROUP_INDEX, WidgetIndices.CombatOptionsTab.WEAPON_STYLE_FOUR_CONTAINER),
    COMBAT_AUTO_CAST_DEFENSIVE_SPELL(WidgetIndices.CombatOptionsTab.GROUP_INDEX, WidgetIndices.CombatOptionsTab.AUTO_CAST_DEFENSIVE_SPELL_CONTAINER),
    COMBAT_AUTO_CAST_SPELL(WidgetIndices.CombatOptionsTab.GROUP_INDEX, WidgetIndices.CombatOptionsTab.AUTO_CAST_SPELL_CONTAINER),
    COMBAT_AUTO_RETALIATE(WidgetIndices.CombatOptionsTab.GROUP_INDEX, WidgetIndices.CombatOptionsTab.AUTO_RETALIATE_CONTAINER),
    //[COMBAT]

    /**
     * [EQUIPMENT] Widget Info
     */
    EQUIPMENT_ITEMS_CONTAINER(WidgetIndices.WornEquipmentTab.GROUP_INDEX, WidgetIndices.WornEquipmentTab.PARENT_CONTAINER),
    EQUIPMENT_HEAD(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.HEAD_DYNAMIC_CONTAINER),
    EQUIPMENT_BACK(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.BACK_DYNAMIC_CONTAINER),
    EQUIPMENT_NECK(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.NECK_DYNAMIC_CONTAINER),
    EQUIPMENT_WEAPON(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.WEAPON_DYNAMIC_CONTAINER),
    EQUIPMENT_BODY(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.CHEST_DYNAMIC_CONTAINER),
    EQUIPMENT_SHIELD(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.SHIELD_DYNAMIC_CONTAINER),
    EQUIPMENT_LEGS(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.LEGS_DYNAMIC_CONTAINER),
    EQUIPMENT_HANDS(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.HANDS_DYNAMIC_CONTAINER),
    EQUIPMENT_FEET(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.FEET_DYNAMIC_CONTAINER),
    EQUIPMENT_RING(WidgetIndices.WornEquipmentTab.GROUP_INDEX,  WidgetIndices.WornEquipmentTab.RING_DYNAMIC_CONTAINER),
    EQUIPMENT_AMMO(WidgetIndices.WornEquipmentTab.GROUP_INDEX, WidgetIndices.WornEquipmentTab.AMMUNITION_DYNAMIC_CONTAINER),
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
    TRADE_MAIN_SCREEN_PARTNER_FREE_SLOTS(0, 0), //TODO: Fix this

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
    STORE_INVENTORY_ITEMS_CONTAINER(INTERFACE_STORE, INTERFACE_STORE_ITEMS_CONTAINER),
    //[STORE]

    /**
     * [GRANDEXCHANGE]
     */
    GRAND_EXCHANGE_SEARCH_INPUT(CHATBOX_FULL_INPUT),
    GRAND_EXCHANGE_OFFER_WINDOW(INTERFACE_GRAND_EXCHANGE_WINDOW, INTERFACE_GRAND_EXCHANGE_OFFER_WINDOW),
    GRAND_EXCHANGE_DESCRIPTION(INTERFACE_GRAND_EXCHANGE_WINDOW, INTERFACE_GRAND_EXCHANGE_DESCRIPTION),
    GRAND_EXCHANGE_COLLECTION_AREA(INTERFACE_GRAND_EXCHANGE_WINDOW, INTERFACE_GRAND_EXCHANGE_COLLECTION_AREA),
    GRAND_EXCHANGE_COLLECT_AREA_ONE(INTERFACE_GRAND_EXCHANGE_COLLECTION_AREA, GRAND_EXCHANGE_COLLECT_BOX_ONE),
    GRAND_EXCHANGE_COLLECT_AREA_TWO(INTERFACE_GRAND_EXCHANGE_COLLECTION_AREA, GRAND_EXCHANGE_COLLECT_BOX_TWO),
    GRAND_EXCHANGE_INTERFACE_LAYOUT(INTERFACE_GRAND_EXCHANGE_WINDOW, INTERFACE_GRAND_EXCHANGE_INTERFACE_LAYOUT),

    GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER(WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER),
    /**
     * [SKILL] Widget Info
     */
    SKILL(0,0),
    //[SKILL]

    /**
     * [MAGIC] Widget Info
     */
    MAGIC_SPELL_LIST(INTERFACE_MAGIC_SPELL_BOOK, INTERFACE_MAGIC_SPELL_LIST),
    MAGIC_AUTOCAST_SPELL_LIST(INTERFACE_MAGIC_AUTOCAST_SPELL_BOOK, INTERFACE_MAGIC_AUTOCAST_SPELL_LIST),
    //[MAGIC]

    /**
     * [MINIMAP] Widget Info
     */
    MINIMAP_XP_ORB(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.XP_ORB_SPRITE),
    MINIMAP_HEALTH_ORB(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.HEALTH_ORB_CONTAINER),
    MINIMAP_HEALTH_ORB_TEXT(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.HEALTH_ORB_LABEL),
    MINIMAP_QUICK_PRAYER_ORB(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.QUICK_PRAYER_ORB_CONTAINER),
    MINIMAP_QUICK_PRAYER_ORB_TEXT(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.QUICK_PRAYER_ORB_LABEL),
    MINIMAP_QUICK_PRAYER_ORB_SPRITE(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.QUICK_PRAYER_ORB_SPRITE),
    MINIMAP_RUN_ORB(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.RUN_ORB_CONTAINER),
    MINIMAP_RUN_ORB_TEXT(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.RUN_ORB_LABEL),
    MINIMAP_SPEC_ORB(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.SPEC_ORB_CONTAINER),
    MINIMAP_SPEC_ORB_TEXT(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.SPEC_ORB_LABEL),
    MINIMAP_BOND_ORB(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.BOND_ORB_CONTAINER),
    MINIMAP_WIKI_BANNER(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.WIKI_BANNER_CONTAINER),
    MINIMAP_WORLDMAP_ORB(WidgetIndices.Minimap.GROUP_INDEX, WidgetIndices.Minimap.WORLDMAP_ORB_SPRITE),
    //[MINIMAP]

    /**
     * [PRAYER] Widget Info
     */
    PRAYER_NORMAL_BOOK(INTERFACE_PRAYER_BOOK, INTERFACE_NORMAL_PRAYERS),
    //[PRAYER]

    /**
     * [QUICKPRAYER]
     */
    QUICK_PRAYER_PRAYERS(WidgetInfo.QUICK_PRAYER_PRAYERS);
    //[QUICKPRAYER]

    private final int groupId;
    private final int childId;

    /**
     * Creates a new {@link GlobalWidgetInfo} enum value based on the passed child and group ids.
     * NOTE: Enumerated values can not be constructed. They must be added here or extended
     * via the extends keyword for your enumerated widget id class.
     *
     * @param groupId    the group id of the widget
     * @param childId    the child id of the widget
     */
    GlobalWidgetInfo(int groupId, int childId)
    {
        this.groupId = groupId;
        this.childId = childId;
    }

    /**
     * Creates a new {@link GlobalWidgetInfo} enum value based on the passed {@link WidgetInfo} enum value.
     * Basically used to re-skin an existing one to assist in contextually calling.
     * Or in simpler terms, an alias for an existing enum value.
     * NOTE: Enumerated values can not be constructed. They must be added here or extended
     * via the extends keyword for your enumerated widget id class.
     *
     * @param widgetInfo    the widget value to base the new enum value off of.
     */
    GlobalWidgetInfo(WidgetInfo widgetInfo) {
        this.groupId = widgetInfo.getGroupId();
        this.childId = widgetInfo.getChildId();
    }

    /**
     * Creates a new {@link GlobalWidgetInfo} enum value based on the passed {@link GlobalWidgetInfo} enum value.
     * Basically used to re-skin an existing one to assist in contextually calling.
     * Or in simpler terms, an alias for an existing enum value.
     * NOTE: Enumerated values can not be constructed. They must be added here or extended
     * via the extends keyword for your enumerated widget id class.
     *
     * @param widgetInfo    the widget value to base the new enum value off of.
     */
    GlobalWidgetInfo(GlobalWidgetInfo widgetInfo) {
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
     * Any usage where regular WidgetInfo is required and an easy conversion is required.
     * This will enable use of the RuneLite WidgetInfo class somewhat indirectly.
     * Method calls using this are for example:
     * client.getWidget()
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
