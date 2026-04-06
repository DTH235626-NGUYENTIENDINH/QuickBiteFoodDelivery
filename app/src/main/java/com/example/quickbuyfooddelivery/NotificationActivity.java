package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickbuyfooddelivery.adapters.Notification1FoodAdapter;
import com.example.quickbuyfooddelivery.models.NotificationFood1;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity {

    private RecyclerView rcvCapNhatDonHang;
    private Notification1FoodAdapter adapter;
    private List<NotificationFood1> list;
    private DataBaseHelper db;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setupTaskbar(R.id.btnNotification);

        db = new DataBaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);

        // Ánh xạ View
        rcvCapNhatDonHang = findViewById(R.id.rcvCapNhatDonHang);
        ConstraintLayout layoutThongBaoKhuyenMai = findViewById(R.id.layoutThongBaoKhuyenMai);
        ConstraintLayout layoutThongBaoDonHang = findViewById(R.id.layoutThongBaoDonHang);
        ImageButton imageGioHang = findViewById(R.id.imageButton);
        TextView tvThongBaoDonHang = findViewById(R.id.tvThongBaoDonHang);
        TextView tvThongBaoDonHangMoi = findViewById(R.id.tvThongBaoDonHangMoi);

        // Setup RecyclerView
        rcvCapNhatDonHang.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new Notification1FoodAdapter(list);
        rcvCapNhatDonHang.setAdapter(adapter);

        // Tải dữ liệu từ Database
        loadNotificationsFromDB();

        // Xử lý click
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

    @Override
    protected void onResume() {
        super.onResume();
        loadNotificationsFromDB();
    }

    private void loadNotificationsFromDB() {
        if (list == null) list = new ArrayList<>();
        list.clear();

        int unreadCount = 0;
        String latestMessage = "Chưa có thông báo mới";

        // 1. Chỉ thực hiện nếu ID hợp lệ
        if (currentUserId != -1) {
            Cursor cursor = db.getUserNotifications(currentUserId);

            if (cursor != null) {
                // Duyệt từ bản ghi mới nhất (Cursor đã sắp xếp DESC trong DB)
                while (cursor.moveToNext()) {
                    // Column indices: 1:title, 2:message, 3:type, 5:is_read
                    String title = cursor.getString(1);
                    String message = cursor.getString(2);
                    String type = cursor.getString(3);
                    int isRead = cursor.getInt(5);

                    // 2. Chỉ lấy thông báo loại ORDER để hiển thị ở mục này
                    if ("ORDER".equals(type)) {
                        list.add(new NotificationFood1(R.mipmap.ic_launcher, title, message));

                        // Đếm số lượng thông báo chưa đọc (is_read == 0)
                        if (isRead == 0) {
                            unreadCount++;
                        }

                        // Lấy nội dung của thông báo đầu tiên (mới nhất) để hiện ra màn hình chính
                        if (list.size() == 1) {
                            latestMessage = message;
                        }
                    }
                }
                cursor.close();
            }
        }

        // 3. Cập nhật giao diện (UI)
        TextView tvThongBaoDonHang = findViewById(R.id.tvThongBaoDonHang);
        TextView tvThongBaoDonHangMoi = findViewById(R.id.tvThongBaoDonHangMoi);

        if (tvThongBaoDonHang != null) {
            tvThongBaoDonHang.setText(latestMessage);
        }

        if (tvThongBaoDonHangMoi != null) {
            if (unreadCount > 0) {
                // Hiển thị số lượng chưa đọc, nếu > 99 thì hiện 99+ cho đẹp
                tvThongBaoDonHangMoi.setText(unreadCount > 99 ? "99+" : String.valueOf(unreadCount));
                tvThongBaoDonHangMoi.setVisibility(View.VISIBLE);
            } else {
                tvThongBaoDonHangMoi.setVisibility(View.GONE);
            }
        }

        // 4. Cập nhật RecyclerView
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}