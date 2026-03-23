package com.example.quickbuyfooddelivery;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;

public class NotificationOrderActivity extends BaseActivity {
    private RecyclerView rcvNotificationOrder;
    private NotificationOrderAdapter adapter;
    private List<NotificationOrder> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_order);

        // Header actions
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        rcvNotificationOrder = findViewById(R.id.rcvNotificationOrder);
        
        // Dữ liệu mẫu giống trong ảnh
        orderList = new ArrayList<>();
        orderList.add(new NotificationOrder(
                "Đơn hàng đã huỷ", 
                "Đơn Pepsi-N11T02N2026_035021 đã được huỷ.", 
                "15:55 11-02-2026", 
                R.mipmap.ic_launcher, 
                Color.parseColor("#8B1E1E") // Màu đỏ cho trạng thái hủy
        ));
        
        orderList.add(new NotificationOrder(
                "Đặt hàng thành công", 
                "Đơn Pepsi-N11T02N2026_035021 đã được đặt.", 
                "15:50 11-02-2026", 
                R.mipmap.ic_launcher, 
                Color.parseColor("#2ABB14") // Màu xanh cho trạng thái thành công
        ));

        adapter = new NotificationOrderAdapter(orderList);
        rcvNotificationOrder.setLayoutManager(new LinearLayoutManager(this));
        rcvNotificationOrder.setAdapter(adapter);
    }
}
