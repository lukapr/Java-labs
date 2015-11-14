import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by pryaly on 11/14/2015.
 */
public class Test {

    @org.junit.Test
    public void TestTest() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000000;i++) {
            list.add(0);
        }
        list.set(0, -1);
        list.set(99999, 1);
        List<Integer> res = new ArrayList<>();
        res.addAll(list);
        res.addAll(list);
        res.addAll(list);
        IterativeParallelismImpl parallelism = new IterativeParallelismImpl();
        Comparator<Integer> comparator = Integer::compareTo;
        Function<Integer, Integer> function = integer -> integer += 10;
        Predicate predicateLess10 = o -> (int) o < 10;
        Predicate predicateMore0 = o -> (int) o > 0;
        for (int thread = 1; thread <= 100; thread+=99) {
            long startTime = System.currentTimeMillis();
            List mappedList = parallelism.map(thread, res, function);
            System.out.println("Time for Map with " + thread + " threads is: " + (System.currentTimeMillis() - startTime));

            startTime = System.currentTimeMillis();
            System.out.println("Filter: " + parallelism.filter(thread, mappedList, predicateLess10));
            System.out.println("Time for Filter with " + thread + " threads is: " + (System.currentTimeMillis() - startTime));

            startTime = System.currentTimeMillis();
            System.out.println("All: " + parallelism.all(thread, mappedList, predicateMore0));
            System.out.println("Time for All with " + thread + " threads is: " + (System.currentTimeMillis() - startTime));

            startTime = System.currentTimeMillis();
            System.out.println("Any: " + parallelism.any(thread, mappedList, Predicate.isEqual(0)));
            System.out.println("Time for Any with " + thread + " threads is: " + (System.currentTimeMillis() - startTime));

            startTime = System.currentTimeMillis();
            System.out.println("Minimum: " + parallelism.minimum(thread, mappedList, comparator));
            System.out.println("Time for Min with " + thread + " threads is: " + (System.currentTimeMillis() - startTime));

            startTime = System.currentTimeMillis();
            System.out.println("Maximum: " + parallelism.maximum(thread, mappedList, comparator));
            System.out.println("Time for Max with " + thread + " threads is: " + (System.currentTimeMillis() - startTime));
        }
        int i = 0;
    }
}
