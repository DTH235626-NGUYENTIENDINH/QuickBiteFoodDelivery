package com.example.quickbuyfooddelivery.models;

public class HistoryFood {
    private int imgDoAn;
    private String tenMon;
    private String trangThai;
    private String soLuong;
    private String donGia;
    private String tongTien;
    private String ngayMua;


    public HistoryFood(int imgDoAn, String tenMon, String trangThai, String soLuong, String donGia, String tongTien, String ngayMua) {
        this.imgDoAn = imgDoAn;
        this.tenMon = tenMon;
        this.trangThai = trangThai;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.tongTien = tongTien;
        this.ngayMua = ngayMua;
    }
    public int getImgDoAn() { return imgDoAn; }
    public String getTenMon() { return tenMon; }
    public String getTrangThai() { return trangThai; }
    public String getSoLuong() { return soLuong; }
    public String getDonGia() { return donGia; }
    public String getTongTien() { return tongTien; }
    public String getNgayMua() { return ngayMua; }
}
