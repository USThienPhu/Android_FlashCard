package com.example.flashcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditFlashcardActivity extends AppCompatActivity {

    // Các hằng số (key) để gửi dữ liệu
    public static final String EXTRA_TOPIC = "com.example.flashcardapp.EXTRA_TOPIC";
    public static final String EXTRA_FRONT = "com.example.flashcardapp.EXTRA_FRONT";
    public static final String EXTRA_BACK = "com.example.flashcardapp.EXTRA_BACK";

    private EditText editTextTopic;
    private EditText editTextFront;
    private EditText editTextBack;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_flashcard);

        editTextTopic = findViewById(R.id.edit_text_topic);
        editTextFront = findViewById(R.id.edit_text_front);
        editTextBack = findViewById(R.id.edit_text_back);
        buttonSave = findViewById(R.id.button_save);

        // Thêm nút đóng (dấu X) ở góc trái
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Dòng này quan trọng để hiện nút
            setTitle("Thêm thẻ mới");
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFlashcard(); // Gọi hàm lưu
            }
        });
    }

    // Hàm để lưu dữ liệu
    private void saveFlashcard() {
        String topic = editTextTopic.getText().toString();
        String front = editTextFront.getText().toString();
        String back = editTextBack.getText().toString();

        // Kiểm tra xem có ô nào bị bỏ trống không
        if (topic.trim().isEmpty() || front.trim().isEmpty() || back.trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đóng gói dữ liệu vào Intent
        Intent data = new Intent();
        data.putExtra(EXTRA_TOPIC, topic);
        data.putExtra(EXTRA_FRONT, front);
        data.putExtra(EXTRA_BACK, back);

        // Gửi kết quả OK và dữ liệu về MainActivity
        setResult(RESULT_OK, data);
        finish(); // Đóng Activity này lại
    }
}