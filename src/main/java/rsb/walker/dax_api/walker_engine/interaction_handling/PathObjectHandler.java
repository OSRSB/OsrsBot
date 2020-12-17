package rsb.walker.dax_api.walker_engine.interaction_handling;

import net.runelite.api.ObjectComposition;
import rsb.internal.wrappers.Filter;
import rsb.methods.Calculations;
import rsb.methods.Web;
import rsb.util.StdRandom;
import rsb.walker.dax_api.Filters;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.shared.helpers.RSObjectHelper;
import rsb.walker.dax_api.walker_engine.Loggable;
import rsb.walker.dax_api.walker_engine.WaitFor;
import rsb.walker.dax_api.walker_engine.WalkerEngine;
import rsb.walker.dax_api.walker_engine.bfs.BFS;
import rsb.walker.dax_api.walker_engine.local_pathfinding.PathAnalyzer;
import rsb.walker.dax_api.walker_engine.local_pathfinding.Reachable;
import rsb.walker.dax_api.walker_engine.real_time_collision.RealTimeCollisionTile;
import rsb.wrappers.RSArea;
import rsb.wrappers.RSItem;
import rsb.wrappers.RSObject;

import java.util.*;
import java.util.stream.Collectors;


public class PathObjectHandler implements Loggable {

    private static PathObjectHandler instance;

    private final TreeSet<String> sortedOptions, sortedBlackList, sortedBlackListOptions, sortedHighPriorityOptions;

    private PathObjectHandler(){
        sortedOptions = new TreeSet<>(
		        Arrays.asList("Enter", "Cross", "Pass", "Open", "Close", "Walk-through", "Use", "Pass-through", "Exit",
                "Walk-Across", "Go-through", "Walk-across", "Climb", "Climb-up", "Climb-down", "Climb-over", "Climb over", "Climb-into", "Climb-through",
                "Board", "Jump-from", "Jump-across", "Jump-to", "Squeeze-through", "Jump-over", "Pay-toll(10gp)", "Step-over", "Walk-down", "Walk-up","Walk-Up", "Travel", "Get in",
                "Investigate", "Operate", "Climb-under","Jump","Crawl-down","Crawl-through","Activate","Push","Squeeze-past","Walk-Down",
                "Swing-on", "Climb up"));

        sortedBlackList = new TreeSet<>(Arrays.asList("Coffin","Drawers","null"));
        sortedBlackListOptions = new TreeSet<>(Arrays.asList("Chop down"));
        sortedHighPriorityOptions = new TreeSet<>(Arrays.asList("Pay-toll(10gp)","Squeeze-past"));
    }

    private static PathObjectHandler getInstance(){
        return instance != null ? instance : (instance = new PathObjectHandler());
    }

    private enum SpecialObject {
        WEB("Web", "Slash", null, new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return Web.methods.objects.getNearest(
                        Filters.Objects.inArea(new RSArea(destinationDetails.getAssumed(), 1))
                                .combine(Filters.Objects.nameEquals("Web"), true)
                                .combine(Filters.Objects.actionsContains("Slash"), true)).isClickable();
            }
        }),
        ROCKFALL("Rockfall", "Mine", null, new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return Web.methods.objects.getNearest(
                        Filters.Objects.inArea(new RSArea(destinationDetails.getAssumed(), 1))
                                .combine(Filters.Objects.nameEquals("Rockfall"), true)
                                .combine(Filters.Objects.actionsContains("Mine"), true)).isClickable();
            }
        }),
        ROOTS("Roots", "Chop", null, new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return Web.methods.objects.getNearest(
                        Filters.Objects.inArea(new RSArea(destinationDetails.getAssumed(), 1))
                                .combine(Filters.Objects.nameEquals("Roots"), true)
                                .combine(Filters.Objects.actionsContains("Chop"), true)).isClickable();
            }
        }),
        ROCK_SLIDE("Rockslide", "Climb-over", null, new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return Web.methods.objects.getNearest(
                        Filters.Objects.inArea(new RSArea(destinationDetails.getAssumed(), 1))
                                .combine(Filters.Objects.nameEquals("Rockslide"), true)
                                .combine(Filters.Objects.actionsContains("Climb-over"), true)).isClickable();
            }
        }),
        ROOT("Root", "Step-over", null, new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return Web.methods.objects.getNearest(
                        Filters.Objects.inArea(new RSArea(destinationDetails.getAssumed(), 1))
                                .combine(Filters.Objects.nameEquals("Root"), true)
                                .combine(Filters.Objects.actionsContains("Step-over"), true)).isClickable();
            }
        }),
        BRIMHAVEN_VINES("Vines", "Chop-down", null, new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return Web.methods.objects.getNearest(
                        Filters.Objects.inArea(new RSArea(destinationDetails.getAssumed(), 1))
                                .combine(Filters.Objects.nameEquals("Vines"), true)
                                .combine(Filters.Objects.actionsContains("Chop-down"), true)).isClickable();
            }
        }),
        AVA_BOOKCASE ("Bookcase", "Search", new WalkerTile(3097, 3359, 0), new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return destinationDetails.getDestination().getX() >= 3097 && destinationDetails.getAssumed().equals(new WalkerTile(3097, 3359, 0));
            }
        }),
        AVA_LEVER ("Lever", "Pull", new WalkerTile(3096, 3357, 0), new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return destinationDetails.getDestination().getX() < 3097 && destinationDetails.getAssumed().equals(new WalkerTile(3097, 3359, 0));
            }
        }),
        ARDY_DOOR_LOCK_SIDE("Door", "Pick-lock", new WalkerTile(2565, 3356, 0), new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).getX() >= 2565 && new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).distanceTo(new WalkerTile(2565, 3356, 0)) < 3;
            }
        }),
        ARDY_DOOR_UNLOCKED_SIDE("Door", "Open", new WalkerTile(2565, 3356, 0), new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).getX() < 2565 && new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).distanceTo(new WalkerTile(2565, 3356, 0)) < 3;
            }
        }),
        YANILLE_DOOR_LOCK_SIDE("Door", "Pick-lock", new WalkerTile(2601, 9482, 0), new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).getY() <= 9481 && new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).distanceTo(new WalkerTile(2601, 9482, 0)) < 3;
            }
        }),
        YANILLE_DOOR_UNLOCKED_SIDE("Door", "Open", new WalkerTile(2601, 9482, 0), new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).getY() > 9481 && new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).distanceTo(new WalkerTile(2601, 9482, 0)) < 3;
            }
        }),
        EDGEVILLE_UNDERWALL_TUNNEL("Underwall tunnel", "Climb-into", new WalkerTile(3138, 3516, 0), new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return destinationDetails.getAssumed().equals(new WalkerTile(3138, 3516, 0));
            }
        }),
        VARROCK_UNDERWALL_TUNNEL("Underwall tunnel", "Climb-into", new WalkerTile(3141, 3513, 0), new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return destinationDetails.getAssumed().equals(new WalkerTile(3141, 3513, 0 ));
            }
        }),
        GAMES_ROOM_STAIRS("Stairs", "Climb-down", new WalkerTile(2899, 3565, 0), new SpecialCondition() {
            @Override
            boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails) {
                return destinationDetails.getDestination().getWalkerTile().equals(new WalkerTile(2899, 3565, 0)) &&
                    destinationDetails.getAssumed().equals(new WalkerTile(2205, 4934, 1));
            }
        });

        private String name, action;
        private WalkerTile location;
        private SpecialCondition specialCondition;

        SpecialObject(String name, String action, WalkerTile location, SpecialCondition specialCondition){
            this.name = name;
            this.action = action;
            this.location = location;
            this.specialCondition = specialCondition;
        }

        public String getName() {
            return name;
        }

        public String getAction() {
            return action;
        }

        public WalkerTile getLocation() {
            return location;
        }

        public boolean isSpecialCondition(PathAnalyzer.DestinationDetails destinationDetails){
            return specialCondition.isSpecialLocation(destinationDetails);
        }

        public static SpecialObject getValidSpecialObjects(PathAnalyzer.DestinationDetails destinationDetails){
            for (SpecialObject object : values()){
                if (object.isSpecialCondition(destinationDetails)){
                    return object;
                }
            }
            return null;
        }

    }

    private abstract static class SpecialCondition {
        abstract boolean isSpecialLocation(PathAnalyzer.DestinationDetails destinationDetails);
    }

    public static boolean handle(PathAnalyzer.DestinationDetails destinationDetails, List<WalkerTile> path){
        RealTimeCollisionTile start = destinationDetails.getDestination(), end = destinationDetails.getNextTile();

        RSObject[] interactiveObjects = null;

        String action = null;
        SpecialObject specialObject = SpecialObject.getValidSpecialObjects(destinationDetails);
        if (specialObject == null) {
            if ((interactiveObjects = getInteractiveObjects(start.getX(), start.getY(), start.getZ(), destinationDetails)).length < 1 && end != null) {
                interactiveObjects = getInteractiveObjects(end.getX(), end.getY(), end.getZ(), destinationDetails);
            }
        } else {
            action = specialObject.getAction();
            Filter<RSObject> specialObjectFilter = Filters.Objects.nameEquals(specialObject.getName())
                    .combine(Filters.Objects.actionsContains(specialObject.getAction()), true)
                    .combine(Filters.Objects.inArea(new RSArea(specialObject.getLocation() != null ? specialObject.getLocation() : destinationDetails.getAssumed(), 1)), true);
            interactiveObjects = new RSObject[]{Web.methods.objects.getNearest(15, specialObjectFilter)};
        }

        if (interactiveObjects.length == 0) {
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder("Sort Order: ");
        Arrays.stream(interactiveObjects).forEach(rsObject -> stringBuilder.append(rsObject.getDef().getName()).append(" ").append(
		        Arrays.asList(rsObject.getDef().getActions())).append(", "));
        getInstance().log(stringBuilder);

        return handle(path, interactiveObjects[0], destinationDetails, action, specialObject);
    }

    private static boolean handle(List<WalkerTile> path, RSObject object, PathAnalyzer.DestinationDetails destinationDetails, String action, SpecialObject specialObject){
        PathAnalyzer.DestinationDetails current = PathAnalyzer.furthestReachableTile(path);

        if (current == null){
            return false;
        }

        RealTimeCollisionTile currentFurthest = current.getDestination();
        if (!Web.methods.players.getMyPlayer().isLocalPlayerMoving() && (!object.isOnScreen() || !object.isClickable())){
            if (!WalkerEngine.getInstance().clickMinimap(destinationDetails.getDestination())){
                return false;
            }
        }
        if (WaitFor.condition(StdRandom.uniform(5000, 8000), () -> object.isOnScreen() && object.isClickable() ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) != WaitFor.Return.SUCCESS) {
            return false;
        }

        boolean successfulClick = false;

        if (specialObject != null) {
            getInstance().log("Detected Special Object: " + specialObject);
            switch (specialObject){
                case WEB:
                    List<RSObject> webs;
                    int iterations = 0;
                    while ((webs = Arrays.stream(Web.methods.objects.getAllAt(object.getLocation()))
                            .filter(object1 -> Arrays.stream(RSObjectHelper.getActions(object1))
                                    .anyMatch(s -> s.equals("Slash"))).collect(Collectors.toList())).size() > 0){
                        RSObject web = webs.get(0);
                        if (canLeftclickWeb()) {
                            InteractionHelper.click(web, "Slash");
                        } else {
                            useBladeOnWeb(web);
                        }
                        if(Web.methods.chooseOption.getHoverText().contains("->")){
                            Web.methods.walking.walkTo(new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())));
                        }
                        if (web.getLocation().distanceTo(new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()))) <= 1) {
                            WaitFor.milliseconds((int) StdRandom.gaussian(50, 800, 250, 150));
                        } else {
                            WaitFor.milliseconds(2000, 4000);
                        }
                        if (Reachable.getMap().getParent(destinationDetails.getAssumedX(), destinationDetails.getAssumedY()) != null &&
                                (webs = Arrays.stream(Web.methods.objects.getAllAt(object.getLocation())).filter(object1 -> Arrays.stream(RSObjectHelper.getActions(object1))
                                        .anyMatch(s -> s.equals("Slash"))).collect(Collectors.toList())).size() == 0){
                            successfulClick = true;
                            break;
                        }
                        if (iterations++ > 5){
                            break;
                        }
                    }
                    break;
                case ARDY_DOOR_LOCK_SIDE:
                case YANILLE_DOOR_LOCK_SIDE:
                    for (int i = 0; i < StdRandom.uniform(15, 25); i++) {
                        if (!clickOnObject(object, new String[]{specialObject.getAction()})){
                            continue;
                        }
                        if (new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).distanceTo(specialObject.getLocation()) > 1){
                            WaitFor.condition(StdRandom.uniform(3000, 4000), () -> new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).distanceTo(specialObject.getLocation()) <= 1 ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE);
                        }
                        if (new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).equals(new WalkerTile(2564, 3356, 0))){
                            successfulClick = true;
                            break;
                        }
                    }
                    break;
                case VARROCK_UNDERWALL_TUNNEL:
                    if(!clickOnObject(object,specialObject.getAction())){
                        return false;
                    }
                    successfulClick = true;
                    WaitFor.condition(10000, () ->
                            SpecialObject.EDGEVILLE_UNDERWALL_TUNNEL.getLocation().equals(new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()))) ?
                                    WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE);
                    break;
                case EDGEVILLE_UNDERWALL_TUNNEL:
                    if(!clickOnObject(object,specialObject.getAction())){
                        return false;
                    }
                    successfulClick = true;
                    WaitFor.condition(10000, () ->
                            SpecialObject.VARROCK_UNDERWALL_TUNNEL.getLocation().equals(new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()))) ?
                                    WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE);
                    break;
            }
        }

        if (!successfulClick){
            String[] validOptions = action != null ? new String[]{action} : getViableOption(
		            Arrays.stream(object.getDef().getActions()).filter(getInstance().sortedOptions::contains).collect(
				            Collectors.toList()), destinationDetails);
            if (!clickOnObject(object, validOptions)) {
                return false;
            }
        }

        boolean strongholdDoor = isStrongholdDoor(object);

        if (strongholdDoor){
            if (WaitFor.condition(StdRandom.uniform(6700, 7800), () -> {
                WalkerTile playerPosition = new WalkerTile(new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())));
                if (BFS.isReachable(RealTimeCollisionTile.get(playerPosition.getX(), playerPosition.getY(), playerPosition.getPlane()), destinationDetails.getNextTile(), 50)) {
                    WaitFor.milliseconds(500, 1000);
                    return WaitFor.Return.SUCCESS;
                }
                if (NPCInteraction.isConversationWindowUp()) {
                    handleStrongholdQuestions();
                    return WaitFor.Return.SUCCESS;
                }
                return WaitFor.Return.IGNORE;
            }) != WaitFor.Return.SUCCESS){
                return false;
            }
        }

        WaitFor.condition(StdRandom.uniform(8500, 11000), () -> {
            DoomsToggle.handleToggle();
            PathAnalyzer.DestinationDetails destinationDetails1 = PathAnalyzer.furthestReachableTile(path);
            if (NPCInteraction.isConversationWindowUp()) {
                NPCInteraction.handleConversation(NPCInteraction.GENERAL_RESPONSES);
            }
            if (destinationDetails1 != null) {
                if (!destinationDetails1.getDestination().equals(currentFurthest)){
                    return WaitFor.Return.SUCCESS;
                }
            }
            if (current.getNextTile() != null){
                PathAnalyzer.DestinationDetails hoverDetails = PathAnalyzer.furthestReachableTile(path, current.getNextTile());
                if (hoverDetails != null && hoverDetails.getDestination() != null && hoverDetails.getDestination().getWalkerTile().distanceTo(new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()))) > 7 && !strongholdDoor && new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).distanceTo(object) <= 2){
                    WalkerEngine.getInstance().hoverMinimap(hoverDetails.getDestination());
                }
            }
            return WaitFor.Return.IGNORE;
        });
        if (strongholdDoor){
            Web.methods.web.sleep(StdRandom.uniform(800, 1200));
        }
        return true;
    }

    public static RSObject[] getInteractiveObjects(int x, int y, int z, PathAnalyzer.DestinationDetails destinationDetails){
        RSObject[] objects = Web.methods.objects.getAll(interactiveObjectFilter(x, y, z, destinationDetails));
        final WalkerTile base = new WalkerTile(x, y, z);
        Arrays.sort(objects, (o1, o2) -> {
            int c = Integer.compare(o1.getLocation().distanceTo(base), o2.getLocation().distanceTo(base));
            int assumedZ = destinationDetails.getAssumedZ(), destinationZ = destinationDetails.getDestination().getZ();
            List<String> actions1 = Arrays.asList(o1.getDef().getActions());
            List<String> actions2 = Arrays.asList(o2.getDef().getActions());

            if (assumedZ > destinationZ){
                if (actions1.contains("Climb-up")){
                    return -1;
                }
                if (actions2.contains("Climb-up")){
                    return 1;
                }
            } else if (assumedZ < destinationZ){
                if (actions1.contains("Climb-down")){
                    return -1;
                }
                if (actions2.contains("Climb-down")){
                    return 1;
                }
            } else if(destinationDetails.getAssumed().distanceTo(destinationDetails.getDestination().getWalkerTile()) > 20){
                if(actions1.contains("Climb-up") || actions1.contains("Climb-down")){
                    return -1;
                } else if(actions2.contains("Climb-up") || actions2.contains("Climb-down")){
                    return 1;
                }
            } else if(actions1.contains("Climb-up") || actions1.contains("Climb-down")){
                return 1;
            } else if(actions2.contains("Climb-up") || actions2.contains("Climb-down")){
                return -1;
            }
            return c;
        });
        StringBuilder a = new StringBuilder("Detected: ");
        Arrays.stream(objects).forEach(object -> a.append(object.getDef().getName()).append(" "));
        getInstance().log(a);



        return objects;
    }

    /**
     * Filter that accepts only interactive objects to progress in path.
     *
     * @param x
     * @param y
     * @param z
     * @param destinationDetails context where destination is at
     * @return
     */
    private static Filter<RSObject> interactiveObjectFilter(int x, int y, int z, PathAnalyzer.DestinationDetails destinationDetails){
        final WalkerTile position = new WalkerTile(x, y, z);
        return new Filter<RSObject>() {
            @Override
            public boolean test(RSObject rsObject) {
                ObjectComposition def = rsObject.getDef();
                if (def == null){
                    return false;
                }
                String name = def.getName();
                if (getInstance().sortedBlackList.contains(name)) {
                    return false;
                }
                if (RSObjectHelper.getActionsList(rsObject).stream().anyMatch(s -> getInstance().sortedBlackListOptions.contains(s))){
                    return false;
                }
                if (rsObject.getLocation().distanceTo(destinationDetails.getDestination().getWalkerTile()) > 5) {
                    return false;
                }
                if (Arrays.stream(rsObject.getArea().getTileArray()).noneMatch(rsTile -> new WalkerTile(rsTile).distanceTo(position) <= 2)) {
                    return false;
                }
                List<String> options = Arrays.asList(def.getActions());
                return options.stream().anyMatch(getInstance().sortedOptions::contains);
            }
        };
    }

    private static String[] getViableOption(Collection<String> collection, PathAnalyzer.DestinationDetails destinationDetails){
        Set<String> set = new HashSet<>(collection);
        if (set.retainAll(getInstance().sortedHighPriorityOptions) && set.size() > 0){
            return set.toArray(new String[set.size()]);
        }
        if (destinationDetails.getAssumedZ() > destinationDetails.getDestination().getZ()){
            if (collection.contains("Climb-up")){
                return new String[]{"Climb-up"};
            }
        }
        if (destinationDetails.getAssumedZ() < destinationDetails.getDestination().getZ()){
            if (collection.contains("Climb-down")){
                return new String[]{"Climb-down"};
            }
        }
        if (destinationDetails.getAssumedY() > 5000 && destinationDetails.getDestination().getZ() == 0 && destinationDetails.getAssumedZ() == 0){
            if (collection.contains("Climb-down")){
                return new String[]{"Climb-down"};
            }
        }
        String[] options = new String[collection.size()];
        collection.toArray(options);
        return options;
    }

    private static boolean clickOnObject(RSObject object, String... options){
        boolean result;

        if (isClosedTrapDoor(object, options)){
            result = handleTrapDoor(object);
        } else {
            result = InteractionHelper.click(object, options);
            getInstance().log("Interacting with (" + RSObjectHelper.getName(object) + ") at " + object.getLocation() + " with options: " + Arrays.toString(options) + " " + (result ? "SUCCESS" : "FAIL"));
            WaitFor.milliseconds(250,800);
        }

        return result;
    }

    private static boolean isStrongholdDoor(RSObject object){
        List<String> doorNames = Arrays.asList("Gate of War", "Rickety door", "Oozing barrier", "Portal of Death");
        return  doorNames.contains(object.getDef().getName());
    }



    private static void handleStrongholdQuestions() {
        NPCInteraction.handleConversation("Use the Account Recovery System.",
            "No, you should never buy an account.",
            "Nobody.",
            "Don't tell them anything and click the 'Report Abuse' button.",
            "Decline the offer and report that player.",
            "Me.",
            "Only on the RuneScape website.",
            "Report the incident and do not click any links.",
            "Authenticator and two-step login on my registered email.",
            "No way! You'll just take my gold for your own! Reported!",
            "No.",
            "Don't give them the information and send an 'Abuse Report'.",
            "Don't give them my password.",
            "The birthday of a famous person or event.",
            "Through account settings on runescape.com.",
            "Secure my device and reset my RuneScape password.",
            "Report the player for phishing.",
            "Don't click any links, forward the email to reportphishing@jagex.com.",
            "Inform Jagex by emailing reportphishing@jagex.com.",
            "Don't give out your password to anyone. Not even close friends.",
            "Politely tell them no and then use the 'Report Abuse' button.",
            "Set up 2 step authentication with my email provider.",
            "No, you should never buy a RuneScape account.",
            "Do not visit the website and report the player who messaged you.",
            "Only on the RuneScape website.",
            "Don't type in my password backwards and report the player.",
            "Virus scan my device then change my password.",
            "No, you should never allow anyone to level your account.",
            "Don't give out your password to anyone. Not even close friends.",
            "Report the stream as a scam. Real Jagex streams have a 'verified' mark.",
            "Report the stream as a scam. Real Jagex streams have a 'verified' mark",
            "Read the text and follow the advice given.",
            "No way! I'm reporting you to Jagex!",
            "Talk to any banker in RuneScape.",
            "Secure my device and reset my RuneScape password.",
            "Secure my device and reset my password.",
            "Delete it - it's a fake!",
            "Use the account management section on the website.",
            "Politely tell them no and then use the 'Report Abuse' button.",
            "Through account setting on oldschool.runescape.com",
            "Through account setting on oldschool.runescape.com.",
            "Nothing, it's a fake.",
            "Only on the Old School RuneScape website.",
            "Don't share your information and report the player.");
    }


    private static boolean isClosedTrapDoor(RSObject object, String[] options){
        return  (object.getDef().getName().equals("Trapdoor") && Arrays.asList(options).contains("Open"));
    }

    private static boolean handleTrapDoor(RSObject object){
        if (getActions(object).contains("Open")){
            if (!InteractionHelper.click(object, "Open", () -> {
                RSObject[] objects = new RSObject[]{Web.methods.objects.getNearest(Filters.Objects.actionsContains("Climb-down").combine(Filters.Objects.inArea(new RSArea(object, 2)), true))};
                if (objects.length > 0 && getActions(objects[0]).contains("Climb-down")){
                    return WaitFor.Return.SUCCESS;
                }
                return WaitFor.Return.IGNORE;
            })){
                return false;
            } else {
                RSObject[] objects = new RSObject[] {Web.methods.objects.getNearest(Filters.Objects.actionsContains("Climb-down").combine(Filters.Objects.inArea(new RSArea(object, 2)), true))};
                return objects.length > 0 && handleTrapDoor(objects[0]);
            }
        }
        getInstance().log("Interacting with (" + object.getDef().getName() + ") at " + object.getLocation() + " with option: Climb-down");
        return InteractionHelper.click(object, "Climb-down");
    }

    public static List<String> getActions(RSObject object){
        List<String> list = new ArrayList<>();
        if (object == null){
            return list;
        }
        ObjectComposition objectDefinition = object.getDef();
        if (objectDefinition == null){
            return list;
        }
        String[] actions = objectDefinition.getActions();
        if (actions == null){
            return list;
        }
        return Arrays.asList(actions);
    }

    @Override
    public String getName() {
        return "Object Handler";
    }

    private static List<Integer> SLASH_WEAPONS = new ArrayList<>(Arrays.asList(1,4,9,10,12,17,20,21));

    private static boolean canLeftclickWeb(){
        return (SLASH_WEAPONS.contains(Web.methods.client.getVarbitValue(357))) || Web.methods.inventory.getItems(Web.methods.inventory.getItemID("Knife")).length > 0;
    }
    private static boolean useBladeOnWeb(RSObject web){
        if(!Web.methods.chooseOption.getHoverText().contains("->")){
            RSItem[] slashable = Web.methods.inventory.find(Filters.Items.nameContains("whip", "sword", "dagger", "claws", "scimitar", " axe", "knife", "halberd", "machete", "rapier"));
            if(slashable.length == 0 || !slashable[0].doAction("Use"))
                return false;
        }
        return InteractionHelper.click(web, Web.methods.chooseOption.getHoverText());
    }

}
