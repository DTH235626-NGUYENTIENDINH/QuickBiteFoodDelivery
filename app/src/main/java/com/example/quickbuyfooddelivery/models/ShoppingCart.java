package com.example.quickbuyfooddelivery.models;

public class ShoppingCart {
    private int Hinh;
    private String Ten;
    private String Gia;
    int Num;
    public ShoppingCart(int Hinh, String Ten, String Gia, int Num) {
        this.Hinh = Hinh;
        this.Ten = Ten;
        this.Gia = Gia;
        this.Num = Num;
    }
    public int getHinh() {
        return Hinh;
    }
    public String getTen() {
        return Ten;
    }
    public String getGia() {
        return Gia;
    }
    public int getNum(){
        return Num;
    }
}