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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);
        holder.txtTitle.setText(voucher.getTitle());
        holder.txtDescription.setText(voucher.getDescription());
        holder.imgLogo.setImageResource(voucher.getImageResId());
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    static class VoucherViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView txtTitle, txtDescription;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgVoucherLogo);
            txtTitle = itemView.findViewById(R.id.txtVoucherTitle);
            txtDescription = itemView.findViewById(R.id.txtVoucherDescription);
        }
    }
}
