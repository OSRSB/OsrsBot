package net.runelite.rsb.script.adaptive.stats;

/**
 * Windowed circular buffer for tracking success rate over the last N actions.
 */
public class SuccessRateTracker {
    private final boolean[] buffer;
    private final int windowSize;
    private int head;
    private int count;
    private int successes;

    public SuccessRateTracker(int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("Window size must be positive: " + windowSize);
        }
        this.windowSize = windowSize;
        this.buffer = new boolean[windowSize];
        this.head = 0;
        this.count = 0;
        this.successes = 0;
    }

    public void record(boolean success) {
        if (count >= windowSize) {
            // Remove the oldest entry
            if (buffer[head]) {
                successes--;
            }
        } else {
            count++;
        }
        buffer[head] = success;
        if (success) {
            successes++;
        }
        head = (head + 1) % windowSize;
    }

    public double getSuccessRate() {
        return count > 0 ? (double) successes / count : 0.0;
    }

    public int getSuccessCount() {
        return successes;
    }

    public int getTotalCount() {
        return count;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void reset() {
        head = 0;
        count = 0;
        successes = 0;
    }
}
