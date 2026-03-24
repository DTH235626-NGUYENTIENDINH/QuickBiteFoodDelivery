package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterVerifyActivity extends AppCompatActivity {

    Button btnVerify;
    TextView tvBackToLogin, tvSendcode;
    EditText edtEmailVerify, edtVerificationCode;
    string sentCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verify);

        btnVerify = findViewById(R.id.btnVerify);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvSendcode = findViewById(R.id.tvSendcode);
        edtEmailVerify = findViewById(R.id.edtEmail);
        edtVerificationCode = findViewById(R.id.edtCode);

        tvSendcode.setOnClickListener(v -> sendCode());
        btnVerify.setonClickListener(v -> verifyCode());
          
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
        
    private void sendCode() {
        String email = edtEmailVerify.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Vui lòng nhập email" );
                return;
            }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Vui lòng nhập email hợp lệ" );
                return;
            }
        tvSendcode.setEnabled(false);
        tvSendcode.setText("Đang gửi...");

        String code = EmailSender.generateCode();
        EmailSender.sendVerificationEmail(email, code, new EmailSender.EmailCallback() {
            @Override
            public void onSuccess(String generatedCode) {
            sentCode = generatedCode; // lưu lại mã để xác minh
            tvSendCode.setText("Gửi lại");
            tvSendCode.setEnabled(true);
            Toast.makeText(RegisterVerifyActivity.this,
                "Đã gửi mã đến " + email, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
            tvSendCode.setText("Gửi mã");
            tvSendCode.setEnabled(true);
            Toast.makeText(RegisterVerifyActivity.this,
                "Gửi thất bại: " + error, Toast.LENGTH_LONG).show();
            }
            });
    }
    private void verifyCode() {
        String code = edtVerificationCode.getText().toString().trim();
        String email = edtEmailVerify.getText().toString().trim();

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

        // Mã đúng → chuyển sang đăng ký
        Toast.makeText(this, "Xác minh thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }
}
