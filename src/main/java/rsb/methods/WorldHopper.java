package rsb.methods;

import net.runelite.api.WorldType;

import java.util.function.Predicate;

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

}
