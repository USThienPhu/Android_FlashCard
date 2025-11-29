package com.example.flashcard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.flashcard.data.Lesson;
import com.example.flashcard.ui.Lesson.LessonListAdapter;
import com.example.flashcard.viewmodel.LessonViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.Toast;


public class LessonListActivity extends AppCompatActivity {
    private LessonViewModel lessonViewModel;
    ActivityResultLauncher<Intent> addLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null) return;
                            String lessonName = data.getStringExtra(AddLessonActivity.EXTRA_LESSON_NAME);
                            lessonViewModel.insert(lessonName);
                        } else {
                            Toast.makeText(this, "Hủy thao tác!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_lessons);
        LessonListAdapter adapter = new LessonListAdapter(new com.example.flashcard.ui.Lesson.LessonClickListener() {
            @Override
            public void onItemClick(Lesson lesson) {
                Toast.makeText(LessonListActivity.this, "Clicked: " + lesson.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LessonListActivity.this, MainActivity.class);
                intent.putExtra("KEY_LESSON_ID", lesson.getLessonId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Lesson lesson) {
                lessonViewModel.delete(lesson);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ViewModel
        lessonViewModel = new ViewModelProvider(this).get(LessonViewModel.class);

        lessonViewModel.getAllLessons().observe(this, adapter::submitList);

        // Click vào lesson → mở danh sách flashcard của lesson đó
//        adapter.setOnItemClickListener(lesson -> {
//            Toast.makeText(this, "Clicked: " + lesson.getName(), Toast.LENGTH_SHORT).show();
//            // TODO: Chuyển qua màn hình Flashcard theo LessonId
//            Intent intent = new Intent(LessonListActivity.this, MainActivity.class);
//            // Gửi ID bài học (Quan trọng nhất)
//            intent.putExtra("KEY_LESSON_ID", lesson.getLessonId());
//            // Gửi thêm tên để hiển thị tiêu đề cho đẹp (Tùy chọn)
//            startActivity(intent);
//        });

        FloatingActionButton fab = findViewById(R.id.fab_add_lesson);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLessonActivity.class);
            addLauncher.launch(intent);
        });

    }
}
