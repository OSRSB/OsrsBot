package net.runelite.rsb.script.adaptive.stats;

import java.util.Arrays;

/**
 * Sorted-window percentile lookup using a circular buffer.
 * Keeps the last N values and can compute any percentile on demand.
 */
public class PercentileEstimator {
    private final double[] buffer;
    private final int windowSize;
    private int head;
    private int count;

    public PercentileEstimator(int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("Window size must be positive: " + windowSize);
        }
        this.windowSize = windowSize;
        this.buffer = new double[windowSize];
        this.head = 0;
        this.count = 0;
    }

    public void addValue(double value) {
        buffer[head] = value;
        head = (head + 1) % windowSize;
        if (count < windowSize) {
            count++;
        }
    }

    /**
     * Returns the value at the given percentile (0-100).
     * Uses nearest-rank method.
     */
    public double getPercentile(double percentile) {
        if (count == 0) return 0.0;
        if (percentile < 0 || percentile > 100) {
            throw new IllegalArgumentException("Percentile must be in [0, 100]: " + percentile);
        }

        double[] sorted = getSortedValues();
        int rank = (int) Math.ceil(percentile / 100.0 * sorted.length) - 1;
        rank = Math.max(0, Math.min(rank, sorted.length - 1));
        return sorted[rank];
    }

    public double getMedian() {
        return getPercentile(50);
    }

    public int getCount() {
        return count;
    }

    private double[] getSortedValues() {
        double[] values = new double[count];
        for (int i = 0; i < count; i++) {
            // Read from the buffer, handling wrap-around
            int idx = (head - count + i + windowSize) % windowSize;
            values[i] = buffer[idx];
        }
        Arrays.sort(values);
        return values;
    }

    public void reset() {
        head = 0;
        count = 0;
    }
}
