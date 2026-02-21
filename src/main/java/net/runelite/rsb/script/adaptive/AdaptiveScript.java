package net.runelite.rsb.script.adaptive;

import lombok.extern.slf4j.Slf4j;
import net.runelite.rsb.event.events.MessageEvent;
import net.runelite.rsb.event.listener.MessageListener;
import net.runelite.rsb.event.listener.PaintListener;
import net.runelite.rsb.script.Script;
import net.runelite.rsb.script.ScriptManifest;

import java.awt.*;

/**
 * Base class for adaptive scripts. Extends Script, implements PaintListener and MessageListener.
 * Scripts opt in to the adaptive system by extending this class instead of Script.
 *
 * Provides convenience methods:
 * - adaptiveSleep(context, min, max): generates optimized sleep durations
 * - beginAction(type, targetId, name): starts tracking an action
 * - endAction(handle, success, xpGained): completes action tracking
 *
 * Lifecycle:
 * - onStart() loads persisted learning data
 * - onFinish() saves learning data
 * - messageReceived() delegates to ChatReactor
 */
@Slf4j
public abstract class AdaptiveScript extends Script implements PaintListener, MessageListener {

    protected AdaptiveContext adaptive;

    /**
     * Initializes the adaptive context and loads persisted data.
     * Subclasses should call super.onStart() first.
     */
    @Override
    public boolean onStart() {
        String scriptName = getScriptName();
        String accountName = getAccountName();
        adaptive = new AdaptiveContext(scriptName, accountName);
        log.info("AdaptiveScript started: {}", scriptName);
        return true;
    }

    /**
     * Saves learning data on script finish.
     * Subclasses should call super.onFinish() to ensure data is persisted.
     */
    @Override
    public void onFinish() {
        if (adaptive != null) {
            adaptive.save();
            log.info("AdaptiveScript finished, data saved.");
        }
    }

    /**
     * Delegates message events to the ChatReactor.
     */
    @Override
    public void messageReceived(MessageEvent e) {
        if (adaptive != null) {
            adaptive.getChat().onMessage(e);
        }
    }

    /**
     * Renders the adaptive paint overlay.
     */
    @Override
    public void onRepaint(Graphics render) {
        if (adaptive != null) {
            adaptive.getPaint().render(render);
        }
    }

    // ---- Convenience methods ----

    /**
     * Returns an adaptive sleep duration based on learned timing data.
     * Records the sleep for correlation tracking.
     *
     * @param context Label for this sleep context (e.g., "between_chops")
     * @param minMs   Minimum sleep in milliseconds
     * @param maxMs   Maximum sleep in milliseconds
     * @return Sleep duration in milliseconds
     */
    protected int adaptiveSleep(String context, int minMs, int maxMs) {
        int duration = adaptive.getSleep().getSleepDuration(context, minMs, maxMs);
        adaptive.getTracker().recordSleep(duration);
        return duration;
    }

    /**
     * Begins tracking a new action.
     *
     * @param actionType  Type of action (e.g., "chop_tree", "mine_rock")
     * @param targetId    Game object/NPC ID being targeted
     * @param targetName  Display name of the target
     * @return ActionHandle to pass to endAction()
     */
    protected ActionTracker.ActionHandle beginAction(String actionType, int targetId, String targetName) {
        return adaptive.getTracker().beginAction(actionType, targetId, targetName);
    }

    /**
     * Ends a tracked action and records the result.
     * If the action was successful with the preceding sleep, feeds that into sleep optimization.
     *
     * @param handle    Handle from beginAction()
     * @param success   Whether the action succeeded
     * @param xpGained  XP gained from this action (0 if failed)
     */
    protected void endAction(ActionTracker.ActionHandle handle, boolean success, int xpGained) {
        adaptive.getTracker().endAction(handle, success, xpGained);
        if (success) {
            // Feed successful action timing back to sleep optimizer
            adaptive.getSleep().recordSuccess(handle.getActionType(), (int) (handle.getStartTime() - System.currentTimeMillis()));
        }
    }

    /**
     * Gets the script name from the ScriptManifest annotation, or class name as fallback.
     */
    private String getScriptName() {
        ScriptManifest manifest = getClass().getAnnotation(ScriptManifest.class);
        if (manifest != null) {
            return manifest.name();
        }
        return getClass().getSimpleName();
    }

    /**
     * Gets the current account name, or "default" if unavailable.
     */
    private String getAccountName() {
        try {
            String name = account.getName();
            return name != null && !name.isEmpty() ? name : "default";
        } catch (Exception e) {
            return "default";
        }
    }
}
