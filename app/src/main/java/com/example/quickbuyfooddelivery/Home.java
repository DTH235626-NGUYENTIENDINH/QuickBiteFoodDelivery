package com.example.quickbuyfooddelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Home extends BaseActivity {
    private View drawerLayout;
    private ImageView btnMenu, btnCloseDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupTaskbar(R.id.btnHome);

        drawerLayout = findViewById(R.id.drawerLayout);
        btnMenu = findViewById(R.id.btnMenu);
        btnCloseDrawer = findViewById(R.id.btnCloseDrawer);

        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (drawerLayout != null) {
                    drawerLayout.setVisibility(View.VISIBLE);
                }
            });
        }

        if (btnCloseDrawer != null) {
            btnCloseDrawer.setOnClickListener(v -> {
                if (drawerLayout != null) {
                    drawerLayout.setVisibility(View.GONE);
                }
            });
        }
        
        // Close drawer if clicking on the dimmed background
        if (drawerLayout != null) {
            drawerLayout.setOnClickListener(v -> drawerLayout.setVisibility(View.GONE));
        }
    }
}
