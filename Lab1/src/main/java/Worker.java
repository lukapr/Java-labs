package main.java;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by pryaly on 10/16/2015.
 */
public class Worker {

    private static ArrayList<Product> products = new ArrayList<Product>();
    private static ArrayList<Colour> colours = new ArrayList<Colour>();

    public static void generateResult(String pathToProducts, String pathToColours) throws IOException, Lab1Exception {
        getAllProducts(pathToProducts);
        if (products.size() == 0) {
            throw new Lab1Exception("All files in folder with products is broken. Path: " + pathToProducts);
        }
        getAllColours(pathToColours);
        Set<Product> resultSet = getResult();
        File file = new File("result.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter out = new PrintWriter(file.getAbsoluteFile());
        try {
            out.print(genarateStringForResult(resultSet));
        } finally {
            out.close();
        }
    }

    private static String genarateStringForResult(Set<Product> resultSet) {
        String result = "";
        for (Product product : resultSet) {
            result += product.toString();
        }
        return result;
    }

    private static Set<Product> getResult() {
        Set<Product> resultProducts = new TreeSet<Product>();
        if (colours.size() == 0) {
            return new TreeSet<Product>(products);
        }
        for (Product product : products) {
            for (Colour colour : colours) {
                Product resultProduct = new Product(product.getName() + "_" + colour.getName(),
                        product.getPrice() * colour.getPrice());
                resultProducts.add(resultProduct);
            }
        }
        return resultProducts;
    }

    private static void getAllColours(String pathToColours) throws Lab1Exception, IOException {
        File file = new File(pathToColours);
        if (!file.exists()) {
            return;
        }
        BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));

        try {
            String s;
            while ((s = in.readLine()) != null) {
                if (checkLineIsCorrect(s)) {
                    String[] split = s.split(" ");
                    Colour colour = new Colour(split[0], Integer.parseInt(split[1]));
                    if (!colours.contains(colour)) {
                        colours.add(colour);
                    }
                }
            }
        } finally {
            //Также не забываем закрыть файл
            in.close();
        }
    }


    private static void getAllProducts(String pathToProducts) throws Lab1Exception, IOException {
        File folder = new File(pathToProducts);
        if (!folder.exists()) {
            throw new Lab1Exception("Folder with products doesn't exist or path is incorrect. Path: " + pathToProducts);
        }
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            throw new Lab1Exception("Folder with products is empty. Path: " + pathToProducts);
        }
        for (File file : files) {
            appendProducts(getProductsFromFile(file));
        }
    }

    private static void appendProducts(ArrayList<Product> productsFromFile) {
        for (Product product : productsFromFile) {
            if (products.contains(product)) {
                Product oldProduct = products.get(products.indexOf(product));
                oldProduct.increaseCount();
                oldProduct.increasePrice(product.getPrice());
            } else {
                products.add(product);
            }
        }
    }

    private static ArrayList<Product> getProductsFromFile(File file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        ArrayList<Product> products = new ArrayList<Product>();

        try {
            String s;
            while ((s = in.readLine()) != null) {
                if (checkLineIsCorrect(s)) {
                    String[] split = s.split(" ");
                    Product product = new Product(split[0], Integer.parseInt(split[1]));
                    if (!products.contains(product)) {
                        products.add(product);
                    }
                }
            }
        } finally {
            //Также не забываем закрыть файл
            in.close();
        }
        return products;

    }

    private static boolean checkLineIsCorrect(String s) {
        String[] split = s.split(" ");
        if (split.length != 2) {
            return false;
        }
        try {
            Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
