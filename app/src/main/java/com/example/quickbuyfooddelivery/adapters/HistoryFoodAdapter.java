package com.example.quickbuyfooddelivery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickbuyfooddelivery.R;
import com.example.quickbuyfooddelivery.models.HistoryFood;

import java.util.List;

public class HistoryFoodAdapter extends RecyclerView.Adapter<HistoryFoodAdapter.HistoryViewHolder> {
    private List<HistoryFood> mListHistory;
    public HistoryFoodAdapter(List<HistoryFood> mListHistory) {
        this.mListHistory = mListHistory;
    }
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_food, parent, false);
        return new HistoryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        //Đổ dữ liệu từ List vào các View của từng dòng
        HistoryFood history = mListHistory.get(position);
        if (history == null) return;

        holder.imgDoAn.setImageResource(history.getImgDoAn());
        holder.tvTenMon.setText(history.getTenMon());
        holder.tvTrangThai.setText(history.getTrangThai());
        holder.tvSoLuong.setText(history.getSoLuong());
        holder.tvDonGia.setText(history.getDonGia());
        holder.tvTongTien.setText(history.getTongTien());
        holder.tvNgayMua.setText(history.getNgayMua());

        //Xử lý sự kiện click cho nút Mua Lại
        holder.btnMuaLai.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Đã thêm " + history.getTenMon() + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        //Xử lý sự kiện click cho nút Hủy Đơn
        holder.btnHuyDon.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Yêu cầu hủy đơn: " + history.getTenMon(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        //Trả về số lượng phần tử trong danh sách
        return (mListHistory != null) ? mListHistory.size() : 0;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDoAn;
        TextView tvTenMon, tvTrangThai, tvSoLuong, tvDonGia, tvTongTien, tvNgayMua;
        Button btnMuaLai, btnHuyDon;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDoAn = itemView.findViewById(R.id.imgDoAn);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvDonGia = itemView.findViewById(R.id.tvDonGia);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            btnMuaLai = itemView.findViewById(R.id.btnMuaLai);
            btnHuyDon = itemView.findViewById(R.id.btnHuyDon);
            tvNgayMua = itemView.findViewById(R.id.tvNgayMua);
        }
    }
}
