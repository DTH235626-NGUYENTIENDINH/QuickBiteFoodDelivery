package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.quickbuyfooddelivery.secondary_profile_activity.HistoryActivity;
import com.example.quickbuyfooddelivery.secondary_profile_activity.SettingActivity;
import com.example.quickbuyfooddelivery.secondary_profile_activity.UserInformationActivity;
import com.example.quickbuyfooddelivery.secondary_profile_activity.WishlistActivity;

public class ProfileActivity extends BaseActivity {
    private TextView tvName;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupTaskbar(R.id.btnUser);

        db = new DataBaseHelper(this);
        tvName = findViewById(R.id.tvName);

        //Mở cài đặt
        ConstraintLayout layoutCaiDat = findViewById(R.id.layoutCaiDat);
        layoutCaiDat.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        //Mở hồ sơ
        ConstraintLayout layoutHoSo = findViewById(R.id.layoutProfile);
        layoutHoSo.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UserInformationActivity.class);
            startActivity(intent);
        });

        //Mở lịch sử mua hàng
        View layoutLichSuMuaHang = findViewById(R.id.layoutLichSuMuaHang);
        layoutLichSuMuaHang.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Mở danh sách món yêu thích
        View layoutDanhSachMonYeuThich = findViewById(R.id.layoutDanhSachMonYeuThich);
        layoutDanhSachMonYeuThich.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, WishlistActivity.class);
            startActivity(intent);
        });

        //Đăng xuất
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> handleLogout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserDisplayName();
    }

    private void updateUserDisplayName() {
        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = pref.getString("username", "");

        if (!username.isEmpty()) {
            Cursor cursor = db.getUserInfo(username);
            if (cursor != null && cursor.moveToFirst()) {
                // Thử lấy full_name trước, nếu trống thì lấy username
                int fullNameIndex = cursor.getColumnIndex("full_name");
                String fullName = (fullNameIndex != -1) ? cursor.getString(fullNameIndex) : null;

                if (fullName == null || fullName.trim().isEmpty()) {
                    int usernameIndex = cursor.getColumnIndex("username");
                    fullName = (usernameIndex != -1) ? cursor.getString(usernameIndex) : username;
                }
                tvName.setText(fullName);
                cursor.close();
            } else {
                tvName.setText(username);
            }
        }
    }

    private void handleLogout() {
        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
