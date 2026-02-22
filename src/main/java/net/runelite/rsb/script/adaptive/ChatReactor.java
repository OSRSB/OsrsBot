package net.runelite.rsb.script.adaptive;

import lombok.extern.slf4j.Slf4j;
import net.runelite.rsb.event.events.MessageEvent;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Reads chat messages and triggers behavior changes.
 * Implements pattern-matching on game messages (MESSAGE_SERVER type = 0).
 * Provides built-in rules for common skilling messages plus custom pattern support.
 */
@Slf4j
public class ChatReactor {
    private static final int MAX_RECENT_MESSAGES = 50;

    // State flags
    private volatile boolean inventoryFull;
    private volatile boolean levelTooLow;
    private volatile boolean missingTool;
    private volatile boolean unreachable;
    private volatile boolean loggedOut;

    // Last trigger message for debugging
    private volatile String lastTrigger;
    private volatile long lastTriggerTime;

    // Recent message log
    private final Deque<TimestampedMessage> recentMessages;

    // Custom pattern handlers
    private final List<PatternHandler> customHandlers;

    // Built-in patterns (compiled once)
    private static final Pattern INVENTORY_FULL_PATTERN = Pattern.compile(
            "(?i)(your inventory is too full|inventory is full|you can't carry any more|not enough space)");
    private static final Pattern LEVEL_TOO_LOW_PATTERN = Pattern.compile(
            "(?i)(you need a higher|you need a .+ level of|you do not have the .+ level)");
    private static final Pattern MISSING_TOOL_PATTERN = Pattern.compile(
            "(?i)(you don't have a|you need a .+ to|you do not have a)");
    private static final Pattern UNREACHABLE_PATTERN = Pattern.compile(
            "(?i)(you can't reach that|i can't reach that|you can't get there)");
    private static final Pattern LOGGED_OUT_PATTERN = Pattern.compile(
            "(?i)(you've been logged out|you have been disconnected)");

    public ChatReactor() {
        this.recentMessages = new ConcurrentLinkedDeque<>();
        this.customHandlers = new ArrayList<>();
    }

    /**
     * Process an incoming message event. Called by AdaptiveScript's messageReceived().
     */
    public void onMessage(MessageEvent event) {
        String message = event.getMessage();
        if (message == null || message.isEmpty()) return;

        // Store in recent messages
        recentMessages.addLast(new TimestampedMessage(
                System.currentTimeMillis(), event.getID(), event.getSender(), message));
        while (recentMessages.size() > MAX_RECENT_MESSAGES) {
            recentMessages.removeFirst();
        }

        // Only process server messages for built-in patterns
        if (event.getID() == MessageEvent.MESSAGE_SERVER ||
            event.getID() == MessageEvent.MESSAGE_ACTION) {
            processBuiltInPatterns(message);
        }

        // Process custom handlers for all message types
        for (PatternHandler handler : customHandlers) {
            if (handler.matches(event)) {
                try {
                    handler.callback.accept(event);
                } catch (Exception e) {
                    log.warn("Custom chat handler error: {}", e.getMessage());
                }
            }
        }
    }

    private void processBuiltInPatterns(String message) {
        if (INVENTORY_FULL_PATTERN.matcher(message).find()) {
            inventoryFull = true;
            setLastTrigger(message);
        }
        if (LEVEL_TOO_LOW_PATTERN.matcher(message).find()) {
            levelTooLow = true;
            setLastTrigger(message);
        }
        if (MISSING_TOOL_PATTERN.matcher(message).find()) {
            missingTool = true;
            setLastTrigger(message);
        }
        if (UNREACHABLE_PATTERN.matcher(message).find()) {
            unreachable = true;
            setLastTrigger(message);
        }
        if (LOGGED_OUT_PATTERN.matcher(message).find()) {
            loggedOut = true;
            setLastTrigger(message);
        }
    }

    private void setLastTrigger(String message) {
        lastTrigger = message;
        lastTriggerTime = System.currentTimeMillis();
    }

    /**
     * Registers a custom message pattern handler.
     *
     * @param pattern  Regex pattern to match against message text
     * @param callback Consumer called with the MessageEvent when pattern matches
     */
    public void onMessage(String pattern, Consumer<MessageEvent> callback) {
        customHandlers.add(new PatternHandler(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE), callback));
    }

    /**
     * Registers a custom handler that also filters by message type.
     */
    public void onMessage(String pattern, int messageType, Consumer<MessageEvent> callback) {
        customHandlers.add(new PatternHandler(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE), messageType, callback));
    }

    // ---- State flag getters ----

    public boolean isInventoryFull() {
        return inventoryFull;
    }

    public boolean isLevelTooLow() {
        return levelTooLow;
    }

    public boolean isMissingTool() {
        return missingTool;
    }

    public boolean isUnreachable() {
        return unreachable;
    }

    public boolean isLoggedOut() {
        return loggedOut;
    }

    public String getLastTrigger() {
        return lastTrigger;
    }

    public long getLastTriggerTime() {
        return lastTriggerTime;
    }

    // ---- Flag clearing ----

    public void clearInventoryFull() {
        inventoryFull = false;
    }

    public void clearLevelTooLow() {
        levelTooLow = false;
    }

    public void clearMissingTool() {
        missingTool = false;
    }

    public void clearUnreachable() {
        unreachable = false;
    }

    public void clearLoggedOut() {
        loggedOut = false;
    }

    public void clearAllFlags() {
        inventoryFull = false;
        levelTooLow = false;
        missingTool = false;
        unreachable = false;
        loggedOut = false;
    }

    // ---- Recent messages ----

    /**
     * Returns the most recent messages (newest last).
     */
    public List<TimestampedMessage> getRecentMessages() {
        return new ArrayList<>(recentMessages);
    }

    /**
     * Returns the most recent N messages.
     */
    public List<TimestampedMessage> getRecentMessages(int count) {
        List<TimestampedMessage> all = new ArrayList<>(recentMessages);
        if (all.size() <= count) return all;
        return all.subList(all.size() - count, all.size());
    }

    /**
     * Checks if any recent message matches a pattern (within last N seconds).
     */
    public boolean hasRecentMessage(String pattern, int withinSeconds) {
        long cutoff = System.currentTimeMillis() - (withinSeconds * 1000L);
        Pattern compiled = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        for (TimestampedMessage msg : recentMessages) {
            if (msg.timestamp >= cutoff && compiled.matcher(msg.message).find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the count of active flags.
     */
    public int getActiveFlagCount() {
        int count = 0;
        if (inventoryFull) count++;
        if (levelTooLow) count++;
        if (missingTool) count++;
        if (unreachable) count++;
        if (loggedOut) count++;
        return count;
    }

    /**
     * Returns a summary of active flags for display.
     */
    public String getActiveFlagsSummary() {
        List<String> active = new ArrayList<>();
        if (inventoryFull) active.add("InvFull");
        if (levelTooLow) active.add("LvlLow");
        if (missingTool) active.add("NoTool");
        if (unreachable) active.add("NoPath");
        if (loggedOut) active.add("LoggedOut");
        return active.isEmpty() ? "None" : String.join(", ", active);
    }

    // ---- Inner classes ----

    public static class TimestampedMessage {
        public final long timestamp;
        public final int type;
        public final String sender;
        public final String message;

        public TimestampedMessage(long timestamp, int type, String sender, String message) {
            this.timestamp = timestamp;
            this.type = type;
            this.sender = sender;
            this.message = message;
        }
    }

    private static class PatternHandler {
        final Pattern pattern;
        final int messageType; // -1 means any type
        final Consumer<MessageEvent> callback;

        PatternHandler(Pattern pattern, Consumer<MessageEvent> callback) {
            this(pattern, -1, callback);
        }

        PatternHandler(Pattern pattern, int messageType, Consumer<MessageEvent> callback) {
            this.pattern = pattern;
            this.messageType = messageType;
            this.callback = callback;
        }

        boolean matches(MessageEvent event) {
            if (messageType >= 0 && event.getID() != messageType) return false;
            return pattern.matcher(event.getMessage()).find();
        }
    }
}
