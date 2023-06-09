package net.runelite.rsb.query.request;

import net.runelite.rsb.wrappers.RSNPC;

import java.util.Arrays;
import java.util.List;

import static net.runelite.rsb.methods.MethodProvider.methods;

public class RSNPCQueryRequest extends PositionableQueryRequest<RSNPC> {
    public RSNPCQueryRequest() {
        super(Integer.MAX_VALUE);
    }

    public List request() {
        return Arrays.asList(methods.npcs.getAll());
    }
}
