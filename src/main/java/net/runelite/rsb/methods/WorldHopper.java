package net.runelite.rsb.methods;

import net.runelite.api.GameState;
import net.runelite.api.WorldType;
import net.runelite.rsb.internal.globval.GlobalWidgetInfo;
import net.runelite.rsb.internal.globval.WidgetIndices;
import net.runelite.rsb.internal.globval.enums.InterfaceTab;
import net.runelite.rsb.wrappers.RSWidget;

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
}
