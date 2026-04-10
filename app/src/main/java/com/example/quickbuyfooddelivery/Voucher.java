package com.example.quickbuyfooddelivery;

public class Voucher {
    public int    id;
    public String code;
    public int    discountPercent;
    public double maxDiscount;
    public double minOrder;
    public String expiryDate;
    public int    usageLimit;
    public int    usedCount;

    // Constructor đầy đủ — dùng cho DB
    public Voucher(int id, String code, int discountPercent,
                   double maxDiscount, double minOrder,
                   String expiryDate, int usageLimit, int usedCount) {
        this.id              = id;
        this.code            = code;
        this.discountPercent = discountPercent;
        this.maxDiscount     = maxDiscount;
        this.minOrder        = minOrder;
        this.expiryDate      = expiryDate;
        this.usageLimit      = usageLimit;
        this.usedCount       = usedCount;
    }

    // Constructor cũ 5 tham số — dùng cho ShoppingCartActivity
    public Voucher(int id, String code, int discountPercent,
                   double maxDiscount, double minOrder) {
        this(id, code, discountPercent, maxDiscount, minOrder,
             null, 999, 0);
    }

    // Getters — để tương thích với ShoppingCartActivity
    public int    getId()              { return id; }
    public String getCode()            { return code; }
    public int    getDiscountPercent() { return discountPercent; }
    public double getMaxDiscount()     { return maxDiscount; }
    public double getMinOrder()        { return minOrder; }

    // Hiển thị
    public String getTitle() {
        return "GIẢM " + discountPercent + "%";
    }

    public String getDescription() {
        return "Đơn từ " + String.format("%,.0f", minOrder) + "đ" +
               " - Giảm tối đa " + String.format("%,.0f", maxDiscount) + "đ";
    }

    public boolean isAvailable() {
        return usedCount < usageLimit;
    }

    @Override
    public String toString() {
        if (code != null) return code + " - Giảm " + discountPercent + "%";
        return "";
    }
}