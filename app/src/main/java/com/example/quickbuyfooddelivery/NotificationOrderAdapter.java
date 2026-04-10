package com.example.quickbuyfooddelivery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationOrderAdapter extends RecyclerView.Adapter<NotificationOrderAdapter.OrderViewHolder> {

    private List<NotificationOrder> orderList;

    public NotificationOrderAdapter(List<NotificationOrder> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        NotificationOrder order = orderList.get(position);
        holder.txtStatus.setText(order.getStatus());
        holder.txtStatus.setTextColor(order.getStatusColor());
        holder.txtMessage.setText(order.getMessage());
        holder.txtTime.setText(order.getTime());
        holder.imgLogo.setImageResource(order.getLogoResId());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView txtStatus, txtMessage, txtTime;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgOrderLogo);
            txtStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtMessage = itemView.findViewById(R.id.txtOrderMessage);
            txtTime = itemView.findViewById(R.id.txtOrderTime);
        }
    }
}
