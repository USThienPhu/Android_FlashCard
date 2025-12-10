package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.flashcard.data.FirebaseSeeder;
import com.example.flashcard.data.LessonRepository;
import com.google.firebase.auth.FirebaseAuth;


public class MenuActivity  extends AppCompatActivity {
    private Button btnStudy;
    private Button btnSetting;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        btnStudy = findViewById(R.id.btnStudy);
        btnSetting = findViewById(R.id.btnSetting);
        btnLogout = findViewById(R.id.btnLogout);

        btnStudy.setOnClickListener(v -> openStudyActivity());
        btnLogout.setOnClickListener(v -> {
            // 1. Đăng xuất Firebase
            FirebaseAuth.getInstance().signOut();
            LessonRepository repository = new LessonRepository(getApplication());
            // 2. Xóa dữ liệu trong Room (Chạy ở background thread)
            repository.deleteAllLessons();

            // 3. Quay về màn hình đăng nhập
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseSeeder.uploadDataToFirestore();
                Toast.makeText(MenuActivity.this, "Setting comming soon", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openStudyActivity() {
        Intent intent = new Intent(this, LessonListActivity.class);
        startActivity(intent);
    }

    // Lỗi khi tạo mới lesson mới và thêm các flashcard thì sync được, khong đăng xuất và vào lại, thêm vào thì không sync
}
