package net.runelite.rsb.query.request;


import net.runelite.rsb.wrappers.RSPlayer;

import java.util.List;

import static net.runelite.rsb.methods.MethodProvider.methods;

public class RSPlayerQueryRequest extends PositionableQueryRequest<RSPlayer> {
    public RSPlayerQueryRequest() {
        super(Integer.MAX_VALUE);
    }
    public List<RSPlayer> request() {
        return List.of(methods.players.getAll());
    }
}