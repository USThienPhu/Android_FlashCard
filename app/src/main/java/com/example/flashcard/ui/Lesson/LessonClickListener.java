package com.example.flashcard.ui.Lesson;

import com.example.flashcard.data.Lesson;

public interface LessonClickListener {
    void onItemClick(Lesson lesson);
    void onDeleteClick(Lesson lesson);
}