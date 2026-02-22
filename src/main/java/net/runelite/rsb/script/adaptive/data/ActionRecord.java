package net.runelite.rsb.script.adaptive.data;

/**
 * POJO representing a single recorded action.
 */
public class ActionRecord {
    private final String actionType;
    private final int targetId;
    private final String targetName;
    private final long startTime;
    private final long endTime;
    private final long duration;
    private final boolean success;
    private final int xpGained;
    private final int sleepBefore;

    public ActionRecord(String actionType, int targetId, String targetName,
                        long startTime, long endTime, boolean success,
                        int xpGained, int sleepBefore) {
        this.actionType = actionType;
        this.targetId = targetId;
        this.targetName = targetName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime - startTime;
        this.success = success;
        this.xpGained = xpGained;
        this.sleepBefore = sleepBefore;
    }

    public String getActionType() {
        return actionType;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getXpGained() {
        return xpGained;
    }

    public int getSleepBefore() {
        return sleepBefore;
    }

    /**
     * Returns the strategy key for this action: "actionType:targetId"
     */
    public String getStrategyKey() {
        return actionType + ":" + targetId;
    }
}
