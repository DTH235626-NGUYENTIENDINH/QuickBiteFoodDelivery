package com.example.quickbuyfooddelivery;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_voucher);
        setupTaskbar(R.id.btnNotification);

        RecyclerView rcvVoucher = findViewById(R.id.rcvVoucher);

        if (rcvVoucher != null) {
            rcvVoucher.setLayoutManager(new LinearLayoutManager(this));

            List<Voucher> list = new ArrayList<>();
            list.add(new Voucher("GIẢM 25%", "Hamburger cực chill", R.mipmap.ico_sale_nof));
            list.add(new Voucher("FREESHIP", "Đơn hàng từ 100k", R.mipmap.ico_sale_nof));

            VoucherAdapter voucherAdapter = new VoucherAdapter(list);
            rcvVoucher.setAdapter(voucherAdapter);
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }
}
