package com.example.quickbuyfooddelivery;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AccountManagementActivity extends AppCompatActivity {

    private ListView lvAdmin, lvUser;
    private DataBaseHelper db;
    private List<String> adminList = new ArrayList<>();
    private List<String> userList = new ArrayList<>();
    private List<Integer> adminIds = new ArrayList<>();
    private List<Integer> userIds = new ArrayList<>();
    private List<String> adminEmails = new ArrayList<>();
    private List<String> userEmails = new ArrayList<>();
    private List<String> adminPasswords = new ArrayList<>();
    private List<String> userPasswords = new ArrayList<>();
    private ArrayAdapter<String> adminAdapter, userAdapter;
    
    private int selectedAdminPos = -1;
    private int selectedUserPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        ImageView imgArrowBack = findViewById(R.id.btnBack);
        imgArrowBack.setOnClickListener(v -> finish());

        db = new DataBaseHelper(this);
        lvAdmin = findViewById(R.id.listView);
        lvUser = findViewById(R.id.listView1);

        adminAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adminList);
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);

        lvAdmin.setAdapter(adminAdapter);
        lvUser.setAdapter(userAdapter);

        loadAccounts();

        // Admin List Selection
        lvAdmin.setOnItemClickListener((parent, view, position, id) -> {
            selectedAdminPos = position;
            for (int i = 0; i < parent.getChildCount(); i++) {
                parent.getChildAt(i).setBackgroundColor(android.graphics.Color.TRANSPARENT);
            }
            view.setBackgroundColor(android.graphics.Color.LTGRAY);
        });

        // User List Selection
        lvUser.setOnItemClickListener((parent, view, position, id) -> {
            selectedUserPos = position;
            for (int i = 0; i < parent.getChildCount(); i++) {
                parent.getChildAt(i).setBackgroundColor(android.graphics.Color.TRANSPARENT);
            }
            view.setBackgroundColor(android.graphics.Color.LTGRAY);
        });

        // Admin Buttons
        findViewById(R.id.btnAdminAdd).setOnClickListener(v -> showAccountDialog(null, 1));
        findViewById(R.id.btnAdminEdit).setOnClickListener(v -> {
            if (selectedAdminPos != -1) {
                showAccountDialog(selectedAdminPos, 1);
            } else {
                Toast.makeText(this, "Chọn một admin để sửa", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btnAdminDelete).setOnClickListener(v -> {
            if (selectedAdminPos != -1) {
                confirmDelete(adminIds.get(selectedAdminPos));
            } else {
                Toast.makeText(this, "Chọn một admin để xóa", Toast.LENGTH_SHORT).show();
            }
        });

        // User Buttons
        findViewById(R.id.btnAdminAdd1).setOnClickListener(v -> showAccountDialog(null, 0));
        findViewById(R.id.btnAdminEdit1).setOnClickListener(v -> {
            if (selectedUserPos != -1) {
                showAccountDialog(selectedUserPos, 0);
            } else {
                Toast.makeText(this, "Chọn một user để sửa", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btnAdminDelete1).setOnClickListener(v -> {
            if (selectedUserPos != -1) {
                confirmDelete(userIds.get(selectedUserPos));
            } else {
                Toast.makeText(this, "Chọn một user để xóa", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnLuu).setOnClickListener(v -> finish());
    }

    private void loadAccounts() {
        adminList.clear(); adminIds.clear(); adminEmails.clear(); adminPasswords.clear();
        userList.clear(); userIds.clear(); userEmails.clear(); userPasswords.clear();
        selectedAdminPos = -1; selectedUserPos = -1;

        Cursor cursor = db.getAllUsers();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String pass = cursor.getString(2);
                String email = cursor.getString(4);
                int role = cursor.getInt(8);

                if (role == 1) {
                    adminList.add(name);
                    adminIds.add(id);
                    adminEmails.add(email);
                    adminPasswords.add(pass);
                } else {
                    userList.add(name);
                    userIds.add(id);
                    userEmails.add(email);
                    userPasswords.add(pass);
                }
            }
            cursor.close();
        }
        adminAdapter.notifyDataSetChanged();
        userAdapter.notifyDataSetChanged();
    }

    private void showAccountDialog(Integer position, int role) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        final EditText inputName = new EditText(this);
        inputName.setHint("Username");
        final EditText inputEmail = new EditText(this);
        inputEmail.setHint("Email");
        final EditText inputPass = new EditText(this);
        inputPass.setHint("Password");
        inputPass.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);
        layout.addView(inputName);
        
        // Chỉ thêm Email nếu là Admin hoặc là thêm mới tài khoản
        if (role == 1 || position == null) {
            layout.addView(inputEmail);
        }
        
        layout.addView(inputPass);

        if (position != null) {
            inputName.setText(role == 1 ? adminList.get(position) : userList.get(position));
            if (role == 1) {
                inputEmail.setText(adminEmails.get(position));
            }
            inputPass.setText(role == 1 ? adminPasswords.get(position) : userPasswords.get(position));
        }

        builder.setTitle(position == null ? "Thêm tài khoản" : "Sửa tài khoản")
                .setView(layout)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = inputName.getText().toString().trim();
                    String email = inputEmail.getText().toString().trim();
                    String pass = inputPass.getText().toString().trim();

                    if (name.isEmpty() || pass.isEmpty()) return;

                    String HashPass = HashUtils.hashPassword(pass);

                    if (position == null) {
                        db.addUser(name, HashPass, email, role);
                    } else {
                        int id = role == 1 ? adminIds.get(position) : userIds.get(position);
                        if (role == 0) {
                            // User chỉ sửa tên và pass, giữ nguyên email cũ
                            String oldEmail = userEmails.get(position);
                            db.updateUserFullWithPass(id, name, HashPass, oldEmail, role);
                        } else {
                            db.updateUserFullWithPass(id, name, HashPass, email, role);
                        }
                    }
                    loadAccounts();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void confirmDelete(int userId) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa tài khoản này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    db.deleteUser(userId);
                    loadAccounts();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
