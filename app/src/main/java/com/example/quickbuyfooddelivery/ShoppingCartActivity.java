package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickbuyfooddelivery.adapters.ShoppingCartAdapter;
import com.example.quickbuyfooddelivery.models.ShoppingCart;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity implements ShoppingCartAdapter.OnCartChangeListener {
    private TextView tvThanhTien;
    private ShoppingCartAdapter adapter;
    private List<ShoppingCart> cartItems;
    private DataBaseHelper db;
    private List<Voucher> voucherList;
    private Voucher selectedVoucher = null;
    private EditText edtHoTen, edtSDT, edtDiaChi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);

        db = new DataBaseHelper(this);
        tvThanhTien = findViewById(R.id.tvThanhTien);
        RecyclerView recyclerView = findViewById(R.id.rcvItemCart);
        edtHoTen = findViewById(R.id.editTextHoTen);
        edtSDT = findViewById(R.id.editTextSDT);
        edtDiaChi = findViewById(R.id.editTextDiaChi);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            cartItems = CartManager.getCartList();
            adapter = new ShoppingCartAdapter(cartItems, this);
            recyclerView.setAdapter(adapter);
        }
        
        loadUserInformation();
        setupSpinner();
        updateTotal();
    }

    private void loadUserInformation() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("user_id", -1);

        if (currentUserId != -1) {
            Cursor cursor = db.getUserInfo2(currentUserId);
            if (cursor != null && cursor.moveToFirst()) {
                // Lấy index của các cột theo tên để tránh lấy nhầm dữ liệu
                int fullNameIndex = cursor.getColumnIndex("full_name");
                int phoneIndex = cursor.getColumnIndex("phone");
                int addressIndex = cursor.getColumnIndex("address");
                int usernameIndex = cursor.getColumnIndex("username");

                String fullName = (fullNameIndex != -1) ? cursor.getString(fullNameIndex) : null;
                String phone = (phoneIndex != -1) ? cursor.getString(phoneIndex) : null;
                String address = (addressIndex != -1) ? cursor.getString(addressIndex) : null;
                String username = (usernameIndex != -1) ? cursor.getString(usernameIndex) : null;

                // Ưu tiên hiển thị Họ tên đầy đủ, nếu trống thì dùng Username
                String displayName = (fullName != null && !fullName.isEmpty()) ? fullName : username;

                if (displayName != null) edtHoTen.setText(displayName);
                if (phone != null) edtSDT.setText(phone);
                if (address != null) edtDiaChi.setText(address);

                cursor.close();
            }
        }
    }

    @Override
    public void onTotalChanged() {
        updateTotal();
    }

    private void updateTotal() {
        long subtotal = 0;
        for (ShoppingCart item : cartItems) {
            subtotal += item.getPriceValue() * item.getNum();
        }

        double discount = 0;
        if (selectedVoucher != null && selectedVoucher.getId() != -1) {
            if (subtotal >= selectedVoucher.getMinOrder()) {
                discount = subtotal * (selectedVoucher.getDiscountPercent() / 100.0);
                if (selectedVoucher.getMaxDiscount() > 0 && discount > selectedVoucher.getMaxDiscount()) {
                    discount = selectedVoucher.getMaxDiscount();
                }
            } else {
                Toast.makeText(this, "Đơn hàng chưa đủ tối thiểu " + (int)selectedVoucher.getMinOrder() + "đ để dùng mã này", Toast.LENGTH_SHORT).show();
                discount = 0;
            }
        }

        long finalTotal = subtotal - (long)discount;
        if (finalTotal < 0) finalTotal = 0;

        DecimalFormat formatter = new DecimalFormat("#,###");
        tvThanhTien.setText(formatter.format(finalTotal) + " vnđ");
    }

    private void setupSpinner() {
        Spinner spinner = findViewById(R.id.spnMaGiamGia);
        if (spinner == null) return;

        voucherList = new ArrayList<>();
        voucherList.add(new Voucher(-1, "Chọn mã giảm giá", 0, 0, 0));
        
        List<Voucher> dbVouchers = db.getAllVouchers();
        voucherList.addAll(dbVouchers);

        ArrayAdapter<Voucher> adapterUuDai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, voucherList);
        adapterUuDai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterUuDai);

        ImageView imgChevron = findViewById(R.id.imgChevron);
        if (imgChevron != null) {
            spinner.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    imgChevron.animate().rotation(180f).setDuration(300).start();
                }
                return false;
            });
            
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    imgChevron.animate().rotation(0f).setDuration(300).start();
                    selectedVoucher = voucherList.get(position);
                    updateTotal();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    imgChevron.animate().rotation(0f).setDuration(300).start();
                }
            });
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        android.widget.Button btnDatHang = findViewById(R.id.btnDatHang);
        if (btnDatHang != null) {
            btnDatHang.setOnClickListener(v -> handlePlaceOrder());
        }
    }

    private void handlePlaceOrder() {
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        String hoTen = edtHoTen.getText().toString().trim();
        String sdt = edtSDT.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();

        if (hoTen.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Họ tên, SĐT và Địa chỉ!", Toast.LENGTH_SHORT).show();
            return;
        }

        String strTongTien = tvThanhTien.getText().toString().replace(" vnđ", "").replace(",", "");
        int totalAmount = 0;
        try {
            totalAmount = Integer.parseInt(strTongTien);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        int voucherId = (selectedVoucher != null && selectedVoucher.getId() != -1) ? selectedVoucher.getId() : 0;

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("user_id", -1);

        int orderId = db.placeOrder(currentUserId, totalAmount, voucherId, cartItems);

        if (orderId != -1) {
            String title = "Cập nhật đơn hàng";
            String message = "Đơn hàng #" + orderId + " trị giá " + tvThanhTien.getText().toString() + " đã được xác nhận. Đang giao đến: " + diaChi;
            db.insertNotification(currentUserId, title, message, "ORDER");

            cartItems.clear();
            adapter.notifyDataSetChanged();
            tvThanhTien.setText("0 vnđ");

            Toast.makeText(this, "Chốt đơn thành công! Cảm ơn bạn.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(ShoppingCartActivity.this, NotificationOrderActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra, không thể đặt hàng!", Toast.LENGTH_SHORT).show();
        }
    }
}
