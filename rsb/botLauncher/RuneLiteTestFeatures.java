package net.runelite.client.rsb.botLauncher;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.rsb.methods.Inventory;
import net.runelite.client.rsb.util.OutputObjectComparer;
import net.runelite.client.rsb.util.Parameters;
import net.runelite.client.rsb.wrappers.RSNPC;
import net.runelite.client.rsb.wrappers.RSPlayer;

import java.awt.*;
import java.util.HashMap;

import static java.lang.Thread.sleep;
@Slf4j
public class RuneLiteTestFeatures {

    static boolean onWelcomeScreen = true;
    static RSNPC enemy = null;
    static HashMap<String, Object> lastOutputs = new HashMap<>();
    static OutputObjectComparer test = null;

    public static void init(RuneLite bot) {
        while (true) {
            try {
                login(bot);
                testFeature(bot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void login(RuneLite bot) throws InterruptedException {
        if (bot.getClient().getLoginIndex() == 0) {
            sleep(3000);
            bot.getMethodContext().keyboard.sendText("\n", false);
            if (bot.getClient().getLoginIndex() == 2) {
                // use later bot.getClient().getCurrentLoginField()
                bot.getMethodContext().keyboard.sendText("SampleMarco.Reca@gmail.com", true);
                bot.getMethodContext().keyboard.sendText("xKFOfUnC", true);
                sleep(18000);
                //Authenticator
                if (bot.getClient().getLoginIndex() == 4) {

                    sleep(6000);
                }
            }
        }
        Widget welcomeScreenMotW = bot.getClient().getWidget(WidgetInfo.LOGIN_CLICK_TO_PLAY_SCREEN.getGroupId(), 6);
        if (welcomeScreenMotW != null) {
            if (welcomeScreenMotW.getTextColor() != -1) {
                Rectangle clickHereToPlayButton = new Rectangle(270, 295, 225, 80);
                bot.getMethodContext().mouse.move(new Point(clickHereToPlayButton.x, clickHereToPlayButton.y), clickHereToPlayButton.width, clickHereToPlayButton.height);
                bot.getMethodContext().mouse.click(true);
                sleep(8000);
            }
        }
    }

    private static void testFeature(RuneLite bot) throws InterruptedException {
        sleep(2000);
            if (bot.getClient() != null && bot.getClient().getLocalPlayer() != null) {
                if (enemy == null || (new RSPlayer(bot.getMethodContext(), bot.getClient().getLocalPlayer())).getInteracting() != enemy.getAccessor()) {


                    //enemy = bot.getMethodContext().npcs.getNearest("Chicken");
                    //bot.getMethodContext().game.logout();
                    //bot.getMethodContext().bank.close();
                    //log.debug("GetHealth: " + bot.getMethodContext().combat.getHealth());

                    if (enemy != null) {
                        if (!enemy.isInteractingWithLocalPlayer()) {
                            //  enemy.turnTo();
                            // enemy.doAction("Examine", "Chicken");
                        }
                    }
                }
            }
    }

    public static boolean conditional(Integer x) {
        return (x < 28 && x >= 0);

    }

}
