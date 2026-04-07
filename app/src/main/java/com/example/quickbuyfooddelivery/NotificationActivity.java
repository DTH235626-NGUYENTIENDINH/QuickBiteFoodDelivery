package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

        // Setup RecyclerView
        if (rcvCapNhatDonHang != null) {
            rcvCapNhatDonHang.setLayoutManager(new LinearLayoutManager(this));
            list = new ArrayList<>();
            adapter = new Notification1FoodAdapter(list);
            rcvCapNhatDonHang.setAdapter(adapter);
        }

        // Tải dữ liệu từ Database
        loadNotificationsFromDB();

        // Xử lý click
        if (layoutThongBaoKhuyenMai != null) {
            layoutThongBaoKhuyenMai.setOnClickListener(v -> {
                if (currentUserId != -1) {
                    db.markNotificationsAsRead(currentUserId, "PROMOTION");
                }
                Intent intent = new Intent(NotificationActivity.this, VoucherActivity.class);
                startActivity(intent);
            });
        }

        if (layoutThongBaoDonHang != null) {
            layoutThongBaoDonHang.setOnClickListener(v -> {
                if (currentUserId != -1) {
                    db.markNotificationsAsRead(currentUserId, "ORDER");
                }
                Intent intent = new Intent(NotificationActivity.this, NotificationOrderActivity.class);
                startActivity(intent);
            });
        }

        if (imageGioHang != null) {
            imageGioHang.setOnClickListener(v -> {
                Intent intent = new Intent(NotificationActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotificationsFromDB();
    }

    private void loadNotificationsFromDB() {
        if (list == null) list = new ArrayList<>();
        list.clear();

        int unreadOrderCount = 0;
        int unreadPromotionCount = 0;
        String latestOrderMsg = "Chưa có thông báo đơn hàng";

        boolean hasOrderData = false;
        if (currentUserId != -1) {
            Cursor cursor = db.getUserNotifications(currentUserId);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(1);
                    String message = cursor.getString(2);
                    String type = cursor.getString(3);
                    int isRead = cursor.getInt(5);

                    if ("ORDER".equals(type)) {
                        hasOrderData = true;
                        list.add(new NotificationFood1(R.mipmap.ic_launcher, title, message));
                        if (isRead == 0) unreadOrderCount++;
                        if (latestOrderMsg.equals("Chưa có thông báo đơn hàng")) {
                            latestOrderMsg = message;
                        }
                    } else if ("PROMOTION".equals(type)) {
                        if (isRead == 0) unreadPromotionCount++;
                    }
                }
                cursor.close();
            }
        }

        // Nếu không có dữ liệu, thêm dữ liệu mẫu để test giao diện
        if (!hasOrderData) {
            list.add(new NotificationFood1(R.mipmap.ic_launcher, "Đơn hàng thành công", "Đơn hàng Pepsi-N11T02 của bạn đã được giao thành công."));
            list.add(new NotificationFood1(R.mipmap.ic_launcher, "Đơn hàng đã huỷ", "Đơn hàng Hamburger-H01 đã bị huỷ do hết hàng."));
            latestOrderMsg = "Đơn hàng Pepsi-N11T02 của bạn đã được giao...";
        }

        // --- Cập nhật giao diện ---
        TextView tvThongBaoDonHang = findViewById(R.id.tvThongBaoDonHang);
        TextView tvThongBaoDonHangMoi = findViewById(R.id.tvThongBaoDonHangMoi);
        TextView tvThongBaoKhuyenMaiMoi = findViewById(R.id.tvThongBaoKhuyenMaiMoi);

        if (tvThongBaoDonHang != null) tvThongBaoDonHang.setText(latestOrderMsg);
        updateBadge(tvThongBaoDonHangMoi, unreadOrderCount);
        updateBadge(tvThongBaoKhuyenMaiMoi, unreadPromotionCount);

        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void updateBadge(TextView tvBadge, int count) {
        if (tvBadge != null) {
            if (count > 0) {
                tvBadge.setText(count > 99 ? "99+" : String.valueOf(count));
                tvBadge.setVisibility(View.VISIBLE);
            } else {
                tvBadge.setVisibility(View.GONE);
            }
        }
    }
}
