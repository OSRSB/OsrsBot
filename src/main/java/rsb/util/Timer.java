package rsb.util;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;

/**
 * A Timer
 */
public class Timer {

	private long end;
	private final long start;
	private final long period;
	private static ExecutorService executor = Executors.newSingleThreadExecutor();

	/**
	 * Instantiates a new Timer with a given time
	 * period in milliseconds.
	 *
	 * @param period Time period in milliseconds.
	 */
	public Timer(long period) {
		this.period = period;
		this.start = System.currentTimeMillis();
		this.end = start + period;
	}

	/**
	 * Returns the number of milliseconds elapsed since
	 * the start time.
	 *
	 * @return The elapsed time in milliseconds.
	 */
	public long getElapsed() {
		return (System.currentTimeMillis() - start);
	}

	/**
	 * Returns the number of milliseconds remaining
	 * until the timer is up.
	 *
	 * @return The remaining time in milliseconds.
	 */
	public long getRemaining() {
		if (isRunning()) {
			return (end - System.currentTimeMillis());
		}
		return 0;
	}

	/**
	 * Returns <tt>true</tt> if this timer's time period
	 * has not yet elapsed.
	 *
	 * @return <tt>true</tt> if the time period has not yet passed.
	 */
	public boolean isRunning() {
		return (System.currentTimeMillis() < end);
	}

	/**
	 * Restarts this timer using its period.
	 */
	public void reset() {
		this.end = System.currentTimeMillis() + period;
	}

	/**
	 * Sets the end time of this timer to a given number of
	 * milliseconds from the time it is called. This does
	 * not edit the period of the timer (so will not affect
	 * operation after reset).
	 *
	 * @param ms The number of milliseconds before the timer
	 *           should stop running.
	 * @return The new end time.
	 */
	public long setEndIn(long ms) {
		this.end = System.currentTimeMillis() + ms;
		return this.end;
	}

	/**
	 * Returns a formatted String of the time elapsed.
	 *
	 * @return The elapsed time formatted hh:mm:ss.
	 */
	public String toElapsedString() {
		return format(getElapsed());
	}

	/**
	 * Returns a formatted String of the time remaining.
	 *
	 * @return The remaining time formatted hh:mm:ss.
	 */
	public String toRemainingString() {
		return format(getRemaining());
	}

	public static long timeFromMark(long ms) {
		return new AtomicLong(System.currentTimeMillis()).addAndGet((-1 * ms));
	}

	/**
	 * Allows a condition to be passed to check and a timeout for the condition to pass
	 * Continuously checks the condition in a while loop on an executor thread
	 * @param condition			the boolean conditional to check for
	 * @param timeout			the time limit for waiting for the condition check to return true
	 * @return					<tt>true</tt> if the task was executed; otherwise <tt>false</tt>
	 */
	public static boolean waitCondition(BooleanSupplier condition, long timeout) {
		long start = System.currentTimeMillis();
		long end = start + timeout;

		Callable<Boolean> future = () -> {
				while (!condition.getAsBoolean()) {
					if (System.currentTimeMillis() > end) {
						return false;
					}
				}
				return true;
		};
		try {
			return executor.submit(future).get();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Converts milliseconds to a String in the format
	 * hh:mm:ss.
	 *
	 * @param time The number of milliseconds.
	 * @return The formatted String.
	 */
	public static String format(long time) {
		StringBuilder t = new StringBuilder();
		long total_secs = time / 1000;
		long total_mins = total_secs / 60;
		long total_hrs = total_mins / 60;
		int secs = (int) total_secs % 60;
		int mins = (int) total_mins % 60;
		int hrs = (int) total_hrs % 60;
		if (hrs < 10) {
			t.append("0");
		}
		t.append(hrs);
		t.append(":");
		if (mins < 10) {
			t.append("0");
		}
		t.append(mins);
		t.append(":");
		if (secs < 10) {
			t.append("0");
		}
		t.append(secs);
		return t.toString();
	}
}