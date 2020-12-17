package rsb.wrappers;

import net.runelite.api.Model;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import rsb.methods.MethodContext;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author GigiaJ
 */
public class RSWallObjectModel extends RSModel {
    private WallObject object;


    RSWallObjectModel(MethodContext ctx, Model model, TileObject object) {
        super(ctx, model);
        this.object = (WallObject) object;
    }

    /**
     * @Author GigiaJ
     * @Broken Fix later for handling two walls otherwise we can only click on the first model
     *
     * @param ctx
     * @param model
     * @param model2
     * @param object
     */
    RSWallObjectModel(MethodContext ctx, Model model, Model model2, TileObject object) {
        super(ctx, model);
        this.object = (WallObject) object;
    }

    protected void update() {

    }

    @Override
    protected int getLocalX() {
        return object.getLocalLocation().getX();
    }

    @Override
    protected int getLocalY() {
        return object.getLocalLocation().getY();
    }
}