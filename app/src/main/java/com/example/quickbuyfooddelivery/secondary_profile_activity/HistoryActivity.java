package com.example.quickbuyfooddelivery.secondary_profile_activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickbuyfooddelivery.R;
import com.example.quickbuyfooddelivery.adapters.HistoryFoodAdapter;
import com.example.quickbuyfooddelivery.models.HistoryFood;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ImageView imgArrowBack = findViewById(R.id.imgArrowBack);
        imgArrowBack.setOnClickListener(v -> {
            finish();
        });

        RecyclerView rcvHistory = findViewById(R.id.rcvHistory);
        //Xep doc
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvHistory.setLayoutManager(layoutManager);
        List<HistoryFood> list = new ArrayList<>();
        //du lieu mau
        list.add(new HistoryFood(R.drawable.img_hbg3, "Hamburger Cá", "Hoàn thành", "x1", "21.000đ", "Tổng số tiền (1 món): 21.000đ", "10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Combo Gà Rán", "Đang giao", "x2", "99.000đ", "Tổng số tiền (2 món): 198.000đ","10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Pizza Hải Sản", "Đã hủy", "x1", "150.000đ", "Tổng số tiền (1 món): 150.000đ","10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Hamburger Cá", "Hoàn thành", "x1", "21.000đ", "Tổng số tiền (1 món): 21.000đ", "10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Combo Gà Rán", "Đang giao", "x2", "99.000đ", "Tổng số tiền (2 món): 198.000đ","10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Pizza Hải Sản", "Đã hủy", "x1", "150.000đ", "Tổng số tiền (1 món): 150.000đ","10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Hamburger Cá", "Hoàn thành", "x1", "21.000đ", "Tổng số tiền (1 món): 21.000đ", "10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Combo Gà Rán", "Đang giao", "x2", "99.000đ", "Tổng số tiền (2 món): 198.000đ","10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Pizza Hải Sản", "Đã hủy", "x1", "150.000đ", "Tổng số tiền (1 món): 150.000đ","10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Hamburger Cá", "Hoàn thành", "x1", "21.000đ", "Tổng số tiền (1 món): 21.000đ", "10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Combo Gà Rán", "Đang giao", "x2", "99.000đ", "Tổng số tiền (2 món): 198.000đ","10/03/2026"));
        list.add(new HistoryFood(R.drawable.img_hbg3, "Pizza Hải Sản", "Đã hủy", "x1", "150.000đ", "Tổng số tiền (1 món): 150.000đ","10/03/2026"));
        //khoi tao
        HistoryFoodAdapter adapter = new HistoryFoodAdapter(list);
        rcvHistory.setAdapter(adapter);
    }
}