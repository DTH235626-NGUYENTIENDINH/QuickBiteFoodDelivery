package com.example.quickbuyfooddelivery;

public class NotificationOrder {
    private String status;
    private String message;
    private String time;
    private int logoResId;
    private int statusColor;

    public NotificationOrder(String status, String message, String time, int logoResId, int statusColor) {
        this.status = status;
        this.message = message;
        this.time = time;
        this.logoResId = logoResId;
        this.statusColor = statusColor;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getTime() { return time; }
    public int getLogoResId() { return logoResId; }
    public int getStatusColor() { return statusColor; }
}
