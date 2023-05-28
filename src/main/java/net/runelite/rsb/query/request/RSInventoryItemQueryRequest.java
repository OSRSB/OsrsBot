package net.runelite.rsb.query.request;

import net.runelite.rsb.wrappers.RSItem;

import java.util.Arrays;
import java.util.List;

import static net.runelite.rsb.methods.MethodProvider.methods;

public class RSInventoryItemQueryRequest extends QueryRequest<RSItem> {
    public RSInventoryItemQueryRequest() {
        super();
    }

    public List request() {
        return Arrays.asList(methods.inventory.getItems());
    }
}