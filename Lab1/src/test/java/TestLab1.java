import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by pryaly on 10/16/2015.
 */
public class TestLab1 {

    private final String PRODUCT_FOLDER_NOT_EXISTS = "Folder with products doesn't exist or path is incorrect. Path: ";
    private final String PRODUCT_FOLDER_IS_EMPTY = "Folder with products is empty. Path: ";
    private final String FILES_IN_PRODUCT_FOLDER_IS_BROKEN = "All files in folder with products is broken. Path: ";

    @Test
    public void TestCheckResultFile() throws IOException{
        String pathProducts = new File(TestLab1.class.getResource("/products").getFile()).getAbsolutePath();
        String pathColours = new File(TestLab1.class.getResource("/colours.txt").getFile()).getAbsolutePath();
        Worker.generateFileWithResult(pathProducts, pathColours);
        File resultFile = new File("result.txt");
        assertTrue(resultFile.exists());
    }

    @Test
    public void TestMainFunctionality() throws IOException {
        String pathProducts = new File(TestLab1.class.getResource("/products").getFile()).getAbsolutePath();
        String pathColours = new File(TestLab1.class.getResource("/colours.txt").getFile()).getAbsolutePath();
        Set<Product> result = Worker.generateResult(pathProducts, pathColours);
        final List<Product> expectedResults = new ArrayList<Product>();
        expectedResults.add(new Product("Book_Green", 875));
        expectedResults.add(new Product("Book_Red", 1250));
        expectedResults.add(new Product("Computer_Green", 490));
        expectedResults.add(new Product("Computer_Red", 700));
        expectedResults.add(new Product("Pen_Green", 700));
        expectedResults.add(new Product("Pen_Red", 1000));
        expectedResults.add(new Product("Pencil_Green", 1750));
        expectedResults.add(new Product("Pencil_Red", 2500));
        expectedResults.add(new Product("Table_Green", 913.5));
        expectedResults.add(new Product("Table_Red", 1305));
        checkResult(expectedResults, result);
    }

    @Test
    public void TestWrongColorFile() throws IOException {
        String pathProducts = new File(TestLab1.class.getResource("/products").getFile()).getAbsolutePath();
        String pathColours = new File(TestLab1.class.getResource("/colours.txt").getFile()).getAbsolutePath().replace(".txt" , "Wrong.txt");
        Set<Product> result = Worker.generateResult(pathProducts, pathColours);
        final List<Product> expectedResults = new ArrayList<Product>();
        expectedResults.add(new Product("Book", 125));
        expectedResults.add(new Product("Computer", 70));
        expectedResults.add(new Product("Pen", 100));
        expectedResults.add(new Product("Pencil", 250));
        expectedResults.add(new Product("Table", 130.5));
        checkResult(expectedResults, result);
    }

    @Test
    public void TestNoProductFolder() throws IOException{
        String pathProducts = new File(TestLab1.class.getResource("/products").getFile()).getAbsolutePath() + "Wrong";
        String pathColours = new File(TestLab1.class.getResource("/colours.txt").getFile()).getAbsolutePath();
        try {
            Worker.generateResult(pathProducts, pathColours);
        } catch (ProductsException e) {
            e.printStackTrace();
            assertEquals(PRODUCT_FOLDER_NOT_EXISTS + pathProducts, e.getMessage());
        }
    }

    @Test
    public void TestBrokenFilesInProductFolder() throws IOException{
        String pathProducts = new File(TestLab1.class.getResource("/productsBroken").getFile()).getAbsolutePath();
        String pathColours = new File(TestLab1.class.getResource("/colours.txt").getFile()).getAbsolutePath();
        try {
            Worker.generateResult(pathProducts, pathColours);
        } catch (ProductsException e) {
            e.printStackTrace();
            assertEquals(FILES_IN_PRODUCT_FOLDER_IS_BROKEN + pathProducts, e.getMessage());
        }
    }

    @Test
    public void TestEmptyProductFolder() throws IOException{
        String pathProducts = new File(TestLab1.class.getResource("/products").getFile()).getAbsolutePath() + "Empty";
        String pathColours = new File(TestLab1.class.getResource("/colours.txt").getFile()).getAbsolutePath();
        try {
            Worker.generateResult(pathProducts, pathColours);
        } catch (ProductsException e) {
            e.printStackTrace();
            assertEquals(PRODUCT_FOLDER_IS_EMPTY + pathProducts, e.getMessage());
        }
    }

    private void checkResult(List<Product> expectedResults, Set<Product> result) {
        int i = 0;
        for (Product product : result) {
            assertEquals(expectedResults.get(i).getName(), product.getName());
            assertEquals(product.getName(), expectedResults.get(i).getResultPrice(), product.getResultPrice());
            i++;
        }
    }
}
