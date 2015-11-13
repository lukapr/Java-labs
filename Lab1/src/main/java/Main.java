import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pryaly on 10/16/2015.
 */
public class Main {
    public static void main(String[] args) throws ProductsException, IOException {
//        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
//            throw new ProductsException("Input arguments should contain path to files with products and path to the file with colours");
//        }
//        Worker.generateFileWithResult(args[0], args[1]);

        List<Integer> list = new ArrayList<>();
        int threads = 2;
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        List<List<Integer>> result = new ArrayList<>();
        int itemsInThread = list.size() / threads;
        for (int i = 0; i < threads - 1; i++) {
            result.add(list.subList(i * itemsInThread, i * itemsInThread + itemsInThread));
        }
        result.add(list.subList((threads - 1) * itemsInThread, list.size()));
int i = 0;
    }
}
