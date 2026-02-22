package net.runelite.rsb.script.adaptive.stats;

/**
 * Exponential Moving Average with configurable smoothing factor (alpha).
 * Higher alpha = more weight on recent values.
 */
public class ExponentialMovingAverage {
    private final double alpha;
    private double value;
    private long count;

    public ExponentialMovingAverage(double alpha) {
        if (alpha <= 0.0 || alpha > 1.0) {
            throw new IllegalArgumentException("Alpha must be in (0, 1]: " + alpha);
        }
        this.alpha = alpha;
        this.value = 0.0;
        this.count = 0;
    }

    public void addValue(double sample) {
        if (count == 0) {
            value = sample;
        } else {
            value = alpha * sample + (1.0 - alpha) * value;
        }
        count++;
    }

    public double getValue() {
        return value;
    }

    public long getCount() {
        return count;
    }

    public double getAlpha() {
        return alpha;
    }

    public void reset() {
        value = 0.0;
        count = 0;
    }

    /**
     * Initializes the EMA with a pre-existing value and count (for persistence).
     */
    public void initialize(double value, long count) {
        this.value = value;
        this.count = count;
    }
}
