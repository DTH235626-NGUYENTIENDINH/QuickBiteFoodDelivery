package com.example.quickbuyfooddelivery.models;

public class Wishlist {
    private int imageResource;
    private String name;
    private String price;

    public Wishlist(int imageResource, String name, String price) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
    }

    public int getImageResource() { return imageResource; }
    public String getName() { return name; }
    public String getPrice() { return price; }
}
