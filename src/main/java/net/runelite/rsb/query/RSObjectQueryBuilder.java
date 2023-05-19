package net.runelite.rsb.query;


import net.runelite.rsb.query.request.RSObjectQueryRequest;
import net.runelite.rsb.query.result.PositionableQueryResult;
import net.runelite.rsb.wrappers.RSObject;

import java.util.Arrays;

public class RSObjectQueryBuilder extends PositionableQueryBuilder<RSObject, RSObjectQueryBuilder, PositionableQueryResult<RSObject>, RSObjectQueryRequest> {
    public RSObjectQueryBuilder() {
        super();
    }

    public RSObjectQueryBuilder named(String... arrayOfString) {
        return filter(object -> Arrays.stream(arrayOfString)
                .anyMatch(string -> object.getName().toLowerCase().contains(string.toLowerCase())));
    }

    public RSObjectQueryBuilder notNamed(String... arrayOfString) {
        return filter(object -> Arrays.stream(arrayOfString)
                .noneMatch(string -> object.getName().toLowerCase().contains(string.toLowerCase())));
    }

    public RSObjectQueryBuilder namedExact(String... arrayOfString) {
        return filter(object -> Arrays.stream(arrayOfString)
                .anyMatch(string -> string.equals(object.getName())));
    }
    public RSObjectQueryBuilder notNamedExact(String... arrayOfString) {
        return filter(object -> Arrays.stream(arrayOfString)
                .noneMatch(string -> string.equals(object.getName())));
    }

    public RSObjectQueryBuilder actions(String... arrayOfString) {
        return filter(object -> Arrays.stream(object.getDef().getActions())
                .anyMatch(objectString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> objectString.toLowerCase().contains(inputString.toLowerCase()))));
    }
    public RSObjectQueryBuilder actionsExact(String... arrayOfString) {
        return filter(object -> Arrays.stream(object.getDef().getActions())
                .anyMatch(objectString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> objectString.equals(inputString))));
    }

    public RSObjectQueryRequest createRequest() {
        return new RSObjectQueryRequest();
    }
}