package rsb.walker.dax_api.walker.utils;

import net.runelite.api.ItemComposition;
import net.runelite.api.ObjectComposition;
import rsb.wrappers.*;

import java.util.Arrays;
import java.util.List;

public class TribotUtil {

    public static String getName(Object o) {
        if (o == null) return null;

        if (o instanceof RSObject) {
            ObjectComposition definition = ((RSObject) o).getDef();
            if (definition == null) return null;
            return definition.getName();
        }

        if (o instanceof RSItem) {
            ItemComposition definition = ((RSItem) o).getDefinition();
            if (definition == null) return null;
            return definition.getName();
        }

        if (o instanceof RSGroundItem) {
            ItemComposition definition = ((RSGroundItem) o).getItem().getDefinition();
            if (definition == null) return null;
            return definition.getName();
        }

        if (o instanceof RSPlayer) {
            return ((RSPlayer) o).getName();
        }

        if (o instanceof RSNPC) {
            return ((RSNPC) o).getName();
        }

        throw new IllegalStateException("Unknown object. Must be qualifying TriBot Object.");
    }

    public static List<String> getActions(Object o) {
        if (o == null) return null;

        if (o instanceof RSObject) {
            ObjectComposition definition = ((RSObject) o).getDef();
            if (definition == null) return null;
            return Arrays.asList(definition.getActions());
        }

        if (o instanceof RSItem) {
            ItemComposition definition = ((RSItem) o).getDefinition();
            if (definition == null) return null;
            return Arrays.asList(definition.getInventoryActions());
        }

        if (o instanceof RSGroundItem) {
            ItemComposition definition = ((RSGroundItem) o).getItem().getDefinition();
            if (definition == null) return null;
            return Arrays.asList(((RSGroundItem) o).getItem().getGroundActions());
        }

        throw new IllegalStateException("Unknown object. Must be qualifying TriBot Object.");
    }

}
