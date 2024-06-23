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
        return filter(npc -> actor.equals(npc.getInteracting()));
    }

    public RSNPCQueryBuilder interactingWithMe() {
        return filter(npc -> methods.players.getMyPlayer().getAccessor().equals(npc.getInteracting()));
    }
    public RSNPCQueryBuilder notInteractingWithMe() {
        return filter(npc -> !methods.players.getMyPlayer().getAccessor().equals(npc.getInteracting()));
    }

    public RSNPCQueryBuilder animation(int... arrayOfInt) {
        if (arrayOfInt == null) return this;
        return filter(npc -> Arrays.stream(arrayOfInt)
                .anyMatch(animation -> animation == npc.getAnimation()));
    }

    public RSNPCQueryBuilder notAnimation(int... arrayOfInt) {
        if (arrayOfInt == null) return this;
        return filter(npc -> Arrays.stream(arrayOfInt)
                .noneMatch(animation -> animation == npc.getAnimation()));
    }

    public RSNPCQueryBuilder namedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(npc -> Arrays.stream(arrayOfString)
                .anyMatch(string -> npc.getName().toLowerCase().contains(string.toLowerCase())));
    }

    public RSNPCQueryBuilder notNamedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(npc -> Arrays.stream(arrayOfString)
                .noneMatch(string -> npc.getName().toLowerCase().contains(string.toLowerCase())));
    }

    public RSNPCQueryBuilder named(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(npc -> Arrays.stream(arrayOfString)
                .anyMatch(string -> string.equals(npc.getName())));
    }
    public RSNPCQueryBuilder notNamed(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(npc -> Arrays.stream(arrayOfString)
                .noneMatch(string -> string.equals(npc.getName())));
    }
    public RSNPCQueryBuilder id(int... arrayOfInt) {
        if (arrayOfInt == null) return this;
        return filter(npc -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == npc.getID()));
    }
    public RSNPCQueryBuilder modelId(int... arrayOfInt) {
        if (arrayOfInt == null) return this;
        return filter(npc -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == npc.getModel().getModel().getSceneId()));
    }

    public RSNPCQueryBuilder actionsContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(npc -> Arrays.stream(npc.getDef().getActions())
                .filter((x) -> x != null)
                .anyMatch(actionString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> actionString.contains(inputString))));
    }

    public RSNPCQueryBuilder actions(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        System.out.println("ARRAYOFSTRING: " + arrayOfString[0]);
        return filter(npc -> Arrays.stream(npc.getDef().getActions())
                .filter((x) -> x != null)
                .anyMatch(actionString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> actionString.equals(inputString))));
    }

    public RSNPCQueryRequest createRequest() {
        return new RSNPCQueryRequest();
    }
}
