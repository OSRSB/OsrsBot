package net.runelite.rsb.script.adaptive.data;

import net.runelite.rsb.script.adaptive.stats.RunningStats;

/**
 * Per-strategy accumulated stats with computed getters for XP/hr, success rate, etc.
 * Persisted across sessions via LearningDataStore.
 */
public class StrategyProfile {
    private String strategyKey;
    private long totalAttempts;
    private long totalSuccesses;
    private long totalXpGained;
    private long totalDurationMs;
    private double avgDuration;
    private double avgXpPerAction;
    private double bestXpPerHour;

    // Transient (not serialized) - rebuilt from persisted values
    private transient RunningStats durationStats;

    public StrategyProfile() {
        this.durationStats = new RunningStats();
    }

    public StrategyProfile(String strategyKey) {
        this.strategyKey = strategyKey;
        this.totalAttempts = 0;
        this.totalSuccesses = 0;
        this.totalXpGained = 0;
        this.totalDurationMs = 0;
        this.avgDuration = 0;
        this.avgXpPerAction = 0;
        this.bestXpPerHour = 0;
        this.durationStats = new RunningStats();
    }

    /**
     * Records a completed action into this strategy profile.
     */
    public void recordAction(ActionRecord record) {
        totalAttempts++;
        totalDurationMs += record.getDuration();
        if (record.isSuccess()) {
            totalSuccesses++;
            totalXpGained += record.getXpGained();
        }
        ensureDurationStats();
        durationStats.addValue(record.getDuration());

        // Update computed averages
        avgDuration = totalAttempts > 0 ? (double) totalDurationMs / totalAttempts : 0;
        avgXpPerAction = totalSuccesses > 0 ? (double) totalXpGained / totalSuccesses : 0;

        // Update best XP/hr if current rate is higher
        double currentXpPerHour = getXpPerHour();
        if (currentXpPerHour > bestXpPerHour) {
            bestXpPerHour = currentXpPerHour;
        }
    }

    public String getStrategyKey() {
        return strategyKey;
    }

    public long getTotalAttempts() {
        return totalAttempts;
    }

    public long getTotalSuccesses() {
        return totalSuccesses;
    }

    public long getTotalXpGained() {
        return totalXpGained;
    }

    public double getSuccessRate() {
        return totalAttempts > 0 ? (double) totalSuccesses / totalAttempts : 0.0;
    }

    public double getAvgDuration() {
        return avgDuration;
    }

    public double getAvgXpPerAction() {
        return avgXpPerAction;
    }

    /**
     * Computes XP/hr based on average duration and XP per successful action.
     */
    public double getXpPerHour() {
        if (avgDuration <= 0 || totalSuccesses == 0) return 0.0;
        double actionsPerHour = 3600000.0 / avgDuration;
        return actionsPerHour * getSuccessRate() * avgXpPerAction;
    }

    public double getBestXpPerHour() {
        return bestXpPerHour;
    }

    public RunningStats getDurationStats() {
        ensureDurationStats();
        return durationStats;
    }

    private void ensureDurationStats() {
        if (durationStats == null) {
            durationStats = new RunningStats();
        }
    }
}
