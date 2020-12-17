package rsb.walker.dax_api.walker_engine;


import rsb.methods.Calculations;
import rsb.methods.Web;
import rsb.util.StdRandom;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.shared.PathFindingNode;
import rsb.walker.dax_api.teleports.Teleport;
import rsb.walker.dax_api.walker.utils.AccurateMouse;
import rsb.walker.dax_api.walker.utils.path.PathUtils;
import rsb.walker.dax_api.walker_engine.bfs.BFS;
import rsb.walker.dax_api.walker_engine.interaction_handling.PathObjectHandler;
import rsb.walker.dax_api.walker_engine.local_pathfinding.PathAnalyzer;
import rsb.walker.dax_api.walker_engine.local_pathfinding.Reachable;
import rsb.walker.dax_api.walker_engine.navigation_utils.Charter;
import rsb.walker.dax_api.walker_engine.navigation_utils.NavigationSpecialCase;
import rsb.walker.dax_api.walker_engine.navigation_utils.ShipUtils;
import rsb.walker.dax_api.walker_engine.real_time_collision.CollisionDataCollector;
import rsb.walker.dax_api.walker_engine.real_time_collision.RealTimeCollisionTile;

import java.awt.*;
import java.util.List;

public class WalkerEngine implements Loggable{

    private static WalkerEngine walkerEngine;

    private int attemptsForAction;
    private final int failThreshold;
    private boolean navigating;
    private List<WalkerTile> currentPath;

    private WalkerEngine(){
        attemptsForAction = 0;
        failThreshold = 3;
        navigating = false;
        currentPath = null;
    }

    public static WalkerEngine getInstance(){
        return walkerEngine != null ? walkerEngine : (walkerEngine = new WalkerEngine());
    }

    public boolean walkPath(List<WalkerTile> path){
        return walkPath(path, null);
    }

    public List<WalkerTile> getCurrentPath() {
        return currentPath;
    }

    /**
     *
     * @param path
     * @param walkingCondition
     * @return
     */
    public boolean walkPath(List<WalkerTile> path, WalkingCondition walkingCondition){
        if (path.size() == 0) {
            log("Path is empty");
            return false;
        }


        if (!handleTeleports(path)) {
            log(Level.WARNING, "Failed to handle teleports...");
            return false;
        }


        navigating = true;
        currentPath = path;
        try {
            PathAnalyzer.DestinationDetails destinationDetails;
            resetAttempts();

            while (true) {

                if (!Web.methods.game.isLoggedIn()){
                    return false;
                }

                if (ShipUtils.isOnShip()) {
                    if (!ShipUtils.crossGangplank()) {
                        log("Failed to exit ship via gangplank.");
                        failedAttempt();
                    }
                    WaitFor.milliseconds(50);
                    continue;
                }

                if (isFailedOverThreshhold()) {
                    log("Too many failed attempts");
                    return false;
                }

                destinationDetails = PathAnalyzer.furthestReachableTile(path);
                if (PathUtils.getFurthestReachableTileInMinimap(path) == null || destinationDetails == null) {
                    log("Could not grab destination details.");
                    failedAttempt();
                    continue;
                }

                RealTimeCollisionTile currentNode = destinationDetails.getDestination();
                WalkerTile assumedNext = destinationDetails.getAssumed();

                if (destinationDetails.getState() != PathAnalyzer.PathState.FURTHEST_CLICKABLE_TILE) {
                    log(destinationDetails.toString());
                }

                final RealTimeCollisionTile destination = currentNode;
                if (!Web.methods.calc.tileOnMap(new WalkerTile(destination.getX(), destination.getY(), destination.getZ()))) {
                    log("Closest tile in path is not in minimap: " + destination);
                    failedAttempt();
                    continue;
                }

                CustomConditionContainer conditionContainer = new CustomConditionContainer(walkingCondition);
                switch (destinationDetails.getState()) {
                    case DISCONNECTED_PATH:
                        if (currentNode.getWalkerTile().distanceTo(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())) > 10){
                            clickMinimap(currentNode);
                            WaitFor.milliseconds(1200, 3400);
                        }
                        NavigationSpecialCase.SpecialLocation specialLocation = NavigationSpecialCase.getLocation(currentNode.getWalkerTile()),
                                specialLocationDestination = NavigationSpecialCase.getLocation(assumedNext);
                        if (specialLocation != null && specialLocationDestination != null) {
                            log("[SPECIAL LOCATION] We are at " + specialLocation + " and our destination is " + specialLocationDestination);
                            if (!NavigationSpecialCase.handle(specialLocationDestination)) {
                                failedAttempt();
                            } else {
                                successfulAttempt();
                            }
                            break;
                        }

                        Charter.LocationProperty
                                locationProperty = Charter.LocationProperty.getLocation(currentNode.getWalkerTile()),
                                destinationProperty = Charter.LocationProperty.getLocation(assumedNext);
                        if (locationProperty != null && destinationProperty != null) {
                            log("Chartering to: " + destinationProperty);
                            if (!Charter.to(destinationProperty)) {
                                failedAttempt();
                            } else {
                                successfulAttempt();
                            }
                            break;
                        }
                        //DO NOT BREAK OUT
                    case OBJECT_BLOCKING:
                        WalkerTile walkingTile = Reachable.getBestWalkableTile(destination.getWalkerTile(), new Reachable());
                        if (isDestinationClose(destination) || (walkingTile != null ? AccurateMouse.clickMinimap(walkingTile) : clickMinimap(destination))) {
                            log("Handling Object...");
                            if (!PathObjectHandler.handle(destinationDetails, path)) {
                                failedAttempt();
                            } else {
                                successfulAttempt();
                            }
                            break;
                        }
                        break;

                    case FURTHEST_CLICKABLE_TILE:
                        if (clickMinimap(currentNode)) {
                            long offsetWalkingTimeout = System.currentTimeMillis() + StdRandom.uniform(2500, 4000);
                            WaitFor.condition(10000, () -> {
                                switch (conditionContainer.trigger()) {
                                    case EXIT_OUT_WALKER_SUCCESS:
                                    case EXIT_OUT_WALKER_FAIL:
                                        return WaitFor.Return.SUCCESS;
                                }

                                PathAnalyzer.DestinationDetails furthestReachable = PathAnalyzer.furthestReachableTile(path);
                                PathFindingNode currentDestination = BFS.bfsClosestToPath(path, RealTimeCollisionTile.get(destination.getX(), destination.getY(), destination.getZ()));
                                if (currentDestination == null) {
                                    log("Could not walk to closest tile in path.");
                                    failedAttempt();
                                    return WaitFor.Return.FAIL;
                                }
                                int indexCurrentDestination = path.indexOf(currentDestination.getWalkerTile());

                                PathFindingNode closestToPlayer = PathAnalyzer.closestTileInPathToPlayer(path);
                                if (closestToPlayer == null) {
                                    log("Could not detect closest tile to player in path.");
                                    failedAttempt();
                                    return WaitFor.Return.FAIL;
                                }
                                int indexCurrentPosition = path.indexOf(closestToPlayer.getWalkerTile());
                                if (furthestReachable == null) {
                                    System.out.println("Furthest reachable is null/");
                                    return WaitFor.Return.FAIL;
                                }
                                int indexNextDestination = path.indexOf(furthestReachable.getDestination().getWalkerTile());
                                if (indexNextDestination - indexCurrentDestination > 5 || indexCurrentDestination - indexCurrentPosition < 5) {
                                    return WaitFor.Return.SUCCESS;
                                }
                                if (System.currentTimeMillis() > offsetWalkingTimeout && !Web.methods.players.getMyPlayer().isLocalPlayerMoving()){
                                    return WaitFor.Return.FAIL;
                                }
                                return WaitFor.milliseconds(100);
                            });
                        }
                        break;

                    case END_OF_PATH:
                        clickMinimap(destinationDetails.getDestination());
                        log("Reached end of path");
                        return true;
                }

                switch (conditionContainer.getResult()) {
                    case EXIT_OUT_WALKER_SUCCESS:
                        return true;
                    case EXIT_OUT_WALKER_FAIL:
                        return false;
                }

                WaitFor.milliseconds(50, 100);

            }
        } finally {
            navigating = false;
        }
    }

    boolean isNavigating() {
        return navigating;
    }

    boolean isDestinationClose(PathFindingNode pathFindingNode){
        final WalkerTile playerPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        return new WalkerTile(pathFindingNode.getX(), pathFindingNode.getY(), pathFindingNode.getZ()).isClickable()
                && playerPosition.distanceTo(new WalkerTile(pathFindingNode.getX(), pathFindingNode.getY(), pathFindingNode.getZ())) <= 12
                && (BFS.isReachable(RealTimeCollisionTile.get(playerPosition.getX(), playerPosition.getY(), playerPosition.getPlane()), RealTimeCollisionTile.get(pathFindingNode.getX(), pathFindingNode.getY(), pathFindingNode.getZ()), 200));
    }

    public boolean clickMinimap(PathFindingNode pathFindingNode){
        final WalkerTile playerPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        if (playerPosition.distanceTo(pathFindingNode.getWalkerTile()) <= 1){
            return true;
        }
        PathFindingNode randomNearby = BFS.getRandomTileNearby(pathFindingNode);

        if (randomNearby == null){
            log("Unable to generate randomization.");
            return false;
        }

        log("Randomize(" + pathFindingNode.getX() + "," + pathFindingNode.getY() + "," + pathFindingNode.getZ() + ") -> (" + randomNearby.getX() + "," + randomNearby.getY() + "," + randomNearby.getZ() + ")");
        return AccurateMouse.clickMinimap(new WalkerTile(randomNearby.getX(), randomNearby.getY(), randomNearby.getZ())) || AccurateMouse.clickMinimap(new WalkerTile(pathFindingNode.getX(), pathFindingNode.getY(), pathFindingNode.getZ()));
    }

    public void hoverMinimap(PathFindingNode pathFindingNode){
        if (pathFindingNode == null){
            return;
        }
        Web.methods.mouse.move(Web.methods.calc.tileToMinimap(new WalkerTile(pathFindingNode.getX(), pathFindingNode.getY(), pathFindingNode.getZ())));
    }

    private boolean resetAttempts(){
        return successfulAttempt();
    }

    private boolean successfulAttempt(){
        attemptsForAction = 0;
        return true;
    }

    private void failedAttempt(){
        if (Web.methods.camera.getPitch() < 90) {
            Web.methods.camera.setPitch(StdRandom.uniform(90, 100));
        }
        if (++attemptsForAction > 1) {
            Web.methods.camera.setAngle(StdRandom.uniform(0, 360));
        }
        log("Failed attempt on action.");
        WaitFor.milliseconds(450 * (attemptsForAction + 1), 850 * (attemptsForAction + 1));
        CollisionDataCollector.generateRealTimeCollision();
    }

    private boolean isFailedOverThreshhold(){
        return attemptsForAction >= failThreshold;
    }

    private class CustomConditionContainer {
        private WalkingCondition walkingCondition;
        private WalkingCondition.State result;
        CustomConditionContainer(WalkingCondition walkingCondition){
            this.walkingCondition = walkingCondition;
            this.result = WalkingCondition.State.CONTINUE_WALKER;
        }
        public WalkingCondition.State trigger(){
            result = (walkingCondition != null ? walkingCondition.action() : result);
            return result != null ? result : WalkingCondition.State.CONTINUE_WALKER;
        }
        public WalkingCondition.State getResult() {
            return result;
        }
    }

    @Override
    public String getName() {
        return "Walker Engine";
    }

    private boolean handleTeleports(List<WalkerTile> path) {
        WalkerTile startPosition = path.get(0);
        WalkerTile playerPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        if(startPosition.equals(playerPosition))
            return true;
        if(Web.methods.bank.isOpen())
            Web.methods.bank.close();
        for (Teleport teleport : Teleport.values()) {
            if (!teleport.getRequirement().satisfies()) continue;
            if(teleport.isAtTeleportSpot(startPosition) && !teleport.isAtTeleportSpot(playerPosition)){
                log("Using teleport method: " + teleport);
                teleport.trigger();
                return WaitFor.condition(StdRandom.uniform(3000, 20000),
                    () -> startPosition.distanceTo(new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()))) < 10 ?
                        WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) == WaitFor.Return.SUCCESS;
            }
        }
        return true;
    }

}
