package com.example.quickbuyfooddelivery;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ManageFoodActivity extends AppCompatActivity {

    private RecyclerView rvFoodList;
    private EditText edtSearch;
    private TextView chipAll, chipPizza, chipBurger, chipDrink;
    private Button btnAddFood;
    private DataBaseHelper db;
    private List<MenuItem> fullList     = new ArrayList<>();
    private List<MenuItem> filteredList = new ArrayList<>();
    private AdminFoodAdapter adapter;
    private String currentCategory = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_food);

        db = new DataBaseHelper(this);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rvFoodList  = findViewById(R.id.rvFoodList);
        edtSearch   = findViewById(R.id.edtSearch);
        chipAll     = findViewById(R.id.chipAll);
        chipPizza   = findViewById(R.id.chipPizza);
        chipBurger  = findViewById(R.id.chipBurger);
        chipDrink   = findViewById(R.id.chipDrink);
        btnAddFood  = findViewById(R.id.btnAddFood);

        adapter = new AdminFoodAdapter(filteredList,
            item -> showFoodDialog(item),
            item -> confirmDelete(item)
        );
        rvFoodList.setLayoutManager(new LinearLayoutManager(this));
        rvFoodList.setAdapter(adapter);

        chipAll.setOnClickListener(v    -> filterByCategory("ALL"));
        chipPizza.setOnClickListener(v  -> filterByCategory("PIZZA"));
        chipBurger.setOnClickListener(v -> filterByCategory("HAMBURGER"));
        chipDrink.setOnClickListener(v  -> filterByCategory("DRINK"));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSearch(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnAddFood.setOnClickListener(v -> showFoodDialog(null));

        loadFoodList();
    }

    private void loadFoodList() {
        fullList = db.getAllMenuItemsAdmin();
        filterByCategory(currentCategory);
    }

    private void filterByCategory(String category) {
        currentCategory = category;

        chipAll.setBackgroundResource(category.equals("ALL")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipAll.setTextColor(category.equals("ALL") ? 0xFFFFFFFF : 0xFF777777);

        chipPizza.setBackgroundResource(category.equals("PIZZA")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipPizza.setTextColor(category.equals("PIZZA") ? 0xFFFFFFFF : 0xFF777777);

        chipBurger.setBackgroundResource(category.equals("HAMBURGER")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipBurger.setTextColor(category.equals("HAMBURGER") ? 0xFFFFFFFF : 0xFF777777);

        chipDrink.setBackgroundResource(category.equals("DRINK")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipDrink.setTextColor(category.equals("DRINK") ? 0xFFFFFFFF : 0xFF777777);

        filteredList.clear();
        for (MenuItem item : fullList) {
            if (category.equals("ALL") || item.category.equals(category)) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void filterSearch(String query) {
        filteredList.clear();
        for (MenuItem item : fullList) {
            boolean matchCat  = currentCategory.equals("ALL")
                || item.category.equals(currentCategory);
            boolean matchName = item.itemName.toLowerCase()
                .contains(query.toLowerCase());
            if (matchCat && matchName) filteredList.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    private void showFoodDialog(MenuItem editItem) {
        View view = LayoutInflater.from(this)
            .inflate(R.layout.dialog_food, null);

        TextView tvTitle    = view.findViewById(R.id.tvDialogTitle);
        EditText edtName    = view.findViewById(R.id.edtFoodName);
        EditText edtPrice   = view.findViewById(R.id.edtFoodPrice);
        Spinner  spinner    = view.findViewById(R.id.spinnerCategory);
        EditText edtImage   = view.findViewById(R.id.edtImageName);
        EditText edtDesc    = view.findViewById(R.id.edtDescription);
        Switch   swAvail    = view.findViewById(R.id.switchAvailable);
        Button   btnCancel  = view.findViewById(R.id.btnCancel);
        Button   btnSave    = view.findViewById(R.id.btnSave);

        String[] categories = {"PIZZA", "HAMBURGER", "DRINK"};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, categories
        );
        spinAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);

        if (editItem != null) {
            tvTitle.setText("Sửa món ăn");
            edtName.setText(editItem.itemName);
            edtPrice.setText(String.valueOf(editItem.price));
            edtImage.setText(editItem.imageName);
            edtDesc.setText(editItem.description);
            swAvail.setChecked(editItem.isAvailable == 1);
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(editItem.category)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(view).create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String name     = edtName.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String category = categories[spinner.getSelectedItemPosition()];
            String image    = edtImage.getText().toString().trim();
            String desc     = edtDesc.getText().toString().trim();
            int available   = swAvail.isChecked() ? 1 : 0;

            if (TextUtils.isEmpty(name)) {
                edtName.setError("Vui lòng nhập tên món");
                return;
            }
            if (TextUtils.isEmpty(priceStr)) {
                edtPrice.setError("Vui lòng nhập giá");
                return;
            }

            int price = Integer.parseInt(priceStr);

            if (editItem == null) {
                boolean ok = db.addMenuItem(
                    name, price, category, image, desc, available);
                if (ok) {
                    Toast.makeText(this,
                        "Thêm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                        "Tên món đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                db.updateMenuItem(editItem.itemId,
                    name, price, category, image, desc, available);
                Toast.makeText(this,
                    "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
            loadFoodList();
        });

        dialog.show();
    }

    private void confirmDelete(MenuItem item) {
        new AlertDialog.Builder(this)
            .setTitle("Xóa món ăn")
            .setMessage("Xóa \"" + item.itemName + "\"?")
            .setPositiveButton("Xóa", (d, w) -> {
                db.deleteMenuItem(item.itemId);
                Toast.makeText(this,
                    "Đã xóa!", Toast.LENGTH_SHORT).show();
                loadFoodList();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoodList();
    }
}