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
        // Chuyển title về chữ thường hết để dễ kiểm tra (đỡ lo viết hoa viết thường)
        String title = item.getTitle().toLowerCase();

        if (title.contains("huỷ")) {
            // Nếu có chữ "huỷ" -> Chữ màu đỏ
            holder.tvTitle.setTextColor(android.graphics.Color.RED);
        } else if (title.contains("thành công")) {
            // Nếu có chữ "thành công" -> Chữ màu xanh lá cây
            holder.tvTitle.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
        } else {
            // TRƯỜNG HỢP CÒN LẠI: Trả về màu mặc định (VD: màu đen)
            // Dòng này RẤT QUAN TRỌNG để khi cuộn danh sách lên xuống, các màu không bị lộn xộn
            holder.tvTitle.setTextColor(android.graphics.Color.BLACK);
        }
        // Xử lý khi nhấn vào nút mũi tên
        holder.btnExpand.setOnClickListener(v -> {
            // Đảo ngược trạng thái đóng/mở
            item.setExpanded(!item.isExpanded());

            if (item.isExpanded()) {
                holder.tvDetail.setMaxLines(Integer.MAX_VALUE); // Hiện đầy đủ chữ
                holder.btnExpand.setRotation(180); // Xoay mũi tên lên
            } else {
                holder.tvDetail.setMaxLines(1); // Thu gọn lại thành ...
                holder.btnExpand.setRotation(0); // Xoay mũi tên xuống
            }
        });
        holder.layoutItem.setOnClickListener(v -> {
            // Đảo ngược trạng thái đóng/mở
            item.setExpanded(!item.isExpanded());

            if (item.isExpanded()) {
                holder.tvDetail.setMaxLines(Integer.MAX_VALUE); // Hiện đầy đủ chữ
                holder.btnExpand.setRotation(180); // Xoay mũi tên lên
            } else {
                holder.tvDetail.setMaxLines(1); // Thu gọn lại thành ...
                holder.btnExpand.setRotation(0); // Xoay mũi tên xuống
            }
        });

    }
    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size(); // Trả về số lượng thực tế của danh sách
        }
        return 0; // Nếu danh sách trống thì trả về 0
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
