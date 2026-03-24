package com.example.quickbuyfooddelivery.secondary_profile_activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quickbuyfooddelivery.R;
import com.google.android.material.imageview.ShapeableImageView;

public class UserInformationActivity extends AppCompatActivity {

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

        //set giới tính bằng spinner
        Spinner spinner = findViewById(R.id.spinnerGioiTinh);
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Thay avatar
        imgAvatar = findViewById(R.id.imgAvatar);
        imgCamera = findViewById(R.id.imgCamera);
            //Bấm vào icon Camera để chọn ảnh
        imgCamera.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

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