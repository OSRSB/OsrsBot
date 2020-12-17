package rsb.wrappers;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import rsb.methods.MethodContext;

import java.awt.*;

class RSObjectModel extends RSModel {

    private final GameObject object;

    RSObjectModel(MethodContext ctx, Model model, GameObject object) {
        super(ctx, model);
        this.object = object;
    }

    protected void update() {

    }

    @Override
    protected int getLocalX() {
        return object.getX();
    }

    @Override
    protected int getLocalY() {
        return object.getY();
    }
    @Override
    public int getOrientation() {
        return object.getOrientation().getAngle();
    }





}