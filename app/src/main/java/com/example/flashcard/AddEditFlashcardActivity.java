package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditFlashcardActivity extends AppCompatActivity {
    public static final String EXTRA_FRONT = "com.example.flashcard.EXTRA_FRONT";
    public static final String EXTRA_BACK = "com.example.flashcard.EXTRA_BACK";
    public static final String EXTRA_ID = "com.example.flashcard.EXTRA_ID";
    private EditText editTextFront;
    private EditText editTextBack;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_flashcard);
        editTextFront = findViewById(R.id.edit_text_front);
        editTextBack = findViewById(R.id.edit_text_back);
        buttonSave = findViewById(R.id.button_save);
        // 2. KIỂM TRA INTENT: Có phải đang sửa không?
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            // Nếu có ID -> Đang là chế độ SỬA
            setTitle("Sửa thẻ flashcard");
            editTextFront.setText(intent.getStringExtra(EXTRA_FRONT)); // Điền dữ liệu cũ vào
            editTextBack.setText(intent.getStringExtra(EXTRA_BACK));
            buttonSave.setText("Cập nhật"); // Đổi tên nút cho hợp lý
        } else {
            // Nếu không có ID -> Đang là chế độ THÊM MỚI
            setTitle("Thêm thẻ mới");
            buttonSave.setText("Lưu thẻ");
        }
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFlashcard();
            }
        });
    }
    private void saveFlashcard() {
        String front = editTextFront.getText().toString();
        String back = editTextBack.getText().toString();

        if (front.trim().isEmpty() || back.trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_FRONT, front);
        data.putExtra(EXTRA_BACK, back);
        // 3. QUAN TRỌNG: Gửi trả lại ID (nếu đang sửa)
        // Lấy ID từ Intent ban đầu, mặc định là -1 nếu không có
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }
}