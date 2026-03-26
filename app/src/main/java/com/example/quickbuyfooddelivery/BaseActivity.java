package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected ImageButton btnHome, btnFood, btnNotification, btnUser;

    protected void setupTaskbar(int activeId) {
        btnHome = findViewById(R.id.btnHome);
        btnFood = findViewById(R.id.btnFood);
        btnNotification = findViewById(R.id.btnNotification);
        btnUser = findViewById(R.id.btnUser);

        int colorSelected = 0xFFFFB300; // Vàng
        int colorUnselected = 0xFF880E0F; // Màu gốc

        // Reset colors
        if (btnHome != null) btnHome.setImageTintList(ColorStateList.valueOf(colorUnselected));
        if (btnFood != null) btnFood.setImageTintList(ColorStateList.valueOf(colorUnselected));
        if (btnNotification != null) btnNotification.setImageTintList(ColorStateList.valueOf(colorUnselected));
        if (btnUser != null) btnUser.setImageTintList(ColorStateList.valueOf(colorUnselected));

        // Highlight active
        if (activeId == R.id.btnHome && btnHome != null) btnHome.setImageTintList(ColorStateList.valueOf(colorSelected));
        if (activeId == R.id.btnFood && btnFood != null) btnFood.setImageTintList(ColorStateList.valueOf(colorSelected));
        if (activeId == R.id.btnNotification && btnNotification != null) btnNotification.setImageTintList(ColorStateList.valueOf(colorSelected));
        if (activeId == R.id.btnUser && btnUser != null) btnUser.setImageTintList(ColorStateList.valueOf(colorSelected));

        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {
                if (!(this instanceof Home)) {
                    startActivity(new Intent(this, Home.class));
                    finish();
                }
            });
        }

        if (btnFood != null) {
            btnFood.setOnClickListener(v -> {
                if (!(this instanceof FoodActivity)) {
                    startActivity(new Intent(this, FoodActivity.class));
                    finish();
                }
            });
        }

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> {
                if (!(this instanceof NotificationActivity)) {
                    startActivity(new Intent(this, NotificationActivity.class));
                    finish();
                }
            });
        }

        if (btnUser != null) {
            btnUser.setOnClickListener(v -> {
                if (!(this instanceof ProfileActivity)) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    finish();
                }
            });
        }
    }
}
