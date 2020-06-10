package net.runelite.client.rsb.methods;

import net.runelite.client.rsb.wrappers.RSCharacter;
import net.runelite.client.rsb.wrappers.RSGroundItem;
import net.runelite.client.rsb.wrappers.RSObject;
import net.runelite.client.rsb.wrappers.RSWidget;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Magic tab and spell related operations.
 *
 * @author Jacmob, Aut0r, Timer
 */
public class Magic extends MethodProvider {



    //Interfaces used
    //FIXED_VIEWPORT 548 (can be gotten from WidgetInfo
    //548->72-> (218(N)->3) N=Nested
    final int FIXED_VIEWPORT_SPELL_BOOK_CONTAINER_ID = 72;
    final int SPELL_LIST = 3;
    final int AUTOCAST_SPELL_BOOK = 201;
    final int SPELL_BOOK = 218;

    /**
     * Provides Magic Book(s) Information.
     *
     * @author GigiaJ
     */
    public static enum Book {

        MODERN(SPELL_WIND_STRIKE), ANCIENT(SPELL_ICE_RUSH), LUNAR(SPELL_BAKE_PIE), ARCEUUS(SPELL_REANIMATE_GOBLIN), NULL(-1);

        private final int index;

        Book(int index) {
            this.index = index;
        }

        int getIndex() {
            return this.index;
        }

    }

    // Buttons

    public static final int INTERFACE_SHOW_COMBAT_SPELLS = 5;
    public static final int INTERFACE_SHOW_TELEPORT_SPELLS = 7;
    public static final int INTERFACE_SHOW_UTILITY_SPELLS = 9;
    public static final int INTERFACE_SHOW_LEVEL_UNCASTABLE_SPELLS = 11;
    public static final int INTERFACE_SHOW_RUNES_UNCASTABLE_SPELLS = 13;

/*
    public static final int INTERFACE_DEFENSIVE_STANCE = 2;
    public static final int INTERFACE_SHOW_COMBAT_SPELLS = 7;
    public static final int INTERFACE_SHOW_TELEPORT_SPELLS = 9;
    public static final int INTERFACE_SHOW_MISC_SPELLS = 11;
    public static final int INTERFACE_SHOW_SKILL_SPELLS = 13;
    public static final int INTERFACE_SORT_BY_LEVEL = 15;
    public static final int INTERFACE_SORT_BY_COMBAT = 16;
    public static final int INTERFACE_SORT_BY_TELEPORTS = 17;
*/



    public static final int SPELL_LUMBRIDGE_HOME_TELEPORT = 4;
    public static final int SPELL_WIND_STRIKE = 5;
    public static final int SPELL_CONFUSE = 6;
    public static final int SPELL_ENCHANT_CROSSBOW_BOLT = 7;
    public static final int SPELL_WATER_STRIKE = 8;
    public static final int SPELL_LVL_1_ENCHANT = 9;
    public static final int SPELL_EARTH_STRIKE = 10;
    public static final int SPELL_WEAKEN = 11;
    public static final int SPELL_FIRE_STRIKE = 12;
    public static final int SPELL_BONES_TO_BANANAS = 13;
    public static final int SPELL_WIND_BOLT = 14;
    public static final int SPELL_CURSE = 15;
    public static final int SPELL_BIND = 16;
    public static final int SPELL_LOW_LEVEL_ALCHEMY = 17;
    public static final int SPELL_WATER_BOLT = 18;
    public static final int SPELL_VARROCK_TELEPORT = 19;
    public static final int SPELL_LVL_2_ENCHANT = 20;
    public static final int SPELL_EARTH_BOLT = 21;
    public static final int SPELL_LUMBRIDGE_TELEPORT = 22;
    public static final int SPELL_TELEKINETIC_GRAB = 23;
    public static final int SPELL_FIRE_BOLT = 24;
    public static final int SPELL_FALADOR_TELEPORT = 25;
    public static final int SPELL_CRUMBLE_UNDEAD = 26;
    public static final int SPELL_TELEPORT_TO_HOUSE = 27;
    public static final int SPELL_WIND_BLAST = 28;
    public static final int SPELL_SUPERHEAT_ITEM = 29;
    public static final int SPELL_CAMELOT_TELEPORT = 30;
    public static final int SPELL_WATER_BLAST = 31;
    public static final int SPELL_LVL_3_ENCHANT = 32;
    public static final int SPELL_IBAN_BLAST = 33;
    public static final int SPELL_SNARE = 34;
    public static final int SPELL_MAGIC_DART = 35;
    public static final int SPELL_ARDOUGNE_TELEPORT = 36;
    public static final int SPELL_EARTH_BLAST = 37;
    public static final int SPELL_HIGH_LEVEL_ALCHEMY = 38;
    public static final int SPELL_CHARGE_WATER_ORB = 39;
    public static final int SPELL_LVL_4_ENCHANT = 40;
    public static final int SPELL_WATCHTOWER_TELEPORT = 41;
    public static final int SPELL_FIRE_BLAST = 42;
    public static final int SPELL_CHARGE_EARTH_ORB = 43;
    public static final int SPELL_BONES_TO_PEACHES = 44;
    public static final int SPELL_SARADOMIN_STRIKE = 45;
    public static final int SPELL_CLAWS_OF_GUTHIX = 46;
    public static final int SPELL_FLAMES_OF_ZAMORAK = 47;
    public static final int SPELL_TROLLHEIM_TELEPORT = 48;
    public static final int SPELL_WIND_WAVE = 49;
    public static final int SPELL_CHARGE_FIRE_ORB = 50;
    public static final int SPELL_APE_ATOLL_TELEPORT = 51;
    public static final int SPELL_WATER_WAVE = 52;
    public static final int SPELL_CHARGE_AIR_ORB = 53;
    public static final int SPELL_VULNERABILITY = 54;
    public static final int SPELL_LVL_5_ENCHANT = 55;
    public static final int SPELL_KOUREND_CASTLE_TELEPORT = 56;
    public static final int SPELL_EARTH_WAVE = 57;
    public static final int SPELL_ENFEEBLE = 58;
    public static final int SPELL_TELEOTHER_LUMBRIDGE = 59;
    public static final int SPELL_FIRE_WAVE = 60;
    public static final int SPELL_ENTANGLE = 61;
    public static final int SPELL_STUN = 62;
    public static final int SPELL_CHARGE = 63;
    public static final int SPELL_WIND_SURGE = 64;
    public static final int SPELL_TELEOTHER_FALADOR = 65;
    public static final int SPELL_WATER_SURGE = 66;
    public static final int SPELL_TELE_BLOCK = 67;
    public static final int SPELL_TELEPORT_TO_BOUNTY_TARGET = 68;
    public static final int SPELL_LVL_6_ENCHANT = 69;
    public static final int SPELL_TELEOTHER_CAMELOT = 70;
    public static final int SPELL_EARTH_SURGE = 71;
    public static final int SPELL_LVL_7_ENCHANT = 72;
    // ANCIENT
    public static final int SPELL_FIRE_SURGE = 73;
    public static final int SPELL_ICE_RUSH = 74;
    public static final int SPELL_ICE_BLITZ = 75;
    public static final int SPELL_ICE_BURST = 76;
    public static final int SPELL_ICE_BARRAGE = 77;
    public static final int SPELL_BLOOD_RUSH = 78;
    public static final int SPELL_BLOOD_BLITZ = 79;
    public static final int SPELL_BLOOD_BURST = 80;
    public static final int SPELL_BLOOD_BARRAGE = 81;
    public static final int SPELL_SMOKE_RUSH = 82;
    public static final int SPELL_SMOKE_BLITZ = 83;
    public static final int SPELL_SMOKE_BURST = 84;
    public static final int SPELL_SMOKE_BARRAGE = 85;
    public static final int SPELL_SHADOW_RUSH = 86;
    public static final int SPELL_SHADOW_BLITZ = 87;
    public static final int SPELL_SHADOW_BURST = 88;
    public static final int SPELL_SHADOW_BARRAGE = 89;
    public static final int SPELL_PADDEWWA_TELEPORT = 90;
    public static final int SPELL_SENNTISTEN_TELEPORT = 91;
    public static final int SPELL_KHARYRLL_TELEPORT = 92;
    public static final int SPELL_LASSAR_TELEPORT = 93;
    public static final int SPELL_DAREEYAK_TELEPORT = 94;
    public static final int SPELL_CARRALLANGAR_TELEPORT = 95;
    public static final int SPELL_ANNAKARL_TELEPORT = 96;
    public static final int SPELL_GHORROCK_TELEPORT = 97;
    public static final int SPELL_EDGEVILLE_HOME_TELEPORT = 98;
    // LUNAR
    public static final int SPELL_LUNAR_HOME_TELEPORT = 99;
    public static final int SPELL_BAKE_PIE = 100;
    public static final int SPELL_CURE_PLANT = 101;
    public static final int SPELL_MONSTER_EXAMINE = 102;
    public static final int SPELL_NPC_CONTACT = 103;
    public static final int SPELL_CURE_OTHER = 104;
    public static final int SPELL_HUMIDIFY = 105;
    public static final int SPELL_MOONCLAN_TELEPORT = 106;
    public static final int SPELL_TELE_GROUP_MOONCLAN = 107;
    public static final int SPELL_CURE_ME = 108;
    public static final int SPELL_HUNTER_KIT = 109;
    public static final int SPELL_WATERBIRTH_TELEPORT = 110;
    public static final int SPELL_TELE_GROUP_WATERBIRTH = 111;
    public static final int SPELL_CURE_GROUP = 112;
    public static final int SPELL_STAT_SPY = 113;
    public static final int SPELL_BARBARIAN_TELEPORT = 114;
    public static final int SPELL_TELE_GROUP_BARBARIAN = 115;
    public static final int SPELL_SUPERGLASS_MAKE = 116;
    public static final int SPELL_TAN_LEATHER = 117;
    public static final int SPELL_KHAZARD_TELEPORT = 118;
    public static final int SPELL_TELE_GROUP_KHAZARD = 119;
    public static final int SPELL_DREAM = 120;
    public static final int SPELL_STRING_JEWELLERY = 121;
    public static final int SPELL_STAT_RESTORE_POT_SHARE = 122;
    public static final int SPELL_MAGIC_IMBUE = 123;
    public static final int SPELL_FERTILE_SOIL = 124;
    public static final int SPELL_BOOST_POTION_SHARE = 125;
    public static final int SPELL_FISHING_GUILD_TELEPORT = 126;
    public static final int SPELL_TELE_GROUP_FISHING_GUILD = 127;
    public static final int SPELL_PLANK_MAKE = 128;
    public static final int SPELL_CATHERBY_TELEPORT = 129;
    public static final int SPELL_TELE_GROUP_CATHERBY = 130;
    public static final int SPELL_RECHARGE_DRAGONSTONE = 131;
    public static final int SPELL_ICE_PLATEAU_TELEPORT = 132;
    public static final int SPELL_TELE_GROUP_ICE_PLATEAU = 133;
    public static final int SPELL_ENERGY_TRANSFER = 134;
    public static final int SPELL_HEAL_OTHER = 135;
    public static final int SPELL_VENGEANCE_OTHER = 136;
    public static final int SPELL_VENGEANCE = 137;
    public static final int SPELL_HEAL_GROUP = 138;
    public static final int SPELL_SPELLBOOK_SWAP = 139;
    public static final int SPELL_GEOMANCY = 140;
    public static final int SPELL_SPIN_FLAX = 141;
    public static final int SPELL_OURANIA_TELEPORT = 142;
    // ARCEUUS
    public static final int SPELL_ARCEUUS_HOME_TELEPORT = 143;
    public static final int SPELL_REANIMATE_GOBLIN = 144;
    public static final int SPELL_LUMBRIDGE_GRAVEYARD_TELEPORT = 145;
    public static final int SPELL_REANIMATE_MONKEY = 146;
    public static final int SPELL_REANIMATE_IMP = 147;
    public static final int SPELL_REANIMATE_MINOTAUR = 148;
    public static final int SPELL_DRAYNOR_MANOR_TELEPORT = 149;
    public static final int SPELL_REANIMATE_SCORPION = 150;
    public static final int SPELL_REANIMATE_BEAR = 151;
    public static final int SPELL_REANIMATE_UNICORN = 152;
    public static final int SPELL_REANIMATE_DOG = 153;
    public static final int SPELL_MIND_ALTAR_TELEPORT = 154;
    public static final int SPELL_REANIMATE_CHAOS_DRUID = 155;
    public static final int SPELL_RESPAWN_TELEPORT = 156;
    public static final int SPELL_REANIMATE_GIANT = 157;
    public static final int SPELL_SALVE_GRAVEYARD_TELEPORT = 158;
    public static final int SPELL_REANIMATE_OGRE = 159;
    public static final int SPELL_REANIMATE_ELF = 160;
    public static final int SPELL_REANIMATE_TROLL = 161;
    public static final int SPELL_FENKENSTRAINS_CASTLE_TELEPORT = 162;
    public static final int SPELL_REANIMATE_HORROR = 163;
    public static final int SPELL_REANIMATE_KALPHITE = 164;
    public static final int SPELL_WEST_ARDOUGNE_TELEPORT = 165;
    public static final int SPELL_REANIMATE_DAGANNOTH = 166;
    public static final int SPELL_REANIMATE_BLOODVELD = 167;
    public static final int SPELL_HARMONY_ISLAND_TELEPORT = 168;
    public static final int SPELL_REANIMATE_TZHAAR = 169;
    public static final int SPELL_CEMETERY_TELEPORT = 170;
    public static final int SPELL_REANIMATE_DEMON = 171;
    public static final int SPELL_REANIMATE_AVIANSIE = 172;
    public static final int SPELL_RESURRECT_CROPS = 173;
    public static final int SPELL_BARROWS_TELEPORT = 174;
    public static final int SPELL_REANIMATE_ABYSSAL_CREATURE = 175;
    public static final int SPELL_APE_ATOLL_TELEPORT_A = 176;
    public static final int SPELL_REANIMATE_DRAGON = 177;
    public static final int SPELL_BATTLEFRONT_TELEPORT = 178;

    Magic(final MethodContext ctx) {
        super(ctx);
    }

    /**
     * Checks whether or not a spell is selected.
     *
     * @return <tt>true</tt> if a spell is selected; otherwise <tt>false</tt>.
     */
    public boolean isSpellSelected() {
        RSWidget widget = methods.interfaces.get(SPELL_BOOK, SPELL_LIST);
        for (RSWidget child : widget.getComponents()) {
                if (child.isVisible() || child.isSelfVisible()) {
                    //Check api.widget to see what border is what or just validate that when one is selected
                    // what border type it has at the time
                    if (child.getBorderThickness() == 2) {
                        return true;
                    }
                }
        }

        return false;
    }

    /**
     * Determines whether a spell is currently set to autocast.
     *
     * @return <tt>true</tt> if autocasting; otherwise <tt>false</tt>.
     */
    public boolean isAutoCasting() {
        return methods.combat.getFightMode() == 4;
    }



    public int getSpell(String name) {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getName().contains(name.toUpperCase())) {
                try {
                    return (int) field.get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * Clicks a specified spell, opens magic tab if not open and uses interface
     * of the spell to click it, so it works if the spells are layout in any
     * sway.
     *
     * @param spell The spell to cast.
     * @return <tt>true</tt> if the spell was clicked; otherwise <tt>false</tt>.
     */
    public boolean castSpell(final int spell) {
        if (methods.game.getCurrentTab() != Game.TAB_MAGIC) {
            methods.game.openTab(Game.TAB_MAGIC);
            for (int i = 0; i < 100; i++) {
                sleep(20);
                if (methods.game.getCurrentTab() == Game.TAB_MAGIC) {
                    break;
                }
            }
            sleep(random(150, 250));
        }
        if (methods.game.getCurrentTab() == Game.TAB_MAGIC) {
            RSWidget inter = getInterface();
            if (inter != null) {
                RSWidget comp = inter.getComponent(spell);
                return comp != null && comp.doAction("Cast");
            }
        }
        return false;
    }

    /**
     * Hovers a specified spell, opens magic tab if not open and uses interface
     * of the spell to hover it, so it works if the spells are layout in any
     * sway.
     *
     * @param spell The spell to hover.
     * @return <tt>true</tt> if the spell was clicked; otherwise <tt>false</tt>.
     */
    public boolean hoverSpell(final int spell) {
        if (methods.game.getCurrentTab() != Game.TAB_MAGIC) {
            methods.game.openTab(Game.TAB_MAGIC);
            for (int i = 0; i < 100; i++) {
                sleep(20);
                if (methods.game.getCurrentTab() == Game.TAB_MAGIC) {
                    break;
                }
            }
            sleep(random(150, 250));
        }
        if (methods.game.getCurrentTab() == Game.TAB_MAGIC) {
            RSWidget inter = getInterface();
            if (inter != null) {
                RSWidget comp = inter.getComponent(spell);
                return comp != null && comp.doHover();
            }
        }
        return false;
    }

    /**
     * Auto-casts a spell via the magic tab.
     *
     * @param spell The spell to auto-cast.
     * @return <tt>true</tt> if the "Auto-cast" interface option was clicked;
     * otherwise <tt>false</tt>.
     */
    public boolean autoCastSpell(final int spell) {
        if (methods.settings.getSetting(Settings.SETTING_COMBAT_STYLE) != 4) {
            if (methods.game.getCurrentTab() != Game.TAB_ATTACK) {
                methods.game.openTab(Game.TAB_ATTACK);
                sleep(random(150, 250));
            }
            if (methods.interfaces.get(Combat.INTERFACE_COMBAT, Combat.INTERFACE_COMBAT_AUTO_CAST_SPELL).doClick()) {
                {
                    sleep(random(500, 700));
                    RSWidget widget = methods.interfaces.get(SPELL_BOOK, SPELL_LIST);
                    //The children are the spells
                    for (RSWidget child : widget.getComponents()) {
                        //To speed up the search we'll filter the undesirables
                        if (child.isVisible() || child.isSelfVisible()) {
                            //This is the autocast book spell list
                            for (RSWidget autoCastSpell : methods.interfaces.get(AUTOCAST_SPELL_BOOK, 1).getComponents()) {
                                //We need to compare sprites to determine if we've found the right value
                                //This alleviates the need to devise a convoluted method to find spells in this book
                                //All the spells start from at 4 so the index needs to be adjusted for that
                                if (child.getIndex() + 3 == spell) {
                                    if (autoCastSpell.getSpriteId() == child.getSpriteId()) {
                                        return autoCastSpell.doClick();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets the open magic book interface.
     *
     * @return The current magic RSWidget.
     */
    public RSWidget getInterface() {
        RSWidget widget = methods.interfaces.get(SPELL_BOOK, SPELL_LIST);
        if (widget.isVisible()) {
            return widget;
        }
        return null;
    }

    /**
     * Gets the current spell book.
     *
     * @return The Book enum of your current spell book.
     */
    public Book getCurrentSpellBook() {
        RSWidget widget;
        for (int x = 0; x < Book.values().length; x++) {
            if (Book.values()[x] != Book.NULL) {
                widget = methods.interfaces.get(SPELL_BOOK, Book.values()[x].getIndex());
                if (widget.isValid() && widget.isSelfVisible()) {
                    return Book.values()[x];
                }
            }
        }
        return Book.NULL;
    }

    /**
     * Casts a spell on a Player/NPC/Object/Ground Item.
     *
     * @param entity A Character or Animable.
     * @param spell  The spell to cast.
     * @return <tt>true</tt> if casted; otherwise <tt>false</tt>.
     */
    public boolean castSpellOn(final Object entity, final int spell) {
        if (isSpellSelected() || entity == null) {
            return false;
        }
        if (castSpell(spell)) {
            if (entity instanceof RSCharacter) {
                return ((RSCharacter) entity).doAction("Cast");
            } else if (entity instanceof RSObject) {
                return ((RSObject) entity).doAction("Cast");
            } else if (entity instanceof RSGroundItem) {
                return ((RSGroundItem) entity).doAction("Cast");
            }
        }
        return false;
    }

    /**
     * Converts the spell list to variables to be used
     * *Variable updating usage*
     *
     * @return string containing the spells in variable form
     */
    public String convertSpellBookToVariables() {
        RSWidget widget = methods.interfaces.get(SPELL_BOOK, SPELL_LIST);
        String spells = "";
        for (RSWidget child : widget.getComponents()) {
            Pattern pattern = Pattern.compile(">(.*)<");
            Matcher matcher = pattern.matcher(child.getName());
            while (matcher.find()) {
                String spellToAdd = "public static final int SPELL_" + matcher.group(1).replaceAll(" ", "_")
                        .replaceAll("-", "_").replaceAll("\'", "").toUpperCase() + " = ";

                    for (int i = 0; i < Book.values().length; i++) {
                        int reverseI = Book.values().length - i - 2;
                        if (reverseI > 0) {
                            if (child.getIndex() + 3 == Book.values()[reverseI].getIndex() - 1) {
                                spells += "// " + Book.values()[reverseI].name() + "\n";
                            }

                            if (child.getIndex() + 3 >= Book.values()[reverseI].getIndex() - 1) {
                                if (spells.contains(spellToAdd)) {
                                    spellToAdd = "public static final int SPELL_" + matcher.group(1).replaceAll(" ", "_")
                                            .replaceAll("-", "_").replaceAll("\'", "").toUpperCase()
                                            + "_" + Book.values()[reverseI].name().charAt(0) + " = ";
                                    break;
                                }
                            }
                        }
                }
                spells += spellToAdd + (child.getIndex() + 3) + ";\n";

            }
        }
        return spells;
    }

}
