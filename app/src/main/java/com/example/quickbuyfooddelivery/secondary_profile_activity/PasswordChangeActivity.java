package com.example.quickbuyfooddelivery.secondary_profile_activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quickbuyfooddelivery.DataBaseHelper;
import com.example.quickbuyfooddelivery.HashUtils;
import com.example.quickbuyfooddelivery.R;

public class PasswordChangeActivity extends AppCompatActivity {
    private EditText txtMatKhauCu, txtMatKhauMoi, txtMatKhauXacNhan;
    private Button btnLuu;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        ImageView imgArrowBack = findViewById(R.id.imgArrowBack);
        imgArrowBack.setOnClickListener(v -> {
            finish();
        });
        db = new DataBaseHelper(this);
        txtMatKhauCu = findViewById(R.id.txtMatKhauCu);
        txtMatKhauMoi = findViewById(R.id.txtMatKhauMoi);
        txtMatKhauXacNhan = findViewById(R.id.txtMatKhauXacNhan);
        btnLuu = findViewById(R.id.btnLuu);

        findViewById(R.id.imgArrowBack).setOnClickListener(v -> finish());

        btnLuu.setOnClickListener(v -> handlePasswordChange());

    }

    private void handlePasswordChange() {
        String oldPass = txtMatKhauCu.getText().toString().trim();
        String newPass = txtMatKhauMoi.getText().toString().trim();
        String confirmPass = txtMatKhauXacNhan.getText().toString().trim();

        //Kiểm tra trống
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            android.widget.Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        //Kiểm tra mật khẩu mới khớp nhau
        if (!newPass.equals(confirmPass)) {
            txtMatKhauXacNhan.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        // 3. Lấy userId từ Session
        android.content.SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        // MÃ HÓA mật khẩu cũ để đi so sánh với DB
        String hashedOldPass = com.example.quickbuyfooddelivery.HashUtils.hashPassword(oldPass);

        if (db.checkOldPassword(userId, hashedOldPass)) {
            // MẬT KHẨU CŨ ĐÚNG -> MÃ HÓA mật khẩu mới để lưu
            String hashedNewPass = com.example.quickbuyfooddelivery.HashUtils.hashPassword(newPass);

            if (db.updatePassword(userId, hashedNewPass)) {
                android.widget.Toast.makeText(this, "Đổi mật khẩu thành công!", android.widget.Toast.LENGTH_SHORT).show();
                finish(); // Đóng trang quay về Profile
            } else {
                android.widget.Toast.makeText(this, "Lỗi cập nhật Database", android.widget.Toast.LENGTH_SHORT).show();
            }
        } else {
            txtMatKhauCu.setError("Mật khẩu cũ không chính xác");
        }
    }
}