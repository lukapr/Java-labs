import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 *
 * Created by pryaly on 11/13/2015.
 */
public class IterativeParallelismImpl implements IterativeParallelism {
    @Override
    public <T> T minimum(int threads, List<T> list, Comparator<T> comparator) {
        return applyToAll(threads, list,
                sublist -> sublist.stream().min(comparator),
                result -> result.stream()
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .min(comparator)
        .orElse(null));
    }

    @Override
    public <T> T maximum(int threads, List<T> list, Comparator<T> comparator) {
        return applyToAll(threads, list,
                sublist -> sublist.stream().max(comparator),
                result -> result.stream()
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .max(comparator)
                        .orElse(null));
    }

    @Override
    public <T> boolean all(int threads, List<T> list, Predicate<T> predicate) {
        return applyToAll(threads, list,
                sublist -> sublist.stream().allMatch(predicate),
                result -> result.stream().allMatch(temp -> temp));
    }

    @Override
    public <T> boolean any(int threads, List<T> list, Predicate<T> predicate) {
        return applyToAll(threads, list,
                sublist -> sublist.stream().anyMatch(predicate),
                result -> result.stream().anyMatch(temp -> temp));
    }

    @Override
    public <T> List<T> filter(int threads, List<T> list, Predicate<T> predicate) {
        return applyToAll(threads, list,
                sublist -> sublist.stream().filter(predicate).collect(toList()),
                result -> result.stream().flatMap(Collection::stream).collect(toList()));
    }

    @Override
    public <T, R> List<R> map(int threads, List<T> list, Function<T, R> function) {
        return applyToAll(threads, list,
                sublist -> sublist.stream().map(function).collect(toList()),
                result -> result.stream().flatMap(Collection::stream).collect(toList()));
    }

    private <T, U, R> R applyToAll(int threads, List<T> list, Function<List<T>, U> functionForThread, Function<List<U>, R> functionForAll) {
        return functionForAll.apply(applyToThread(getListsForThreads(threads, list), functionForThread));
    }

    private <T, U> List<U> applyToThread(Collection<T> listsForThreads, Function<T, U> functionForThread) {
        Collection<Worker<T, U>> workers = listsForThreads.stream()
                .map(list -> new Worker<>(list, functionForThread))
                .collect(toList());

        Collection<Thread> threads = workers.stream()
                .map(Thread::new)
                .collect(toList());

        threads.stream().forEach(Thread::start);
        threads.stream().forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return workers.stream()
                .map(worker -> worker.output)
                .collect(toList());
    }

    private <T> List<List<T>> getListsForThreads(int threads, List<T> list) {
        List<List<T>> result = new ArrayList<>();
        int itemsInThread = list.size() / threads;
        if (itemsInThread > 0) {
            for (int i = 0; i < threads - 1; i++) {
                result.add(list.subList(i * itemsInThread, (i + 1) * itemsInThread));
            }
        }
        result.add(list.subList((threads - 1) * itemsInThread, list.size()));
        return result;
    }

    private class Worker<T, U> implements Runnable {

        private T input;
        private Function<T, U> functionForThread;
        private U output;

        public Worker(T input, Function<T, U> functionForThread) {
            this.input = input;
            this.functionForThread = functionForThread;
        }

        @Override
        public void run() {
            output = functionForThread.apply(input);
        }
    }
}
