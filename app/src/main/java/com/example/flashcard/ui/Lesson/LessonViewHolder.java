package com.example.flashcard.ui.Lesson;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard.R;
import com.example.flashcard.data.Lesson;
//import com.example.flashcard.ui.LessonListAdapter;

class LessonViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewName;

    public LessonViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.textViewLessonName);
    }


    public void bind(Lesson lesson, LessonClickListener lis)
    {
        textViewName.setText(lesson.getName());
        itemView.setOnClickListener(v -> {
            if (lis != null) {
                lis.onItemClick(lesson);
            }
        });
    }
}