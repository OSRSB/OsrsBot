package net.runelite.rsb.internal.globval.enums;

import static net.runelite.rsb.methods.MethodProvider.methods;

import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.Arrays;

public enum MagicBook
{
    STANDARD(0),
    ANCIENT(1),
    LUNAR(2),
    NECROMANCY(3);

    private static final int SPELLBOOK_VARBIT = 4070;

    private final int varbitValue;

    MagicBook(int varbitValue)
    {
        this.varbitValue = varbitValue;
    }

    public static MagicBook getCurrent()
    {
        return Arrays.stream(values()).filter(x -> methods.client.getVarbitValue(SPELLBOOK_VARBIT) == x.varbitValue)
                .findFirst().orElse(null);
    }

    public enum Standard implements Spell
    {
        // Teleport spells
        HOME_TELEPORT(
                0,
                GlobalWidgetInfo.SPELL_LUMBRIDGE_HOME_TELEPORT,
                false
        ),
        VARROCK_TELEPORT(
                25,
                GlobalWidgetInfo.SPELL_VARROCK_TELEPORT,
                false,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(1, Rune.FIRE),
                new RuneRequirement(1, Rune.LAW)
        ),
        LUMBRIDGE_TELEPORT(
                31,
                GlobalWidgetInfo.SPELL_LUMBRIDGE_TELEPORT,
                false,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(1, Rune.EARTH),
                new RuneRequirement(1, Rune.LAW)
        ),
        FALADOR_TELEPORT(
                37,
                GlobalWidgetInfo.SPELL_FALADOR_TELEPORT,
                false,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(1, Rune.WATER),
                new RuneRequirement(1, Rune.LAW)
        ),
        TELEPORT_TO_HOUSE(
                40,
                GlobalWidgetInfo.SPELL_TELEPORT_TO_HOUSE,
                true,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(1, Rune.EARTH),
                new RuneRequirement(1, Rune.LAW)
        ),
        CAMELOT_TELEPORT(
                45,
                GlobalWidgetInfo.SPELL_CAMELOT_TELEPORT,
                true,
                new RuneRequirement(5, Rune.AIR),
                new RuneRequirement(1, Rune.LAW)
        ),
        ARDOUGNE_TELEPORT(
                51,
                GlobalWidgetInfo.SPELL_ARDOUGNE_TELEPORT,
                true,
                new RuneRequirement(2, Rune.WATER),
                new RuneRequirement(2, Rune.LAW)
        ),
        WATCHTOWER_TELEPORT(
                58,
                GlobalWidgetInfo.SPELL_WATCHTOWER_TELEPORT,
                true,
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(2, Rune.LAW)
        ),
        TROLLHEIM_TELEPORT(
                61,
                GlobalWidgetInfo.SPELL_TROLLHEIM_TELEPORT,
                true,
                new RuneRequirement(2, Rune.FIRE),
                new RuneRequirement(2, Rune.LAW)
        ),
        APE_ATOLL_TELEPORT_STANDARD(
                64,
                GlobalWidgetInfo.SPELL_APE_ATOLL_TELEPORT_STANDARD,
                true,
                new RuneRequirement(2, Rune.FIRE),
                new RuneRequirement(2, Rune.WATER),
                new RuneRequirement(2, Rune.LAW)
        ),
        KOUREND_CASTLE_TELEPORT(
                69,
                GlobalWidgetInfo.SPELL_KOUREND_CASTLE_TELEPORT,
                true,
                new RuneRequirement(5, Rune.FIRE),
                new RuneRequirement(4, Rune.WATER),
                new RuneRequirement(2, Rune.SOUL),
                new RuneRequirement(2, Rune.LAW)
        ),
        TELEOTHER_LUMBRIDGE(
                74,
                GlobalWidgetInfo.SPELL_TELEOTHER_LUMBRIDGE,
                true,
                new RuneRequirement(1, Rune.EARTH),
                new RuneRequirement(1, Rune.LAW),
                new RuneRequirement(1, Rune.SOUL)
        ),
        TELEOTHER_FALADOR(
                82,
                GlobalWidgetInfo.SPELL_TELEOTHER_FALADOR,
                true,
                new RuneRequirement(1, Rune.WATER),
                new RuneRequirement(1, Rune.LAW),
                new RuneRequirement(1, Rune.SOUL)
        ),
        TELEOTHER_CAMELOT(
                90,
                GlobalWidgetInfo.SPELL_TELEOTHER_CAMELOT,
                true,
                new RuneRequirement(1, Rune.LAW),
                new RuneRequirement(2, Rune.SOUL)
        ),

        // Strike spells
        WIND_STRIKE(
                1,
                2,
                GlobalWidgetInfo.SPELL_WIND_STRIKE,
                false,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(1, Rune.MIND)
        ),
        WATER_STRIKE(
                5,
                4,
                GlobalWidgetInfo.SPELL_WATER_STRIKE,
                false,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(1, Rune.WATER),
                new RuneRequirement(1, Rune.MIND)
        ),
        EARTH_STRIKE(
                9,
                6,
                GlobalWidgetInfo.SPELL_EARTH_STRIKE,
                false,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(1, Rune.MIND)
        ),
        FIRE_STRIKE(
                13,
                8,
                GlobalWidgetInfo.SPELL_FIRE_STRIKE,
                false,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(3, Rune.FIRE),
                new RuneRequirement(1, Rune.MIND)
        ),

        // Bolt spells
        WIND_BOLT(
                17,
                9,
                GlobalWidgetInfo.SPELL_WIND_BOLT,
                false,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(1, Rune.CHAOS)
        ),
        WATER_BOLT(
                23,
                10,
                GlobalWidgetInfo.SPELL_WATER_BOLT,
                false,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(2, Rune.WATER),
                new RuneRequirement(1, Rune.CHAOS)
        ),
        EARTH_BOLT(
                29,
                11,
                GlobalWidgetInfo.SPELL_EARTH_BOLT,
                false,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(3, Rune.EARTH),
                new RuneRequirement(1, Rune.CHAOS)
        ),
        FIRE_BOLT(
                35,
                12,
                GlobalWidgetInfo.SPELL_FIRE_BOLT,
                false,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(4, Rune.FIRE),
                new RuneRequirement(1, Rune.CHAOS)
        ),

        // Blast spells
        WIND_BLAST(
                41,
                13,
                GlobalWidgetInfo.SPELL_WIND_BLAST,
                false,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(1, Rune.DEATH)
        ),
        WATER_BLAST(
                47,
                14,
                GlobalWidgetInfo.SPELL_WATER_BLAST,
                false,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(3, Rune.WATER),
                new RuneRequirement(1, Rune.DEATH)
        ),
        EARTH_BLAST(
                53,
                15,
                GlobalWidgetInfo.SPELL_EARTH_BLAST,
                false,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(4, Rune.EARTH),
                new RuneRequirement(1, Rune.DEATH)
        ),
        FIRE_BLAST(
                59,
                16,
                GlobalWidgetInfo.SPELL_FIRE_BLAST,
                false,
                new RuneRequirement(4, Rune.AIR),
                new RuneRequirement(5, Rune.FIRE),
                new RuneRequirement(1, Rune.DEATH)
        ),

        // Wave spells
        WIND_WAVE(
                62,
                17,
                GlobalWidgetInfo.SPELL_WIND_WAVE,
                true,
                new RuneRequirement(5, Rune.AIR),
                new RuneRequirement(1, Rune.BLOOD)
        ),
        WATER_WAVE(
                65,
                18,
                GlobalWidgetInfo.SPELL_WATER_WAVE,
                true,
                new RuneRequirement(5, Rune.AIR),
                new RuneRequirement(7, Rune.WATER),
                new RuneRequirement(1, Rune.BLOOD)
        ),
        EARTH_WAVE(
                70,
                19,
                GlobalWidgetInfo.SPELL_EARTH_WAVE,
                true,
                new RuneRequirement(5, Rune.AIR),
                new RuneRequirement(7, Rune.EARTH),
                new RuneRequirement(1, Rune.BLOOD)
        ),
        FIRE_WAVE(
                75,
                20,
                GlobalWidgetInfo.SPELL_FIRE_WAVE,
                true,
                new RuneRequirement(5, Rune.AIR),
                new RuneRequirement(7, Rune.FIRE),
                new RuneRequirement(1, Rune.BLOOD)
        ),

        // Surge spells
        WIND_SURGE(
                81,
                21,
                GlobalWidgetInfo.SPELL_WIND_SURGE,
                true,
                new RuneRequirement(7, Rune.AIR),
                new RuneRequirement(1, Rune.WRATH)
        ),
        WATER_SURGE(
                85,
                22,
                GlobalWidgetInfo.SPELL_WATER_SURGE,
                true,
                new RuneRequirement(7, Rune.AIR),
                new RuneRequirement(10, Rune.WATER),
                new RuneRequirement(1, Rune.WRATH)
        ),
        EARTH_SURGE(
                90,
                23,
                GlobalWidgetInfo.SPELL_EARTH_SURGE,
                true,
                new RuneRequirement(7, Rune.AIR),
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(1, Rune.WRATH)
        ),
        FIRE_SURGE(
                95,
                24,
                GlobalWidgetInfo.SPELL_FIRE_SURGE,
                true,
                new RuneRequirement(7, Rune.AIR),
                new RuneRequirement(10, Rune.FIRE),
                new RuneRequirement(1, Rune.WRATH)
        ),

        // God spells
        SARADOMIN_STRIKE(
                60,
                20,
                GlobalWidgetInfo.SPELL_SARADOMIN_STRIKE,
                true,
                new RuneRequirement(4, Rune.AIR),
                new RuneRequirement(2, Rune.FIRE),
                new RuneRequirement(2, Rune.BLOOD)
        ),
        CLAWS_OF_GUTHIX(
                60,
                20,
                GlobalWidgetInfo.SPELL_CLAWS_OF_GUTHIX,
                true,
                new RuneRequirement(4, Rune.AIR),
                new RuneRequirement(1, Rune.FIRE),
                new RuneRequirement(2, Rune.BLOOD)
        ),
        FLAMES_OF_ZAMORAK(
                60,
                20,
                GlobalWidgetInfo.SPELL_FLAMES_OF_ZAMORAK,
                true,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(4, Rune.FIRE),
                new RuneRequirement(2, Rune.BLOOD)
        ),

        // Other combat spells
        CRUMBLE_UNDEAD(
                39,
                15,
                GlobalWidgetInfo.SPELL_CRUMBLE_UNDEAD,
                false,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(1, Rune.CHAOS)
        ),
        IBAN_BLAST(
                50,
                25,
                GlobalWidgetInfo.SPELL_IBAN_BLAST,
                true,
                new RuneRequirement(5, Rune.FIRE),
                new RuneRequirement(1, Rune.DEATH)
        ),
        MAGIC_DART(
                50,
                GlobalWidgetInfo.SPELL_MAGIC_DART,
                true,
                new RuneRequirement(1, Rune.DEATH),
                new RuneRequirement(4, Rune.MIND)
        ),

        // Curse spells
        CONFUSE(
                3,
                GlobalWidgetInfo.SPELL_CONFUSE,
                false,
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(3, Rune.WATER),
                new RuneRequirement(1, Rune.BODY)
        ),
        WEAKEN(
                11,
                GlobalWidgetInfo.SPELL_WEAKEN,
                false,
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(3, Rune.WATER),
                new RuneRequirement(1, Rune.BODY)
        ),
        CURSE(
                19,
                GlobalWidgetInfo.SPELL_CURSE,
                false,
                new RuneRequirement(3, Rune.EARTH),
                new RuneRequirement(2, Rune.WATER),
                new RuneRequirement(1, Rune.BODY)
        ),
        BIND(
                20,
                GlobalWidgetInfo.SPELL_BIND,
                false,
                new RuneRequirement(3, Rune.EARTH),
                new RuneRequirement(3, Rune.WATER),
                new RuneRequirement(2, Rune.NATURE)
        ),
        SNARE(
                50,
                GlobalWidgetInfo.SPELL_SNARE,
                false,
                new RuneRequirement(4, Rune.EARTH),
                new RuneRequirement(4, Rune.WATER),
                new RuneRequirement(3, Rune.NATURE)
        ),
        VULNERABILITY(
                66,
                GlobalWidgetInfo.SPELL_VULNERABILITY,
                true,
                new RuneRequirement(5, Rune.EARTH),
                new RuneRequirement(5, Rune.WATER),
                new RuneRequirement(1, Rune.SOUL)
        ),
        ENFEEBLE(
                73,
                GlobalWidgetInfo.SPELL_ENFEEBLE,
                true,
                new RuneRequirement(8, Rune.EARTH),
                new RuneRequirement(8, Rune.WATER),
                new RuneRequirement(1, Rune.SOUL)
        ),
        ENTANGLE(
                79,
                GlobalWidgetInfo.SPELL_ENTANGLE,
                true,
                new RuneRequirement(5, Rune.EARTH),
                new RuneRequirement(5, Rune.WATER),
                new RuneRequirement(4, Rune.NATURE)
        ),
        STUN(
                80,
                GlobalWidgetInfo.SPELL_STUN,
                true,
                new RuneRequirement(12, Rune.EARTH),
                new RuneRequirement(12, Rune.WATER),
                new RuneRequirement(1, Rune.SOUL)
        ),
        TELE_BLOCK(
                85,
                GlobalWidgetInfo.SPELL_TELE_BLOCK,
                false,
                new RuneRequirement(1, Rune.CHAOS),
                new RuneRequirement(1, Rune.DEATH),
                new RuneRequirement(1, Rune.LAW)
        ),

        // Support spells
        CHARGE(
                80,
                GlobalWidgetInfo.SPELL_CHARGE,
                true,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(3, Rune.FIRE),
                new RuneRequirement(3, Rune.BLOOD)
        ),

        // Utility spells
        BONES_TO_BANANAS(
                15,
                GlobalWidgetInfo.SPELL_BONES_TO_BANANAS,
                false,
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(2, Rune.WATER),
                new RuneRequirement(1, Rune.NATURE)
        ),
        LOW_LEVEL_ALCHEMY(
                21,
                GlobalWidgetInfo.SPELL_LOW_LEVEL_ALCHEMY,
                false,
                new RuneRequirement(3, Rune.FIRE),
                new RuneRequirement(1, Rune.NATURE)
        ),
        SUPERHEAT_ITEM(
                43,
                GlobalWidgetInfo.SPELL_SUPERHEAT_ITEM,
                false,
                new RuneRequirement(4, Rune.FIRE),
                new RuneRequirement(1, Rune.NATURE)
        ),
        HIGH_LEVEL_ALCHEMY(
                55,
                GlobalWidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY,
                false,
                new RuneRequirement(5, Rune.FIRE),
                new RuneRequirement(1, Rune.NATURE)
        ),
        BONES_TO_PEACHES(
                60,
                GlobalWidgetInfo.SPELL_BONES_TO_PEACHES,
                true,
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(4, Rune.WATER),
                new RuneRequirement(2, Rune.NATURE)
        ),

        // Enchantment spells
        LVL_1_ENCHANT(
                7,
                GlobalWidgetInfo.SPELL_LVL_1_ENCHANT,
                false,
                new RuneRequirement(1, Rune.WATER),
                new RuneRequirement(1, Rune.COSMIC)
        ),
        LVL_2_ENCHANT(
                27,
                GlobalWidgetInfo.SPELL_LVL_2_ENCHANT,
                false,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(1, Rune.COSMIC)
        ),
        LVL_3_ENCHANT(
                49,
                GlobalWidgetInfo.SPELL_LVL_3_ENCHANT,
                false,
                new RuneRequirement(5, Rune.FIRE),
                new RuneRequirement(1, Rune.COSMIC)
        ),
        CHARGE_WATER_ORB(
                56,
                GlobalWidgetInfo.SPELL_CHARGE_WATER_ORB,
                true,
                new RuneRequirement(30, Rune.WATER),
                new RuneRequirement(3, Rune.COSMIC)
        ),
        LVL_4_ENCHANT(
                57,
                GlobalWidgetInfo.SPELL_LVL_4_ENCHANT,
                false,
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(1, Rune.COSMIC)
        ),
        CHARGE_EARTH_ORB(
                60,
                GlobalWidgetInfo.SPELL_CHARGE_EARTH_ORB,
                true,
                new RuneRequirement(30, Rune.EARTH),
                new RuneRequirement(3, Rune.COSMIC)
        ),
        CHARGE_FIRE_ORB(
                63,
                GlobalWidgetInfo.SPELL_CHARGE_FIRE_ORB,
                true,
                new RuneRequirement(30, Rune.FIRE),
                new RuneRequirement(3, Rune.COSMIC)
        ),
        CHARGE_AIR_ORB(
                66,
                GlobalWidgetInfo.SPELL_CHARGE_AIR_ORB,
                true,
                new RuneRequirement(30, Rune.AIR),
                new RuneRequirement(3, Rune.COSMIC)
        ),
        LVL_5_ENCHANT(
                68,
                GlobalWidgetInfo.SPELL_LVL_5_ENCHANT,
                true,
                new RuneRequirement(15, Rune.EARTH),
                new RuneRequirement(15, Rune.WATER),
                new RuneRequirement(1, Rune.COSMIC)
        ),
        LVL_6_ENCHANT(
                87,
                GlobalWidgetInfo.SPELL_LVL_6_ENCHANT,
                true,
                new RuneRequirement(20, Rune.EARTH),
                new RuneRequirement(20, Rune.FIRE),
                new RuneRequirement(1, Rune.COSMIC)
        ),
        LVL_7_ENCHANT(
                93,
                GlobalWidgetInfo.SPELL_LVL_7_ENCHANT,
                true,
                new RuneRequirement(20, Rune.BLOOD),
                new RuneRequirement(20, Rune.SOUL),
                new RuneRequirement(1, Rune.COSMIC)
        ),

        // Other spells
        TELEKINETIC_GRAB(
                31,
                GlobalWidgetInfo.SPELL_TELEKINETIC_GRAB,
                false,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(1, Rune.LAW)
        ),
        ;

        private final int level;
        private final GlobalWidgetInfo widgetInfo;
        private final boolean members;
        private final RuneRequirement[] requirements;

        private final int baseHit;

        Standard(int level, GlobalWidgetInfo widgetInfo, boolean members, RuneRequirement... requirements)
        {
            this(level, -1, widgetInfo, members, requirements);
        }

        Standard(int level, int baseHit, GlobalWidgetInfo widgetInfo, boolean members, RuneRequirement... requirements)
        {
            this.level = level;
            this.baseHit = baseHit;
            this.widgetInfo = widgetInfo;
            this.members = members;
            this.requirements = requirements;
        }

        @Override
        public int getLevel()
        {
            return level;
        }

        @Override
        public RSWidget getWidget()
        {
            return methods.interfaces.getComponent(widgetInfo);
        }

        @Override
        public boolean canCast()
        {
            if (getCurrent() != STANDARD)
            {
                return false;
            }

            if (members && !methods.worldHopper.isCurrentWorldMembers())
            {
                return false;
            }

            if (this == HOME_TELEPORT)
            {
                return methods.magic.isHomeTeleportOnCooldown();
            }

            if (level > methods.skills.getRealLevel(Skill.MAGIC) || level > methods.skills.getCurrentLevel(Skill.MAGIC))
            {
                return false;
            }

            if (this == ARDOUGNE_TELEPORT && methods.client.getVarpValue(165) < 30)
            {
                return false;
            }

            if (this == TROLLHEIM_TELEPORT && methods.client.getVarpValue(335) < 110)
            {
                return false;
            }

            return haveEquipment() && haveItem() && haveRunesAvailable();
        }

        public boolean haveRunesAvailable()
        {
            for (RuneRequirement req : requirements)
            {
                if (!req.meetsRequirements())
                {
                    return false;
                }
            }

            return true;
        }

        public int getBaseHit() {
            return baseHit;
        }

        public boolean haveEquipment()
        {
            switch (this)
            {
                case IBAN_BLAST:
                    return methods.equipment.containsOneOf(ItemID.IBANS_STAFF, ItemID.IBANS_STAFF_1410, ItemID.IBANS_STAFF_U);
                case MAGIC_DART:
                    return methods.equipment.containsOneOf(ItemID.SLAYERS_STAFF_E, ItemID.SLAYERS_STAFF, ItemID.STAFF_OF_THE_DEAD, ItemID.STAFF_OF_THE_DEAD_23613, ItemID.TOXIC_STAFF_OF_THE_DEAD, ItemID.STAFF_OF_LIGHT, ItemID.STAFF_OF_BALANCE);
                case SARADOMIN_STRIKE:
                    return methods.equipment.containsOneOf(ItemID.SARADOMIN_STAFF, ItemID.STAFF_OF_LIGHT);
                case FLAMES_OF_ZAMORAK:
                    return methods.equipment.containsOneOf(ItemID.ZAMORAK_STAFF, ItemID.STAFF_OF_THE_DEAD, ItemID.STAFF_OF_THE_DEAD_23613, ItemID.TOXIC_STAFF_OF_THE_DEAD);
                case CLAWS_OF_GUTHIX:
                    return methods.equipment.containsOneOf(ItemID.GUTHIX_STAFF, ItemID.VOID_KNIGHT_MACE, ItemID.STAFF_OF_BALANCE);
                default:
                    return true;
            }
        }

        public boolean haveItem()
        {
            switch (this)
            {
                case APE_ATOLL_TELEPORT_STANDARD:
                    return methods.inventory.contains(ItemID.BANANA);
                case CHARGE_AIR_ORB:
                case CHARGE_WATER_ORB:
                case CHARGE_EARTH_ORB:
                case CHARGE_FIRE_ORB:
                    return methods.inventory.contains(ItemID.UNPOWERED_ORB);
                default:
                    return true;
            }
        }
    }

    public enum Ancient implements Spell
    {
        // Teleport spells
        EDGEVILLE_HOME_TELEPORT(
                0,
                GlobalWidgetInfo.SPELL_EDGEVILLE_HOME_TELEPORT
        ),
        PADDEWWA_TELEPORT(
                54,
                GlobalWidgetInfo.SPELL_PADDEWWA_TELEPORT,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(1, Rune.FIRE),
                new RuneRequirement(2, Rune.LAW)
        ),
        SENNTISTEN_TELEPORT(
                60,
                GlobalWidgetInfo.SPELL_SENNTISTEN_TELEPORT,
                new RuneRequirement(2, Rune.LAW),
                new RuneRequirement(1, Rune.SOUL)
        ),
        KHARYRLL_TELEPORT(
                66,
                GlobalWidgetInfo.SPELL_KHARYRLL_TELEPORT,
                new RuneRequirement(2, Rune.LAW),
                new RuneRequirement(1, Rune.BLOOD)
        ),
        LASSAR_TELEPORT(
                72,
                GlobalWidgetInfo.SPELL_LASSAR_TELEPORT,
                new RuneRequirement(4, Rune.WATER),
                new RuneRequirement(2, Rune.LAW)
        ),
        DAREEYAK_TELEPORT(
                78,
                GlobalWidgetInfo.SPELL_DAREEYAK_TELEPORT,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(3, Rune.FIRE),
                new RuneRequirement(2, Rune.LAW)
        ),
        CARRALLANGER_TELEPORT(
                84,
                GlobalWidgetInfo.SPELL_CARRALLANGER_TELEPORT,
                new RuneRequirement(2, Rune.LAW),
                new RuneRequirement(2, Rune.SOUL)
        ),
        ANNAKARL_TELEPORT(
                90,
                GlobalWidgetInfo.SPELL_ANNAKARL_TELEPORT,
                new RuneRequirement(2, Rune.LAW),
                new RuneRequirement(2, Rune.BLOOD)
        ),
        GHORROCK_TELEPORT(
                96,
                GlobalWidgetInfo.SPELL_GHORROCK_TELEPORT,
                new RuneRequirement(8, Rune.WATER),
                new RuneRequirement(2, Rune.LAW)
        ),

        // Rush Spells
        SMOKE_RUSH(
                50,
                13,
                GlobalWidgetInfo.SPELL_SMOKE_RUSH,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(1, Rune.FIRE),
                new RuneRequirement(2, Rune.CHAOS),
                new RuneRequirement(2, Rune.DEATH)
        ),
        SHADOW_RUSH(
                52,
                14,
                GlobalWidgetInfo.SPELL_SHADOW_RUSH,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(2, Rune.CHAOS),
                new RuneRequirement(2, Rune.DEATH),
                new RuneRequirement(1, Rune.SOUL)
        ),
        BLOOD_RUSH(
                56,
                15,
                GlobalWidgetInfo.SPELL_BLOOD_RUSH,
                new RuneRequirement(2, Rune.CHAOS),
                new RuneRequirement(2, Rune.DEATH),
                new RuneRequirement(1, Rune.BLOOD)
        ),
        ICE_RUSH(
                58,
                16,
                GlobalWidgetInfo.SPELL_ICE_RUSH,
                new RuneRequirement(2, Rune.WATER),
                new RuneRequirement(2, Rune.CHAOS),
                new RuneRequirement(2, Rune.DEATH)
        ),

        // Burst Spells
        SMOKE_BURST(
                62,
                17,
                GlobalWidgetInfo.SPELL_SMOKE_BURST,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(2, Rune.FIRE),
                new RuneRequirement(4, Rune.CHAOS),
                new RuneRequirement(2, Rune.DEATH)
        ),
        SHADOW_BURST(
                64,
                18,
                GlobalWidgetInfo.SPELL_SHADOW_BURST,
                new RuneRequirement(1, Rune.AIR),
                new RuneRequirement(4, Rune.CHAOS),
                new RuneRequirement(2, Rune.DEATH),
                new RuneRequirement(2, Rune.SOUL)
        ),
        BLOOD_BURST(
                68,
                21,
                GlobalWidgetInfo.SPELL_BLOOD_BURST,
                new RuneRequirement(2, Rune.CHAOS),
                new RuneRequirement(4, Rune.DEATH),
                new RuneRequirement(2, Rune.BLOOD)
        ),
        ICE_BURST(
                70,
                22,
                GlobalWidgetInfo.SPELL_ICE_BURST,
                new RuneRequirement(4, Rune.WATER),
                new RuneRequirement(4, Rune.CHAOS),
                new RuneRequirement(2, Rune.DEATH)
        ),

        // Blitz Spells
        SMOKE_BLITZ(
                74,
                23,
                GlobalWidgetInfo.SPELL_SMOKE_BLITZ,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(2, Rune.FIRE),
                new RuneRequirement(2, Rune.DEATH),
                new RuneRequirement(2, Rune.BLOOD)
        ),
        SHADOW_BLITZ(
                76,
                24,
                GlobalWidgetInfo.SPELL_SHADOW_BLITZ,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(2, Rune.DEATH),
                new RuneRequirement(2, Rune.BLOOD),
                new RuneRequirement(2, Rune.SOUL)
        ),
        BLOOD_BLITZ(
                80,
                25,
                GlobalWidgetInfo.SPELL_BLOOD_BLITZ,
                new RuneRequirement(2, Rune.DEATH),
                new RuneRequirement(4, Rune.BLOOD)
        ),
        ICE_BLITZ(
                82,
                26,
                GlobalWidgetInfo.SPELL_ICE_BLITZ,
                new RuneRequirement(3, Rune.WATER),
                new RuneRequirement(2, Rune.DEATH),
                new RuneRequirement(2, Rune.BLOOD)
        ),

        // Barrage Spells
        SMOKE_BARRAGE(
                86,
                27,
                GlobalWidgetInfo.SPELL_SMOKE_BARRAGE,
                new RuneRequirement(4, Rune.AIR),
                new RuneRequirement(4, Rune.FIRE),
                new RuneRequirement(4, Rune.DEATH),
                new RuneRequirement(2, Rune.BLOOD)
        ),
        SHADOW_BARRAGE(
                88,
                28,
                GlobalWidgetInfo.SPELL_SHADOW_BARRAGE,
                new RuneRequirement(4, Rune.AIR),
                new RuneRequirement(4, Rune.DEATH),
                new RuneRequirement(2, Rune.BLOOD),
                new RuneRequirement(3, Rune.SOUL)
        ),
        BLOOD_BARRAGE(
                92,
                29,
                GlobalWidgetInfo.SPELL_BLOOD_BARRAGE,
                new RuneRequirement(4, Rune.DEATH),
                new RuneRequirement(4, Rune.BLOOD),
                new RuneRequirement(1, Rune.SOUL)
        ),
        ICE_BARRAGE(
                94,
                30,
                GlobalWidgetInfo.SPELL_ICE_BARRAGE,
                new RuneRequirement(6, Rune.WATER),
                new RuneRequirement(4, Rune.DEATH),
                new RuneRequirement(2, Rune.BLOOD)
        );

        private final int level;
        private final GlobalWidgetInfo widgetInfo;
        private final RuneRequirement[] requirements;

        private final int baseHit;

        Ancient(int level, GlobalWidgetInfo widgetInfo, RuneRequirement... requirements)
        {
            this(level, -1, widgetInfo, requirements);
        }
        Ancient(int level, int baseHit, GlobalWidgetInfo widgetInfo, RuneRequirement... requirements)
        {
            this.level = level;
            this.baseHit = baseHit;
            this.widgetInfo = widgetInfo;
            this.requirements = requirements;
        }

        @Override
        public int getLevel()
        {
            return level;
        }

        @Override
        public RSWidget getWidget()
        {
            return methods.interfaces.getComponent(widgetInfo);
        }

        public boolean canCast()
        {
            if (getCurrent() != ANCIENT)
            {
                return false;
            }

            if (!methods.worldHopper.isCurrentWorldMembers())
            {
                return false;
            }

            if (this == EDGEVILLE_HOME_TELEPORT)
            {
                return methods.magic.isHomeTeleportOnCooldown();
            }

            if (level > methods.skills.getRealLevel(Skill.MAGIC) || level > methods.skills.getCurrentLevel(Skill.MAGIC))
            {
                return false;
            }

            return haveRunesAvailable();
        }

        public boolean haveRunesAvailable()
        {
            for (RuneRequirement req : requirements)
            {
                if (!req.meetsRequirements())
                {
                    return false;
                }
            }

            return true;
        }

        public int getBaseHit() {
            return baseHit;
        }
    }

    public enum Lunar implements Spell {
        // Teleport spells
        LUNAR_HOME_TELEPORT(
                0,
                GlobalWidgetInfo.SPELL_LUNAR_HOME_TELEPORT
        ),
        MOONCLAN_TELEPORT(
                69,
                GlobalWidgetInfo.SPELL_MOONCLAN_TELEPORT,
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(1, Rune.LAW)
        ),
        TELE_GROUP_MOONCLAN(
                70,
                GlobalWidgetInfo.SPELL_TELE_GROUP_MOONCLAN,
                new RuneRequirement(4, Rune.EARTH),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(1, Rune.LAW)
        ),
        OURANIA_TELEPORT(
                71,
                GlobalWidgetInfo.SPELL_OURANIA_TELEPORT,
                new RuneRequirement(6, Rune.EARTH),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(1, Rune.LAW)
        ),
        WATERBIRTH_TELEPORT(
                72,
                GlobalWidgetInfo.SPELL_WATERBIRTH_TELEPORT,
                new RuneRequirement(1, Rune.WATER),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(1, Rune.LAW)
        ),
        TELE_GROUP_WATERBIRTH(
                73,
                GlobalWidgetInfo.SPELL_TELE_GROUP_WATERBIRTH,
                new RuneRequirement(5, Rune.WATER),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(1, Rune.LAW)
        ),
        BARBARIAN_TELEPORT(
                75,
                GlobalWidgetInfo.SPELL_BARBARIAN_TELEPORT,
                new RuneRequirement(3, Rune.FIRE),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(2, Rune.LAW)
        ),
        TELE_GROUP_BARBARIAN(
                76,
                GlobalWidgetInfo.SPELL_TELE_GROUP_BARBARIAN,
                new RuneRequirement(6, Rune.FIRE),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(2, Rune.LAW)
        ),
        KHAZARD_TELEPORT(
                78,
                GlobalWidgetInfo.SPELL_KHAZARD_TELEPORT,
                new RuneRequirement(4, Rune.WATER),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(2, Rune.LAW)
        ),
        TELE_GROUP_KHAZARD(
                79,
                GlobalWidgetInfo.SPELL_TELE_GROUP_KHAZARD,
                new RuneRequirement(8, Rune.WATER),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(2, Rune.LAW)
        ),
        FISHING_GUILD_TELEPORT(
                85,
                GlobalWidgetInfo.SPELL_FISHING_GUILD_TELEPORT,
                new RuneRequirement(10, Rune.WATER),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(3, Rune.LAW)
        ),
        TELE_GROUP_FISHING_GUILD(
                86,
                GlobalWidgetInfo.SPELL_TELE_GROUP_FISHING_GUILD,
                new RuneRequirement(14, Rune.WATER),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(3, Rune.LAW)
        ),
        CATHERBY_TELEPORT(
                87,
                GlobalWidgetInfo.SPELL_CATHERBY_TELEPORT,
                new RuneRequirement(10, Rune.WATER),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(3, Rune.LAW)
        ),
        TELE_GROUP_CATHERBY(
                88,
                GlobalWidgetInfo.SPELL_TELE_GROUP_CATHERBY,
                new RuneRequirement(15, Rune.WATER),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(3, Rune.LAW)
        ),
        ICE_PLATEAU_TELEPORT(
                89,
                GlobalWidgetInfo.SPELL_ICE_PLATEAU_TELEPORT,
                new RuneRequirement(8, Rune.WATER),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(3, Rune.LAW)
        ),
        TELE_GROUP_ICE_PLATEAU(
                90,
                GlobalWidgetInfo.SPELL_TELE_GROUP_ICE_PLATEAU,
                new RuneRequirement(16, Rune.WATER),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(3, Rune.LAW)
        ),

        // Combat spells
        MONSTER_EXAMINE(
                66,
                GlobalWidgetInfo.SPELL_MONSTER_EXAMINE,
                new RuneRequirement(1, Rune.MIND),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(1, Rune.ASTRAL)
        ),
        CURE_OTHER(
                66,
                GlobalWidgetInfo.SPELL_CURE_OTHER,
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(1, Rune.ASTRAL),
                new RuneRequirement(1, Rune.LAW)
        ),
        CURE_ME(
                66,
                GlobalWidgetInfo.SPELL_CURE_ME,
                new RuneRequirement(2, Rune.COSMIC),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(1, Rune.LAW)
        ),
        CURE_GROUP(
                66,
                GlobalWidgetInfo.SPELL_CURE_GROUP,
                new RuneRequirement(2, Rune.COSMIC),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(2, Rune.LAW)
        ),
        STAT_SPY(
                66,
                GlobalWidgetInfo.SPELL_STAT_SPY,
                new RuneRequirement(5, Rune.BODY),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(2, Rune.ASTRAL)
        ),
        DREAM(
                66,
                GlobalWidgetInfo.SPELL_DREAM,
                new RuneRequirement(5, Rune.BODY),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(2, Rune.ASTRAL)
        ),
        STAT_RESTORE_POT_SHARE(
                66,
                GlobalWidgetInfo.SPELL_STAT_RESTORE_POT_SHARE,
                new RuneRequirement(10, Rune.WATER),
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(2, Rune.ASTRAL)
        ),
        BOOST_POTION_SHARE(
                66,
                GlobalWidgetInfo.SPELL_BOOST_POTION_SHARE,
                new RuneRequirement(10, Rune.WATER),
                new RuneRequirement(12, Rune.EARTH),
                new RuneRequirement(3, Rune.ASTRAL)
        ),
        ENERGY_TRANSFER(
                66,
                GlobalWidgetInfo.SPELL_ENERGY_TRANSFER,
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(1, Rune.NATURE),
                new RuneRequirement(2, Rune.LAW)
        ),
        HEAL_OTHER(
                66,
                GlobalWidgetInfo.SPELL_HEAL_OTHER,
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(3, Rune.LAW),
                new RuneRequirement(1, Rune.BLOOD)
        ),
        VENGEANCE_OTHER(
                66,
                GlobalWidgetInfo.SPELL_VENGEANCE_OTHER,
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(2, Rune.DEATH)
        ),
        VENGEANCE(
                66,
                GlobalWidgetInfo.SPELL_VENGEANCE,
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(4, Rune.ASTRAL),
                new RuneRequirement(2, Rune.DEATH)
        ),
        HEAL_GROUP(
                66,
                GlobalWidgetInfo.SPELL_HEAL_GROUP,
                new RuneRequirement(4, Rune.ASTRAL),
                new RuneRequirement(6, Rune.LAW),
                new RuneRequirement(3, Rune.BLOOD)
        ),

        // Utility spells
        BAKE_PIE(
                66,
                GlobalWidgetInfo.SPELL_BAKE_PIE,
                new RuneRequirement(4, Rune.WATER),
                new RuneRequirement(5, Rune.FIRE),
                new RuneRequirement(1, Rune.ASTRAL)
        ),
        GEOMANCY(
                66,
                GlobalWidgetInfo.SPELL_GEOMANCY,
                new RuneRequirement(8, Rune.EARTH),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(3, Rune.NATURE)
        ),
        CURE_PLANT(
                66,
                GlobalWidgetInfo.SPELL_CURE_PLANT,
                new RuneRequirement(8, Rune.EARTH),
                new RuneRequirement(1, Rune.ASTRAL)
        ),
        NPC_CONTACT(
                66,
                GlobalWidgetInfo.SPELL_NPC_CONTACT,
                new RuneRequirement(2, Rune.AIR),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(1, Rune.ASTRAL)
        ),
        HUMIDIFY(
                66,
                GlobalWidgetInfo.SPELL_HUMIDIFY,
                new RuneRequirement(3, Rune.WATER),
                new RuneRequirement(1, Rune.FIRE),
                new RuneRequirement(1, Rune.ASTRAL)
        ),
        HUNTER_KIT(
                66,
                GlobalWidgetInfo.SPELL_HUNTER_KIT,
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(2, Rune.ASTRAL)
        ),
        SPIN_FLAX(
                66,
                GlobalWidgetInfo.SPELL_SPIN_FLAX,
                new RuneRequirement(5, Rune.AIR),
                new RuneRequirement(1, Rune.ASTRAL),
                new RuneRequirement(2, Rune.NATURE)
        ),
        SUPERGLASS_MAKE(
                66,
                GlobalWidgetInfo.SPELL_SUPERGLASS_MAKE,
                new RuneRequirement(10, Rune.AIR),
                new RuneRequirement(6, Rune.FIRE),
                new RuneRequirement(2, Rune.ASTRAL)
        ),
        TAN_LEATHER(
                66,
                GlobalWidgetInfo.SPELL_TAN_LEATHER,
                new RuneRequirement(5, Rune.FIRE),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(1, Rune.NATURE)
        ),
        STRING_JEWELLERY(
                66,
                GlobalWidgetInfo.SPELL_STRING_JEWELLERY,
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(5, Rune.WATER),
                new RuneRequirement(2, Rune.ASTRAL)
        ),
        MAGIC_IMBUE(
                66,
                GlobalWidgetInfo.SPELL_MAGIC_IMBUE,
                new RuneRequirement(7, Rune.WATER),
                new RuneRequirement(7, Rune.FIRE),
                new RuneRequirement(2, Rune.ASTRAL)
        ),
        FERTILE_SOIL(
                66,
                GlobalWidgetInfo.SPELL_FERTILE_SOIL,
                new RuneRequirement(15, Rune.EARTH),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(2, Rune.NATURE)
        ),
        PLANK_MAKE(
                66,
                GlobalWidgetInfo.SPELL_PLANK_MAKE,
                new RuneRequirement(15, Rune.EARTH),
                new RuneRequirement(2, Rune.ASTRAL),
                new RuneRequirement(1, Rune.NATURE)
        ),
        RECHARGE_DRAGONSTONE(
                66,
                GlobalWidgetInfo.SPELL_RECHARGE_DRAGONSTONE,
                new RuneRequirement(4, Rune.WATER),
                new RuneRequirement(1, Rune.ASTRAL),
                new RuneRequirement(1, Rune.SOUL)
        ),
        SPELLBOOK_SWAP(
                66,
                GlobalWidgetInfo.SPELL_SPELLBOOK_SWAP,
                new RuneRequirement(2, Rune.COSMIC),
                new RuneRequirement(3, Rune.ASTRAL),
                new RuneRequirement(1, Rune.LAW)
        ),
        ;

        private final int level;
        private final GlobalWidgetInfo widgetInfo;
        private final RuneRequirement[] requirements;

        private final int baseHit;

        Lunar(int level, GlobalWidgetInfo widgetInfo, RuneRequirement... requirements) {
            this(level, -1, widgetInfo, requirements);
        }

        Lunar(int level, int baseHit, GlobalWidgetInfo widgetInfo, RuneRequirement... requirements)
        {
            this.level = level;
            this.baseHit = baseHit;
            this.widgetInfo = widgetInfo;
            this.requirements = requirements;
        }

        @Override
        public int getLevel()
        {
            return level;
        }

        @Override
        public RSWidget getWidget()
        {
            return methods.interfaces.getComponent(widgetInfo);
        }

        public boolean canCast()
        {
            if (getCurrent() != LUNAR)
            {
                return false;
            }

            if (!methods.worldHopper.isCurrentWorldMembers())
            {
                return false;
            }

            if (this == LUNAR_HOME_TELEPORT)
            {
                return methods.magic.isHomeTeleportOnCooldown();
            }

            if (level > methods.skills.getRealLevel(Skill.MAGIC) || level > methods.skills.getCurrentLevel(Skill.MAGIC))
            {
                return false;
            }

            return haveRunesAvailable();
        }

        public boolean haveRunesAvailable()
        {
            for (RuneRequirement req : requirements)
            {
                if (!req.meetsRequirements())
                {
                    return false;
                }
            }

            return true;
        }

        public int getBaseHit() {
            return baseHit;
        }
    }

    public enum Necromancy implements Spell
    {
        // Teleport spells
        ARCEUUS_HOME_TELEPORT(
                1,
                GlobalWidgetInfo.SPELL_ARCEUUS_HOME_TELEPORT
        ),
        ARCEUUS_LIBRARY_TELEPORT(
                6,
                GlobalWidgetInfo.SPELL_ARCEUUS_LIBRARY_TELEPORT,
                new RuneRequirement(2, Rune.EARTH),
                new RuneRequirement(1, Rune.LAW)
        ),
        DRAYNOR_MANOR_TELEPORT(
                17,
                GlobalWidgetInfo.SPELL_DRAYNOR_MANOR_TELEPORT,
                new RuneRequirement(1, Rune.EARTH),
                new RuneRequirement(1, Rune.WATER),
                new RuneRequirement(1, Rune.LAW)
        ),
        BATTLEFRONT_TELEPORT(
                23,
                GlobalWidgetInfo.SPELL_BATTLEFRONT_TELEPORT,
                new RuneRequirement(1, Rune.EARTH),
                new RuneRequirement(1, Rune.FIRE),
                new RuneRequirement(1, Rune.LAW)
        ),
        MIND_ALTAR_TELEPORT(
                28,
                GlobalWidgetInfo.SPELL_MIND_ALTAR_TELEPORT,
                new RuneRequirement(2, Rune.MIND),
                new RuneRequirement(1, Rune.LAW)
        ),
        RESPAWN_TELEPORT(
                34,
                GlobalWidgetInfo.SPELL_RESPAWN_TELEPORT,
                new RuneRequirement(1, Rune.SOUL),
                new RuneRequirement(1, Rune.LAW)
        ),
        SALVE_GRAVEYARD_TELEPORT(
                40,
                GlobalWidgetInfo.SPELL_SALVE_GRAVEYARD_TELEPORT,
                new RuneRequirement(2, Rune.SOUL),
                new RuneRequirement(1, Rune.LAW)
        ),
        FENKENSTRAINS_CASTLE_TELEPORT(
                48,
                GlobalWidgetInfo.SPELL_FENKENSTRAINS_CASTLE_TELEPORT,
                new RuneRequirement(1, Rune.EARTH),
                new RuneRequirement(1, Rune.SOUL),
                new RuneRequirement(1, Rune.LAW)
        ),
        WEST_ARDOUGNE_TELEPORT(
                61,
                GlobalWidgetInfo.SPELL_WEST_ARDOUGNE_TELEPORT,
                new RuneRequirement(2, Rune.SOUL),
                new RuneRequirement(2, Rune.LAW)
        ),
        HARMONY_ISLAND_TELEPORT(
                65,
                GlobalWidgetInfo.SPELL_HARMONY_ISLAND_TELEPORT,
                new RuneRequirement(1, Rune.NATURE),
                new RuneRequirement(1, Rune.SOUL),
                new RuneRequirement(1, Rune.LAW)
        ),
        CEMETERY_TELEPORT(
                71,
                GlobalWidgetInfo.SPELL_CEMETERY_TELEPORT,
                new RuneRequirement(1, Rune.BLOOD),
                new RuneRequirement(1, Rune.SOUL),
                new RuneRequirement(1, Rune.LAW)
        ),
        BARROWS_TELEPORT(
                83,
                GlobalWidgetInfo.SPELL_BARROWS_TELEPORT,
                new RuneRequirement(1, Rune.BLOOD),
                new RuneRequirement(2, Rune.SOUL),
                new RuneRequirement(2, Rune.LAW)
        ),
        APE_ATOLL_TELEPORT_ARCEUUS(
                90,
                GlobalWidgetInfo.SPELL_APE_ATOLL_TELEPORT_ARCEUUS,
                new RuneRequirement(2, Rune.BLOOD),
                new RuneRequirement(2, Rune.SOUL),
                new RuneRequirement(2, Rune.LAW)
        ),

        // Combat spells
        GHOSTLY_GRASP(
                35,
                12,
                GlobalWidgetInfo.SPELL_GHOSTLY_GRASP,
                new RuneRequirement(4, Rune.AIR),
                new RuneRequirement(1, Rune.CHAOS)
        ),
        SKELETAL_GRASP(
                56,
                17,
                GlobalWidgetInfo.SPELL_SKELETAL_GRASP,
                new RuneRequirement(8, Rune.EARTH),
                new RuneRequirement(1, Rune.DEATH)
        ),
        UNDEAD_GRASP(
                79,
                24,
                GlobalWidgetInfo.SPELL_UNDEAD_GRASP,
                new RuneRequirement(12, Rune.FIRE),
                new RuneRequirement(1, Rune.BLOOD)
        ),
        INFERIOR_DEMONBANE(
                44,
                16,
                GlobalWidgetInfo.SPELL_INFERIOR_DEMONBANE,
                new RuneRequirement(4, Rune.FIRE),
                new RuneRequirement(1, Rune.CHAOS)
        ),
        SUPERIOR_DEMONBANE(
                62,
                23,
                GlobalWidgetInfo.SPELL_SUPERIOR_DEMONBANE,
                new RuneRequirement(8, Rune.FIRE),
                new RuneRequirement(1, Rune.SOUL)
        ),
        DARK_DEMONBANE(
                82,
                30,
                GlobalWidgetInfo.SPELL_DARK_DEMONBANE,
                new RuneRequirement(12, Rune.FIRE),
                new RuneRequirement(2, Rune.SOUL)
        ),
        LESSER_CORRUPTION(
                64,
                GlobalWidgetInfo.SPELL_LESSER_CORRUPTION,
                new RuneRequirement(1, Rune.DEATH),
                new RuneRequirement(2, Rune.SOUL)
        ),
        GREATER_CORRUPTION(
                85,
                GlobalWidgetInfo.SPELL_GREATER_CORRUPTION,
                new RuneRequirement(1, Rune.BLOOD),
                new RuneRequirement(3, Rune.SOUL)
        ),
        RESURRECT_LESSER_GHOST(
                38,
                GlobalWidgetInfo.SPELL_RESURRECT_LESSER_GHOST,
                new RuneRequirement(10, Rune.AIR),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(5, Rune.MIND)
        ),
        RESURRECT_LESSER_SKELETON(
                38,
                GlobalWidgetInfo.SPELL_RESURRECT_LESSER_SKELETON,
                new RuneRequirement(10, Rune.AIR),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(5, Rune.MIND)
        ),
        RESURRECT_LESSER_ZOMBIE(
                38,
                GlobalWidgetInfo.SPELL_RESURRECT_LESSER_ZOMBIE,
                new RuneRequirement(10, Rune.AIR),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(5, Rune.MIND)
        ),
        RESURRECT_SUPERIOR_GHOST(
                57,
                GlobalWidgetInfo.SPELL_RESURRECT_SUPERIOR_GHOST,
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(5, Rune.DEATH)
        ),
        RESURRECT_SUPERIOR_SKELETON(
                57,
                GlobalWidgetInfo.SPELL_RESURRECT_SUPERIOR_SKELETON,
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(5, Rune.DEATH)
        ),
        RESURRECT_SUPERIOR_ZOMBIE(
                57,
                GlobalWidgetInfo.SPELL_RESURRECT_SUPERIOR_ZOMBIE,
                new RuneRequirement(10, Rune.EARTH),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(5, Rune.DEATH)
        ),
        RESURRECT_GREATER_GHOST(
                76,
                GlobalWidgetInfo.SPELL_RESURRECT_GREATER_GHOST,
                new RuneRequirement(10, Rune.FIRE),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(5, Rune.BLOOD)
        ),
        RESURRECT_GREATER_SKELETON(
                76,
                GlobalWidgetInfo.SPELL_RESURRECT_GREATER_SKELETON,
                new RuneRequirement(10, Rune.FIRE),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(5, Rune.BLOOD)
        ),
        RESURRECT_GREATER_ZOMBIE(
                76,
                GlobalWidgetInfo.SPELL_RESURRECT_GREATER_ZOMBIE,
                new RuneRequirement(10, Rune.FIRE),
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(5, Rune.BLOOD)
        ),
        DARK_LURE(
                50,
                GlobalWidgetInfo.SPELL_DARK_LURE,
                new RuneRequirement(1, Rune.DEATH),
                new RuneRequirement(1, Rune.NATURE)
        ),
        MARK_OF_DARKNESS(
                59,
                GlobalWidgetInfo.SPELL_MARK_OF_DARKNESS,
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(1, Rune.SOUL)
        ),
        WARD_OF_ARCEUUS(
                73,
                GlobalWidgetInfo.SPELL_WARD_OF_ARCEUUS,
                new RuneRequirement(1, Rune.COSMIC),
                new RuneRequirement(2, Rune.NATURE),
                new RuneRequirement(4, Rune.SOUL)
        ),

        // Utility spells
        BASIC_REANIMATION(
                16,
                GlobalWidgetInfo.SPELL_BASIC_REANIMATION,
                new RuneRequirement(4, Rune.BODY),
                new RuneRequirement(2, Rune.NATURE)
        ),
        ADEPT_REANIMATION(
                41,
                GlobalWidgetInfo.SPELL_ADEPT_REANIMATION,
                new RuneRequirement(4, Rune.BODY),
                new RuneRequirement(3, Rune.NATURE),
                new RuneRequirement(1, Rune.SOUL)
        ),
        EXPERT_REANIMATION(
                72,
                GlobalWidgetInfo.SPELL_EXPERT_REANIMATION,
                new RuneRequirement(1, Rune.BLOOD),
                new RuneRequirement(3, Rune.NATURE),
                new RuneRequirement(2, Rune.SOUL)
        ),
        MASTER_REANIMATION(
                90,
                GlobalWidgetInfo.SPELL_MASTER_REANIMATION,
                new RuneRequirement(2, Rune.BLOOD),
                new RuneRequirement(4, Rune.NATURE),
                new RuneRequirement(4, Rune.SOUL)
        ),
        DEMONIC_OFFERING(
                84,
                GlobalWidgetInfo.SPELL_DEMONIC_OFFERING,
                new RuneRequirement(1, Rune.SOUL),
                new RuneRequirement(1, Rune.WRATH)
        ),
        SINISTER_OFFERING(
                92,
                GlobalWidgetInfo.SPELL_SINISTER_OFFERING,
                new RuneRequirement(1, Rune.BLOOD),
                new RuneRequirement(1, Rune.WRATH)
        ),
        SHADOW_VEIL(
                47,
                GlobalWidgetInfo.SPELL_SHADOW_VEIL,
                new RuneRequirement(5, Rune.EARTH),
                new RuneRequirement(5, Rune.FIRE),
                new RuneRequirement(5, Rune.COSMIC)
        ),
        VILE_VIGOUR(
                66,
                GlobalWidgetInfo.SPELL_VILE_VIGOUR,
                new RuneRequirement(3, Rune.AIR),
                new RuneRequirement(1, Rune.SOUL)
        ),
        DEGRIME(
                70,
                GlobalWidgetInfo.SPELL_DEGRIME,
                new RuneRequirement(4, Rune.EARTH),
                new RuneRequirement(2, Rune.NATURE)
        ),
        RESURRECT_CROPS(
                78,
                GlobalWidgetInfo.SPELL_RESURRECT_CROPS,
                new RuneRequirement(25, Rune.EARTH),
                new RuneRequirement(8, Rune.BLOOD),
                new RuneRequirement(12, Rune.NATURE),
                new RuneRequirement(8, Rune.SOUL)
        ),
        DEATH_CHARGE(
                80,
                GlobalWidgetInfo.SPELL_DEATH_CHARGE,
                new RuneRequirement(1, Rune.BLOOD),
                new RuneRequirement(1, Rune.DEATH),
                new RuneRequirement(1, Rune.SOUL)
        ),
        ;

        private final int level;
        private final GlobalWidgetInfo widgetInfo;
        private final RuneRequirement[] requirements;

        private final int baseHit;

        Necromancy(int level, GlobalWidgetInfo widgetInfo, RuneRequirement... requirements)
        {
            this(level, -1, widgetInfo, requirements);
        }

        Necromancy(int level, int baseHit, GlobalWidgetInfo widgetInfo, RuneRequirement... requirements)
        {
            this.level = level;
            this.baseHit = baseHit;
            this.widgetInfo = widgetInfo;
            this.requirements = requirements;
        }

        @Override
        public int getLevel()
        {
            return level;
        }

        @Override
        public RSWidget getWidget()
        {
            return methods.interfaces.getComponent(widgetInfo);
        }

        public boolean canCast()
        {
            if (getCurrent() != NECROMANCY)
            {
                return false;
            }

            if (!methods.worldHopper.isCurrentWorldMembers())
            {
                return false;
            }

            if (this == ARCEUUS_HOME_TELEPORT)
            {
                return methods.magic.isHomeTeleportOnCooldown();
            }

            if (level > methods.skills.getRealLevel(Skill.MAGIC) || level > methods.skills.getCurrentLevel(Skill.MAGIC))
            {
                return false;
            }

            return haveRunesAvailable();
        }

        public boolean haveRunesAvailable()
        {
            for (RuneRequirement req : requirements)
            {
                if (!req.meetsRequirements())
                {
                    return false;
                }
            }

            return true;
        }

        public int getBaseHit() {
            return baseHit;
        }
    }
}