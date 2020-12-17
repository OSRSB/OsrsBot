package rsb.walker.dax_api.walker_engine.interaction_handling;

import rsb.methods.Web;
import rsb.walker.dax_api.shared.helpers.InterfaceHelper;
import rsb.walker.dax_api.walker_engine.Loggable;
import rsb.walker.dax_api.walker_engine.WaitFor;
import rsb.wrappers.RSWidget;

import java.util.Arrays;
import java.util.Optional;


public class DoomsToggle implements Loggable {

    private static final int STRONGHOLD_TOGGLE = 579, WILDERNESS_TOGGLE = 475, SHANTY_TOGGLE = 565, WATERBIRTH = 574, MORT_MYRE = 580, LUMBRIDGE_SWAMP = 572,
            OBSERVATORY_TOGGLE = 560;

    private static final int[] GENERAL_CASES = {STRONGHOLD_TOGGLE, WILDERNESS_TOGGLE, SHANTY_TOGGLE, WATERBIRTH, MORT_MYRE, LUMBRIDGE_SWAMP, OBSERVATORY_TOGGLE};

    private static DoomsToggle instance;

    private static DoomsToggle getInstance(){
        return instance != null ? instance : (instance = new DoomsToggle());
    }


    public static void handleToggle(){
        for (int generalCase : GENERAL_CASES){
            handle(generalCase, "Yes", "Enter Wilderness","Enter the swamp.","I'll be fine without a tinderbox.",
                    "Proceed regardless");
        }
    }

    public static void handle(int parentInterface, String... options){
        if (!Web.methods.interfaces.isInterfaceSubstantiated(parentInterface)){
            return;
        }
        getInstance().log("Handling Interface: " + parentInterface);
        Optional<RSWidget> optional = InterfaceHelper.getAllInterfaces(parentInterface).stream().filter(rsInterface -> {
            String[] actions = rsInterface.getActions();
            return actions != null && Arrays.stream(options).anyMatch(s -> Arrays.stream(actions).anyMatch(s1 -> s1.equals(s)));
        }).findAny();
        optional.ifPresent(rsInterface -> {
            for (String option : options) rsInterface.doAction(option);
        });
        WaitFor.milliseconds(500, 1500);
    }

    @Override
    public String getName() {
        return "Dooms Toggle";
    }

}
