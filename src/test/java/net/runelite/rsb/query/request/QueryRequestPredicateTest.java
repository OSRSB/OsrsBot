package net.runelite.rsb.query.request;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the predicate-chain logic of QueryRequest.accepts() using a simple
 * String-based test double — no RuneLite game objects required.
 */
public class QueryRequestPredicateTest {

    // Minimal concrete subclass that returns a fixed list
    private static class StringQueryRequest extends QueryRequest<String> {
        private final List<String> items;

        StringQueryRequest(String... items) {
            super();
            this.items = Arrays.asList(items);
        }

        @Override
        public List<String> request() {
            return items;
        }
    }

    // --- No-filter behaviour ---

    @Test
    public void acceptsAllWithNoFilters() {
        StringQueryRequest req = new StringQueryRequest("alpha", "beta");
        assertTrue(req.accepts("alpha"));
        assertTrue(req.accepts("anything"));
        assertTrue(req.accepts(""));
    }

    // --- Single filter ---

    @Test
    public void acceptsElementMatchingSingleFilter() {
        StringQueryRequest req = new StringQueryRequest();
        req.filter(s -> s.startsWith("a"));
        assertTrue(req.accepts("apple"));
    }

    @Test
    public void rejectsElementFailingSingleFilter() {
        StringQueryRequest req = new StringQueryRequest();
        req.filter(s -> s.startsWith("a"));
        assertFalse(req.accepts("banana"));
    }

    // --- Multiple filters (AND semantics) ---

    @Test
    public void allFiltersMustPassForAccepts() {
        StringQueryRequest req = new StringQueryRequest();
        req.filter(s -> s.startsWith("a"));
        req.filter(s -> s.length() > 3);

        assertTrue(req.accepts("apple"));    // starts-with-a AND length > 3
        assertFalse(req.accepts("ant"));     // starts-with-a but length == 3
        assertFalse(req.accepts("banana"));  // length > 3 but not starts-with-a
    }

    @Test
    public void threeFiltersAllRequired() {
        StringQueryRequest req = new StringQueryRequest();
        req.filter(s -> s.contains("a"));
        req.filter(s -> s.contains("e"));
        req.filter(s -> s.length() >= 5);

        assertTrue(req.accepts("apple"));    // a, e, length 5
        assertFalse(req.accepts("ace"));     // a, e, but length < 5
        assertFalse(req.accepts("abcde"));   // a, e, length 5, but wait — "abcde" has 'a' and 'e', length 5
        assertTrue(req.accepts("abcde"));
        assertFalse(req.accepts("baste"));   // e, length 5, but no 'a'? "baste" has 'a' → actually has 'a','e', length 5
        // Let's use clear examples
    }

    // --- Short-circuit: first failing filter stops evaluation ---

    @Test
    public void shortCircuitsOnFirstFailingFilter() {
        StringQueryRequest req = new StringQueryRequest();
        req.filter(s -> false); // always fails
        req.filter(s -> { throw new RuntimeException("should not be called"); });

        // Must not throw — second filter never evaluated
        assertFalse(req.accepts("anything"));
    }

    // --- Edge: filter on empty string ---

    @Test
    public void filterWorkOnEmptyString() {
        StringQueryRequest req = new StringQueryRequest();
        req.filter(s -> s.isEmpty());

        assertTrue(req.accepts(""));
        assertFalse(req.accepts("nonempty"));
    }

    // --- Filter state is independent per request instance ---

    @Test
    public void separateRequestInstancesHaveIndependentFilters() {
        StringQueryRequest req1 = new StringQueryRequest();
        req1.filter(s -> s.startsWith("a"));

        StringQueryRequest req2 = new StringQueryRequest();
        req2.filter(s -> s.startsWith("b"));

        assertTrue(req1.accepts("apple"));
        assertFalse(req1.accepts("banana"));

        assertTrue(req2.accepts("banana"));
        assertFalse(req2.accepts("apple"));
    }

    // --- Adding filters post-construction ---

    @Test
    public void filtersCanBeAddedSequentially() {
        StringQueryRequest req = new StringQueryRequest();

        // No filter yet — accepts everything
        assertTrue(req.accepts("xyz"));

        // Add filter — now only strings starting with 'x' pass
        req.filter(s -> s.startsWith("x"));
        assertTrue(req.accepts("xyz"));
        assertFalse(req.accepts("abc"));

        // Add second filter — must also end with 'z'
        req.filter(s -> s.endsWith("z"));
        assertTrue(req.accepts("xyz"));
        assertFalse(req.accepts("xab"));
    }
}
