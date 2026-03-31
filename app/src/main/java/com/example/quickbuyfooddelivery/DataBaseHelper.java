package com.example.quickbuyfooddelivery;

import java.util.List;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FoodDeliveryDB2.db";
    private static final int DB_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
            "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL," +
            "password TEXT NOT NULL," +
            "full_name TEXT," +
            "address TEXT," +
            "role INTEGER DEFAULT 0" +
            ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS menu_item (" +
            "item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "item_name TEXT NOT NULL UNIQUE," +
            "price INTEGER NOT NULL," +
            "category TEXT NOT NULL," +
            "image_name TEXT," +
            "is_available INTEGER DEFAULT 1," +
            "description TEXT" +
            ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS food_order (" +
            "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id INTEGER," +
            "total_amount INTEGER," +
            "status TEXT," +
            "order_datetime TEXT DEFAULT (datetime('now'))," +
            "rating INTEGER," +
            "review_text TEXT," +
            "voucher_id INTEGER," +
            "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
            ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS vouchers (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "code TEXT UNIQUE," +
            "discount_percent INTEGER," +
            "max_discount REAL," +
            "min_order REAL," +
            "expiry_date TEXT," +
            "usage_limit INTEGER," +
            "used_count INTEGER DEFAULT 0" +
            ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS order_detail (" +
            "detail_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "order_id INTEGER NOT NULL," +
            "item_id INTEGER NOT NULL," +
            "quantity INTEGER NOT NULL," +
            "unit_price INTEGER NOT NULL," +
            "FOREIGN KEY (order_id) REFERENCES food_order(order_id)," +
            "FOREIGN KEY (item_id) REFERENCES menu_item(item_id)" +
            ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS cart (" +
            "cart_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id INTEGER NOT NULL," +
            "item_id INTEGER NOT NULL," +
            "quantity INTEGER DEFAULT 1," +
            "added_at TEXT DEFAULT (datetime('now'))," +
            "FOREIGN KEY (user_id) REFERENCES users(user_id)," +
            "FOREIGN KEY (item_id) REFERENCES menu_item(item_id)" +
            ")");

        db.execSQL("INSERT INTO users (username, password, full_name, address, role) " +
            "VALUES ('admin', '123', 'Quản Trị Viên', 'HCM', 1)");

        Log.d("DB_DEBUG", "Tạo DB thành công!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS order_detail");
        db.execSQL("DROP TABLE IF EXISTS vouchers");
        db.execSQL("DROP TABLE IF EXISTS food_order");
        db.execSQL("DROP TABLE IF EXISTS menu_item");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public int login(String username, String password) {
        Log.d("DB_DEBUG", "Login với: " + username + " / " + password);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT role FROM users WHERE username=? AND password=?",
            new String[]{username, password}
        );
        Log.d("DB_DEBUG", "Số dòng tìm được: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            int role = cursor.getInt(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return -1;
    }

    // Tổng doanh thu
    public int getTotalRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT SUM(total_amount) FROM food_order WHERE status='done'", null
        );
        if (cursor.moveToFirst()) {
            int total = cursor.getInt(0);
            cursor.close();
            return total;
        }
        cursor.close();
        return 0;
    }

    // Tổng đơn hàng
    public int getTotalOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT COUNT(*) FROM food_order", null
        );
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        }
        cursor.close();
        return 0;
    }

    // Tổng người dùng không tính admin
    public int getTotalUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT COUNT(*) FROM users WHERE role = 0", null
        );
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        }
        cursor.close();
        return 0;
    }

    // Tổng món ăn đang bán
    public int getTotalMenuItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT COUNT(*) FROM menu_item WHERE is_available = 1", null
        );
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        }
        cursor.close();
        return 0;

    }
    // Lấy tất cả món (cho admin)
    public List<MenuItem> getAllMenuItemsAdmin() {
        List<MenuItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT * FROM menu_item ORDER BY category", null
        );
        while (cursor.moveToNext()) {
            MenuItem item    = new MenuItem();
            item.itemId      = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
            item.itemName    = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
            item.price       = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
            item.category    = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            item.imageName   = cursor.getString(cursor.getColumnIndexOrThrow("image_name"));
            item.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            item.isAvailable = cursor.getInt(cursor.getColumnIndexOrThrow("is_available"));
            list.add(item);
        }
        cursor.close();
        return list;
    }

    // Thêm món mới
    public boolean addMenuItem(String name, int price, String category,
                            String image, String desc, int available) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_name",    name);
        values.put("price",        price);
        values.put("category",     category);
        values.put("image_name",   image);
        values.put("description",  desc);
        values.put("is_available", available);
        long result = db.insert("menu_item", null, values);
        return result != -1;
    }

    // Cập nhật món
    public void updateMenuItem(int itemId, String name, int price, String category,
                            String image, String desc, int available) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_name",    name);
        values.put("price",        price);
        values.put("category",     category);
        values.put("image_name",   image);
        values.put("description",  desc);
        values.put("is_available", available);
        db.update("menu_item", values, "item_id=?",
            new String[]{String.valueOf(itemId)});
    }

    // Xóa món
    public void deleteMenuItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("menu_item", "item_id=?",
            new String[]{String.valueOf(itemId)});
    }

    // Lấy tất cả đơn hàng kèm username
public List<FoodOrder> getAllOrders() {
    List<FoodOrder> list = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT f.order_id, f.user_id, u.username, f.total_amount, " +
        "f.status, f.order_datetime " +
        "FROM food_order f " +
        "LEFT JOIN users u ON f.user_id = u.user_id " +
        "ORDER BY f.order_id DESC", null
    );
    while (cursor.moveToNext()) {
        FoodOrder o       = new FoodOrder();
        o.orderId         = cursor.getInt(0);
        o.userId          = cursor.getInt(1);
        o.username        = cursor.getString(2);
        o.totalAmount     = cursor.getInt(3);
        o.status          = cursor.getString(4);
        o.orderDatetime   = cursor.getString(5);
        list.add(o);
    }
    cursor.close();
    return list;
}

// Lấy đơn theo trạng thái
public List<FoodOrder> getOrdersByStatus(String status) {
    List<FoodOrder> list = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT f.order_id, f.user_id, u.username, f.total_amount, " +
        "f.status, f.order_datetime " +
        "FROM food_order f " +
        "LEFT JOIN users u ON f.user_id = u.user_id " +
        "WHERE f.status = ? ORDER BY f.order_id DESC",
        new String[]{status}
    );
    while (cursor.moveToNext()) {
        FoodOrder o     = new FoodOrder();
        o.orderId       = cursor.getInt(0);
        o.userId        = cursor.getInt(1);
        o.username      = cursor.getString(2);
        o.totalAmount   = cursor.getInt(3);
        o.status        = cursor.getString(4);
        o.orderDatetime = cursor.getString(5);
        list.add(o);
    }
    cursor.close();
    return list;
}

// Cập nhật trạng thái đơn
public void updateOrderStatus(int orderId, String status) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("status", status);
    db.update("food_order", values, "order_id=?",
        new String[]{String.valueOf(orderId)});
}

// Lấy chi tiết món trong đơn
public List<String> getOrderDetails(int orderId) {
    List<String> list = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT m.item_name, od.quantity, od.unit_price " +
        "FROM order_detail od " +
        "JOIN menu_item m ON od.item_id = m.item_id " +
        "WHERE od.order_id = ?",
        new String[]{String.valueOf(orderId)}
    );
    while (cursor.moveToNext()) {
        String name  = cursor.getString(0);
        int    qty   = cursor.getInt(1);
        int    price = cursor.getInt(2);
        list.add(name + " x" + qty + "|" + (price * qty));
    }
    cursor.close();
    return list;
}
}
