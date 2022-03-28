package rsb.internal.globval;

import java.util.HashMap;
import java.util.Map;

public enum VarcValues {
    // Interface Tabs
    TAB_COMBAT_OPTIONS(0),
    TAB_SKILLS(1),
    TAB_QUEST_LIST(2),
    TAB_INVENTORY(3),
    TAB_WORN_EQUIPMENT(4),
    TAB_PRAYER(5),
    TAB_SPELLBOOK(6),
    TAB_CHAT_CHANNEL(7),
    TAB_ACC_MANAGEMENT(8),
    TAB_FRIEND_LIST(9),
    TAB_LOGOUT(10),
    TAB_SETTINGS(11),
    TAB_EMOTES(12),
    TAB_MUSIC(13);

    // cache values on load
    private static final Map<Object, Object> hashMap = new HashMap<>();
    static {
        for (VarcValues varcEnum : VarcValues.values()) {
            hashMap.put(varcEnum.value, varcEnum);
        }
    }

    private final int value;

    VarcValues(int value) {
        this.value = value;
    }

    public static VarcValues valueOf(int varcEnum) {
        return (VarcValues) hashMap.get(varcEnum);
    }

    public int getValue() {
        return value;
    }
}
