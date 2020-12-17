package rsb.walker.dax_api.teleports.teleport_utils;

import rsb.methods.Web;
import rsb.util.StdRandom;
import rsb.util.Timer;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.wrappers.RSItem;
import rsb.wrappers.RSWidget;
import rsb.wrappers.common.Clickable07;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class MasterScrollBook {

	public static final int
			INTERFACE_MASTER = 597, DEFAULT_VARBIT = 5685,
			SELECT_OPTION_MASTER = 219, SELECT_OPTION_CHILD = 1,
			GAMETABS_INTERFACE_MASTER = 161;
	private static Map<String, Integer> cache = new HashMap<String, Integer>();
	
	public enum Teleports {
		NARDAH(5672,"Nardah", TeleportScrolls.NARDAH.getLocation()),
		DIGSITE(5673,"Digsite", TeleportScrolls.DIGSITE.getLocation()),
		FELDIP_HILLS(5674,"Feldip Hills", TeleportScrolls.FELDIP_HILLS.getLocation()),
		LUNAR_ISLE(5675,"Lunar Isle", TeleportScrolls.LUNAR_ISLE.getLocation()),
		MORTTON(5676,"Mort'ton", TeleportScrolls.MORTTON.getLocation()),
		PEST_CONTROL(5677,"Pest Control", TeleportScrolls.PEST_CONTROL.getLocation()),
		PISCATORIS(5678,"Piscatoris", TeleportScrolls.PISCATORIS.getLocation()),
		TAI_BWO_WANNAI(5679,"Tai Bwo Wannai", TeleportScrolls.TAI_BWO_WANNAI.getLocation()),
		ELF_CAMP(5680,"Elf Camp", TeleportScrolls.ELF_CAMP.getLocation()),
		MOS_LE_HARMLESS(5681,"Mos Le'Harmless", TeleportScrolls.MOS_LE_HARMLESS.getLocation()),
		LUMBERYARD(5682,"Lumberyard", TeleportScrolls.LUMBERYARD.getLocation()),
		ZULLANDRA(5683,"Zul-Andra", TeleportScrolls.ZULLANDRA.getLocation()),
		KEY_MASTER(5684,"Key Master", TeleportScrolls.KEY_MASTER.getLocation()),
		REVENANT_CAVES(6056,"Revenant cave", TeleportScrolls.REVENANT_CAVES.getLocation()),
		WATSON(8253, "Watson", TeleportScrolls.WATSON.getLocation());
		
		private int varbit;
		private String name;
		private WalkerTile destination;
		Teleports(int varbit, String name, WalkerTile destination){
			this.varbit = varbit;
			this.name = name;
			this.destination = destination;
		}
		
		//Returns the number of scrolls stored in the book.
		public int getCount(){
			return Web.methods.client.getVarbitValue(varbit);
		}
		
		//Returns the name of the teleport.
		public String getName(){
			return name;
		}
		
		//Returns the destination that the teleport will take you to.
		public WalkerTile getDestination(){
			return destination;
		}
		
		//Sets the teleport as the default left-click option of the book.
		public boolean setAsDefault(){
			if(Web.methods.npcChat.getOptions() != null){
				String text = getDefaultTeleportText();
				if(text.contains(this.getName())){
					return Web.methods.npcChat.selectOption("Yes", true);
				}
			}
			if(!isOpen()){
				openBook();
			}
			RSWidget target = getInterface(this);
			if(target == null)
				return false;
			return click(target,"Set as default") && waitForOptions() && Web.methods.npcChat.selectOption("Yes", true);
			
		}
		
		//Uses the teleport and waits until you arrive at the destination.
		public boolean use(){
			if(this == getDefault()){
				RSItem[] book = getBook();
				return book.length > 0 && click(book[0],"Teleport") && waitTillAtDestination(this);
			}
			if(this == REVENANT_CAVES) // bug where you can't activate it from the interface for whatever reason.
				return setAsDefault() && use();
			if(!isOpen() && !openBook())
				return false;
			RSWidget target = getInterface(this);
			return target != null && click(target, "Activate") && waitTillAtDestination(this);
		}
		
	}
	
	public static boolean teleport(Teleports teleport){
		return teleport != null && teleport.getCount() > 0 && teleport.use();
	}
	
	public static int getCount(Teleports teleport){
		return teleport != null ? teleport.getCount() : 0;
	}
	
	public static boolean isDefault(Teleports teleport){
		return getDefault() == teleport;
	}
	
	public static boolean setAsDefault(Teleports teleport){
		return teleport != null && teleport.setAsDefault();
	}
	
	public static Teleports getDefault(){
		int value = Web.methods.client.getVarbitValue(DEFAULT_VARBIT);
		if(value == 0)
			return null;
		return Teleports.values()[value-1];
	}
	
	//Removes the default left click teleport option.
	public static boolean removeDefault(){
		RSItem[] book = getBook();
		if(Web.methods.chooseOption.getHoverText().contains("->")){
			resetUptext();
		}
		return book.length > 0 && click(book[0],"Remove default") && waitForOptions() && Web.methods.npcChat.selectOption("Yes", true);
	}
	
	//Caches the index and returns the RSInterface associated with the selected teleport.
	private static RSWidget getInterface(Teleports teleport){
		if(cache.containsKey(teleport.getName())){
			return Web.methods.interfaces.get(INTERFACE_MASTER,cache.get(teleport.getName()));
		}
		RSWidget master = Web.methods.interfaces.get(INTERFACE_MASTER);
		if(master == null)
			return null;
		for(RSWidget child:master.getComponents()){
			String name = child.getName();
			if(name == null){
				continue;
			} else if(name.startsWith("<") && Web.methods.menu.stripFormatting(name).contains(teleport.getName())){
				cache.put(teleport.getName(), child.getIndex());
				return child;
			}
		}
		return null;
	}
	
	//Returns true if the Master scroll book interface is open.
	public static boolean isOpen(){
		return Web.methods.interfaces.isInterfaceSubstantiated(INTERFACE_MASTER);
	}
	
	//Opens the master scroll book interface.
	public static boolean openBook(){
		RSItem[] book = getBook();
		if(Web.methods.chooseOption.getHoverText().contains("->")){
			resetUptext();
		}
		return book.length > 0 && click(book[0],"Open") && waitForBookToOpen();
	}


	public static boolean hasBook(){
		return getBook().length > 0;
	}

	public static boolean has(){
		return getBook().length > 0;
	}

	private static RSItem[] getBook(){
		return Web.methods.inventory.getItems(Web.methods.inventory.getItemID("Master scroll book"));
	}
	
	private static boolean waitForBookToOpen(){
		return Timer.waitCondition(new BooleanSupplier(){

			@Override
			public boolean getAsBoolean() {
				Web.methods.web.sleep(StdRandom.uniform(50,200));
				return isOpen();
			}
			
		}, 5000);
	}
	
	private static boolean waitForOptions(){
		return Timer.waitCondition(new BooleanSupplier(){

			@Override
			public boolean getAsBoolean() {
				Web.methods.web.sleep(StdRandom.uniform(50,200));
				return Web.methods.npcChat.getOptions().length > 0;
			}
			
		}, 5000);
	}
	
	//Checks which scroll we are setting to default currently.
	private static String getDefaultTeleportText(){
		RSWidget master = Web.methods.interfaces.get(SELECT_OPTION_MASTER,SELECT_OPTION_CHILD);
		if(master == null)
			return null;
		RSWidget[] ifaces = master.getComponents();
		if(ifaces == null)
			return null;
		for(RSWidget iface:ifaces){
			String txt = iface.getText();
			if(txt == null || !txt.startsWith("Set"))
				continue;
			return txt;
		}
		return null;
	}
	
	//Resets uptext.
	private static void resetUptext(){
		RSWidget master = Web.methods.interfaces.get(GAMETABS_INTERFACE_MASTER);
		RSWidget[] children = master.getComponents();
		if(children == null)
			return;
		RSWidget inventory = null;
		for(RSWidget child:children){
			String[] actions = child.getActions();
			if(actions == null || actions.length == 0)
				continue;
			if(Arrays.asList(actions).contains("Inventory")){
				inventory = child;
				break;
			}
		}
		if(inventory != null)
			inventory.doClick();
	}
	
	private static boolean waitTillAtDestination(Teleports location){
		return Timer.waitCondition(new BooleanSupplier(){

			@Override
			public boolean getAsBoolean() {
				Web.methods.web.sleep(StdRandom.uniform(50,200));
				return location.getDestination().distanceTo(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())) < 10;
			}
			
		}, 8000);
	}
	
	private static boolean click(Clickable07 clickable, String action){
		if(Web.methods.chooseOption.getHoverText().contains("->") && !action.contains("->")){
			resetUptext();
		}
		return clickable.doAction(action);
	}
	
	
}
