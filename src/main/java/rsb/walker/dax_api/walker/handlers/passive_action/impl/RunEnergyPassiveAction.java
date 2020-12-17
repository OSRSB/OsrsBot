package rsb.walker.dax_api.walker.handlers.passive_action.impl;

import rsb.methods.Web;
import rsb.util.StdRandom;
import rsb.walker.dax_api.walker.handlers.passive_action.PassiveAction;
import rsb.walker.dax_api.walker.models.enums.ActionResult;

public class RunEnergyPassiveAction implements PassiveAction {

    private int random;

    public RunEnergyPassiveAction() {
        random = (int) StdRandom.gaussian(3, 20, 10, 3);
    }

    @Override
    public boolean shouldActivate() {
        return !Web.methods.walking.isRunEnabled() && Web.methods.game.getEnergy() > random;
    }

    @Override
    public ActionResult activate() {
        random = (int) StdRandom.gaussian(3, 20, 10, 3);
        return Web.methods.walking.setRun(true) ? ActionResult.CONTINUE : ActionResult.FAILURE;
    }

}
