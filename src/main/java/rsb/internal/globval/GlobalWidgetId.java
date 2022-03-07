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
