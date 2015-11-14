import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by pryaly on 11/13/2015.
 */
@RunWith(Parameterized.class)
public class TestLab2 {

    IterativeParallelismImpl parallelism = new IterativeParallelismImpl();
    private static  final List<Integer> list = asList(2,8,4,5,1,3,2);
    private static  final List<Integer> list2 = asList(2,2,2);
    private final int threads;

    public TestLab2(int threads) {
        this.threads = threads;
    }


    @Parameterized.Parameters(name= "[number of threads]: {0}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{{1},{2},{3},{4},{5},{6},{7},{8},{9},{10}});
    }

    @Test
    public void TestMinimum() {
        assertEquals(1, (int) parallelism.minimum(threads, list, Comparator.<Integer>naturalOrder()));
        assertEquals(2, (int) parallelism.minimum(threads, list2, Comparator.<Integer>naturalOrder()));
    }

    @Test
    public void TestMaximum() {
        assertEquals(8, (int) parallelism.maximum(threads, list, Comparator.<Integer>naturalOrder()));
        assertEquals(2, (int) parallelism.maximum(threads, list2, Comparator.<Integer>naturalOrder()));
    }

    @Test
    public void TestAll() {
        assertFalse(parallelism.all(threads, list, Predicate.isEqual(2)));
        assertTrue(parallelism.all(threads, list2, Predicate.isEqual(2)));
    }

    @Test
    public void TestAny() {
        assertTrue(parallelism.any(threads, list, Predicate.isEqual(2)));
        assertTrue(parallelism.any(threads, list2, Predicate.isEqual(2)));
    }

    @Test
    public void TestFilter() {
        assertEquals(asList(2,2), parallelism.filter(threads, list, Predicate.isEqual(2)));
        assertEquals(asList(2,2,2), parallelism.filter(threads, list2, Predicate.isEqual(2)));
    }

    @Test
    public void TestMap() {
        assertEquals(asList("10", "1000", "100", "101", "1", "11", "10"),
                parallelism.map(threads, list, Integer::toBinaryString));
        assertEquals(asList("10", "10", "10"), parallelism.map(threads, list2, Integer::toBinaryString));
    }


}
