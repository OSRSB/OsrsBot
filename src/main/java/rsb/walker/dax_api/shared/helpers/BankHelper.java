package rsb.walker.dax_api.shared.helpers;

import rsb.internal.wrappers.Filter;
import rsb.methods.Web;
import rsb.walker.dax_api.Filters;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.walker_engine.WaitFor;
import rsb.walker.dax_api.walker_engine.interaction_handling.InteractionHelper;
import rsb.wrappers.RSObject;
import rsb.wrappers.common.Positionable;

import java.util.HashSet;

public class BankHelper {

    private static final Filter<RSObject> BANK_OBJECT_FILTER = Filters.Objects.nameContains("bank", "Bank", "Exchange booth", "Open chest")
            .combine(Filters.Objects.actionsContains("Collect"), true)
            .combine(Filters.Objects.actionsContains("Bank"), true);

    public static boolean isInBank(){
        return isInBank(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
    }

    public static boolean isInBank(Positionable positionable){
        RSObject bankObjects = Web.methods.objects.getNearest(15, BANK_OBJECT_FILTER);
        if (bankObjects == null){
            return false;
        }
        RSObject bankObject = bankObjects;
        HashSet<WalkerTile> building = getBuilding(bankObject);
        return building.contains(positionable.getLocation()) || (building.size() == 0 && positionable.getLocation().distanceTo(bankObject) < 5);
    }

    /**
     *
     * @return whether if the action succeeded
     */
    public static boolean openBank() {
        return Web.methods.bank.isOpen() || InteractionHelper.click(InteractionHelper.getRSObject(BANK_OBJECT_FILTER), "Bank");
    }

    /**
     *
     * @return bank screen is open
     */
    public static boolean openBankAndWait(){
        if (Web.methods.bank.isOpen()){
            return true;
        }
        RSObject object = InteractionHelper.getRSObject(BANK_OBJECT_FILTER);
        return InteractionHelper.click(object, "Bank") && waitForBankScreen(object);
    }

    public static HashSet<WalkerTile> getBuilding(Positionable positionable){
        return computeBuilding(positionable, Web.methods.game.getSceneFlags(), new HashSet<>());
    }

    private static HashSet<WalkerTile> computeBuilding(Positionable positionable, byte[][][] sceneFlags, HashSet<WalkerTile> tiles){
        try {
            WalkerTile local = positionable.getLocation().toSceneTile();
            int localX = local.getX(), localY = local.getY(), localZ = local.getPlane();
            if (localX < 0 || localY < 0 || localZ < 0){
                return tiles;
            }
            if (sceneFlags.length <= localZ || sceneFlags[localZ].length <= localX || sceneFlags[localZ][localX].length <= localY){ //Not within bounds
                return tiles;
            }
            if (sceneFlags[localZ][localX][localY] < 4){ //Not a building
                return tiles;
            }
            if (!tiles.add(local.toWorldTile())){ //Already computed
                return tiles;
            }
            computeBuilding(new WalkerTile(localX, localY + 1, localZ, WalkerTile.TYPES.SCENE).toWorldTile(), sceneFlags, tiles);
            computeBuilding(new WalkerTile(localX + 1, localY, localZ, WalkerTile.TYPES.SCENE).toWorldTile(), sceneFlags, tiles);
            computeBuilding(new WalkerTile(localX, localY - 1, localZ, WalkerTile.TYPES.SCENE).toWorldTile(), sceneFlags, tiles);
            computeBuilding(new WalkerTile(localX - 1, localY, localZ, WalkerTile.TYPES.SCENE).toWorldTile(), sceneFlags, tiles);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return tiles;
    }

    private static boolean isInBuilding(WalkerTile localWalkerTile, byte[][][] sceneFlags) {
        return !(sceneFlags.length <= localWalkerTile.getPlane()
                    || sceneFlags[localWalkerTile.getPlane()].length <= localWalkerTile.getX()
                    || sceneFlags[localWalkerTile.getPlane()][localWalkerTile.getX()].length <= localWalkerTile.getY())
                && sceneFlags[localWalkerTile.getPlane()][localWalkerTile.getX()][localWalkerTile.getY()] >= 4;
    }

    private static boolean waitForBankScreen(RSObject object){
        return WaitFor.condition(WaitFor.getMovementRandomSleep(object), ((WaitFor.Condition) () -> Web.methods.bank.isOpen() ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE).combine(WaitFor.getNotMovingCondition())) == WaitFor.Return.SUCCESS;
    }

}
