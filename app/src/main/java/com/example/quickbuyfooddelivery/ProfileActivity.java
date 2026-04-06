package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private ConstraintLayout layoutCaiDat;
    private ConstraintLayout layoutHoSo;
    private TextView tvName;
    private DataBaseHelper db;

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupTaskbar(R.id.btnUser);
        //Mở cài đặt
        layoutCaiDat = findViewById(R.id.layoutCaiDat);
        layoutCaiDat.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        //Mở hồ sơ
        layoutHoSo = findViewById(R.id.layoutProfile);
        layoutHoSo.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UserInformationActivity.class);
                startActivity(intent);
            }
        });
        //Mở lịch sở mua hàng
        View layoutLichSuMuaHang = findViewById(R.id.layoutLichSuMuaHang);
        layoutLichSuMuaHang.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        // Mở danh sách món yêu thích
        View layoutDanhSachMonYeuThich = findViewById(R.id.layoutDanhSachMonYeuThich);
        layoutDanhSachMonYeuThich.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, WishlistActivity.class);
                startActivity(intent);
            }
        });
        // Hiển thị tên người dùng đã đăng nhập
        tvName = findViewById(R.id.tvName); // Tên ID của cái TextView chứa họ tên
        db = new DataBaseHelper(this);

        //Đằn xuất
        android.widget.Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });

    }
    // DÙNG HÀM onResume ĐỂ LUÔN CẬP NHẬT TÊN MỚI NHẤT
    @Override
    protected void onResume() {
        super.onResume();

        // lấy username người dùng hiện tại
        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String currentUsername = pref.getString("username", "");

        if (!currentUsername.isEmpty()) {
            // Dùng lại hàm getUserInfo
            android.database.Cursor cursor = db.getUserInfo(currentUsername);

            if (cursor != null && cursor.moveToFirst()) {
                // Ở hàm getUserInfo, full_name nằm ở cột đầu tiên (vị trí số 0)
                String fullName = cursor.getString(0);

                if (fullName != null && !fullName.isEmpty()) {
                    tvName.setText(fullName);
                } else {
                    tvName.setText("Người dùng");
                }
                cursor.close();
            }
        }
    }
    //Xử lý đăng xuất
    private void handleLogout() {
        android.content.SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
        android.content.Intent intent = new android.content.Intent(this, LoginActivity.class);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
