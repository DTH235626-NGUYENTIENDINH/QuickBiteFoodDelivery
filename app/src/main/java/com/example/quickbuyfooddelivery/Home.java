package com.example.quickbuyfooddelivery;

import android.os.Bundle;

public class Home extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupTaskbar(R.id.btnHome);
    }
}
