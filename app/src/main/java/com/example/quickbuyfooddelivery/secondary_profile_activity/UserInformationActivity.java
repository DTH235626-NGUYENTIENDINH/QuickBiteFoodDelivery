package com.example.quickbuyfooddelivery.secondary_profile_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.quickbuyfooddelivery.DataBaseHelper;
import com.example.quickbuyfooddelivery.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.bumptech.glide.signature.ObjectKey;
import com.yalantis.ucrop.UCrop;


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

            int avatarIndex = cursor.getColumnIndex("avatarPath");
            if (avatarIndex != -1){
                String avatarUrl = cursor.getString(avatarIndex);
                if (avatarUrl != null && !avatarUrl.isEmpty()) {
                    File file = new File(avatarUrl);
                    Glide.with(this)
                            .load(file)
                            .signature(new ObjectKey(file.lastModified()))
                            .circleCrop()
                            .placeholder(R.mipmap.img_profile_avatar)
                            .into(imgAvatar);
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
    // 1. Định nghĩa bộ lọc chọn ảnh từ Gallery
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri sourceUri = result.getData().getData();
                    if (sourceUri != null) {
                        startCrop(sourceUri); // Bước tiếp theo: Đi cắt ảnh
                    }
                }
            }
    );

    // 2. Hàm cấu hình giao diện Cắt ảnh
    private void startCrop(Uri sourceUri) {
        // Tạo file đích để lưu ảnh sau khi cắt (lưu vào bộ nhớ riêng của App)
        String fileName = "avatar_" + currentUsername + ".jpg";
        File destinationFile = new File(getFilesDir(), fileName);
        Uri destinationUri = Uri.fromFile(destinationFile);

        // Cấu hình uCrop (Cắt hình vuông)
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true); // Hiển thị khung mờ hình tròn (hợp với avatar tròn)
        options.setCompressionQuality(90);  // Giảm dung lượng ảnh một chút cho nhẹ app
        options.setToolbarTitle("Chỉnh sửa ảnh");

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Cắt tỉ lệ 1:1 (hình vuông)
                .withMaxResultSize(500, 500) // Giới hạn kích thước ảnh đầu ra
                .withOptions(options)
                .start(this, cropLauncher); // Chuyển sang màn hình uCrop
    }

    // 3. Launcher nhận kết quả từ uCrop (để lưu vào SQLite)
    private final ActivityResultLauncher<Intent> cropLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri resultUri = UCrop.getOutput(result.getData());
                    if (resultUri != null) {
                        // Cắt xong rồi! Giờ lưu đường dẫn vào DB
                        saveAvatarToDatabase(resultUri.getPath());
                    }
                } else if (result.getResultCode() == UCrop.RESULT_ERROR) {
                    Throwable cropError = UCrop.getError(result.getData());
                    Toast.makeText(this, "Lỗi cắt ảnh: " + cropError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
    );

    // 4. Hàm lưu đường dẫn và cập nhật UI
    private void saveAvatarToDatabase(String path) {
        if (db.updateUserAvatar(currentUsername, path)) {
            File file = new File(path);
            // Dùng Glide hiện ngay lên (kèm Signature để tránh lỗi cache)
            Glide.with(this)
                    .load(file)
                    .signature(new com.bumptech.glide.signature.ObjectKey(file.lastModified()))
                    .circleCrop()
                    .into(imgAvatar);
            Toast.makeText(this, "Đã cập nhật ảnh đại diện!", Toast.LENGTH_SHORT).show();
        }
    }
}