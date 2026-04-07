package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnVerifyForgot;
    TextView tvGoToRegister, tvGoToLogin, tvSendCodeForgot;

    EditText edtEmailForgot, edtVerificationCodeForgot;
    private String sentCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // 1. Ánh xạ từ giao diện
        btnVerifyForgot = findViewById(R.id.btnVerifyForgot);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
        tvSendCodeForgot = findViewById(R.id.tvSendCodeForgot);
        edtEmailForgot = findViewById(R.id.edtEmailForgot);
        edtVerificationCodeForgot = findViewById(R.id.edtVerificationCodeForgot);

        // 2. Sự kiện Click

        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, RegisterVerifyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (tvSendCodeForgot != null) {
            tvSendCodeForgot.setOnClickListener(v -> sendCode());
        }
        if (btnVerifyForgot != null) {
            btnVerifyForgot.setOnClickListener(v -> verifyCode());
        }
    }

    private void verifyCode() {
        String email = edtEmailForgot.getText().toString().trim();
        String inputCode = edtVerificationCodeForgot.getText().toString().trim();

        if (sentCode.isEmpty()) {
            Toast.makeText(this, "Vui lòng lấy mã xác minh trước!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(inputCode)) {
            edtVerificationCodeForgot.setError("Nhập mã OTP");
            return;
        }

        // So sánh mã người dùng nhập với mã đã gửi
        if (!inputCode.equals(sentCode)) {
            edtVerificationCodeForgot.setError("Mã xác minh không chính xác!");
            return;
        }

        // --- XÁC MINH THÀNH CÔNG ---
        Toast.makeText(this, "Xác minh thành công!", Toast.LENGTH_SHORT).show();

        // Chuyển sang RegisterActivity và mang theo Email đã xác minh
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    private void sendCode() {
        String email = edtEmailForgot.getText().toString().trim();

        // 1. Kiểm tra Email hợp lệ
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        DataBaseHelper db = new DataBaseHelper(this);
        if (!db.isEmailExists(email)) {
            Toast.makeText(this, "Email này chưa được đăng ký tài khoản!", Toast.LENGTH_LONG).show();
            return;
        }

        // Vô hiệu hóa nút để tránh khách bấm liên tục khi đang gửi
        tvSendCodeForgot.setEnabled(false);
        tvSendCodeForgot.setText("Đang gửi...");

        // 2. Tạo mã 6 số ngẫu nhiên
        sentCode = String.valueOf((int) ((Math.random() * 900000) + 100000));

        // 3. Gửi Email thật (Chạy luồng phụ Thread)
        new Thread(() -> {
            try {
                // Gọi tới class tiện ích EmailSender của bạn
                EmailSender.sendOTP(email, sentCode);

                // Quay lại luồng chính để cập nhật giao diện
                runOnUiThread(() -> {
                    Toast.makeText(this, "Mã xác minh đã được gửi tới email của bạn!", Toast.LENGTH_LONG).show();
                    tvSendCodeForgot.setEnabled(true);
                    tvSendCodeForgot.setText("Gửi lại mã");
                });

            } catch (Exception e) {
                // Xử lý khi có lỗi (Sai mật khẩu ứng dụng, mất mạng...)
                runOnUiThread(() -> {
                    Log.e("OTP_ERROR", "Lỗi: " + e.getMessage());
                    Toast.makeText(this, "Gửi mail thất bại! Vui lòng thử lại.", Toast.LENGTH_LONG).show();
                    tvSendCodeForgot.setEnabled(true);
                    tvSendCodeForgot.setText("Gửi lại mã");
                });
            }
        }).start();
    }
}