package com.example.quickbuyfooddelivery;

public class Voucher {
    private int id;
    private String code;
    private int discountPercent;
    private double maxDiscount;
    private double minOrder;

    private String title;
    private String description;
    private int imageResId;

    public Voucher(int id, String code, int discountPercent, double maxDiscount, double minOrder) {
        this.id = id;
        this.code = code;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
        this.minOrder = minOrder;
    }

    public Voucher(String title, String description, int imageResId) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
    }

    public int getId() { return id; }
    public String getCode() { return code; }
    public int getDiscountPercent() { return discountPercent; }
    public double getMaxDiscount() { return maxDiscount; }
    public double getMinOrder() { return minOrder; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }

    @Override
    public String toString() {
        if (id == -1) return code;
        if (code != null) return code + " - Giảm " + discountPercent + "%";
        return title;
    }
}
