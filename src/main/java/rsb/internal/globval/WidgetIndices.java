package rsb.internal.globval;

public class WidgetIndices {
    /**
     * Describes bank widgets
     * Last reviewed: 2/3/2022 1:57 am UTC+1
     */
    public static class Bank {
        public static final int GROUP_INDEX = 12;
        public static final int INVENTORY_GROUP_INDEX = 15;
        public static final int PARENT_CONTAINER = 0;
        public static final int CONTAINER = 1;
        public static final int FRAME_DYNAMIC_CONTAINER = 2; // Contains stone border sprites 0-11, 11 is close button
        public static final int TITLE_LABEL = 3;
        public static final int INVENTORY_ITEM_CONTAINER = 3; // In INVENTORY_GROUP_INDEX, spot #3
        public static final int BUTTON_SHOW_TUTORIAL_CONTAINER = 4;
        public static final int ITEM_SLOTS_USED_LABEL = 5;
        public static final int ITEM_SLOTS_SEPARATOR_LINE = 6;
        public static final int ITEM_SLOTS_CONTAINER= 7;
        public static final int BUTTON_GROUP_STORAGE_CONTAINER = 8;
        public static final int ITEM_SLOTS_MAX_LABEL = 9;
        public static final int INNER_FRAME_DYNAMIC_CONTAINER = 10; // Contains 0 sprite tab add
        public static final int TABS_DYNAMIC_CONTAINER = 11; // Contains TAB sprites 0-19
        public static final int TABS_SEPARATOR_LINE = 12;
        public static final int ITEMS_DYNAMIC_CONTAINER = 13; // Contains 0-x bank items
        public static final int SCROLLBAR_DYNAMIC_CONTAINER = 14; // Contains 0-5 sprites
        public static final int FOOTER_CONTAINER = 15;
        public static final int REARRANGE_MODE_LABEL = 16;
        public static final int BUTTON_SWAP_CONTAINER = 17;
        public static final int BUTTON_SWAP_LABEL = 18;
        public static final int BUTTON_INSERT_CONTAINER = 19;
        public static final int BUTTON_INSERT_LABEL = 20;
        public static final int WITHDRAW_AS_LABEL = 21;
        public static final int BUTTON_ITEM_CONTAINER = 22;
        public static final int BUTTON_ITEM_LABEL = 23;
        public static final int BUTTON_NOTE_CONTAINER = 24;
        public static final int BUTTON_NOTE_LABEL = 25;
        public static final int BOTTOM_BAR_CONTAINER = 26;
        public static final int QUANTITY_LABEL = 27;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_1_CONTAINER = 28;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_1_LABEL = 29;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_5_CONTAINER = 30;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_5_LABEL = 31;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_10_CONTAINER = 32;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_10_LABEL = 33;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_X_CONTAINER = 34;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_X_LABEL = 35;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_ALL_CONTAINER = 36;
        public static final int BUTTON_WITHDRAWAL_QUANTITY_ALL_LABEL = 37;
        public static final int BUTTON_PLACEHOLDERS_SPRITE = 38;
        public static final int BUTTON_PLACEHOLDERS_ICON_SPRITE = 39;
        public static final int BUTTON_SEARCH_SPRITE = 40;
        public static final int BUTTON_SEARCH_ICON_SPRITE = 41;
        public static final int BUTTON_DEPOSIT_CARRIED_ITEMS_SPRITE = 42;
        public static final int BUTTON_DEPOSIT_CARRIED_ITEMS_ICON_SPRITE = 43;
        public static final int BUTTON_DEPOSIT_WORN_ITEMS_SPRITE = 44;
        public static final int BUTTON_DEPOSIT_WORN_ITEMS_ICON_SPRITE = 45;
        public static final int INCINERATOR_DYNAMIC_CONTAINER = 46;
        // 47-48
        public static final int SETTINGS_CONTAINER = 49;
        // 50 - 111
        public static final int BUTTON_SETTINGS_DYNAMIC_CONTAINER = 112; // Contains 0-1 sprites
        public static final int BUTTON_EQUIPMENT_DYNAMIC_CONTAINER = 113; // Contains 0-1 sprites
        // 114 - 115
    }

    /**
     * Describes minimap widgets
     * Last reviewed: 28/2/2022 1:57 am UTC+1
     */
    public static class Minimap {
        public static final int GROUP_INDEX = 160;
        public static final int XP_ORB_SPRITE = 1; // Interactions: "Hide", "Setup"
        public static final int HEALTH_ORB_PARENT_CONTAINER = 2;
        public static final int HEALTH_ORB_PARENT_SPRITE = 3;
        public static final int HEALTH_ORB_CONTAINER = 4; // Interactions: "Cure"
        public static final int HEALTH_ORB_LABEL = 5;
        public static final int HEALTH_ORB_SPRITE = 6;
        // 7 - 8 gap
        public static final int HEALTH_ORB_ENERGY_CONSUMED_CONTAINER = 9;
        public static final int HEALTH_ORB_ENERGY_CONSUMED_SPRITE = 10;
        public static final int HEALTH_ORB_ICON_CONTAINER = 11;
        public static final int HEALTH_ORB_ICON_SPRITE = 12;
        public static final int QUICK_PRAYER_ORB_PARENT_CONTAINER = 13;
        public static final int QUICK_PRAYER_ORB_PARENT_SPRITE = 14;
        public static final int QUICK_PRAYER_ORB_CONTAINER = 15; // Interactions: "Activate", "Setup"
        public static final int QUICK_PRAYER_ORB_LABEL = 16;
        public static final int QUICK_PRAYER_ORB_SPRITE = 17;
        public static final int QUICK_PRAYER_ORB_ENERGY_CONSUMED_CONTAINER = 18;
        public static final int QUICK_PRAYER_ORB_ENERGY_CONSUMED_SPRITE = 19;
        public static final int QUICK_PRAYER_ORB_ICON_SPRITE = 20;
        public static final int RUN_ORB_PARENT_CONTAINER = 21;
        public static final int RUN_ORB_PARENT_SPRITE = 22;
        public static final int RUN_ORB_CONTAINER = 23; // Interactions: "Toggle"
        public static final int RUN_ORB_LABEL = 24;
        public static final int RUN_ORB_SPRITE = 25;
        public static final int RUN_ORB_ENERGY_CONSUMED_CONTAINER = 26;
        public static final int RUN_ORB_ENERGY_CONSUMED_SPRITE = 27;
        public static final int RUN_ORB_ICON_SPRITE = 28;
        public static final int SPEC_ORB_PARENT_CONTAINER = 29;
        public static final int SPEC_ORB_PARENT_SPRITE = 30;
        public static final int SPEC_ORB_CONTAINER = 31; // Interactions: "Use"
        public static final int SPEC_ORB_LABEL = 32;
        public static final int SPEC_ORB_ICON_SPRITE = 33;
        public static final int SPEC_ORB_ENERGY_CONSUMED_CONTAINER = 34;
        public static final int SPEC_ORB_ENERGY_CONSUMED_SPRITE = 35;
        public static final int SPEC_ORB_RECHARGE_CONTAINER = 36; // NOTE: unclear
        public static final int SPEC_ORB_SPRITE = 37;
        public static final int BOND_ORB_PARENT_CONTAINER = 38;
        public static final int BOND_ORB_PARENT_SPRITE = 39;
        public static final int BOND_ORB_ICON_SPRITE = 40;
        public static final int BOND_ORB_CONTAINER = 41; // Interactions: "Open Store", "Bond Pouch"
        public static final int BOND_ORB_SPRITE = 42;
        public static final int WORLDMAP_ORB_CONTAINER = 43;
        public static final int WIKI_BANNER_PARENT_CONTAINER = 44;
        public static final int WIKI_BANNER_CONTAINER = 45; // Interactions: ... 10 unknown
        public static final int WIKI_BANNER_SPRITE = 46;
        public static final int WORLDMAP_ORB_PARENT_SPRITE = 47;
        public static final int WORLDMAP_ORB_SPRITE = 48; // Interactions: "Floating World Map", "Fullscreen World Map"
    }

    /**
     * Describes chatbox widgets
     * TODO: review
     */
    public static class ChatBox {
        public static final int GROUP_INDEX = 162;
        public static final int PARENT_CONTAINER = 0;
        public static final int BOTTOM_BAR_PARENT_CONTAINER = 1;
        public static final int BOTTOM_BAR_CONTAINER = 2;
        public static final int BOTTOM_BAR_SPRITE = 3;
        public static final int TAB_ALL_CONTAINER = 4;
        public static final int TAB_ALL_SPRITE = 5;
        public static final int TAB_ALL_LABEL = 6;
        public static final int TAB_GAME_CONTAINER = 7;
        public static final int TAB_GAME_SPRITE = 8;
        public static final int TAB_GAME_LABEL = 9;
        public static final int TAB_GAME_STATE_LABEL = 10;
        public static final int TAB_PUBLIC_CONTAINER = 11;
        public static final int TAB_PUBLIC_SPRITE = 12;
        public static final int TAB_PUBLIC_LABEL = 13;
        public static final int TAB_PUBLIC_STATE_LABEL = 14;
        public static final int TAB_PRIVATE_CONTAINER = 15;
        public static final int TAB_PRIVATE_SPRITE= 16;
        public static final int TAB_PRIVATE_LABEL = 17;
        public static final int TAB_PRIVATE_STATE_LABEL = 18;
        public static final int TAB_CHANNEL_CONTAINER = 19;
        public static final int TAB_CHANNEL_SPRITE = 20;
        public static final int TAB_CHANNEL_LABEL = 21;
        public static final int TAB_CHANNEL_STATE_LABEL = 22;
        public static final int TAB_CLAN_CONTAINER = 23;
        public static final int TAB_CLAN_SPRITE = 24;
        public static final int TAB_CLAN_LABEL = 25;
        public static final int TAB_CLAN_STATE_LABEL = 26;
        public static final int TAB_TRADE_CONTAINER = 27;
        public static final int TAB_TRADE_SPRITE = 28;
        public static final int TAB_TRADE_LABEL = 29;
        public static final int TAB_TRADE_STATE_LABEL = 30;
        public static final int REPORT_TAB_CONTAINER = 31;
        public static final int REPORT_TAB_LABEL = 32;
        public static final int REPORT_TAB_TEXT = 33;
        public static final int FRAME_CONTAINER = 34;
        public static final int TRANSPARENT_BACKGROUND_CONTAINER = 35;
        // 36
        public static final int CONTAINER = 37;
        // 38 - 40 gap
        public static final int TITLE = 41;
        public static final int FULL_INPUT = 42;
        // 43 - 49 gap
        public static final int GE_SEARCH_RESULTS = 50;
        // 51 - 52 gap
        public static final int MESSAGES_CONTAINER = 53;
        public static final int TRANSPARENT_BACKGROUND_LINES_CONTAINER = 54;
        public static final int INPUT_LABEL = 55;
        public static final int MESSAGE_LINES_CONTAINER = 56;
        public static final int FIRST_MESSAGE_LABEL = 57;
        public static final int LAST_MESSAGE_LABEL = 556;
        public static final int SCROLLBAR_CONTAINER = 557;
    }

    /**
     * Describes deposit box widgets
     * Last reviewed: 28/2/2022 2:51 am UTC+1
     */
    public static class DepositBox {
        public static final int GROUP_INDEX = 192;
        public static final int PARENT_CONTAINER = 0;
        public static final int DYNAMIC_CONTAINER = 1; // Contains stone border sprites 0-11
        public static final int ITEMS_DYNAMIC_CONTAINER = 2; // Contains sprites of items inside deposit box 0-27
        public static final int BOTTOM_BAR_RIGHT_SIDE_CONTAINER = 3;
        public static final int BUTTON_DEPOSIT_INVENTORY_SPRITE = 4;
        public static final int BUTTON_DEPOSIT_INVENTORY_ICON_SPRITE = 5;
        public static final int BUTTON_DEPOSIT_WORN_ITEMS_SPRITE = 6;
        public static final int BUTTON_DEPOSIT_WORN_ITEMS_ICON_SPRITE = 7;
        public static final int BUTTON_DEPOSIT_LOOT_SPRITE = 8;
        public static final int BUTTON_DEPOSIT_LOOT_ICON_SPRITE = 9;
        public static final int BOTTOM_BAR_LEFT_SIDE_CONTAINER = 10;
        public static final int BUTTON_DEFAULT_QUANTITY_1_DYNAMIC_CONTAINER = 11; // Contains button sprites 0-8
        public static final int BUTTON_DEFAULT_QUANTITY_1_LABEL = 12;
        public static final int BUTTON_DEFAULT_QUANTITY_5_DYNAMIC_CONTAINER = 13; // Contains button sprites 0-8
        public static final int BUTTON_DEFAULT_QUANTITY_5_LABEL = 14;
        public static final int BUTTON_DEFAULT_QUANTITY_10_DYNAMIC_CONTAINER = 15; // Contains button sprites 0-8
        public static final int BUTTON_DEFAULT_QUANTITY_10_LABEL = 16;
        public static final int BUTTON_DEFAULT_QUANTITY_X_DYNAMIC_CONTAINER = 17; // Contains button sprites 0-8
        public static final int BUTTON_DEFAULT_QUANTITY_X_LABEL = 18;
        public static final int BUTTON_DEFAULT_QUANTITY_ALL_DYNAMIC_CONTAINER = 19; // Contains button sprites 0-8
        public static final int BUTTON_DEFAULT_QUANTITY_ALL_LABEL = 20;
    }

    /**
     * Describes spell autocast widgets
     * Last reviewed: 3/3/2022 4:46 am UTC+1
     */
    public static class SpellAutocast {
        public static final int GROUP_INDEX = 201;
        public static final int PARENT_CONTAINER = 0;
        public static final int SPELLS_DYNAMIC_CONTAINER = 1;
        public static final int SPELL_INFO_BOX_DYNAMIC_CONTAINER = 2;
    }

    /**
     * Describes emotes tab
     * Last reviewed: 18/3/2022 4:59 am UTC+1
     */
    public static class EmotesTab {
        public static final int GROUP_INDEX = 216;
        public static final int PARENT_CONTAINER = 0;
        public static final int EMOTES_DYNAMIC_CONTAINER = 1; // 0-50 emote sprites
        public static final int SCROLLBAR_DYNAMIC_CONTAINER = 2; // 0-5 scrollbar sprites
        public static final int TOOLTIP_CONTAINER = 3;
    }

    /**
     * Describes spellbook tab widgets
     * Last reviewed: 3/3/2022 4:21 am UTC+1
     */
    public static class SpellbookTab {
        public static final int GROUP_INDEX = 218;
        public static final int PARENT_CONTAINER = 0;
        public static final int INNER_FRAME_CONTAINER = 1;
        // 2
        public static final int SPELLS_CONTAINER = 3;
        // 4
        public static final int CATHERBY_HOME_TELEPORT_SPRITE = 5; // on Leagues
        public static final int LUMBRIDGE_HOME_TELEPORT_SPRITE = 6;
        public static final int WIND_STRIKE_SPRITE = 7;
        public static final int CONFUSE_SPRITE = 8;
        public static final int ENCHANT_CROSSBOW_BOLT_SPRITE = 9;
        public static final int WATER_STRIKE_SPRITE = 10;
        public static final int LVL_1_ENCHANT_SPRITE = 11;
        public static final int EARTH_STRIKE_SPRITE = 12;
        public static final int WEAKEN_SPRITE = 13;
        public static final int FIRE_STRIKE_SPRITE = 14;
        public static final int BONES_TO_BANANAS_SPRITE = 15;
        public static final int WIND_BOLT_SPRITE = 16;
        public static final int CURSE_SPRITE = 17;
        public static final int BIND_SPRITE = 18;
        public static final int LOW_LEVEL_ALCHEMY_SPRITE = 19;
        public static final int WATER_BOLT_SPRITE = 20;
        public static final int VARROCK_TELEPORT_SPRITE = 21;
        public static final int LVL_2_ENCHANT_SPRITE = 22;
        public static final int EARTH_BOLT_SPRITE = 23;
        public static final int LUMBRIDGE_TELEPORT_SPRITE = 24;
        public static final int TELEKINETIC_GRAB_SPRITE = 25;
        public static final int FIRE_BOLT_SPRITE = 26;
        public static final int FALADOR_TELEPORT_SPRITE = 27;
        public static final int CRUMBLE_UNDEAD_SPRITE = 28;
        public static final int TELEPORT_TO_HOUSE_SPRITE = 29;
        public static final int WIND_BLAST_SPRITE = 30;
        public static final int SUPERHEAT_ITEM_SPRITE = 31;
        public static final int CAMELOT_TELEPORT_SPRITE = 32;
        public static final int WATER_BLAST_SPRITE = 33;
        public static final int LVL_3_ENCHANT_SPRITE = 34;
        public static final int IBAN_BLAST_SPRITE = 35;
        public static final int SNARE_SPRITE = 36;
        public static final int MAGIC_DART_SPRITE = 37;
        public static final int ARDOUGNE_TELEPORT_SPRITE = 38;
        public static final int EARTH_BLAST_SPRITE = 39;
        public static final int HIGH_LEVEL_ALCHEMY_SPRITE = 40;
        public static final int CHARGE_WATER_ORB_SPRITE = 41;
        public static final int LVL_4_ENCHANT_SPRITE = 42;
        public static final int WATCHTOWER_TELEPORT_SPRITE = 43;
        public static final int FIRE_BLAST_SPRITE = 44;
        public static final int CHARGE_EARTH_ORB_SPRITE = 45;
        public static final int BONES_TO_PEACHES_SPRITE = 46;
        public static final int SARADOMIN_STRIKE_SPRITE = 47;
        public static final int CLAWS_OF_GUTHIX_SPRITE = 48;
        public static final int FLAMES_OF_ZAMORAK_SPRITE = 49;
        public static final int TROLLHEIM_TELEPORT_SPRITE = 50;
        public static final int WIND_WAVE_SPRITE = 51;
        public static final int CHARGE_FIRE_ORB_SPRITE = 52;
        public static final int APE_ATOLL_TELEPORT_SPRITE = 53;
        public static final int WATER_WAVE_SPRITE = 54;
        public static final int CHARGE_AIR_ORB_SPRITE = 55;
        public static final int VULNERABILITY_SPRITE = 56;
        public static final int LVL_5_ENCHANT_SPRITE = 57;
        public static final int KOUREND_CASTLE_TELEPORT_SPRITE = 58;
        public static final int EARTH_WAVE_SPRITE = 59;
        public static final int ENFEEBLE_SPRITE = 60;
        public static final int TELEOTHER_LUMBRIDGE_SPRITE = 61;
        public static final int FIRE_WAVE_SPRITE = 62;
        public static final int ENTANGLE_SPRITE = 63;
        public static final int STUN_SPRITE = 64;
        public static final int CHARGE_SPRITE = 65;
        public static final int WIND_SURGE_SPRITE = 66;
        public static final int TELEOTHER_FALADOR_SPRITE = 67;
        public static final int WATER_SURGE_SPRITE = 68;
        public static final int TELE_BLOCK_SPRITE = 69;
        public static final int TELEPORT_TO_TARGET_SPRITE = 70;
        public static final int LVL_6_ENCHANT_SPRITE = 71;
        public static final int TELEOTHER_CAMELOT_SPRITE = 72;
        public static final int EARTH_SURGE_SPRITE = 73;
        public static final int LVL_7_ENCHANT_SPRITE = 74;
        public static final int FIRE_SURGE_SPRITE = 75;
        // Ancient
        public static final int ICE_RUSH_SPRITE = 76;
        public static final int ICE_BLITZ_SPRITE = 77;
        public static final int ICE_BURST_SPRITE = 78;
        public static final int ICE_BARRAGE_SPRITE = 79;
        public static final int BLOOD_RUSH_SPRITE = 80;
        public static final int BLOOD_BLITZ_SPRITE = 81;
        public static final int BLOOD_BURST_SPRITE = 82;
        public static final int BLOOD_BARRAGE_SPRITE = 83;
        public static final int SMOKE_RUSH_SPRITE = 84;
        public static final int SMOKE_BLITZ_SPRITE = 85;
        public static final int SMOKE_BURST_SPRITE = 86;
        public static final int SMOKE_BARRAGE_SPRITE = 87;
        public static final int SHADOW_RUSH_SPRITE = 88;
        public static final int SHADOW_BLITZ_SPRITE = 89;
        public static final int SHADOW_BURST_SPRITE = 90;
        public static final int SHADOW_BARRAGE_SPRITE = 91;
        public static final int PADDEWWA_TELEPORT_SPRITE = 92;
        public static final int SENNTISTEN_TELEPORT_SPRITE = 93;
        public static final int KHARYRLL_TELEPORT_SPRITE = 94;
        public static final int LASSAR_TELEPORT_SPRITE = 95;
        public static final int DAREEYAK_TELEPORT_SPRITE = 96;
        public static final int CARRALLANGAR_TELEPORT_SPRITE = 97;
        public static final int ANNAKARL_TELEPORT_SPRITE = 98;
        public static final int GHORROCK_TELEPORT_SPRITE = 99;
        public static final int EDGEVILLE_HOME_TELEPORT_SPRITE = 100;
        // Lunar
        public static final int LUNAR_HOME_TELEPORT_SPRITE = 101;
        public static final int BAKE_PIE_SPRITE = 102;
        public static final int CURE_PLANT_SPRITE = 103;
        public static final int MONSTER_EXAMINE_SPRITE = 104;
        public static final int NPC_CONTACT_SPRITE = 105;
        public static final int CURE_OTHER_SPRITE = 106;
        public static final int HUMIDIFY_SPRITE = 107;
        public static final int MOONCLAN_TELEPORT_SPRITE = 108;
        public static final int TELE_GROUP_MOONCLAN_SPRITE = 109;
        public static final int CURE_ME_SPRITE = 110;
        public static final int HUNTER_KIT_SPRITE = 111;
        public static final int WATERBIRTH_TELEPORT_SPRITE = 112;
        public static final int TELE_GROUP_WATERBIRTH_SPRITE = 113;
        public static final int CURE_GROUP_SPRITE = 114;
        public static final int STAT_SPY_SPRITE = 115;
        public static final int BARBARIAN_TELEPORT_SPRITE = 116;
        public static final int TELE_GROUP_BARBARIAN_SPRITE = 117;
        public static final int SUPERGLASS_MAKE_SPRITE = 118;
        public static final int TAN_LEATHER_SPRITE = 119;
        public static final int KHAZARD_TELEPORT_SPRITE = 120;
        public static final int TELE_GROUP_KHAZARD_SPRITE = 121;
        public static final int DREAM_SPRITE = 122;
        public static final int STRING_JEWELLERY_SPRITE = 123;
        public static final int STAT_RESTORE_POT_SHARE_SPRITE = 124;
        public static final int MAGIC_IMBUE_SPRITE = 125;
        public static final int FERTILE_SOIL_SPRITE = 126;
        public static final int BOOST_POTION_SHARE_SPRITE = 127;
        public static final int FISHING_GUILD_TELEPORT_SPRITE = 128;
        public static final int TELE_GROUP_FISHING_GUILD_SPRITE = 129;
        public static final int PLANK_MAKE_SPRITE = 130;
        public static final int CATHERBY_TELEPORT_SPRITE = 131;
        public static final int TELE_GROUP_CATHERBY_SPRITE = 132;
        public static final int RECHARGE_DRAGONSTONE_SPRITE = 133;
        public static final int ICE_PLATEAU_TELEPORT_SPRITE = 134;
        public static final int TELE_GROUP_ICE_PLATEAU_SPRITE = 135;
        public static final int ENERGY_TRANSFER_SPRITE = 136;
        public static final int HEAL_OTHER_SPRITE = 137;
        public static final int VENGEANCE_OTHER_SPRITE = 138;
        public static final int VENGEANCE_SPRITE = 139;
        public static final int HEAL_GROUP_SPRITE = 140;
        public static final int SPELLBOOK_SWAP_SPRITE = 141;
        public static final int GEOMANCY_SPRITE = 142;
        public static final int SPIN_FLAX_SPRITE = 143;
        public static final int OURANIA_TELEPORT_SPRITE = 144;
        //  Arceeus book
        public static final int ARCEEUS_HOME_TELEPORT_SPRITE = 145;
        public static final int BASIC_REANIMATION = 146;
        // TODO: arceeus spells 147 - 188
        public static final int RESSURECT_GREATER_ZOMBIE = 189;
        // 190 - 191
        public static final int SPELL_FILTERS_DYNAMIC_CONTAINER = 192;
        public static final int SPELL_FILTERS_FRAME_SPRITE = 193;
        public static final int SPELL_FILTERS_TITLE_LABEL = 194;
        public static final int SPELL_FILTERS_INNER_MSG_DYNAMIC_CONTAINER = 195;
        public static final int BOTTOM_BAR_CONTAINER = 196;
        // 197
        public static final int FILTERS_DYNAMIC_CONTAINER = 198; // Contains 0-8 button sprites 9 button label
    }

    /**
     * Describes quest list sub tab widgets
     * Last reviewed: 18/3/2022 4:31 am UTC+1
     */
    public static class MusicPlayerTab {
        public static final int GROUP_INDEX = 239;
        public static final int PARENT_DYNAMIC_CONTAINER = 0; //0-1 toggle all sprite, search sprite
        public static final int INNER_FRAME_DYNAMIC_CONTAINER = 1;
        public static final int INNER_FRAME_BOX = 2;
        public static final int MUSIC_TRACK_LIST_DYNAMIC_CONTAINER = 3; //0-679, music labels
        public static final int SCROLLBAR_DYNAMIC_CONTAINER = 4; //0-5, scrollbar sprites
        public static final int PLAYING_TEXT_LABEL = 5;
        public static final int CURRENT_TRACK_LABEL = 6;
        public static final int AUTO_BUTTON_SPRITE = 7;
        public static final int AUTO_BUTTON_LABEL = 8;
        public static final int MANUAL_BUTTON_SPRITE = 9;
        public static final int MANUAL_BUTTON_LABEL = 10;
        public static final int LOOP_BUTTON_SPRITE = 11;
        public static final int LOOP_BUTTON_LABEL = 12;
        public static final int UNLOCKED_TRACKS_COUNT = 13;
    }

    /**
     * Describes store widgets
     * Last reviewed: 2/3/2022 1:47 am UTC+1
     */
    public static class Store {
        public static final int GROUP_INDEX = 300;
        public static final int PARENT_CONTAINER = 0;
        public static final int DYNAMIC_CONTAINER = 1; // Contains stone border sprites 0-11
        public static final int ITEMS_DYNAMIC_CONTAINER = 16; // Contains items sold sprites 0-16
        public static final int BOTTOM_TEXT_LABEL = 18;
    }

    /**
     * Describes quest list sub tab widgets
     * Last reviewed: 18/3/2022 4:02 am UTC+1
     */
    public static class Smithing {
        public static final int GROUP_INDEX = 312;
        public static final int PARENT_CONTAINER = 0;
        public static final int DYNAMIC_CONTAINER = 1; // borders sprites, title 1, close button 11
        public static final int BUTTONS_CONTAINER = 2;
        public static final int BUTTON_ONE_DYNAMIC_CONTAINER = 3;
        public static final int BUTTON_X_DYNAMIC_CONTAINER = 6;
        public static final int BUTTON_ALL_DYNAMIC_CONTAINER = 7;
        public static final int ITEM_ONE_DYNAMIC_CONTAINER = 9;
        public static final int ITEM_TWO_DYNAMIC_CONTAINER = 10;
        public static final int ITEM_THREE_DYNAMIC_CONTAINER = 11;
        public static final int ITEM_FOUR_DYNAMIC_CONTAINER = 12;
        public static final int ITEM_FIFE_DYNAMIC_CONTAINER = 13;
        public static final int ITEM_SIX_DYNAMIC_CONTAINER = 14;
        public static final int ITEM_SEVEN_DYNAMIC_CONTAINER = 15;
        public static final int ITEM_EIGHT_DYNAMIC_CONTAINER = 16;
        public static final int ITEM_NINE_DYNAMIC_CONTAINER = 17;
        public static final int ITEM_TEN_DYNAMIC_CONTAINER = 18;
        public static final int ITEM_ELEVEN_DYNAMIC_CONTAINER = 19;
        public static final int ITEM_TWELVE_DYNAMIC_CONTAINER = 20;
        public static final int ITEM_THIRTEEN_DYNAMIC_CONTAINER = 21;
        public static final int ITEM_FOURTEEN_DYNAMIC_CONTAINER = 22;
        public static final int ITEM_FIFTEEN_DYNAMIC_CONTAINER = 23;
        public static final int ITEM_SIXTEEN_DYNAMIC_CONTAINER = 24;
        public static final int ITEM_SEVENTEEN_DYNAMIC_CONTAINER = 25;
        public static final int ITEM_EIGHTEEN_DYNAMIC_CONTAINER = 26;
        public static final int ITEM_NINETEEN_DYNAMIC_CONTAINER = 27;
        public static final int ITEM_TWENTIETH_DYNAMIC_CONTAINER = 28;
        public static final int ITEM_TWENTYFIRST_DYNAMIC_CONTAINER = 29;
        public static final int ITEM_TWENTYSECOND_DYNAMIC_CONTAINER = 30;
        public static final int ITEM_TWENTYTHIRD_DYNAMIC_CONTAINER = 31;
        public static final int ITEM_TWENTYFOURTH_DYNAMIC_CONTAINER = 32;
    }

    /**
     * Describes skill tab widgets
     * Last reviewed: 28/2/2022 2:18 am UTC+1
     */
    public static class SkillsTab {
        public static final int GROUP_INDEX = 320;
        public static final int CONTAINER = 0;
        public static final int ATTACK_CONTAINER = 1;
        public static final int STRENGTH_CONTAINER = 2;
        public static final int DEFENCE_CONTAINER = 3;
        public static final int RANGED_CONTAINER = 4;
        public static final int PRAYER_CONTAINER = 5;
        public static final int MAGIC_CONTAINER = 6;
        public static final int RUNECRAFT_CONTAINER = 7;
        public static final int CONSTRUCTION_CONTAINER = 8;
        public static final int HITPOINTS_CONTAINER = 9;
        public static final int AGILITY_CONTAINER = 10;
        public static final int HERBLORE_CONTAINER = 11;
        public static final int THIEVING_CONTAINER = 12;
        public static final int CRAFTING_CONTAINER = 13;
        public static final int FLETCHING_CONTAINER = 14;
        public static final int SLAYER_CONTAINER = 15;
        public static final int HUNTER_CONTAINER = 16;
        public static final int MINING_CONTAINER = 17;
        public static final int SMITHING_CONTAINER = 18;
        public static final int FISHING_CONTAINER = 19;
        public static final int COOKING_CONTAINER = 20;
        public static final int FIREMAKING_CONTAINER = 21;
        public static final int WOODCUTTING_CONTAINER = 22;
        public static final int FARMING_CONTAINER = 23;
        public static final int TOTAL_LEVEL_CONTAINER = 24;
        public static final int TOTAL_LEVEL_LEFT_SIDE_SPRITE = 25;
        public static final int TOTAL_LEVEL_RIGHT_SIDE_SPRITE = 26;
        public static final int TOTAL_LEVEL_LABEL = 27;
        public static final int MOUSEOVER_TOOLTIP_CONTAINER = 28;
    }

    /**
     * Describe second trade screen widgets
     * Last reviewed: 5/3/2022 1:29 am UTC+1
     */
    public static class TradeSecondScreen {
        public static final int GROUP_INDEX = 334;
        public static final int TRADE_WINDOW_SHADOW_BOX = 0;
        public static final int PARENT_CONTAINER = 1;
        public static final int TRADE_WINDOW_DYNAMIC_CONTAINER = 2; //0-7 steel border sprites
        public static final int TRADE_WINDOW_SPRITE = 3;
        public static final int TRADE_WINDOW_TITLE_FIRST_ROW = 4;
        public static final int TRADE_WINDOW_TITLE_SECOND_ROW = 5;
        // 6-12 steel border sprites
        public static final int BUTTON_ACCEPT_SHADOW_BOX = 13;
        public static final int BUTTON_DECLINE_SHADOW_BOX = 14;
        // 15-22 steel border sprites
        public static final int MY_OFFER_VALUE_LABEL = 23;
        public static final int PARTNER_OFFER_VALUE_LABEL = 24;
        public static final int BUTTON_ACCEPT_LABEL = 25;
        public static final int BUTTON_DECLINE_LABEL = 26;
        public static final int PARTNER_UNKNOWN_CONTAINER = 27;
        public static final int MY_OFFER_ITEM_LIST_DYNAMIC_CONTAINER = 28;
        public static final int MY_PARTNER_OFFER_ITEM_LIST_DYNAMIC_CONTAINER = 29;
        public static final int NAME_OF_TRADE_PARTNER_LABEL = 30;
        // 31
        public static final int CLOSE_BUTTON_SPRITE = 32;
    }

    /**
     * Describes first trade screen widgets
     * Last reviewed: 5/3/2022 1:07 am UTC+1
     */
    public static class TradeFirstScreen {
        public static final int GROUP_INDEX = 335;
        public static final int PARENT_CONTAINER = 0;
        public static final int TRADE_WINDOW_SHADOW_BOX = 1;
        public static final int TRADE_WINDOW_CONTAINER = 2;
        public static final int TRADE_WINDOW_DYNAMIC_CONTAINER = 3;
        // 4-6 gap
        public final static int BUTTON_FREE_INV_SLOTS_DYNAMIC_CONTAINER = 7; // Contains 0-7 steel border sprites
        public final static int BUTTON_FREE_INV_SLOTS_BOX = 8;
        public final static int BUTTON_FREE_INV_SLOTS_LABEL = 9;
        public static final int BUTTON_ACCEPT_DYNAMIC_CONTAINER = 10; // Contains 0-7 steel border sprites
        public static final int BUTTON_ACCEPT_SHADOW_BOX = 11;
        public static final int BUTTON_ACCEPT_LABEL = 12;
        public static final int BUTTON_DECLINE_DYNAMIC_CONTAINER = 13; // Contains 0-7 steel border sprites
        public static final int BUTTON_DECLINE_SHADOW_BOX = 14;
        public static final int BUTTON_DECLINE_LABEL = 15;
        // 16-23 trade window steel border sprites
        public static final int MY_OFFER_VALUE_LABEL = 24;
        public static final int MY_OFFER_DYNAMIC_CONTAINER = 25;
        public static final int MY_OFFER_TRADE_STATUS_LABEL = 26;
        public static final int PARTNER_OFFER_VALUE_LABEL = 27;
        public static final int PARTNER_OFFER_DYNAMIC_CONTAINER = 28;
        public static final int PARTNER_OFFER_TRADE_STATUS_LABEL = 29;
        public static final int TRADE_STATUS_LABEL = 30;
        public static final int NAME_OF_TRADE_PARTNER_LABEL = 31;
    }

    /**
     * Describes click to play screen widgets
     * Last reviewed: 2/3/2022 2:28 am UTC+1
     */
    public static class ClickToPlayScreen {
        public static final int GROUP_INDEX = 378;
        public static final int PARENT_FIRST_CONTAINER = 0;
        public static final int PARENT_SECOND_CONTAINER = 1;
        public static final int INNER_FRAME_CONTAINER = 2;
        public static final int UNKNOWN_CONTAINER = 3;
        public static final int MOTW_CONTAINER = 4;
        public static final int UNKNOWN_MODEL = 5;
        public static final int MOTW_TITLE_LABEL = 6;
        public static final int MOTW_TEXT_LABEL = 7;
        // 8 - 55 gap
        public static final int MOTW_LEFT_SIDE_MODEL = 56;
        public static final int MOTW_RIGHT_SIDE_MODEL = 57;
        public static final int ABOVE_MOTW_CONTAINER = 58;
        public static final int SWITCH_STYLE_DYNAMIC_CONTAINER = 59; // Contains label[0]
        public static final int WELCOME_MSG_CONTAINER = 60;
        public static final int WELCOME_MSG_SPRITE = 61;
        public static final int WELCOME_MSG_TITLE_LABEL = 62;
        public static final int WELCOME_MSG_TEXT_LABEL = 63;
        // 64 - 73 gap
        public static final int BUTTON_MEMBERSHIP_SPRITE = 74;
        // 75
        public static final int BUTTON_MEMBERSHIP_ICON_SPRITE = 76;
        public static final int BUTTON_MEMBERSHIP_TEXT_LABEL = 77;
        public static final int BUTTON_CLICK_HERE_TO_PLAY_CONTAINER = 78;
        public static final int BUTTON_MEMBERSHIP_CONTAINER = 79;
        public static final int BUTTON_CLICK_HERE_TO_PLAY_SPRITE = 80;
        public static final int BUTTON_BANK_PIN_CONTAINER = 81;
        // 82
        public static final int BUTTON_UNREAD_MSG_CONTAINER = 83;
        public static final int BUTTON_CLICK_HERE_TO_PLAY_ICON_SPRITE = 84;
        public static final int BUTTON_PASSWORD_CONTAINER = 85;
        public static final int BANNER_CONTAINER = 86;
        public static final int BUTTON_CLICK_HERE_TO_PLAY_LABEL = 87;
    }

    /**
     * Describes worn equipment tab widgets
     * Last reviewed: 1/3/2022 1:21 am UTC+1
     */
    public static class WornEquipmentTab {
        public static final int GROUP_INDEX = 387;
        public static final int PARENT_CONTAINER = 0;
        public static final int EQUIPMENT_STATS_DYNAMIC_CONTAINER = 1;     // Contains button sprites 0-8 Interactions: View equipment stats
        public static final int EQUIPMENT_STATS_ICON_SPRITE = 2;
        public static final int GUIDE_PRICES_DYNAMIC_CONTAINER = 3;        // Contains button sprites 0-8 Interactions: View guide prices
        public static final int GUIDE_PRICES_ICON_SPRITE = 4;
        public static final int ITEMS_KEPT_ON_DEATH_DYNAMIC_CONTAINER = 5; // Contains button sprites 0-8 Interactions  View items kept on death
        public static final int ITEMS_KEPT_ON_DEATH_SPRITE = 6;
        public static final int CALL_FOLLOWER_DYNAMIC_CONTAINER = 7;       // Contains button sprites 0-8 Interactions  Call follower
        public static final int CALL_FOLLOWER_SPRITE = 8;
        public static final int MIDDLE_STRAIGHT_LINE_SPRITE = 9;
        public static final int LEFT_STRAIGHT_LINE_SPRITE = 10;
        public static final int RIGHT_STRAIGHT_LINE_SPRITE = 11;
        public static final int MIDDLE_HORIZONTAL_STRAIGHT_LINE_SPRITE = 12;
        public static final int TOP_HORIZONTAL_STRAIGHT_LINE_SPRITE = 13;
        // 14
        public static final int HEAD_DYNAMIC_CONTAINER = 15;  // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int BACK_DYNAMIC_CONTAINER = 16;  // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int NECK_DYNAMIC_CONTAINER = 17; // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int WEAPON_DYNAMIC_CONTAINER = 18; // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int CHEST_DYNAMIC_CONTAINER = 19; // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int SHIELD_DYNAMIC_CONTAINER = 20; // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int LEGS_DYNAMIC_CONTAINER = 21; // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int HANDS_DYNAMIC_CONTAINER = 22; // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int FEET_DYNAMIC_CONTAINER = 23; // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int RING_DYNAMIC_CONTAINER = 24; // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int AMMUNITION_DYNAMIC_CONTAINER = 25; // Contains slot[0] sprite and item[1] sprite which has itemID
        public static final int UNKNOWN1_CONTAINER = 26;
        public static final int UNKNOWN2_CONTAINER = 27;
    }

    /**
     * Describes quest list sub tab widgets
     * Last reviewed: 18/3/2022 3:42 am UTC+1
     */
    public static class QuestListSubTab {
        public static final int GROUP_INDEX = 399;
        public static final int PARENT_CONTAINER = 0;
        public static final int QUEST_LIST_TITLE_LABEL = 1;
        public static final int DYNAMIC_CONTAINER = 2;
        public static final int QUEST_LIST_LIST = 3;
        public static final int SCROLLBAR_DYNAMIC_CONTAINER = 4;
        public static final int QUEST_LIST_CONTAINER = 5;
        public static final int FREE_QUESTS_DYNAMIC_CONTAINER = 6; // labels
        public static final int MEMBER_QUESTS_DYNAMIC_CONTAINER = 7; // labels
        public static final int MINIQUESTS_DYNAMIC_CONTAINER = 8; // labels
    }

    /**
     * Describes collection box widgets
     * Last reviewed: 18/3/2022 3:10 am UTC+1
     */
    public static class CollectionBox {
        public static final int GROUP_INDEX = 402;
        public static final int PARENT_CONTAINER = 0;
        public static final int CONTAINER = 1;
        public static final int DYNAMIC_CONTAINER = 2; // stone borders, title, close
        public static final int BUTTON_INVENTORY_DYNAMIC_CONTAINER = 3; // sprite, text
        public static final int BUTTON_BANK_DYNAMIC_CONTAINER = 4; // sprite, text
        public static final int SLOT_FIRST_DYNAMIC_CONTAINER = 5; // sprites, layers, steel border 0-20
        public static final int SLOT_SECOND_DYNAMIC_CONTAINER = 6; // sprites, layers, steel border 0-20
        public static final int SLOT_THIRD_DYNAMIC_CONTAINER = 7; // sprites, layers, steel border 0-20
        public static final int SLOT_FOURTH_DYNAMIC_CONTAINER = 8; // sprites, layers, steel border 0-20
        public static final int SLOT_FIFTH_DYNAMIC_CONTAINER = 9; // sprites, layers, steel border 0-20
        public static final int SLOT_SIXTH_DYNAMIC_CONTAINER = 10; // sprites, layers, steel border 0-20
        public static final int SLOT_SEVENTH_DYNAMIC_CONTAINER = 11; // sprites, layers, steel border 0-20
        public static final int SLOT_EIGHT_DYNAMIC_CONTAINER = 12; // sprites, layers, steel border 0-20
        public static final int UNKNOWN_CONTAINER = 13;
    }

    /**
     * Describes grand exchange widgets
     * Last reviewed: 18/3/2022 2:46 am UTC+1
     */
    public static class FriendlistTab {
        public static final int GROUP_INDEX = 429;
        public static final int PARENT_CONTAINER = 0;
        public static final int VIEW_IGNORE_LIST_CONTAINER = 1;
        public static final int VIEW_IGNORE_LIST_SPRITE = 2;
        public static final int TITLE_LABEL = 3;
        public static final int DYNAMIC_CONTAINER = 4; // Contains dynamic components [0-7]
        // 5 - 12 gap
        public static final int ADD_FRIEND_TEXT_LABEL = 13;
        public static final int ADD_FRIEND_SPRITE = 14;
        public static final int ADD_FRIEND_LABEL = 15;
        public static final int DEL_FRIEND_SPRITE = 16;
        public static final int DEL_FRIEND_LABEL = 17;
        public static final int PREVIOUS_NAME_CONTAINER = 18;
    }

    /**
     * Describes grand exchange widgets
     * Last reviewed: 3/3/2022 0:14 am UTC+1
     */
    public static class GrandExchange {
        public static final int GROUP_INDEX = 465;
        public static final int PARENT_CONTAINER = 0;
        public static final int CONTAINER = 1;
        public static final int DYNAMIC_CONTAINER = 2; // Contains stone borders close button 0-11
        public static final int BUTTON_HISTORY_DYNAMIC_CONTAINER = 3; // Contains sprites, label 0-9
        public static final int OFFER_STATUS_BUTTON_BACK_SPRITE = 4;
        public static final int INNER_FRAME_CONTAINER = 5;
        public static final int TITLE_DYNAMIC_CONTAINER = 6; // Contains label[0]
        public static final int FIRST_SLOT_DYNAMIC_CONTAINER = 7; // Contains 0-4 icon layers, 5-15 steel borders, 16 title label, 26 left icon sprite, 27 right icon sprite
        public static final int SECOND_SLOT_DYNAMIC_CONTAINER = 8; // Contains 0-4 icon layers, 5-15 steel borders, 16 title label, 26 left icon sprite, 27 right icon sprite
        public static final int THIRD_SLOT_DYNAMIC_CONTAINER = 9; // Contains 0-4 icon layers, 5-15 steel borders, 16 title label, 26 left icon sprite, 27 right icon sprite
        public static final int FOURTH_SLOT_DYNAMIC_CONTAINER = 10; // Contains 0-4 icon layers, 5-15 steel borders, 16 title label, 26 left icon sprite, 27 right icon sprite
        public static final int FIFTH_SLOT_DYNAMIC_CONTAINER = 11; // Contains 0-4 icon layers, 5-15 steel borders, 16 title label, 26 left icon sprite, 27 right icon sprite
        public static final int SIXTH_SLOT_DYNAMIC_CONTAINER = 12; // Contains 0-4 icon layers, 5-15 steel borders, 16 title label, 26 left icon sprite, 27 right icon sprite
        public static final int SEVENTH_SLOT_DYNAMIC_CONTAINER = 13; // Contains 0-4 icon layers, 5-15 steel borders, 16 title label, 26 left icon sprite, 27 right icon sprite
        public static final int EIGHT_SLOT_DYNAMIC_CONTAINER = 14; // Contains 0-4 icon layers, 5-15 steel borders, 16 title label, 26 left icon sprite, 27 right icon sprite
        public static final int OFFER_STATUS_DYNAMIC_CONTAINER = 15;
        public static final int OFFER_STATUS_ITEM_DESCRIPTION_LABEL = 16;
        public static final int OFFER_STATUS_ABOVE_PRICE_TEXT_LABEL = 17;
        public static final int OFFER_STATUS_INFO_ICON_DYNAMIC_CONTAINER = 18; // 0 icon sprite
        public static final int OFFER_STATUS_COLLECTION_AREA_SPRITE = 19;
        public static final int OFFER_STATUS_STEEL_BORDER_SEPARATOR_LINE = 20;
        public static final int OFFER_STATUS_STEEL_BORDER_VERTICAL_SEPARATOR_LINE = 21;
        public static final int UNKNOWN_SPRITE = 22;
        public static final int OFFER_STATUS_BOTTOM_BAR_DYNAMIC_CONTAINER = 23; // 0 cancel button sprite, 1 sold text
        public static final int OFFER_STATUS_COLLECTION_AREA_DYNAMIC_CONTAINER = 24; // 0 bg sprite left item, 1 bg sprite right item, 2 item sprite, 3 item sprite
    }

    /**
     * Describes prayers tab widgets
     * Last reviewed: 4/3/2022 0:34 am UTC+1
     */
    public static class PrayersTab {
        public static final int GROUP_ID = 541;
        public static final int PARENT_CONTAINER = 0;
        // 1
        public static final int BOTTOM_BAR_DYNAMIC_CONTAINER = 2;
        // 3
        public static final int PRAYERS_CONTAINER = 4;
        public static final int THICK_SKIN = 5;
        public static final int BURST_OF_STRENGTH = 6;
        public static final int CLARITY_OF_THOUGHT = 7;
        public static final int ROCK_SKIN = 8;
        public static final int SUPERHUMAN_STRENGTH = 9;
        public static final int IMPROVED_REFLEXES = 10;
        public static final int RAPID_RESTORE = 11;
        public static final int RAPID_HEAL = 12;
        public static final int PROTECT_ITEM = 13;
        public static final int STEEL_SKIN = 14;
        public static final int ULTIMATE_STRENGTH = 15;
        public static final int INCREDIBLE_REFLEXES = 16;
        public static final int PROTECT_FROM_MAGIC = 17;
        public static final int PROTECT_FROM_MISSILES = 18;
        public static final int PROTECT_FROM_MELEE = 19;
        public static final int RETRIBUTION = 20;
        public static final int REDEMPTION = 21;
        public static final int SMITE = 22;
        public static final int SHARP_EYE = 23;
        public static final int MYSTIC_WILL = 24;
        public static final int HAWK_EYE = 25;
        public static final int MYSTIC_LORE = 26;
        public static final int EAGLE_EYE = 27;
        public static final int MYSTIC_MIGHT = 28;
        public static final int CHIVALRY = 29;
        public static final int PIETY = 30;
        public static final int RIGOUR = 31;
        public static final int AUGURY = 32;
        public static final int PRESERVE = 33;
        public static final int MOUSEOVER_TOOLTIP_CONTAINER = 34;
    }

    /**
     * Describes combat options tab widgets
     * Last reviewed: 1/3/2022 0:31 am UTC+1
     */
    public static class CombatOptionsTab {
        public static final int GROUP_INDEX = 593;
        public static final int PARENT_CONTAINER = 0;
        public static final int WEAPON_NAME_LABEL = 1;
        public static final int WEAPON_CATEGORY_LABEL = 2;
        public static final int COMBAT_LEVEL_LABEL = 3;
        public static final int WEAPON_STYLE_ONE_CONTAINER = 4;
        public static final int WEAPON_STYLE_ONE_DYNAMIC_CONTAINER = 5; // Contains button sprites 0-8
        public static final int WEAPON_STYLE_ONE_SPRITE = 6;
        public static final int WEAPON_STYLE_ONE_LABEL = 7;
        public static final int WEAPON_STYLE_TWO_CONTAINER = 8;
        public static final int WEAPON_STYLE_TWO_DYNAMIC_CONTAINER = 9;
        public static final int WEAPON_STYLE_TWO_SPRITE = 10;
        public static final int WEAPON_STYLE_TWO_LABEL = 11;
        public static final int WEAPON_STYLE_THREE_CONTAINER = 12;
        public static final int WEAPON_STYLE_THREE_DYNAMIC_CONTAINER = 13;
        public static final int WEAPON_STYLE_THREE_SPRITE = 14;
        public static final int WEAPON_STYLE_THREE_LABEL = 15;
        public static final int WEAPON_STYLE_FOUR_CONTAINER = 16;
        public static final int WEAPON_STYLE_FOUR_DYNAMIC_CONTAINER = 17;
        public static final int WEAPON_STYLE_FOUR_SPRITE = 18;
        public static final int WEAPON_STYLE_FOUR_LABEL = 19;
        public static final int AUTO_CAST_SPELL_PARENT_CONTAINER = 20;
        public static final int AUTO_CAST_DEFENSIVE_SPELL_DYNAMIC_CONTAINER = 21; // Contains button sprites 0-8
        public static final int AUTO_CAST_DEFENSIVE_SPELL_CONTAINER = 22;
        public static final int AUTO_CAST_DEFENSIVE_SPELL_ICON_SPRITE = 23;
        public static final int AUTO_CAST_DEFENSIVE_SPELL_SHIELD_SPRITE = 24;
        public static final int AUTO_CAST_DEFENSIVE_SPELL_LABEL = 25;
        public static final int AUTO_CAST_SPELL_DYNAMIC_CONTAINER = 26;  // Contains button sprites 0-8
        public static final int AUTO_CAST_SPELL_CONTAINER = 27;
        public static final int AUTO_CAST_SPELL_ICON_SPRITE = 28;
        public static final int AUTO_CAST_SPELL_LABEL = 29;
        public static final int AUTO_RETALIATE_CONTAINER = 30;
        public static final int AUTO_RETALIATE_ICON_LABEL = 31;
        public static final int AUTO_RETALIATE_DYNAMIC_CONTAINER = 32;  // Contains button sprites 0-8
        public static final int AUTO_RETALIATE_SPRITE = 33;
        public static final int AUTO_RETALIATE_LABEL = 34;
        public static final int SPECIAL_ATTACK_CONTAINER = 35;
        public static final int SPECIAL_ATTACK_DYNAMIC_CONTAINER = 36;  // Contains button sprites 0-8
        public static final int SPECIAL_ATTACK_BAR = 37;
        public static final int SPECIAL_ATTACK_GREEN_BAR_CONTAINER = 38;
        public static final int SPECIAL_ATTACK_GREEN_BAR_GREEN_BOX = 39;
        public static final int SPECIAL_ATTACK_LABEL = 40;
        public static final int SPECIAL_ATTACK_BROWN_GREEN_BOX = 41;
        public static final int MOUSEOVER_TOOLTIP_CONTAINER = 42;
    }

    /**
     * Describes world map widget
     * Last reviewed: 18/3/2022 0:04 am UTC+1
     */
    public static class WorldMap {
        public static final int GROUP_INDEX = 595;
        public static final int PARENT_CONTAINER = 0;
        public static final int MAIN_CONTAINER = 1;
        public static final int DYNAMIC_CONTAINER = 2; // 0-7 borders,corners sprites
        // 3
        public static final int UNKNOWN_CONTAINER = 4;
        public static final int MAIN_FRAME_CONTAINER = 5;
        public static final int MAPVIEW_BOX = 6;
        public static final int MAPVIEW_CONTAINER = 7;
        public static final int CONTAINER = 8; // Content Type 1400
        public static final int MAPVIEW_DYNAMIC_CONTAINER = 9; // 1 your position icon
        public static final int SIDEBAR_DYNAMIC_CONTAINER = 14;
        public static final int KEY_DYNAMIC_CONTAINER = 16;
        public static final int LEGENDS_DYNAMIC_CONTAINER = 21;
        public static final int BOTTOM_BAR_CONTAINER = 22;
        public static final int BOTTOM_BAR_SPRITE = 23;
        public static final int HIDE_MENU_DYNAMIC_SPRITE = 24;
        public static final int SEARCH_INPUT_BOX_DYNAMIC_SPRITE = 25;
        public static final int SURFACE_DYNAMIC_CONTAINER = 26;
        public static final int BUTTON_CLOSE_SPRITE = 38;
        public static final int RESIZE_CORNER_CONTAINER = 39;
        public static final int RESIZE_CORNER_SPRITE = 40;
        public static final int MOUSEOVER_TOOLTIP_CONTAINER = 41;
    }

    /**
     * Describes character summary tab widget
     * Last reviewed: 18/3/2022 0:29 am UTC+1
     */
    public static class CharacterSummaryTab {
        public static final int GROUP_INDEX = 629;
        public static final int PARENT_CONTAINER = 0;
        public static final int TOP_BAR_CONTAINER = 1;
        public static final int TOP_SEPARATOR_LINE = 2;
        public static final int TAB_CHARACTER_SUMMARY_CONTAINER = 3;
        public static final int TAB_CHARACTER_SUMMARY_LEFT_SIDE_SPRITE = 4;
        public static final int TAB_CHARACTER_SUMMARY_MID_SIDE_SPRITE = 5;
        public static final int TAB_CHARACTER_SUMMARY_RIGHT_SIDE_SPRITE = 6;
        public static final int TAB_CHARACTER_SUMMARY_ICON_SPRITE = 7;
        public static final int TAB_QUEST_LIST_CONTAINER = 8;
        public static final int TAB_QUEST_LIST_LEFT_SIDE_SPRITE = 9;
        public static final int TAB_QUEST_LIST_MID_SIDE_SPRITE = 10;
        public static final int TAB_QUEST_LIST_RIGHT_SIDE_SPRITE = 11;
        public static final int TAB_QUEST_LIST_ICON_SPRITE = 12;
        public static final int TAB_ACHIEVEMENT_DIARIES_CONTAINER = 13;
        public static final int TAB_ACHIEVEMENT_DIARIES_LEFT_SIDE_SPRITE = 14;
        public static final int TAB_ACHIEVEMENT_DIARIES_MID_SIDE_SPRITE = 15;
        public static final int TAB_ACHIEVEMENT_DIARIES_RIGHT_SIDE_SPRITE = 16;
        public static final int TAB_ACHIEVEMENT_DIARIES_ICON_SPRITE = 17;
        public static final int TAB_KOUREND_FAVOR_CONTAINER = 18;
        public static final int TAB_KOUREND_FAVOR_LEFT_SIDE_SPRITE = 19;
        public static final int TAB_KOUREND_FAVOR_MID_SIDE_SPRITE = 20;
        public static final int TAB_KOUREND_FAVOR_RIGHT_SIDE_SPRITE = 21;
        public static final int TAB_KOUREND_FAVOR_ICON_SPRITE = 22;
        // 23 - 27 gap
        public static final int TAB_LEAGUES_CONTAINER = 28;
        public static final int TAB_LEAGUES_LEFT_SIDE_SPRITE = 29;
        public static final int TAB_LEAGUES_MID_SIDE_SPRITE = 30;
        public static final int TAB_LEAGUES_RIGHT_SIDE_SPRITE = 31;
        public static final int TAB_LEAGUES_ICON_SPRITE = 32;
        public static final int INNER_FRAME_CONTAINER = 33;
    }

    /**
     * Describes your clan sub-tab widget under social tab widget
     * Last reviewed: 18/3/2022 1:19 am UTC+1
     */
    public static class YourClanSubTab {
        public static final int GROUP_INDEX = 701;
        public static final int PARENT_CONTAINER = 0;
        public static final int HEADER_DYNAMIC_CONTAINER = 1; // 0-1 header titles
        public static final int REFRESH_DYNAMIC_CONTAINER = 2;
        public static final int INNER_FRAME_DYNAMIC_CONTAINER = 3;
        public static final int JOIN_DYNAMIC_CONTAINER = 8;
        public static final int SETUP_DYNAMIC_CONTAINER = 9;
    }

    /**
     * Describes another clan sub-tab widget under social tab widget
     * Last reviewed: 18/3/2022 1:47 am UTC+1
     */
    public static class AnotherClanSubTab {
        public static final int GROUP_INDEX = 702;
        public static final int PARENT_CONTAINER = 0;
        public static final int HEADER_DYNAMIC_CONTAINER = 1; // 0-1 header titles
        public static final int REFRESH_DYNAMIC_CONTAINER = 2;
        public static final int INNER_FRAME_DYNAMIC_CONTAINER = 3;
        public static final int BUTTON1_DYNAMIC_CONTAINER = 8;
        public static final int BUTTON2_DYNAMIC_CONTAINER = 9;
        public static final int FIND_DYNAMIC_CONTAINER = 10;
    }

    /**
     * Describes social tab widget
     * Last reviewed: 18/3/2022 0:57 am UTC+1
     */
    public static class SocialTab {
        public static final int GROUP_INDEX = 707;
        public static final int PARENT_CONTAINER = 0;
        public static final int TOP_BAR_CONTAINER = 1;
        public static final int TOP_BAR_SEPARATOR_LINE = 2;
        public static final int TAB_CHAT_CHANNEL_CONTAINER = 3;
        public static final int TAB_YOUR_CLAN_CONTAINER = 4;
        public static final int TAB_VIEW_ANOTHER_CLAN_CONTAINER = 5;
        public static final int TAB_CHAT_GROUPING_CONTAINER = 6;
        public static final int FRAME_CONTAINER = 7;
        public static final int FRAME_TIME_LABEL = 8;
    }

    /**
     * Describes character summary sub-tab widget under character summary tab widget
     * Last reviewed: 18/3/2022 2:34 am UTC+1
     */
    public static class CharacterSummarySubTab {
        public static final int GROUP_INDEX = 712;
        public static final int PARENT_CONTAINER = 0;
        public static final int CHARACTER_NAME_LABEL = 1;
        public static final int SPRITES_LABELS_DYNAMIC_CONTAINER = 2;
        public static final int BUTTONS_DYNAMIC_CONTAINER = 3;
    }

    /**
     * Describes dynamic widgets
     * Last reviewed: 3/3/2022 0:37 am UTC+1
     */
    public static class DynamicComponents {
        public static class PrayerWidget {
            public static final int BG_LIGHT_SPRITE = 0;
            public static final int ICON_SPRITE = 1;
        }
        public static class GrandExchangeOfferTittle {
            public static final int BUTTON_COLLECT_SPRITE = 0;
            public static final int BUTTON_COLLECT_LABEL = 1;
            public static final int TITLE_LABEL = 2;
        }
        public static class GrandExchangeSlot {
            public static final int BUTTON_BUY_BG_BOX = 1;
            public static final int BUTTON_SELL_BG_BOX = 2;
            public static final int BUTTON_BUY_ICON_BOX = 3;
            public static final int BUTTON_SELL_ICON_BOX = 4;
            // 5-15 steel border sprites
            public static final int TITLE_LABEL = 16;
            public static final int ITEM_SPRITE = 17;
            public static final int ITEM_ID_SPRITE = 18; // id, quantity, quantity mode
            public static final int ITEM_NAME_LABEL = 19;
            public static final int PROGRESS_BAR_BOX = 20;
            public static final int ITEM_EACH_PRICE_LABEL = 25;
            public static final int BUY_ICON_SPRITE = 26;
            public static final int SELL_ICON_SPRITE = 27;
        }
        public static class GrandExchangeOfferDetails {
            public static final int ITEM_QUANTITY_LABEL = 18;
            public static final int ITEM_EACH_PRICE_LABEL = 25;
            public static final int ITEM_TOTAL_PRICE_LABEL = 29;
        }
        public static class GrandExchangeCollectionArea {
            public static final int LEFT_ITEM_BG_SPRITE = 0;
            public static final int RIGHT_ITEM_BG_SPRITE = 1;
            public static final int LEFT_ITEM_SPRITE = 2;
            public static final int RIGHT_ITEM_SPRITE = 3;
        }
        public static class AutocastSpells {
            public static final int CANCEL_LABEL = 0;
            public static final int WIND_STRIKE_SPRITE = 1;
            public static final int WATER_STRIKE_SPRITE = 2;
            public static final int EARTH_STRIKE_SPRITE = 3;
            public static final int FIRE_STRIKE_SPRITE = 4;
            public static final int WIND_BOLT_SPRITE = 5;
            public static final int WATER_BOLT_SPRITE = 6;
            public static final int EARTH_BOLT_SPRITE = 7;
            public static final int FIRE_BOLT_SPRITE = 8;
            public static final int WIND_BLAST_SPRITE = 9;
            public static final int WATER_BLAST_SPRITE = 10;
            public static final int EARTH_BLAST_SPRITE = 11;
            public static final int FIRE_BLAST_SPRITE = 12;
            public static final int WIND_WAVE_SPRITE = 13;
            public static final int WATER_WAVE_SPRITE = 14;
            public static final int EARTH_WAVE_SPRITE = 15;
            public static final int FIRE_WAVE_SPRITE = 16;
            public static final int CRUMBLE_UNDEAD_SPRITE = 17;
            public static final int MAGIC_DART_SPRITE = 18;
            // TODO: flames of zamorak, saradomin strike, claws of guthix
            // 19 - 30
            public static final int SMOKE_RUSH_SPRITE = 31;
            public static final int SHADOW_RUSH_SPRITE = 32;
            public static final int BLOOD_RUSH_SPRITE = 33;
            public static final int ICE_RUSH_SPRITE = 34;
            public static final int SMOKE_BURST_SPRITE = 35;
            public static final int SHADOW_BURST_SPRITE = 36;
            public static final int BLOOD_BURST_SPRITE = 37;
            public static final int ICE_BURST_SPRITE = 38;
            public static final int SMOKE_BLITZ_SPRITE = 39;
            public static final int SHADOW_BLITZ_SPRITE = 40;
            public static final int BLOOD_BLITZ_SPRITE = 41;
            public static final int ICE_BLITZ_SPRITE = 42;
            public static final int SMOKE_BARRAGE_SPRITE = 43;
            public static final int SHADOW_BARRAGE_SPRITE = 44;
            public static final int BLOOD_BARRAGE_SPRITE = 45;
            public static final int ICE_BARRAGE_SPRITE = 46;
            public static final int IBAN_BLAST_SPRITE = 47;
            public static final int WIND_SURGE_SPRITE = 48;
            public static final int WATER_SURGE_SPRITE = 49;
            public static final int EARTH_SURGE_SPRITE = 50;
            public static final int FIRE_SURGE_SPRITE = 51;
            // TODO: arceeus autocast
        }
        public static class TradeWindowDynamicContainer {
            public static final int TRADE_WINDOW_SPRITE = 0;
            public static final int TRADE_WINDOW_TITLE_LABEL = 1;
            // 2-12 steel border sprites
            public static final int TRADE_WINDOW_CLOSE_BUTTON = 13;
        }
        public static class TradeOfferDynamicContainer {
            public static final int USED_ITEM_SLOT_ONE = 0;
            public static final int USED_ITEM_SLOT_TWO = 1;
            public static final int USED_ITEM_SLOT_THREE = 2;
            public static final int USED_ITEM_SLOT_FOUR = 3;
            public static final int USED_ITEM_SLOT_FIVE = 4;
            public static final int USED_ITEM_SLOT_SIX = 5;
            public static final int USED_ITEM_SLOT_SEVEN = 6;
            public static final int USED_ITEM_SLOT_EIGHT = 7;
            public static final int USED_ITEM_SLOT_NINE = 8;
            public static final int USED_ITEM_SLOT_TEN = 9;
            public static final int USED_ITEM_SLOT_ELEVEN = 10;
            public static final int USED_ITEM_SLOT_TWELVE = 11;
            public static final int USED_ITEM_SLOT_THIRTEEN = 12;
            public static final int USED_ITEM_SLOT_FOURTEEN = 13;
            public static final int USED_ITEM_SLOT_FIFTEEN = 14;
            public static final int USED_ITEM_SLOT_SIXTEEN = 15;
            public static final int USED_ITEM_SLOT_SEVENTEEN = 16;
            public static final int USED_ITEM_SLOT_EIGHTEEN = 17;
            public static final int USED_ITEM_SLOT_NINETEEN = 18;
            public static final int USED_ITEM_SLOT_TWENTY = 19;
            public static final int USED_ITEM_SLOT_TWENTYONE = 20;
            public static final int USED_ITEM_SLOT_TWENTYTWO = 21;
            public static final int USED_ITEM_SLOT_TWENTYTHREE = 22;
            public static final int USED_ITEM_SLOT_TWENTYFOUR = 23;
            public static final int USED_ITEM_SLOT_TWENTYFIVE = 24;
            public static final int USED_ITEM_SLOT_TWENTYSIX = 25;
            public static final int USED_ITEM_SLOT_TWENTYSEVEN = 26;
            public static final int USED_ITEM_SLOT_TWENTYEIGHT = 27;
            public static final int UNUSED_ITEM_SLOT_ONE = 28;
            public static final int UNUSED_ITEM_SLOT_TWO = 29;
            public static final int UNUSED_ITEM_SLOT_THREE = 30;
            public static final int UNUSED_ITEM_SLOT_FOUR = 31;
            public static final int UNUSED_ITEM_SLOT_FIVE = 32;
            public static final int UNUSED_ITEM_SLOT_SIX = 33;
            public static final int UNUSED_ITEM_SLOT_SEVEN = 34;
            public static final int UNUSED_ITEM_SLOT_EIGHT = 35;
            public static final int UNUSED_ITEM_SLOT_NINE = 36;
            public static final int UNUSED_ITEM_SLOT_TEN = 37;
            public static final int UNUSED_ITEM_SLOT_ELEVEN = 38;
            public static final int UNUSED_ITEM_SLOT_TWELVE = 39;
            public static final int UNUSED_ITEM_SLOT_THIRTEEN = 40;
            public static final int UNUSED_ITEM_SLOT_FOURTEEN = 41;
            public static final int UNUSED_ITEM_SLOT_FIFTEEN = 42;
            public static final int UNUSED_ITEM_SLOT_SIXTEEN = 43;
            public static final int UNUSED_ITEM_SLOT_SEVENTEEN = 44;
            public static final int UNUSED_ITEM_SLOT_EIGHTEEN = 45;
            public static final int UNUSED_ITEM_SLOT_NINETEEN = 46;
            public static final int UNUSED_ITEM_SLOT_TWENTY = 47;
            public static final int UNUSED_ITEM_SLOT_TWENTYONE = 48;
            public static final int UNUSED_ITEM_SLOT_TWENTYTWO = 49;
            public static final int UNUSED_ITEM_SLOT_TWENTYTHREE = 50;
            public static final int UNUSED_ITEM_SLOT_TWENTYFOUR = 51;
            public static final int UNUSED_ITEM_SLOT_TWENTYFIVE = 52;
            public static final int UNUSED_ITEM_SLOT_TWENTYSIX = 53;
            public static final int UNUSED_ITEM_SLOT_TWENTYSEVEN = 54;
            public static final int UNUSED_ITEM_SLOT_TWENTYEIGHT = 55;
        }
        // NOTE: this is rubbish not every interface will implement close sprite under 11
        public static class Global {
            public static final int DYNAMIC_CLOSE_BUTTON = 11;
        }
    }
}