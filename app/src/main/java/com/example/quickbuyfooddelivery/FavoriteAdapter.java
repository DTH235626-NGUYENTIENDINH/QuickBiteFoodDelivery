package com.example.quickbuyfooddelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Food> favoriteList;
    private int userId;
    private DataBaseHelper db;

    public FavoriteAdapter(List<Food> favoriteList, int userId, Context context) {
        this.favoriteList = favoriteList;
        this.userId = userId;
        this.db = new DataBaseHelper(context);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Food food = favoriteList.get(position);
        holder.txtFavName.setText(food.getName());
        holder.txtFavPrice.setText(food.getPrice() + " vnđ");

        // Xử lý ảnh (tìm id từ tên ảnh)
        Context context = holder.itemView.getContext();
        int imageId = context.getResources().getIdentifier(food.getImageName(), "drawable", context.getPackageName());
        if (imageId != 0) {
            holder.imgFavFood.setImageResource(imageId);
        } else {
            holder.imgFavFood.setImageResource(R.drawable.img_pz1); // Ảnh mặc định
        }

        holder.chkFavHeart.setChecked(true);
        holder.chkFavHeart.setOnClickListener(v -> {
            db.toggleFavorite(userId, food.getName());
            favoriteList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteList.size());
            Toast.makeText(context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
        });

        holder.btnAddFromFav.setOnClickListener(v -> {
            CartManager.addToCart(food);
            Toast.makeText(context, "Đã thêm " + food.getName() + " vào giỏ", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFavFood;
        TextView txtFavName, txtFavPrice;
        CheckBox chkFavHeart;
        ImageButton btnAddFromFav;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFavFood = itemView.findViewById(R.id.imgFavFood);
            txtFavName = itemView.findViewById(R.id.txtFavName);
            txtFavPrice = itemView.findViewById(R.id.txtFavPrice);
            chkFavHeart = itemView.findViewById(R.id.chkFavHeart);
            btnAddFromFav = itemView.findViewById(R.id.btnAddFromFav);
        }
    }
}
