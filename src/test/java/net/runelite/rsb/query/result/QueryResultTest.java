package net.runelite.rsb.query.result;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class QueryResultTest {

    private QueryResult<String> result;

    @Before
    public void setUp() {
        result = new QueryResult<>(new ArrayList<>(Arrays.asList("apple", "banana", "cherry")));
    }

    // --- Boundary access ---

    @Test
    public void firstReturnsFirstElement() {
        assertEquals("apple", result.first());
    }

    @Test
    public void lastReturnsLastElement() {
        assertEquals("cherry", result.last());
    }

    @Test
    public void firstOnEmptyReturnsNull() {
        assertNull(new QueryResult<String>(new ArrayList<>()).first());
    }

    @Test
    public void lastOnEmptyReturnsNull() {
        assertNull(new QueryResult<String>(new ArrayList<>()).last());
    }

    // --- Size & emptiness ---

    @Test
    public void sizeMatchesListSize() {
        assertEquals(3, result.size());
    }

    @Test
    public void isEmptyFalseWhenNotEmpty() {
        assertFalse(result.isEmpty());
    }

    @Test
    public void isEmptyTrueForEmptyResult() {
        assertTrue(new QueryResult<String>(new ArrayList<>()).isEmpty());
    }

    // --- Containment ---

    @Test
    public void containsReturnsTrueForPresentElement() {
        assertTrue(result.contains("banana"));
    }

    @Test
    public void containsReturnsFalseForMissingElement() {
        assertFalse(result.contains("mango"));
    }

    @Test
    public void containsAllReturnsTrueForSubset() {
        assertTrue(result.containsAll(Arrays.asList("apple", "cherry")));
    }

    @Test
    public void containsAllReturnsFalseWhenOneElementMissing() {
        assertFalse(result.containsAll(Arrays.asList("apple", "mango")));
    }

    // --- Ordering ---

    @Test
    public void reverseReversesOrder() {
        result.reverse();
        assertEquals("cherry", result.first());
        assertEquals("apple", result.last());
    }

    @Test
    public void sortSortsWithComparator() {
        QueryResult<String> r = new QueryResult<>(new ArrayList<>(Arrays.asList("cherry", "apple", "banana")));
        r.sort(String::compareTo);
        assertEquals("apple", r.first());
        assertEquals("cherry", r.last());
    }

    // --- Limiting ---

    @Test
    public void limitReducesToNElements() {
        QueryResult<String> limited = result.limit(2);
        assertEquals(2, limited.size());
        assertEquals("apple", limited.first());
        assertEquals("banana", limited.last());
    }

    @Test
    public void limitWithSkipSkipsLeadingElements() {
        // skip 1, take 2 → ["banana", "cherry"]
        QueryResult<String> limited = result.limit(1, 2);
        assertEquals(2, limited.size());
        assertEquals("banana", limited.first());
        assertEquals("cherry", limited.last());
    }

    @Test
    public void limitBeyondSizeReturnAll() {
        QueryResult<String> limited = result.limit(100);
        assertEquals(3, limited.size());
    }

    // --- Mutations ---

    @Test
    public void addIncreasesSize() {
        result.add("date");
        assertEquals(4, result.size());
        assertTrue(result.contains("date"));
    }

    @Test
    public void removeDecreasesSize() {
        result.remove("banana");
        assertEquals(2, result.size());
        assertFalse(result.contains("banana"));
    }

    @Test
    public void removeAllRemovesMultiple() {
        result.removeAll(Arrays.asList("apple", "cherry"));
        assertEquals(1, result.size());
        assertTrue(result.contains("banana"));
    }

    @Test
    public void retainAllKeepsOnlyMatching() {
        result.retainAll(Arrays.asList("apple", "cherry"));
        assertEquals(2, result.size());
        assertFalse(result.contains("banana"));
    }

    @Test
    public void addAllIncreasesSize() {
        result.addAll(Arrays.asList("date", "elderberry"));
        assertEquals(5, result.size());
    }

    @Test
    public void clearEmptiesResult() {
        result.clear();
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    // --- Conversion ---

    @Test
    public void toListReturnsAllElements() {
        List<String> list = result.toList();
        assertEquals(3, list.size());
        assertTrue(list.containsAll(Arrays.asList("apple", "banana", "cherry")));
    }

    @Test
    public void toArrayReturnsAllElements() {
        Object[] arr = result.toArray();
        assertEquals(3, arr.length);
    }

    // --- Iteration ---

    @Test
    public void iteratorCoversAllElements() {
        int count = 0;
        for (String s : result) {
            count++;
        }
        assertEquals(3, count);
    }

    // --- Random ---

    @Test
    public void randomOnEmptyReturnsNull() {
        assertNull(new QueryResult<String>(new ArrayList<>()).random());
    }

    @Test
    public void randomOnSingleElementReturnsThatElement() {
        QueryResult<String> single = new QueryResult<>(new ArrayList<>(List.of("only")));
        assertEquals("only", single.random());
    }

    @Test
    public void randomReturnsElementFromResult() {
        String r = result.random();
        assertTrue(Arrays.asList("apple", "banana", "cherry").contains(r));
    }

    // --- Shuffle ---

    @Test
    public void shufflePreservesAllElements() {
        result.shuffle();
        assertEquals(3, result.size());
        assertTrue(result.contains("apple"));
        assertTrue(result.contains("banana"));
        assertTrue(result.contains("cherry"));
    }
}
