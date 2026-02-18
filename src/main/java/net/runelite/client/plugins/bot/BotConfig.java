package net.runelite.client.plugins.bot;

import ch.qos.logback.classic.Level;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("bot")
public interface BotConfig extends Config {
    @ConfigItem(
            keyName = "bot",
            name = "OSRSBot",
            description = "Fuck Adam",
            position = 0
    )
    default boolean bot() {
        return true;
    }

    @ConfigItem(
            keyName = "debugLogLevel",
            name = "Debug Log Level",
            description = "Set the log level of bot",
            hidden = true,
            position = 1
    )
    default String debugLogLevel() {
        return "INFO";
    }

    @ConfigItem(
            keyName = "debugDrawMouse",
            name = "Draw Mouse",
            description = "Draw the mouse on script start",
            hidden = true,
            position = 2
    )
    default boolean debugDrawMouse() {
        return false;
    }

    @ConfigItem(
            keyName = "debugDrawMouseTrail",
            name = "Draw Mouse Trail",
            description = "Draw the mouse trail on script start",
            hidden = true,
            position = 3
    )
    default boolean debugDrawMouseTrail() {
        return false;
    }

    @ConfigItem(
            keyName = "debugEnableMouse",
            name = "Enable Mouse",
            description = "Enable the mouse on script start",
            hidden = true,
            position = 4
    )
    default boolean debugEnableMouse() {
        return true;
    }

    @ConfigItem(
            keyName = "debugDrawBoundaries",
            name = "Draw Boundaries",
            description = "Draw boundaries",
            hidden = true,
            position = 5
    )
    default boolean debugDrawBoundaries() {
        return false;
    }

    @ConfigItem(
            keyName = "debugDrawGround",
            name = "Draw Ground",
            description = "Draw ground",
            hidden = true,
            position = 6
    )
    default boolean debugDrawGround() {
        return false;
    }

    @ConfigItem(
            keyName = "debugDrawInventory",
            name = "Draw Inventory",
            description = "Draw inventory",
            hidden = true,
            position = 7
    )
    default boolean debugDrawInventory() {
        return false;
    }

    @ConfigItem(
            keyName = "debugDrawNPCs",
            name = "Draw NPCs",
            description = "Draw NPCs",
            hidden = true,
            position = 8
    )
    default boolean debugDrawNPCs() {
        return false;
    }

    @ConfigItem(
            keyName = "debugDrawObjects",
            name = "Draw Objects",
            description = "Draw objects",
            hidden = true,
            position = 9
    )
    default boolean debugDrawObjects() {
        return false;
    }

    @ConfigItem(
            keyName = "debugDrawPlayers",
            name = "Draw Players",
            description = "Draw players",
            hidden = true,
            position = 10
    )
    default boolean debugDrawPlayers() {
        return false;
    }

    @ConfigItem(
            keyName = "debugDrawSettings",
            name = "Draw Settings",
            description = "Draw settings",
            hidden = true,
            position = 11
    )
    default boolean debugDrawSettings() {
        return false;
    }

    @ConfigItem(
            keyName = "debugDrawWeb",
            name = "Draw Web",
            description = "Draw web",
            hidden = true,
            position = 12
    )
    default boolean debugDrawWeb() {
        return false;
    }

}
