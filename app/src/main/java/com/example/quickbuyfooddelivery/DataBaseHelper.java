package com.example.quickbuyfooddelivery;

import java.util.List;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.quickbuyfooddelivery.models.ShoppingCart;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FoodDeliveryDB2.db";
    private static final int DB_VERSION = 3; // Nâng cấp để tạo bảng favorites

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
            "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL UNIQUE," +
            "password TEXT NOT NULL," +
            "phone TEXT," +
            "email TEXT NOT NULL UNIQUE," +
            "sex TEXT,"+
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

        db.execSQL("CREATE TABLE IF NOT EXISTS notifications (" +
                "notif_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "title TEXT," +
                "message TEXT," +
                "type TEXT," +
                "created_at TEXT DEFAULT (datetime('now', 'localtime'))," +
                "is_read INTEGER DEFAULT 0," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS favorites (" +
                "fav_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "item_name TEXT," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                ")");

        db.execSQL("INSERT INTO users (username, password, phone, email, sex, full_name, address, role) " +
            "VALUES ('admin', '8d969eee76698219887552047034a0b29c50b930e5821758801fed1434195b9d','0312121212','dinh_dth235626@student.agu.edu.vn', 'Nam', 'Quản Trị Viên','HCM', 1)");

        seedData(db);
        Log.d("DB_DEBUG", "Khởi tạo DB thành công!");
    }

    private void seedData(SQLiteDatabase db) {
        insertMenuItem(db, "Pizza Phô Mai", 45000, "PIZZA", "img_pz1");
        insertMenuItem(db, "Pizza Hải Sản", 55000, "PIZZA", "img_pz2");
        insertMenuItem(db, "Hamburger Gà", 28000, "HAMBURGER", "img_hbg1");
        insertMenuItem(db, "Pepsi", 15000, "DRINK", "img_pepsi");
        
        insertVoucher(db, "KM10", 10, 50000, 100000);
        insertVoucher(db, "FREESHIP", 100, 15000, 50000);
    }

    private void insertMenuItem(SQLiteDatabase db, String name, int price, String category, String img) {
        ContentValues v = new ContentValues();
        v.put("item_name", name);
        v.put("price", price);
        v.put("category", category);
        v.put("image_name", img);
        db.insertWithOnConflict("menu_item", null, v, SQLiteDatabase.CONFLICT_IGNORE);
    }

    // Lấy tất cả voucher
public List<Voucher> getAllVouchers() {
    List<Voucher> list = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT id, code, discount_percent, max_discount, " +
        "min_order, expiry_date, usage_limit, used_count " +
        "FROM vouchers ORDER BY id DESC", null
    );
    while (cursor.moveToNext()) {
        list.add(new Voucher(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getInt(2),
            cursor.getDouble(3),
            cursor.getDouble(4),
            cursor.getString(5),
            cursor.getInt(6),
            cursor.getInt(7)
        ));
    }
    cursor.close();
    return list;
}

// Thêm voucher
public boolean addVoucher(String code, int discountPercent,
                          double maxDiscount, double minOrder,
                          String expiryDate, int usageLimit) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("code",             code);
    values.put("discount_percent", discountPercent);
    values.put("max_discount",     maxDiscount);
    values.put("min_order",        minOrder);
    values.put("expiry_date",      expiryDate);
    values.put("usage_limit",      usageLimit);
    values.put("used_count",       0);
    long result = db.insert("vouchers", null, values);
    return result != -1;
}

// Xóa voucher
public void deleteVoucher(int id) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete("vouchers", "id=?", new String[]{String.valueOf(id)});
}

// Kiểm tra voucher hợp lệ
public Voucher getVoucherByCode(String code) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(
        "SELECT id, code, discount_percent, max_discount, " +
        "min_order, expiry_date, usage_limit, used_count " +
        "FROM vouchers WHERE code=?",
        new String[]{code}
    );
    if (cursor.moveToFirst()) {
        Voucher v = new Voucher(
            cursor.getInt(0), cursor.getString(1),
            cursor.getInt(2), cursor.getDouble(3),
            cursor.getDouble(4), cursor.getString(5),
            cursor.getInt(6), cursor.getInt(7)
        );
        cursor.close();
        return v;
    }
    cursor.close();
    return null;
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favorites");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS order_detail");
        db.execSQL("DROP TABLE IF EXISTS vouchers");
        db.execSQL("DROP TABLE IF EXISTS food_order");
        db.execSQL("DROP TABLE IF EXISTS menu_item");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public int[] loginExtended(String identifier, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role, user_id FROM users WHERE (username=? OR email=?) AND password=?", 
                new String[]{identifier, identifier, password});
        if (cursor != null && cursor.moveToFirst()) {
            int[] res = {cursor.getInt(0), cursor.getInt(1)};
            cursor.close();
            return res;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public boolean registerUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username.trim());
        values.put("password", password.trim());
        values.put("email", email.trim());
        values.put("role", 0);
        return db.insert("users", null, values) != -1;
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_id FROM users WHERE username=?", new String[]{username.trim()});
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        return exists;
    }

    public Cursor getUserNotifications(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT notif_id, title, message, type, created_at, is_read " +
                "FROM notifications WHERE user_id = ? ORDER BY notif_id DESC",
                new String[]{String.valueOf(userId)}
        );
    }

    public void markNotificationsAsRead(int userId, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_read", 1);
        db.update("notifications", values, "user_id = ? AND type = ?", new String[]{String.valueOf(userId), type});
    }

    public int getTotalRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(total_amount) FROM food_order WHERE status != 'cancelled'", null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

    public int getTotalOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM food_order", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getTotalUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM users WHERE role = 0", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getTotalMenuItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM menu_item", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public List<Food> getAllFood() {
        List<Food> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT item_name, price, category, image_name FROM menu_item WHERE is_available = 1", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new Food(cursor.getString(0), String.valueOf(cursor.getInt(1)), 0, cursor.getString(2), cursor.getString(3)));
            }
            cursor.close();
        }
        return list;
    }

    public List<MenuItem> getAllMenuItemsAdmin() {
        List<MenuItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM menu_item", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MenuItem item = new MenuItem();
                item.itemId = cursor.getInt(0);
                item.itemName = cursor.getString(1);
                item.price = cursor.getInt(2);
                item.category = cursor.getString(3);
                item.imageName = cursor.getString(4);
                item.isAvailable = cursor.getInt(5);
                item.description = cursor.getString(6);
                list.add(item);
            }
            cursor.close();
        }
        return list;
    }

    public boolean addMenuItem(String name, int price, String category, String image, String desc, int available) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("item_name", name);
        v.put("price", price);
        v.put("category", category);
        v.put("image_name", image);
        v.put("description", desc);
        v.put("is_available", available);
        return db.insert("menu_item", null, v) != -1;
    }

    public void updateMenuItem(int id, String name, int price, String category, String image, String desc, int available) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("item_name", name);
        v.put("price", price);
        v.put("category", category);
        v.put("image_name", image);
        v.put("description", desc);
        v.put("is_available", available);
        db.update("menu_item", v, "item_id=?", new String[]{String.valueOf(id)});
    }

    public void deleteMenuItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("menu_item", "item_id=?", new String[]{String.valueOf(id)});
    }

    public List<FoodOrder> getAllOrders() {
        List<FoodOrder> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT o.order_id, o.user_id, u.username, o.total_amount, o.status, o.order_datetime " +
                     "FROM food_order o LEFT JOIN users u ON o.user_id = u.user_id " +
                     "ORDER BY o.order_id DESC";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                FoodOrder o = new FoodOrder();
                o.orderId = cursor.getInt(0);
                o.userId = cursor.getInt(1);
                o.username = cursor.getString(2);
                o.totalAmount = cursor.getInt(3);
                o.status = cursor.getString(4);
                o.orderDatetime = cursor.getString(5);
                list.add(o);
            }
            cursor.close();
        }
        return list;
    }

    public void updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("status", status);
        db.update("food_order", v, "order_id=?", new String[]{String.valueOf(orderId)});
    }

    public List<String> getOrderDetails(int orderId) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT m.item_name, d.unit_price FROM order_detail d " +
                     "JOIN menu_item m ON d.item_id = m.item_id " +
                     "WHERE d.order_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(orderId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0) + "|" + cursor.getInt(1));
            }
            cursor.close();
        }
        return list;
    }

    public Cursor getUserInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE username=?", new String[]{username});
    }
    public Cursor getUserInfo2(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT full_name, phone, address FROM Users WHERE user_id = ?", new String[]{String.valueOf(userId)});
    }

    public boolean updateUserInfo(String username, String fullName, String email, String address, String phone, String sex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("full_name", fullName);
        v.put("email", email);
        v.put("address", address);
        v.put("phone", phone);
        v.put("sex", sex);
        return db.update("users", v, "username=?", new String[]{username}) > 0;
    }

    public int placeOrder(int userId, int totalAmount, int voucherId, List<ShoppingCart> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues v = new ContentValues();
            v.put("user_id", userId);
            v.put("total_amount", totalAmount);
            v.put("status", "pending");
            if (voucherId != -1) v.put("voucher_id", voucherId);
            long orderId = db.insert("food_order", null, v);
            
            if (orderId == -1) return -1;

            for (ShoppingCart item : cartItems) {
                Cursor c = db.rawQuery("SELECT item_id, price FROM menu_item WHERE item_name=?", new String[]{item.getTen()});
                if (c.moveToFirst()) {
                    int itemId = c.getInt(0);
                    int price = c.getInt(1);
                    ContentValues dv = new ContentValues();
                    dv.put("order_id", orderId);
                    dv.put("item_id", itemId);
                    dv.put("quantity", item.getNum());
                    dv.put("unit_price", price);
                    db.insert("order_detail", null, dv);
                }
                c.close();
            }
            
            db.delete("cart", "user_id=?", new String[]{String.valueOf(userId)});
            
            db.setTransactionSuccessful();
            return (int) orderId;
        } catch (Exception e) {
            return -1;
        } finally {
            db.endTransaction();
        }
    }

    public void insertNotification(int userId, String title, String message, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("user_id", userId);
        v.put("title", title);
        v.put("message", message);
        v.put("type", type);
        db.insert("notifications", null, v);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users ORDER BY role DESC, username ASC", null);
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", "user_id=?", new String[]{String.valueOf(userId)});
    }

    public void updateUserRole(int userId, int role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("role", role);
        db.update("users", v, "user_id=?", new String[]{String.valueOf(userId)});
    }

    public boolean addUser(String username, String password, String email, int role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("username", username);
        v.put("password", password);
        v.put("email", email);
        v.put("role", role);
        return db.insert("users", null, v) != -1;
    }

    public void updateUserFullWithPass(int id, String username, String password, String email, int role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("username", username);
        v.put("password", password);
        v.put("email", email);
        v.put("role", role);
        db.update("users", v, "user_id=?", new String[]{String.valueOf(id)});
    }

    public boolean checkOldPassword(int userId, String hashedOldPass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE user_id = ? AND password = ?",
                new String[]{String.valueOf(userId), hashedOldPass});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
    public boolean updatePassword(int userId, String hashedNewPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", hashedNewPass);
        return db.update("users", values, "user_id = ?", new String[]{String.valueOf(userId)}) > 0;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public boolean updatePasswordByEmail(String email, String hashedPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", hashedPass);
        return db.update("users", values, "email = ?", new String[]{email}) > 0;
    }

    public boolean toggleFavorite(int userId, String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE user_id=? AND item_name=?", 
            new String[]{String.valueOf(userId), itemName});
        
        if (cursor.getCount() > 0) {
            db.delete("favorites", "user_id=? AND item_name=?", new String[]{String.valueOf(userId), itemName});
            cursor.close();
            return false;
        } else {
            ContentValues v = new ContentValues();
            v.put("user_id", userId);
            v.put("item_name", itemName);
            db.insert("favorites", null, v);
            cursor.close();
            return true;
        }
    }

    public boolean isFavorite(int userId, String itemName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE user_id=? AND item_name=?", 
            new String[]{String.valueOf(userId), itemName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<Food> getFavoriteFoods(int userId) {
        List<Food> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT m.item_name, m.price, m.category, m.image_name FROM menu_item m " +
                     "JOIN favorites f ON m.item_name = f.item_name WHERE f.user_id = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        while (c.moveToNext()) {
            list.add(new Food(c.getString(0), String.valueOf(c.getInt(1)), 0, c.getString(2), c.getString(3)));
        }
        c.close();
        return list;
    }
}
