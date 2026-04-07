package com.example.quickbuyfooddelivery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ImageView btnBack = findViewById(R.id.btnBackFav);
        RecyclerView rvFavorites = findViewById(R.id.rvFavorites);

        btnBack.setOnClickListener(v -> finish());

        SharedPreferences pref = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        if (userId != -1) {
            DataBaseHelper db = new DataBaseHelper(this);
            List<Food> favList = db.getFavoriteFoods(userId);

            FavoriteAdapter adapter = new FavoriteAdapter(favList, userId, this);
            rvFavorites.setLayoutManager(new LinearLayoutManager(this));
            rvFavorites.setAdapter(adapter);
        }
    }
}
