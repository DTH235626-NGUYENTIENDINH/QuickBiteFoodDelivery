package com.example.quickbuyfooddelivery;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.widget.Button;
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
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                }
            });
        }

        if (btnFood != null) {
            btnFood.setOnClickListener(v -> {
                if (!(this instanceof FoodActivity)) {
                    startActivity(new Intent(this, FoodActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                }
            });
        }

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> {
                if (!(this instanceof NotificationActivity)) {
                    startActivity(new Intent(this, NotificationActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                }
            });
        }

        if (btnUser != null) {
            btnUser.setOnClickListener(v -> {
                if (!(this instanceof ProfileActivity)) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                }
            });
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (this instanceof Home) {
            showExitDialog();
        } else {
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    private void showExitDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_exit);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnThoat = dialog.findViewById(R.id.btnThoat);

        if (btnThoat != null) {
            btnThoat.setOnClickListener(v -> {
                dialog.dismiss();
                finishAffinity();
            });
        }

        if (btnHuy != null) {
            btnHuy.setOnClickListener(v -> {
                dialog.dismiss();
            });
        }

        dialog.show();
    }
}
