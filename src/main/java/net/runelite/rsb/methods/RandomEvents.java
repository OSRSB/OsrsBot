package net.runelite.rsb.methods;

import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.internal.globval.enums.Skill;
import net.runelite.rsb.wrappers.RSItem;
import net.runelite.rsb.wrappers.RSNPC;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.function.BooleanSupplier;

public class RandomEvents extends MethodProvider {

    RandomEvents(MethodContext ctx) {
        super(ctx);
    }

    public boolean isGenieNearby() {
        RSNPC genie = methods.npcs.getNearest("Genie");
        return genie != null && methods.calc.distanceTo(genie) <= 4;
    }

    public void solveGenieLamp(Skill skillToLevel) {
        RSItem genieLamp = methods.inventory.getFirstWithAction("Rub");
        if (genieLamp != null) {
            methods.inventory.open();
            sleep(300);
            genieLamp.doAction("Rub");
            BooleanSupplier genieLampNotNull = () -> methods.client.getWidget(
                    WidgetIndices.GenieLampWindow.GROUP_INDEX,
                    WidgetIndices.GenieLampWindow.PARENT_CONTAINER) != null;
            sleepUntil(genieLampNotNull, random(300, 600));
            switch (skillToLevel) {
                case ATTACK:
                    RSWidget skillAttack = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.ATTACK_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillAttack != null)
                        skillAttack.doClick();
                case STRENGTH:
                    RSWidget skillStrength = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.STRENGHT_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillStrength != null)
                        skillStrength.doClick();
                case DEFENCE:
                    RSWidget skillDefence = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.DEFENSE_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillDefence != null)
                        skillDefence.doClick();
                case RANGED:
                    RSWidget skillRanged = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.RANGED_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillRanged != null)
                        skillRanged.doClick();
                case PRAYER:
                    RSWidget skillPrayer = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.PRAYER_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillPrayer != null)
                        skillPrayer.doClick();
                case MAGIC:
                    RSWidget skillMagic = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.MAGIC_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillMagic != null)
                        skillMagic.doClick();
                case RUNECRAFT:
                    RSWidget skillRunecraft = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.RUNECRAFTING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillRunecraft != null)
                        skillRunecraft.doClick();
                case CONSTRUCTION:
                    RSWidget skillConstruction = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.CONSTRUCTION_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillConstruction != null)
                        skillConstruction.doClick();
                case HITPOINTS:
                    RSWidget skillHitpoints = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.HITPOINTS_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillHitpoints != null)
                        skillHitpoints.doClick();
                case AGILITY:
                    RSWidget skillAgility = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.AGILITY_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillAgility != null)
                        skillAgility.doClick();
                case HERBLORE:
                    RSWidget skillHerblore = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.HERBOLORE_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillHerblore != null)
                        skillHerblore.doClick();
                case THIEVING:
                    RSWidget skillThieving = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.THIEVING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillThieving != null)
                        skillThieving.doClick();
                case CRAFTING:
                    RSWidget skillCrafting = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.CRAFTING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillCrafting != null)
                        skillCrafting.doClick();
                case FLETCHING:
                    RSWidget skillFletching = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.FLETCHING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillFletching != null)
                        skillFletching.doClick();
                case SLAYER:
                    RSWidget skillSlayer = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.SLAYER_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillSlayer != null)
                        skillSlayer.doClick();
                case HUNTER:
                    RSWidget skillHunter = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.HUNTER_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillHunter != null)
                        skillHunter.doClick();
                case MINING:
                    RSWidget skillMining = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.MINING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillMining != null)
                        skillMining.doClick();
                case SMITHING:
                    RSWidget skillSmithing = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.SMITHING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillSmithing != null)
                        skillSmithing.doClick();
                case FISHING:
                    RSWidget skillFishing = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.FISHING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillFishing != null)
                        skillFishing.doClick();
                case COOKING:
                    RSWidget skillCooking = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.COOKING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillCooking != null)
                        skillCooking.doClick();
                case FIREMAKING:
                    RSWidget skillFiremaking = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.FIREMAKING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillFiremaking != null)
                        skillFiremaking.doClick();
                case WOODCUTTING:
                    RSWidget skillWoodcutting = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.WOODCUTTING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillWoodcutting != null)
                        skillWoodcutting.doClick();
                case FARMING:
                    RSWidget skillFarming = methods.interfaces.getComponent(
                            WidgetIndices.GenieLampWindow.GROUP_INDEX,
                            WidgetIndices.GenieLampWindow.FARMING_DYNAMIC_CONTAINER)
                            .getDynamicComponent(9);
                    if (skillFarming != null)
                        skillFarming.doClick();
            }
        }
    }
}
