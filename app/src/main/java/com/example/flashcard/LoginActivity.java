package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. QUAN TRỌNG: Ánh xạ View (Tìm view từ XML)
        // Hãy đảm bảo ID trong file XML của bạn khớp với các tên này
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvGuest = findViewById(R.id.tvGuest); // Giả sử nút Guest là TextView hoặc Button

        mAuth = FirebaseAuth.getInstance();

        // Kiểm tra nếu đã đăng nhập từ trước
        if (mAuth.getCurrentUser() != null) {
            goToMainActivity();
        }

        // Xử lý nút ĐĂNG NHẬP
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim(); // .trim() để xóa khoảng trắng thừa
            String password = edtPassword.getText().toString().trim();

            // 2. Validation: Kiểm tra rỗng trước khi gửi lên Firebase
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ Email và Mật khẩu!", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });
        TextView tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Xử lý nút GUEST
        tvGuest.setOnClickListener(v -> {
            loginAsGuest();
        });
    }

    private void loginUser(String email, String pass) {
        Log.d("Login", "check");
        // Đây chính là dòng lệnh "Check trên Firebase"
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        // 1. Firebase xác nhận: ĐÚNG Email và Password
                        Log.d("Login", "Đăng nhập thành công!");

                        // Reset cờ Guest (Quan trọng)
                        getSharedPreferences("AppPrefs", MODE_PRIVATE)
                                .edit()
                                .putBoolean("is_guest", false)
                                .apply();

                        goToMainActivity();

                    } else {
                        Log.d("Login", "Đăng nhập không thành công!");
                        String errorMessage;
                        try {
                            throw task.getException();
                        } catch(com.google.firebase.auth.FirebaseAuthInvalidUserException e) {
                            errorMessage = "Tài khoản này không tồn tại!";
                        } catch(com.google.firebase.auth.FirebaseAuthInvalidCredentialsException e) {
                            errorMessage = "Sai mật khẩu, vui lòng thử lại!";
                        } catch(Exception e) {
                            errorMessage = "Lỗi đăng nhập: " + e.getMessage();
                        }

                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loginAsGuest() {
        // Lưu cờ Guest = true
        getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("is_guest", true)
                .apply();

        Toast.makeText(this, "Đang vào chế độ Khách...", Toast.LENGTH_SHORT).show();
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}