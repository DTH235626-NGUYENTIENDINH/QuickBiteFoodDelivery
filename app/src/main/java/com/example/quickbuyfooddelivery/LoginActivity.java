package com.example.quickbuyfooddelivery;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = pref.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            int role = pref.getInt("role", 0);
            if (role == 1) {
                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            } else{
                Intent intent = new Intent(LoginActivity.this, Home.class);
                startActivity(intent);
            }
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

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

        String hashedPassword = HashUtils.hashPassword(password);

        Log.d("LOGIN_DEBUG", "Thử đăng nhập với: '" + username + "' / '" + password + "'");
        int[] loginResult = db.loginExtended(username, hashedPassword);

        if (loginResult != null) {
            int role = loginResult[0];
            int userId = loginResult[1];
            
            Log.d("LOGIN_DEBUG", "Thành công! Role: " + role + ", UserId: " + userId);

            // Lưu Session
            SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("user_id", userId);
            editor.putInt("role", role);
            editor.putBoolean("isLoggedIn", true);
            editor.putString("username", username);
            editor.apply();

            if (role == 1) {
                Toast.makeText(this, "Chào Admin!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AdminDashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Home.class));
                finish();
            }
        } else {
            Log.d("LOGIN_DEBUG", "Thất bại: Sai username hoặc password");
            Toast.makeText(this, "Sai thông tin đăng nhập!", Toast.LENGTH_LONG).show();
        }
    }
}
