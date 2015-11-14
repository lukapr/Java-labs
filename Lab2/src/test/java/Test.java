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
        for (int i = 0; i < 100000;i++) {
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
        long startTime = System.currentTimeMillis();
        List mappedList1Thread = parallelism.map(1, res, function);
        System.out.println("Time for Map with " + 1 + " threads is: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        List mappedList3Thread = parallelism.map(3, res, function);
        System.out.println("Time for Map with " + 3 + " threads is: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("Filter: " + parallelism.filter(1, mappedList1Thread, predicateLess10));
        System.out.println("Time for Filter with " + 1 + " threads is: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        System.out.println("Filter: " + parallelism.filter(3, mappedList3Thread, predicateLess10));
        System.out.println("Time for Filter with " + 3 + " threads is: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("All: " + parallelism.all(1, mappedList1Thread, predicateMore0));
        System.out.println("Time for All with " + 1 + " threads is: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        System.out.println("All: " + parallelism.all(3, mappedList3Thread, predicateMore0));
        System.out.println("Time for All with " + 3 + " threads is: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("Any: " + parallelism.any(1, mappedList1Thread, Predicate.isEqual(0)));
        System.out.println("Time for Any with " + 1 + " threads is: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("Any: " + parallelism.any(3, mappedList3Thread, Predicate.isEqual(0)));
        System.out.println("Time for Any with " + 3 + " threads is: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("Minimum: " + parallelism.minimum(1, mappedList1Thread, comparator));
        System.out.println("Time for Min with " + 1 + " threads is: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        System.out.println("Minimum: " + parallelism.minimum(3, mappedList3Thread, comparator));
        System.out.println("Time for Min with " + 3 + " threads is: " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println("Maximum: " + parallelism.maximum(1, mappedList1Thread, comparator));
        System.out.println("Time for Max with " + 1 + " threads is: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        System.out.println("Maximum: " + parallelism.maximum(3, mappedList3Thread, comparator));
        System.out.println("Time for Max with " + 3 + " threads is: " + (System.currentTimeMillis() - startTime));
    }
}
