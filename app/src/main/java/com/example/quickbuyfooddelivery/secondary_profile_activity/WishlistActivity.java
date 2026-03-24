package com.example.quickbuyfooddelivery.secondary_profile_activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickbuyfooddelivery.R;
import com.example.quickbuyfooddelivery.adapters.WishlistAdapter;
import com.example.quickbuyfooddelivery.models.Wishlist;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        ImageView imgArrowBack = findViewById(R.id.imgArrowBack);
        imgArrowBack.setOnClickListener(v -> {
            finish();
        });

        RecyclerView rcvWishlist = findViewById(R.id.rcvWishlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvWishlist.setLayoutManager(layoutManager);
        List<Wishlist> list = new ArrayList<>();

        list.add(new Wishlist(R.drawable.img_7up, "7Up", "15.000 vnđ"));
        list.add(new Wishlist(R.drawable.img_coca, "CocaCola", "15.000 vnđ"));

        WishlistAdapter adapter = new WishlistAdapter(list);
        rcvWishlist.setAdapter(adapter);

    }
}