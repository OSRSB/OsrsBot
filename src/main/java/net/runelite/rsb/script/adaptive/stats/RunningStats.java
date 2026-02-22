package net.runelite.rsb.script.adaptive.stats;

/**
 * Welford's online algorithm for computing running mean, variance, min, and max
 * in a single pass with O(1) memory.
 */
public class RunningStats {
    private long count;
    private double mean;
    private double m2;
    private double min;
    private double max;

    public RunningStats() {
        this.count = 0;
        this.mean = 0.0;
        this.m2 = 0.0;
        this.min = Double.MAX_VALUE;
        this.max = Double.MIN_VALUE;
    }

    public void addValue(double value) {
        count++;
        double delta = value - mean;
        mean += delta / count;
        double delta2 = value - mean;
        m2 += delta * delta2;

        if (value < min) min = value;
        if (value > max) max = value;
    }

    public long getCount() {
        return count;
    }

    public double getMean() {
        return count > 0 ? mean : 0.0;
    }

    public double getVariance() {
        return count > 1 ? m2 / (count - 1) : 0.0;
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double getMin() {
        return count > 0 ? min : 0.0;
    }

    public double getMax() {
        return count > 0 ? max : 0.0;
    }

    /**
     * Merges another RunningStats into this one (for combining session data).
     */
    public void merge(RunningStats other) {
        if (other.count == 0) return;
        if (this.count == 0) {
            this.count = other.count;
            this.mean = other.mean;
            this.m2 = other.m2;
            this.min = other.min;
            this.max = other.max;
            return;
        }
        long combinedCount = this.count + other.count;
        double delta = other.mean - this.mean;
        double combinedMean = this.mean + delta * other.count / combinedCount;
        double combinedM2 = this.m2 + other.m2 + delta * delta * this.count * other.count / combinedCount;

        this.count = combinedCount;
        this.mean = combinedMean;
        this.m2 = combinedM2;
        if (other.min < this.min) this.min = other.min;
        if (other.max > this.max) this.max = other.max;
    }

    public void reset() {
        count = 0;
        mean = 0.0;
        m2 = 0.0;
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
    }
}
