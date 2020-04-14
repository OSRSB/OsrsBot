package net.runelite.client.rsb.botLauncher;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.rsb.internal.input.Mouse;
import net.runelite.client.rsb.methods.Inventory;
import net.runelite.client.rsb.methods.Menu;
import net.runelite.client.rsb.util.OutputObjectComparer;
import net.runelite.client.rsb.util.Parameters;
import net.runelite.client.rsb.wrappers.*;
import net.runelite.client.ui.FontManager;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.LineMetrics;
import java.util.HashMap;

import static java.lang.Thread.sleep;
import static java.lang.Thread.yield;
import static net.runelite.client.rsb.methods.Game.CHAT_OPTION;

@Slf4j
public class RuneLiteTestFeatures {

    static boolean onWelcomeScreen = true;
    static RSNPC enemy = null;
    static RSObject object = null;
    static HashMap<String, Object> lastOutputs = new HashMap<>();
    static OutputObjectComparer test = null;
    static int welcome = 0;

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
                bot.getMethodContext().keyboard.sendText("", true);
                bot.getMethodContext().keyboard.sendText("", true);
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

    static RSArea logbalanceStart = new RSArea(new RSTile(2471, 3436, 0), new RSTile(2478, 3438, 0));
    public static void testFeature(RuneLite bot) throws InterruptedException {
        sleep(5000);
        if (bot.getMethodContext().client != null && bot.getMethodContext().client.getLocalPlayer() != null) {
            if (logbalanceStart.contains(bot.getMethodContext().players.getMyPlayer().getLocation().getWorldLocation().getX(),
                    bot.getMethodContext().players.getMyPlayer().getLocation().getWorldLocation().getY())) {
                System.out.println("Log Balance Area");
            }
        }
    }

    public static boolean conditional(Integer x) {
        return (x < 1000 && x >= -6);

    }

}
