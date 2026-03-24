package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterVerifyActivity extends AppCompatActivity {

    Button btnVerify;
    TextView tvBackToLogin, tvSendcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verify);

        btnVerify = findViewById(R.id.btnVerify);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvSendcode = findViewById(R.id.tvSendcode);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterVerifyActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvSendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạm thời hiện thông báo giả lập việc gửi mã thành công
                Toast.makeText(RegisterVerifyActivity.this, "Mã xác minh đã được gửi đến Email của bạn!", Toast.LENGTH_SHORT).show();
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}