package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickbuyfooddelivery.adapters.Notification1FoodAdapter;
import com.example.quickbuyfooddelivery.models.NotificationFood1;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setupTaskbar(R.id.btnNotification);

        RecyclerView rcvCapNhatDonHang = findViewById(R.id.rcvCapNhatDonHang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvCapNhatDonHang.setLayoutManager(layoutManager);
        List<NotificationFood1> list = new ArrayList<>();
        list.add( new NotificationFood1(R.mipmap.ic_launcher, "Đơn hàng đã huỷ",
                "Đơn Pepsi-N11T02N2026_035021 đã huỷ gergre ghẻ h ehg ẻhg ẻth ẻt hẻ hử hư rh ew he ư fwe g fewf ewfeqfjukefqukfgqeofugfoqwfgofgfogo geqoig ogegroqegofrgqepofgeqi"));
        list.add( new NotificationFood1(R.mipmap.ic_launcher, "Đơn hàng đặt thành công",
                "Đơn Pepsi-N11T02N2026_035021 đã huỷ gergre ghẻ h ehg ẻhg ẻth ẻt hẻ hử hư rh ew he ư fwe g fewf ewfeqfjukefqukfgqeofugfoqwfgofgfogo geqoig ogegroqegofrgqepofgeqi"));
        Notification1FoodAdapter adapter = new Notification1FoodAdapter(list);
        rcvCapNhatDonHang.setAdapter(adapter);

        ConstraintLayout layoutThongBaoKhuyenMai = findViewById(R.id.layoutThongBaoKhuyenMai);
        ConstraintLayout layoutThongBaoDonHang = findViewById(R.id.layoutThongBaoDonHang);
        ImageButton imageGioHang = findViewById(R.id.imageButton);

        layoutThongBaoKhuyenMai.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, VoucherActivity.class);
            startActivity(intent);
        });

        layoutThongBaoDonHang.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, NotificationOrderActivity.class);
            startActivity(intent);
        });

        imageGioHang.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, ShoppingCartActivity.class);
            startActivity(intent);
        });
    }
}