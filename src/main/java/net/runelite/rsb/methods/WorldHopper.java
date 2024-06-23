package net.runelite.rsb.methods;

import net.runelite.api.GameState;
import net.runelite.api.World;
import net.runelite.api.WorldType;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.http.api.worlds.WorldResult;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.wrappers.RSWidget;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class WorldHopper extends MethodProvider {
    WorldHopper(MethodContext ctx) {
        super(ctx);
    }

    public int getWorld() {
        return methods.client.getWorld();
    }

    public boolean isCurrentWorldMembers() {
        return methods.client.getWorldType().stream().anyMatch((worldType) -> worldType == WorldType.MEMBERS);
    }

    public boolean isWorldMenuOpen() {
        return methods.interfaces.isInterfaceSubstantiated(WidgetIndices.WorldSwitcher.GROUP_INDEX);
    }

    public boolean openWorldMenu() {
        if (isWorldMenuOpen()) {
            return true;
        }
        else {
            if (!methods.game.isOnLogoutTab()) {
                methods.game.openTab(InterfaceTab.LOGOUT);
                sleep(random(300, 600));
            }
            if (!isWorldMenuOpen()) {
                if (methods.interfaces.getComponent(GlobalWidgetInfo.WORLD_SWITCH_BUTTON).doClick()) {
                    sleepUntil(() -> methods.interfaces.getComponent(GlobalWidgetInfo.WORLD_SWITCH_DYNAMIC_CONTAINER).isVisible(),5000);
                    sleep(400);
                }
            }
            return isWorldMenuOpen();
        }
    }

    public boolean hop(int world) {
        if (methods.client.getWorld() == world) {
            return true;
        }
        if (isWorldMenuOpen() || openWorldMenu()) {
            Optional<RSWidget> worldWidget = Stream.of(methods.interfaces.getComponent(GlobalWidgetInfo.WORLD_SWITCH_DYNAMIC_CONTAINER).getComponents())
                    .filter(widget -> widget.getName().contains(String.valueOf(world)))
                    .findFirst();
            if (worldWidget.isPresent()) {
                if (methods.interfaces.scrollTo(worldWidget.get(), methods.interfaces.getComponent(GlobalWidgetInfo.WORLD_SWITCH_SCROLL_BAR))) {
                    if (worldWidget.get().doClick()) {
                        sleepUntil(() -> methods.game.getClientState() == GameState.HOPPING, 2000);
                        sleepUntil(() -> methods.game.getClientState() == GameState.LOGGED_IN, 15000);
                        if (methods.game.getClientState() == GameState.LOGGED_IN && methods.client.getWorld() == world) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public boolean hop(int worldID, boolean invoke) {
        if (methods.client.getWorld() == worldID) {
            return true;
        }
        if (!invoke) {
            return hop(worldID);
        }
        WorldResult worldResult = methods.runeLite.worldServiceProvider.getWorlds();
        // Don't try to hop if the world doesn't exist
        Optional<World> world = Arrays.stream(methods.client.getWorldList()).filter((x) -> x.getId() == worldID).findFirst();
        if (world.isPresent()) {
            hop(world.get());
            if (methods.game.getClientState() == GameState.LOGGED_IN && methods.client.getWorld() == worldID) {
                return true;
            }
        }
        return false;
    }

    private void hop(World world) {
        if (!isWorldMenuOpen()) {
            methods.client.openWorldHopper();
            sleepUntil(() -> isWorldMenuOpen(), 5000);
        }
        methods.client.hopToWorld(world);
        sleepUntil(() -> methods.game.getClientState() == GameState.HOPPING, 2000);
        sleepUntil(() -> methods.game.getClientState() == GameState.LOGGED_IN, 15000);
    }
}
