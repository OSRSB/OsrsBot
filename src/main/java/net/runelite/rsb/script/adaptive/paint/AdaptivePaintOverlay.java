package net.runelite.rsb.script.adaptive.paint;

import net.runelite.rsb.script.adaptive.AdaptiveContext;
import net.runelite.rsb.script.adaptive.ActionTracker;
import net.runelite.rsb.script.adaptive.StrategyOptimizer;
import net.runelite.rsb.script.adaptive.ChatReactor;

import java.awt.*;

/**
 * Renders an adaptive stats overlay on the game screen.
 * Shows runtime, XP/hr, actions/hr, explore rate, confidence, and chat status.
 */
public class AdaptivePaintOverlay {
    private static final int X = 10;
    private static final int Y = 340;
    private static final int LINE_HEIGHT = 16;
    private static final int BOX_WIDTH = 220;

    private static final Color BG_COLOR = new Color(0, 0, 0, 180);
    private static final Color BORDER_COLOR = new Color(200, 170, 0);
    private static final Color TITLE_COLOR = new Color(255, 215, 0);
    private static final Color LABEL_COLOR = new Color(200, 200, 200);
    private static final Color VALUE_COLOR = Color.WHITE;
    private static final Color ALERT_COLOR = new Color(255, 80, 80);
    private static final Color SUCCESS_COLOR = new Color(80, 255, 80);

    private final AdaptiveContext context;

    public AdaptivePaintOverlay(AdaptiveContext context) {
        this.context = context;
    }

    public void render(Graphics g) {
        ActionTracker tracker = context.getTracker();
        StrategyOptimizer optimizer = context.getOptimizer();
        ChatReactor chat = context.getChat();

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Count lines to determine box height
        int lineCount = 9; // base lines
        if (chat.getActiveFlagCount() > 0) lineCount++;

        int boxHeight = LINE_HEIGHT * lineCount + 12;

        // Background
        g2.setColor(BG_COLOR);
        g2.fillRoundRect(X, Y, BOX_WIDTH, boxHeight, 8, 8);
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(X, Y, BOX_WIDTH, boxHeight, 8, 8);

        int textX = X + 8;
        int textY = Y + LINE_HEIGHT;

        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(TITLE_COLOR);
        g2.drawString("Adaptive Bot", textX, textY);
        textY += LINE_HEIGHT;

        // Runtime
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        drawLabelValue(g2, textX, textY, "Runtime:", formatTime(tracker.getSessionElapsedMs()));
        textY += LINE_HEIGHT;

        // XP/hr
        drawLabelValue(g2, textX, textY, "XP/hr:", formatNumber(tracker.getLiveXpPerHour()));
        textY += LINE_HEIGHT;

        // Actions/hr
        drawLabelValue(g2, textX, textY, "Actions/hr:", formatNumber(tracker.getLiveActionsPerHour()));
        textY += LINE_HEIGHT;

        // Actions (success/total)
        String actionsStr = tracker.getSuccessfulActions() + "/" + tracker.getTotalActions();
        drawLabelValue(g2, textX, textY, "Actions:", actionsStr);
        textY += LINE_HEIGHT;

        // Success rate
        double rate = tracker.getLiveSuccessRate();
        g2.setColor(LABEL_COLOR);
        g2.drawString("Success:", textX, textY);
        g2.setColor(rate >= 0.8 ? SUCCESS_COLOR : rate >= 0.5 ? VALUE_COLOR : ALERT_COLOR);
        g2.drawString(String.format("%.1f%%", rate * 100), textX + 80, textY);
        textY += LINE_HEIGHT;

        // Explore rate
        drawLabelValue(g2, textX, textY, "Explore:", optimizer.getExploreRateDisplay());
        textY += LINE_HEIGHT;

        // Confidence
        drawLabelValue(g2, textX, textY, "Confidence:", optimizer.getConfidenceLevel());
        textY += LINE_HEIGHT;

        // Total XP
        drawLabelValue(g2, textX, textY, "Total XP:", formatNumber(tracker.getTotalXpGained()));
        textY += LINE_HEIGHT;

        // Chat flags (only if active)
        if (chat.getActiveFlagCount() > 0) {
            g2.setColor(LABEL_COLOR);
            g2.drawString("Alerts:", textX, textY);
            g2.setColor(ALERT_COLOR);
            g2.drawString(chat.getActiveFlagsSummary(), textX + 80, textY);
        }
    }

    private void drawLabelValue(Graphics2D g2, int x, int y, String label, String value) {
        g2.setColor(LABEL_COLOR);
        g2.drawString(label, x, y);
        g2.setColor(VALUE_COLOR);
        g2.drawString(value, x + 80, y);
    }

    private static String formatTime(long ms) {
        long seconds = ms / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    private static String formatNumber(double value) {
        if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000);
        } else if (value >= 1_000) {
            return String.format("%.1fK", value / 1_000);
        }
        return String.format("%.0f", value);
    }
}
