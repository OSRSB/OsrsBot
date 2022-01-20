package rsb.util;

/**
 * A record designed to hold a pair of information
 *
 * @param <A> The type of the first object
 * @param <B> The type of the second object
 */
public final class Pair<A, B> {
    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "Pair[]";
    }
}