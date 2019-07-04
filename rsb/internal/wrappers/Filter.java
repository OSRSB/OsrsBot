package net.runelite.client.rsb.internal.wrappers;

/**
 * Provides a predicate that determines whether or not
 * a given input should be filtered out of a collection.
 *
 * @author Jacmob
 */
public interface Filter<T> {

	/**
	 * Determines whether a given input
	 * should be included or excluded.
	 *
	 * @param t The input.
	 * @return <tt>true</tt> to include;
	 *         <tt>false</tt> to exclude.
	 */
	public boolean accept(T t);

}
