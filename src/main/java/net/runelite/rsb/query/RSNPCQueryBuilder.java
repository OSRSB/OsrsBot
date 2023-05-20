package net.runelite.rsb.query;


import net.runelite.api.Actor;
import net.runelite.rsb.query.request.RSNPCQueryRequest;
import net.runelite.rsb.query.result.PositionableQueryResult;
import net.runelite.rsb.wrappers.RSNPC;

import java.util.Arrays;

import static net.runelite.rsb.methods.MethodProvider.methods;

public class RSNPCQueryBuilder extends PositionableQueryBuilder<RSNPC, RSNPCQueryBuilder, PositionableQueryResult<RSNPC>, RSNPCQueryRequest> {
    public RSNPCQueryBuilder() {
        super();
    }

    public RSNPCQueryBuilder inCombat() {
        return filter(npc -> npc.isInCombat());
    }
    public RSNPCQueryBuilder notInCombat() {
        return filter(npc -> !npc.isInCombat());
    }

    public RSNPCQueryBuilder isIdle() {
        return filter(npc -> npc.isIdle());
    }
    public RSNPCQueryBuilder notIdle() {
        return filter(npc -> !npc.isIdle());
    }

    public RSNPCQueryBuilder interactingWith(Actor actor) {
        return filter(npc -> npc.getInteracting().equals(actor));
    }

    public RSNPCQueryBuilder interactingWithMe() {
        return filter(npc -> npc.getInteracting().equals(methods.players.getMyPlayer().getAccessor()));
    }
    public RSNPCQueryBuilder notInteractingWithMe() {
        return filter(npc -> !npc.getInteracting().equals(methods.players.getMyPlayer().getAccessor()));
    }

    public RSNPCQueryBuilder animation(int... arrayOfInt) {
        return filter(npc -> Arrays.stream(arrayOfInt)
                .anyMatch(animation -> animation == npc.getAnimation()));
    }

    public RSNPCQueryBuilder notAnimation(int... arrayOfInt) {
        return filter(npc -> Arrays.stream(arrayOfInt)
                .noneMatch(animation -> animation == npc.getAnimation()));
    }

    public RSNPCQueryBuilder named(String... arrayOfString) {
        return filter(npc -> Arrays.stream(arrayOfString)
                .anyMatch(string -> npc.getName().toLowerCase().contains(string.toLowerCase())));
    }

    public RSNPCQueryBuilder notNamed(String... arrayOfString) {
        return filter(npc -> Arrays.stream(arrayOfString)
                .noneMatch(string -> npc.getName().toLowerCase().contains(string.toLowerCase())));
    }

    public RSNPCQueryBuilder namedExact(String... arrayOfString) {
        return filter(npc -> Arrays.stream(arrayOfString)
                .anyMatch(string -> string.equals(npc.getName())));
    }
    public RSNPCQueryBuilder notNamedExact(String... arrayOfString) {
        return filter(npc -> Arrays.stream(arrayOfString)
                .noneMatch(string -> string.equals(npc.getName())));
    }
    public RSNPCQueryBuilder id(int... arrayOfInt) {
        return filter(npc -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == npc.getID()));
    }
    public RSNPCQueryBuilder modelId(int... arrayOfInt) {
        return filter(npc -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == npc.getModel().getModel().getSceneId()));
    }

    public RSNPCQueryBuilder actions(String... arrayOfString) {
        return filter(npc -> Arrays.stream(npc.getDef().getActions())
                .anyMatch(actionString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> actionString.toLowerCase().contains(inputString.toLowerCase()))));
    }

    public RSNPCQueryBuilder actionsExact(String... arrayOfString) {
        return filter(npc -> Arrays.stream(npc.getDef().getActions())
                .anyMatch(actionString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> actionString.equals(inputString))));
    }

    public RSNPCQueryRequest createRequest() {
        return new RSNPCQueryRequest();
    }
}
