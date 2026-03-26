package com.example.quickbuyfooddelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickbuyfooddelivery.R;
import com.example.quickbuyfooddelivery.models.ShoppingCart;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> {
    private List<ShoppingCart> mList;
    public ShoppingCartAdapter(List<ShoppingCart> shoppingCartList) {
        this.mList = shoppingCartList;
    }
    public static class ShoppingCartViewHolder extends  RecyclerView.ViewHolder {
        ImageView imgDoAn;
        TextView tvTenMon, tvDonGia, tvNumUp, tvNumDown, tvNum;
        public ShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDoAn = itemView.findViewById(R.id.imgDoAn);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvDonGia = itemView.findViewById(R.id.tvDonGia);
            tvNumUp = itemView.findViewById(R.id.tvUp);
            tvNumDown = itemView.findViewById(R.id.tvDown);
            tvNum = itemView.findViewById(R.id.tvNum);
        }
    }
    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shoppingcart, parent, false);
        return new ShoppingCartViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        ShoppingCart item = mList.get(position);
        if(item == null) return;
        holder.imgDoAn.setImageResource(item.getHinh());
        holder.tvTenMon.setText(item.getTen());
        holder.tvDonGia.setText(item.getGia());
        holder.tvNumUp.setOnClickListener(v -> {
            int soLuongHienTai = Integer.parseInt(holder.tvNum.getText().toString());
            soLuongHienTai++;
            holder.tvNum.setText(String.valueOf(soLuongHienTai));
            //item.setSoLuong(soLuongHienTai);
        });
        holder.tvNumDown.setOnClickListener(v -> {
            int soLuongHienTai = Integer.parseInt(holder.tvNum.getText().toString());

            if (soLuongHienTai > 1) {
                soLuongHienTai--;
                holder.tvNum.setText(String.valueOf(soLuongHienTai));

            } else {

            }
        });

    }
    @Override
    public int getItemCount() {
        return mList.size();
    }


}
