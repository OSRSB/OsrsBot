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


    /**
     * Bank Widget IDs
     */
    //Parent ID
    public static final int INTERFACE_BANK = WidgetInfo.BANK_CONTAINER.getGroupId();
    //Child ID
    public static final int INTERFACE_BANK_DYNAMIC_COMPONENTS = 2;
    public static final int INTERFACE_BANK_ITEM_COUNT = 5;
    public static final int INTERFACE_BANK_ITEM_MAX = 8;
    public static final int INTERFACE_BANK_TAB = 10;
    //Dynamic close button = 11
    public static final int INTERFACE_BANK_INVENTORY = 12;
    public static final int INTERFACE_BANK_SCROLLBAR = 13;
    public static final int INTERFACE_BANK_BUTTON_SWAP = 17;
    //Fill
    public static final int INTERFACE_BANK_BUTTON_INSERT = 19;
    public static final int INTERFACE_BANK_BUTTON_ITEM = 22;
    public static final int INTERFACE_BANK_BUTTON_NOTE = 24;
    public static final int INTERFACE_BANK_BUTTON_SEARCH = 39;
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
    public static final int INTERFACE_EQUIPMENT_COMPONENT = WidgetInfo.EQUIPMENT_INVENTORY_ITEMS_CONTAINER.getChildId();
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
    public static final int INTERFACE_STORE_ITEMS = WidgetInfo.SHOP_INVENTORY_ITEMS_CONTAINER.getChildId();

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
        public static final int SPELL_LUMBRIDGE_HOME_TELEPORT = 4;
        public static final int SPELL_WIND_STRIKE = 5;
        public static final int SPELL_CONFUSE = 6;
        public static final int SPELL_ENCHANT_CROSSBOW_BOLT = 7;
        public static final int SPELL_WATER_STRIKE = 8;
        public static final int SPELL_LVL_1_ENCHANT = 9;
        public static final int SPELL_EARTH_STRIKE = 10;
        public static final int SPELL_WEAKEN = 11;
        public static final int SPELL_FIRE_STRIKE = 12;
        public static final int SPELL_BONES_TO_BANANAS = 13;
        public static final int SPELL_WIND_BOLT = 14;
        public static final int SPELL_CURSE = 15;
        public static final int SPELL_BIND = 16;
        public static final int SPELL_LOW_LEVEL_ALCHEMY = 17;
        public static final int SPELL_WATER_BOLT = 18;
        public static final int SPELL_VARROCK_TELEPORT = 19;
        public static final int SPELL_LVL_2_ENCHANT = 20;
        public static final int SPELL_EARTH_BOLT = 21;
        public static final int SPELL_LUMBRIDGE_TELEPORT = 22;
        public static final int SPELL_TELEKINETIC_GRAB = 23;
        public static final int SPELL_FIRE_BOLT = 24;
        public static final int SPELL_FALADOR_TELEPORT = 25;
        public static final int SPELL_CRUMBLE_UNDEAD = 26;
        public static final int SPELL_TELEPORT_TO_HOUSE = 27;
        public static final int SPELL_WIND_BLAST = 28;
        public static final int SPELL_SUPERHEAT_ITEM = 29;
        public static final int SPELL_CAMELOT_TELEPORT = 30;
        public static final int SPELL_WATER_BLAST = 31;
        public static final int SPELL_LVL_3_ENCHANT = 32;
        public static final int SPELL_IBAN_BLAST = 33;
        public static final int SPELL_SNARE = 34;
        public static final int SPELL_MAGIC_DART = 35;
        public static final int SPELL_ARDOUGNE_TELEPORT = 36;
        public static final int SPELL_EARTH_BLAST = 37;
        public static final int SPELL_HIGH_LEVEL_ALCHEMY = 38;
        public static final int SPELL_CHARGE_WATER_ORB = 39;
        public static final int SPELL_LVL_4_ENCHANT = 40;
        public static final int SPELL_WATCHTOWER_TELEPORT = 41;
        public static final int SPELL_FIRE_BLAST = 42;
        public static final int SPELL_CHARGE_EARTH_ORB = 43;
        public static final int SPELL_BONES_TO_PEACHES = 44;
        public static final int SPELL_SARADOMIN_STRIKE = 45;
        public static final int SPELL_CLAWS_OF_GUTHIX = 46;
        public static final int SPELL_FLAMES_OF_ZAMORAK = 47;
        public static final int SPELL_TROLLHEIM_TELEPORT = 48;
        public static final int SPELL_WIND_WAVE = 49;
        public static final int SPELL_CHARGE_FIRE_ORB = 50;
        public static final int SPELL_APE_ATOLL_TELEPORT = 51;
        public static final int SPELL_WATER_WAVE = 52;
        public static final int SPELL_CHARGE_AIR_ORB = 53;
        public static final int SPELL_VULNERABILITY = 54;
        public static final int SPELL_LVL_5_ENCHANT = 55;
        public static final int SPELL_KOUREND_CASTLE_TELEPORT = 56;
        public static final int SPELL_EARTH_WAVE = 57;
        public static final int SPELL_ENFEEBLE = 58;
        public static final int SPELL_TELEOTHER_LUMBRIDGE = 59;
        public static final int SPELL_FIRE_WAVE = 60;
        public static final int SPELL_ENTANGLE = 61;
        public static final int SPELL_STUN = 62;
        public static final int SPELL_CHARGE = 63;
        public static final int SPELL_WIND_SURGE = 64;
        public static final int SPELL_TELEOTHER_FALADOR = 65;
        public static final int SPELL_WATER_SURGE = 66;
        public static final int SPELL_TELE_BLOCK = 67;
        public static final int SPELL_TELEPORT_TO_BOUNTY_TARGET = 68;
        public static final int SPELL_LVL_6_ENCHANT = 69;
        public static final int SPELL_TELEOTHER_CAMELOT = 70;
        public static final int SPELL_EARTH_SURGE = 71;
        public static final int SPELL_LVL_7_ENCHANT = 72;
        // ANCIENT
        public static final int SPELL_FIRE_SURGE = 73;
        public static final int SPELL_ICE_RUSH = 74;
        public static final int SPELL_ICE_BLITZ = 75;
        public static final int SPELL_ICE_BURST = 76;
        public static final int SPELL_ICE_BARRAGE = 77;
        public static final int SPELL_BLOOD_RUSH = 78;
        public static final int SPELL_BLOOD_BLITZ = 79;
        public static final int SPELL_BLOOD_BURST = 80;
        public static final int SPELL_BLOOD_BARRAGE = 81;
        public static final int SPELL_SMOKE_RUSH = 82;
        public static final int SPELL_SMOKE_BLITZ = 83;
        public static final int SPELL_SMOKE_BURST = 84;
        public static final int SPELL_SMOKE_BARRAGE = 85;
        public static final int SPELL_SHADOW_RUSH = 86;
        public static final int SPELL_SHADOW_BLITZ = 87;
        public static final int SPELL_SHADOW_BURST = 88;
        public static final int SPELL_SHADOW_BARRAGE = 89;
        public static final int SPELL_PADDEWWA_TELEPORT = 90;
        public static final int SPELL_SENNTISTEN_TELEPORT = 91;
        public static final int SPELL_KHARYRLL_TELEPORT = 92;
        public static final int SPELL_LASSAR_TELEPORT = 93;
        public static final int SPELL_DAREEYAK_TELEPORT = 94;
        public static final int SPELL_CARRALLANGAR_TELEPORT = 95;
        public static final int SPELL_ANNAKARL_TELEPORT = 96;
        public static final int SPELL_GHORROCK_TELEPORT = 97;
        public static final int SPELL_EDGEVILLE_HOME_TELEPORT = 98;
        // LUNAR
        public static final int SPELL_LUNAR_HOME_TELEPORT = 99;
        public static final int SPELL_BAKE_PIE = 100;
        public static final int SPELL_CURE_PLANT = 101;
        public static final int SPELL_MONSTER_EXAMINE = 102;
        public static final int SPELL_NPC_CONTACT = 103;
        public static final int SPELL_CURE_OTHER = 104;
        public static final int SPELL_HUMIDIFY = 105;
        public static final int SPELL_MOONCLAN_TELEPORT = 106;
        public static final int SPELL_TELE_GROUP_MOONCLAN = 107;
        public static final int SPELL_CURE_ME = 108;
        public static final int SPELL_HUNTER_KIT = 109;
        public static final int SPELL_WATERBIRTH_TELEPORT = 110;
        public static final int SPELL_TELE_GROUP_WATERBIRTH = 111;
        public static final int SPELL_CURE_GROUP = 112;
        public static final int SPELL_STAT_SPY = 113;
        public static final int SPELL_BARBARIAN_TELEPORT = 114;
        public static final int SPELL_TELE_GROUP_BARBARIAN = 115;
        public static final int SPELL_SUPERGLASS_MAKE = 116;
        public static final int SPELL_TAN_LEATHER = 117;
        public static final int SPELL_KHAZARD_TELEPORT = 118;
        public static final int SPELL_TELE_GROUP_KHAZARD = 119;
        public static final int SPELL_DREAM = 120;
        public static final int SPELL_STRING_JEWELLERY = 121;
        public static final int SPELL_STAT_RESTORE_POT_SHARE = 122;
        public static final int SPELL_MAGIC_IMBUE = 123;
        public static final int SPELL_FERTILE_SOIL = 124;
        public static final int SPELL_BOOST_POTION_SHARE = 125;
        public static final int SPELL_FISHING_GUILD_TELEPORT = 126;
        public static final int SPELL_TELE_GROUP_FISHING_GUILD = 127;
        public static final int SPELL_PLANK_MAKE = 128;
        public static final int SPELL_CATHERBY_TELEPORT = 129;
        public static final int SPELL_TELE_GROUP_CATHERBY = 130;
        public static final int SPELL_RECHARGE_DRAGONSTONE = 131;
        public static final int SPELL_ICE_PLATEAU_TELEPORT = 132;
        public static final int SPELL_TELE_GROUP_ICE_PLATEAU = 133;
        public static final int SPELL_ENERGY_TRANSFER = 134;
        public static final int SPELL_HEAL_OTHER = 135;
        public static final int SPELL_VENGEANCE_OTHER = 136;
        public static final int SPELL_VENGEANCE = 137;
        public static final int SPELL_HEAL_GROUP = 138;
        public static final int SPELL_SPELLBOOK_SWAP = 139;
        public static final int SPELL_GEOMANCY = 140;
        public static final int SPELL_SPIN_FLAX = 141;
        public static final int SPELL_OURANIA_TELEPORT = 142;
        // ARCEUUS
        public static final int SPELL_ARCEUUS_HOME_TELEPORT = 143;
        public static final int SPELL_REANIMATE_GOBLIN = 144;
        public static final int SPELL_LUMBRIDGE_GRAVEYARD_TELEPORT = 145;
        public static final int SPELL_REANIMATE_MONKEY = 146;
        public static final int SPELL_REANIMATE_IMP = 147;
        public static final int SPELL_REANIMATE_MINOTAUR = 148;
        public static final int SPELL_DRAYNOR_MANOR_TELEPORT = 149;
        public static final int SPELL_REANIMATE_SCORPION = 150;
        public static final int SPELL_REANIMATE_BEAR = 151;
        public static final int SPELL_REANIMATE_UNICORN = 152;
        public static final int SPELL_REANIMATE_DOG = 153;
        public static final int SPELL_MIND_ALTAR_TELEPORT = 154;
        public static final int SPELL_REANIMATE_CHAOS_DRUID = 155;
        public static final int SPELL_RESPAWN_TELEPORT = 156;
        public static final int SPELL_REANIMATE_GIANT = 157;
        public static final int SPELL_SALVE_GRAVEYARD_TELEPORT = 158;
        public static final int SPELL_REANIMATE_OGRE = 159;
        public static final int SPELL_REANIMATE_ELF = 160;
        public static final int SPELL_REANIMATE_TROLL = 161;
        public static final int SPELL_FENKENSTRAINS_CASTLE_TELEPORT = 162;
        public static final int SPELL_REANIMATE_HORROR = 163;
        public static final int SPELL_REANIMATE_KALPHITE = 164;
        public static final int SPELL_WEST_ARDOUGNE_TELEPORT = 165;
        public static final int SPELL_REANIMATE_DAGANNOTH = 166;
        public static final int SPELL_REANIMATE_BLOODVELD = 167;
        public static final int SPELL_HARMONY_ISLAND_TELEPORT = 168;
        public static final int SPELL_REANIMATE_TZHAAR = 169;
        public static final int SPELL_CEMETERY_TELEPORT = 170;
        public static final int SPELL_REANIMATE_DEMON = 171;
        public static final int SPELL_REANIMATE_AVIANSIE = 172;
        public static final int SPELL_RESURRECT_CROPS = 173;
        public static final int SPELL_BARROWS_TELEPORT = 174;
        public static final int SPELL_REANIMATE_ABYSSAL_CREATURE = 175;
        public static final int SPELL_APE_ATOLL_TELEPORT_A = 176;
        public static final int SPELL_REANIMATE_DRAGON = 177;
        public static final int SPELL_BATTLEFRONT_TELEPORT = 178;
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
    public static final int INTERFACE_MINIMAP_HEALTH_ORB_TEXT = 5;
    public static final int INTERFACE_MINIMAP_PRAYER_ORB_TEXT = 15;
    public static final int INTERFACE_MINIMAP_RUN_ORB_TEXT = 23;
    public static final int INTERFACE_MINIMAP_SPEC_ORB_TEXT = 31;


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

    public enum Prayers {

        THICK_SKIN(0, 1), BURST_OF_STRENGTH(1, 4), CLARITY_OF_THOUGHT(2, 7), SHARP_EYE(
                3, 8), MYSTIC_WILL(4, 9), ROCK_SKIN(5, 10), SUPERHUMAN_STRENGTH(
                6, 13), IMPROVED_REFLEXES(7, 16), RAPID_RESTORE(8, 19), RAPID_HEAL(
                9, 22), PROTECT_ITEM(10, 25), HAWK_EYE(11, 26), MYSTIC_LORE(12,
                27), STEEL_SKIN(13, 28), ULTIMATE_STRENGTH(
                14, 31), INCREDIBLE_REFLEXES(
                15, 34), PROTECT_FROM_SUMMONING(16, 35), PROTECT_FROM_MAGIC(17,
                37), PROTECT_FROM_MISSILES(18,
                40), PROTECT_FROM_MELEE(
                19, 43), EAGLE_EYE(
                20, 44), MYSTIC_MIGHT(21, 45), RETRIBUTION(22, 46), REDEMPTION(
                23, 49), SMITE(24, 52), CHIVALRY(25, 60), RAPID_RENEWAL(26, 65), PIETY(
                27, 70), RIGOUR(28, 74), AUGURY(29, 77);

        private static final int PRAYER_BOOK_OFFSET = INTERFACE_NORMAL_PRAYERS + 1;

        private final int index;
        private final int level;

        Prayers(int index, int level) {
            this.index = index;
            this.level = level;
        }

        public int getIndex() {
            return PRAYER_BOOK_OFFSET + index;
        }

        public int getRequiredLevel() {
            return level;
        }

    }

}
