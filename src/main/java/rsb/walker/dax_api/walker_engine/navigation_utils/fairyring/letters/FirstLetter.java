package rsb.walker.dax_api.walker_engine.navigation_utils.fairyring.letters;

import rsb.methods.Interfaces;
import rsb.methods.Web;
import rsb.util.StdRandom;
import rsb.util.Timer;
import rsb.walker.dax_api.walker_engine.navigation_utils.fairyring.FairyRing;
import rsb.wrappers.RSWidget;

public enum FirstLetter {
    A(0),
    B(3),
    C(2),
    D(1)
    ;
    int value;
    FirstLetter(int value){
        this.value = value;
    }


    public int getValue(){
        return value;
    }

    public static final int
            VARBIT = 3985,
            CLOCKWISE_CHILD = 19,
            ANTI_CLOCKWISE_CHILD = 20;

    private static int get(){
        return Web.methods.client.getVarbitValue(VARBIT);
    }

    public boolean isSelected(){
        return get() == this.value;
    }

    public boolean turnTo(){
        int current = get();
        int target = getValue();
        if(current == target)
            return true;
        int diff = current - target;
        int abs = Math.abs(diff);
        if(abs == 2){
            return StdRandom.uniform(0, 1) > .50 ? turnClockwise(2) : turnAntiClockwise(2);
        } else if(diff == 3 || diff == -1){
            return turnClockwise(1);
        } else {
            return turnAntiClockwise(1);
        }
    }

    public static boolean turnClockwise(int rotations){
        if(rotations == 0)
            return true;
        RSWidget iface = getClockwise();
        final int value = get();
        return iface != null && iface.doClick()
                && Timer.waitCondition(() -> get() != value ,2500)
                && turnClockwise(--rotations);
    }

    public static boolean turnAntiClockwise(int rotations){
        if(rotations == 0)
            return true;
        RSWidget iface = getAntiClockwise();
        final int value = get();
        return iface != null && iface.doClick()
                && Timer.waitCondition(() -> get() != value ,2500)
                && turnAntiClockwise(--rotations);
    }

    private static RSWidget getClockwise() {
        return Web.methods.interfaces.get(FairyRing.INTERFACE_MASTER, CLOCKWISE_CHILD);
    }
    private static RSWidget getAntiClockwise() {
        return Web.methods.interfaces.get(FairyRing.INTERFACE_MASTER, ANTI_CLOCKWISE_CHILD);
    }
}
