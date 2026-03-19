package com.example.quickbuyfooddelivery;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private FoodAdapter foodAdapter;
    private TextView btnAll, btnPizza, btnHamburger, btnDrinks;
    private EditText edtSearch;
    private ImageButton btnSearchAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food);

        setupTaskbar(R.id.btnFood);
        initViews();
        setupRecyclerView();
        setupCategoryButtons();
        setupSearch();
        updateCategoryUI("ALL");
    }

    private void initViews() {
        btnAll = findViewById(R.id.btnCategoryAll);
        btnPizza = findViewById(R.id.btnCategoryPizza);
        btnHamburger = findViewById(R.id.btnCategoryHamburger);
        btnDrinks = findViewById(R.id.btnCategoryDrinks);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearchAction = findViewById(R.id.btnSearchAction);
    }

    private void setupRecyclerView() {
        RecyclerView rcvFood = findViewById(R.id.rcvFood);
        foodAdapter = new FoodAdapter(getInitialFoodData());
        rcvFood.setLayoutManager(new GridLayoutManager(this, 2));
        rcvFood.setAdapter(foodAdapter);
    }

    private void setupCategoryButtons() {
        btnAll.setOnClickListener(v -> updateCategoryUI("ALL"));
        btnPizza.setOnClickListener(v -> updateCategoryUI("PIZZA"));
        btnHamburger.setOnClickListener(v -> updateCategoryUI("HAMBURGER"));
        btnDrinks.setOnClickListener(v -> updateCategoryUI("DRINK"));
    }

    private void setupSearch() {
        if (btnSearchAction != null && edtSearch != null) {
            btnSearchAction.setOnClickListener(v -> {
                String query = edtSearch.getText().toString();
                foodAdapter.search(query);
            });
        }
    }

    private List<Food> getInitialFoodData() {
        List<Food> list = new ArrayList<>();
        // Pizza
        list.add(new Food("Pizza Phô Mai", "45.000 vnđ", R.drawable.img_pz1, "PIZZA"));
        list.add(new Food("Pizza Hải Sản", "55.000 vnđ", R.drawable.img_pz2, "PIZZA"));
        list.add(new Food("Pizza Nấm", "45.000 vnđ", R.drawable.img_pz3, "PIZZA"));
        list.add(new Food("Pizza Xúc Xích Ý", "40.000 vnđ", R.drawable.img_pz4, "PIZZA"));
        list.add(new Food("Pizza Gà Nướng Dứa", "40.000 vnđ", R.drawable.img_pz5, "PIZZA"));
        list.add(new Food("Phô Mai Thêm", "45.000 vnđ", R.drawable.img_pz6, "PIZZA"));
        list.add(new Food("Pizza Rau Củ", "32.000 vnđ", R.drawable.img_pz7, "PIZZA"));
        list.add(new Food("Pizza Gà Phô Mai", "45.000 vnđ", R.drawable.img_pz8, "PIZZA"));
        list.add(new Food("Pizza Táo Mật Ong", "35.000 vnđ", R.drawable.img_pz7, "PIZZA"));
        // Hamburger
        list.add(new Food("Hamburger Gà", "28.000 vnđ", R.drawable.img_hbg1, "HAMBURGER"));
        list.add(new Food("Hamburger Bò", "30.000 vnđ", R.drawable.img_hbg2, "HAMBURGER"));
        list.add(new Food("Hamburger Tôm", "28.000 vnđ", R.drawable.img_hbg3, "HAMBURGER"));
        list.add(new Food("Hamburger Heo", "28.000 vnđ", R.drawable.img_hbg4, "HAMBURGER"));
        list.add(new Food("Hamburger Cá", "28.000 vnđ", R.drawable.img_hbg5, "HAMBURGER"));
        list.add(new Food("Bò Sốt Tiêu Đen", "45.000 vnđ", R.drawable.img_hbg6, "HAMBURGER"));
        list.add(new Food("Salad Trộn", "15.000 vnđ", R.drawable.img_hbg7, "HAMBURGER"));
        // Drinks
        list.add(new Food("Pepsi", "15.000 vnđ", R.drawable.img_pepsi, "DRINK"));
        list.add(new Food("Sting", "15.000 vnđ", R.drawable.img_sting, "DRINK"));
        list.add(new Food("7Up", "15.000 vnđ", R.drawable.img_7up, "DRINK"));
        list.add(new Food("Sprite", "15.000 vnđ", R.drawable.img_sprite, "DRINK"));
        list.add(new Food("Mirinda Cam", "15.000 vnđ", R.drawable.img_mirinda, "DRINK"));
        list.add(new Food("CocaCola", "15.000 vnđ", R.drawable.img_coca, "DRINK"));
        list.add(new Food("Nước Suối", "10.000 vnđ", R.drawable.img_water, "DRINK"));
        return list;
    }

    private void updateCategoryUI(String category) {
        int selectedColor = ContextCompat.getColor(this, R.color.vàng);
        int unselectedColor = 0xFFE0E0E0;
        int white = ContextCompat.getColor(this, R.color.white);
        int gray = 0xFFBDBDBD;

        btnAll.setBackgroundTintList(ColorStateList.valueOf(category.equals("ALL") ? selectedColor : unselectedColor));
        btnAll.setTextColor(category.equals("ALL") ? white : gray);
        btnPizza.setBackgroundTintList(ColorStateList.valueOf(category.equals("PIZZA") ? selectedColor : unselectedColor));
        btnPizza.setTextColor(category.equals("PIZZA") ? white : gray);
        btnHamburger.setBackgroundTintList(ColorStateList.valueOf(category.equals("HAMBURGER") ? selectedColor : unselectedColor));
        btnHamburger.setTextColor(category.equals("HAMBURGER") ? white : gray);
        btnDrinks.setBackgroundTintList(ColorStateList.valueOf(category.equals("DRINK") ? selectedColor : unselectedColor));
        btnDrinks.setTextColor(category.equals("DRINK") ? white : gray);

        foodAdapter.filter(category);
    }
}
