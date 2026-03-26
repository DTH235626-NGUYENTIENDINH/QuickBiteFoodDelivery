package com.example.quickbuyfooddelivery;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);

        tvThanhTien = findViewById(R.id.tvThanhTien);
        RecyclerView recyclerView = findViewById(R.id.rcvItemCart);
        
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            cartItems = CartManager.getCartList();
            adapter = new ShoppingCartAdapter(cartItems, this);
            recyclerView.setAdapter(adapter);
        }

        updateTotal();
        setupSpinner();
    }

    @Override
    public void onTotalChanged() {
        updateTotal();
    }

    private void updateTotal() {
        long total = 0;
        for (ShoppingCart item : cartItems) {
            total += item.getPriceValue() * item.getNum();
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvThanhTien.setText(formatter.format(total) + " vnđ");
    }

    private void setupSpinner() {
        Spinner spinner = findViewById(R.id.spnMaGiamGia);
        if (spinner == null) return;

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Mã giảm giá");
        listSpinner.add("KM10 - Giảm 10%");
        listSpinner.add("FREESHIP");

        ArrayAdapter<String> adapterUuDai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listSpinner);
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
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    imgChevron.animate().rotation(0f).setDuration(300).start();
                }
            });
        }
    }
}
