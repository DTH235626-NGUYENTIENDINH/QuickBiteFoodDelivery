package com.example.quickbuyfooddelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foodList;
    private List<Food> filteredList;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
        this.filteredList = new ArrayList<>(foodList);
    }

    public void filter(String category) {
        filteredList.clear();
        if (category.equals("ALL")) {
            filteredList.addAll(foodList);
            Collections.shuffle(filteredList);
        } else {
            for (Food food : foodList) {
                if (food.getCategory().equals(category)) {
                    filteredList.add(food);
                }
            }
        }
        notifyDataSetChanged();
    }

    private String removeAccents(String str) {
        if (str == null) return "";
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("")
                .replace('đ', 'd').replace('Đ', 'D');
    }

    public void search(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(foodList);
        } else {
            String queryNormalized = removeAccents(query.toLowerCase().trim());
            for (Food food : foodList) {
                String nameNormalized = removeAccents(food.getName().toLowerCase());
                if (nameNormalized.contains(queryNormalized)) {
                    filteredList.add(food);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = filteredList.get(position);
        holder.txtFoodName.setText(food.getName());
        holder.txtFoodPrice.setText(food.getPrice());
        
        // Load ảnh
        Context context = holder.itemView.getContext();
        int imageId = context.getResources().getIdentifier(food.getImageName(), "drawable", context.getPackageName());
        if (imageId != 0) {
            holder.imgFood.setImageResource(imageId);
        }

        DataBaseHelper db = new DataBaseHelper(context);
        SharedPreferences pref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        // Hiển thị trạng thái yêu thích từ DB
        if (userId != -1) {
            holder.chkWishlist.setChecked(db.isFavorite(userId, food.getName()));
        }

        // Xử lý Click Trái tim
        holder.chkWishlist.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(context, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
                holder.chkWishlist.setChecked(false);
                return;
            }
            
            // Toggle trong DB
            db.toggleFavorite(userId, food.getName());
            
            // Cập nhật lại CheckBox theo DB để đảm bảo chính xác
            boolean nowFavorite = db.isFavorite(userId, food.getName());
            holder.chkWishlist.setChecked(nowFavorite);
            
            if (nowFavorite) {
                Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnThemVao.setOnClickListener(v -> {
            CartManager.addToCart(food);
            Toast.makeText(context, "Đã thêm " + food.getName() + " vào giỏ", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        Button btnThemVao;
        TextView txtFoodName, txtFoodPrice;
        CheckBox chkWishlist;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtFoodPrice = itemView.findViewById(R.id.txtFoodPrice);
            btnThemVao = itemView.findViewById(R.id.btnThemVao);
            chkWishlist = itemView.findViewById(R.id.chkWishlist);
        }
    }
}
