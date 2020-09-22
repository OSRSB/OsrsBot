package net.runelite.client.rsb.botLauncher;

import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.rsb.methods.Game;
import net.runelite.client.rsb.methods.GroundItems;
import net.runelite.client.rsb.methods.MethodContext;
import net.runelite.client.rsb.testsScript.Test;
import net.runelite.client.rsb.util.OutputObjectComparer;
import net.runelite.client.rsb.util.Parameters;
import net.runelite.client.rsb.util.StdRandom;
import net.runelite.client.rsb.wrappers.*;
import net.runelite.client.rsb.wrappers.subwrap.WalkerTile;

import java.awt.*;
import java.lang.reflect.Field;
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

    public static void init(RuneLite bot) {
        while (true) {
            try {
                login(bot);
                //testFeature(bot);
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




    public static String[] itemNames = {"Guam leaf", "Vial of water"};
    public static int[] itemIDs = {0, 0};

    private static boolean maybe = true;

    public static void testFeature(RuneLite bot, boolean startUp) {
        MethodContext ctx = bot.getMethodContext();
        if (bot.getMethodContext().client != null && bot.getMethodContext().client.getLocalPlayer() != null) {
            if (itemIDs[0] == 0) {
                nap(StdRandom.uniform(500, 700));
                start(ctx);
            }
            if (itemIDs[0] != 0) {
                withdrawItems(ctx);
                ctx.bank.close();
                nap(StdRandom.uniform(100, 300));
                if (!(ctx.game.getCurrentTab() == Game.TAB_INVENTORY)) {
                    ctx.game.openTab(Game.TAB_INVENTORY);
                }
                ctx.inventory.useItem(itemIDs[0], itemIDs[1]);
                nap(StdRandom.uniform(800, 1300));
                ctx.interfaces.makeX(-1);
                nap(StdRandom.uniform(14000,16000));
                ctx.bank.open();
                nap(StdRandom.uniform(830, 1000));
                ctx.bank.depositAll();
            }
        }
    }

    public static void nap(long time) {
        try {
            sleep(time);
        } catch (Exception e) {
            log.debug("Error", e);
        }
    }

    public static void withdrawItems(MethodContext ctx) {
        ctx.bank.open();
        RSWidget widget = new RSWidget(ctx, ctx.client.getWidget(402, 0));
        if (widget.isVisible() && widget.isSelfVisible()) {
            closeUnrelatedInterface(ctx);
            ctx.bank.open();
        }
        try {
            sleep(StdRandom.uniform(500, 1000));
        } catch (Exception e) {
            log.debug("Error", e);
        }
        ctx.bank.withdraw(itemIDs[0], 14);
        ctx.bank.withdraw(itemIDs[1], 14);
    }

    public static void closeUnrelatedInterface(MethodContext ctx) {
        RSWidget widget = new RSWidget(ctx, ctx.client.getWidget(402, 2));
        widget.getDynamicComponent(11).doClick();
    }

    public static void start(MethodContext ctx) {
        ctx.bank.open();
        while (ctx.bank.isOpen()) {
            for (int i = 0; i < itemNames.length; i++) {
                if (itemIDs[i] > -1) {
                    itemIDs[i] = ctx.bank.getItemID(itemNames[i]);
                }
            }
            ctx.bank.close();
        }
        if (itemIDs[0] == -1) {
            for (int i = 0; i < itemNames.length; i++) {
                ctx.grandExchange.buy(itemNames[i], 13000, 10, true);
                ctx.grandExchange.collect(true);
            }
            ctx.grandExchange.close();
            ctx.bank.open();
            ctx.bank.depositAll();
        }
    }


}
