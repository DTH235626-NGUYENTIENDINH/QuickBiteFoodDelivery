package com.example.quickbuyfooddelivery;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.flexbox.FlexboxLayout;

public class MainActivity extends BaseActivity {

    private FlexboxLayout flexboxLayoutFood;
    private TextView btnAll, btnPizza, btnHamburger, btnDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setupTaskbar(R.id.btnFood);

        View mainView = findViewById(R.id.Wishlist);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize other views
        flexboxLayoutFood = findViewById(R.id.gridLayoutFood); // ID vẫn giữ là gridLayoutFood trong XML
        btnAll = findViewById(R.id.btnCategoryAll);
        btnPizza = findViewById(R.id.btnCategoryPizza);
        btnHamburger = findViewById(R.id.btnCategoryHamburger);
        btnDrinks = findViewById(R.id.btnCategoryDrinks);

        if (btnAll != null) btnAll.setOnClickListener(v -> filterFood("ALL"));
        if (btnPizza != null) btnPizza.setOnClickListener(v -> filterFood("PIZZA"));
        if (btnHamburger != null) btnHamburger.setOnClickListener(v -> filterFood("HAMBURGER"));
        if (btnDrinks != null) btnDrinks.setOnClickListener(v -> filterFood("DRINK"));

        // Initialize state
        filterFood("ALL");
    }

    private void filterFood(String category) {
        // Colors
        int colorSelected = 0xFFFFB300;
        int colorUnselected = 0xFFE0E0E0;
        int textSelected = 0xFFFFFFFF;
        int textUnselected = 0xFFBDBDBD;

        // Reset all buttons to unselected state
        if (btnAll != null) {
            btnAll.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
            btnAll.setTextColor(textUnselected);
        }
        if (btnPizza != null) {
            btnPizza.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
            btnPizza.setTextColor(textUnselected);
        }
        if (btnHamburger != null) {
            btnHamburger.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
            btnHamburger.setTextColor(textUnselected);
        }
        if (btnDrinks != null) {
            btnDrinks.setBackgroundTintList(ColorStateList.valueOf(colorUnselected));
            btnDrinks.setTextColor(textUnselected);
        }

        // Highlight selected button
        if (category.equals("ALL") && btnAll != null) {
            btnAll.setBackgroundTintList(ColorStateList.valueOf(colorSelected));
            btnAll.setTextColor(textSelected);
        } else if (category.equals("PIZZA") && btnPizza != null) {
            btnPizza.setBackgroundTintList(ColorStateList.valueOf(colorSelected));
            btnPizza.setTextColor(textSelected);
        } else if (category.equals("HAMBURGER") && btnHamburger != null) {
            btnHamburger.setBackgroundTintList(ColorStateList.valueOf(colorSelected));
            btnHamburger.setTextColor(textSelected);
        } else if (category.equals("DRINK") && btnDrinks != null) {
            btnDrinks.setBackgroundTintList(ColorStateList.valueOf(colorSelected));
            btnDrinks.setTextColor(textSelected);
        }

        if (flexboxLayoutFood != null) {
            for (int i = 0; i < flexboxLayoutFood.getChildCount(); i++) {
                View child = flexboxLayoutFood.getChildAt(i);
                Object tag = child.getTag();
                if (category.equals("ALL") || (tag != null && tag.toString().equals(category))) {
                    child.setVisibility(View.VISIBLE);
                } else {
                    child.setVisibility(View.GONE);
                }
            }
        }
    }
}
