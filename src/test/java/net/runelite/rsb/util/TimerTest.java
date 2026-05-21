package net.runelite.rsb.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    // --- Timer.format() — pure static, no time dependency ---

    @Test
    public void formatZero() {
        assertEquals("00:00:00", Timer.format(0));
    }

    @Test
    public void formatOneSecond() {
        assertEquals("00:00:01", Timer.format(1_000));
    }

    @Test
    public void formatNineSeconds() {
        assertEquals("00:00:09", Timer.format(9_000));
    }

    @Test
    public void formatSixtySeconds() {
        assertEquals("00:01:00", Timer.format(60_000));
    }

    @Test
    public void formatNineMinutes() {
        assertEquals("00:09:00", Timer.format(540_000));
    }

    @Test
    public void formatOneHour() {
        assertEquals("01:00:00", Timer.format(3_600_000));
    }

    @Test
    public void formatMixedTime() {
        // 1h 23m 45s = (3600 + 23*60 + 45) * 1000 ms
        long ms = (3600L + 23 * 60L + 45L) * 1000L;
        assertEquals("01:23:45", Timer.format(ms));
    }

    @Test
    public void formatPadsAllComponentsWithZero() {
        // 9h 9m 9s
        long ms = (9 * 3600L + 9 * 60L + 9L) * 1000L;
        assertEquals("09:09:09", Timer.format(ms));
    }

    @Test
    public void formatSubSecondMillisecondsTruncated() {
        // 1500 ms → 1 second (integer division)
        assertEquals("00:00:01", Timer.format(1_500));
    }

    @Test
    public void formatLargeValue() {
        // 99h 59m 59s = (99*3600 + 59*60 + 59) * 1000
        long ms = (99 * 3600L + 59 * 60L + 59L) * 1000L;
        assertEquals("99:59:59", Timer.format(ms));
    }

    // --- Timer lifecycle (real-time, very short periods) ---

    @Test
    public void timerIsRunningImmediatelyAfterCreation() {
        Timer t = new Timer(10_000);
        assertTrue(t.isRunning());
    }

    @Test
    public void timerIsNotRunningAfterExpiry() throws InterruptedException {
        Timer t = new Timer(50);
        Thread.sleep(120);
        assertFalse(t.isRunning());
    }

    @Test
    public void getRemainingPositiveWhileRunning() {
        Timer t = new Timer(10_000);
        assertTrue(t.getRemaining() > 0);
        assertTrue(t.getRemaining() <= 10_000);
    }

    @Test
    public void getRemainingZeroAfterExpiry() throws InterruptedException {
        Timer t = new Timer(50);
        Thread.sleep(120);
        assertEquals(0, t.getRemaining());
    }

    @Test
    public void getElapsedIncreasesOverTime() throws InterruptedException {
        Timer t = new Timer(10_000);
        long e1 = t.getElapsed();
        Thread.sleep(60);
        long e2 = t.getElapsed();
        assertTrue("elapsed should increase", e2 > e1);
    }

    @Test
    public void resetExtendsExpiredTimer() throws InterruptedException {
        Timer t = new Timer(50);
        Thread.sleep(120);
        assertFalse(t.isRunning());
        t.reset();
        assertTrue(t.isRunning());
    }

    @Test
    public void setEndInExtendsTimer() throws InterruptedException {
        Timer t = new Timer(50);
        Thread.sleep(120);
        assertFalse(t.isRunning());
        t.setEndIn(10_000);
        assertTrue(t.isRunning());
    }

    // --- waitCondition ---

    @Test
    public void waitConditionReturnsTrueForImmediatelyTrueCondition() {
        assertTrue(Timer.waitCondition(() -> true, 1_000));
    }

    @Test
    public void waitConditionReturnsFalseWhenTimeoutExpires() {
        // Condition never becomes true; should return false after 100ms
        assertFalse(Timer.waitCondition(() -> false, 100));
    }

    @Test
    public void waitConditionReturnsTrueBeforeTimeout() throws InterruptedException {
        // Condition becomes true after ~30ms; timeout is 500ms
        long[] becomeTrue = {System.currentTimeMillis() + 30};
        assertTrue(Timer.waitCondition(
                () -> System.currentTimeMillis() >= becomeTrue[0],
                500
        ));
    }
}
