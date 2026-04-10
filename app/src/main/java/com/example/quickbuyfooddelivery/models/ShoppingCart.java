package com.example.quickbuyfooddelivery.models;

public class ShoppingCart {
    private int Hinh;
    private String Ten;
    private String Gia;
    private int Num;

    public ShoppingCart(int Hinh, String Ten, String Gia, int Num) {
        this.Hinh = Hinh;
        this.Ten = Ten;
        this.Gia = Gia;
        this.Num = Num;
    }

    public int getHinh() { return Hinh; }
    public String getTen() { return Ten; }
    public String getGia() { return Gia; }
    public int getNum() { return Num; }
    public void setNum(int num) { this.Num = num; }

    // Helper to get price as long for calculation
    public long getPriceValue() {
        try {
            return Long.parseLong(Gia.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
