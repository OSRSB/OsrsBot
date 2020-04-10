package net.runelite.client.rsb.testsScript;

import net.runelite.api.Point;
import net.runelite.client.rsb.botLauncher.RuneLiteTestFeatures;
import net.runelite.client.rsb.event.impl.TFPS;
import net.runelite.client.rsb.event.listener.PaintListener;
import net.runelite.client.rsb.methods.Magic;
import net.runelite.client.rsb.script.Script;
import net.runelite.client.rsb.script.ScriptManifest;
import net.runelite.client.rsb.wrappers.RSNPC;
import net.runelite.client.rsb.wrappers.RSObject;
import net.runelite.client.rsb.wrappers.RSPlayer;
import net.runelite.client.rsb.wrappers.RSWidget;

import javax.swing.*;
import java.awt.*;


@ScriptManifest(authors={"Gigia"}, description="Example Script.", version=0.1, name="Example")
public class Test extends Script implements PaintListener {
    public int i = 50;

    public boolean up = false;
    RSNPC enemy = null;
    RSObject object = null;
    public int loop() {
        if (ctx.client != null && ctx.client.getLocalPlayer() != null) {
            if (object == null) {
                object = ctx.objects.getNearest("Tree");
                RuneLiteTestFeatures.init(ctx.runeLite);
                if (object != null) {
                    if (!object.isOnScreen()) {
                        object.turnTo();
                    } else {
                        object.doAction("Chop down", "Tree");
                    }
                }

            /*
            if (enemy == null || (new RSPlayer(ctx, ctx.client.getLocalPlayer())).getInteracting() != enemy.getAccessor()) {
                enemy = ctx.npcs.getNearest("Grand Exchange Clerk");
                if (enemy != null) {
                    if (!enemy.isInteractingWithLocalPlayer()) {
                        if (!enemy.isOnScreen()) {
                            enemy.turnTo();
                        } else {
                            enemy.doAction("Examine", "Grand Exchange Clerk");
                        }
                    }
                }
            }
            */
            }
        }
        return random(1000, 1000);
    }


    public final void onRepaint(Graphics g) {
        Point p = mouse.getLocation();
        int w = game.getWidth(), h = game.getHeight();
        if (i >= 70 && !up) {
            i--;
        } else {
            i++;
            up = i < 130;
        }
        g.setColor(new Color(0, 255, 0, i));
        g.fillRect(0, 0, p.getX() - 1, p.getY() - 1);
        g.fillRect(p.getX() + 1, 0, w - (p.getX() + 1), p.getY() - 1);
        g.fillRect(0, p.getY() + 1, p.getX() - 1, h - (p.getY() - 1));
        g.fillRect(p.getX() + 1, p.getY() + 1, w - (p.getX() + 1), h - (p.getY() - 1));
        g.setColor(Color.RED);
        g.drawString("Script Active: " + getClass().getAnnotation(ScriptManifest.class).name(), 540, 20);
    }
}
