package rsb.methods;

import lombok.extern.slf4j.Slf4j;

/**
 * A class that provides methods that use data from the game client. For
 * internal use.
 *
 * @author GigiaJ
 */
@Slf4j
public abstract class MethodProvider {

	public static MethodContext methods = null;

	public MethodProvider(MethodContext ctx) {
		this.methods = ctx;
	}

	/**
	 * Returns a linearly distributed pseudorandom integer.
	 *
	 * @param min The inclusive lower bound.
	 * @param max The exclusive upper bound.
	 * @return Random integer min (less than or equal) to n (less than) max.
	 */
	public int random(int min, int max) {
		return min + (max == min ? 0 : methods.random.nextInt(max - min));
	}

	/**
	 * Returns a normally distributed pseudorandom integer about a mean centered
	 * between min and max with a provided standard deviation.
	 *
	 * @param min The inclusive lower bound.
	 * @param max The exclusive upper bound.
	 * @param sd  The standard deviation. A higher value will increase the
	 *            probability of numbers further from the mean being returned.
	 * @return Random integer min (less than or equal to) n (less than) max from the normal distribution
	 *         described by the parameters.
	 */
	public int random(int min, int max, int sd) {
		int mean = min + (max - min) / 2;
		int rand;
		do {
			rand = (int) (methods.random.nextGaussian() * sd + mean);
		} while (rand < min || rand >= max);
		return rand;
	}

	/**
	 * Returns a normally distributed pseudorandom integer with a provided
	 * standard deviation about a provided mean.
	 *
	 * @param min  The inclusive lower bound.
	 * @param max  The exclusive upper bound.
	 * @param mean The mean (greater than or equal to min and less than max).
	 * @param sd   The standard deviation. A higher value will increase the
	 *             probability of numbers further from the mean being returned.
	 * @return Random integer min (less than or equal) to n (less than) max from the normal distribution
	 *         described by the parameters.
	 */
	public int random(int min, int max, int mean, int sd) {
		int rand;
		do {
			rand = (int) (methods.random.nextGaussian() * sd + mean);
		} while (rand < min || rand >= max);
		return rand;
	}

	/**
	 * Returns a linearly distributed pseudorandom <code>double</code>.
	 *
	 * @param min The inclusive lower bound.
	 * @param max The exclusive upper bound.
	 * @return Random min (less than or equal) to n (less than) max.
	 */
	public double random(double min, double max) {
		return min + methods.random.nextDouble() * (max - min);
	}

	/**
	 * @param toSleep The time to sleep in milliseconds.
	 */
	public void sleep(int toSleep) {
		try {
			long start = System.currentTimeMillis();
			Thread.sleep(toSleep);
			long now; // Guarantee minimum sleep
			while (start + toSleep > (now = System.currentTimeMillis())) {
				Thread.sleep(start + toSleep - now);
			}
		} catch (InterruptedException ignored) {
			log.debug("Method sleep disrupted", ignored);
		}
	}

	/**
	 * Gets the digit at the index of the number
	 * @param number the number to get the digit from
	 * @param index the position to check
	 *
	 * @return the digit in the number
	 */
	int nth ( int number, int index ) {
		return (int)(number / Math.pow(10, index)) % 10;
	}
}
