package com.example.quickbuyfooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvRevenue, tvOrders, tvUsers, tvMenuItems;
    private LinearLayout btnManageFood, btnManageOrders, btnManageUsers, btnSettings;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = new DataBaseHelper(this);

        tvRevenue   = findViewById(R.id.tvRevenue);
        tvOrders    = findViewById(R.id.tvOrders);
        tvUsers     = findViewById(R.id.tvUsers);
        tvMenuItems = findViewById(R.id.tvMenuItems);

        btnManageFood   = findViewById(R.id.btnManageFood);
        btnManageOrders = findViewById(R.id.btnManageOrders);
        btnManageUsers  = findViewById(R.id.btnManageUsers);
        btnSettings     = findViewById(R.id.btnSettings);

        loadStats();

        btnManageFood.setOnClickListener(v ->
            startActivity(new Intent(this, ManageFoodActivity.class)));

        btnManageOrders.setOnClickListener(v ->
            startActivity(new Intent(this, ManageOrderActivity.class)));

        btnManageUsers.setOnClickListener(v ->
            startActivity(new Intent(this, AccountManagementActivity.class)));

        btnSettings.setOnClickListener(v ->
            startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void loadStats() {
        int revenue = db.getTotalRevenue();
        if (revenue >= 1000000) {
            tvRevenue.setText(String.format("%.0ftr%s",
                revenue / 1000000.0,
                revenue % 1000000 != 0 ? String.valueOf(revenue % 1000000 / 1000) : ""));
        } else {
            tvRevenue.setText(String.format("%,dđ", revenue));
        }

        tvOrders.setText(String.valueOf(db.getTotalOrders()));
        tvUsers.setText(String.valueOf(db.getTotalUsers()));
        tvMenuItems.setText(String.valueOf(db.getTotalMenuItems()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStats();
    }
}
