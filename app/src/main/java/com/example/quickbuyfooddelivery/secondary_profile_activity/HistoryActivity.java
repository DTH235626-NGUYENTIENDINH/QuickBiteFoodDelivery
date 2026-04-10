package com.example.quickbuyfooddelivery.secondary_profile_activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickbuyfooddelivery.DataBaseHelper;
import com.example.quickbuyfooddelivery.R;
import com.example.quickbuyfooddelivery.adapters.HistoryFoodAdapter;
import com.example.quickbuyfooddelivery.models.HistoryFood;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private RecyclerView rcvHistory;
    private List<HistoryFood> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DataBaseHelper(this);
        
        ImageView imgArrowBack = findViewById(R.id.imgArrowBack);
        imgArrowBack.setOnClickListener(v -> finish());

        rcvHistory = findViewById(R.id.rcvHistory);
        rcvHistory.setLayoutManager(new LinearLayoutManager(this));
        
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        historyList = new ArrayList<>();
        
        // 1. Lấy ID người dùng hiện tại từ Session
        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        if (userId == -1) return;

        // 2. Truy vấn SQLite (Lấy thông tin món ăn từ các đơn hàng của user này)
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT m.item_name, m.image_name, o.status, d.quantity, d.unit_price, o.total_amount, o.order_datetime " +
                     "FROM food_order o " +
                     "JOIN order_detail d ON o.order_id = d.order_id " +
                     "JOIN menu_item m ON d.item_id = m.item_id " +
                     "WHERE o.user_id = ? ORDER BY o.order_id DESC";
        
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});

        // 3. Đổ dữ liệu vào List
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String itemName = cursor.getString(0);
                String imageName = cursor.getString(1);
                String status = translateStatus(cursor.getString(2));
                int quantity = cursor.getInt(3);
                int price = cursor.getInt(4);
                int total = cursor.getInt(5);
                String date = cursor.getString(6);

                // Lấy ID ảnh từ tên file ảnh trong DB
                int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                if (resId == 0) resId = R.drawable.img_pz1;

                historyList.add(new HistoryFood(
                        resId,
                        itemName,
                        status,
                        "x" + quantity,
                        price + "đ",
                        "Tổng: " + total + "đ",
                        date
                ));
            }
            cursor.close();
        }

        // 4. Hiển thị lên RecyclerView
        rcvHistory.setAdapter(new HistoryFoodAdapter(historyList));
    }

    // Hàm đổi trạng thái sang tiếng Việt cho đơn giản
    private String translateStatus(String status) {
        if (status == null) return "Chờ duyệt";
        switch (status.toLowerCase()) {
            case "pending": return "Chờ duyệt";
            case "shipping": return "Đang giao";
            case "completed": return "Hoàn thành";
            case "cancelled": return "Đã hủy";
            default: return "Đang xử lý";
        }
    }
}
