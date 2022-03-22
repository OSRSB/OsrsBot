package rsb.internal.globval;

import java.util.HashMap;
import java.util.Map;

public enum VarpValues {
    AUTO_RETALIATE_ENABLED(0),
    AUTO_RETALIATE_DISABLED(1);

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