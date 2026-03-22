package com.example.quickbuyfooddelivery.models;

public class NotificationFood1
{
    private int logo;
    private String title;
    private String detail;
    private boolean isExpanded;

    public NotificationFood1(int logo, String title, String detail) {
        this.logo = logo;
        this.title = title;
        this.detail = detail;
        this.isExpanded = false;
    }
    public int getLogo() { return logo; }
    public String getTitle() { return title; }
    public String getDetail() { return detail; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}
