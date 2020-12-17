package rsb.walker.dax_api.walker.utils;

import net.runelite.api.ItemComposition;
import net.runelite.api.ObjectComposition;
import net.runelite.api.coords.LocalPoint;
import rsb.internal.wrappers.Filter;
import rsb.methods.Calculations;
import rsb.methods.Web;
import rsb.util.StdRandom;
import rsb.walker.dax_api.Filters;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.shared.helpers.RSItemHelper;
import rsb.walker.dax_api.shared.helpers.RSNPCHelper;
import rsb.walker.dax_api.shared.helpers.RSObjectHelper;
import rsb.walker.dax_api.walker_engine.WaitFor;
import rsb.wrappers.*;
import rsb.wrappers.common.Clickable;
import rsb.wrappers.common.Positionable;
import rsb.wrappers.subwrap.RSMenuNode;
import net.runelite.api.Point;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class does NOT examine objects.
 * <p>
 * clickAction should never include the target entity's name. Just the action.
 */
public class AccurateMouse {

    public static void move(int x, int y) {
        move(new Point(x, y));
    }

    public static void move(Point point) {
        Web.methods.mouse.move(point.getX(), point.getY());
    }

    public static void click(int button) {
        click(Web.methods.mouse.getLocation(), button);
    }

    public static void click(int x, int y) {
        click(x, y, 1);
    }

    public static void click(int x, int y, int button) {
        click(new Point(x, y), button);
    }

    public static void click(Point point) {
        click(point.getX(), point.getY(), 1);
    }

    public static void click(Point point, int button) {
        Web.methods.mouse.click(point, button==1);
    }


    public static boolean click(Clickable clickable, String... clickActions) {
        return action(clickable, false, clickActions);
    }

    public static boolean hover(Clickable clickable, String... clickActions) {
        return action(clickable, true, clickActions);
    }

    public static boolean clickMinimap(Positionable tile) {
        if (tile == null) {
            return false;
        }
        for (int i = 0; i < StdRandom.uniform(4, 6); i++) {
            LocalPoint dest = Web.methods.client.getLocalDestinationLocation();
            WalkerTile currentDestination = (dest != null) ? new WalkerTile(dest.getX(), dest.getY(), Web.methods.client.getPlane(), WalkerTile.TYPES.SCENE).toWorldTile() : null;
            if (currentDestination != null && currentDestination.equals(tile.getLocation())) {
                return true;
            }

            Point point = Web.methods.calc.tileToMinimap(tile.getLocation());
            if (point == null || !Web.methods.calc.tileOnMap(tile.getLocation())) {
                return false;
            }

            AccurateMouse.click(point);


            WalkerTile newDestination = WaitFor.getValue(250, () -> {
                RSTile rsTile = Web.methods.walking.getDestination();
                return rsTile == null || new WalkerTile(rsTile).equals(currentDestination) ? null : new WalkerTile(rsTile);
            });
            if (newDestination != null && newDestination.equals(tile)) {
                return true;
            }
        }
        return false;
    }

    public static boolean action(Clickable clickable, boolean hover, String... clickActions) {
        if (clickable == null) {
            return false;
        }
        String name = null;
        RSModel model = null;
        if (clickable instanceof RSCharacter) {
            RSCharacter rsCharacter = ((RSCharacter) clickable);
            name = rsCharacter.getName();
            model = rsCharacter.getModel();
        } else if (clickable instanceof RSGroundItem) {
            RSGroundItem rsGroundItem = ((RSGroundItem) clickable);
            ItemComposition rsItemDefinition = rsGroundItem.getItem().getDefinition();
            name = rsItemDefinition != null ? rsItemDefinition.getName() : null;
            model = rsGroundItem.getModel();
        } else if (clickable instanceof RSObject) {
            RSObject rsObject = ((RSObject) clickable);
            ObjectComposition rsObjectDefinition = rsObject.getDef();
            name = rsObjectDefinition != null ? rsObjectDefinition.getName() : null;
            model = rsObject.getModel();
        } else if (clickable instanceof RSItem) {
            name = RSItemHelper.getItemName((RSItem) clickable);
        }
        return action(model, clickable, name, hover, clickActions);
    }

    /**
     * @param model        model of {@code clickable}
     * @param clickable    target entity
     * @param clickActions actions to click or hover. Do not include {@code targetName}
     * @param targetName   name of the {@code clickable} entity
     * @param hover        True to hover the OPTION, not the entity model. It will right click {@code clickable} and hover over option {@code clickAction}
     * @return whether action was successful.
     */
    private static boolean action(RSModel model, Clickable clickable, String targetName, boolean hover, String... clickActions) {
        for (int i = 0; i < StdRandom.uniform(4, 7); i++) {
            if (attemptAction(model, clickable, targetName, hover, clickActions)) {
                return true;
            }
        }
        return false;
    }


    public static boolean walkScreenTile(WalkerTile destination) {
        if (!destination.isOnScreen() || !destination.isClickable()) {
            return false;
        }
        for (int i = 0; i < StdRandom.uniform(3, 5); i++) {
            Point point = getWalkingPoint(destination);
            if (point == null) {
                continue;
            }
            Web.methods.mouse.move(point);
            if (WaitFor.condition(100, () -> {
                String uptext = Web.methods.chooseOption.getHoverText();
                return uptext != null && uptext.startsWith("Walk here") ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE;
            }) == WaitFor.Return.SUCCESS) {
                click(1);
                if (waitResponse() == State.YELLOW) {
                    WalkerTile clicked = new WalkerTile(Objects.requireNonNull(WaitFor.getValue(900, Web.methods.walking::getDestination)));
                    return clicked.equals(destination) || Web.methods.players.getMyPlayer().getPosition().equals(destination);
                } else {
                    break;
                }
            }
        }
        return false;
    }

    public static boolean hoverScreenTileWalkHere(WalkerTile destination) {
        for (int i = 0; i < Web.methods.web.random(4, 6); i++) {
            Point point = getWalkingPoint(destination);
            if (point == null) {
                continue;
            }
            Web.methods.mouse.move(point);
            Web.methods.web.sleep(StdRandom.uniform(20, 30));
            return isHoveringScreenTileWalkHere(destination);
        }
        ;
        return false;
    }

    public static boolean isHoveringScreenTileWalkHere(WalkerTile destination) {
        return isWalkingPoint(new java.awt.Point(Web.methods.mouse.getLocation().getX(), Web.methods.mouse.getLocation().getY())
                , destination);
    }

    /**
     * Clicks or hovers desired action of entity.
     *
     * @param model        target entity model
     * @param clickable    target entity
     * @param clickActions actions
     * @param targetName   name of target
     * @param hover        hover option or not
     * @return result of action
     */
    private static boolean attemptAction(RSModel model, Clickable clickable, String targetName, boolean hover, String... clickActions) {
//        System.out.println((hover ? "Hovering over" : "Clicking on") + " " + targetName + " with [" + Arrays.stream(clickActions).reduce("", String::concat) + "]");

        if (handleRSItemRSInterface(clickable, hover, clickActions)) {
            return true;
        }

        Point point = null;
        if (clickable instanceof WalkerTile && Arrays.stream(clickActions).anyMatch(s -> s.matches("Walk here"))) {

            Web.methods.calc.getRandomPolyPoint(Web.methods.calc.getTileBoundsPoly((WalkerTile) clickable, 0));
            //point = ((WalkerTile) clickable).getHumanHoverPoint();
        } else if (model == null) {
            return false;
        }

        if (Web.methods.chooseOption.isOpen()) {
            RSMenuNode menuNode = getValidMenuNode(clickable, targetName, Web.methods.chooseOption.getMenuNodes(), clickActions);
            if (handleMenuNode(menuNode, hover)) {
                return true;
            } else {
                Web.methods.chooseOption.close();
            }
        }

        if (point == null) {
            point = model.getPointOnScreen();
            //point = model.getHumanHoverPoint();
        }

        if (point == null || point.getX() == -1) {
            return false;
        }

        java.awt.Point jPoint = new java.awt.Point(point.getX(), point.getY());

        if (jPoint.distance(new java.awt.Point(Web.methods.mouse.getLocation().getX(), Web.methods.mouse.getLocation().getY())) < Web.methods.mouse.getSpeed() / 20) {
            Web.methods.mouse.hop(point);
        } else {
            Web.methods.mouse.move(point);
        }

        if (!model.getConvexHull().contains(new java.awt.Point(point.getX(), point.getY()))) {
            return false;
        }

        if (hover && clickActions.length == 0) {
            return true;
        }

        String regex = //String.format("(%s)", Arrays.stream(clickActions).map(
                //Pattern::quote).collect(Collectors.toList())).replace("[", "").replace("]", "");
                String.format("(%s %s)(.*)", String.join("|", Arrays.stream(clickActions).map(
                Pattern::quote).collect(Collectors.toList())), targetName != null ? Pattern.quote(targetName) : "");



        if (WaitFor.condition(80, () -> Arrays.stream(Web.methods.chooseOption.getEntriesString()).anyMatch(s -> s.matches(regex)) ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) == WaitFor.Return.SUCCESS) {
            boolean multipleMatches = false;

            String[] options = Web.methods.chooseOption.getOptions();
            if (Arrays.stream(options).filter(s -> s.matches(regex)).count() > 1) {
                multipleMatches = true;
            }

            String uptext = Web.methods.chooseOption.getHoverText();
            if (uptext == null) { //double check
                return false;
            }

            if (uptext.matches(regex) && !hover && !multipleMatches) {
                click(1);
                return waitResponse() == State.RED;
            }

            click(3);
            RSMenuNode menuNode = getValidMenuNode(clickable, targetName, Web.methods.chooseOption.getMenuNodes(), clickActions);
            if (handleMenuNode(menuNode, hover)) {
                return true;
            }

        }
        return false;
    }

    private static boolean handleRSItemRSInterface(Clickable clickable, boolean hover, String... clickActions) {
        /**
         * TODO: Check usage to understand whether it is using the widget item or the actual interface
         *
         */
        if (!(clickable instanceof RSItem || clickable instanceof RSWidget)) {
            return false;
        }

        Rectangle area = clickable instanceof RSItem ? ((RSItem) clickable).getItem().getArea() : ((RSWidget) clickable).getArea();
        String uptext = Web.methods.chooseOption.getHoverText();
        if (area.contains(Calculations.convertRLPointToAWTPoint(Web.methods.mouse.getLocation()))) {
            if (uptext != null && (clickActions.length == 0 || Arrays.stream(clickActions).anyMatch(uptext::contains))) {
                if (hover) {
                    return true;
                }
                click(1);
                return true;
            } else {
                Web.methods.mouse.click(false);
                return Web.methods.chooseOption.select(clickActions);
            }
        } else {
            Web.methods.mouse.move(AccurateMouse.getRandomPoint(area));
            if (!hover) {
                for (String option : clickActions) {
                    if (clickable.doAction(option)) {
                        return true;
                    }
                }
                return false;
            }
            //TODO: handle hovering of interfaces for secondary actions such as right click hover
            return true;
        }
    }

    private static boolean handleMenuNode(RSMenuNode rsMenuNode, boolean hover) {
        if (rsMenuNode == null) {
            return false;
        }
        Rectangle rectangle = rsMenuNode.getArea();
        if (rectangle == null) {
            Web.methods.chooseOption.close();
            return false;
        }
        java.awt.Point currentMousePosition = new java.awt.Point (Web.methods.mouse.getLocation().getX(), Web.methods.mouse.getLocation().getY());
        if (hover) {
            if (!rectangle.contains(currentMousePosition)) {
                Web.methods.mouse.move(getRandomPoint(rectangle));
            }
        } else {
            Web.methods.mouse.click(getRandomPoint(rectangle), true);
        }
        return true;
    }

    private static RSMenuNode getValidMenuNode(Clickable clickable, String targetName, RSMenuNode[] menuNodes, String... clickActions) {
        if (clickable == null || targetName == null || menuNodes == null) {
            return null;
        }
        List<RSMenuNode> list = Arrays.stream(menuNodes).filter(rsMenuNode -> {
            String target = rsMenuNode.getTarget(), action = rsMenuNode.getAction();
            return target != null && action != null && Arrays.stream(clickActions).anyMatch(s -> s.equals(action)) && target.startsWith(targetName);
        }).collect(Collectors.toList());
        return list.stream().filter(rsMenuNode -> rsMenuNode.correlatesTo(clickable)).findFirst().orElse(list.size() > 0 ? list.get(0) : null);
    }

    public static State waitResponse() {
        State response = WaitFor.getValue(250, () -> {
            switch (getState()) {
                case YELLOW:
                    return State.YELLOW;
                case RED:
                    return State.RED;
            }
            return null;
        });
        return response != null ? response : State.NONE;
    }


    public static State getState() {
        int crosshairState = Web.methods.game.getCrosshairState();
        for (State state : State.values()) {
            if (state.id == crosshairState) {
                return state;
            }
        }
        return State.NONE;
    }

    public enum State {
        NONE(0),
        YELLOW(1),
        RED(2);
        private int id;

        State(int id) {
            this.id = id;
        }
    }


    /**
     * This is a cpu intensive method.
     *
     * @param destination
     * @return
     */
    public static Point getWalkingPoint(WalkerTile destination) {
        Area area = getTileModel(destination);
        ArrayList<Polygon> polygons = new ArrayList<>();
        for (RSTile tile : new RSArea(destination, 1).getTileArray()) {
            if (tile.equals(destination)) {
                continue;
            }
            polygons.add(Web.methods.calc.getTileBoundsPoly(new WalkerTile(tile), 0));
            polygons.addAll(
                    Arrays.stream(Web.methods.objects.getAllAt(tile)).filter(object -> RSObjectHelper.getActions(object).length > 0).map(RSObject::getModel).filter(Objects::nonNull).map(RSModel::getConvexHull).filter(Objects::nonNull).collect(
                            Collectors.toList()));
            polygons.addAll(
                    Arrays.stream(Web.methods.groundItems.getAllAt(tile)).filter(object -> RSItemHelper.getItemActions(object).length > 0).map(RSGroundItem::getModel).filter(Objects::nonNull).map(RSModel::getConvexHull).filter(Objects::nonNull).collect(
                            Collectors.toList()));
            polygons.addAll(
                    Arrays.stream(Web.methods.npcs.getAll(Filters.NPCs.tileEquals(new WalkerTile(tile)))).filter(object -> RSNPCHelper.getActions(object).length > 0).map(RSNPC::getModel).filter(Objects::nonNull).map(RSModel::getConvexHull).filter(Objects::nonNull).collect(
                            Collectors.toList()));
        }

        outterLoop:
        for (int i = 0; i < 1000; i++) {
            //Polygon uses a different kind of point to check so both are needed here
            Point runeLitePoint = getRandomPoint(area.getBounds());
            java.awt.Point point = new java.awt.Point(runeLitePoint.getX(), runeLitePoint.getY());
            if (Web.methods.calc.pointOnScreen(runeLitePoint) && area.contains(point)) {
                for (Polygon polygon : polygons) {
                    if (polygon.contains(point)) {
                        continue outterLoop;
                    }
                }
                return runeLitePoint;
            }
        }
        return null;
    }

    private static boolean isWalkingPoint(java.awt.Point point, WalkerTile destination) {
        String uptext = Web.methods.chooseOption.getHoverText();
        if (uptext == null || !uptext.startsWith("Walk here")) {
            return false;
        }

        Area area = getTileModel(destination);
        ArrayList<Polygon> polygons = new ArrayList<>();
        for (RSTile tile : new RSArea(destination, 1).getTileArray()) {
            if (tile.equals(destination)) {
                continue;
            }
            polygons.add(Web.methods.calc.getTileBoundsPoly(new WalkerTile(tile), 0));
        }

        if (!area.contains(point)) {
            return false;
        }

        for (Polygon polygon : polygons) {
            if (polygon.contains(point)) {
                return false;
            }
        }
        return true;
    }

    public static Point getRandomPoint(Rectangle rectangle) {
        return new Point(getRandomInteger(rectangle.x, rectangle.x + rectangle.width), getRandomInteger(rectangle.y, rectangle.y + rectangle.height));
    }

    private static int getRandomInteger(int min, int max) {
        return (int) (min + Math.floor(Math.random() * (max + 1 - min)));
    }

    private static Area getTileModel(WalkerTile tile) {
        Polygon tilePolygon = Web.methods.calc.getTileBoundsPoly(tile, 0);
        Area area = new Area(tilePolygon);
        for (RSObject rsObject : Web.methods.objects.getAll(Filters.Objects.inArea(new RSArea(tile, 3)))) {
            ObjectComposition definition = rsObject.getDef();
            if (definition == null) {
                continue;
            }

            String[] actions = definition.getActions();

            if (actions == null || actions.length == 0) {
                continue;
            }

            RSModel rsModel = rsObject.getModel();
            if (rsModel == null) {
                continue;
            }
            Area objectArea = new Area(rsModel.getConvexHull());
            area.subtract(objectArea);
        }
        for (RSGroundItem rsGroundItem : Web.methods.groundItems.getAll(new Filter<RSGroundItem>() {
            @Override
            public boolean test(RSGroundItem rsGroundItem) {
                return rsGroundItem.getLocation().equals(tile);
            }
        })) {
            ItemComposition definition = rsGroundItem.getItem().getDefinition();
            if (definition == null) {
                continue;
            }

            String[] actions = definition.getInventoryActions();

            if (actions == null || actions.length == 0) {
                continue;
            }

            RSModel rsModel = rsGroundItem.getModel();
            if (rsModel == null) {
                continue;
            }
            Area objectArea = new Area(rsModel.getConvexHull());
            area.subtract(objectArea);
        }
        return area;
    }

}
