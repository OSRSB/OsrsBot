package rsb.walker.dax_api;

import rsb.internal.wrappers.Filter;
import rsb.methods.Web;
import rsb.wrappers.*;
import rsb.wrappers.common.Positionable;
import rsb.wrappers.subwrap.WalkerTile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public interface Filters {
    
    /**
     * A class used to compare an object against an array with a passable comparing operation
     * @param <T> The type of the objects being compared
     */
    class Comparator<T> {
        /**
         * Iterates through the array and uses the function to perform the passed comparing operation on the passed
         * <var>obj</var> amd each entry in the array to find a result that returns a true statement from the
         * comparing function.
         * @param obj The object derived from a instance of an in-game entity
         * @param array The array of options to compare against
         * @param operation The comparing operation being passed to perform on each obj and option in the array
         * @return <tt>True</tt> if any option in the array fulfills the comparing operation; otherwise <tt>false</tt>
         */
        boolean iterateAndCompare(T obj, T[] array, Function<ArrayList<T>, Boolean> operation) {
            for (T t : array) {
                ArrayList<T> opArray = new ArrayList<>(Arrays.asList(t, obj));
                if (operation.apply(opArray)) {
                    return true;
                }
            }
            return false;
        }
    }


    class Items implements Filters {

        
        public static Filter<RSItem> actionsContains(String... actions) {
            return (RSItem item) -> {
                String[] itemActions = item.getInventoryActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSItem> actionsEquals(String... actions) {
            return (RSItem item) -> {
                String[] itemActions = item.getInventoryActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSItem> actionsNotContains(String... actions) {
            return (RSItem item) -> {
                String[] itemActions = item.getInventoryActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSItem> actionsNotEquals(String... actions) {
            return (RSItem item) -> {
                String[] itemActions = item.getInventoryActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSItem> idEquals(int... ids) {
            return (RSItem item) -> {
                int iid = item.getItem().getItemId();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(iid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        
        public static Filter<RSItem> idNotEquals(int... ids) {
            return (RSItem item) -> {
                int iid = item.getItem().getItemId();
                Function<ArrayList<Integer>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(iid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        
        public static Filter<RSItem> nameContains(String... names) {
            return (RSItem item) -> {
                String iName = item.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSItem> nameEquals(String... names) {
            return (RSItem item) -> {
                String iName = item.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSItem> nameNotContains(String... names) {
            return (RSItem item) -> {
                String iName = item.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSItem> nameNotEquals(String... names) {
            return (RSItem item) -> {
                String iName = item.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }
    }

    class Objects implements Filters {

        
        public static Filter<RSObject> actionsContains(String... actions) {
            return (RSObject object) -> {
                String[] objectActions = object.getDef().getActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<objectActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(objectActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSObject> actionsEquals(String... actions) {
            return (RSObject object) -> {
                String[] objectActions = object.getDef().getActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<objectActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(objectActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSObject> actionsNotContains(String... actions) {
            return (RSObject object) -> {
                String[] objectActions = object.getDef().getActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<objectActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(objectActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSObject> actionsNotEquals(String... actions) {
            return (RSObject object) -> {
                String[] objectActions = object.getDef().getActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<objectActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(objectActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSObject> idEquals(int... ids) {
            return (RSObject object) -> {
                int oid = object.getID();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(oid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        
        public static Filter<RSObject> idNotEquals(int... ids) {
            return (RSObject object) -> {
                int oid = object.getID();
                Function<ArrayList<Integer>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(oid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        
        public static Filter<RSObject> nameContains(String... names) {
            return (RSObject object) -> {
                String oName = object.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(oName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSObject> nameEquals(String... names) {
            return (RSObject object) -> {
                String oName = object.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(oName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSObject> nameNotContains(String... names) {
            return (RSObject object) -> {
                String oName = object.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(oName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSObject> nameNotEquals(String... names) {
            return (RSObject object) -> {
                String oName = object.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(oName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        public static Filter<RSObject> modelIndexCount(int... counts) {
            return (RSObject object) -> {
                int oIndexCount = object.getModel().getIndexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(oIndexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public static Filter<RSObject> modelVertexCount(int... counts) {
            return (RSObject object) -> {
                int oVertexCount = object.getModel().getVertexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(oVertexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public static Filter<RSObject> inArea(RSArea area) {
            return (RSObject object) -> {
                RSArea oArea = object.getArea();
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(oArea, new RSArea[]{area}, operation);
            };
        }

        public static Filter<RSObject> notInArea(RSArea area) {
            return (RSObject object) -> {
                RSArea oArea = object.getArea();
                Function<ArrayList<RSArea>, Boolean> operation = e -> !e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(oArea, new RSArea[]{area}, operation);
            };
        }

        public static Filter<RSObject> tileEquals(Positionable pos) {
            return (RSObject object) -> {
                WalkerTile oTile = new WalkerTile(object.getLocation());
                WalkerTile pTile = pos.getLocation();
                Function<ArrayList<WalkerTile>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<WalkerTile>()).iterateAndCompare(oTile, new WalkerTile[]{pTile}, operation);
            };
        }

        public static Filter<RSObject> tileNotEquals(Positionable pos) {
            return (RSObject object) -> {
                WalkerTile oTile = new WalkerTile(object.getLocation());
                WalkerTile pTile = pos.getLocation();
                Function<ArrayList<WalkerTile>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<WalkerTile>()).iterateAndCompare(oTile, new WalkerTile[]{pTile}, operation);
            };
        }


    }

    class NPCs implements Filters {

        
        public static Filter<RSNPC> actionsContains(String... actions) {
            return (RSNPC npc) -> {
                String[] npcActions = npc.getActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<npcActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(npcActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSNPC> actionsEquals(String... actions) {
            return (RSNPC npc) -> {
                String[] npcActions = npc.getActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<npcActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(npcActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSNPC> actionsNotContains(String... actions) {
            return (RSNPC npc) -> {
                String[] npcActions = npc.getActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<npcActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(npcActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSNPC> actionsNotEquals(String... actions) {
            return (RSNPC npc) -> {
                String[] npcActions = npc.getActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<npcActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(npcActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSNPC> idEquals(int... ids) {
            return (RSNPC npc) -> {
                int nid = npc.getID();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(nid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        
        public static Filter<RSNPC> idNotEquals(int... ids) {
            return (RSNPC npc) -> {
                int nid = npc.getID();
                Function<ArrayList<Integer>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(nid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        
        public static Filter<RSNPC> nameContains(String... names) {
            return (RSNPC npc) -> {
                String nName = npc.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSNPC> nameEquals(String... names) {
            return (RSNPC npc) -> {
                String nName = npc.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSNPC> nameNotContains(String... names) {
            return (RSNPC npc) -> {
                String nName = npc.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSNPC> nameNotEquals(String... names) {
            return (RSNPC npc) -> {
                String nName = npc.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        public static Filter<RSNPC> modelIndexCount(int... counts) {
            return (RSNPC npc) -> {
                int nIndexCount = npc.getModel().getIndexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(nIndexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public static Filter<RSNPC> modelVertexCount(int... counts) {
            return (RSNPC npc) -> {
                int nVertexCount = npc.getModel().getVertexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(nVertexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public static Filter<RSNPC> inArea(RSArea area) {
            return (RSNPC npc) -> {
                RSArea nArea = new RSArea(new WalkerTile[]{new WalkerTile(npc.getLocation())});
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(nArea, new RSArea[]{area}, operation);
            };
        }

        public static Filter<RSNPC> notInArea(RSArea area) {
            return (RSNPC npc) -> {
                RSArea nArea = new RSArea(new WalkerTile[]{new WalkerTile(npc.getLocation())});
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(nArea, new RSArea[]{area}, operation);
            };
        }

        public static Filter<RSNPC> tileEquals(Positionable pos) {
            return (RSNPC npc) -> {
                WalkerTile nTile = new WalkerTile(npc.getLocation());
                WalkerTile pTile = pos.getLocation();
                Function<ArrayList<WalkerTile>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<WalkerTile>()).iterateAndCompare(nTile, new WalkerTile[]{pTile}, operation);
            };
        }

        public static Filter<RSNPC> tileNotEquals(Positionable pos) {
            return (RSNPC npc) -> {
                WalkerTile nTile = new WalkerTile(npc.getLocation());
                WalkerTile pTile = pos.getLocation();
                Function<ArrayList<WalkerTile>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<WalkerTile>()).iterateAndCompare(nTile, new WalkerTile[]{pTile}, operation);
            };
        }
    }

    class Players implements Filters {

        //Actions might be add-able later
        
        public static Filter<RSPlayer> actionsContains(String... actions) {
            return null;
        }

        
        public static Filter<RSPlayer> actionsEquals(String... actions) {
            return null;
        }

        
        public static Filter<RSPlayer> actionsNotContains(String... actions) {
            return null;
        }

        
        public static Filter<RSPlayer> actionsNotEquals(String... actions) {
            return null;
        }

        //Players lack ID
        
        public static Filter<RSPlayer> idEquals(int... ids) {
            return null;
        }

        
        public static Filter<RSPlayer> idNotEquals(int... ids) {
            return null;
        }

        
        public static Filter<RSPlayer> nameContains(String... names) {
            return (RSPlayer player) -> {
                String pName = player.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(pName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSPlayer> nameEquals(String... names) {
            return (RSPlayer player) -> {
                String pName = player.getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(pName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSPlayer> nameNotContains(String... names) {
            return (RSPlayer player) -> {
                String nName = player.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(nName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSPlayer> nameNotEquals(String... names) {
            return (RSPlayer player) -> {
                String pName = player.getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(pName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        public static Filter<RSPlayer> modelIndexCount(int... counts) {
            return (RSPlayer player) -> {
                int pIndexCount = player.getModel().getIndexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(pIndexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public static Filter<RSPlayer> modelVertexCount(int... counts) {
            return (RSPlayer player) -> {
                int pVertexCount = player.getModel().getVertexCount();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(pVertexCount, Arrays.stream(counts).boxed().toArray(Integer[]::new), operation);
            };
        }

        public static Filter<RSPlayer> inArea(RSArea area) {
            return (RSPlayer player) -> {
                RSArea pArea = new RSArea(new WalkerTile[]{new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()))});
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(pArea, new RSArea[]{area}, operation);
            };
        }

        public static Filter<RSPlayer> notInArea(RSArea area) {
            return (RSPlayer player) -> {
                RSArea pArea = new RSArea(new WalkerTile[]{new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()))});
                Function<ArrayList<RSArea>, Boolean> operation = e -> e.get(0).contains(e.get(1).getTileArray());
                return (new Comparator<RSArea>()).iterateAndCompare(pArea, new RSArea[]{area}, operation);
            };
        }

        public static Filter<RSPlayer> tileEquals(Positionable pos) {
            return (RSPlayer player) -> {
                WalkerTile playerTile = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
                WalkerTile pTile = pos.getLocation();
                Function<ArrayList<WalkerTile>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<WalkerTile>()).iterateAndCompare(playerTile, new WalkerTile[]{pTile}, operation);
            };
        }

        public static Filter<RSPlayer> tileNotEquals(Positionable pos) {
            return (RSPlayer player) -> {
                WalkerTile playerTile = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
                WalkerTile pTile = pos.getLocation();
                Function<ArrayList<WalkerTile>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<WalkerTile>()).iterateAndCompare(playerTile, new WalkerTile[]{pTile}, operation);
            };
        }
    }

    class GroundItems implements Filters {
        
        public static Filter<RSGroundItem> actionsContains(String... actions) {
            return (RSGroundItem item) -> {
                String[] itemActions = item.getItem().getGroundActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSGroundItem> actionsEquals(String... actions) {
            return (RSGroundItem item) -> {
                String[] itemActions = item.getItem().getGroundActions();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSGroundItem> actionsNotContains(String... actions) {
            return (RSGroundItem item) -> {
                String[] itemActions = item.getItem().getGroundActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSGroundItem> actionsNotEquals(String... actions) {
            return (RSGroundItem item) -> {
                String[] itemActions = item.getItem().getGroundActions();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                boolean actionCheck = false;
                for (int i = 0; i<itemActions.length; i++)
                    actionCheck = actionCheck  || (new Comparator<String>()).iterateAndCompare(itemActions[i], Arrays.stream(actions).toArray(String[]::new), operation);
                return actionCheck;
            };
        }

        
        public static Filter<RSGroundItem> idEquals(int... ids) {
            return (RSGroundItem item) -> {
                int iid = item.getItem().getItem().getItemId();
                Function<ArrayList<Integer>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(iid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        
        public static Filter<RSGroundItem> idNotEquals(int... ids) {
            return (RSGroundItem item) -> {
                int iid = item.getItem().getItem().getItemId();
                Function<ArrayList<Integer>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<Integer>()).iterateAndCompare(iid, Arrays.stream(ids).boxed().toArray(Integer[]::new), operation);
            };
        }

        
        public static Filter<RSGroundItem> nameContains(String... names) {
            return (RSGroundItem item) -> {
                String iName = item.getItem().getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSGroundItem> nameEquals(String... names) {
            return (RSGroundItem item) -> {
                String iName = item.getItem().getName();
                Function<ArrayList<String>, Boolean> operation = e -> e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSGroundItem> nameNotContains(String... names) {
            return (RSGroundItem item) -> {
                String iName = item.getItem().getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).contains(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }

        
        public static Filter<RSGroundItem> nameNotEquals(String... names) {
            return (RSGroundItem item) -> {
                String iName = item.getItem().getName();
                Function<ArrayList<String>, Boolean> operation = e -> !e.get(0).equals(e.get(1));
                return (new Comparator<String>()).iterateAndCompare(iName, Arrays.stream(names).toArray(String[]::new), operation);
            };
        }
    }
}
