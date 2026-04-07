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

        // Mở cài đặt
        findViewById(R.id.layoutCaiDat).setOnClickListener(v -> 
            startActivity(new Intent(ProfileActivity.this, SettingActivity.class)));

        // Mở hồ sơ
        findViewById(R.id.layoutProfile).setOnClickListener(v -> 
            startActivity(new Intent(ProfileActivity.this, UserInformationActivity.class)));

        // Mở lịch sử mua hàng
        findViewById(R.id.layoutLichSuMuaHang).setOnClickListener(v -> 
            startActivity(new Intent(ProfileActivity.this, HistoryActivity.class)));

        // Mở danh sách món yêu thích (Đổi sang FavoriteActivity)
        findViewById(R.id.layoutDanhSachMonYeuThich).setOnClickListener(v -> 
            startActivity(new Intent(ProfileActivity.this, FavoriteActivity.class)));

        // Đăng xuất
        findViewById(R.id.btnLogout).setOnClickListener(v -> handleLogout());
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
                int fullNameIndex = cursor.getColumnIndex("full_name");
                String fullName = (fullNameIndex != -1) ? cursor.getString(fullNameIndex) : null;
                if (fullName == null || fullName.trim().isEmpty()) {
                    fullName = username;
                }
                tvName.setText(fullName);
                cursor.close();
            }
        }
    }

    private void handleLogout() {
        getSharedPreferences("UserSession", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
