package com.example.quickbuyfooddelivery;

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

            // Nếu DB chưa có voucher thì thêm mẫu
            if (list.isEmpty()) {
                db.addVoucher("SALE10", 10, 50000, 100000, "2025-12-31", 100);
                db.addVoucher("GIAM25", 25, 80000, 200000, "2025-12-31", 50);
                list = db.getAllVouchers();
            }

            VoucherAdapter adapter = new VoucherAdapter(list);
            rcvVoucher.setAdapter(adapter);
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }
}