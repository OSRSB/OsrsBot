package net.runelite.rsb.internal.globval.enums;

import net.runelite.api.Prayer;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;

public enum Prayers {
    THICK_SKIN(1, WidgetIndices.PrayersTab.THICK_SKIN, Prayer.THICK_SKIN),
    BURST_OF_STRENGTH(4, WidgetIndices.PrayersTab.BURST_OF_STRENGTH, Prayer.BURST_OF_STRENGTH),
    CLARITY_OF_THOUGHT(7, WidgetIndices.PrayersTab.CLARITY_OF_THOUGHT, Prayer.CLARITY_OF_THOUGHT),
    SHARP_EYE(8, WidgetIndices.PrayersTab.SHARP_EYE, Prayer.SHARP_EYE),
    MYSTIC_WILL(9, WidgetIndices.PrayersTab.MYSTIC_WILL, Prayer.MYSTIC_WILL),
    ROCK_SKIN(10, WidgetIndices.PrayersTab.ROCK_SKIN, Prayer.ROCK_SKIN),
    SUPERHUMAN_STRENGTH(13, WidgetIndices.PrayersTab.SUPERHUMAN_STRENGTH, Prayer.SUPERHUMAN_STRENGTH),
    IMPROVED_REFLEXES(16, WidgetIndices.PrayersTab.IMPROVED_REFLEXES, Prayer.IMPROVED_REFLEXES),
    RAPID_RESTORE(19, WidgetIndices.PrayersTab.RAPID_RESTORE, Prayer.RAPID_RESTORE),
    RAPID_HEAL(22, WidgetIndices.PrayersTab.RAPID_HEAL, Prayer.RAPID_HEAL),
    PROTECT_ITEM(25, WidgetIndices.PrayersTab.PROTECT_ITEM, Prayer.PROTECT_ITEM),
    HAWK_EYE(26, WidgetIndices.PrayersTab.HAWK_EYE, Prayer.HAWK_EYE),
    MYSTIC_LORE(27, WidgetIndices.PrayersTab.MYSTIC_LORE, Prayer.MYSTIC_LORE),
    STEEL_SKIN(28, WidgetIndices.PrayersTab.STEEL_SKIN, Prayer.STEEL_SKIN),
    ULTIMATE_STRENGTH(31, WidgetIndices.PrayersTab.ULTIMATE_STRENGTH, Prayer.ULTIMATE_STRENGTH),
    INCREDIBLE_REFLEXES(34, WidgetIndices.PrayersTab.INCREDIBLE_REFLEXES, Prayer.INCREDIBLE_REFLEXES),
    PROTECT_FROM_MAGIC(37, WidgetIndices.PrayersTab.PROTECT_FROM_MAGIC, Prayer.PROTECT_FROM_MAGIC),
    PROTECT_FROM_MISSILES(40, WidgetIndices.PrayersTab.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MISSILES),
    PROTECT_FROM_MELEE(43, WidgetIndices.PrayersTab.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MELEE),
    EAGLE_EYE(44, WidgetIndices.PrayersTab.EAGLE_EYE, Prayer.EAGLE_EYE),
    MYSTIC_MIGHT(45, WidgetIndices.PrayersTab.MYSTIC_MIGHT, Prayer.MYSTIC_MIGHT),
    RETRIBUTION(46, WidgetIndices.PrayersTab.RETRIBUTION, Prayer.RETRIBUTION),
    REDEMPTION(49, WidgetIndices.PrayersTab.REDEMPTION, Prayer.REDEMPTION),
    SMITE(52, WidgetIndices.PrayersTab.SMITE, Prayer.SMITE),
    PRESERVE(55, WidgetIndices.PrayersTab.PRESERVE, Prayer.PRESERVE),
    CHIVALRY(60, WidgetIndices.PrayersTab.CHIVALRY, Prayer.CHIVALRY),
    PIETY(70, WidgetIndices.PrayersTab.PIETY, Prayer.PIETY),
    RIGOUR(74, WidgetIndices.PrayersTab.RIGOUR, Prayer.RIGOUR),
    AUGURY(77, WidgetIndices.PrayersTab.AUGURY, Prayer.AUGURY);

    private final int level;
    private final int prayerChildIndex;
    private final Prayer runelitePrayer;

    Prayers(int level, int prayerChildIndex, Prayer runelitePrayer) {
        this.level = level;
        this.prayerChildIndex = prayerChildIndex;
        this.runelitePrayer = runelitePrayer;
    }

    public int getPrayerChildIndex() {
        return prayerChildIndex;
    }

    public int getRequiredLevel() {
        return level;
    }

    public int getVarbit() {
        return runelitePrayer.getVarbit();
    }

    public double getDrainRate() {
        return runelitePrayer.getDrainRate();
    }
}