package rsb.walker.dax_api.shared.helpers;


import rsb.methods.Web;
import rsb.wrappers.RSWidget;

import java.util.*;

public class InterfaceHelper {

    /**
     *
     * @param ids
     * @return never null
     */
    public static List<RSWidget> getAllInterfaces(int... ids){
        ArrayList<RSWidget> interfaces = new ArrayList<>();

        for (int id : ids) {
            Queue<RSWidget> queue = new LinkedList<>();
            RSWidget master = Web.methods.interfaces.get(id);

            if (master == null) {
                return interfaces;
            }

            queue.add(master);
            RSWidget[] components = master.getComponents();
            if (components != null) {
                Collections.addAll(queue, components);
            }

            while (!queue.isEmpty()) {
                RSWidget RSWidget = queue.poll();
                interfaces.add(RSWidget);
                RSWidget[] children = RSWidget.getComponents();
                if (children != null) {
                    Collections.addAll(queue, children);
                }
            }
        }

        return interfaces;
    }

    public static List<RSWidget> getAllInterfaces(RSWidget parent){
        ArrayList<RSWidget> interfaces = new ArrayList<>();
        Queue<RSWidget> queue = new LinkedList<>();

        if (parent == null){
            return interfaces;
        }

        queue.add(parent);
        while (!queue.isEmpty()){
            RSWidget RSWidget = queue.poll();
            interfaces.add(RSWidget);
            RSWidget[] children = RSWidget.getComponents();
            if (children != null) {
                Collections.addAll(queue, children);
            }
        }

        return interfaces;
    }

    public static boolean textEquals(RSWidget RSWidget, String match){
        String text = RSWidget.getText();
        return text != null && text.equals(match);
    }

    public static boolean textContains(RSWidget RSWidget, String match){
        String text = RSWidget.getText();
        return text != null && text.contains(match);
    }

    public static boolean textMatches(RSWidget RSWidget, String match){
        if (RSWidget == null){
            return false;
        }
        String text = RSWidget.getText();
        return text != null && text.matches(match);
    }

    public static List<String> getActions(RSWidget RSWidget){
        if (RSWidget == null){
            return Collections.emptyList();
        }
        String[] actions = RSWidget.getActions();
        if (actions == null){
            return Collections.emptyList();
        }
        return Arrays.asList(actions);
    }

}
