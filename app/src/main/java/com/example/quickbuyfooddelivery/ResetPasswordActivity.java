package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    Button btnRecoverPassword;
    TextView tvGoToRegister2, tvGoToLogin2;

    EditText edtNewPassword, edtConfirmPassword;
    private String userEmail; // Biến lưu email nhận được
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        db = new DataBaseHelper(this);

        // LẤY EMAIL TỪ INTENT
        userEmail = getIntent().getStringExtra("email");
        // Ánh xạ
        btnRecoverPassword = findViewById(R.id.btnRecoverPassword);
        tvGoToRegister2 = findViewById(R.id.tvGoToRegister2);
        tvGoToLogin2 = findViewById(R.id.tvGoToLogin2);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnRecoverPassword.setOnClickListener(v -> handlePasswordChange());

     
        tvGoToRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, RegisterVerifyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvGoToLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handlePasswordChange() {
        String newPass = edtNewPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            android.widget.Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPass.equals(confirmPass)) {
            edtConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        String HashPassword = HashUtils.hashPassword(newPass);
        if (userEmail != null && db.updatePasswordByEmail(userEmail, HashPassword)) {
            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show();

            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
}