package net.runelite.rsb.query;


import net.runelite.rsb.query.request.RSObjectQueryRequest;
import net.runelite.rsb.query.result.PositionableQueryResult;
import net.runelite.rsb.wrappers.RSObject;

import java.util.Arrays;

public class RSObjectQueryBuilder extends PositionableQueryBuilder<RSObject, RSObjectQueryBuilder, PositionableQueryResult<RSObject>, RSObjectQueryRequest> {
    public RSObjectQueryBuilder() {
        super();
    }

    public RSObjectQueryBuilder namedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(object -> Arrays.stream(arrayOfString)
                .anyMatch(string -> object.getName().contains(string)));
    }

    public RSObjectQueryBuilder notNamedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(object -> Arrays.stream(arrayOfString)
                .noneMatch(string -> object.getName().contains(string)));
    }

    public RSObjectQueryBuilder named(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(object -> Arrays.stream(arrayOfString)
                .anyMatch(string -> string.equals(object.getName())));
    }
    public RSObjectQueryBuilder notNamed(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(object -> Arrays.stream(arrayOfString)
                .noneMatch(string -> string.equals(object.getName())));
    }

    public RSObjectQueryBuilder id(int... arrayOfInt) {
        if (arrayOfInt == null) return this;
        return filter(object -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == object.getID()));
    }
    public RSObjectQueryBuilder modelId(int... arrayOfInt) {
        if (arrayOfInt == null) return this;
        return filter(object -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == object.getModel().getModel().getSceneId()));
    }
    public RSObjectQueryBuilder actionsContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(object -> Arrays.stream(object.getDef().getActions())
                .filter((x) -> x != null)
                .anyMatch(objectString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> objectString.contains(inputString))));
    }
    public RSObjectQueryBuilder actions(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(object -> Arrays.stream(object.getDef().getActions())
                .filter((x) -> x != null)
                .anyMatch(objectString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> objectString.equals(inputString))));
    }

    public RSObjectQueryRequest createRequest() {
        return new RSObjectQueryRequest();
    }
}