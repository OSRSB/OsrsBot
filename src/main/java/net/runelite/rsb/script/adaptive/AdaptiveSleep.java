package net.runelite.rsb.script.adaptive;

import net.runelite.rsb.script.adaptive.stats.ExponentialMovingAverage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * EMA-based delay optimization with Gaussian sampling.
 * Tracks delays that preceded successful actions and learns optimal timing.
 * Variance shrinks as sample count grows (wide exploration → tight optimization).
 */
public class AdaptiveSleep {
    private static final double EMA_ALPHA = 0.1;
    private static final int MIN_SAMPLES_FOR_ADAPTIVE = 10;

    private final Map<String, ExponentialMovingAverage> sleepEmas;
    private final Map<String, Long> sampleCounts;
    private final Random random;

    public AdaptiveSleep() {
        this.sleepEmas = new HashMap<>();
        this.sampleCounts = new HashMap<>();
        this.random = new Random();
    }

    /**
     * Records a sleep duration that preceded a successful action.
     */
    public void recordSuccess(String context, int sleepMs) {
        ExponentialMovingAverage ema = sleepEmas.computeIfAbsent(context,
                k -> new ExponentialMovingAverage(EMA_ALPHA));
        ema.addValue(sleepMs);
        sampleCounts.merge(context, 1L, Long::sum);
    }

    /**
     * Generates an adaptive sleep duration.
     * With few samples, uses uniform random between min and max.
     * With enough data, uses Gaussian centered on learned optimal with shrinking variance.
     *
     * @param context  A label for the sleep context (e.g., "between_chops", "post_drop")
     * @param minMs    Minimum sleep in milliseconds
     * @param maxMs    Maximum sleep in milliseconds
     * @return Sleep duration in milliseconds
     */
    public int getSleepDuration(String context, int minMs, int maxMs) {
        long samples = sampleCounts.getOrDefault(context, 0L);
        ExponentialMovingAverage ema = sleepEmas.get(context);

        if (samples < MIN_SAMPLES_FOR_ADAPTIVE || ema == null) {
            // Not enough data: uniform random in [min, max]
            return minMs + random.nextInt(Math.max(1, maxMs - minMs));
        }

        // Gaussian centered on EMA value, variance shrinks with more samples
        double center = ema.getValue();
        double range = maxMs - minMs;
        // Variance factor: starts at 0.3 of range, decays to 0.05
        double varianceFactor = Math.max(0.05, 0.3 * MIN_SAMPLES_FOR_ADAPTIVE / samples);
        double stdDev = range * varianceFactor;

        double sample = center + random.nextGaussian() * stdDev;

        // Clamp to [min, max]
        int result = (int) Math.round(Math.max(minMs, Math.min(maxMs, sample)));
        return result;
    }

    /**
     * Returns the current learned optimal sleep for a context, or -1 if not enough data.
     */
    public double getLearnedOptimal(String context) {
        ExponentialMovingAverage ema = sleepEmas.get(context);
        if (ema == null || ema.getCount() < MIN_SAMPLES_FOR_ADAPTIVE) return -1;
        return ema.getValue();
    }

    /**
     * Returns the sample count for a context.
     */
    public long getSampleCount(String context) {
        return sampleCounts.getOrDefault(context, 0L);
    }

    /**
     * Returns whether adaptive mode is active for a context.
     */
    public boolean isAdaptive(String context) {
        return sampleCounts.getOrDefault(context, 0L) >= MIN_SAMPLES_FOR_ADAPTIVE;
    }
}
