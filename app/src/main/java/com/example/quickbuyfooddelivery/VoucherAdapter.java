package com.example.quickbuyfooddelivery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private List<Voucher> voucherList;

    public VoucherAdapter(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher v = voucherList.get(position);

        holder.txtTitle.setText(v.getTitle());
        holder.txtDescription.setText(v.getDescription());
        holder.txtCode.setText(v.code);

        // Trạng thái còn lượt dùng không
        if (v.isAvailable()) {
            holder.txtStatus.setText("Còn " + (v.usageLimit - v.usedCount) + " lượt");
            holder.txtStatus.setTextColor(0xFF2E7D32);
        } else {
            holder.txtStatus.setText("Hết lượt");
            holder.txtStatus.setTextColor(0xFFB71C1C);
        }

        // Ngày hết hạn
        if (v.expiryDate != null && !v.expiryDate.isEmpty()) {
            holder.txtExpiry.setText("HSD: " + v.expiryDate);
        } else {
            holder.txtExpiry.setText("Không giới hạn");
        }

        // Icon mặc định
        holder.imgLogo.setImageResource(R.mipmap.ico_sale_nof);
    }

    @Override
    public int getItemCount() { return voucherList.size(); }

    static class VoucherViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView  txtTitle, txtDescription, txtCode, txtStatus, txtExpiry;

        VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo        = itemView.findViewById(R.id.imgVoucherLogo);
            txtTitle       = itemView.findViewById(R.id.txtVoucherTitle);
            txtDescription = itemView.findViewById(R.id.txtVoucherDescription);
            txtCode        = itemView.findViewById(R.id.txtVoucherCode);
            txtStatus      = itemView.findViewById(R.id.txtVoucherStatus);
            txtExpiry      = itemView.findViewById(R.id.txtVoucherExpiry);
        }
    }
}