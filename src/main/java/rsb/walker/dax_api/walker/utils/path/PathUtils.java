package rsb.walker.dax_api.walker.utils.path;

import rsb.methods.Calculations;
import rsb.methods.Web;
import rsb.wrappers.subwrap.WalkerTile;


import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PathUtils {

    public static WalkerTile getNextTileInPath(WalkerTile current, List<WalkerTile> path) {
        int index = path.indexOf(current);

        if (index == -1) {
            return null;
        }

        int next = index + 1;
        return next < path.size() ? path.get(next) : null;
    }

    public static WalkerTile getClosestTileInPath(List<WalkerTile> path) {
        WalkerTile player = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        return path.stream().min(Comparator.comparingDouble(o -> o.distanceToDouble(player))).orElse(null);
    }

    public static WalkerTile getFurthestReachableTileInMinimap(List<WalkerTile> path) {
        List<WalkerTile> reversed = new ArrayList<>(path);
        Collections.reverse(reversed);

        DaxPathFinder.Destination[][] map = DaxPathFinder.getMap();
        for (WalkerTile tile : reversed) {
            net.runelite.api.Point point = Web.methods.calc.tileToMinimap(tile);
            if (point == null) {
                continue;
            }
            if (DaxPathFinder.canReach(map, tile) && Web.methods.calc.tileOnMap(tile)) {
                return tile;
            }
        }
        return null;
    }

    public static WalkerTile getFurthestReachableTileOnScreen(List<WalkerTile> path) {
        List<WalkerTile> reversed = new ArrayList<>(path);
        Collections.reverse(reversed);

        DaxPathFinder.Destination[][] map = DaxPathFinder.getMap();
        for (WalkerTile tile : reversed) {
            if (DaxPathFinder.canReach(map, tile) && tile.isOnScreen() && tile.isClickable()) {
                return tile;
            }
        }
        return null;
    }

    public static void drawDebug(Graphics graphics, List<WalkerTile> path) {
        Graphics2D g = (Graphics2D) graphics;
        WalkerTile player = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));

        g.setColor(new Color(0, 191, 23, 80));
        for (WalkerTile tile : path) {
            if (tile.distanceTo(player) > 25) {
                continue;
            }
            Polygon polygon = Web.methods.calc.getTileBoundsPoly(tile, 0);
            if (polygon == null) {
                continue;
            }
            g.fillPolygon(polygon);
        }

        WalkerTile closest = getClosestTileInPath(path);
        if (closest != null) {
            Polygon polygon = Web.methods.calc.getTileBoundsPoly(closest, 0);
            if (polygon != null) {
                g.setColor(new Color(205, 0, 255, 80));
                g.fillPolygon(polygon);

                g.setColor(Color.BLACK);
                graphics.drawString("Closest In Path", polygon.xpoints[0] - 24, polygon.ypoints[1] + 1);
                g.setColor(Color.WHITE);
                graphics.drawString("Closest In Path", polygon.xpoints[0] - 25, polygon.ypoints[1]);
            }
        }

        WalkerTile furthestScreenTile = getFurthestReachableTileOnScreen(path);
        if (furthestScreenTile != null) {
            Polygon polygon = Web.methods.calc.getTileBoundsPoly(furthestScreenTile, 0);
            if (polygon != null) {
                g.setColor(new Color(255, 0, 11, 157));
                g.fillPolygon(polygon);

                g.setColor(Color.BLACK);
                graphics.drawString("Furthest Screen Tile", polygon.xpoints[0] - 24, polygon.ypoints[1] + 30);
                g.setColor(Color.WHITE);
                graphics.drawString("Furthest Screen Tile", polygon.xpoints[0] - 25, polygon.ypoints[1] + 30);
            }
        }

        WalkerTile furthestMapTile = getFurthestReachableTileInMinimap(path);
        if (furthestMapTile != null) {
            Point p = Calculations.convertRLPointToAWTPoint(Web.methods.calc.tileToMinimap(furthestMapTile));
            if (p != null) {
                g.setColor(new Color(255, 0, 11, 157));
                g.fillRect(p.x - 3, p.y - 3, 6, 6);

                g.setColor(Color.BLACK);
                graphics.drawString("Furthest Map Tile", p.x + 1, p.y + 14);
                g.setColor(Color.WHITE);
                graphics.drawString("Furthest Map Tile", p.x, p.y + 15);
            }
        }

        WalkerTile nextTile = getNextTileInPath(furthestMapTile, path);
        if (nextTile != null) {
            Polygon polygon = Web.methods.calc.getTileBoundsPoly(nextTile, 0);
            if (polygon != null) {
                g.setColor(new Color(255, 242, 0, 157));
                g.fillPolygon(polygon);

                g.setColor(Color.BLACK);
                graphics.drawString("Next Tile", polygon.xpoints[0] - 24, polygon.ypoints[1]);
                g.setColor(Color.WHITE);
                graphics.drawString("Next Tile", polygon.xpoints[0] - 25, polygon.ypoints[1]);
            }
        }


    }

    public static class NotInPathException extends RuntimeException {
        public NotInPathException() {
        }
    }

}
