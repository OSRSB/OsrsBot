package rsb.internal.globval;

import java.util.HashMap;
import java.util.Map;

public enum VarpValues {
    // Auto retaliate
    AUTO_RETALIATE_ENABLED(0),
    AUTO_RETALIATE_DISABLED(1),
    // Toggle run
    TOGGLE_RUN_DISABLED(0),
    TOGGLE_RUN_ENABLED(1),
    // Special attack
    SPECIAL_ATTACK_DISABLED(0),
    SPECIAL_ATTACK_ENABLED(1),
    // Bank rearrange mode
    BANK_REARRANGE_MODE_SWAP(0),
    BANK_REARRANGE_MODE_INSERT(1);

    // cache values on load
    private static final Map<Object, Object> hashMap = new HashMap<>();
    static {
        for (VarpValues varpEnum : VarpValues.values()) {
            hashMap.put(varpEnum.value, varpEnum);
        }
    }

    private final int value;

    VarpValues(int value) {
        this.value = value;
    }

    public static VarpValues valueOf(int varpEnum) {
        return (VarpValues) hashMap.get(varpEnum);
    }

    public int getValue() {
        return value;
    }
}