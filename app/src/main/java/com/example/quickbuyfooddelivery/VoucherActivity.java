package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickbuyfooddelivery.adapters.Notification1FoodAdapter;
import com.example.quickbuyfooddelivery.models.NotificationFood1;
import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends BaseActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 1. SỬA: Phải dùng layout của Voucher
            setContentView(R.layout.activity_notification_voucher);
            setupTaskbar(R.id.btnNotification);

            // 2. SỬA: Ánh xạ đúng ID RecyclerView trong file activity_voucher.xml
            RecyclerView rcvVoucher = findViewById(R.id.rcvVoucher);

            if (rcvVoucher != null) {
                rcvVoucher.setLayoutManager(new LinearLayoutManager(this));

                List<Voucher> list = new ArrayList<>();
                list.add(new Voucher("GIẢM 25%", "Hamburger cực chill", R.mipmap.ico_sale_nof));
                list.add(new Voucher("FREESHIP", "Đơn hàng từ 100k", R.mipmap.ico_sale_nof));

                VoucherAdapter voucherAdapter = new VoucherAdapter(list);
                rcvVoucher.setAdapter(voucherAdapter);
            }

            // 3. XỬ LÝ NÚT BẤM
            ImageButton btnKhuyenMai = findViewById(R.id.imgIconKhuyenMai);
            ImageButton btnDonHang = findViewById(R.id.imgIconDonHang);

            if (btnKhuyenMai != null) {
                btnKhuyenMai.setOnClickListener(v -> {
                    Intent intent = new Intent(VoucherActivity.this, VoucherActivity.class);
                    startActivity(intent);
                });
            }
            ImageButton btnBack = findViewById(R.id.btnBack);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> {
                    finish();
                });
            }
            if (btnDonHang != null) {
                btnDonHang.setOnClickListener(v -> {
                    Intent intent = new Intent(VoucherActivity.this, NotificationActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        }
    }
