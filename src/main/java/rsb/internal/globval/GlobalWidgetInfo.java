package rsb.internal.globval;

import net.runelite.api.widgets.WidgetInfo;

/**
 * The list of widget info in the form of (parent, child)
 * The [Group] at the start and end denotes a segment
 * Though it is not indicative of the underlying parent interface
 * Rather just for grouping and potentially future updating purposes
 */
public enum GlobalWidgetInfo {
    /**
     * [CHAT] Widget Info
     */
    CHATBOX_MESSAGES(WidgetInfo.CHATBOX_MESSAGES),
    CHATBOX_FULL_INPUT(WidgetInfo.CHATBOX_FULL_INPUT),
    DIALOG_NPC_TEXT(WidgetInfo.DIALOG_NPC_TEXT),
    // [CHAT

    /**
     * [INVENTORY] Widget Info
     */
    INVENTORY_ITEMS_CONTAINER(WidgetInfo.INVENTORY),
    INVENTORY_DESTROY_ITEM(WidgetInfo.DESTROY_ITEM),
    INVENTORY_DESTROY_ITEM_YES(WidgetInfo.DESTROY_ITEM_YES),
    // [INVENTORY]

    /**
     * [QUICKPRAYER]
     */
    QUICK_PRAYER_PRAYERS(WidgetInfo.QUICK_PRAYER_PRAYERS),
    // [QUICKPRAYER]

    /**
     * [LOGOUT] Widget Info
     */
    LOGOUT_BUTTON(WidgetIndices.Logout.GROUP_INDEX, WidgetIndices.Logout.BUTTON_LOGOUT_CONTAINER),
    // [LOGOUT]

    /**
     * [WILDERNESS] Widget Info
     */
    PVP_WILDERNESS_LEVEL(WidgetIndices.PvPScreen.GROUP_INDEX, WidgetIndices.PvPScreen.WILDNERESS_LEVEL_LABEL),
    // [WILDERNESS]

    /**
     * [WORLDMAP] Widget Info
     */
    WORLD_MAP_VIEW(WidgetIndices.WorldMap.GROUP_INDEX, WidgetIndices.WorldMap.MAPVIEW_CONTAINER),
    // [WORLDMAP]

    /**
     * [LOGIN] Widget Info
     */
    LOGIN_MOTW(WidgetIndices.ClickToPlayScreen.GROUP_INDEX, WidgetIndices.ClickToPlayScreen.MOTW_CONTAINER),
    LOGIN_MOTW_TITLE(WidgetIndices.ClickToPlayScreen.GROUP_INDEX, WidgetIndices.ClickToPlayScreen.MOTW_TITLE_LABEL),
    LOGIN_MOTW_TEXT(WidgetIndices.ClickToPlayScreen.GROUP_INDEX, WidgetIndices.ClickToPlayScreen.MOTW_TEXT_LABEL),
    LOGIN_CLICK_TO_PLAY(WidgetIndices.ClickToPlayScreen.GROUP_INDEX, WidgetIndices.ClickToPlayScreen.BUTTON_CLICK_HERE_TO_PLAY_CONTAINER),
    // [LOGIN]

    /**
     * [BANK] Widget Info
     */
    BANK_DYNAMIC_CONTAINER(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.FRAME_DYNAMIC_CONTAINER),
    BANK_ITEM_COUNT(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.ITEM_SLOTS_USED_LABEL),
    BANK_LINE_SEPARATOR(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.ITEM_SLOTS_SEPARATOR_LINE),
    BANK_ITEM_MAX(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.ITEM_SLOTS_MAX_LABEL),
    BANK_TAB(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.TABS_DYNAMIC_CONTAINER),
    BANK_ITEMS_CONTAINER(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.ITEMS_DYNAMIC_CONTAINER),
    BANK_SCROLLBAR(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.SCROLLBAR_DYNAMIC_CONTAINER),
    BANK_BUTTON_SWAP(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.BUTTON_SWAP_CONTAINER),
    BANK_BUTTON_INSERT(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.BUTTON_INSERT_CONTAINER),
    BANK_BUTTON_ITEM(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.BUTTON_ITEM_CONTAINER),
    BANK_BUTTON_NOTE(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.BUTTON_NOTE_CONTAINER),
    BANK_BUTTON_SEARCH(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.BUTTON_SEARCH_SPRITE),
    BANK_BUTTON_DEPOSIT_CARRIED_ITEMS(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.BUTTON_DEPOSIT_CARRIED_ITEMS_SPRITE),
    BANK_BUTTON_DEPOSIT_WORN_ITEMS(WidgetIndices.Bank.GROUP_INDEX, WidgetIndices.Bank.BUTTON_DEPOSIT_WORN_ITEMS_SPRITE),
    BANK_SEARCH_INPUT(CHATBOX_FULL_INPUT), // dafuq is this?
    // [BANK]

    /**
     * [BANK INVENTORY ITEMS] Widget Info
     */
    BANK_INVENTORY_ITEMS_CONTAINER(WidgetIndices.BankInventoryItems.GROUP_INDEX, WidgetIndices.BankInventoryItems.ITEMS_DYNAMIC_CONTAINER),
    // [BANK INVENTORY ITEMS]

    /**
     * [DEPOSIT BOX] Widget Info
     */
    DEPOSIT_DYNAMIC_COMPONENTS(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.DYNAMIC_CONTAINER),
    DEPOSIT_ITEMS_CONTAINER(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.ITEMS_DYNAMIC_CONTAINER),
    DEPOSIT_BUTTON_DEPOSIT_INVENTORY_ITEMS(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.BUTTON_DEPOSIT_INVENTORY_SPRITE),
    DEPOSIT_BUTTON_DEPOSIT_WORN_ITEMS(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.BUTTON_DEPOSIT_WORN_ITEMS_SPRITE),
    DEPOSIT_BUTTON_DEPOSIT_LOOT(WidgetIndices.DepositBox.GROUP_INDEX, WidgetIndices.DepositBox.BUTTON_DEPOSIT_LOOT_SPRITE),
    // [DEPOSIT BOX]

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
    // [COMBAT]

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
    // [EQUIPMENT]

    /**
     * [TRADE] Widget Info
     */
    TRADE_MAIN_SCREEN_WINDOW_CONTAINER(WidgetIndices.TradeFirstScreen.GROUP_INDEX, WidgetIndices.TradeFirstScreen.TRADE_WINDOW_CONTAINER),
    TRADE_MAIN_SCREEN_PARTNER_FREE_SLOTS(WidgetIndices.TradeFirstScreen.GROUP_INDEX, WidgetIndices.TradeFirstScreen.BUTTON_FREE_INV_SLOTS_LABEL),
    TRADE_MAIN_SCREEN_ACCEPT(WidgetIndices.TradeFirstScreen.GROUP_INDEX, WidgetIndices.TradeFirstScreen.BUTTON_ACCEPT_DYNAMIC_CONTAINER),
    TRADE_MAIN_SCREEN_DECLINE(WidgetIndices.TradeFirstScreen.GROUP_INDEX, WidgetIndices.TradeFirstScreen.BUTTON_DECLINE_DYNAMIC_CONTAINER),
    TRADE_MAIN_SCREEN_MY_OFFER_ITEMS(WidgetIndices.TradeFirstScreen.GROUP_INDEX, WidgetIndices.TradeFirstScreen.MY_OFFER_DYNAMIC_CONTAINER),
    TRADE_MAIN_SCREEN_PARTNER_OFFER_ITEMS(WidgetIndices.TradeFirstScreen.GROUP_INDEX, WidgetIndices.TradeFirstScreen.PARTNER_OFFER_DYNAMIC_CONTAINER),
    TRADE_MAIN_SCREEN_PARTNER_NAME(WidgetIndices.TradeFirstScreen.GROUP_INDEX, WidgetIndices.TradeFirstScreen.NAME_OF_TRADE_PARTNER_LABEL),
    TRADE_SECOND_SCREEN_ACCEPT(WidgetIndices.TradeSecondScreen.GROUP_INDEX, WidgetIndices.TradeSecondScreen.BUTTON_ACCEPT_SHADOW_BOX),
    TRADE_SECOND_SCREEN_DECLINE(WidgetIndices.TradeSecondScreen.GROUP_INDEX, WidgetIndices.TradeSecondScreen.BUTTON_DECLINE_SHADOW_BOX),
    TRADE_SECOND_SCREEN_MY_OFFER_ITEM_LIST(WidgetIndices.TradeSecondScreen.GROUP_INDEX, WidgetIndices.TradeSecondScreen.MY_OFFER_ITEM_LIST_DYNAMIC_CONTAINER),
    TRADE_SECOND_SCREEN_PARTNER_OFFER_ITEM_LIST(WidgetIndices.TradeSecondScreen.GROUP_INDEX, WidgetIndices.TradeSecondScreen.MY_PARTNER_OFFER_ITEM_LIST_DYNAMIC_CONTAINER),
    TRADE_SECOND_SCREEN_PARTNER_NAME(WidgetIndices.TradeSecondScreen.GROUP_INDEX, WidgetIndices.TradeSecondScreen.NAME_OF_TRADE_PARTNER_LABEL),
    // [TRADE]

    /**
     * [STORE] Widget Info
     */
    STORE_DYNAMIC_COMPONENTS(WidgetIndices.Store.GROUP_INDEX, WidgetIndices.Store.DYNAMIC_CONTAINER),
    STORE_ITEMS_CONTAINER(WidgetIndices.Store.GROUP_INDEX, WidgetIndices.Store.ITEMS_DYNAMIC_CONTAINER),
    // [STORE]

    /**
     * [GRANDEXCHANGE]
     */
    GRAND_EXCHANGE_SEARCH_INPUT(CHATBOX_FULL_INPUT),
    GRAND_EXCHANGE_OFFER_WINDOW(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.GrandExchange.CONTAINER),
    GRAND_EXCHANGE_INTERFACE_LAYOUT(WidgetIndices.GrandExchange.GROUP_INDEX,WidgetIndices.GrandExchange.DYNAMIC_CONTAINER ),
    GRAND_EXCHANGE_TITLE(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.GrandExchange.TITLE_DYNAMIC_CONTAINER),
    GRAND_EXCHANGE_COLLECTION_AREA(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.GrandExchange.OFFER_STATUS_COLLECTION_AREA_DYNAMIC_CONTAINER),
    GRAND_EXCHANGE_COLLECT_AREA_ONE(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.DynamicComponents.GrandExchangeCollectionArea.RIGHT_ITEM_SPRITE),
    GRAND_EXCHANGE_COLLECT_AREA_TWO(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.DynamicComponents.GrandExchangeCollectionArea.LEFT_ITEM_SPRITE),
    GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER(WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER),
    // [GRANDEXCHANGE]

    /**
     * [SKILL] Widget Info
     */
    SKILL(0,0), // dafuq is this?
    // [SKILL]

    /**
     * [MAGIC] Widget Info
     */
    MAGIC_SPELL_LIST(WidgetIndices.SpellbookTab.GROUP_INDEX, WidgetIndices.SpellbookTab.SPELLS_CONTAINER),
    MAGIC_AUTOCAST_SPELL_LIST(WidgetIndices.SpellAutocast.GROUP_INDEX, WidgetIndices.SpellAutocast.SPELLS_DYNAMIC_CONTAINER),
    // [MAGIC]

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
    // [MINIMAP]

    /**
     * [PRAYER] Widget Info
     */
    PRAYER_STANDARD_BOOK(WidgetIndices.PrayersTab.GROUP_ID, WidgetIndices.PrayersTab.PRAYERS_CONTAINER);
    // [PRAYER]

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
