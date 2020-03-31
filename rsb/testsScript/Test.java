package net.runelite.client.rsb.testsScript;

import net.runelite.client.rsb.event.impl.TFPS;
import net.runelite.client.rsb.gui.ClientPanel;
import net.runelite.client.rsb.methods.Magic;
import net.runelite.client.rsb.script.Script;
import net.runelite.client.rsb.script.ScriptManifest;
import net.runelite.client.rsb.wrappers.RSNPC;
import net.runelite.client.rsb.wrappers.RSPlayer;
import net.runelite.client.rsb.wrappers.RSWidget;

import javax.swing.*;


@ScriptManifest(authors={"Gigia"}, description="Test Program.", version=0.1, name="Test")
public class Test extends Script {
    RSNPC enemy = null;
    public int loop() {
        if (ctx.client != null && ctx.client.getLocalPlayer() != null) {
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
        }
        return random(1000, 1000);
    }
}