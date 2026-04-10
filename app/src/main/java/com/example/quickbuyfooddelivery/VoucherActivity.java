package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VoucherActivity extends BaseActivity {

    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_voucher);
        setupTaskbar(R.id.btnNotification);

        db = new DataBaseHelper(this);

        RecyclerView rcvVoucher = findViewById(R.id.rcvVoucher);

        if (rcvVoucher != null) {
            rcvVoucher.setLayoutManager(new LinearLayoutManager(this));

            // ✅ Lấy từ SQLite thay vì hardcode
            List<Voucher> list = db.getAllVouchers();

            VoucherAdapter adapter = new VoucherAdapter(list);
            rcvVoucher.setAdapter(adapter);
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        ImageButton spcart = findViewById(R.id.imgbtnspcart);
        spcart.setOnClickListener(v -> {
            startActivity(new Intent(VoucherActivity.this, ShoppingCartActivity.class));
        });
    }
}