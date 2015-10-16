package main.java;

/**
 * Created by pryaly on 10/16/2015.
 */
public class Product implements Comparable<Product> {
    private String name;
    private int price;
    private int count;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
        count = 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return !(name != null ? !name.equals(product.name) : product.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public void increaseCount() {
        count++;
    }

    public void increasePrice(int price) {
        this.price += price;
    }

    @Override
    public String toString() {
//        return "Product{" +
//                "name='" + name + '\'' +
//                ", price=" + price +
//                ", count=" + count +
//                ", totalPrice=" + (double) price / count +
//                "}\n";
        return name + " " + getResultPrice() + "\n";
    }

    private String getResultPrice() {
//        double resultPrice = (double) price / count;
        Double resultPrice = price % count == 0 ? price / count : (double) price / count;
        return resultPrice.toString();
    }

    @Override
    public int compareTo(Product o) {
        return this.name.compareTo(o.name);
    }
}
