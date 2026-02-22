package net.runelite.rsb.script.adaptive;

import lombok.extern.slf4j.Slf4j;
import net.runelite.rsb.script.adaptive.data.LearningDataStore;
import net.runelite.rsb.script.adaptive.data.SessionSummary;
import net.runelite.rsb.script.adaptive.data.StrategyProfile;
import net.runelite.rsb.script.adaptive.paint.AdaptivePaintOverlay;

import java.util.Map;

/**
 * Central hub that wires all adaptive components together.
 * Created by AdaptiveScript on start, provides access to tracker, optimizer, sleep, chat, and store.
 */
@Slf4j
public class AdaptiveContext {
    private final ActionTracker tracker;
    private final StrategyOptimizer optimizer;
    private final AdaptiveSleep sleep;
    private final ChatReactor chat;
    private final LearningDataStore store;
    private final AdaptivePaintOverlay paint;
    private final Map<String, StrategyProfile> strategies;

    public AdaptiveContext(String scriptName, String accountName) {
        this.store = new LearningDataStore(scriptName, accountName);
        this.strategies = store.loadStrategies();
        this.tracker = new ActionTracker(strategies);
        this.optimizer = new StrategyOptimizer(strategies);
        this.sleep = new AdaptiveSleep();
        this.chat = new ChatReactor();
        this.paint = new AdaptivePaintOverlay(this);

        log.info("Adaptive context initialized for script={}, account={}", scriptName, accountName);
        log.info("Loaded {} strategy profiles from {}", strategies.size(), store.getDataPath());
    }

    /**
     * Saves all learning data to disk. Called on script finish.
     */
    public void save() {
        store.saveStrategies(strategies);
        SessionSummary summary = tracker.buildSessionSummary();
        store.saveSession(summary);
        log.info("Saved learning data: {} strategies, session: {} actions, {} XP",
                strategies.size(), summary.getTotalActions(), summary.getTotalXpGained());
    }

    public ActionTracker getTracker() {
        return tracker;
    }

    public StrategyOptimizer getOptimizer() {
        return optimizer;
    }

    public AdaptiveSleep getSleep() {
        return sleep;
    }

    public ChatReactor getChat() {
        return chat;
    }

    public LearningDataStore getStore() {
        return store;
    }

    public AdaptivePaintOverlay getPaint() {
        return paint;
    }

    public Map<String, StrategyProfile> getStrategies() {
        return strategies;
    }
}
