package rsb.walker.dax_api.walker.handlers.move_task;

import rsb.methods.Web;
import rsb.util.Timer;
import rsb.walker.dax_api.walker.handlers.passive_action.PassiveAction;
import rsb.walker.dax_api.walker.models.MoveTask;
import rsb.walker.dax_api.walker.models.WaitCondition;
import rsb.walker.dax_api.walker.models.enums.ActionResult;
import rsb.walker.dax_api.walker.models.enums.MoveActionResult;
import rsb.walker.dax_api.walker_engine.WaitFor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public interface MoveTaskHandler {

    MoveActionResult handle(MoveTask moveTask, List<PassiveAction> passiveActionList);

    /**
     * @param waitCondition
     * @param timeout
     * @param passiveActionList
     * @return If player stops moving, return fail condition.
     */
    default ActionResult waitForConditionOrNoMovement(WaitCondition waitCondition, long timeout,
                                                      List<PassiveAction> passiveActionList) {
        if (passiveActionList == null) passiveActionList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        AtomicLong lastMoved = new AtomicLong(System.currentTimeMillis());
        passiveActionList.add(() -> {
            if (Web.methods.players.getMyPlayer().isLocalPlayerMoving() || Timer.timeFromMark(startTime) < 1750) {
                lastMoved.set(System.currentTimeMillis());
                return ActionResult.CONTINUE;
            }

            if (Timer.timeFromMark(lastMoved.get()) > 1250) return ActionResult.FAILURE;
            return ActionResult.CONTINUE;
        });
        return waitFor(waitCondition, timeout, passiveActionList);
    }

    default ActionResult waitFor(WaitCondition waitCondition, long timeout, List<PassiveAction> passiveActionList) {
        long end = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < end) {
            if (waitCondition.action()) return ActionResult.CONTINUE;

            for (PassiveAction passiveAction : passiveActionList) {
                if (!passiveAction.shouldActivate()) continue;

                ActionResult actionResult = passiveAction.activate();
                switch (actionResult) {
                    case EXIT_SUCCESS:
                    case EXIT_FAILURE:
                        return actionResult;
                }

            }
            WaitFor.milliseconds(80, 150);
        }
        return ActionResult.FAILURE;
    }

}
