package net.runelite.rsb.query.request;

import net.runelite.rsb.query.result.QueryResult;
import net.runelite.rsb.wrappers.RSWidgetItem;

import java.util.List;

import static net.runelite.rsb.methods.MethodProvider.methods;

public class RSWidgetItemQueryRequest extends QueryRequest<RSWidgetItem> {
    public RSWidgetItemQueryRequest() {
        super();
    }
    public List<RSWidgetItem> request() {
        // TODO: add widgets to methods.
        return null;
    }
}
