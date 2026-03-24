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
        ImageButton btnKhuyenMai = findViewById(R.id.imgIconKhuyenMai);
        ImageButton btnDonHang = findViewById(R.id.imgIconDonHang);
        if (btnKhuyenMai != null)
        {
            btnKhuyenMai.setOnClickListener(v -> {
                Intent intent = new Intent(NotificationActivity.this, VoucherActivity.class);
                startActivity(intent);
            });
        }
        if (btnDonHang != null)
        {
            btnDonHang.setOnClickListener(v -> {
                Intent intent = new Intent(NotificationActivity.this, NotificationOrderActivity.class);
                startActivity(intent);
            });
        }
    }
}