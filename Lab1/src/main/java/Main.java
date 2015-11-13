import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pryaly on 10/16/2015.
 */
public class Main {
    public static void main(String[] args) throws ProductsException, IOException {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            throw new ProductsException("Input arguments should contain path to files with products and path to the file with colours");
        }
        Worker.generateFileWithResult(args[0], args[1]);

    }
}
