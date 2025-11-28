package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.flashcard.data.Lesson;
import com.example.flashcard.ui.LessonListAdapter;
import com.example.flashcard.viewmodel.LessonViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.Toast;

import java.util.List;

public class LessonListActivity extends AppCompatActivity {

    private LessonViewModel lessonViewModel;
    public static final int ADD_LESSON_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_lessons);
        LessonListAdapter adapter = new LessonListAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ViewModel
        lessonViewModel = new ViewModelProvider(this).get(LessonViewModel.class);

        lessonViewModel.getAllLessons().observe(this, lessons -> {
            adapter.submitList(lessons);
        });

        // Click vào lesson → mở danh sách flashcard của lesson đó
        adapter.setOnItemClickListener(lesson -> {
            Toast.makeText(this, "Clicked: " + lesson.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Chuyển qua màn hình Flashcard theo LessonId
        });

        // Nút thêm Lesson
        FloatingActionButton fab = findViewById(R.id.fab_add_lesson);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLessonActivity.class);
            startActivityForResult(intent, ADD_LESSON_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_LESSON_REQUEST && resultCode == RESULT_OK) {
            String lessonName = data.getStringExtra(AddLessonActivity.EXTRA_LESSON_NAME);

            Lesson lesson = new Lesson(lessonName);
            lessonViewModel.insert(lesson);

            Toast.makeText(this, "Thêm Lesson thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Hủy thêm Lesson", Toast.LENGTH_SHORT).show();
        }
    }
}
