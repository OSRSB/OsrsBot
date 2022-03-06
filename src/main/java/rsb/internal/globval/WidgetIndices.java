package rsb.internal.globval;

public class WidgetIndices {
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
     * Describes deposit box widgets
     * Last reviewed: 28/2/2022 2:51 am UTC+1
     */
    public static class DepositBox {
        public static final int GROUP_INDEX = 192;
        public static final int PARENT_CONTAINER = 0;
        public static final int DYNAMIC_CONTAINER = 1; // Contains thick border sprites 0-11
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
}
