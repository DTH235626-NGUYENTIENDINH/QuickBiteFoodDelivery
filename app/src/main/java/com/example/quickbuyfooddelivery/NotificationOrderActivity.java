package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class NotificationOrderActivity extends BaseActivity {
    private RecyclerView rcvNotificationOrder;
    private NotificationOrderAdapter adapter;
    private List<NotificationOrder> orderList;
    private DataBaseHelper db;
    private int currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_order);
        db = new DataBaseHelper(this);

        android.content.SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);
        // Header actions
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        rcvNotificationOrder = findViewById(R.id.rcvNotificationOrder);
        rcvNotificationOrder.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        loadOrders();
        adapter = new NotificationOrderAdapter(orderList);
        rcvNotificationOrder.setAdapter(adapter);

        ImageButton spcart = findViewById(R.id.imgbtnspcart);
        spcart.setOnClickListener(v -> {
            startActivity(new Intent(NotificationOrderActivity.this, ShoppingCartActivity.class));
        });
    }

    private void loadOrders() {
        Cursor cursor = db.getUserNotifications(currentUserId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Cột 0: notif_id, Cột 1: user_id, Cột 2: title, Cột 3: message, Cột 4: type, Cột 5: created_at
                String title = cursor.getString(2);   // Lấy title thay vì user_id
                String message = cursor.getString(3); // Lấy message
                String type = cursor.getString(4);    // Lấy type
                String time = cursor.getString(5);    // Lấy created_at

                // Logic tự động chọn màu: Nếu tiêu đề có chữ "huỷ" thì tô màu đỏ, còn lại màu xanh
                int statusColor = Color.parseColor("#2ABB14"); // Mặc định màu Xanh (Thành công)
                if (title != null && title.toLowerCase().contains("huỷ")) {
                    statusColor = Color.parseColor("#8B1E1E"); // Màu Đỏ (Huỷ)
                }
                
                // Nhét dữ liệu vào danh sách
                orderList.add(new NotificationOrder(
                        title,
                        message,
                        time,
                        R.mipmap.ic_launcher, 
                        statusColor
                ));
            }
            cursor.close();
        }
    }
}
