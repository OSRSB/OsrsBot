package net.runelite.rsb.query;

import net.runelite.rsb.query.request.QueryRequest;
import net.runelite.rsb.query.request.RSInventoryItemQueryRequest;
import net.runelite.rsb.query.result.PositionableQueryResult;
import net.runelite.rsb.query.result.QueryResult;
import net.runelite.rsb.wrappers.RSItem;

import java.util.Arrays;

public class RSInventoryItemQueryBuilder extends AbstractQueryBuilder<RSItem, net.runelite.rsb.query.RSInventoryItemQueryBuilder, QueryResult<RSItem>, RSInventoryItemQueryRequest> {
        public RSInventoryItemQueryBuilder() {
            super();
        }

        public net.runelite.rsb.query.RSInventoryItemQueryBuilder notStackable() {
            return filter(item -> item.getDefinition().stackable > 0);
        }

        public net.runelite.rsb.query.RSInventoryItemQueryBuilder noted() {
            return filter(item -> item.getDefinition().notedID == item.getID());
        }

        public net.runelite.rsb.query.RSInventoryItemQueryBuilder unnoted() {
            return filter(item -> item.getDefinition().notedID != item.getID());
        }

        public net.runelite.rsb.query.RSInventoryItemQueryBuilder stackable() {
            return filter(item -> item.getDefinition().notedID == item.getID());
        }

        public net.runelite.rsb.query.RSInventoryItemQueryBuilder namedContains(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(arrayOfString)
                    .anyMatch(string -> item.getName().contains(string)));
        }

        public net.runelite.rsb.query.RSInventoryItemQueryBuilder notNamedContains(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(arrayOfString)
                    .noneMatch(string -> item.getName().contains(string)));
        }

        public net.runelite.rsb.query.RSInventoryItemQueryBuilder named(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(arrayOfString)
                    .anyMatch(string -> string.equals(item.getName())));
        }
        public net.runelite.rsb.query.RSInventoryItemQueryBuilder notNamed(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(arrayOfString)
                    .noneMatch(string -> string.equals(item.getName())));
        }

        public net.runelite.rsb.query.RSInventoryItemQueryBuilder id(int... arrayOfInt) {
            if (arrayOfInt == null) return this;
            return filter(item -> Arrays.stream(arrayOfInt)
                    .anyMatch(i -> i == item.getID()));
        }
        public net.runelite.rsb.query.RSInventoryItemQueryBuilder stackSize(int minimum) {
            return filter(item -> (item.getItem().getStackSize() >= minimum));
        }
        public net.runelite.rsb.query.RSInventoryItemQueryBuilder stackSize(int minimum, int maximum) {
            return filter(item -> (item.getItem().getStackSize() >= minimum && item.getItem().getStackSize() <= maximum));
        }

        public net.runelite.rsb.query.RSInventoryItemQueryBuilder actionsContains(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(item.getDefinition().options)
                    .filter((x) -> x != null)
                    .anyMatch(itemString -> Arrays.stream(arrayOfString)
                            .anyMatch(inputString -> itemString.contains(inputString))));
        }
        public net.runelite.rsb.query.RSInventoryItemQueryBuilder actions(String... arrayOfString) {
            if (arrayOfString == null || Arrays.stream(arrayOfString).allMatch((x) -> x == null || x.equals(""))) return this;
            return filter(item -> Arrays.stream(item.getDefinition().options)
                    .filter((x) -> x != null)
                    .anyMatch(itemString -> Arrays.stream(arrayOfString)
                            .anyMatch(inputString -> itemString.equals(inputString))));
        }

        public RSInventoryItemQueryRequest createRequest() {
            return new RSInventoryItemQueryRequest();
        }
    }