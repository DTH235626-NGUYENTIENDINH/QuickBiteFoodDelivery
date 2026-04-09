package com.example.quickbuyfooddelivery;

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

        int colorSelected = 0xFFFFB300; 
        int colorUnselected = 0xFF880E0F;

        if (btnHome != null) btnHome.setImageTintList(ColorStateList.valueOf(activeId == R.id.btnHome ? colorSelected : colorUnselected));
        if (btnFood != null) btnFood.setImageTintList(ColorStateList.valueOf(activeId == R.id.btnFood ? colorSelected : colorUnselected));
        if (btnNotification != null) btnNotification.setImageTintList(ColorStateList.valueOf(activeId == R.id.btnNotification ? colorSelected : colorUnselected));
        if (btnUser != null) btnUser.setImageTintList(ColorStateList.valueOf(activeId == R.id.btnUser ? colorSelected : colorUnselected));

        if (btnHome != null) btnHome.setOnClickListener(v -> navigateTo(Home.class));
        if (btnFood != null) btnFood.setOnClickListener(v -> navigateTo(FoodActivity.class));
        if (btnNotification != null) btnNotification.setOnClickListener(v -> navigateTo(NotificationActivity.class));
        if (btnUser != null) btnUser.setOnClickListener(v -> navigateTo(ProfileActivity.class));
    }

    private void navigateTo(Class<?> target) {
        if (!this.getClass().equals(target)) {
            Intent intent = new Intent(this, target);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (this instanceof Home) {
            showExitDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showExitDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_exit);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        Button btnThoat = dialog.findViewById(R.id.btnThoat);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        if (btnThoat != null) btnThoat.setOnClickListener(v -> finishAffinity());
        dialog.show();
        if(btnHuy != null){
            btnHuy.setOnClickListener(v -> dialog.dismiss());
        }
    }
}
