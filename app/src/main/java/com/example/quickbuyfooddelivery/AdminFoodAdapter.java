package com.example.quickbuyfooddelivery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminFoodAdapter extends RecyclerView.Adapter<AdminFoodAdapter.ViewHolder> {

    public interface OnActionListener {
        void onAction(MenuItem item);
    }

    private List<MenuItem> list;
    private OnActionListener onEdit, onDelete;

    public AdminFoodAdapter(List<MenuItem> list,
                            OnActionListener onEdit,
                            OnActionListener onDelete) {
        this.list     = list;
        this.onEdit   = onEdit;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_food_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = list.get(position);

        holder.tvName.setText(item.itemName);
        holder.tvCategory.setText(item.category);
        holder.tvPrice.setText(String.format("%,dđ", item.price));

        if (item.isAvailable == 1) {
            holder.tvStatus.setText("Đang bán");
            holder.tvStatus.setTextColor(0xFF2E7D32);
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_green);
        } else {
            holder.tvStatus.setText("Hết hàng");
            holder.tvStatus.setTextColor(0xFFC62828);
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_red);
        }

        // Load ảnh từ drawable
        if (item.imageName != null && !item.imageName.isEmpty()) {
            int resId = holder.imgFood.getContext()
                .getResources().getIdentifier(
                    item.imageName, "drawable",
                    holder.imgFood.getContext().getPackageName()
                );
            if (resId != 0) {
                holder.imgFood.setImageResource(resId);
            } else {
                holder.imgFood.setImageResource(R.mipmap.ic_launcher);
            }
        }

        holder.btnEdit.setOnClickListener(v   -> onEdit.onAction(item));
        holder.btnDelete.setOnClickListener(v -> onDelete.onAction(item));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView  tvName, tvCategory, tvPrice, tvStatus;
        Button    btnEdit, btnDelete;

        ViewHolder(View v) {
            super(v);
            imgFood    = v.findViewById(R.id.imgFood);
            tvName     = v.findViewById(R.id.tvFoodName);
            tvCategory = v.findViewById(R.id.tvFoodCategory);
            tvPrice    = v.findViewById(R.id.tvFoodPrice);
            tvStatus   = v.findViewById(R.id.tvFoodStatus);
            btnEdit    = v.findViewById(R.id.btnEdit);
            btnDelete  = v.findViewById(R.id.btnDelete);
        }
    }
}