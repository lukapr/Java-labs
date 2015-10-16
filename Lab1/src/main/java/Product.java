/**
 * Created by pryaly on 10/16/2015.
 */
public class Product implements Comparable<Product> {
    private String name;
    private double price;
    private int count;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
        count = 1;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
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

    public void increasePrice(double price) {
        this.price += price;
    }

    @Override
    public String toString() {
        return name + " " + getResultPrice() + "\n";
    }

    public Double getResultPrice() {
        return price / count;
    }

    @Override
    public int compareTo(Product o) {
        return this.name.compareTo(o.name);
    }
}
