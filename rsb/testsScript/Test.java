package net.runelite.client.rsb.testsScript;

import net.runelite.api.Point;
import net.runelite.client.rsb.botLauncher.RuneLite;
import net.runelite.client.rsb.botLauncher.RuneLiteTestFeatures;
import net.runelite.client.rsb.event.impl.TFPS;
import net.runelite.client.rsb.event.listener.PaintListener;
import net.runelite.client.rsb.methods.Magic;
import net.runelite.client.rsb.script.Script;
import net.runelite.client.rsb.script.ScriptManifest;
import net.runelite.client.rsb.util.StdRandom;
import net.runelite.client.rsb.wrappers.*;

import javax.swing.*;
import java.awt.*;


@ScriptManifest(authors={"Gigia"}, description="Example Script.", version=0.1, name="Test")
public class Test extends Script implements PaintListener {
    public int i = 50;

    public boolean up = false;

    public int loop() {
        if (ctx.client != null && ctx.client.getLocalPlayer() != null) {
            RuneLiteTestFeatures.testFeature(getBot());
        }
        sleep(StdRandom.uniform(1000, 1500));
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
