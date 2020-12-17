package rsb.walker.dax_api.teleports;

import rsb.methods.Game;
import rsb.methods.Web;
import rsb.util.Timer;
import rsb.walker.dax_api.Filters;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.api_lib.models.Requirement;
import rsb.walker.dax_api.shared.helpers.InterfaceHelper;
import rsb.walker.dax_api.shared.helpers.RSItemHelper;
import rsb.walker.dax_api.shared.helpers.WorldHelper;
import rsb.walker.dax_api.shared.helpers.magic.Spell;
import rsb.walker.dax_api.teleports.teleport_utils.TeleportConstants;
import rsb.walker.dax_api.teleports.teleport_utils.TeleportLimit;
import rsb.walker.dax_api.teleports.teleport_utils.TeleportScrolls;
import rsb.wrappers.RSItem;
import rsb.wrappers.RSWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public enum Teleport {

	VARROCK_TELEPORT(
			35, new WalkerTile(3212, 3424, 0),
			Spell.VARROCK_TELEPORT::canUse,
			() -> selectSpell("Varrock Teleport","Cast")
	),

	VARROCK_TELEPORT_TAB(
			35, new WalkerTile(3212, 3424, 0),
			() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Varrock teleport")) > 0,
			() -> RSItemHelper.click("Varrock t.*", "Break")
	),

	VARROCK_TELEPORT_GRAND_EXCHANGE(
			35, new WalkerTile(3161, 3478, 0),
			() -> Spell.VARROCK_TELEPORT.canUse() && TeleportConstants.isVarrockTeleportAtGE(),
			() -> selectSpell("Varrock Teleport","Grand Exchange")
	),

	LUMBRIDGE_TELEPORT(
			35, new WalkerTile(3225, 3219, 0),
			Spell.LUMBRIDGE_TELEPORT::canUse,
			() -> selectSpell("Lumbridge Teleport","Cast")
	),

	LUMBRIDGE_TELEPORT_TAB(
			35, new WalkerTile(3225, 3219, 0),
			() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Lumbridge teleport")) > 0,
			() -> RSItemHelper.click("Lumbridge t.*", "Break")
	),

	FALADOR_TELEPORT(
			35, new WalkerTile(2966, 3379, 0),
			Spell.FALADOR_TELEPORT::canUse,
			() -> selectSpell("Falador Teleport","Cast")
	),

	FALADOR_TELEPORT_TAB(
			35, new WalkerTile(2966, 3379, 0),
			() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Falador teleport")) > 0,
			() -> RSItemHelper.click("Falador t.*", "Break")
	),

	CAMELOT_TELEPORT(
			35, new WalkerTile(2757, 3479, 0),
			() -> inMembersWorld() && Spell.CAMELOT_TELEPORT.canUse(),
			() -> selectSpell("Camelot Teleport","Cast")

	),

	CAMELOT_TELEPORT_TAB(
			35, new WalkerTile(2757, 3479, 0),
			() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Camelot teleport")) > 0,
			() -> RSItemHelper.click("Camelot t.*", "Break")
	),

	SEERS_TELEPORT(
			35, new WalkerTile(2757, 3479, 0),
			() -> inMembersWorld() && Spell.CAMELOT_TELEPORT.canUse() && Web.methods.client.getVarbitValue(4560) == 1,
			() -> selectSpell("Camelot Teleport","Seers'")
	),

	ARDOUGNE_TELEPORT(
			35, new WalkerTile(2661, 3300, 0),
			() -> inMembersWorld() && Spell.ARDOUGNE_TELEPORT.canUse(),
			() -> selectSpell("Ardougne Teleport","Cast")

	),

	ARDOUGNE_TELEPORT_TAB(
			35, new WalkerTile(2661, 3300, 0),
			() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Ardougne teleport")) > 0,
			() -> RSItemHelper.click("Ardougne t.*", "Break")
	),

	NARDAH_TELEPORT(
			35, TeleportScrolls.NARDAH
	),
	DIGSITE_TELEPORT(
			35, TeleportScrolls.DIGSITE
	),
	FELDIP_HILLS_TELEPORT(
			35, TeleportScrolls.FELDIP_HILLS
	),
	LUNAR_ISLE_TELEPORT(
			35, TeleportScrolls.LUNAR_ISLE
	),
	MORTTON_TELEPORT(
			35, TeleportScrolls.MORTTON
	),
	PEST_CONTROL_TELEPORT(
			35, TeleportScrolls.PEST_CONTROL
	),
	PISCATORIS_TELEPORT(
			35, TeleportScrolls.PISCATORIS
	),
	TAI_BWO_WANNAI_TELEPORT(
			35, TeleportScrolls.TAI_BWO_WANNAI
	),
	ELF_CAMP_TELEPORT(
			35, TeleportScrolls.ELF_CAMP
	),
	MOS_LE_HARMLESS_TELEPORT(
			35, TeleportScrolls.MOS_LE_HARMLESS
	),
	LUMBERYARD_TELEPORT(
			35, TeleportScrolls.LUMBERYARD
	),
	ZULLANDRA_TELEPORT(
			35, TeleportScrolls.ZULLANDRA
	),
	KEY_MASTER_TELEPORT(
			35, TeleportScrolls.KEY_MASTER
	),
	REVENANT_CAVES_TELEPORT(
			35, TeleportScrolls.REVENANT_CAVES
	),
	WATSON_TELEPORT(
			35, TeleportScrolls.WATSON
	),


	RING_OF_WEALTH_GRAND_EXCHANGE(
			35, new WalkerTile(3161, 3478, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.RING_OF_WEALTH_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.RING_OF_WEALTH_FILTER, "(?i)Grand Exchange"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	RING_OF_WEALTH_FALADOR(
			35, new WalkerTile(2994, 3377, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.RING_OF_WEALTH_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.RING_OF_WEALTH_FILTER, "(?i)falador.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	RING_OF_WEALTH_MISCELLANIA(
			35, new WalkerTile(2535, 3861, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.RING_OF_WEALTH_FILTER) && Web.methods.settings.getSetting(359) >= 100,
			() -> WearableItemTeleport.teleport(WearableItemTeleport.RING_OF_WEALTH_FILTER, "(?i)misc.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	RING_OF_DUELING_DUEL_ARENA (
			35, new WalkerTile(3313, 3233, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.RING_OF_DUELING_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.RING_OF_DUELING_FILTER, "(?i).*duel arena.*")
	),

	RING_OF_DUELING_CASTLE_WARS (
			35, new WalkerTile(2440, 3090, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.RING_OF_DUELING_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.RING_OF_DUELING_FILTER, "(?i).*Castle Wars.*")
	),

	RING_OF_DUELING_CLAN_WARS (
			35, new WalkerTile(3388, 3161, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.RING_OF_DUELING_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.RING_OF_DUELING_FILTER, "(?i).*Clan Wars.*")
	),

	NECKLACE_OF_PASSAGE_WIZARD_TOWER (
			35, new WalkerTile(3113, 3179, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.NECKLACE_OF_PASSAGE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.NECKLACE_OF_PASSAGE_FILTER, "(?i).*wizard.+tower.*")
	),

	NECKLACE_OF_PASSAGE_OUTPOST (
			35, new WalkerTile(2430, 3347, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.NECKLACE_OF_PASSAGE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.NECKLACE_OF_PASSAGE_FILTER, "(?i).*the.+outpost.*")
	),

	NECKLACE_OF_PASSAGE_EYRIE (
			35, new WalkerTile(3406, 3156, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.NECKLACE_OF_PASSAGE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.NECKLACE_OF_PASSAGE_FILTER, "(?i).*eagl.+eyrie.*")
	),

	COMBAT_BRACE_WARRIORS_GUILD (
			35, new WalkerTile(2882, 3550, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.COMBAT_BRACE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.COMBAT_BRACE_FILTER, "(?i).*warrior.+guild.*")
	),

	COMBAT_BRACE_CHAMPIONS_GUILD (
			35, new WalkerTile(3190, 3366, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.COMBAT_BRACE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.COMBAT_BRACE_FILTER, "(?i).*champion.+guild.*")
	),

	COMBAT_BRACE_MONASTARY (
			35, new WalkerTile(3053, 3486, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.COMBAT_BRACE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.COMBAT_BRACE_FILTER, "(?i).*monastery.*")
	),

	COMBAT_BRACE_RANGE_GUILD (
			35, new WalkerTile(2656, 3442, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.COMBAT_BRACE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.COMBAT_BRACE_FILTER, "(?i).*rang.+guild.*")
	),

	GAMES_NECK_BURTHORPE (
			35, new WalkerTile(2897, 3551, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.GAMES_NECKLACE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.GAMES_NECKLACE_FILTER, "(?i).*burthorpe.*")
	),

	GAMES_NECK_BARBARIAN_OUTPOST (
			35, new WalkerTile(2520, 3570, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.GAMES_NECKLACE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.GAMES_NECKLACE_FILTER, "(?i).*barbarian.*")
	),

	GAMES_NECK_CORPOREAL (
			35, new WalkerTile(2965, 4832, 2),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.GAMES_NECKLACE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.GAMES_NECKLACE_FILTER, "(?i).*corpreal.*")
	),

	GAMES_NECK_WINTERTODT (
			35, new WalkerTile(1623, 3937, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.GAMES_NECKLACE_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.GAMES_NECKLACE_FILTER, "(?i).*wintertodt.*")
	),

	GLORY_EDGEVILLE (
			35, new WalkerTile(3087, 3496, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.GLORY_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.GLORY_FILTER, "(?i).*edgeville.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	GLORY_KARAMJA (
			35, new WalkerTile(2918, 3176, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.GLORY_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.GLORY_FILTER,"(?i).*karamja.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	GLORY_DRAYNOR (
			35, new WalkerTile(3105, 3251, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.GLORY_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.GLORY_FILTER,"(?i).*draynor.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	GLORY_AL_KHARID (
			35, new WalkerTile(3293, 3163, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.GLORY_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.GLORY_FILTER, "(?i).*al kharid.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	SKILLS_FISHING_GUILD (
			35, new WalkerTile(2610, 3391, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.SKILLS_FILTER),
			() -> teleportWithScrollInterface(WearableItemTeleport.SKILLS_FILTER, ".*Fishing.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	SKILLS_MINING_GUILD (
			35, new WalkerTile(3293, 3163, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.SKILLS_FILTER),
			() -> teleportWithScrollInterface(WearableItemTeleport.SKILLS_FILTER, ".*Mining.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	SKILLS_CRAFTING_GUILD (
			35, new WalkerTile(3293, 3163, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.SKILLS_FILTER),
			() -> teleportWithScrollInterface(WearableItemTeleport.SKILLS_FILTER, ".*Craft.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	SKILLS_COOKING_GUILD (
			35, new WalkerTile(3293, 3163, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.SKILLS_FILTER),
			() -> teleportWithScrollInterface(WearableItemTeleport.SKILLS_FILTER, ".*Cooking.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	SKILLS_WOODCUTTING_GUILD (
			35, new WalkerTile(3293, 3163, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.SKILLS_FILTER),
			() -> teleportWithScrollInterface(WearableItemTeleport.SKILLS_FILTER, ".*Woodcutting.*"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	BURNING_AMULET_CHAOS_TEMPLE (
			35, new WalkerTile(3236, 3635, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.BURNING_AMULET_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.BURNING_AMULET_FILTER, "(Chaos.*|Okay, teleport to level.*)"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	BURNING_AMULET_BANDIT_CAMP (
			35, new WalkerTile(3039, 3652, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.BURNING_AMULET_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.BURNING_AMULET_FILTER, "(Bandit.*|Okay, teleport to level.*)"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	BURNING_AMULET_LAVA_MAZE (
			35, new WalkerTile(3029, 3843, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.BURNING_AMULET_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.BURNING_AMULET_FILTER, "(Lava.*|Okay, teleport to level.*)"),
			TeleportConstants.LEVEL_30_WILDERNESS_LIMIT
	),

	DIGSITE_PENDANT (
			35, new WalkerTile(3346,3445,0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.DIGSITE_PENDANT_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.DIGSITE_PENDANT_FILTER, "Digsite")
	),

	ECTOPHIAL (
			0, new WalkerTile(3660, 3524, 0),
			() -> inMembersWorld() && Web.methods.inventory.find(Filters.Items.nameContains("Ectophial")).length > 0,
			() -> RSItemHelper.click(Filters.Items.nameContains("Ectophial"), "Empty")
	),

	LLETYA (
			35, new WalkerTile(2330,3172,0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.TELEPORT_CRYSTAL_FILTER),
			() -> WearableItemTeleport.teleport(WearableItemTeleport.TELEPORT_CRYSTAL_FILTER, "Lletya")
	),

	XERICS_GLADE(
			35, new WalkerTile(1753, 3565, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.XERICS_TALISMAN_FILTER),
			() -> teleportWithScrollInterface(WearableItemTeleport.XERICS_TALISMAN_FILTER, ".*Xeric's Glade")
	),
	XERICS_INFERNO(
			35, new WalkerTile(1505,3809,0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.XERICS_TALISMAN_FILTER),
			() -> teleportWithScrollInterface(WearableItemTeleport.XERICS_TALISMAN_FILTER, ".*Xeric's Inferno")
	),
	XERICS_LOOKOUT(
			35, new WalkerTile(1575, 3531, 0),
			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.XERICS_TALISMAN_FILTER),
			() -> teleportWithScrollInterface(WearableItemTeleport.XERICS_TALISMAN_FILTER, ".*Xeric's Look-out")
	),

	WEST_ARDOUGNE_TELEPORT_TAB(
			35, new WalkerTile(2500,3290,0),
			() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("West ardougne teleport")) > 0,
					() -> RSItemHelper.click("West ardougne t.*", "Break")
			),

			RADAS_BLESSING_KOUREND_WOODLAND(
					0, new WalkerTile(1558, 3458, 0),
					() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.RADAS_BLESSING_FILTER),
					() -> WearableItemTeleport.teleport(WearableItemTeleport.RADAS_BLESSING_FILTER, "Kourend .*")
			),
			RADAS_BLESSING_MOUNT_KARUULM(
					0, new WalkerTile(1310, 3796, 0),
					() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.RADAS_BLESSING_FILTER.and(Filters.Items.nameContains("3","4"))),
					() -> WearableItemTeleport.teleport(WearableItemTeleport.RADAS_BLESSING_FILTER, "Mount.*")
			),

			CRAFTING_CAPE_TELEPORT(
					0, new WalkerTile(2931, 3286, 0),
					() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.CRAFTING_CAPE_FILTER),
					() -> WearableItemTeleport.teleport(WearableItemTeleport.CRAFTING_CAPE_FILTER, "Teleport")
			),

			CABBAGE_PATCH_TELEPORT(
					0, new WalkerTile(3049, 3287, 0),
					() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.EXPLORERS_RING_FILTER),
					() -> WearableItemTeleport.teleport(WearableItemTeleport.EXPLORERS_RING_FILTER, "Teleport")
			),

			LEGENDS_GUILD_TELEPORT(
					0, new WalkerTile(2729, 3348, 0),
					() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.QUEST_CAPE_FILTER),
					() -> WearableItemTeleport.teleport(WearableItemTeleport.QUEST_CAPE_FILTER, "Teleport")
			),

			KANDARIN_MONASTERY_TELEPORT(
					0, new WalkerTile(2606, 3216, 0),
					() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.ARDOUGNE_CLOAK_FILTER),
					() -> WearableItemTeleport.teleport(WearableItemTeleport.ARDOUGNE_CLOAK_FILTER, "Monastery.*")
			),

			RIMMINGTON_TELEPORT_TAB(
					35, new WalkerTile(2954,3224, 0),
					() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Rimmington teleport")) > 0,
							() -> RSItemHelper.click("Rimmington t.*", "Break")
					),

					TAVERLEY_TELEPORT_TAB(
							35, new WalkerTile(2894, 3465, 0),
							() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Taverley teleport")) > 0,
									() -> RSItemHelper.click("Taverley t.*", "Break")
							),

							RELLEKKA_TELEPORT_TAB(
									35, new WalkerTile(2668, 3631, 0),
									() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Rellekka teleport")) > 0,
											() -> RSItemHelper.click("Rellekka t.*", "Break")
									),

									BRIMHAVEN_TELEPORT_TAB(
											35, new WalkerTile(2758, 3178, 0),
											() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Brimhaven teleport")) > 0,
													() -> RSItemHelper.click("Brimhaven t.*", "Break")
											),

											POLLNIVNEACH_TELEPORT_TAB(
													35, new WalkerTile(3340, 3004, 0),
													() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Pollnivneach teleport")) > 0,
															() -> RSItemHelper.click("Pollnivneach t.*", "Break")
													),

													YANILLE_TELEPORT_TAB(
															35, new WalkerTile(2544, 3095, 0),
															() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Yanille teleport")) > 0,
																	() -> RSItemHelper.click("Yanille t.*", "Break")
															),

															HOSIDIUS_TELEPORT_TAB(
																	35, new WalkerTile(1744, 3517, 0),
																	() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Hosidius teleport")) > 0,
																			() -> RSItemHelper.click("Hosidius t.*", "Break")
																	),

																	CONSTRUCTION_CAPE_RIMMINGTON(
																			0, new WalkerTile(2954,3224, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER),
																			() -> teleportWithScrollInterface(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER,".*Rimmington")
																	),

																	CONSTRUCTION_CAPE_TAVERLEY(
																			0, new WalkerTile(2894, 3465, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER),
																			() -> teleportWithScrollInterface(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER,".*Taverley")
																	),

																	CONSTRUCTION_CAPE_RELLEKKA(
																			0, new WalkerTile(2668, 3631, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER),
																			() -> teleportWithScrollInterface(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER,".*Rellekka")
																	),

																	CONSTRUCTION_CAPE_BRIMHAVEN(
																			0, new WalkerTile(2758, 3178, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER),
																			() -> teleportWithScrollInterface(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER,".*Brimhaven")
																	),

																	CONSTRUCTION_CAPE_POLLNIVNEACH(
																			0, new WalkerTile(3340, 3004, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER),
																			() -> teleportWithScrollInterface(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER,".*Pollnivneach")
																	),

																	CONSTRUCTION_CAPE_YANILLE(
																			0, new WalkerTile(2544, 3095, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER),
																			() -> teleportWithScrollInterface(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER,".*Yanille")
																	),

																	CONSTRUCTION_CAPE_HOSIDIUS(
																			0, new WalkerTile(1744, 3517, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER),
																			() -> teleportWithScrollInterface(WearableItemTeleport.CONSTRUCTION_CAPE_FILTER,".*Hosidius")
																	),

																	SLAYER_RING_GNOME_STRONGHOLD(
																			35, new WalkerTile(2433, 3424, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.SLAYER_RING),
																			() -> WearableItemTeleport.teleport(WearableItemTeleport.SLAYER_RING, ".*Stronghold")
																	),

																	SLAYER_RING_MORYTANIA(
																			35, new WalkerTile(3422, 3537, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.SLAYER_RING),
																			() -> WearableItemTeleport.teleport(WearableItemTeleport.SLAYER_RING, ".*Tower")
																	),

																	SLAYER_RING_RELLEKKA_CAVE(
																			35, new WalkerTile(2801, 9999, 0),
																			() -> inMembersWorld() && WearableItemTeleport.has(WearableItemTeleport.SLAYER_RING),
																			() -> WearableItemTeleport.teleport(WearableItemTeleport.SLAYER_RING, ".*Rellekka")
																	),

																	SALVE_GRAVEYARD_TAB(
																			35, new WalkerTile(3432, 3460, 0),
																			() -> inMembersWorld() && Web.methods.inventory.getCount(Web.methods.inventory.getItemID("Salve graveyard teleport")) > 0,
																					() -> RSItemHelper.click("Salve graveyard t.*", "Break")
																			)

	;
	private int moveCost;
	private WalkerTile location;
	private Requirement requirement;
	private Action action;
	private TeleportLimit teleportLimit;

	private boolean canUse = true;

	private int failedAttempts = 0;

	Teleport(int moveCost, WalkerTile location, Requirement requirement, Action action) {
		this.moveCost = moveCost;
		this.location = location;
		this.requirement = requirement;
		this.action = action;
		this.teleportLimit = TeleportConstants.LEVEL_20_WILDERNESS_LIMIT;
	}

	Teleport(int moveCost, WalkerTile location, Requirement requirement, Action action, TeleportLimit limit) {
		this.moveCost = moveCost;
		this.location = location;
		this.requirement = requirement;
		this.action = action;
		this.teleportLimit = limit;
	}

	Teleport(int movecost, TeleportScrolls scroll){
		this.moveCost = movecost;
		this.location = scroll.getLocation();
		this.requirement = () -> inMembersWorld() && scroll.canUse();
		this.action = () -> scroll.teleportTo(false);
		this.teleportLimit = TeleportConstants.LEVEL_20_WILDERNESS_LIMIT;
	}


	public int getMoveCost() {
		return moveCost;
	}

	public void setMoveCost(int cost){
		if(this.moveCost == 0)
			return;
		this.moveCost = cost;
	}

	public WalkerTile getLocation() {
		return location;
	}

	public Requirement getRequirement() {
		return requirement;
	}

	public boolean trigger() {
		boolean value = this.action.trigger();
		if(!value){
			failedAttempts++;
			if(failedAttempts > 3){
				canUse = false;
			}
		}
		return value;
	}

	public boolean isAtTeleportSpot(WalkerTile tile) {
		return tile.distanceTo(location) < 10;
	}

	public static void setMoveCosts(int moveCost){
		Arrays.stream(values()).forEach(t -> t.setMoveCost(moveCost));
	}

	private static List<Teleport> blacklist = new ArrayList<>();

	public static void blacklistTeleports(Teleport... teleports){
		blacklist.addAll(Arrays.asList(teleports));
	}

	public static void clearTeleportBlacklist(){
		blacklist.clear();
	}

	public static List<WalkerTile> getValidStartingWalkerTiles() {
		List<WalkerTile> WalkerTiles = new ArrayList<>();
		for (Teleport teleport : values()) {

			if (blacklist.contains(teleport) || !teleport.teleportLimit.canCast() ||
					!teleport.canUse || !teleport.requirement.satisfies()) continue;
			WalkerTiles.add(teleport.location);
		}
		return WalkerTiles;
	}

	private interface Action {
		boolean trigger();
	}

	private static boolean inMembersWorld() {
		return WorldHelper.isMember(Web.methods.worldHopper.getWorld());
	}

	private static Predicate<RSItem> notNotedFilter() {
		return rsItem -> rsItem.getDefinition() != null && rsItem.getDefinition().getNote() != 799;
	}

	private static boolean itemAction(String name, String... actions) {
		RSItem[] items = Web.methods.inventory.getItems(Web.methods.inventory.getItemID(name));
		if (items.length == 0) {
			return false;
		}
		for (String action : actions) {
			if (items[0].doAction(action)) {
				return true;
			}
		}
		return false;
	}



	private static boolean teleportWithScrollInterface(Predicate<RSItem> itemFilter, String regex){
		ArrayList<RSItem> items = new ArrayList<>();
		items.addAll(Arrays.asList(Web.methods.inventory.find(itemFilter)));
		items.addAll(Arrays.asList(Web.methods.equipment.find(itemFilter)));

		if (items.size() == 0) {
			return false;
		}

		if(!Web.methods.interfaces.isInterfaceSubstantiated(TeleportConstants.SCROLL_INTERFACE_MASTER)){
			RSItem teleportItem = items.get(0);
			if (!RSItemHelper.clickMatch(teleportItem, "(Rub|Teleport|" + regex + ")") ||
					!Timer.waitCondition(() -> Web.methods.interfaces.isInterfaceSubstantiated(
							TeleportConstants.SCROLL_INTERFACE_MASTER),2500)) {
				return false;
			}
		}

		return handleScrollInterface(regex);
	}

	private static boolean handleScrollInterface(String regex){
		RSWidget box = Web.methods.interfaces.get(187, 3);
		if(box == null)
			return false;
		RSWidget[] children = box.getComponents();
		if(children == null)
			return false;
		for(RSWidget child:children){
			String txt = child.getText();
			if(txt != null && Web.methods.menu.stripFormatting(txt).matches(regex)){
				Web.methods.keyboard.sendText(Web.methods.menu.stripFormatting(txt).substring(0,1), true);
				return true;
			}
		}
		return false;
	}

	private static boolean selectSpell(String spellName, String action){
		if(!Web.methods.game.openTab(Game.TAB_MAGIC)){
			return false;
		}
		List<RSWidget> spells = InterfaceHelper.getAllInterfaces(TeleportConstants.SPELLBOOK_INTERFACE_MASTER);
		RSWidget target = spells.stream().filter(spell -> {
			String name = spell.getName();
			return name != null && name.contains(spellName) && spell.isVisible();
		}).findFirst().orElse(null);
		return target != null && target.doAction(action);
	}
}
