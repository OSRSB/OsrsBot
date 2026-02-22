package net.runelite.rsb.script.adaptive;

import net.runelite.rsb.script.adaptive.data.StrategyProfile;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * UCB1 + epsilon-greedy strategy selection.
 * Balances exploration of new strategies with exploitation of known-good ones.
 */
public class StrategyOptimizer {
    private static final int MIN_SAMPLES_FOR_EXPLOIT = 5;
    private static final double INITIAL_EPSILON = 0.35;
    private static final double MIN_EPSILON = 0.05;
    private static final double EPSILON_DECAY_RATE = 50.0; // samples until epsilon halves

    private final Map<String, StrategyProfile> strategies;
    private final Random random;
    private long totalSelections;

    public StrategyOptimizer(Map<String, StrategyProfile> strategies) {
        this.strategies = strategies;
        this.random = new Random();
        this.totalSelections = 0;

        // Initialize totalSelections from existing data
        for (StrategyProfile profile : strategies.values()) {
            totalSelections += profile.getTotalAttempts();
        }
    }

    /**
     * Selects a strategy from the candidates using UCB1 + epsilon-greedy.
     *
     * @param candidates List of strategy keys (e.g., "chop_tree:1276")
     * @param epsilon    Exploration rate (0 = pure exploitation, 1 = pure exploration)
     * @return The selected strategy key
     */
    public String selectStrategy(List<String> candidates, double epsilon) {
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException("Candidates list cannot be empty");
        }
        if (candidates.size() == 1) {
            totalSelections++;
            return candidates.get(0);
        }

        // Force-explore strategies with insufficient data
        for (String candidate : candidates) {
            StrategyProfile profile = strategies.get(candidate);
            if (profile == null || profile.getTotalAttempts() < MIN_SAMPLES_FOR_EXPLOIT) {
                totalSelections++;
                return candidate;
            }
        }

        // Epsilon-greedy: explore randomly with probability epsilon
        if (random.nextDouble() < epsilon) {
            totalSelections++;
            return candidates.get(random.nextInt(candidates.size()));
        }

        // UCB1 exploitation: pick the strategy with the highest UCB1 score
        String best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (String candidate : candidates) {
            double score = computeUCB1Score(candidate);
            if (score > bestScore) {
                bestScore = score;
                best = candidate;
            }
        }

        totalSelections++;
        return best != null ? best : candidates.get(0);
    }

    /**
     * Computes the UCB1 score for a strategy.
     * UCB1 = mean_reward + C * sqrt(ln(total_selections) / strategy_selections)
     */
    private double computeUCB1Score(String strategyKey) {
        StrategyProfile profile = strategies.get(strategyKey);
        if (profile == null || profile.getTotalAttempts() == 0) {
            return Double.MAX_VALUE; // Unexplored = highest priority
        }

        // Normalize XP/hr to [0, 1] using best known XP/hr
        double maxXpPerHour = 0;
        for (StrategyProfile p : strategies.values()) {
            if (p.getXpPerHour() > maxXpPerHour) {
                maxXpPerHour = p.getXpPerHour();
            }
        }

        double normalizedReward = maxXpPerHour > 0 ? profile.getXpPerHour() / maxXpPerHour : 0;
        // Weight by success rate
        double reward = normalizedReward * 0.7 + profile.getSuccessRate() * 0.3;

        double exploration = Math.sqrt(Math.log(Math.max(1, totalSelections)) / profile.getTotalAttempts());
        double C = 1.414; // sqrt(2), standard UCB1 constant

        return reward + C * exploration;
    }

    /**
     * Returns the recommended epsilon based on total data accumulated.
     * Starts at ~0.35, decays to ~0.05 as data grows.
     */
    public double getRecommendedEpsilon() {
        double decay = Math.exp(-totalSelections / EPSILON_DECAY_RATE);
        return MIN_EPSILON + (INITIAL_EPSILON - MIN_EPSILON) * decay;
    }

    /**
     * Returns a confidence level string based on data quality.
     */
    public String getConfidenceLevel() {
        if (totalSelections < 10) return "Very Low";
        if (totalSelections < 30) return "Low";
        if (totalSelections < 100) return "Medium";
        if (totalSelections < 300) return "High";
        return "Very High";
    }

    /**
     * Returns the exploration rate as a percentage string.
     */
    public String getExploreRateDisplay() {
        return String.format("%.0f%%", getRecommendedEpsilon() * 100);
    }

    public long getTotalSelections() {
        return totalSelections;
    }

    public StrategyProfile getProfile(String strategyKey) {
        return strategies.get(strategyKey);
    }
}
