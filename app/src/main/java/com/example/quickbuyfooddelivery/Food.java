package com.example.quickbuyfooddelivery;

public class Food {
    private String name;
    private String price;
    private int imageResId;
    private String category;
    private String imageName;

    public Food(String name, String price, int imageResId, String category) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.category = category;
    }

    public Food(String name, String price, int imageResId, String category, String imageName) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.category = category;
        this.imageName = imageName;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }
    public String getImageName() { return imageName; }
}
