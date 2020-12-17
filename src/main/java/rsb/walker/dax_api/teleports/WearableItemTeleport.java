package rsb.walker.dax_api.teleports;

import rsb.methods.Web;
import rsb.util.StdRandom;
import rsb.walker.dax_api.Filters;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.shared.helpers.RSItemHelper;
import rsb.walker.dax_api.walker_engine.WaitFor;
import rsb.walker.dax_api.walker_engine.interaction_handling.NPCInteraction;
import rsb.wrappers.RSItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

public class WearableItemTeleport {

	public static final Predicate<RSItem> RING_OF_WEALTH_FILTER = Filters.Items.nameContains("Ring of wealth (").and(i -> i.getDefinition().getName().matches(".*[1-5]\\)"));
	public static final Predicate<RSItem> RING_OF_DUELING_FILTER = Filters.Items.nameContains("Ring of dueling");
	public static final Predicate<RSItem> NECKLACE_OF_PASSAGE_FILTER = Filters.Items.nameContains("Necklace of passage");
	public static final Predicate<RSItem> COMBAT_BRACE_FILTER = Filters.Items.nameContains("Combat bracelet(");
	public static final Predicate<RSItem> GAMES_NECKLACE_FILTER = Filters.Items.nameContains("Games necklace");
	public static final Predicate<RSItem> GLORY_FILTER = Filters.Items.nameContains("glory").and(Filters.Items.nameContains("eternal","("));
	public static final Predicate<RSItem> SKILLS_FILTER = Filters.Items.nameContains("Skills necklace(");
	public static final Predicate<RSItem> BURNING_AMULET_FILTER = Filters.Items.nameContains("Burning amulet(");
	public static final Predicate<RSItem> DIGSITE_PENDANT_FILTER = Filters.Items.nameContains("Digsite pendant");
	public static final Predicate<RSItem> TELEPORT_CRYSTAL_FILTER = Filters.Items.nameContains("Teleport crystal");
	public static final Predicate<RSItem> XERICS_TALISMAN_FILTER = Filters.Items.nameEquals("Xeric's talisman");
	public static final Predicate<RSItem> RADAS_BLESSING_FILTER = Filters.Items.nameContains("Rada's blessing");
	public static final Predicate<RSItem> CRAFTING_CAPE_FILTER = Filters.Items.nameContains("Crafting cape");
	public static final Predicate<RSItem> EXPLORERS_RING_FILTER = Filters.Items.nameContains("Explorer's ring");
	public static final Predicate<RSItem> QUEST_CAPE_FILTER = Filters.Items.nameContains("Quest point cape");
	public static final Predicate<RSItem> ARDOUGNE_CLOAK_FILTER = Filters.Items.nameContains("Ardougne cloak");
	public static final Predicate<RSItem> CONSTRUCTION_CAPE_FILTER = Filters.Items.nameContains("Construct. cape");
	public static final Predicate<RSItem> SLAYER_RING = Filters.Items.nameContains("Slayer ring");


	private WearableItemTeleport() {

	}

	public static boolean has(Predicate<RSItem> filter) {
		return Web.methods.inventory.find(filter).length > 0 || Web.methods.equipment.find(filter).length > 0;
	}

	public static boolean teleport(Predicate<RSItem> filter, String action) {
		return teleportWithItem(filter,action);
	}


	private static boolean teleportWithItem(Predicate<RSItem> itemFilter, String regex) {
		ArrayList<RSItem> items = new ArrayList<>();
		items.addAll(Arrays.asList(Web.methods.inventory.find(itemFilter)));
		items.addAll(Arrays.asList(Web.methods.equipment.find(itemFilter)));

		if (items.size() == 0) {
			return false;
		}

		RSItem teleportItem = items.get(0);
		final WalkerTile startingPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));

		return RSItemHelper.clickMatch(teleportItem, "(Rub|Teleport|" + regex + ")") && WaitFor.condition(
				StdRandom.uniform(3800, 4600), () -> {
					NPCInteraction.handleConversationRegex(regex);
					if (startingPosition.distanceTo(new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()))) > 5) {
						return WaitFor.Return.SUCCESS;
					}
					return WaitFor.Return.IGNORE;
				}) == WaitFor.Return.SUCCESS;
	}

}