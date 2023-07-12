package net.runelite.rsb.internal.globval.enums;


import net.runelite.api.ItemID;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.wrappers.RSWidget;

import static net.runelite.rsb.methods.MethodProvider.methods;

public interface Spell
{
    int getLevel();

    RSWidget getWidget();

    boolean canCast();

    public int getBaseHit();

    public class RuneRequirement
    {
        int quantity;
        Rune rune;

        public RuneRequirement(int quantity, Rune rune) {
            this.quantity = quantity;
            this.rune = rune;
        }

        public boolean meetsRequirements()
        {
            return rune.getQuantity() >= quantity;
        }
    }
}