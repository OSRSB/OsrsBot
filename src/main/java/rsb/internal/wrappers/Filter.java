package rsb.internal.wrappers;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #test(Object)}.
 *
 * @param <T> the type of the input to the predicate
 *
 * @since 1.8
 */
@FunctionalInterface
public interface Filter<T> extends Predicate<T> {


	/**
	 * Returns a composed predicate that represents a short-circuiting logical
	 * AND of this predicate and another.  When evaluating the composed
	 * predicate, if this predicate is {@code false}, then the {@code other}
	 * predicate is not evaluated.
	 *
	 * <p>Any exceptions thrown during evaluation of either predicate are relayed
	 * to the caller; if evaluation of this predicate throws an exception, the
	 * {@code other} predicate will not be evaluated.
	 *
	 * @param other a predicate that will be logically-ANDed with this
	 *              predicate
	 * @param check_before a non-used parameter
	 * @return a composed predicate that represents the short-circuiting logical
	 * AND of this predicate and the {@code other} predicate
	 * @throws NullPointerException if other is null
	 */
	default Filter<T> combine(Filter<? super T> other, boolean check_before) {
		Objects.requireNonNull(other);
		return (t) -> test(t) && other.test(t);
	}

	/**
	 * Returns a predicate that represents the logical negation of this
	 * predicate.
	 *
	 * @return a predicate that represents the logical negation of this
	 * predicate
	 */
	default Filter<T> negate() {
		return (t) -> !test(t);
	}

	/**
	 * Returns a composed predicate that represents a short-circuiting logical
	 * OR of this predicate and another.  When evaluating the composed
	 * predicate, if this predicate is {@code true}, then the {@code other}
	 * predicate is not evaluated.
	 *
	 * <p>Any exceptions thrown during evaluation of either predicate are relayed
	 * to the caller; if evaluation of this predicate throws an exception, the
	 * {@code other} predicate will not be evaluated.
	 *
	 * @param other a predicate that will be logically-ORed with this
	 *              predicate
	 * @return a composed predicate that represents the short-circuiting logical
	 * OR of this predicate and the {@code other} predicate
	 * @throws NullPointerException if other is null
	 */
	default Filter<T> or(Filter<? super T> other) {
		Objects.requireNonNull(other);
		return (t) -> test(t) || other.test(t);
	}

	/**
	 * Returns a predicate that tests if two arguments are equal according
	 * to {@link Objects#equals(Object, Object)}.
	 *
	 * @param <T> the type of arguments to the predicate
	 * @param targetRef the object reference with which to compare for equality,
	 *               which may be {@code null}
	 * @return a predicate that tests if two arguments are equal according
	 * to {@link Objects#equals(Object, Object)}
	 */
	static <T> Filter<T> isEqual(Object targetRef) {
		return (null == targetRef)
				? Objects::isNull
				: object -> targetRef.equals(object);
	}

	/**
	 * Returns a predicate that is the negation of the supplied predicate.
	 * This is accomplished by returning result of the calling
	 * {@code target.negate()}.
	 *
	 * @param <T>     the type of arguments to the specified predicate
	 * @param target  predicate to negate
	 *
	 * @return a predicate that negates the results of the supplied
	 *         predicate
	 *
	 * @throws NullPointerException if target is null
	 *
	 * @since 11
	 */
	@SuppressWarnings("unchecked")
	static <T> Filter<T> not(Filter<? super T> target) {
		Objects.requireNonNull(target);
		return (Filter<T>)target.negate();
	}
}
