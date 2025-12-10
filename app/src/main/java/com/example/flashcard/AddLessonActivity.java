package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddLessonActivity extends AppCompatActivity {

    public static final String EXTRA_LESSON_NAME = "com.example.flashcard.EXTRA_LESSON_NAME";

    private EditText editTextLessonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        editTextLessonName = findViewById(R.id.editTextLessonName);
        Button buttonSave = findViewById(R.id.buttonSaveLesson);

        buttonSave.setOnClickListener(v -> {
            String lessonName = editTextLessonName.getText().toString().trim();
            Intent data = new Intent();
            data.putExtra(EXTRA_LESSON_NAME, lessonName);
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
