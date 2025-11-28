package com.example.flashcard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FlipActivity extends AppCompatActivity {

    private TextView textCard;
    private String front;
    private String back;
    private boolean showingFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flip_activity);

        textCard = findViewById(R.id.textCard);

        // Nhận dữ liệu từ MainActivity
        front = getIntent().getStringExtra("front");
        back = getIntent().getStringExtra("back");

        textCard.setText(front);

        // Click để flip
        textCard.setOnClickListener(v -> flipCard());
    }

    private void flipCard() {
        // Animation xoay 90 độ (ra khỏi màn hình)
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(textCard, "rotationY", 0f, 90f);
        animator1.setDuration(200);

        // Sau khi xoay 90°, đổi nội dung thẻ
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (showingFront) {
                    textCard.setText(back);
                } else {
                    textCard.setText(front);
                }
                showingFront = !showingFront;

                // Animation xoay tiếp từ -90 về 0 (xuất hiện lại)
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(textCard, "rotationY", -90f, 0f);
                animator2.setDuration(200);
                animator2.start();
            }

            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });

        animator1.start();
    }
}
