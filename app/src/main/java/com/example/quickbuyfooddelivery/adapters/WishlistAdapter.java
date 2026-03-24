package com.example.quickbuyfooddelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickbuyfooddelivery.R;
import com.example.quickbuyfooddelivery.models.Wishlist;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    private List<Wishlist> mList;
    public WishlistAdapter(List<Wishlist> mList) {
        this.mList = mList;
    }
    public static class WishlistViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDoAn;
        TextView tvTenMon, tvDonGia;

        ImageButton imgbtnAdd;
        CheckBox chkWishlist;

        public  WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDoAn = itemView.findViewById(R.id.imgAnhMon);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvDonGia = itemView.findViewById(R.id.tvDonGia);
            imgbtnAdd = itemView.findViewById(R.id.imgbtnAdd);
            chkWishlist = itemView.findViewById(R.id.chkWishlist);
        }
    }
    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Wishlist item = mList.get(position);
        if (item == null) return;

        holder.imgDoAn.setImageResource(item.getImageResource());
        holder.tvTenMon.setText(item.getName());
        holder.tvDonGia.setText(item.getPrice());

        //Xử lý sự kiện click cho nút Mua Lại
        holder.imgbtnAdd.setOnClickListener(v -> {
            //Thêm vào giỏ hàng
        });
        //Xử lý sự kiện click cho nút Xoá yêu thích
        holder.chkWishlist.setOnClickListener(v -> {
            //Xoá khỏi danh sách yêu thích
        });
    }
    @Override
    public int getItemCount() {
        return (mList != null) ? mList.size() : 0;
    }
}
