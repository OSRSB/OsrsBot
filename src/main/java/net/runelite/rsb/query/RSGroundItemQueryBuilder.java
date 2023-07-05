package net.runelite.rsb.query;


import net.runelite.rsb.query.request.RSGroundItemQueryRequest;
import net.runelite.rsb.query.result.PositionableQueryResult;
import net.runelite.rsb.wrappers.RSGroundItem;

import java.util.Arrays;
import java.util.function.Predicate;

public class RSGroundItemQueryBuilder extends PositionableQueryBuilder<RSGroundItem, RSGroundItemQueryBuilder, PositionableQueryResult<RSGroundItem>, RSGroundItemQueryRequest> {
    public RSGroundItemQueryBuilder() {
        super();
    }

    public RSGroundItemQueryBuilder notStackable() {
        return filter(groundItem -> groundItem.getItem().getDefinition().stackable > 0);
    }

    public RSGroundItemQueryBuilder noted() {
        return filter(groundItem -> groundItem.getItem().getDefinition().notedID == groundItem.getItem().getID());
    }

    public RSGroundItemQueryBuilder unnoted() {
        return filter(groundItem -> groundItem.getItem().getDefinition().notedID != groundItem.getItem().getID());
    }

    public RSGroundItemQueryBuilder stackable() {
        return filter(groundItem -> groundItem.getItem().getDefinition().notedID == groundItem.getItem().getID());
    }

    public RSGroundItemQueryBuilder namedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(groundItem -> Arrays.stream(arrayOfString)
                .anyMatch(string -> groundItem.getItem().getName().contains(string)));
    }

    public RSGroundItemQueryBuilder notNamedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(groundItem -> Arrays.stream(arrayOfString)
                .noneMatch(string -> groundItem.getItem().getName().contains(string)));
    }

    public RSGroundItemQueryBuilder named(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(groundItem -> Arrays.stream(arrayOfString)
                .anyMatch(string -> string.equals(groundItem.getItem().getName())));
    }
    public RSGroundItemQueryBuilder notNamed(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(groundItem -> Arrays.stream(arrayOfString)
                .noneMatch(string -> string.equals(groundItem.getItem().getName())));
    }

    public RSGroundItemQueryBuilder id(int... arrayOfInt) {
        if (arrayOfInt == null) return this;
        return filter(groundItem -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == groundItem.getItem().getID()));
    }
    public RSGroundItemQueryBuilder modelId(int... arrayOfInt) {
        if (arrayOfInt == null) return this;
        return filter(groundItem -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == groundItem.getModel().getModel().getSceneId()));
    }
    public RSGroundItemQueryBuilder stackSize(int minimum) {
        return filter(groundItem -> (groundItem.getItem().getStackSize() >= minimum));
    }
    public RSGroundItemQueryBuilder stackSize(int minimum, int maximum) {
        return filter(groundItem -> (groundItem.getItem().getStackSize() >= minimum && groundItem.getItem().getStackSize() <= maximum));
    }

    public RSGroundItemQueryBuilder actionsContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(groundItem -> Arrays.stream(groundItem.getItem().getGroundActions())
                .filter((x) -> x != null)
                .anyMatch(itemString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> itemString.contains(inputString))));
    }
    public RSGroundItemQueryBuilder actions(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(groundItem -> Arrays.stream(groundItem.getItem().getGroundActions())
                .filter((x) -> x != null)
                .anyMatch(itemString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> itemString.equals(inputString))));
    }

    public RSGroundItemQueryRequest createRequest() {
        return new RSGroundItemQueryRequest();
    }
}
