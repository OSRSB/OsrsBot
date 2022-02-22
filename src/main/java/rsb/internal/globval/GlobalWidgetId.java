package rsb.internal.globval;

import net.runelite.api.widgets.WidgetInfo;

public class GlobalWidgetId {

    /**
     * Global Dynamic Components
     * Used in almost every interface for the respective function
     * if it exists
     */
    //This seems to be universal in interfaces for the close component.
    public static final int DYNAMIC_CLOSE_BUTTON = 11;

    //NON GLOBAL
    public static final int DYNAMIC_CHAT_BOX_FIRST_MESSAGE = 0; //TODO: Check these. Not sure what they do.
    public static final int DYNAMIC_CHAT_BOX_LATEST_MESSAGE = 1; //TODO: Check these. Not sure what they do.


    /**Login Screen Widget IDs*/
    public static final int INTERFACE_LOGIN_SCREEN = WidgetInfo.LOGIN_CLICK_TO_PLAY_SCREEN.getGroupId();
    public static final int INTERFACE_LOGIN_SCREEN_MOTW = 6;

    /**
     * Bank Widget IDs
     */
    //Parent ID
    public static final int INTERFACE_BANK = WidgetInfo.BANK_CONTAINER.getGroupId();
    //Child ID
    public static final int INTERFACE_BANK_DYNAMIC_COMPONENTS = 2;
    public static final int INTERFACE_BANK_INVENTORY_ITEMS_CONTAINER = 3;
    public static final int INTERFACE_BANK_ITEM_COUNT = 5;
    public static final int INTERFACE_BANK_ITEM_MAX = 9;
    public static final int INTERFACE_BANK_TAB = 11;
    //Dynamic close button = 11
    public static final int INTERFACE_BANK_INVENTORY = 13;
    public static final int INTERFACE_BANK_SCROLLBAR = 14;
    public static final int INTERFACE_BANK_BUTTON_SWAP = 18;
    //Fill
    public static final int INTERFACE_BANK_BUTTON_INSERT = 19;
    public static final int INTERFACE_BANK_BUTTON_ITEM = 23;
    public static final int INTERFACE_BANK_BUTTON_NOTE = 25;
    public static final int INTERFACE_BANK_BUTTON_SEARCH = 40;
    public static final int INTERFACE_BANK_BUTTON_DEPOSIT_CARRIED_ITEMS = WidgetInfo.BANK_DEPOSIT_INVENTORY.getChildId();
    public static final int INTERFACE_BANK_BUTTON_DEPOSIT_WORN_ITEMS = WidgetInfo.BANK_DEPOSIT_EQUIPMENT.getChildId();



    /**
     * Collection box
     */
    public static final int INTERFACE_COLLECTION_BOX = 402;


    /**
     * Deposit box ids
     */
    //Parent ID
    public static final int INTERFACE_DEPOSIT_BOX = WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER.getGroupId();
    //Child ID
    public static final int INTERFACE_DEPOSIT_DYNAMIC_COMPONENTS = 1;
    public static final int INTERFACE_DEPOSIT_INVENTORY_ITEMS_CONTAINER = WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER.getChildId();
    public static final int INTERFACE_DEPOSIT_BUTTON_DEPOSIT_CARRIED_ITEMS = 4;
    public static final int INTERFACE_DEPOSIT_BUTTON_DEPOSIT_WORN_ITEMS = 6;
    public static final int INTERFACE_DEPOSIT_BUTTON_DEPOSIT_LOOT = 8;

    /**
     * Combat ids
     */
    //Parent ID
    public static int INTERFACE_COMBAT = WidgetInfo.COMBAT_LEVEL.getGroupId();
    //Child ID
    public static int INTERFACE_COMBAT_DEFENSIVE_CAST_SPELL = 22;
    public static int INTERFACE_COMBAT_AUTO_CAST_SPELL = 27;
    public static int INTERFACE_COMBAT_AUTO_RETALIATE = 30;

    /**
     * Equipment ids
     */
    //Parent ID
    public static final int INTERFACE_EQUIPMENT = WidgetInfo.EQUIPMENT.getGroupId();
    //Child ID
    public static final int INTERFACE_EQUIPMENT_INVENTORY_ITEMS_CONTAINER = WidgetInfo.EQUIPMENT_INVENTORY_ITEMS_CONTAINER.getChildId();
    public static final int INTERFACE_EQUIPMENT_ITEM_SLOTS = 11;
    public static class EquipmentSlotId {
        public static final int INTERFACE_EQUIPMENT_HELMET = 14;
        public static final int INTERFACE_EQUIPMENT_CAPE = 15;
        public static final int INTERFACE_EQUIPMENT_NECK = 16;
        public static final int INTERFACE_EQUIPMENT_WEAPON = 17;
        public static final int INTERFACE_EQUIPMENT_BODY = 18;
        public static final int INTERFACE_EQUIPMENT_SHIELD = 19;
        public static final int INTERFACE_EQUIPMENT_LEGS = 20;
        public static final int INTERFACE_EQUIPMENT_HANDS = 21;
        public static final int INTERFACE_EQUIPMENT_FEET = 22;
        public static final int INTERFACE_EQUIPMENT_RING = 23;
        public static final int INTERFACE_EQUIPMENT_AMMO = 24;
    }

    /**
     * Trade ids
     */
    //Parent id
    public static final int INTERFACE_TRADE_MAIN = 335;
    //Child id
    public static final int INTERFACE_TRADE_SECOND_PERSONAL = 28;
    public static final int INTERFACE_TRADE_SECOND_PARTNER = 29;

    public final static int INTERFACE_TRADE_MAIN_INV_SLOTS = 9;
    public static final int INTERFACE_TRADE_MAIN_ACCEPT = 10;
    public static final int INTERFACE_TRADE_MAIN_DECLINE = 13;
    public static final int INTERFACE_TRADE_MAIN_PERSONAL = 25;
    public static final int INTERFACE_TRADE_MAIN_PARTNER = 28;
    public static final int INTERFACE_TRADE_MAIN_NAME = 31;
    //Parent id
    public static final int INTERFACE_TRADE_SECOND = 334;
    //Child id
    public static final int INTERFACE_TRADE_SECOND_ACCEPT = 13;
    public static final int INTERFACE_TRADE_SECOND_DECLINE = 14;
    public static final int INTERFACE_TRADE_SECOND_NAME = 30;

    /**
     * Store ids
     */
    //Parent id
    public static final int INTERFACE_STORE = WidgetInfo.SHOP_INVENTORY_ITEMS_CONTAINER.getGroupId();
    //Child id
    public static final int INTERFACE_STORE_DYNAMIC_COMPONENTS = 1;
    public static final int INTERFACE_STORE_ITEMS_CONTAINER = WidgetInfo.SHOP_INVENTORY_ITEMS_CONTAINER.getChildId();

    /**
     * Skill ids
     */
    //Parent id
    public static final int INTERFACE_STATS = 320;



    //Child id
    public enum Skill {
        INTERFACE_SKILL_ATTACK,
        INTERFACE_SKILL_STRENGTH,
        INTERFACE_SKILL_DEFENCE,
        INTERFACE_SKILL_RANGED,
        INTERFACE_SKILL_PRAYER,
        INTERFACE_SKILL_MAGIC,
        INTERFACE_SKILL_RUNECRAFT,
        INTERFACE_SKILL_CONSTRUCTION,
        INTERFACE_SKILL_HITPOINTS,
        INTERFACE_SKILL_AGILITY,
        INTERFACE_SKILL_HERBLORE,
        INTERFACE_SKILL_THIEVING,
        INTERFACE_SKILL_CRAFTING,
        INTERFACE_SKILL_FLETCHING,
        INTERFACE_SKILL_SLAYER,
        INTERFACE_SKILL_HUNTER,
        INTERFACE_SKILL_MINING,
        INTERFACE_SKILL_SMITHING,
        INTERFACE_SKILL_FISHING,
        INTERFACE_SKILL_COOKING,
        INTERFACE_SKILL_FIREMAKING,
        INTERFACE_SKILL_WOODCUTTING,
        INTERFACE_SKILL_FARMING,
        INTERFACE_SKILL_TOTAL;
    }

    /**
     * Magic ids
     * Interfaces used
     *  FIXED_VIEWPORT 548 (can be gotten from WidgetInfo
     *  548 into 72 into (218(N) into 3) N=Nested
     */

    //Parent id
    public static final int FIXED_VIEWPORT_SPELL_BOOK_CONTAINER_ID = 72;


    //Parent id
    public static final int INTERFACE_MAGIC_AUTOCAST_SPELL_BOOK = 201;
    //Child id
    public static final int INTERFACE_MAGIC_AUTOCAST_SPELL_LIST = 1;

    //Parent id
    public static final int INTERFACE_MAGIC_SPELL_BOOK = 218;
    //Child id
    public static final int INTERFACE_MAGIC_SPELL_LIST = 3;

    //Child id
    public static final int INTERFACE_SHOW_COMBAT_SPELLS = 5;
    public static final int INTERFACE_SHOW_TELEPORT_SPELLS = 7;
    public static final int INTERFACE_SHOW_UTILITY_SPELLS = 9;
    public static final int INTERFACE_SHOW_LEVEL_UNCASTABLE_SPELLS = 11;
    public static final int INTERFACE_SHOW_RUNES_UNCASTABLE_SPELLS = 13;

    /*
    public static final int INTERFACE_DEFENSIVE_STANCE = 2;
    public static final int INTERFACE_SHOW_COMBAT_SPELLS = 7;
    public static final int INTERFACE_SHOW_TELEPORT_SPELLS = 9;
    public static final int INTERFACE_SHOW_MISC_SPELLS = 11;
    public static final int INTERFACE_SHOW_SKILL_SPELLS = 13;
    public static final int INTERFACE_SORT_BY_LEVEL = 15;
    public static final int INTERFACE_SORT_BY_COMBAT = 16;
    public static final int INTERFACE_SORT_BY_TELEPORTS = 17;
    */

    public static class SpellId {
        public static final int SPELL_WIND_STRIKE = 7;
        public static final int SPELL_CONFUSE = 8;
        public static final int SPELL_ENCHANT_CROSSBOW_BOLT = 9;
        public static final int SPELL_WATER_STRIKE = 10;
        public static final int SPELL_LVL_1_ENCHANT = 11;
        public static final int SPELL_EARTH_STRIKE = 12;
        public static final int SPELL_WEAKEN = 13;
        public static final int SPELL_FIRE_STRIKE = 14;
        public static final int SPELL_BONES_TO_BANANAS = 15;
        public static final int SPELL_WIND_BOLT = 16;
        public static final int SPELL_CURSE = 17;
        public static final int SPELL_BIND = 18;
        public static final int SPELL_LOW_LEVEL_ALCHEMY = 19;
        public static final int SPELL_WATER_BOLT = 20;
        public static final int SPELL_VARROCK_TELEPORT = 21;
        public static final int SPELL_LVL_2_ENCHANT = 22;
        public static final int SPELL_EARTH_BOLT = 23;
        public static final int SPELL_LUMBRIDGE_TELEPORT = 24;
        public static final int SPELL_TELEKINETIC_GRAB = 25;
        public static final int SPELL_FIRE_BOLT = 26;
        public static final int SPELL_FALADOR_TELEPORT = 27;
        public static final int SPELL_CRUMBLE_UNDEAD = 28;
        public static final int SPELL_TELEPORT_TO_HOUSE = 29;
        public static final int SPELL_WIND_BLAST = 30;
        public static final int SPELL_SUPERHEAT_ITEM = 31;
        public static final int SPELL_CAMELOT_TELEPORT = 32;
        public static final int SPELL_WATER_BLAST = 33;
        public static final int SPELL_LVL_3_ENCHANT = 34;
        public static final int SPELL_IBAN_BLAST = 35;
        public static final int SPELL_SNARE = 36;
        public static final int SPELL_MAGIC_DART = 37;
        public static final int SPELL_ARDOUGNE_TELEPORT = 38;
        public static final int SPELL_EARTH_BLAST = 39;
        public static final int SPELL_HIGH_LEVEL_ALCHEMY = 40;
        public static final int SPELL_CHARGE_WATER_ORB = 41;
        public static final int SPELL_LVL_4_ENCHANT = 42;
        public static final int SPELL_WATCHTOWER_TELEPORT = 43;
        public static final int SPELL_FIRE_BLAST = 44;
        public static final int SPELL_CHARGE_EARTH_ORB = 45;
        public static final int SPELL_BONES_TO_PEACHES = 46;
        public static final int SPELL_SARADOMIN_STRIKE = 47;
        public static final int SPELL_CLAWS_OF_GUTHIX = 48;
        public static final int SPELL_FLAMES_OF_ZAMORAK = 49;
        public static final int SPELL_TROLLHEIM_TELEPORT = 50;
        public static final int SPELL_WIND_WAVE = 51;
        public static final int SPELL_CHARGE_FIRE_ORB = 52;
        public static final int SPELL_APE_ATOLL_TELEPORT = 53;
        public static final int SPELL_WATER_WAVE = 54;
        public static final int SPELL_CHARGE_AIR_ORB = 55;
        public static final int SPELL_VULNERABILITY = 56;
        public static final int SPELL_LVL_5_ENCHANT = 57;
        public static final int SPELL_KOUREND_CASTLE_TELEPORT = 58;
        public static final int SPELL_EARTH_WAVE = 59;
        public static final int SPELL_ENFEEBLE = 60;
        public static final int SPELL_TELEOTHER_LUMBRIDGE = 61;
        public static final int SPELL_FIRE_WAVE = 62;
        public static final int SPELL_ENTANGLE = 63;
        public static final int SPELL_STUN = 64;
        public static final int SPELL_CHARGE = 65;
        public static final int SPELL_WIND_SURGE = 66;
        public static final int SPELL_TELEOTHER_FALADOR = 67;
        public static final int SPELL_WATER_SURGE = 68;
        public static final int SPELL_TELE_BLOCK = 69;
        public static final int SPELL_TELEPORT_TO_TARGET = 70;
        public static final int SPELL_LVL_6_ENCHANT = 71;
        public static final int SPELL_TELEOTHER_CAMELOT = 72;
        // ANCIENT
        public static final int SPELL_EARTH_SURGE = 73;
        public static final int SPELL_LVL_7_ENCHANT = 74;
        public static final int SPELL_FIRE_SURGE = 75;
        public static final int SPELL_ICE_RUSH = 76;
        public static final int SPELL_ICE_BLITZ = 77;
        public static final int SPELL_ICE_BURST = 78;
        public static final int SPELL_ICE_BARRAGE = 79;
        public static final int SPELL_BLOOD_RUSH = 80;
        public static final int SPELL_BLOOD_BLITZ = 81;
        public static final int SPELL_BLOOD_BURST = 82;
        public static final int SPELL_BLOOD_BARRAGE = 83;
        public static final int SPELL_SMOKE_RUSH = 84;
        public static final int SPELL_SMOKE_BLITZ = 85;
        public static final int SPELL_SMOKE_BURST = 86;
        public static final int SPELL_SMOKE_BARRAGE = 87;
        public static final int SPELL_SHADOW_RUSH = 88;
        public static final int SPELL_SHADOW_BLITZ = 89;
        public static final int SPELL_SHADOW_BURST = 90;
        public static final int SPELL_SHADOW_BARRAGE = 91;
        public static final int SPELL_PADDEWWA_TELEPORT = 92;
        public static final int SPELL_SENNTISTEN_TELEPORT = 93;
        public static final int SPELL_KHARYRLL_TELEPORT = 94;
        public static final int SPELL_LASSAR_TELEPORT = 95;
        public static final int SPELL_DAREEYAK_TELEPORT = 96;
        public static final int SPELL_CARRALLANGAR_TELEPORT = 97;
        public static final int SPELL_ANNAKARL_TELEPORT = 98;
        // LUNAR
        public static final int SPELL_GHORROCK_TELEPORT = 99;
        public static final int SPELL_EDGEVILLE_HOME_TELEPORT = 100;
        public static final int SPELL_LUNAR_HOME_TELEPORT = 101;
        public static final int SPELL_BAKE_PIE = 102;
        public static final int SPELL_CURE_PLANT = 103;
        public static final int SPELL_MONSTER_EXAMINE = 104;
        public static final int SPELL_NPC_CONTACT = 105;
        public static final int SPELL_CURE_OTHER = 106;
        public static final int SPELL_HUMIDIFY = 107;
        public static final int SPELL_MOONCLAN_TELEPORT = 108;
        public static final int SPELL_TELE_GROUP_MOONCLAN = 109;
        public static final int SPELL_CURE_ME = 110;
        public static final int SPELL_HUNTER_KIT = 111;
        public static final int SPELL_WATERBIRTH_TELEPORT = 112;
        public static final int SPELL_TELE_GROUP_WATERBIRTH = 113;
        public static final int SPELL_CURE_GROUP = 114;
        public static final int SPELL_STAT_SPY = 115;
        public static final int SPELL_BARBARIAN_TELEPORT = 116;
        public static final int SPELL_TELE_GROUP_BARBARIAN = 117;
        public static final int SPELL_SUPERGLASS_MAKE = 118;
        public static final int SPELL_TAN_LEATHER = 119;
        public static final int SPELL_KHAZARD_TELEPORT = 120;
        public static final int SPELL_TELE_GROUP_KHAZARD = 121;
        public static final int SPELL_DREAM = 122;
        public static final int SPELL_STRING_JEWELLERY = 123;
        public static final int SPELL_STAT_RESTORE_POT_SHARE = 124;
        public static final int SPELL_MAGIC_IMBUE = 125;
        public static final int SPELL_FERTILE_SOIL = 126;
        public static final int SPELL_BOOST_POTION_SHARE = 127;
        public static final int SPELL_FISHING_GUILD_TELEPORT = 128;
        public static final int SPELL_TELE_GROUP_FISHING_GUILD = 129;
        public static final int SPELL_PLANK_MAKE = 130;
        public static final int SPELL_CATHERBY_TELEPORT = 131;
        public static final int SPELL_TELE_GROUP_CATHERBY = 132;
        public static final int SPELL_RECHARGE_DRAGONSTONE = 133;
        public static final int SPELL_ICE_PLATEAU_TELEPORT = 134;
        public static final int SPELL_TELE_GROUP_ICE_PLATEAU = 135;
        public static final int SPELL_ENERGY_TRANSFER = 136;
        public static final int SPELL_HEAL_OTHER = 137;
        public static final int SPELL_VENGEANCE_OTHER = 138;
        public static final int SPELL_VENGEANCE = 139;
        public static final int SPELL_HEAL_GROUP = 140;
        public static final int SPELL_SPELLBOOK_SWAP = 141;
        public static final int SPELL_GEOMANCY = 142;
        // ARCEUUS
        public static final int SPELL_SPIN_FLAX = 143;
        public static final int SPELL_OURANIA_TELEPORT = 144;
        public static final int SPELL_ARCEUUS_HOME_TELEPORT = 145;
        public static final int SPELL_BASIC_REANIMATION = 146;
        public static final int SPELL_ARCEUUS_LIBRARY_TELEPORT = 147;
        public static final int SPELL_ADEPT_REANIMATION = 148;
        public static final int SPELL_EXPERT_REANIMATION = 149;
        public static final int SPELL_MASTER_REANIMATION = 150;
        public static final int SPELL_DRAYNOR_MANOR_TELEPORT = 151;
        public static final int SPELL_MIND_ALTAR_TELEPORT = 153;
        public static final int SPELL_RESPAWN_TELEPORT = 154;
        public static final int SPELL_SALVE_GRAVEYARD_TELEPORT = 155;
        public static final int SPELL_FENKENSTRAINS_CASTLE_TELEPORT = 156;
        public static final int SPELL_WEST_ARDOUGNE_TELEPORT = 157;
        public static final int SPELL_HARMONY_ISLAND_TELEPORT = 158;
        public static final int SPELL_CEMETERY_TELEPORT = 159;
        public static final int SPELL_RESURRECT_CROPS = 160;
        public static final int SPELL_BARROWS_TELEPORT = 161;
        public static final int SPELL_APE_ATOLL_TELEPORT_A = 162;
        public static final int SPELL_BATTLEFRONT_TELEPORT = 163;
        public static final int SPELL_INFERIOR_DEMONBANE = 164;
        public static final int SPELL_SUPERIOR_DEMONBANE = 165;
        public static final int SPELL_DARK_DEMONBANE = 166;
        public static final int SPELL_MARK_OF_DARKNESS = 167;
        public static final int SPELL_GHOSTLY_GRASP = 168;
        public static final int SPELL_SKELETAL_GRASP = 169;
        public static final int SPELL_UNDEAD_GRASP = 170;
        public static final int SPELL_WARD_OF_ARCEUUS = 171;
        public static final int SPELL_LESSER_CORRUPTION = 172;
        public static final int SPELL_GREATER_CORRUPTION = 173;
        public static final int SPELL_DEMONIC_OFFERING = 174;
        public static final int SPELL_SINISTER_OFFERING = 175;
        public static final int SPELL_DEGRIME = 176;
        public static final int SPELL_SHADOW_VEIL = 177;
        public static final int SPELL_VILE_VIGOUR = 178;
        public static final int SPELL_DARK_LURE = 179;
        public static final int SPELL_DEATH_CHARGE = 180;
        public static final int SPELL_RESURRECT_LESSER_GHOST = 181;
        public static final int SPELL_RESURRECT_LESSER_SKELETON = 182;
        public static final int SPELL_RESURRECT_LESSER_ZOMBIE = 183;
        public static final int SPELL_RESURRECT_SUPERIOR_GHOST = 184;
        public static final int SPELL_RESURRECT_SUPERIOR_SKELETON = 185;
        public static final int SPELL_RESURRECT_SUPERIOR_ZOMBIE = 186;
        public static final int SPELL_RESURRECT_GREATER_GHOST = 187;
        public static final int SPELL_RESURRECT_GREATER_SKELETON = 188;
        public static final int SPELL_RESURRECT_GREATER_ZOMBIE = 189;
    }


    /**
     * GrandExchange ids
     */
    public static final int INTERFACE_GRAND_EXCHANGE_WINDOW = WidgetInfo.GRAND_EXCHANGE_WINDOW_CONTAINER.getGroupId();
    public static final int INTERFACE_GRAND_EXCHANGE_OFFER_WINDOW = WidgetInfo.GRAND_EXCHANGE_OFFER_CONTAINER.getChildId();

    public static final int INTERFACE_GRAND_EXCHANGE_INTERFACE_LAYOUT = 2;

    public static final int INTERFACE_GRAND_EXCHANGE_SELL_INVENTORY = 0;
    public static final int INTERFACE_BUY_SEARCH_BOX = 389;

    public static final int INTERFACE_GRAND_EXCHANGE_DESCRIPTION = 6;

    //This is a dynamic child of the description widget
    public static final int GRAND_EXCHANGE_DESCRIPTION_COLLECT = 1;

    //These are dynamic children in the offer box
    public static final int GRAND_EXCHANGE_BUY_BUTTON = 3;
    public static final int GRAND_EXCHANGE_SELL_BUTTON = 4;
    public static final int GRAND_EXCHANGE_SLOT_TITLE = 16;
    public static final int GRAND_EXCHANGE_ITEM_SPRITE = 17;
    public static final int GRAND_EXCHANGE_ITEM_ID = 18;
    public static final int GRAND_EXCHANGE_ITEM_NAME = 19;
    public static final int GRAND_EXCHANGE_BUY_ICON = 26;
    public static final int GRAND_EXCHANGE_SELL_ICON = 27;


    public static final int[] GRAND_EXCHANGE_OFFER_BOXES = {7, 8, 9, 10, 11, 12, 13, 14};

    public static final int INTERFACE_GRAND_EXCHANGE_COLLECTION_AREA = 23;
    //These are dynamic children of the collection area
    public static final int GRAND_EXCHANGE_COLLECT_BOX_ONE = 2;
    public static final int GRAND_EXCHANGE_COLLECT_BOX_TWO = 3;


    /**
     *  Minimap ids
     */
    public static final int INTERFACE_MINIMAP_ORBS = WidgetInfo.MINIMAP_ORBS.getGroupId();
    public static final int INTERFACE_MINIMAP_HEALTH_ORB = WidgetInfo.MINIMAP_HEALTH_ORB.getChildId();
    public static final int INTERFACE_MINIMAP_PRAYER_ORB = WidgetInfo.MINIMAP_PRAYER_ORB.getChildId();
    public static final int INTERFACE_MINIMAP_QUICK_PRAYER_ORB = WidgetInfo.MINIMAP_QUICK_PRAYER_ORB.getChildId();
    public static final int INTERFACE_MINIMAP_RUN_ORB = WidgetInfo.MINIMAP_RUN_ORB.getChildId();
    public static final int INTERFACE_MINIMAP_SPEC_ORB = WidgetInfo.MINIMAP_SPEC_ORB.getChildId();
    public static final int INTERFACE_MINIMAP_HEALTH_ORB_TEXT = 5;
    public static final int INTERFACE_MINIMAP_PRAYER_ORB_TEXT = 15;
    public static final int INTERFACE_MINIMAP_RUN_ORB_TEXT = 23;
    public static final int INTERFACE_MINIMAP_SPEC_ORB_TEXT = 31;
    public static final int INTERFACE_MINIMAP_QUICK_PRAYER_ORB_SPRITE = 2;


    /**
     * Prayer ids
     */

    public static final int INTERFACE_PRAYER_BOOK = WidgetInfo.PRAYER_THICK_SKIN.getGroupId();
    public static final int INTERFACE_NORMAL_PRAYERS = 4;
    //The dynamic component in each prayer widget that is visible when prayers are active (the white border)
    public static final int ACTIVE_PRAYER_BORDER = 1;

    /**
     * Provides Prayer Book(s) Information.
     * This is written in preparation for future prayer books
     * Likely they'll be similar to MagicBook and simply be one set of child components
     * of the one we list here
     *
     * @author GigiaJ
     */
    public enum PrayerBook {

        NORMAL(INTERFACE_NORMAL_PRAYERS), NULL(-1);

        private final int index;

        PrayerBook(int index) {
            this.index = index;
        }

        int getIndex() {
            return this.index;
        }
    }

    public enum Prayer {

        THICK_SKIN(0, 1, 5, 0),
        BURST_OF_STRENGTH(1, 4, 6, 1),
        CLARITY_OF_THOUGHT(2, 7, 7, 2),
        SHARP_EYE(3, 8, 23, 18),
        MYSTIC_WILL(4, 9, 24, 19),
        ROCK_SKIN(5, 10, 8, 3),
        SUPERHUMAN_STRENGTH(6, 13, 9, 4),
        IMPROVED_REFLEXES(7, 16, 10, 5),
        RAPID_RESTORE(8, 19, 11, 6),
        RAPID_HEAL(9, 22, 12, 7),
        PROTECT_ITEM(10, 25, 13, 8),
        HAWK_EYE(11, 26, 25, 20),
        MYSTIC_LORE(12, 27, 26, 21),
        STEEL_SKIN(13, 28, 14, 9),
        ULTIMATE_STRENGTH(14, 31, 15, 10),
        INCREDIBLE_REFLEXES(15, 34, 16, 11),
        PROTECT_FROM_MAGIC(17, 37, 17, 12),
        PROTECT_FROM_MISSILES(18, 40, 18, 13),
        PROTECT_FROM_MELEE(19, 43, 19, 14),
        EAGLE_EYE(20, 44, 27, 22),
        MYSTIC_MIGHT(21, 45, 28, 23),
        RETRIBUTION(22, 46, 20, 15),
        REDEMPTION(23, 49, 21, 16),
        SMITE(24, 52, 22, 17),
        PRESERVE(25, 55, 33, 28),
        CHIVALRY(26, 60, 29, 25),
        PIETY(27, 70, 30, 26),
        RIGOUR(28, 74, 31, 24),
        AUGURY(29, 77, 32, 27);

        private final int index;
        private final int level;
        private final int prayerId;
        private final int quickPrayerId;

        Prayer(int index, int level, int prayerId, int quickPrayerId) {
            this.index = index;
            this.level = level;
            this.prayerId = prayerId;
            this.quickPrayerId = quickPrayerId;
        }

        public int getPrayerId() {
            return prayerId;
        }

        public int getQuickPrayerId() {
            return quickPrayerId;
        }

        public int getRequiredLevel() {
            return level;
        }

    }



}
