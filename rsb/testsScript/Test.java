package net.runelite.client.rsb.testsScript;

import net.runelite.client.rsb.methods.Magic;
import net.runelite.client.rsb.script.Script;
import net.runelite.client.rsb.script.ScriptManifest;
import net.runelite.client.rsb.wrappers.RSNPC;
import net.runelite.client.rsb.wrappers.RSPlayer;


@ScriptManifest(authors={"Gigia"}, description="Test Program.", version=0.1, name="Test")
public class Test extends Script {
    RSNPC enemy = null;
    public int loop() {
        if (ctx.client != null && ctx.client.getLocalPlayer() != null) {
            sleep(2000);
            if (enemy == null || (new RSPlayer(ctx, ctx.client.getLocalPlayer())).getInteracting() != enemy.getAccessor()) {
                //enemy = bot.getMethodContext().npcs.getNearest("Chicken");
                ctx.magic.castSpell(Magic.SPELL_ENFEEBLE);
                if (enemy != null) {
                    if (!enemy.isInteractingWithLocalPlayer()) {
                        //enemy.turnTo();
                        //enemy.doAction("Attack", "Chicken");
                    }
                }
            }
        }
        return random(1000, 1000);
    }

}