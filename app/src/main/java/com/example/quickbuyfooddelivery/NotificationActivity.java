package com.example.quickbuyfooddelivery;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickbuyfooddelivery.adapters.Notification1FoodAdapter;
import com.example.quickbuyfooddelivery.models.NotificationFood1;
import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity {
    private RecyclerView rcvVoucher;
    private VoucherAdapter voucherAdapter;
    private List<Voucher> voucherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_voucher);
        setupTaskbar(R.id.btnNotification);

        rcvVoucher = findViewById(R.id.rcvVoucher);

        // Tạo dữ liệu mẫu cho Voucher
        voucherList = new ArrayList<>();
        voucherList.add(new Voucher("Có Giảm 25% Nè!", "DEAL CỰC CHILL-HAMBURGER CÁ GIẢM 25%", R.mipmap.ic_launcher));
        voucherList.add(new Voucher("Pizza Mua 1 Tặng 1", "Duy nhất hôm nay cho mọi loại Pizza cỡ lớn", R.mipmap.ic_launcher));
        voucherList.add(new Voucher("Miễn Phí Vận Chuyển", "Áp dụng cho đơn hàng từ 100.000 vnđ", R.mipmap.ic_launcher));
        voucherList.add(new Voucher("Giảm 10k Cho Nước", "Thưởng thức Pepsi/Sting lạnh giá chỉ 5k", R.mipmap.ic_launcher));

        voucherAdapter = new VoucherAdapter(voucherList);
        rcvVoucher.setLayoutManager(new LinearLayoutManager(this));
        rcvVoucher.setAdapter(voucherAdapter);

        // Nút quay lại (nếu có)
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        RecyclerView rcvCapNhatDonHang = findViewById(R.id.rcvCapNhatDonHang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvCapNhatDonHang.setLayoutManager(layoutManager);
        List<NotificationFood1> list = new ArrayList<>();
        list.add( new NotificationFood1(R.mipmap.ic_launcher, "Đơn hàng đã huỷ",
                "Đơn Pepsi-N11T02N2026_035021 đã huỷ gergre ghẻ h ehg ẻhg ẻth ẻt hẻ hử hư rh ew he ư fwe g fewf ewfeqfjukefqukfgqeofugfoqwfgofgfogo geqoig ogegroqegofrgqepofgeqi"));
        list.add( new NotificationFood1(R.mipmap.ic_launcher, "Đơn hàng đặt thành công",
                "Đơn Pepsi-N11T02N2026_035021 đã huỷ gergre ghẻ h ehg ẻhg ẻth ẻt hẻ hử hư rh ew he ư fwe g fewf ewfeqfjukefqukfgqeofugfoqwfgofgfogo geqoig ogegroqegofrgqepofgeqi"));
        Notification1FoodAdapter adapter = new Notification1FoodAdapter(list);
        rcvCapNhatDonHang.setAdapter(adapter);
    }
}
