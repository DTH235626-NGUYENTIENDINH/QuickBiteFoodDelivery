package com.example.quickbuyfooddelivery;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickbuyfooddelivery.adapters.ShoppingCartAdapter;
import com.example.quickbuyfooddelivery.models.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);

        RecyclerView recyclerView = findViewById(R.id.rcvItemCart);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<ShoppingCart> list = new ArrayList<>();
        list.add(new ShoppingCart(R.drawable.img_coca, "Coca", "15000 vnđ", 1));
        list.add(new ShoppingCart(R.drawable.img_mirinda, "Coca", "15000 vnđ", 1));
        list.add(new ShoppingCart(R.drawable.img_hbg1, "HamBurger gà", "28.000 vnđ", 1));

        ShoppingCartAdapter adapter = new ShoppingCartAdapter(list);
        recyclerView.setAdapter(adapter);

        //Thiết lập spinner
        Spinner spinner = findViewById(R.id.spnMaGiamGia);
        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Mã giảm giá");

        listSpinner.add("a");
        listSpinner.add("b");
        listSpinner.add("c");

        ArrayAdapter<String> adapterUuDai = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item, listSpinner
        );
        adapterUuDai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterUuDai);
        //Xoay chevron AI làm :))
        ImageView imgChevron = findViewById(R.id.imgChevron);
        // 1. Khi khách chạm vào Spinner để thả danh sách xuống -> Xoay mũi tên lên (180 độ)
        spinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                imgChevron.animate().rotation(180f).setDuration(300).start();
            }
            return false; // Phải return false để Spinner vẫn bung danh sách ra bình thường
        });

        // 2. Khi khách chọn xong 1 ưu đãi (hoặc bấm ra ngoài) -> Xoay mũi tên cụp xuống (0 độ)
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