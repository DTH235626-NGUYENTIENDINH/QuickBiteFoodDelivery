package com.example.quickbuyfooddelivery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

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

        seedData(db);
        Log.d("DB_DEBUG", "Khởi tạo DB và nạp dữ liệu thành công!");
    }

    private void seedData(SQLiteDatabase db) {
        // Pizza
        insertMenuItem(db, "Pizza Phô Mai", 45000, "PIZZA", "img_pz1");
        insertMenuItem(db, "Pizza Hải Sản", 55000, "PIZZA", "img_pz2");
        insertMenuItem(db, "Pizza Nấm", 45000, "PIZZA", "img_pz3");
        insertMenuItem(db, "Pizza Xúc Xích Ý", 40000, "PIZZA", "img_pz4");
        insertMenuItem(db, "Pizza Gà Nướng Dứa", 40000, "PIZZA", "img_pz5");
        insertMenuItem(db, "Pizza Gà Phô Mai", 45000, "PIZZA", "img_pz8");
        
        // Hamburger
        insertMenuItem(db, "Hamburger Gà", 28000, "HAMBURGER", "img_hbg1");
        insertMenuItem(db, "Hamburger Bò", 30000, "HAMBURGER", "img_hbg2");
        insertMenuItem(db, "Hamburger Tôm", 28000, "HAMBURGER", "img_hbg3");
        insertMenuItem(db, "Bò Sốt Tiêu Đen", 45000, "HAMBURGER", "img_hbg6");
        
        // Drinks
        insertMenuItem(db, "Pepsi", 15000, "DRINK", "img_pepsi");
        insertMenuItem(db, "Sting", 15000, "DRINK", "img_sting");
        insertMenuItem(db, "CocaCola", 15000, "DRINK", "img_coca");
        insertMenuItem(db, "7Up", 15000, "DRINK", "img_7up");
    }

    private void insertMenuItem(SQLiteDatabase db, String name, int price, String category, String img) {
        ContentValues v = new ContentValues();
        v.put("item_name", name);
        v.put("price", price);
        v.put("category", category);
        v.put("image_name", img);
        db.insertWithOnConflict("menu_item", null, v, SQLiteDatabase.CONFLICT_IGNORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Khi tăng version, xóa hết bảng cũ và chạy lại onCreate để nạp dữ liệu mới
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS order_detail");
        db.execSQL("DROP TABLE IF EXISTS vouchers");
        db.execSQL("DROP TABLE IF EXISTS food_order");
        db.execSQL("DROP TABLE IF EXISTS menu_item");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public int login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT role FROM users WHERE username=? AND password=?",
            new String[]{username, password}
        );
        if (cursor.moveToFirst()) {
            int role = cursor.getInt(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return -1;
    }

    public List<Food> getAllFood() {
        List<Food> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT item_name, price, category, image_name FROM menu_item", null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                int price = cursor.getInt(1);
                String category = cursor.getString(2);
                String imgName = cursor.getString(3);
                
                // Trả về đối tượng Food (imageResId để tạm là 0 vì FoodActivity sẽ tự tìm theo imgName)
                list.add(new Food(name, String.valueOf(price), 0, category, imgName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
