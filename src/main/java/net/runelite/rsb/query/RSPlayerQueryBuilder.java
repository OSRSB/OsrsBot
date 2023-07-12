package net.runelite.rsb.query;


import net.runelite.api.Actor;
import net.runelite.rsb.query.request.RSPlayerQueryRequest;
import net.runelite.rsb.query.result.PositionableQueryResult;
import net.runelite.rsb.wrappers.RSPlayer;

import java.util.Arrays;

import static net.runelite.rsb.methods.MethodProvider.methods;

public class RSPlayerQueryBuilder extends PositionableQueryBuilder<RSPlayer, RSPlayerQueryBuilder, PositionableQueryResult<RSPlayer>, RSPlayerQueryRequest> {
    public RSPlayerQueryBuilder() {
        super();
    }
    public RSPlayerQueryBuilder notMe() {
        return filter((player) -> {
            return !player.equals(methods.players.getMyPlayer());
        });
    }
    // TODO: add overhead icons from skull and prayers

    public RSPlayerQueryBuilder inCombat() {
        return filter(player -> player.isInCombat());
    }
    public RSPlayerQueryBuilder notInCombat() {
        return filter(player -> !player.isInCombat());
    }

    public RSPlayerQueryBuilder isIdle() {
        return filter(player -> player.isIdle());
    }
    public RSPlayerQueryBuilder notIdle() {
        return filter(player -> !player.isIdle());
    }

    public RSPlayerQueryBuilder interactingWith(Actor actor) {
        return filter(player -> actor.equals(player.getInteracting()));
    }

    public RSPlayerQueryBuilder interactingWithMe() {
        return filter(player -> methods.players.getMyPlayer().getAccessor().equals(player.getInteracting()));
    }
    public RSPlayerQueryBuilder notInteractingWithMe() {
        return filter(player -> !methods.players.getMyPlayer().getAccessor().equals(player.getInteracting()));
    }

    public RSPlayerQueryBuilder animation(int... arrayOfInt) {
        return filter(player -> Arrays.stream(arrayOfInt)
                .anyMatch(animation -> animation == player.getAnimation()));
    }

    public RSPlayerQueryBuilder notAnimation(int... arrayOfInt) {
        return filter(player -> Arrays.stream(arrayOfInt)
                .noneMatch(animation -> animation == player.getAnimation()));
    }

    public RSPlayerQueryBuilder namedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(player -> Arrays.stream(arrayOfString)
                .anyMatch(string -> player.getName().contains(string)));
    }

    public RSPlayerQueryBuilder notNamedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(player -> Arrays.stream(arrayOfString)
                .noneMatch(string -> player.getName().contains(string)));
    }

    public RSPlayerQueryBuilder named(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(player -> Arrays.stream(arrayOfString)
                .anyMatch(string -> string.equals(player.getName())));
    }
    public RSPlayerQueryBuilder notNamed(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(player -> Arrays.stream(arrayOfString)
                .noneMatch(string -> string.equals(player.getName())));
    }

    public RSPlayerQueryBuilder modelId(int... arrayOfInt) {
        if (arrayOfInt == null ) return this;
        return filter(player -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == player.getModel().getModel().getSceneId()));
    }

    public RSPlayerQueryRequest createRequest() {
        return new RSPlayerQueryRequest();
    }

}
