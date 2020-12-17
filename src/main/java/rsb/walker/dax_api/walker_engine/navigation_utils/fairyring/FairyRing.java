package rsb.walker.dax_api.walker_engine.navigation_utils.fairyring;

import rsb.methods.Web;
import rsb.util.Timer;
import rsb.walker.dax_api.Filters;
import rsb.wrappers.RSWidget;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.walker_engine.WaitFor;
import rsb.walker.dax_api.walker_engine.interaction_handling.InteractionHelper;
import rsb.walker.dax_api.walker_engine.navigation_utils.fairyring.letters.FirstLetter;
import rsb.walker.dax_api.walker_engine.navigation_utils.fairyring.letters.SecondLetter;
import rsb.walker.dax_api.walker_engine.navigation_utils.fairyring.letters.ThirdLetter;
import rsb.wrappers.RSObject;

import java.util.Arrays;

import static rsb.walker.dax_api.walker_engine.navigation_utils.fairyring.letters.FirstLetter.*;
import static rsb.walker.dax_api.walker_engine.navigation_utils.fairyring.letters.SecondLetter.*;
import static rsb.walker.dax_api.walker_engine.navigation_utils.fairyring.letters.ThirdLetter.*;

public class FairyRing {

	public static final int
		INTERFACE_MASTER = 398,
		TELEPORT_CHILD = 26,
		ELITE_DIARY_VARBIT = 4538;
	private static final int[]
			DRAMEN_STAFFS = {772,9084};

	private static RSObject[] ring;


	private static RSWidget getTeleportButton() {
		return Web.methods.interfaces.get(INTERFACE_MASTER, TELEPORT_CHILD);
	}

	public static boolean takeFairyRing(Locations location){

		if(location == null)
			return false;
		if (Web.methods.client.getVarbitValue(ELITE_DIARY_VARBIT) == 0 && Arrays.stream(DRAMEN_STAFFS).allMatch((id) -> Web.methods.equipment.getCount(id) == 0));

		{
			if (!InteractionHelper.click(InteractionHelper.getRSItem(Filters.Items.idEquals(DRAMEN_STAFFS)), "Wield")){
				return false;
			}
		}
		if(!hasInterface()){
			if(hasCachedLocation(location)){
				return takeLastDestination(location);
			} else if(!openFairyRing()){
				return false;
			}
		}
		final WalkerTile myPos = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
		return location.turnTo() && pressTeleport() && Timer.waitCondition(() -> myPos.distanceTo(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())) > 20,8000);
	}

	private static boolean hasInterface(){
		return Web.methods.interfaces.isInterfaceSubstantiated(INTERFACE_MASTER);
	}

	private static boolean hasCachedLocation(Locations location){
		ring = new RSObject[] {Web.methods.objects.findNearest(25, "Fairy ring")};
		return ring.length > 0 && Filters.Objects.actionsContains(location.toString()).test(ring[0]);
	}

	private static boolean takeLastDestination(Locations location){
		final WalkerTile myPos = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
		return InteractionHelper.click(ring[0],"Last-destination (" + location + ")") &&
				Timer.waitCondition(() -> myPos.distanceTo(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())) > 20,8000);
	}

	private static boolean pressTeleport(){
		RSWidget iface = getTeleportButton();
		return iface != null && iface.doClick();
	}

	private static boolean openFairyRing(){
		if(ring.length == 0)
			return false;
		return InteractionHelper.click(ring[0],"Configure") &&
				Timer.waitCondition(() -> Web.methods.interfaces.isInterfaceSubstantiated(INTERFACE_MASTER),10000);
	}

	public enum Locations {
		ABYSSAL_AREA(A, L, R),
		ABYSSAL_NEXUS(D, I, P),
		APE_ATOLL(C, L, R),
		ARCEUUS_LIBRARY(C, I, S),
		ARDOUGNE_ZOO(B, I, S),
		CANIFIS(C, K, S),
		CHASM_OF_FIRE(D, J, R),
		COSMIC_ENTITYS_PLANE(C, K, P),
		DORGESH_KAAN_SOUTHERN_CAVE(A, J, Q),
		DRAYNOR_VILLAGE_ISLAND(C, L, P),
		EDGEVILLE(D, K, R),
		ENCHANTED_VALLEY(B, K, Q),
		FELDIP_HILLS_HUNTER_AREA(A, K, S),
		FISHER_KINGS_REALM(B, J, R),
		GORAKS_PLANE(D, I, R),
		HAUNTED_WOODS(A, L, Q),
		HAZELMERE(C, L, S),
		ISLAND_SOUTHEAST_ARDOUGNE(A, I, R),
		KALPHITE_HIVE(B, I, Q),
		KARAMJA_KARAMBWAN_SPOT(D, K, P),
		LEGENDS_GUILD(B, L, R),
		LIGHTHOUSE(A, L, P),
		MCGRUBOR_WOODS(A, L, S),
		MISCELLANIA(C, I, P),
		MISCELLANIA_PENGUINS(A, J, S),
		MORT_MYRE_ISLAND(B, I, P),
		MORT_MYRE_SWAMP(B, K, R),
		MOUNT_KARUULM(C, I, R),
		MUDSKIPPER_POINT(A, I, Q),
		MYREQUE_HIDEOUT(D, L, S),
		NORTH_OF_NARDAH(D, L, Q),
		PISCATORIS_HUNTER_AREA(A, K, Q),
		POH(D, I, Q),
		POISON_WASTE(D, L, R),
		POLAR_HUNTER_AREA(D, K, S),
		RELLEKKA_SLAYER_CAVE(A, J, R),
		SHILO_VILLAGE(C, K, R),
		SINCLAIR_MANSION(C, J, R),
		SOUTH_CASTLE_WARS(B, K, P),
		TOWER_OF_LIFE(D, J, P),
		TZHAAR(B, L, P),
		WIZARDS_TOWER(D, I, S),
		YANILLE(C, I, Q),
		ZANARIS(B, K, S),
		ZUL_ANDRA(B, J, S);

		FirstLetter first;
		SecondLetter second;
		ThirdLetter third;

		Locations(FirstLetter first, SecondLetter second, ThirdLetter third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}

		public boolean turnTo() {
			return first.turnTo() && WaitFor.milliseconds(200, 800) != null &&
					second.turnTo() && WaitFor.milliseconds(200, 800) != null &&
					third.turnTo() && WaitFor.milliseconds(200, 800) != null;
		}

		@Override
		public String toString() {
			return "" + first + second + third;
		}
	}
}
