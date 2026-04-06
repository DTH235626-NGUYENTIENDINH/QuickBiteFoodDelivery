package com.example.quickbuyfooddelivery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtRegPassword, edtRegConfirmPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;
    private DataBaseHelper db;

    private String Email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DataBaseHelper(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtRegConfirmPassword = findViewById(R.id.edtRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        Intent intent = getIntent();
        if(intent!=null){
            Email = intent.getStringExtra("email");
        }

        // Xử lý khi bấm Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void performRegistration() {
        String username = edtUsername.getText().toString().trim();
        String password = edtRegPassword.getText().toString().trim();
        String confirmPassword = edtRegConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtRegPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }
        if (!password.equals(confirmPassword)) {
            edtRegConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        if (db.isUsernameExists(username)) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = db.registerUser(username, password, Email);
        if (success) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle("Chúc mừng!");
            builder.setMessage("Tài khoản của bạn đã được tạo thành công.");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            builder.show();
        } else {
            Toast.makeText(this, "Đăng ký thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
}
