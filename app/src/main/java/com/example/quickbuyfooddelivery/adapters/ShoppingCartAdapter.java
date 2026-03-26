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
    private OnCartChangeListener mListener;

    public interface OnCartChangeListener {
        void onTotalChanged();
    }

    public ShoppingCartAdapter(List<ShoppingCart> shoppingCartList, OnCartChangeListener listener) {
        this.mList = shoppingCartList;
        this.mListener = listener;
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
        if (item == null) return;

        holder.imgDoAn.setImageResource(item.getHinh());
        holder.tvTenMon.setText(item.getTen());
        holder.tvDonGia.setText(item.getGia());
        holder.tvNum.setText(String.valueOf(item.getNum()));

        holder.tvNumUp.setOnClickListener(v -> {
            item.setNum(item.getNum() + 1);
            notifyItemChanged(position);
            if (mListener != null) mListener.onTotalChanged();
        });

        holder.tvNumDown.setOnClickListener(v -> {
            if (item.getNum() > 1) {
                item.setNum(item.getNum() - 1);
                notifyItemChanged(position);
            } else {
                // Nếu số lượng về 1 và bấm giảm nữa -> Xóa khỏi giỏ hàng
                mList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mList.size());
            }
            if (mListener != null) mListener.onTotalChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
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
}
