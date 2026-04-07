package com.example.quickbuyfooddelivery.secondary_profile_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quickbuyfooddelivery.DataBaseHelper;
import com.example.quickbuyfooddelivery.R;
import com.google.android.material.imageview.ShapeableImageView;

public class UserInformationActivity extends AppCompatActivity {
    // Khai báo các view nhập liệu
    private EditText edtHoTen, edtSDT, edtEmail, edtDiaChi;
    private Spinner spinnerGioiTinh;
    private Button btnCapNhat;

    private DataBaseHelper db;
    private String[] genders = {"Nam", "Nữ", "Khác"};
    private String currentUsername;
    //khai báo xử lý avartar---------------------------------
    private ShapeableImageView imgAvatar;
    private ImageView imgCamera;
    //--------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        //Trở lại
        ImageView imgArrowBack = findViewById(R.id.imgArrowBack);
        imgArrowBack.setOnClickListener(v -> {
            finish();
        });
        // lấy tên người dùng đã lưu từ lúc Đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");

        //Thay avatar
        imgAvatar = findViewById(R.id.imgAvatar);
        imgCamera = findViewById(R.id.imgCamera);
            //Bấm vào icon Camera để chọn ảnh
        imgCamera.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        //set giới tính bằng spinner
        spinnerGioiTinh = findViewById(R.id.spinnerGioiTinh);
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGioiTinh.setAdapter(adapter);

        //Dỗ data
        db = new DataBaseHelper(this);

        edtHoTen = findViewById(R.id.edtHoTen);
        edtSDT = findViewById(R.id.edtSDT);
        edtEmail = findViewById(R.id.edtEmail);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnCapNhat = findViewById(R.id.btnCapNhat);

        loadUserData();

        btnCapNhat.setOnClickListener(v -> {
            saveUserData();
        });

    }

    // --- HÀM LẤY DỮ LIỆU TỪ DB LÊN GIAO DIỆN ---
    private void loadUserData() {
        Cursor cursor = db.getUserInfo(currentUsername);
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy text gán vào EditText
            edtHoTen.setText(cursor.getString(6));
            edtEmail.setText(cursor.getString(4));
            edtDiaChi.setText(cursor.getString(7) != null ? cursor.getString(7) : "");
            edtSDT.setText(cursor.getString(3) != null ? cursor.getString(3) : "");

            // Xử lý Spinner
            String savedSex = cursor.getString(5);
            if (savedSex != null) {
                for (int i = 0; i < genders.length; i++) {
                    if (genders[i].equals(savedSex)) {
                        spinnerGioiTinh.setSelection(i);
                        break;
                    }
                }
            }
            cursor.close();
        }
    }

    // --- HÀM LƯU DỮ LIỆU TỪ GIAO DIỆN XUỐNG DB ---
    private void saveUserData() {
        String hoten = edtHoTen.getText().toString().trim();
        String sdt = edtSDT.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String diachi = edtDiaChi.getText().toString().trim();
        String gioitinh = spinnerGioiTinh.getSelectedItem().toString();

        if (hoten.isEmpty()) {
            edtHoTen.setError("Không được để trống họ tên");
            return;
        }

        boolean isSuccess = db.updateUserInfo(currentUsername, hoten, email, diachi, sdt, gioitinh);

        if (isSuccess) {
            Toast.makeText(this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
    // Định nghĩa bộ lọc chọn ảnh
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    // 2. Set cái ảnh người dùng vừa chọn vào Avatar tròn
                    imgAvatar.setImageURI(imageUri);
                }
            }
    );
}