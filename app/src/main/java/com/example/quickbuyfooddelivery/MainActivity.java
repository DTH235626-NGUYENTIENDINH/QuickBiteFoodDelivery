package com.example.quickbuyfooddelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridLayoutFood;
    private TextView btnAll, btnPizza, btnHamburger, btnDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        gridLayoutFood = findViewById(R.id.gridLayoutFood);
        btnAll = findViewById(R.id.btnCategoryAll);
        btnPizza = findViewById(R.id.btnCategoryPizza);
        btnHamburger = findViewById(R.id.btnCategoryHamburger);
        btnDrinks = findViewById(R.id.btnCategoryDrinks);

        btnAll.setOnClickListener(v -> filterFood("ALL"));
        btnPizza.setOnClickListener(v -> filterFood("PIZZA"));
        btnHamburger.setOnClickListener(v -> filterFood("HAMBURGER"));
        btnDrinks.setOnClickListener(v -> filterFood("DRINK"));
    }

    private void filterFood(String category) {
        // Reset colors
        btnAll.setBackgroundResource(R.drawable.o1);
        btnAll.setTextColor(0xFFBDBDBD);
        btnPizza.setBackgroundResource(R.drawable.o2);
        btnPizza.setTextColor(0xFFBDBDBD);
        btnHamburger.setBackgroundResource(R.drawable.o1);
        btnHamburger.setTextColor(0xFFBDBDBD);
        btnDrinks.setBackgroundResource(R.drawable.o3);
        btnDrinks.setTextColor(0xFFBDBDBD);

        // Highlight selected
        if (category.equals("ALL")) {
            btnAll.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFB300));
            btnAll.setTextColor(0xFFFFFFFF);
        } else if (category.equals("PIZZA")) {
            btnPizza.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFB300));
            btnPizza.setTextColor(0xFFFFFFFF);
        } else if (category.equals("HAMBURGER")) {
            btnHamburger.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFB300));
            btnHamburger.setTextColor(0xFFFFFFFF);
        } else if (category.equals("DRINK")) {
            btnDrinks.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFB300));
            btnDrinks.setTextColor(0xFFFFFFFF);
        }

        // Filter items in GridLayout
        for (int i = 0; i < gridLayoutFood.getChildCount(); i++) {
            View child = gridLayoutFood.getChildAt(i);
            Object tag = child.getTag();
            if (category.equals("ALL") || (tag != null && tag.toString().equals(category))) {
                child.setVisibility(View.VISIBLE);
            } else {
                child.setVisibility(View.GONE);
            }
        }
    }
}