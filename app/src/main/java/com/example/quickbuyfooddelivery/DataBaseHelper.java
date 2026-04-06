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
            "phone TEXT," +
            "email TEXT," +
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
                "type TEXT," + // Loại thông báo (VD: 'ORDER', 'SYSTEM')
                "created_at TEXT DEFAULT (datetime('now', 'localtime'))," +
                "is_read INTEGER DEFAULT 0," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                ")");

        db.execSQL("INSERT INTO users (username, password, phone, email, sex, full_name, address, role) " +
            "VALUES ('admin', '123','0312121212','admin@quickbuy.com', 'Nam', 'Quản Trị Viên','HCM', 1)");

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

        // Vouchers
        insertVoucher(db, "KM10", 10, 50000, 100000);
        insertVoucher(db, "QUICKBUY50", 50, 30000, 0);
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

    private void insertVoucher(SQLiteDatabase db, String code, int percent, double max, double min) {
        ContentValues v = new ContentValues();
        v.put("code", code);
        v.put("discount_percent", percent);
        v.put("max_discount", max);
        v.put("min_order", min);
        db.insertWithOnConflict("vouchers", null, v, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public List<Voucher> getAllVouchers() {
        List<Voucher> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, code, discount_percent, max_discount, min_order FROM vouchers", null);
        while (cursor.moveToNext()) {
            list.add(new Voucher(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getDouble(3),
                cursor.getDouble(4)
            ));
        }
        cursor.close();
        return list;
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

    // Trả về mảng {role, user_id}
    public int[] loginExtended(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT role, user_id FROM users WHERE username=? AND password=?",
            new String[]{username, password}
        );
        if (cursor.moveToFirst()) {
            int role = cursor.getInt(0);
            int userId = cursor.getInt(1);
            cursor.close();
            return new int[]{role, userId};
        }
        cursor.close();
        return null;
    }

    public int login(String username, String password) {
        int[] result = loginExtended(username, password);
        return (result != null) ? result[0] : -1;
    }

    public boolean registerUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username.trim());
        values.put("password", password.trim());
        values.put("email", email.trim());
        values.put("role", 0);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=?", new String[]{username.trim()});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
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
    //Phần User
        //Lấy user
    public android.database.Cursor getUserInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Lấy ra: Họ tên (0), Email (1), Địa chỉ (2), SĐT (3), Giới tính (4)
        return db.rawQuery(
                "SELECT full_name, email, address, phone, sex FROM users WHERE username=?",
                new String[]{username}
        );
    }
    // Cập nhật thông tin người dùng
    public boolean updateUserInfo(String username, String fullName, String email, String address, String phone, String sex) {
        SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();

        values.put("full_name", fullName);
        values.put("email", email);
        values.put("address", address);
        values.put("phone", phone);
        values.put("sex", sex);

        int result = db.update("users", values, "username=?", new String[]{username});
        return result > 0; // Trả về true nếu cập nhật thành công ít nhất 1 dòng
    }
    //Phần thong bao
        //Thêm thông báo mới vào DB
    public boolean insertNotification(int userId, String title, String message, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("title", title);
        values.put("message", message);
        values.put("type", type);

        long result = db.insert("notifications", null, values);
        return result != -1;
    }

        //Lấy danh sách thông báo của 1 user
    public Cursor getUserNotifications(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT notif_id, title, message, type, created_at, is_read " +
                        "FROM notifications WHERE user_id = ? ORDER BY notif_id DESC",
                new String[]{String.valueOf(userId)}
        );
    }
        //Đánh dấu thông báo đã đọc
        public void markNotificationsAsRead(int userId, String type) {
            SQLiteDatabase db = this.getWritableDatabase();
            android.content.ContentValues values = new android.content.ContentValues();
            values.put("is_read", 1);

            // Cập nhật theo UserID và Loại thông báo (ORDER hoặc PROMOTION)
            db.update("notifications", values, "user_id = ? AND type = ?",
                    new String[]{String.valueOf(userId), type});
        }

    //Dat đơn
    public int placeOrder(int userId, int totalAmount, int voucherId, java.util.List<com.example.quickbuyfooddelivery.models.ShoppingCart> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        int newOrderId = -1;

        db.beginTransaction();
        try {
            // 1. Tạo đơn hàng mới trong bảng food_order
            android.content.ContentValues orderValues = new android.content.ContentValues();
            orderValues.put("user_id", userId);
            orderValues.put("total_amount", totalAmount);
            orderValues.put("status", "pending");
            if (voucherId > 0) {
                orderValues.put("voucher_id", voucherId);
            }

            long insertedId = db.insert("food_order", null, orderValues);

            if (insertedId != -1) {
                newOrderId = (int) insertedId;

                // 2. Chép từng món vào order_detail
                for (com.example.quickbuyfooddelivery.models.ShoppingCart item : cartItems) {
                    int itemId = -1;

                    // TÌM ID BẰNG TÊN MÓN
                    Cursor cursor = db.rawQuery("SELECT item_id FROM menu_item WHERE item_name = ?", new String[]{item.getTen()});
                    if (cursor.moveToFirst()) {
                        itemId = cursor.getInt(0); // Lấy được ID rồi
                    }
                    cursor.close();

                    // Nếu tìm thấy ID thì mới nhét vào hóa đơn
                    if (itemId != -1) {
                        android.content.ContentValues detailValues = new android.content.ContentValues();
                        detailValues.put("order_id", newOrderId);
                        detailValues.put("item_id", itemId); // Dùng ID vừa tìm được
                        detailValues.put("quantity", item.getNum());
                        detailValues.put("unit_price", item.getPriceValue());

                        db.insert("order_detail", null, detailValues);
                    }
                }

                // 3. Xác nhận chốt đơn thành công
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return newOrderId;
    }
}
