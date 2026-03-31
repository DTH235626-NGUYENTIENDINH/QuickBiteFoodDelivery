package com.example.quickbuyfooddelivery;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ManageOrderActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private TextView chipAll, chipPending, chipConfirmed,
                     chipDelivering, chipDone, chipCancelled;
    private DataBaseHelper db;
    private List<FoodOrder> fullList     = new ArrayList<>();
    private List<FoodOrder> filteredList = new ArrayList<>();
    private OrderAdapter adapter;
    private String currentStatus = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);

        db = new DataBaseHelper(this);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rvOrders        = findViewById(R.id.rvOrders);
        chipAll         = findViewById(R.id.chipAll);
        chipPending     = findViewById(R.id.chipPending);
        chipConfirmed   = findViewById(R.id.chipConfirmed);
        chipDelivering  = findViewById(R.id.chipDelivering);
        chipDone        = findViewById(R.id.chipDone);
        chipCancelled   = findViewById(R.id.chipCancelled);

        adapter = new OrderAdapter(filteredList,
            order -> showOrderDetail(order),
            (order, action) -> updateStatus(order, action)
        );
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter);

        chipAll.setOnClickListener(v        -> filterByStatus("ALL"));
        chipPending.setOnClickListener(v    -> filterByStatus("pending"));
        chipConfirmed.setOnClickListener(v  -> filterByStatus("confirmed"));
        chipDelivering.setOnClickListener(v -> filterByStatus("delivering"));
        chipDone.setOnClickListener(v       -> filterByStatus("done"));
        chipCancelled.setOnClickListener(v  -> filterByStatus("cancelled"));

        loadOrders();
    }

    private void loadOrders() {
        fullList = db.getAllOrders();
        filterByStatus(currentStatus);
    }

    private void filterByStatus(String status) {
        currentStatus = status;
        updateChips(status);

        filteredList.clear();
        for (FoodOrder o : fullList) {
            if (status.equals("ALL") ||
                (o.status != null && o.status.equals(status))) {
                filteredList.add(o);    
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateChips(String status) {
        int active   = 0xFFFFFFFF;
        int inactive = 0xFF777777;

        chipAll.setBackgroundResource(status.equals("ALL")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipAll.setTextColor(status.equals("ALL") ? active : inactive);

        chipPending.setBackgroundResource(status.equals("pending")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipPending.setTextColor(status.equals("pending") ? active : inactive);

        chipConfirmed.setBackgroundResource(status.equals("confirmed")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipConfirmed.setTextColor(status.equals("confirmed") ? active : inactive);

        chipDelivering.setBackgroundResource(status.equals("delivering")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipDelivering.setTextColor(status.equals("delivering") ? active : inactive);

        chipDone.setBackgroundResource(status.equals("done")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipDone.setTextColor(status.equals("done") ? active : inactive);

        chipCancelled.setBackgroundResource(status.equals("cancelled")
            ? R.drawable.bg_chip_active : R.drawable.bg_chip_inactive);
        chipCancelled.setTextColor(status.equals("cancelled") ? active : inactive);
    }

    private void updateStatus(FoodOrder order, String newStatus) {
        String msg;
        switch (newStatus) {
            case "confirmed":  msg = "Xác nhận đơn #" + order.orderId + "?"; break;
            case "delivering": msg = "Bắt đầu giao đơn #" + order.orderId + "?"; break;
            case "done":       msg = "Hoàn thành đơn #" + order.orderId + "?"; break;
            case "cancelled":  msg = "Hủy đơn #" + order.orderId + "?"; break;
            default:           msg = "Cập nhật trạng thái?";
        }

        new AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton("Xác nhận", (d, w) -> {
                db.updateOrderStatus(order.orderId, newStatus);
                Toast.makeText(this, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
                loadOrders();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showOrderDetail(FoodOrder order) {
        View view = LayoutInflater.from(this)
            .inflate(R.layout.dialog_order_detail, null);

        TextView     tvTitle   = view.findViewById(R.id.tvDetailTitle);
        TextView     tvUser    = view.findViewById(R.id.tvDetailUser);
        TextView     tvTime    = view.findViewById(R.id.tvDetailTime);
        TextView     tvStatus  = view.findViewById(R.id.tvDetailStatus);
        TextView     tvTotal   = view.findViewById(R.id.tvDetailTotal);
        LinearLayout lyItems   = view.findViewById(R.id.layoutOrderItems);
        Spinner      spinner   = view.findViewById(R.id.spinnerStatus);
        Button       btnUpdate = view.findViewById(R.id.btnUpdateStatus);

        tvTitle.setText("Chi tiết #" + String.format("%04d", order.orderId));
        tvUser.setText(order.username != null ? order.username : "--");
        tvTime.setText(order.orderDatetime != null
            ? order.orderDatetime : "--");
        tvStatus.setText(getStatusText(order.status));
        tvTotal.setText(String.format("%,dđ", order.totalAmount));

        // Load món ăn trong đơn
        List<String> items = db.getOrderDetails(order.orderId);
        lyItems.removeAllViews();
        for (String item : items) {
            String[] parts = item.split("\\|");
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 4, 0, 4);

            TextView tvName = new TextView(this);
            tvName.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            tvName.setText(parts[0]);
            tvName.setTextSize(11);
            tvName.setTextColor(0xFF666666);

            TextView tvPrice = new TextView(this);
            tvPrice.setText(String.format("%,dđ", Integer.parseInt(parts[1])));
            tvPrice.setTextSize(11);
            tvPrice.setTextColor(0xFF7B1F1F);

            row.addView(tvName);
            row.addView(tvPrice);
            lyItems.addView(row);
        }

        // Spinner trạng thái
        String[] statuses  = {"pending", "confirmed", "delivering", "done", "cancelled"};
        String[] statusTxts = {"Chờ xác nhận", "Đã xác nhận", "Đang giao", "Hoàn thành", "Đã hủy"};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, statusTxts
        );
        spinAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);

        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(order.status)) {
                spinner.setSelection(i);
                break;
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(view).create();

        btnUpdate.setOnClickListener(v -> {
            String newStatus = statuses[spinner.getSelectedItemPosition()];
            db.updateOrderStatus(order.orderId, newStatus);
            Toast.makeText(this, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            loadOrders();
        });

        dialog.show();
    }

    private String getStatusText(String status) {
        if (status == null) return "--";
        switch (status) {
            case "pending":    return "Chờ xác nhận";
            case "confirmed":  return "Đã xác nhận";
            case "delivering": return "Đang giao";
            case "done":       return "Hoàn thành";
            case "cancelled":  return "Đã hủy";
            default:           return status;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}