package com.example.quickbuyfooddelivery.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickbuyfooddelivery.R;
import com.example.quickbuyfooddelivery.models.NotificationFood1;
import java.util.List;

public class Notification1FoodAdapter extends RecyclerView.Adapter<Notification1FoodAdapter.NotificationFoodViewHolder> {
    private List<NotificationFood1> mList;
    public Notification1FoodAdapter(List<NotificationFood1> mList) {
        this.mList = mList;
    }
    @NonNull
    @Override
    public NotificationFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_food1, parent, false);
        return new NotificationFoodViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NotificationFoodViewHolder holder, int position) {
        NotificationFood1 item = mList.get(position);
        if(item == null){
            return;
        }
        holder.imgLogo.setImageResource(item.getLogo());
        holder.tvTitle.setText(item.getTitle());
        holder.tvDetail.setText(item.getDetail());
        
        String title = item.getTitle().toLowerCase();

        if (title.contains("huỷ")) {
            holder.tvTitle.setTextColor(android.graphics.Color.RED);
        } else if (title.contains("thành công")) {
            holder.tvTitle.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
        } else {
            holder.tvTitle.setTextColor(android.graphics.Color.BLACK);
        }

        if (item.isExpanded()) {
            holder.tvDetail.setMaxLines(Integer.MAX_VALUE);
            if (holder.btnExpand != null) holder.btnExpand.setRotation(180);
        } else {
            holder.tvDetail.setMaxLines(1);
            if (holder.btnExpand != null) holder.btnExpand.setRotation(0);
        }

        if (holder.btnExpand != null) {
            holder.btnExpand.setOnClickListener(v -> toggleExpand(item, holder));
        }
        
        if (holder.layoutItem != null) {
            holder.layoutItem.setOnClickListener(v -> toggleExpand(item, holder));
        }
    }

    private void toggleExpand(NotificationFood1 item, NotificationFoodViewHolder holder) {
        item.setExpanded(!item.isExpanded());
        if (item.isExpanded()) {
            holder.tvDetail.setMaxLines(Integer.MAX_VALUE);
            if (holder.btnExpand != null) holder.btnExpand.setRotation(180);
        } else {
            holder.tvDetail.setMaxLines(1);
            if (holder.btnExpand != null) holder.btnExpand.setRotation(0);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
    public static class NotificationFoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView tvTitle, tvDetail;
        ImageButton btnExpand;
        ConstraintLayout layoutItem;

        public NotificationFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.img_Logo);
            tvTitle = itemView.findViewById(R.id.tvTrangThaiItem);
            tvDetail = itemView.findViewById(R.id.tvChiTietItem);
            btnExpand = itemView.findViewById(R.id.btnExpand);
            layoutItem = itemView.findViewById(R.id.item_notification_food);
        }
    }
}
