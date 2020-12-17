package rsb.walker.dax_api.walker_engine;

import rsb.methods.Web;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.shared.NodeInfo;
import rsb.walker.dax_api.walker_engine.local_pathfinding.PathAnalyzer;
import rsb.walker.dax_api.walker_engine.real_time_collision.RealTimeCollisionTile;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WebWalkerPaint {

    private final int REGION_SIZE = 104, TILE_WIDTH = 4;
    private final BufferedImage nonDisplayableMapImage, mapDisplay;
    private final Graphics2D nonDisplayableMapImageGraphics, mapGraphicsDisplay;

    private final Point mapCenter;
    private final ExecutorService service;
    private WalkerTile playerPosition;
    private int lastChange;


    private static WebWalkerPaint instance;

    private WebWalkerPaint(){
        nonDisplayableMapImage = new BufferedImage(REGION_SIZE * TILE_WIDTH, REGION_SIZE * TILE_WIDTH, BufferedImage.TYPE_INT_ARGB);
        mapDisplay = new BufferedImage(REGION_SIZE * TILE_WIDTH, REGION_SIZE * TILE_WIDTH, BufferedImage.TYPE_INT_ARGB);
        nonDisplayableMapImageGraphics = nonDisplayableMapImage.createGraphics();
        mapGraphicsDisplay = mapDisplay.createGraphics();
        mapCenter = new Point(641, 83);
        service = Executors.newSingleThreadExecutor();
        lastChange = -1;
    }

    public static WebWalkerPaint getInstance(){
        return instance != null ? instance : (instance = new WebWalkerPaint());
    }

    public void drawDebug(Graphics graphics) {
        drawDebug(graphics, true);
    }

    /**
     *
     * @param graphics graphics variable from on paint method
     * @param drawMap if you want to draw the map or not.
     */
    public void drawDebug(Graphics graphics, boolean drawMap) {
        if (!WalkerEngine.getInstance().isNavigating()){
            return;
        }
        if (playerPosition == null || !playerPosition.equals(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())) || lastChange != RealTimeCollisionTile.getAllInitialized().size()) {
            lastChange = RealTimeCollisionTile.getAllInitialized().size();
            playerPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
            final int playerX = playerPosition.getX(), playerY = playerPosition.getY();
            service.submit(() -> {
                nonDisplayableMapImageGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                nonDisplayableMapImageGraphics.fillRect(0, 0, REGION_SIZE * TILE_WIDTH, REGION_SIZE * TILE_WIDTH);
                nonDisplayableMapImageGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

                nonDisplayableMapImageGraphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

                int previousLocalX = -1, previousLocalY = -1;
                List<WalkerTile> path = WalkerEngine.getInstance().getCurrentPath();
                if (path != null) {
                    for (WalkerTile node : path) {
                        int relativeX = node.getX() - playerX, relativeY = playerY - node.getY();
                        int localX = (relativeX + REGION_SIZE / 2) * TILE_WIDTH, localY = (relativeY + REGION_SIZE / 2) * TILE_WIDTH;

//                    nonDisplayableMapImageGraphics.fillRect(localX, localY, TILE_WIDTH, TILE_WIDTH);

                        if (previousLocalX == -1) {
                            previousLocalX = localX;
                            previousLocalY = localY;
                            continue;
                        }

                        switch (node.getPlane()){
                            case 1:
                                nonDisplayableMapImageGraphics.setColor(new Color(0, 224, 255));
                                break;
                            case 2:
                                nonDisplayableMapImageGraphics.setColor(new Color(255, 115, 166));
                                break;
                            default:
                                nonDisplayableMapImageGraphics.setColor(new Color(0, 255, 23));
                        }

                        if (Point2D.distance(previousLocalX, previousLocalY, localX, localY) > 20){
                            nonDisplayableMapImageGraphics.setColor(new Color(233, 255, 224, 120));
                        }

                        nonDisplayableMapImageGraphics.drawLine(previousLocalX + TILE_WIDTH / 2, previousLocalY + TILE_WIDTH / 2, localX + TILE_WIDTH / 2, localY + TILE_WIDTH / 2);
                        previousLocalX = localX;
                        previousLocalY = localY;
                    }
                }
                if (drawMap) {
                    for (RealTimeCollisionTile realTimeCollisionTile : RealTimeCollisionTile.getAllInitialized()) {
                        int relativeX = realTimeCollisionTile.getX() - playerX, relativeY = playerY - realTimeCollisionTile.getY();

                        int localX = (relativeX + REGION_SIZE / 2) * TILE_WIDTH, localY = (relativeY + REGION_SIZE / 2) * TILE_WIDTH;

//                        nonDisplayableMapImageGraphics.setColor(new Color(0, 0, 0, 32));
//                        nonDisplayableMapImageGraphics.drawRect(localX, localY, TILE_WIDTH, TILE_WIDTH);

                        nonDisplayableMapImageGraphics.setColor(new Color(255, 255, 255, 47));
                        nonDisplayableMapImageGraphics.fillRect(localX, localY, TILE_WIDTH, TILE_WIDTH);

                        if (!realTimeCollisionTile.isWalkable()) {
                            nonDisplayableMapImageGraphics.setColor(new Color(255, 170, 4, 161));
                            nonDisplayableMapImageGraphics.fillRect(localX, localY, TILE_WIDTH, TILE_WIDTH);
                        }

                        RealTimeCollisionTile furthestReachable = PathAnalyzer.furthestReachable;
                        if (furthestReachable != null && furthestReachable.equals(realTimeCollisionTile)) {
                            nonDisplayableMapImageGraphics.setColor(new Color(0, 183, 255, 255));
                            nonDisplayableMapImageGraphics.fillRect(localX, localY, TILE_WIDTH, TILE_WIDTH);
                        }

                        if (playerX == realTimeCollisionTile.getX() && playerY == realTimeCollisionTile.getY()) {
                            nonDisplayableMapImageGraphics.setColor(new Color(255, 8, 0, 161));
                            nonDisplayableMapImageGraphics.fillRect(localX, localY, TILE_WIDTH, TILE_WIDTH);


                            RealTimeCollisionTile cache = PathAnalyzer.closestToPlayer;
                            if (cache != null) {
                                int relativeXToPlayer = cache.getX() - playerX, relativeYToPlayer = playerY - cache.getY();
                                int localXToPlayer = (relativeXToPlayer + REGION_SIZE / 2) * TILE_WIDTH, localYToPlayer = (relativeYToPlayer + REGION_SIZE / 2) * TILE_WIDTH;
                                nonDisplayableMapImageGraphics.drawLine(localXToPlayer, localYToPlayer, localX + TILE_WIDTH / 2, localY + TILE_WIDTH / 2);
                            }

                        }

                        try {
                            if (NodeInfo.get(realTimeCollisionTile).traversed) {
                                nonDisplayableMapImageGraphics.setColor(new Color(237, 98, 255, 161));
                                nonDisplayableMapImageGraphics.fillRect(localX, localY, TILE_WIDTH, TILE_WIDTH);
                            }
                        } catch (Exception e) {

                        }

                        nonDisplayableMapImageGraphics.setColor(new Color(255, 254, 253, 223));
                        if (realTimeCollisionTile.blockedNorth()) {
                            nonDisplayableMapImageGraphics.fillRect(localX, localY, TILE_WIDTH, TILE_WIDTH / 4);
                        }
                        if (realTimeCollisionTile.blockedEast()) {
                            nonDisplayableMapImageGraphics.fillRect(localX + TILE_WIDTH - TILE_WIDTH / 4, localY, TILE_WIDTH / 4, TILE_WIDTH);
                        }
                        if (realTimeCollisionTile.blockedSouth()) {
                            nonDisplayableMapImageGraphics.fillRect(localX, localY + TILE_WIDTH - TILE_WIDTH / 4, TILE_WIDTH, TILE_WIDTH / 4);
                        }
                        if (realTimeCollisionTile.blockedWest()) {
                            nonDisplayableMapImageGraphics.fillRect(localX, localY, TILE_WIDTH / 4, TILE_WIDTH);
                        }

                    }
                }
                mapGraphicsDisplay.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                mapGraphicsDisplay.fillRect(0, 0, REGION_SIZE * TILE_WIDTH, REGION_SIZE * TILE_WIDTH);
                mapGraphicsDisplay.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

                mapGraphicsDisplay.drawImage(nonDisplayableMapImage, 0, 0, null);
            });
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(Web.methods.camera.getAngle()), mapCenter.x - (REGION_SIZE/2 * TILE_WIDTH) + (REGION_SIZE/2 * TILE_WIDTH) + TILE_WIDTH/2, mapCenter.y -(REGION_SIZE/2 * TILE_WIDTH) + (REGION_SIZE/2 * TILE_WIDTH) + TILE_WIDTH/2);
//        affineTransform.rotate(Game.getMinimapRotation(), mapCenter.x - (REGION_SIZE/2 * TILE_WIDTH) + (REGION_SIZE/2 * TILE_WIDTH) + TILE_WIDTH/2, mapCenter.y -(REGION_SIZE/2 * TILE_WIDTH) + (REGION_SIZE/2 * TILE_WIDTH) + TILE_WIDTH/2);
        affineTransform.translate(mapCenter.x - (REGION_SIZE/2 * TILE_WIDTH), mapCenter.y -(REGION_SIZE/2 * TILE_WIDTH));
        graphics2D.drawImage(mapDisplay, affineTransform, null);
    }

}
