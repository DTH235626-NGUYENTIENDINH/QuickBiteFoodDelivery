package com.example.quickbuyfooddelivery;

import android.os.Bundle;

public class NotificationActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setupTaskbar(R.id.btnNotification);
    }
}
