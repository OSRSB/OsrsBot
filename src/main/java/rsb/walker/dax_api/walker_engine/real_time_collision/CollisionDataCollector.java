package rsb.walker.dax_api.walker_engine.real_time_collision;

import rsb.methods.Web;
import rsb.wrappers.subwrap.WalkerTile;


public class CollisionDataCollector {

    public static void generateRealTimeCollision(){
        RealTimeCollisionTile.clearMemory();

        WalkerTile playerPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        int[][] collisionData = Web.methods.walking.getCollisionFlags(Web.methods.client.getPlane());

        if (collisionData == null) {
            return;
        }

        for (int i = 0; i < collisionData.length; i++) {
            for (int j = 0; j < collisionData[i].length; j++) {
                WalkerTile localTile = new WalkerTile(i, j, playerPosition.getPlane(), WalkerTile.TYPES.SCENE);
                WalkerTile worldTile = localTile.toWorldTile();
                RealTimeCollisionTile.create(worldTile.getX(), worldTile.getY(), worldTile.getPlane(), collisionData[i][j]);
            }
        }
    }

    public static void updateRealTimeCollision(){
        WalkerTile playerPosition = new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation()));
        int[][] collisionData = Web.methods.walking.getCollisionFlags(Web.methods.client.getPlane());
        if(collisionData == null)
            return;
        for (int i = 0; i < collisionData.length; i++) {
            for (int j = 0; j < collisionData[i].length; j++) {
                WalkerTile localTile = new WalkerTile(i, j, playerPosition.getPlane(), WalkerTile.TYPES.SCENE);
                WalkerTile worldTile = localTile.toWorldTile();
                RealTimeCollisionTile realTimeCollisionTile = RealTimeCollisionTile.get(worldTile.getX(), worldTile.getY(), worldTile.getPlane());
                if (realTimeCollisionTile != null){
                    realTimeCollisionTile.setCollisionData(collisionData[i][j]);
                } else {
                    RealTimeCollisionTile.create(worldTile.getX(), worldTile.getY(), worldTile.getPlane(), collisionData[i][j]);
                }
            }
        }
    }

}
