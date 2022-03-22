package rsb.internal.globval;

import java.util.HashMap;
import java.util.Map;

public enum VarbitValues {
    // Weapon Type
    WEAPON_TYPE_UNARMED(0),
    WEAPON_TYPE_AXE(1),
    WEAPON_TYPE_BLUNT(2),
    WEAPON_TYPE_BOW(3),
    WEAPON_TYPE_CLAW(4),
    WEAPON_TYPE_CROSSBOW(5),
    WEAPON_TYPE_SALAMANDER(6),
    WEAPON_TYPE_CHINCHOMPAS(7),
    WEAPON_TYPE_UNKNOWN_8(8),
    WEAPON_TYPE_SLASH_SWORD(9),
    WEAPON_TYPE_TWO_HANDED_SWORD(10),
    WEAPON_TYPE_PICKAXE(11),
    WEAPON_TYPE_POLEARM(12),
    WEAPON_TYPE_UNKNOWN_13(13),
    WEAPON_TYPE_SCYTHE(14),
    WEAPON_TYPE_SPEAR(15),
    WEAPON_TYPE_SPIKED(16),
    WEAPON_TYPE_STAB_SWORD(17),
    WEAPON_TYPE_STAFF(18),
    WEAPON_TYPE_THROWN(19),
    WEAPON_TYPE_WHIP(20),
    WEAPON_TYPE_BLADED_STAFF(21),
    WEAPON_TYPE_UNKNOWN_22(22),
    WEAPON_TYPE_POWERED_STAFF(23),
    WEAPON_TYPE_BANNER(24),
    WEAPON_TYPE_UNKNOWN_25(25),
    WEAPON_TYPE_BLUDGEON(26),
    WEAPON_TYPE_BULWARK(27);

    // cache values on load
    private static final Map<Object, Object> hashMap = new HashMap<>();
    static {
        for (VarbitValues varbitEnum : VarbitValues.values()) {
            hashMap.put(varbitEnum.value, varbitEnum);
        }
    }

    private final int value;

    VarbitValues(int value) {
        this.value = value;
    }

    public static VarbitValues valueOf(int varbitEnum) {
        return (VarbitValues) hashMap.get(varbitEnum);
    }

    public int getValue() {
        return value;
    }
}