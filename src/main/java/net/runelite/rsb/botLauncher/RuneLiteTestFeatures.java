package net.runelite.rsb.botLauncher;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.methods.MethodContext;
import net.runelite.rsb.util.OutputObjectComparer;
import net.runelite.rsb.wrappers.*;

import java.awt.*;
import java.util.HashMap;

import static java.lang.Thread.sleep;

@Slf4j
public class RuneLiteTestFeatures {

    static boolean onWelcomeScreen = true;
    static RSNPC enemy = null;
    static RSObject object = null;
    static HashMap<String, Object> lastOutputs = new HashMap<>();
    static OutputObjectComparer test = null;
    static int welcome = 0;

    private static void login(BotLite bot) throws InterruptedException {
        if (bot.getClient().getLoginIndex() == 0) {
            sleep(3000);
            bot.getMethodContext().keyboard.sendText("\n", false);
            if (bot.getClient().getLoginIndex() == 2) {
                // use later bot.getClient().getCurrentLoginField()
                bot.getMethodContext().keyboard.sendText("", true);
                bot.getMethodContext().keyboard.sendText("", true);
                sleep(18000);
                //Authenticator
                if (bot.getClient().getLoginIndex() == 4) {
                    sleep(6000);
                }
            }
        }
        Widget welcomeScreenMotW = bot.getClient().getWidget(GlobalWidgetInfo.LOGIN_MOTW.getPackedId());
        if (welcomeScreenMotW != null) {
            if (welcomeScreenMotW.getTextColor() != -1) {
                Rectangle clickHereToPlayButton = new Rectangle(270, 295, 225, 80);
                bot.getMethodContext().mouse.move(new Point(clickHereToPlayButton.x, clickHereToPlayButton.y), clickHereToPlayButton.width, clickHereToPlayButton.height);
                bot.getMethodContext().mouse.click(true);
                sleep(8000);
            }
        }
    }



    public static void testFeature(BotLite bot) {
        MethodContext ctx = bot.getMethodContext();
        if (bot.getMethodContext().client != null && bot.getMethodContext().client.getLocalPlayer() != null) {
            /*
            Code Here
            */
        }
    }

}
