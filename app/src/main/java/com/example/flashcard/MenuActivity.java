package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashcard.data.FirebaseSeeder;

public class MenuActivity  extends AppCompatActivity {
    private Button btnStudy;
    private Button btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnStudy = findViewById(R.id.btnStudy);
        btnSetting = findViewById(R.id.btnSetting);


        btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudyActivity();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseSeeder.uploadDataToFirestore();
            }
        });

    }

    private void openStudyActivity() {
        Intent intent = new Intent(this, LessonListActivity.class);
        startActivity(intent);
    }
}
