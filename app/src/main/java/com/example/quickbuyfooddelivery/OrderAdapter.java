package com.example.quickbuyfooddelivery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClick(FoodOrder order);
    }

    public interface OnActionListener {
        void onAction(FoodOrder order, String action);
    }

    private List<FoodOrder>  list;
    private OnClickListener  onDetail;
    private OnActionListener onAction;

    public OrderAdapter(List<FoodOrder> list,
                        OnClickListener onDetail,
                        OnActionListener onAction) {
        this.list     = list;
        this.onDetail = onDetail;
        this.onAction = onAction;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_order_admin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        FoodOrder o = list.get(position);

        h.tvId.setText("#" + String.format("%04d", o.orderId));
        h.tvTime.setText(o.orderDatetime != null
            ? o.orderDatetime.substring(5, 16) : "--");
        h.tvUser.setText(o.username != null ? o.username : "--");
        h.tvTotal.setText(String.format("%,dđ", o.totalAmount));

        // Trạng thái màu sắc
        switch (o.status != null ? o.status : "") {
            case "pending":
                h.tvStatus.setText("Chờ xác nhận");
                h.tvStatus.setTextColor(0xFFBF360C);
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_orange);
                h.btnAction1.setText("Xác nhận");
                h.btnAction1.setTextColor(0xFF0D47A1);
                h.btnAction1.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(0xFFE3F2FD));
                h.btnAction2.setText("Hủy đơn");
                h.layoutActions.setVisibility(View.VISIBLE);
                h.btnAction1.setOnClickListener(v ->
                    onAction.onAction(o, "confirmed"));
                h.btnAction2.setOnClickListener(v ->
                    onAction.onAction(o, "cancelled"));
                break;
            case "confirmed":
                h.tvStatus.setText("Đã xác nhận");
                h.tvStatus.setTextColor(0xFF0D47A1);
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_blue);
                h.btnAction1.setText("Bắt đầu giao");
                h.btnAction1.setTextColor(0xFF311B92);
                h.btnAction1.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(0xFFEDE7F6));
                h.btnAction2.setText("Hủy đơn");
                h.layoutActions.setVisibility(View.VISIBLE);
                h.btnAction1.setOnClickListener(v ->
                    onAction.onAction(o, "delivering"));
                h.btnAction2.setOnClickListener(v ->
                    onAction.onAction(o, "cancelled"));
                break;
            case "delivering":
                h.tvStatus.setText("Đang giao");
                h.tvStatus.setTextColor(0xFF311B92);
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_purple);
                h.btnAction1.setText("Hoàn thành");
                h.btnAction1.setTextColor(0xFF1B5E20);
                h.btnAction1.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(0xFFE8F5E9));
                h.btnAction2.setText("Hủy đơn");
                h.layoutActions.setVisibility(View.VISIBLE);
                h.btnAction1.setOnClickListener(v ->
                    onAction.onAction(o, "done"));
                h.btnAction2.setOnClickListener(v ->
                    onAction.onAction(o, "cancelled"));
                break;
            case "done":
                h.tvStatus.setText("Hoàn thành");
                h.tvStatus.setTextColor(0xFF1B5E20);
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_green);
                h.layoutActions.setVisibility(View.GONE);
                break;
            case "cancelled":
                h.tvStatus.setText("Đã hủy");
                h.tvStatus.setTextColor(0xFFB71C1C);
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_red);
                h.layoutActions.setVisibility(View.GONE);
                break;
            default:
                h.tvStatus.setText(o.status);
                h.layoutActions.setVisibility(View.GONE);
        }

        // Click xem chi tiết
        h.itemView.setOnClickListener(v -> onDetail.onClick(o));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView      tvId, tvTime, tvUser, tvTotal, tvStatus;
        Button        btnAction1, btnAction2;
        LinearLayout  layoutActions;

        ViewHolder(View v) {
            super(v);
            tvId         = v.findViewById(R.id.tvOrderId);
            tvTime       = v.findViewById(R.id.tvOrderTime);
            tvUser       = v.findViewById(R.id.tvOrderUser);
            tvTotal      = v.findViewById(R.id.tvOrderTotal);
            tvStatus     = v.findViewById(R.id.tvOrderStatus);
            btnAction1   = v.findViewById(R.id.btnAction1);
            btnAction2   = v.findViewById(R.id.btnAction2);
            layoutActions = v.findViewById(R.id.layoutActions);
        }
    }
}