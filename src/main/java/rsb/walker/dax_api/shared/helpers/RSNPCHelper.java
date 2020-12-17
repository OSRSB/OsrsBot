package rsb.walker.dax_api.shared.helpers;

import rsb.wrappers.RSNPC;


public class RSNPCHelper {

    public static String getName(RSNPC rsnpc){
        if (rsnpc == null){
            return null;
        }
        String name = rsnpc.getName();
        return name != null ? name : "null";
    }

    public static String[] getActions(RSNPC rsnpc){
        String[] actions = rsnpc.getActions();
        return actions != null ? actions : new String[0];
    }

}
