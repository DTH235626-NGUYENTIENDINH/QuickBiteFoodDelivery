package com.example.quickbuyfooddelivery;

import android.database.Cursor;
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

    }

    private void loadOrders() {
        Cursor cursor = db.getUserNotifications(currentUserId);
        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String message = cursor.getString(2);
            String type = cursor.getString(3);
            String time = cursor.getString(4);

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
                    R.mipmap.ic_launcher, // Có thể đổi icon tùy theo biến 'type'
                    statusColor
            ));
        }
        cursor.close();
    }
}
