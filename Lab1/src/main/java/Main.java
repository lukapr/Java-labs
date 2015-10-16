import java.io.IOException;

/**
 * Created by pryaly on 10/16/2015.
 */
public class Main {
    public static void main(String[] args) throws Lab1Exception, IOException {
        if (args == null || args.length != 2) {
            throw new Lab1Exception("Input arguments should contain path to files with products and path to the file with colours");
        }
        Worker.generateFileWithResult(args[0], args[1]);
    }
}
