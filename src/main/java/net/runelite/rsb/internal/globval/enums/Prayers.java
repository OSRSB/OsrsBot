package net.runelite.rsb.internal.globval.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Prayer;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;

@Getter
public enum Prayers {
    THICK_SKIN(1, WidgetIndices.PrayersTab.THICK_SKIN, Prayer.THICK_SKIN, 5),
    BURST_OF_STRENGTH(4, WidgetIndices.PrayersTab.BURST_OF_STRENGTH, Prayer.BURST_OF_STRENGTH, 5),
    CLARITY_OF_THOUGHT(7, WidgetIndices.PrayersTab.CLARITY_OF_THOUGHT, Prayer.CLARITY_OF_THOUGHT, 5),
    SHARP_EYE(8, WidgetIndices.PrayersTab.SHARP_EYE, Prayer.SHARP_EYE, 5),
    MYSTIC_WILL(9, WidgetIndices.PrayersTab.MYSTIC_WILL, Prayer.MYSTIC_WILL, 5),
    ROCK_SKIN(10, WidgetIndices.PrayersTab.ROCK_SKIN, Prayer.ROCK_SKIN, 5),
    SUPERHUMAN_STRENGTH(13, WidgetIndices.PrayersTab.SUPERHUMAN_STRENGTH, Prayer.SUPERHUMAN_STRENGTH, 10),
    IMPROVED_REFLEXES(16, WidgetIndices.PrayersTab.IMPROVED_REFLEXES, Prayer.IMPROVED_REFLEXES, 10),
    RAPID_RESTORE(19, WidgetIndices.PrayersTab.RAPID_RESTORE, Prayer.RAPID_RESTORE, 60 / 36),
    RAPID_HEAL(22, WidgetIndices.PrayersTab.RAPID_HEAL, Prayer.RAPID_HEAL, 60 / 18),
    PROTECT_ITEM(25, WidgetIndices.PrayersTab.PROTECT_ITEM, Prayer.PROTECT_ITEM, 60 / 18),
    HAWK_EYE(26, WidgetIndices.PrayersTab.HAWK_EYE, Prayer.HAWK_EYE, 10),
    MYSTIC_LORE(27, WidgetIndices.PrayersTab.MYSTIC_LORE, Prayer.MYSTIC_LORE, 10),
    STEEL_SKIN(28, WidgetIndices.PrayersTab.STEEL_SKIN, Prayer.STEEL_SKIN, 20),
    ULTIMATE_STRENGTH(31, WidgetIndices.PrayersTab.ULTIMATE_STRENGTH, Prayer.ULTIMATE_STRENGTH, 20),
    INCREDIBLE_REFLEXES(34, WidgetIndices.PrayersTab.INCREDIBLE_REFLEXES, Prayer.INCREDIBLE_REFLEXES, 20),
    PROTECT_FROM_MAGIC(37, WidgetIndices.PrayersTab.PROTECT_FROM_MAGIC, Prayer.PROTECT_FROM_MAGIC, 20),
    PROTECT_FROM_MISSILES(40, WidgetIndices.PrayersTab.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MISSILES, 20),
    PROTECT_FROM_MELEE(43, WidgetIndices.PrayersTab.PROTECT_FROM_MELEE, Prayer.PROTECT_FROM_MELEE, 20),
    EAGLE_EYE(44, WidgetIndices.PrayersTab.EAGLE_EYE, Prayer.EAGLE_EYE, 20),
    MYSTIC_MIGHT(45, WidgetIndices.PrayersTab.MYSTIC_MIGHT, Prayer.MYSTIC_MIGHT, 20),
    RETRIBUTION(46, WidgetIndices.PrayersTab.RETRIBUTION, Prayer.RETRIBUTION, 20),
    REDEMPTION(49, WidgetIndices.PrayersTab.REDEMPTION, Prayer.REDEMPTION, 5),
    SMITE(52, WidgetIndices.PrayersTab.SMITE, Prayer.SMITE, 30),
    PRESERVE(55, WidgetIndices.PrayersTab.PRESERVE, Prayer.PRESERVE, 60 / 18),
    CHIVALRY(60, WidgetIndices.PrayersTab.CHIVALRY, Prayer.CHIVALRY, 40),
    PIETY(70, WidgetIndices.PrayersTab.PIETY, Prayer.PIETY, 40),
    RIGOUR(74, WidgetIndices.PrayersTab.RIGOUR, Prayer.RIGOUR, 40),
    AUGURY(77, WidgetIndices.PrayersTab.AUGURY, Prayer.AUGURY, 40);

    private final int level;
    private final int prayerChildIndex;
    private final Prayer runelitePrayer;
    private final double drainRate;

    Prayers(int level, int prayerChildIndex, Prayer runelitePrayer, double drainRate) {
        this.level = level;
        this.prayerChildIndex = prayerChildIndex;
        this.runelitePrayer = runelitePrayer;
        this.drainRate = drainRate;
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

    public double drainRate() { return drainRate; };

}