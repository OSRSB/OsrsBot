package net.runelite.rsb.query;

import net.runelite.rsb.query.request.QueryRequest;
import net.runelite.rsb.query.request.RSInventoryItemQueryRequest;
import net.runelite.rsb.query.result.PositionableQueryResult;
import net.runelite.rsb.query.result.QueryResult;
import net.runelite.rsb.wrappers.RSItem;

import java.util.Arrays;

public class RSInventoryItemQueryBuilder extends AbstractQueryBuilder<RSItem, RSInventoryItemQueryBuilder, QueryResult<RSItem>, RSInventoryItemQueryRequest> {
        public RSInventoryItemQueryBuilder() {
            super();
        }

        public RSInventoryItemQueryBuilder notStackable() {
            return filter(item -> item.getDefinition().stackable > 0);
        }

        public RSInventoryItemQueryBuilder noted() {
            return filter(item -> item.getDefinition().notedID == item.getID());
        }

        public RSInventoryItemQueryBuilder unnoted() {
            return filter(item -> item.getDefinition().notedID != item.getID());
        }

        public RSInventoryItemQueryBuilder stackable() {
            return filter(item -> item.getDefinition().notedID == item.getID());
        }

        public RSInventoryItemQueryBuilder namedContains(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(arrayOfString)
                    .anyMatch(string -> item.getName().contains(string)));
        }

        public RSInventoryItemQueryBuilder notNamedContains(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(arrayOfString)
                    .noneMatch(string -> item.getName().contains(string)));
        }

        public RSInventoryItemQueryBuilder named(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(arrayOfString)
                    .anyMatch(string -> string.equals(item.getName())));
        }
        public RSInventoryItemQueryBuilder notNamed(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(arrayOfString)
                    .noneMatch(string -> string.equals(item.getName())));
        }

        public RSInventoryItemQueryBuilder id(int... arrayOfInt) {
            if (arrayOfInt == null) return this;
            return filter(item -> Arrays.stream(arrayOfInt)
                    .anyMatch(i -> i == item.getID()));
        }
        public RSInventoryItemQueryBuilder stackSize(int minimum) {
            return filter(item -> (item.getItem().getStackSize() >= minimum));
        }
        public RSInventoryItemQueryBuilder stackSize(int minimum, int maximum) {
            return filter(item -> (item.getItem().getStackSize() >= minimum && item.getItem().getStackSize() <= maximum));
        }

        public RSInventoryItemQueryBuilder actionsContains(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(item.getInventoryActions())
                    .filter((x) -> x != null)
                    .anyMatch(itemString -> Arrays.stream(arrayOfString)
                            .anyMatch(inputString -> itemString.contains(inputString))));
        }
        public RSInventoryItemQueryBuilder actions(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(item.getInventoryActions())
                    .filter((x) -> x != null)
                    .anyMatch(itemString -> Arrays.stream(arrayOfString)
                            .anyMatch(inputString -> itemString.equals(inputString))));
        }

        public RSInventoryItemQueryRequest createRequest() {
            return new RSInventoryItemQueryRequest();
        }
    }