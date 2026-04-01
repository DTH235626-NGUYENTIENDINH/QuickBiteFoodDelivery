package com.example.quickbuyfooddelivery;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvForgotPassword, tvRegister;
    private DataBaseHelper db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); 

        db = new DataBaseHelper(this);

        // Ánh xạ các view
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        }); 

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterVerifyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
    String username = edtEmail.getText().toString().trim();
    String password = edtPassword.getText().toString().trim();

    if (TextUtils.isEmpty(username)) {
        edtEmail.setError("Vui lòng nhập email hoặc username");
        return;
    }
    if (TextUtils.isEmpty(password)) {
        edtPassword.setError("Vui lòng nhập mật khẩu");
        return;
    }

    // SQLite tự xử lý cả username lẫn email
    int role = db.login(username, password);

    if (role == 1) {
        Toast.makeText(this, "Chào Admin!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AdminDashboardActivity.class));
        finish();
    } else if (role == 0) {
        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Home.class));
        finish();
    } else {
        Toast.makeText(this, "Sai thông tin đăng nhập!", Toast.LENGTH_LONG).show();
    }
}
}

       