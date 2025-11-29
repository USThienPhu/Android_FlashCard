package com.example.flashcard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.flashcard.ui.FlashCard.FlashcardListAdapter;
import com.example.flashcard.viewmodel.FlashcardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_FLASHCARD_REQUEST = 1;
    private FlashcardViewModel mFlashcardViewModel;
    private int currentLessonId = -1;

    ActivityResultLauncher<Intent> addEditLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK) {

                            Intent data = result.getData();
                            if (data == null) return;

                            String front = data.getStringExtra(AddEditFlashcardActivity.EXTRA_FRONT);
                            String back = data.getStringExtra(AddEditFlashcardActivity.EXTRA_BACK);
                            int id = data.getIntExtra(AddEditFlashcardActivity.EXTRA_ID, -1);

                            if (id != -1) {
                                // UPDATE
                                Flashcard card = new Flashcard(front, back, currentLessonId);
                                card.setId(id);
                                mFlashcardViewModel.update(card);
                                Toast.makeText(this, "Đã cập nhật thẻ!", Toast.LENGTH_SHORT).show();
                            } else {
                                // INSERT
                                Flashcard newCard = new Flashcard(front, back, currentLessonId);
                                mFlashcardViewModel.insert(newCard);
                                Toast.makeText(this, "Đã tạo thẻ mới!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, "Hủy thao tác!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đảm bảo layout này có RecyclerView

        // 1. Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final FlashcardListAdapter adapter = new FlashcardListAdapter(new com.example.flashcard.ui.FlashCard.FlashcardClickListener() {
            @Override
            public void onItemClick(Flashcard flashcard) {
                // Để trống vì bạn đã lật thẻ ngay trong ViewHolder rồi
            }
            @Override
            public void onEditClick(Flashcard flashcard) {
                // --- LOGIC SỬA ---
                Intent intent = new Intent(MainActivity.this, AddEditFlashcardActivity.class);
                // Gửi dữ liệu cũ sang màn hình nhập liệu
                intent.putExtra(AddEditFlashcardActivity.EXTRA_FRONT, flashcard.getFrontText());
                intent.putExtra(AddEditFlashcardActivity.EXTRA_BACK, flashcard.getBackText());
                // QUAN TRỌNG: Gửi ID để biết đây là thẻ nào
                // (Bạn cần chắc chắn bên AddEditFlashcardActivity có biến EXTRA_ID, nếu chưa thì cứ dùng chuỗi cứng như dưới)
//                intent.putExtra("com.example.flashcard.EXTRA_ID", flashcard.getId());
                intent.putExtra(AddEditFlashcardActivity.EXTRA_ID, flashcard.getId());
//                startActivityForResult(intent, ADD_FLASHCARD_REQUEST);
                addEditLauncher.launch(intent);
            }
            @Override
            public void onDeleteClick(Flashcard flashcard) {
                // --- LOGIC XÓA ---
                mFlashcardViewModel.delete(flashcard);
                Toast.makeText(MainActivity.this, "Đã xóa thẻ!", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFlashcardViewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditFlashcardActivity.class);
            addEditLauncher.launch(intent);
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == ADD_FLASHCARD_REQUEST && resultCode == RESULT_OK) {
//            String front = data.getStringExtra(AddEditFlashcardActivity.EXTRA_FRONT);
//            String back = data.getStringExtra(AddEditFlashcardActivity.EXTRA_BACK);
//
//            // Lấy ID trả về (Mặc định là -1 nếu không có)
//            int id = data.getIntExtra("com.example.flashcard.EXTRA_ID", -1);
//            if (currentLessonId == -1) {
//                Toast.makeText(this, "Lỗi: Không xác định được bài học!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (id != -1) {
//                // --- TRƯỜNG HỢP SỬA (UPDATE) ---
//                Flashcard updateCard = new Flashcard(front, back, currentLessonId);
//                updateCard.setId(id); // Quan trọng: Phải gán đúng ID cũ
//                mFlashcardViewModel.update(updateCard);
//                Toast.makeText(this, "Đã cập nhật thẻ!", Toast.LENGTH_SHORT).show();
//            } else {
//                // --- TRƯỜNG HỢP THÊM MỚI (INSERT) ---
//                Flashcard newCard = new Flashcard(front, back, currentLessonId);
//                mFlashcardViewModel.insert(newCard);
//                Toast.makeText(this, "Đã lưu thẻ mới!", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "Hủy thao tác", Toast.LENGTH_SHORT).show();
//        }
//    }
}