package com.example.quickbuyfooddelivery.secondary_profile_activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickbuyfooddelivery.DataBaseHelper;
import com.example.quickbuyfooddelivery.LoginActivity;
import com.example.quickbuyfooddelivery.ProfileActivity;
import com.example.quickbuyfooddelivery.R;

public class SettingActivity extends AppCompatActivity {

    private TextView tvUserName;
    DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ImageView imgArrowBack = findViewById(R.id.imgArrowBack);
        imgArrowBack.setOnClickListener(v -> {
            finish();
        });
        db = new DataBaseHelper(this);

        View btnOpenDialog = findViewById(R.id.layoutXoaTK);

        //Mở xác nhận xoá tài khoản
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(SettingActivity.this);
                dialog.setContentView(R.layout.dialog_delete);
                if (dialog.getWindow() != null)
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                //Nút huỷ
                View btnHuy = dialog.findViewById(R.id.btnHuy);
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        dialog.dismiss();
                    }
                });
                //Nút xoá
                View btnXoa = dialog.findViewById(R.id.btnXoa);
                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        //Xóa tài khoản
                        DeleteAccount();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //Mở hồ sơ
        View layoutHoSo = findViewById(R.id.layoutHoSo);
        layoutHoSo.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, UserInformationActivity.class);
                startActivity(intent);
            }
        });

        //Mở đổi mật khẩu
        View layoutDoiMatKhau = findViewById(R.id.layoutDoiMatKhau);
        layoutDoiMatKhau.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, PasswordChangeActivity.class);
                startActivity(intent);
            }
        });

        //Lấy tên người đùng
        tvUserName = findViewById(R.id.tvTenNguoiDungHienThi);
        android.content.SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String currentUsername = pref.getString("username", "");
        if (tvUserName != null) {
            tvUserName.setText(currentUsername);
        }
    }

    private void DeleteAccount() {
        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String currentUsername = pref.getString("username", "");
        db = new DataBaseHelper(this);
        if (db.deleteAccount(currentUsername)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(this, "Xoá tài khoản thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}