package net.runelite.rsb.internal.globval;

import net.runelite.api.widgets.WidgetInfo;

/**
 * The list of widget info in the form of (parent, child)
 * The [Group] at the start and end denotes a segment
 * Though it is not indicative of the underlying parent interface
 * Rather just for grouping and potentially future updating purposes
 */
public enum GlobalWidgetInfo {
    /**
     * [DIALOG DESTROY ITEM] Widget Info
     */
    DIALOG_DESTROY_ITEM(WidgetIndices.DialogDestroyItem.GROUP_INDEX, WidgetIndices.DialogDestroyItem.PARENT_DYNAMIC_CONTAINER),
    DIALOG_DESTROY_ITEM_YES(WidgetIndices.DialogDestroyItem.GROUP_INDEX, WidgetIndices.DialogDestroyItem.BUTTON_YES_SPRITE),
    // [DIALOG DESTROY ITEM]

    /**
     * [INVENTORY] Widget Info
     */
    INVENTORY_ITEMS_CONTAINER(WidgetIndices.Inventory.GROUP_INDEX, WidgetIndices.Inventory.SPRITE_GRID),
    // [INVENTORY]

    /**
     * [QUICK PRAYER]
     */
    QUICK_PRAYER_PRAYERS(WidgetIndices.QuickPrayers.GROUP_INDEX, WidgetIndices.QuickPrayers.PRAYERS_DYNAMIC_CONTAINER),
    // [QUICK PRAYER]

    /**
     * [CHAT] Widget Info
     */
    CHATBOX_MESSAGES(WidgetIndices.ChatBox.GROUP_INDEX, WidgetIndices.ChatBox.MESSAGES_CONTAINER),
    CHATBOX_FULL_INPUT(WidgetIndices.ChatBox.GROUP_INDEX, WidgetIndices.ChatBox.FULL_INPUT),
    // [CHAT

    /**
     * [DIALOG NPC]
     */
    DIALOG_NPC_TEXT(WidgetIndices.DialogNPC.GROUP_INDEX, WidgetIndices.DialogNPC.DIALOG_TEXT_LABEL),
    DIALOG_NPC_CONTINUE(WidgetIndices.DialogNPC.GROUP_INDEX, WidgetIndices.DialogNPC.DIALOG_CONTINUE_LABEL),
    // [DIALOG NPC]
    DIALOG_OPTION(WidgetIndices.DialogOptions.GROUP_INDEX, WidgetIndices.DialogOptions.CONTAINER),

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
     * [WORLD MAP] Widget Info
     */
    WORLD_MAP_VIEW(WidgetIndices.WorldMap.GROUP_INDEX, WidgetIndices.WorldMap.MAPVIEW_CONTAINER),
    // [WORLD MAP]

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
     * [GRAND EXCHANGE]
     */
    GRAND_EXCHANGE_SEARCH_INPUT(CHATBOX_FULL_INPUT),
    GRAND_EXCHANGE_OFFER_WINDOW(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.GrandExchange.CONTAINER),
    GRAND_EXCHANGE_INTERFACE_LAYOUT(WidgetIndices.GrandExchange.GROUP_INDEX,WidgetIndices.GrandExchange.DYNAMIC_CONTAINER ),
    GRAND_EXCHANGE_TITLE(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.GrandExchange.TITLE_DYNAMIC_CONTAINER),
    GRAND_EXCHANGE_COLLECTION_AREA(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.GrandExchange.OFFER_STATUS_COLLECTION_AREA_DYNAMIC_CONTAINER),
    GRAND_EXCHANGE_COLLECT_AREA_ONE(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.DynamicComponents.GrandExchangeCollectionArea.RIGHT_ITEM_SPRITE),
    GRAND_EXCHANGE_COLLECT_AREA_TWO(WidgetIndices.GrandExchange.GROUP_INDEX, WidgetIndices.DynamicComponents.GrandExchangeCollectionArea.LEFT_ITEM_SPRITE),
    GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER(WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER),
    // [GRAND EXCHANGE]

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
     * [RESIZABLE CLASSIC VIEWPORT] Widget Info
     */
    RESIZABLE_CLASSIC_MINIMAP(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.MINIMAP_CONTAINER),
    RESIZABLE_CLASSIC_COMPASS(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.MINIMAP_COMPASS_SPRITE),
    RESIZABLE_CLASSIC_CHAT_CHANNEL(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_CHAT_CHANNEL_SPRITE),
    RESIZABLE_CLASSIC_ACC_MANAGEMENT(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_ACC_MANAGEMENT_SPRITE),
    RESIZABLE_CLASSIC_FRIEND_LIST(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_FRIENDS_LIST_SPRITE),
    RESIZABLE_CLASSIC_LOGOUT(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_LOGOUT_SPRITE),
    RESIZABLE_CLASSIC_SETTINGS(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_SETTINGS_SPRITE),
    RESIZABLE_CLASSIC_EMOTES(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_EMOTES_SPRITE),
    RESIZABLE_CLASSIC_MUSIC_PLAYER(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_MUSIC_PLAYER_SPRITE),
    RESIZABLE_CLASSIC_COMBAT_OPTIONS(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_COMBAT_OPTIONS_SPRITE),
    RESIZABLE_CLASSIC_SKILLS(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_SKILLS_SPRITE),
    RESIZABLE_CLASSIC_QUESTS(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_QUEST_LIST_SPRITE),
    RESIZABLE_CLASSIC_INVENTORY(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_INVENTORY_SPRITE),
    RESIZABLE_CLASSIC_WORN_EQUIPMENT(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_WORN_EQUIPMENT_SPRITE),
    RESIZABLE_CLASSIC_PRAYER(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_PRAYER_SPRITE),
    RESIZABLE_CLASSIC_MAGIC(WidgetIndices.ResizableClassicViewport.GROUP_INDEX, WidgetIndices.ResizableClassicViewport.TAB_MAGIC_SPRITE),
    // [RESIZABLE CLASSIC VIEWPORT]

    /**
     * [RESIZABLE MODERN VIEWPORT] Widget Info
     */
    RESIZABLE_MODERN_MINIMAP(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.MINIMAP_CONTAINER),
    RESIZABLE_MODERN_COMPASS(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.MINIMAP_COMPASS_SPRITE),
    RESIZABLE_MODERN_CHAT_CHANNEL(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_CHAT_CHANNEL_SPRITE),
    RESIZABLE_MODERN_ACC_MANAGEMENT(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_ACC_MANAGEMENT_SPRITE),
    RESIZABLE_MODERN_FRIEND_LIST(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_FRIENDS_LIST_SPRITE),
    RESIZABLE_MODERN_LOGOUT(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.LOGOUT_SPRITE),
    RESIZABLE_MODERN_SETTINGS(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_SETTINGS_SPRITE),
    RESIZABLE_MODERN_EMOTES(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_EMOTES_SPRITE),
    RESIZABLE_MODERN_MUSIC_PLAYER(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_MUSIC_PLAYER_SPRITE),
    RESIZABLE_MODERN_COMBAT_OPTIONS(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_COMBAT_OPTIONS_SPRITE),
    RESIZABLE_MODERN_SKILLS(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_SKILLS_SPRITE),
    RESIZABLE_MODERN_QUESTS(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_QUEST_LIST_SPRITE),
    RESIZABLE_MODERN_INVENTORY(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_INVENTORY_SPRITE),
    RESIZABLE_MODERN_WORN_EQUIPMENT(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_WORN_EQUIPMENT_SPRITE),
    RESIZABLE_MODERN_PRAYER(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_PRAYER_SPRITE),
    RESIZABLE_MODERN_MAGIC(WidgetIndices.ResizableModernViewport.GROUP_INDEX, WidgetIndices.ResizableModernViewport.TAB_MAGIC_SPRITE),
    // [RESIZABLE MODERN VIEWPORT]

    /**
     * [FIXED CLASSIC VIEWPORT] Widget Info
     */
    FIXED_CLASSIC_MINIMAP(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.MINIMAP_CONTAINER),
    FIXED_CLASSIC_COMPASS(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.MINIMAP_COMPASS_SPRITE),
    FIXED_CLASSIC_CHAT_CHANNEL(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_CHAT_CHANNEL_SPRITE),
    FIXED_CLASSIC_ACC_MANAGEMENT(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_ACC_MANAGEMENT_SPRITE),
    FIXED_CLASSIC_FRIEND_LIST(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_FRIENDS_LIST_SPRITE),
    FIXED_CLASSIC_LOGOUT(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_LOGOUT_SPRITE),
    FIXED_CLASSIC_SETTINGS(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_SETTINGS_SPRITE),
    FIXED_CLASSIC_EMOTES(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_EMOTES_SPRITE),
    FIXED_CLASSIC_MUSIC_PLAYER(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_MUSIC_PLAYER_SPRITE),
    FIXED_CLASSIC_COMBAT_OPTIONS(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_COMBAT_OPTIONS_SPRITE),
    FIXED_CLASSIC_SKILLS(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_SKILLS_SPRITE),
    FIXED_CLASSIC_QUESTS(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_QUEST_LIST_SPRITE),
    FIXED_CLASSIC_INVENTORY(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_INVENTORY_SPRITE),
    FIXED_CLASSIC_WORN_EQUIPMENT(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_WORN_EQUIPMENT_SPRITE),
    FIXED_CLASSIC_PRAYER(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_PRAYER_SPRITE),
    FIXED_CLASSIC_MAGIC(WidgetIndices.FixedClassicViewport.GROUP_INDEX, WidgetIndices.FixedClassicViewport.TAB_MAGIC_SPRITE),
    // [FIXED CLASSIC  VIEWPORT]

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
