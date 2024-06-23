package net.runelite.rsb.wrappers;

import net.runelite.api.ItemLayer;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.rsb.methods.MethodContext;

public class RSGroundObjectModel extends RSModel {
    private final Tile tile;

    RSGroundObjectModel(MethodContext ctx, Model model, Tile tile) {
        super(ctx, model);
        this.tile = tile;
    }

    protected void update() {
    }

    @Override
    protected int getLocalX() {
        return tile.getLocalLocation().getX();
    }

    @Override
    protected int getLocalY() {
        return tile.getLocalLocation().getY();
    }

    @Override
    protected int getLocalZ() {
        ItemLayer itemLayer = this.tile.getItemLayer();
        if (itemLayer != null) {
            return itemLayer.getZ() - itemLayer.getHeight();
        }
        else {
            return Perspective.getTileHeight(methods.client, new LocalPoint(getLocalX(), getLocalY()), methods.client.getPlane());
        }
    }
}
