package net.runelite.rsb.internal.globval.enums;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.rsb.wrappers.RSItem;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static net.runelite.api.Varbits.*;
import static net.runelite.rsb.methods.MethodProvider.methods;

public enum Rune
{
    AIR(ItemID.AIR_RUNE, "Air", "Smoke", "Mist", "Dust"),
    EARTH(ItemID.EARTH_RUNE, "Earth", "Lava", "Mud", "Dust"),
    FIRE(ItemID.FIRE_RUNE, "Fire", "Lava", "Smoke", "Steam"),
    WATER(ItemID.WATER_RUNE, "Water", "Mud", "Steam", "Mist"),
    MIND(ItemID.MIND_RUNE, "Mind"),
    BODY(ItemID.BODY_RUNE, "Body"),
    COSMIC(ItemID.COSMIC_RUNE, "Cosmic"),
    CHAOS(ItemID.CHAOS_RUNE, "Chaos"),
    NATURE(ItemID.NATURE_RUNE, "Nature"),
    LAW(ItemID.LAW_RUNE, "Law"),
    DEATH(ItemID.DEATH_RUNE, "Death"),
    ASTRAL(ItemID.ASTRAL_RUNE, "Astral"),
    BLOOD(ItemID.BLOOD_RUNE, "Blood"),
    SOUL(ItemID.SOUL_RUNE, "Soul"),
    WRATH(ItemID.WRATH_RUNE, "Wrath");

    private final int runeId;
    private final String[] runeNames;

    Rune(int runeId, String... runeNames)
    {
        this.runeId = runeId;
        this.runeNames = runeNames;
    }

    public String[] getRuneNames()
    {
        return runeNames;
    }

    public int getRuneId()
    {
        return runeId;
    }

    public int getQuantity()
    {
        if (isStaffEquipped() || isTomeEquipped())
        {
            return Integer.MAX_VALUE;
        }

        RSItem rune = methods.inventory.query()
                .namedContains("rune")
                .namedContains(runeNames).first();

        if (rune == null)
        {
            return RunePouch.getQuantity(this);
        }

        return rune.getStackSize() + RunePouch.getQuantity(this);
    }

    private boolean isStaffEquipped()
    {
        return !methods.equipment.query()
                .namedContains("staff")
                .namedContains(runeNames)
                .isEmpty();
    }

    private boolean isTomeEquipped()
    {
        return !methods.equipment.query()
                .namedContains("Tome of")
                .namedContains(runeNames)
                .isEmpty();
    }

    @Slf4j
    public class RunePouch
    {
        public enum RuneSlot
        {
            FIRST(RUNE_POUCH_RUNE1, RUNE_POUCH_AMOUNT1),
            SECOND(RUNE_POUCH_RUNE2, RUNE_POUCH_AMOUNT2),
            THIRD(RUNE_POUCH_RUNE3, RUNE_POUCH_AMOUNT3),
            FOURTH(RUNE_POUCH_RUNE4, RUNE_POUCH_AMOUNT4);

            private final int type;
            private final int quantityVarbitIdx;
            RuneSlot(int type, int quantityVarbitIdx)
            {
                this.type = type;
                this.quantityVarbitIdx = quantityVarbitIdx;
            }

            public int getType()
            {
                return type;
            }

            public int getQuantityVarbitIdx()
            {
                return quantityVarbitIdx;
            }

            public int getVarbit()
            {
                return methods.client.getVarbitValue(type);
            }

            public String getRuneName()
            {
                switch (getVarbit())
                {
                    case 1:
                        return "Air rune";
                    case 2:
                        return "Water rune";
                    case 3:
                        return "Earth rune";
                    case 4:
                        return "Fire rune";
                    case 5:
                        return "Mind rune";
                    case 6:
                        return "Chaos rune";
                    case 7:
                        return "Death rune";
                    case 8:
                        return "Blood rune";
                    case 9:
                        return "Cosmic rune";
                    case 10:
                        return "Nature rune";
                    case 11:
                        return "Law rune";
                    case 12:
                        return "Body rune";
                    case 13:
                        return "Soul rune";
                    case 14:
                        return "Astral rune";
                    case 15:
                        return "Mist rune";
                    case 16:
                        return "Mud rune";
                    case 17:
                        return "Dust rune";
                    case 18:
                        return "Lava rune";
                    case 19:
                        return "Steam rune";
                    case 20:
                        return "Smoke rune";
                    default:
                        return null;
                }
            }

            public int getQuantity()
            {
                return methods.client.getVarbitValue(quantityVarbitIdx);
            }
        }

        public static int getQuantity(Rune rune)
        {
            if (!hasPouch())
            {
                return 0;
            }

            RuneSlot runeSlot =
                    Arrays.stream(RuneSlot.values()).filter(x -> Arrays.stream(rune.getRuneNames())
                                    .anyMatch(name -> x.getRuneName() != null && x.getRuneName().startsWith(name)))
                            .findFirst()
                            .orElse(null);

            if (runeSlot == null)
            {
                return 0;
            }

            return runeSlot.getQuantity();
        }

        public static boolean hasPouch()
        {
            return methods.inventory.getItem("Rune pouch", "Divine rune pouch") != null;
        }
    }

}
