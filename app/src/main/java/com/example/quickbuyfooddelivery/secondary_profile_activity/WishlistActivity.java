package com.example.quickbuyfooddelivery.secondary_profile_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickbuyfooddelivery.DataBaseHelper;
import com.example.quickbuyfooddelivery.FavoriteAdapter;
import com.example.quickbuyfooddelivery.Food;
import com.example.quickbuyfooddelivery.R;

import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ImageView imgBack = findViewById(R.id.btnBackFav);
        imgBack.setOnClickListener(v -> finish());

        RecyclerView rcvWishlist = findViewById(R.id.rvFavorites);
        rcvWishlist.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences pref = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        if (userId != -1) {
            DataBaseHelper db = new DataBaseHelper(this);
            List<Food> list = db.getFavoriteFoods(userId);

            FavoriteAdapter adapter = new FavoriteAdapter(list, userId, this);
            rcvWishlist.setAdapter(adapter);
        }
    }
}
