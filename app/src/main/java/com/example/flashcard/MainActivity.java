package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.flashcard.data.Flashcard;
import com.example.flashcard.ui.FlashcardListAdapter;
import com.example.flashcard.viewmodel.FlashcardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_FLASHCARD_REQUEST = 1;
    private FlashcardViewModel mFlashcardViewModel;
    private int currentLessonId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đảm bảo layout này có RecyclerView

        // 1. Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final FlashcardListAdapter adapter = new FlashcardListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 2. Kết nối ViewModel
        mFlashcardViewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);

        // 3. Quan sát dữ liệu (Observer)
        // Bất cứ khi nào database thay đổi, đoạn code trong onChanged sẽ chạy
//        mFlashcardViewModel.getAllFlashcards().observe(this, new androidx.lifecycle.Observer<List<Flashcard>>() {
//            @Override
//            public void onChanged(List<Flashcard> flashcards) {
//                // Cập nhật danh sách mới vào Adapter
//                adapter.submitList(flashcards);
//            }
//        });
        Intent lessonintent = getIntent();
        if (lessonintent.hasExtra("KEY_LESSON_ID")) {
            // 1. Lấy ID bài học ra và lưu vào biến toàn cục
            currentLessonId = lessonintent.getIntExtra("KEY_LESSON_ID", -1);
            // 2. Gọi hàm load dữ liệu theo ID (Sửa lỗi logic cũ của bạn ở đây)
            if (currentLessonId != -1) {
                mFlashcardViewModel.getFlashcardsByLessonId(currentLessonId).observe(this, new Observer<List<Flashcard>>() {
                    @Override
                    public void onChanged(List<Flashcard> flashcards) {
                        adapter.submitList(flashcards);
                    }
                });
            }
        }
        // 4. Xử lý click vào item (Ví dụ: Để sửa hoặc xem chi tiết lật mặt)
    //        adapter.setOnItemClickListener(flashcard -> {
//            Intent FlipIntent = new Intent(MainActivity.this, FlipActivity.class);
//            FlipIntent.putExtra("front", flashcard.getFrontText());
//            FlipIntent.putExtra("back", flashcard.getBackText());
//            startActivity(FlipIntent);
//        });
//        adapter.setOnItemClickListener();

        // Nút FAB để thêm mới (sẽ code ở bước sau)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditFlashcardActivity.class);
            // Mở Activity và CHỜ kết quả trả về
            startActivityForResult(intent, ADD_FLASHCARD_REQUEST);
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra đúng là phản hồi từ màn hình Thêm Flashcard và kết quả OK
        if (requestCode == ADD_FLASHCARD_REQUEST && resultCode == RESULT_OK) {
            String front = data.getStringExtra(AddEditFlashcardActivity.EXTRA_FRONT);
            String back = data.getStringExtra(AddEditFlashcardActivity.EXTRA_BACK);

            // Tạo flashcard mới và lưu vào Database thông qua ViewModel
            if (currentLessonId != -1) {
                Flashcard flashcard = new Flashcard(front, back, currentLessonId);
                mFlashcardViewModel.insert(flashcard);
                Toast.makeText(this, "Đã lưu thẻ nhớ vào bài học!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi: Không xác định được bài học!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Hủy lưu thẻ", Toast.LENGTH_SHORT).show();
        }
    }
}