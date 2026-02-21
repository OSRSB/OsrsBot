package net.runelite.rsb.script.adaptive.data;

/**
 * POJO representing aggregated session statistics.
 */
public class SessionSummary {
    private long startTime;
    private long endTime;
    private int totalActions;
    private int successfulActions;
    private int totalXpGained;
    private double actionsPerHour;
    private double xpPerHour;
    private double avgActionDuration;
    private double successRate;

    public SessionSummary() {
    }

    public SessionSummary(long startTime, long endTime, int totalActions,
                          int successfulActions, int totalXpGained) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalActions = totalActions;
        this.successfulActions = successfulActions;
        this.totalXpGained = totalXpGained;
        computeRates();
    }

    private void computeRates() {
        long durationMs = endTime - startTime;
        double hours = durationMs / 3600000.0;
        this.actionsPerHour = hours > 0 ? totalActions / hours : 0;
        this.xpPerHour = hours > 0 ? totalXpGained / hours : 0;
        this.successRate = totalActions > 0 ? (double) successfulActions / totalActions : 0;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
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

    public double getActionsPerHour() {
        return actionsPerHour;
    }

    public double getXpPerHour() {
        return xpPerHour;
    }

    public double getAvgActionDuration() {
        return avgActionDuration;
    }

    public void setAvgActionDuration(double avgActionDuration) {
        this.avgActionDuration = avgActionDuration;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public long getDurationMs() {
        return endTime - startTime;
    }
}
