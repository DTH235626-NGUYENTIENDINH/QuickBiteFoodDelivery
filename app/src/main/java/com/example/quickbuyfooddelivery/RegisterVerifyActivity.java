package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterVerifyActivity extends AppCompatActivity {

    private Button btnVerify;
    private TextView tvBackToLogin, tvSendCode;
    private EditText edtEmailVerify, edtVerificationCode;
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

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        tvSendCode.setEnabled(false);
        tvSendCode.setText("Đang gửi...");

        // Giả lập gửi mã
        sentCode = "123456"; 
        Toast.makeText(this, "Mã xác minh là: " + sentCode, Toast.LENGTH_LONG).show();
        tvSendCode.setEnabled(true);
        tvSendCode.setText("Gửi lại");
    }

    private void verifyCode() {
        String email = edtEmailVerify.getText().toString().trim();
        String code = edtVerificationCode.getText().toString().trim();

        if (sentCode.isEmpty()) {
            Toast.makeText(this, "Vui lòng gửi mã trước!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            edtVerificationCode.setError("Vui lòng nhập mã xác minh");
            return;
        }
        if (!code.equals(sentCode)) {
            edtVerificationCode.setError("Mã xác minh không đúng!");
            return;
        }

        // Chuyển sang màn hình đăng ký chính thức
        Toast.makeText(this, "Xác minh thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }
}
