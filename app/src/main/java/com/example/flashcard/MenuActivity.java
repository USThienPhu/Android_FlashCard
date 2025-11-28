package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity  extends AppCompatActivity {
    private Button btnStudy;
    private Button btnSetting;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnStudy = findViewById(R.id.btnStudy);
        btnSetting = findViewById(R.id.btnSetting);
        btnExit = findViewById(R.id.btnExit);


        btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudyActivity();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void openStudyActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
