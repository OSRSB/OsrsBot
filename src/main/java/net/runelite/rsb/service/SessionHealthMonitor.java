package net.runelite.rsb.service;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks session length and loop() action rate to surface ban-risk warnings.
 *
 * Risk thresholds are based on community analysis of Jagex detection patterns:
 *  - Sessions longer than 4 h without a break are elevated risk.
 *  - Sessions longer than 6 h are considered critical.
 *  - More than 300 loop() calls per minute suggests insufficient sleep times.
 */
@Slf4j
public class SessionHealthMonitor {

    private static final long WARN_SESSION_MS     = 4 * 60 * 60_000L;
    private static final long CRITICAL_SESSION_MS = 6 * 60 * 60_000L;
    private static final int  MAX_LOOPS_PER_MIN   = 300;
    private static final long RATE_WINDOW_MS      = 60_000L;

    private final String scriptName;
    private final long sessionStart = System.currentTimeMillis();
    private final AtomicLong loopCount = new AtomicLong();

    private long rateWindowStart = sessionStart;
    private long rateWindowBase  = 0;

    private boolean warnFired     = false;
    private boolean criticalFired = false;

    public SessionHealthMonitor(String scriptName) {
        this.scriptName = scriptName;
        log.info("Session started for script '{}'", scriptName);
    }

    /**
     * Call once per loop() invocation. Checks action rate and session length,
     * logging a warning or error when thresholds are exceeded.
     */
    public void onLoop() {
        long count = loopCount.incrementAndGet();
        long now   = System.currentTimeMillis();

        checkActionRate(count, now);
        checkSessionLength(now);
    }

    private void checkActionRate(long count, long now) {
        long windowMs = now - rateWindowStart;
        if (windowMs >= RATE_WINDOW_MS) {
            long windowLoops = count - rateWindowBase;
            double rate = windowLoops * 60_000.0 / windowMs;
            if (rate > MAX_LOOPS_PER_MIN) {
                log.warn("[{}] High action rate: {}/min (max {}). Increase loop() sleep times.",
                        scriptName, (int) rate, MAX_LOOPS_PER_MIN);
            }
            rateWindowStart = now;
            rateWindowBase  = count;
        }
    }

    private void checkSessionLength(long now) {
        long elapsedMs = now - sessionStart;
        if (!criticalFired && elapsedMs >= CRITICAL_SESSION_MS) {
            criticalFired = true;
            log.error("[{}] SESSION CRITICAL: {}h elapsed — ban risk is very high. Stop the script.",
                    scriptName, elapsedMs / 3_600_000);
        } else if (!warnFired && elapsedMs >= WARN_SESSION_MS) {
            warnFired = true;
            log.warn("[{}] SESSION WARNING: {}h elapsed — take a break soon.",
                    scriptName, elapsedMs / 3_600_000);
        }
    }

    public void onFinish() {
        long elapsedMs = System.currentTimeMillis() - sessionStart;
        log.info("[{}] Session ended. Duration: {}m, loop() calls: {}",
                scriptName, elapsedMs / 60_000, loopCount.get());
    }

    public long getElapsedMs()  { return System.currentTimeMillis() - sessionStart; }
    public long getLoopCount()  { return loopCount.get(); }
}
