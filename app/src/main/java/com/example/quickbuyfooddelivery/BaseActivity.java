package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected ImageView btnHome, btnFood, btnNotification, btnUser;

    protected void setupTaskbar(int activeId) {
        btnHome = findViewById(R.id.btnHome);
        btnFood = findViewById(R.id.btnFood);
        btnNotification = findViewById(R.id.btnNotification);
        btnUser = findViewById(R.id.btnUser);

        int colorSelected = 0xFFFFB300; // Vàng
        int colorUnselected = 0xFF880E0F; // Màu gốc của bạn

        // Reset colors
        btnHome.setImageTintList(ColorStateList.valueOf(colorUnselected));
        btnFood.setImageTintList(ColorStateList.valueOf(colorUnselected));
        btnNotification.setImageTintList(ColorStateList.valueOf(colorUnselected));
        btnUser.setImageTintList(ColorStateList.valueOf(colorUnselected));

        // Highlight active
        if (activeId == R.id.btnHome) btnHome.setImageTintList(ColorStateList.valueOf(colorSelected));
        if (activeId == R.id.btnFood) btnFood.setImageTintList(ColorStateList.valueOf(colorSelected));
        if (activeId == R.id.btnNotification) btnNotification.setImageTintList(ColorStateList.valueOf(colorSelected));
        if (activeId == R.id.btnUser) btnUser.setImageTintList(ColorStateList.valueOf(colorSelected));

        btnHome.setOnClickListener(v -> {
            if (!(this instanceof Home)) {
                startActivity(new Intent(this, Home.class));
                finish();
            }
        });

        btnFood.setOnClickListener(v -> {
            if (!(this instanceof MainActivity)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });

        btnNotification.setOnClickListener(v -> {
            if (!(this instanceof NotificationActivity)) {
                startActivity(new Intent(this, NotificationActivity.class));
                finish();
            }
        });

        btnUser.setOnClickListener(v -> {
            if (!(this instanceof ProfileActivity)) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
            }
        });
    }
}
