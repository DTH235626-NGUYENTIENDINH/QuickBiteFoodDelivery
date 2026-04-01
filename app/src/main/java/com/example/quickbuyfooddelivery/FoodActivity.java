package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends BaseActivity {
    private FoodAdapter foodAdapter;
    private TextView btnAll, btnPizza, btnHamburger, btnDrinks;
    private EditText edtSearch;
    private ImageButton btnSearchAction;
    private ImageView imgeGiohang;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food);

        db = new DataBaseHelper(this);
        setupTaskbar(R.id.btnFood);
        initViews();
        setupRecyclerView();
        setupCategoryButtons();
        setupSearch();
        setupCartButton();
        updateCategoryUI("ALL");
    }

    private void initViews() {
        btnAll = findViewById(R.id.btnCategoryAll);
        btnPizza = findViewById(R.id.btnCategoryPizza);
        btnHamburger = findViewById(R.id.btnCategoryHamburger);
        btnDrinks = findViewById(R.id.btnCategoryDrinks);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearchAction = findViewById(R.id.btnSearchAction);
        imgeGiohang = findViewById(R.id.imgeGiohang);
    }

    private void setupCartButton() {
        if (imgeGiohang != null) {
            imgeGiohang.setOnClickListener(v -> {
                Intent intent = new Intent(FoodActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            });
        }
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
        List<Food> rawList = db.getAllFood();
        List<Food> finalFoodList = new ArrayList<>();
        DecimalFormat formatter = new DecimalFormat("#,###");

        for (Food item : rawList) {
            // Lấy ID ảnh từ tên ảnh trong DB
            int imageResId = getResources().getIdentifier(item.getImageName(), "drawable", getPackageName());
            if (imageResId == 0) imageResId = R.drawable.img_pz1; // Ảnh mặc định nếu không tìm thấy

            // Định dạng lại giá tiền có dấu chấm và "vnđ"
            String formattedPrice = "";
            try {
                formattedPrice = formatter.format(Long.parseLong(item.getPrice())) + " vnđ";
            } catch (Exception e) {
                formattedPrice = item.getPrice() + " vnđ";
            }

            finalFoodList.add(new Food(
                    item.getName(),
                    formattedPrice,
                    imageResId,
                    item.getCategory()
            ));
        }
        return finalFoodList;
    }

    private void updateCategoryUI(String category) {
        int selectedColor = ContextCompat.getColor(this, R.color.vàng);
        int unselectedColor = 0xFFE0E0E0;
        int white = ContextCompat.getColor(this, R.color.white);
        int gray = 0xFFBDBDBD;

        if (btnAll != null) {
            btnAll.setBackgroundTintList(ColorStateList.valueOf(category.equals("ALL") ? selectedColor : unselectedColor));
            btnAll.setTextColor(category.equals("ALL") ? white : gray);
        }
        if (btnPizza != null) {
            btnPizza.setBackgroundTintList(ColorStateList.valueOf(category.equals("PIZZA") ? selectedColor : unselectedColor));
            btnPizza.setTextColor(category.equals("PIZZA") ? white : gray);
        }
        if (btnHamburger != null) {
            btnHamburger.setBackgroundTintList(ColorStateList.valueOf(category.equals("HAMBURGER") ? selectedColor : unselectedColor));
            btnHamburger.setTextColor(category.equals("HAMBURGER") ? white : gray);
        }
        if (btnDrinks != null) {
            btnDrinks.setBackgroundTintList(ColorStateList.valueOf(category.equals("DRINK") ? selectedColor : unselectedColor));
            btnDrinks.setTextColor(category.equals("DRINK") ? white : gray);
        }

        if (foodAdapter != null) {
            foodAdapter.filter(category);
        }
    }
}
