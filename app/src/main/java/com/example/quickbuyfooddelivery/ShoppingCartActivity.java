package com.example.quickbuyfooddelivery;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);

        db = new DataBaseHelper(this);
        tvThanhTien = findViewById(R.id.tvThanhTien);
        RecyclerView recyclerView = findViewById(R.id.rcvItemCart);
        
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            cartItems = CartManager.getCartList();
            adapter = new ShoppingCartAdapter(cartItems, this);
            recyclerView.setAdapter(adapter);
        }

        setupSpinner();
        updateTotal();
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
                // Reset spinner if needed or just don't apply discount
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
        // Add default option
        voucherList.add(new Voucher(-1, "Chọn mã giảm giá", 0, 0, 0));
        
        // Load from DB
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
}
