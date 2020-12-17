package rsb.walker.dax_api.walker.handlers.passive_action;

import rsb.walker.dax_api.walker.models.enums.ActionResult;

public interface PassiveAction {

    default boolean shouldActivate() {
        return true;
    }

    ActionResult activate();

}
