package net.runelite.rsb.query;

import net.runelite.rsb.query.request.RSEquipmentItemQueryRequest;
import net.runelite.rsb.query.request.RSInventoryItemQueryRequest;
import net.runelite.rsb.query.result.QueryResult;
import net.runelite.rsb.wrappers.RSItem;

import java.util.Arrays;

public class RSEquipmentItemQueryBuilder extends AbstractQueryBuilder<RSItem, RSEquipmentItemQueryBuilder, QueryResult<RSItem>, RSEquipmentItemQueryRequest> {
    public RSEquipmentItemQueryBuilder() {
        super();
    }

    public RSEquipmentItemQueryBuilder notStackable() {
        return filter(item -> item.getDefinition().stackable > 0);
    }

    public net.runelite.rsb.query.RSEquipmentItemQueryBuilder stackable() {
        return filter(item -> item.getDefinition().notedID == item.getID());
    }

    public RSEquipmentItemQueryBuilder namedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(item -> Arrays.stream(arrayOfString)
                .anyMatch(string -> item.getName().contains(string)));
    }

    public RSEquipmentItemQueryBuilder notNamedContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(item -> Arrays.stream(arrayOfString)
                .noneMatch(string -> item.getName().contains(string)));
    }

    public RSEquipmentItemQueryBuilder named(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(item -> Arrays.stream(arrayOfString)
                .anyMatch(string -> string.equals(item.getName())));
    }
    public RSEquipmentItemQueryBuilder notNamed(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(item -> Arrays.stream(arrayOfString)
                .noneMatch(string -> string.equals(item.getName())));
    }

    public RSEquipmentItemQueryBuilder id(int... arrayOfInt) {
        if (arrayOfInt == null) return this;
        return filter(item -> Arrays.stream(arrayOfInt)
                .anyMatch(i -> i == item.getID()));
    }
    public RSEquipmentItemQueryBuilder stackSize(int minimum) {
        return filter(item -> (item.getItem().getStackSize() >= minimum));
    }
    public RSEquipmentItemQueryBuilder stackSize(int minimum, int maximum) {
        return filter(item -> (item.getItem().getStackSize() >= minimum && item.getItem().getStackSize() <= maximum));
    }

    public RSEquipmentItemQueryBuilder actionsContains(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(item -> Arrays.stream(item.getInventoryActions())
                .filter((x) -> x != null)
                .anyMatch(itemString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> itemString.contains(inputString))));
    }
    public RSEquipmentItemQueryBuilder actions(String... arrayOfString) {
        if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
        return filter(item -> Arrays.stream(item.getInventoryActions())
                .filter((x) -> x != null)
                .anyMatch(itemString -> Arrays.stream(arrayOfString)
                        .anyMatch(inputString -> itemString.equals(inputString))));
    }

    public RSEquipmentItemQueryRequest createRequest() {
        return new RSEquipmentItemQueryRequest();
    }
}