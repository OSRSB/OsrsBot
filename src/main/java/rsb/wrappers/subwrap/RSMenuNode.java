package rsb.wrappers.subwrap;

import rsb.methods.Menu;
import rsb.wrappers.common.Clickable;

import java.awt.*;

public class RSMenuNode {


    private int index;
    private Rectangle area;
    private String action;
    private String target;
    private int type;
    private int data1;
    private int data2;
    private int data3;


    RSMenuNode(int index, Rectangle area, String action, String target, int type, int data1, int data2, int data3){
        this.index = index;
        this.area = area;
        this.action = action;
        this.target = target;
        this.type = type;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    public boolean contains(String... text) {
        for (String check : text) {
            if ((this.action + " " + this.target).contains(check)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsTarget(String... text) {
        for (String check : text) {
            if ((this.target).equals(check)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAction(String... text) {
        for (String check : text) {
            if ((this.action).equals(check)) {
                return true;
            }
        }
        return false;
    }

    /**
     * TODO: Properly implement feature
     * Checks if the RSMenuNode is associated with an option of a clickable entity
     * @param clickable The clickable object to check
     * @return <tt>True</tt> if the RSMenuNode is associated with the clickable
     */
    public boolean correlatesTo(Clickable clickable) {
        return false;
    }


    /*
    public boolean equals(Object o) {
    }
     */


    public int getIndex() {
        return index;
    }

    public Rectangle getArea() {
        return area;
    }

    public String getAction() {
        return Menu.stripFormatting(action);
    }

    public String getTarget() {
        return Menu.stripFormatting(target);
    }

    public int getType() {
        return type;
    }

    public int getData1() {
        return data1;
    }

    public int getData2() {
        return data2;
    }

    public int getData3() {
        return data3;
    }
}
