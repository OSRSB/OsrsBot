package net.runelite.rsb.query.request;

import net.runelite.rsb.wrappers.RSObject;

import java.util.Arrays;
import java.util.List;

import static net.runelite.rsb.methods.MethodProvider.methods;

public class RSObjectQueryRequest extends PositionableQueryRequest<RSObject> {
    public RSObjectQueryRequest() {
        super( 26);
    }
    public List<RSObject> request() {
        return Arrays.asList(methods.objects.getAll());
    }
}