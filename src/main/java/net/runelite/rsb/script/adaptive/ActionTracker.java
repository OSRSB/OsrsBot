package net.runelite.rsb.script.adaptive;

import net.runelite.rsb.script.adaptive.data.ActionRecord;
import net.runelite.rsb.script.adaptive.data.SessionSummary;
import net.runelite.rsb.script.adaptive.data.StrategyProfile;
import net.runelite.rsb.script.adaptive.stats.RunningStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Records actions, computes live XP/hr and actions/hr.
 * Manages ActionHandle instances for begin/end tracking.
 */
public class ActionTracker {
    private final Map<String, StrategyProfile> strategies;
    private final List<ActionRecord> sessionActions;
    private final RunningStats durationStats;
    private final long sessionStartTime;

    private int totalXpGained;
    private int totalActions;
    private int successfulActions;
    private int lastSleepDuration;

    public ActionTracker(Map<String, StrategyProfile> strategies) {
        this.strategies = strategies;
        this.sessionActions = new ArrayList<>();
        this.durationStats = new RunningStats();
        this.sessionStartTime = System.currentTimeMillis();
        this.totalXpGained = 0;
        this.totalActions = 0;
        this.successfulActions = 0;
        this.lastSleepDuration = 0;
    }

    /**
     * Begins tracking a new action. Returns a handle to be passed to endAction().
     */
    public ActionHandle beginAction(String actionType, int targetId, String targetName) {
        return new ActionHandle(actionType, targetId, targetName, System.currentTimeMillis(), lastSleepDuration);
    }

    /**
     * Ends a tracked action and records its result.
     */
    public ActionRecord endAction(ActionHandle handle, boolean success, int xpGained) {
        long endTime = System.currentTimeMillis();
        ActionRecord record = new ActionRecord(
                handle.actionType, handle.targetId, handle.targetName,
                handle.startTime, endTime, success, xpGained, handle.sleepBefore);

        sessionActions.add(record);
        durationStats.addValue(record.getDuration());
        totalActions++;
        if (success) {
            successfulActions++;
            totalXpGained += xpGained;
        }

        // Update strategy profile
        String key = record.getStrategyKey();
        StrategyProfile profile = strategies.computeIfAbsent(key, StrategyProfile::new);
        profile.recordAction(record);

        return record;
    }

    /**
     * Records the last sleep duration for correlation with action success.
     */
    public void recordSleep(int sleepMs) {
        this.lastSleepDuration = sleepMs;
    }

    /**
     * Returns live XP per hour based on session elapsed time.
     */
    public double getLiveXpPerHour() {
        long elapsed = System.currentTimeMillis() - sessionStartTime;
        if (elapsed <= 0) return 0;
        return totalXpGained * 3600000.0 / elapsed;
    }

    /**
     * Returns live actions per hour based on session elapsed time.
     */
    public double getLiveActionsPerHour() {
        long elapsed = System.currentTimeMillis() - sessionStartTime;
        if (elapsed <= 0) return 0;
        return totalActions * 3600000.0 / elapsed;
    }

    public double getLiveSuccessRate() {
        return totalActions > 0 ? (double) successfulActions / totalActions : 0;
    }

    public int getTotalActions() {
        return totalActions;
    }

    public int getSuccessfulActions() {
        return successfulActions;
    }

    public int getTotalXpGained() {
        return totalXpGained;
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public long getSessionElapsedMs() {
        return System.currentTimeMillis() - sessionStartTime;
    }

    public RunningStats getDurationStats() {
        return durationStats;
    }

    public List<ActionRecord> getSessionActions() {
        return sessionActions;
    }

    /**
     * Builds a SessionSummary from the current session data.
     */
    public SessionSummary buildSessionSummary() {
        SessionSummary summary = new SessionSummary(
                sessionStartTime, System.currentTimeMillis(),
                totalActions, successfulActions, totalXpGained);
        summary.setAvgActionDuration(durationStats.getMean());
        return summary;
    }

    /**
     * Handle returned by beginAction(), passed to endAction() to complete tracking.
     */
    public static class ActionHandle {
        final String actionType;
        final int targetId;
        final String targetName;
        final long startTime;
        final int sleepBefore;

        ActionHandle(String actionType, int targetId, String targetName,
                     long startTime, int sleepBefore) {
            this.actionType = actionType;
            this.targetId = targetId;
            this.targetName = targetName;
            this.startTime = startTime;
            this.sleepBefore = sleepBefore;
        }

        public String getActionType() {
            return actionType;
        }

        public int getTargetId() {
            return targetId;
        }

        public long getStartTime() {
            return startTime;
        }
    }
}
