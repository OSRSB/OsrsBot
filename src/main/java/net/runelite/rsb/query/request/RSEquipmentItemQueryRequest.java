package net.runelite.rsb.query.request;

import net.runelite.rsb.wrappers.RSItem;

import java.util.Arrays;
import java.util.List;

import static net.runelite.rsb.methods.MethodProvider.methods;

public class RSEquipmentItemQueryRequest extends QueryRequest<RSItem> {
    public RSEquipmentItemQueryRequest() { super(); }

    @Override
    public List<RSItem> request() {
        return Arrays.asList(methods.equipment.getItems());
    }
}