package net.runelite.rsb.script.randoms;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.plugin.AccountManager;
import net.runelite.rsb.script.Random;
import net.runelite.rsb.script.ScriptManifest;
import net.runelite.rsb.wrappers.RSWidget;

import java.awt.*;

/**
 * @author Gigiaj
 */
@ScriptManifest(authors = {"GigiaJ"}, name = "Login", version = 1.0)
@Slf4j
public class LoginBot extends Random {
    //TODO: this should be elsewhere
    private static final int INTERFACE_MAIN = 905;
    private static final int INTERFACE_MAIN_CHILD = 59;
    private static final int INTERFACE_MAIN_CHILD_COMPONENT_ID = 4;
    private static final int INTERFACE_LOGIN_SCREEN = 596;
    private static final int INTERFACE_USERNAME = 65;
    private static final int INTERFACE_USERNAME_WINDOW = 37;
    private static final int INTERFACE_PASSWORD = 71;
    private static final int INTERFACE_PASSWORD_WINDOW = 39;
    private static final int INTERFACE_BUTTON_LOGIN = 42;
    private static final int INTERFACE_TEXT_RETURN = 11;
    private static final int INTERFACE_BUTTON_BACK = 60;
    private static final int INTERFACE_WELCOME_SCREEN = 906;
    private static final int INTERFACE_WELCOME_SCREEN_BUTTON_PLAY_1 = 160;
    private static final int INTERFACE_WELCOME_SCREEN_BUTTON_PLAY_2 = 171;
    //private static final int INTERFACE_WELCOME_SCREEN_BUTTON_LOGOUT = 193;
    private static final int INTERFACE_WELCOME_SCREEN_TEXT_RETURN = 221;
    private static final int INTERFACE_WELCOME_SCREEN_BUTTON_BACK = 218;
    private static final int INTERFACE_WELCOME_SCREEN_HIGH_RISK_WORLD_TEXT = 86;
    private static final int INTERFACE_WELCOME_SCREEN_HIGH_RISK_WORLD_LOGIN_BUTTON = 93;
    private static final int INTERFACE_GRAPHICS_NOTICE = 976;
    private static final int INTERFACE_GRAPHICS_LEAVE_ALONE = 6;
    private static final int INDEX_LOGGED_OUT = 3;
    private static final int INDEX_LOBBY = 7;

    private int invalidCount, worldFullCount;
    public Random Rand;

    @Override
    public boolean activateCondition() {
        GameState idx = game.getClientState();
        return ((ctx.menu.getIndex("Play") == 0 || (idx == GameState.LOGIN_SCREEN || idx == GameState.LOGGING_IN)) && account.getName() != null)
                || (idx == GameState.LOGGED_IN && ctx.client.getWidget(GlobalWidgetInfo.LOGIN_MOTW_TEXT.getPackedId()) != null);
    }

    @Override
    public int loop() {
        if (game.getClientState() != GameState.LOGGED_IN) {
            try {
                switch (ctx.client.getLoginIndex()) {
                    case 0:
                        ctx.keyboard.sendText("\n", false);
                        return random(1000, 5000);
                    case 1:
                        log.info("State 1");
                        return 10000;
                    case 2:
                        switch (ctx.client.getCurrentLoginField()) {
                            case 0 -> {
                                ctx.keyboard.sendText(account.getName().toLowerCase().trim(), true);
                                return random(1000, 5000);
                            }
                            case 1 -> {
                                ctx.keyboard.sendText(AccountManager.getPassword(account.getName()), true);
                                return random(5000, 10000);
                            }
                            default -> {
                                throw new Exception("Unknown login state");
                            }
                        }
                    case 3:
                        log.info("State 3");
                        return 10000;
                    case 4:
                        log.info("Auth");
                        return 10000;
                }
            } catch (Exception e) {
                log.error("Login failed. Try again.");
            }
        }
        if (game.getClientState() == GameState.LOGGED_IN) {
            RSWidget clickToPlay = ctx.interfaces.getComponent(GlobalWidgetInfo.LOGIN_CLICK_TO_PLAY);
            if (clickToPlay != null && clickToPlay.doClick()) {
                return random(800, 2000);
            }
        }
        return -1;
    }

    private boolean switchingWorlds() {
        return interfaces.get(INTERFACE_WELCOME_SCREEN)
                .getComponent(INTERFACE_WELCOME_SCREEN_TEXT_RETURN).isValid()
                && interfaces.get(INTERFACE_WELCOME_SCREEN)
                .getComponent(INTERFACE_WELCOME_SCREEN_TEXT_RETURN)
                .containsText("just left another world");
    }

    // Clicks past all of the letters
    private boolean atLoginInterface(RSWidget i) {
        if (!i.isValid()) {
            return false;
        }
        Rectangle pos = i.getArea();
        if (pos.x == -1 || pos.y == -1 || pos.width == -1 || pos.height == -1) {
            return false;
        }
        int dy = (int) (pos.getHeight() - 4) / 2;
        int maxRandomX = (int) (pos.getMaxX() - pos.getCenterX());
        int midx = (int) (pos.getCenterX());
        int midy = (int) (pos.getMinY() + pos.getHeight() / 2);
        if (i.getIndex() == INTERFACE_PASSWORD_WINDOW) {
            mouse.click(minX(i), midy + random(-dy, dy), true);
        } else {
            mouse.click(midx + random(1, maxRandomX), midy + random(-dy, dy),
                    true);
        }
        return true;
    }

    /*
     * Returns x int based on the letters in a Child Only the password text is
     * needed as the username text cannot reach past the middle of the interface
     */
    private int minX(RSWidget a) {
        int x = 0;
        Rectangle pos = a.getArea();
        int dx = (int) (pos.getWidth() - 4) / 2;
        int midx = (int) (pos.getMinX() + pos.getWidth() / 2);
        if (pos.x == -1 || pos.y == -1 || pos.width == -1 || pos.height == -1) {
            return 0;
        }
        for (int i = 0; i < interfaces.get(INTERFACE_LOGIN_SCREEN)
                .getComponent(INTERFACE_PASSWORD).getText().length(); i++) {
            x += 11;
        }
        if (x > 44) {
            return (int) (pos.getMinX() + x + 15);
        } else {
            return midx + random(-dx, dx);
        }
    }

    private boolean atLoginScreen() {
        return interfaces.get(596).isValid();
    }

    private boolean isUsernameFilled() {
        String username = account.getName().toLowerCase().trim();
        return interfaces.get(INTERFACE_LOGIN_SCREEN)
                .getComponent(INTERFACE_USERNAME).getText().toLowerCase()
                .equalsIgnoreCase(username);
    }

    private boolean isPasswordFilled() {
        String passWord = AccountManager.getPassword(account.getName());
        return interfaces.get(INTERFACE_LOGIN_SCREEN)
                .getComponent(INTERFACE_PASSWORD).getText().toLowerCase()
                .length() == (passWord == null ? 0 : passWord.length());
    }
}
