package main.java;

/**
 * Created by pryaly on 10/16/2015.
 */
public class Colour {

    private String name;
    private int price;

    public Colour(String name, int price) {
        this.name = name;
        this.price = price;
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
}
