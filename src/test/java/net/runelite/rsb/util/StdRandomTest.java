package net.runelite.rsb.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdRandomTest {

    @Before
    public void resetSeed() {
        StdRandom.setSeed(42L);
    }

    // --- Seed management ---

    @Test
    public void getSeedMatchesSetSeed() {
        StdRandom.setSeed(99999L);
        assertEquals(99999L, StdRandom.getSeed());
    }

    @Test
    public void setSeedProducesReproducibleSequence() {
        StdRandom.setSeed(12345L);
        int a1 = StdRandom.uniform(1000);
        int a2 = StdRandom.uniform(1000);

        StdRandom.setSeed(12345L);
        int b1 = StdRandom.uniform(1000);
        int b2 = StdRandom.uniform(1000);

        assertEquals(a1, b1);
        assertEquals(a2, b2);
    }

    // --- uniform(int n) ---

    @Test
    public void uniformIntInRange() {
        for (int i = 0; i < 200; i++) {
            int val = StdRandom.uniform(10);
            assertTrue("Expected [0,10) but got " + val, val >= 0 && val < 10);
        }
    }

    @Test
    public void uniformIntSingleValueReturnsZero() {
        assertEquals(0, StdRandom.uniform(1));
    }

    // --- uniform(int a, int b) ---

    @Test
    public void uniformIntRangeInBounds() {
        for (int i = 0; i < 200; i++) {
            int val = StdRandom.uniform(5, 15);
            assertTrue("Expected [5,15) but got " + val, val >= 5 && val < 15);
        }
    }

    // --- uniform() double ---

    @Test
    public void uniformDoubleInUnitInterval() {
        for (int i = 0; i < 200; i++) {
            double val = StdRandom.uniform();
            assertTrue("Expected [0,1) but got " + val, val >= 0.0 && val < 1.0);
        }
    }

    // --- uniform(double a, double b) ---

    @Test
    public void uniformDoubleRangeInBounds() {
        for (int i = 0; i < 200; i++) {
            double val = StdRandom.uniform(2.0, 5.0);
            assertTrue("Expected [2.0,5.0) but got " + val, val >= 2.0 && val < 5.0);
        }
    }

    // --- uniform(long n) ---

    @Test
    public void uniformLongInRange() {
        for (int i = 0; i < 200; i++) {
            long val = StdRandom.uniform(100L);
            assertTrue("Expected [0,100) but got " + val, val >= 0 && val < 100);
        }
    }

    // --- bernoulli ---

    @Test
    public void bernoulliZeroProbabilityAlwaysFalse() {
        for (int i = 0; i < 50; i++) {
            assertFalse(StdRandom.bernoulli(0.0));
        }
    }

    @Test
    public void bernoulliOneProbabilityAlwaysTrue() {
        for (int i = 0; i < 50; i++) {
            assertTrue(StdRandom.bernoulli(1.0));
        }
    }

    @Test
    public void bernoulliHalfProducesRoughlyFiftyPercent() {
        StdRandom.setSeed(777L);
        int trues = 0;
        for (int i = 0; i < 1000; i++) {
            if (StdRandom.bernoulli(0.5)) trues++;
        }
        // Expect 40-60% true (very generous for a seeded test)
        assertTrue("Expected ~50% true, got " + trues, trues > 350 && trues < 650);
    }

    @Test
    public void bernoulliNoArgEquivalentToHalf() {
        StdRandom.setSeed(1L);
        int trues = 0;
        for (int i = 0; i < 200; i++) {
            if (StdRandom.bernoulli()) trues++;
        }
        assertTrue("Expected roughly 50%, got " + trues, trues > 60 && trues < 140);
    }

    // --- gaussian ---

    @Test
    public void gaussianMeanAndSigmaRoughlyCorrect() {
        StdRandom.setSeed(42L);
        double sum = 0;
        int n = 10_000;
        for (int i = 0; i < n; i++) {
            sum += StdRandom.gaussian(10.0, 2.0);
        }
        double mean = sum / n;
        // Should be within 0.1 of expected mean 10.0
        assertEquals(10.0, mean, 0.1);
    }

    @Test
    public void gaussianAlmostAlwaysWithinFourSigmas() {
        StdRandom.setSeed(42L);
        int outOfRange = 0;
        for (int i = 0; i < 1000; i++) {
            double val = StdRandom.gaussian(0.0, 1.0);
            if (val < -4.0 || val > 4.0) outOfRange++;
        }
        // Expect fewer than 1% outside ±4σ
        assertTrue("Too many outliers beyond ±4σ: " + outOfRange, outOfRange < 10);
    }

    // --- shuffle ---

    @Test
    public void shuffleIntArrayPreservesAllElements() {
        int[] arr = {1, 2, 3, 4, 5};
        StdRandom.shuffle(arr);
        int sum = 0;
        for (int v : arr) sum += v;
        assertEquals(15, sum);
        assertEquals(5, arr.length);
    }

    @Test
    public void shuffleDoubleArrayPreservesAllElements() {
        double[] arr = {1.0, 2.0, 3.0, 4.0};
        StdRandom.shuffle(arr);
        double sum = 0;
        for (double v : arr) sum += v;
        assertEquals(10.0, sum, 0.001);
    }

    @Test
    public void shuffleObjectArrayPreservesAllElements() {
        String[] arr = {"a", "b", "c", "d", "e"};
        StdRandom.shuffle(arr);
        int count = 0;
        for (String s : arr) if (s != null) count++;
        assertEquals(5, count);
    }

    @Test
    public void shuffleSubarrayPreservesElements() {
        int[] arr = {0, 1, 2, 3, 4};
        StdRandom.shuffle(arr, 1, 4); // shuffle indices 1..3
        int sum = 0;
        for (int v : arr) sum += v;
        assertEquals(10, sum);
        assertEquals(0, arr[0]);  // index 0 untouched
        assertEquals(4, arr[4]);  // index 4 untouched
    }

    // --- permutation ---

    @Test
    public void permutationHasCorrectLength() {
        int[] perm = StdRandom.permutation(5);
        assertEquals(5, perm.length);
    }

    @Test
    public void permutationContainsAllValuesZeroToN() {
        int n = 6;
        int[] perm = StdRandom.permutation(n);
        int sum = 0;
        for (int v : perm) sum += v;
        assertEquals(0 + 1 + 2 + 3 + 4 + 5, sum);
    }

    @Test
    public void permutationSubRangeContainsCorrectValues() {
        int[] perm = StdRandom.permutation(10, 4);
        assertEquals(4, perm.length);
        // All values should be in [0, 10)
        for (int v : perm) {
            assertTrue(v >= 0 && v < 10);
        }
    }

    // --- discrete ---

    @Test
    public void discreteReturnsIndexInRange() {
        double[] weights = {0.1, 0.5, 0.3, 0.1};
        for (int i = 0; i < 100; i++) {
            int idx = StdRandom.discrete(weights);
            assertTrue(idx >= 0 && idx < weights.length);
        }
    }

    @Test
    public void discreteIntReturnsIndexInRange() {
        int[] weights = {1, 5, 3, 1};
        for (int i = 0; i < 100; i++) {
            int idx = StdRandom.discrete(weights);
            assertTrue(idx >= 0 && idx < weights.length);
        }
    }
}
