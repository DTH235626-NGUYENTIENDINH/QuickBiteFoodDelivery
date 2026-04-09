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

    // Tiêu đề hiển thị
    public String getTitle() {
        return "GIẢM " + discountPercent + "%";
    }

    // Mô tả hiển thị
    public String getDescription() {
        return "Đơn từ " + String.format("%,.0f", minOrder) + "đ" +
               " - Giảm tối đa " + String.format("%,.0f", maxDiscount) + "đ";
    }

    // Còn lượt dùng không
    public boolean isAvailable() {
        return usedCount < usageLimit;
    }
}