package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterVerifyActivity extends AppCompatActivity {

    private Button btnVerify;
    private TextView tvBackToLogin, tvSendCode;
    private EditText edtEmailVerify, edtVerificationCode;

    // Biến lưu mã đã gửi để so sánh lúc sau
    private String sentCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verify);

        // Ánh xạ View
        btnVerify = findViewById(R.id.btnVerify);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvSendCode = findViewById(R.id.tvSendcode);
        edtEmailVerify = findViewById(R.id.edtEmailVerify);
        edtVerificationCode = findViewById(R.id.edtVerificationCode);

        // Sự kiện Click
        if (tvSendCode != null) {
            tvSendCode.setOnClickListener(v -> sendCode());
        }
        if (btnVerify != null) {
            btnVerify.setOnClickListener(v -> verifyCode());
        }
        if (tvBackToLogin != null) {
            tvBackToLogin.setOnClickListener(v -> finish());
        }
    }

    private void sendCode() {
        String email = edtEmailVerify.getText().toString().trim();

        // 1. Kiểm tra Email hợp lệ
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vô hiệu hóa nút để tránh khách bấm liên tục khi đang gửi
        tvSendCode.setEnabled(false);
        tvSendCode.setText("Đang gửi...");

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
                    tvSendCode.setEnabled(true);
                    tvSendCode.setText("Gửi lại mã");
                });

            } catch (Exception e) {
                // Xử lý khi có lỗi (Sai mật khẩu ứng dụng, mất mạng...)
                runOnUiThread(() -> {
                    Log.e("OTP_ERROR", "Lỗi: " + e.getMessage());
                    Toast.makeText(this, "Gửi mail thất bại! Vui lòng thử lại.", Toast.LENGTH_LONG).show();
                    tvSendCode.setEnabled(true);
                    tvSendCode.setText("Gửi lại mã");
                });
            }
        }).start();
    }

    private void verifyCode() {
        String email = edtEmailVerify.getText().toString().trim();
        String inputCode = edtVerificationCode.getText().toString().trim();

        if (sentCode.isEmpty()) {
            Toast.makeText(this, "Vui lòng lấy mã xác minh trước!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(inputCode)) {
            edtVerificationCode.setError("Nhập mã OTP");
            return;
        }

        // So sánh mã người dùng nhập với mã đã gửi
        if (!inputCode.equals(sentCode)) {
            edtVerificationCode.setError("Mã xác minh không chính xác!");
            return;
        }

        // --- XÁC MINH THÀNH CÔNG ---
        Toast.makeText(this, "Xác minh thành công!", Toast.LENGTH_SHORT).show();

        // Chuyển sang RegisterActivity và mang theo Email đã xác minh
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }
}
