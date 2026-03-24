package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.quickbuyfooddelivery.secondary_profile_activity.HistoryActivity;
import com.example.quickbuyfooddelivery.secondary_profile_activity.SettingActivity;
import com.example.quickbuyfooddelivery.secondary_profile_activity.UserInformationActivity;

public class ProfileActivity extends BaseActivity {
    private ConstraintLayout layoutCaiDat;
    private ConstraintLayout layoutHoSo;

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
    }
}
