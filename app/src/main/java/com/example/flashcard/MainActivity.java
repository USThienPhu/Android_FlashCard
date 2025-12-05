package com.example.flashcard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.flashcard.data.Flashcard;
import com.example.flashcard.ui.FlashCard.FlashcardListAdapter;
import com.example.flashcard.viewmodel.FlashcardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FlashcardViewModel mFlashcardViewModel;
    private int currentLessonId = -1;
    Button btnShuffle;
    TextView tvLessonTitle;

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

                            if (id != -1) { // Update
                                Flashcard card = new Flashcard(front, back, currentLessonId);
                                card.setId(id);
                                mFlashcardViewModel.update(card);
                            } else { // Insert
                                mFlashcardViewModel.insert(front, back, currentLessonId);
                            }
                        } else {
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final FlashcardListAdapter adapter = new FlashcardListAdapter(new com.example.flashcard.ui.FlashCard.FlashcardClickListener() {
            @Override
            public void onItemClick(Flashcard flashcard) {
            }
            @Override
            public void onEditClick(Flashcard flashcard) {
                Intent intent = new Intent(MainActivity.this, AddEditFlashcardActivity.class);
                intent.putExtra(AddEditFlashcardActivity.EXTRA_FRONT, flashcard.getFrontText());
                intent.putExtra(AddEditFlashcardActivity.EXTRA_BACK, flashcard.getBackText());
                intent.putExtra(AddEditFlashcardActivity.EXTRA_ID, flashcard.getId());
                addEditLauncher.launch(intent);
            }
            @Override
            public void onDeleteClick(Flashcard flashcard) {
                mFlashcardViewModel.delete(flashcard);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 1. Khởi tạo ViewModel
        mFlashcardViewModel = new ViewModelProvider(this).get(FlashcardViewModel.class);

        // 2. Quan sát dữ liệu hiển thị (Bao gồm cả lúc load thường và lúc shuffle)
        mFlashcardViewModel.getDisplayedFlashcards().observe(this, flashcards -> {
            adapter.submitList(flashcards);
        });

        // 3. Lấy LessonID và báo cho ViewModel biết để load dữ liệu
        Intent lessonintent = getIntent();
        if (lessonintent.hasExtra("KEY_LESSON_ID")) {
            currentLessonId = lessonintent.getIntExtra("KEY_LESSON_ID", -1);
            if (currentLessonId != -1) {
                // Gọi hàm này để ViewModel bắt đầu lấy dữ liệu từ DB lên
                mFlashcardViewModel.setLessonId(currentLessonId);
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditFlashcardActivity.class);
            addEditLauncher.launch(intent);
        });

        // 4. Xử lý nút Shuffle
        btnShuffle = findViewById(R.id.btnShuffle);
        btnShuffle.setOnClickListener(v -> {
            // Gọi hàm shuffle trong ViewModel
            mFlashcardViewModel.shuffle();
            // Cuộn lên đầu trang cho dễ nhìn
            recyclerView.scrollToPosition(0);
        });

        // Trong onCreate()
        TextView tvLessonTitle = findViewById(R.id.tvLessonTitle); // Ánh xạ

        Intent intent = getIntent();
        if (intent.hasExtra("KEY_LESSON_NAME")) {
            String topicName = intent.getStringExtra("KEY_LESSON_NAME");
            // Hiển thị tên bài lên TextView thay vì setTitle (trên thanh Action Bar)
            tvLessonTitle.setText(topicName);
            // (Tùy chọn) Ẩn thanh Action Bar mặc định đi cho đẹp
//            if (getSupportActionBar() != null) getSupportActionBar().hide();
        }
    }
}