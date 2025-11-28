package com.example.flashcard.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lessons")
public class Lesson {
    @PrimaryKey(autoGenerate = true)
    private int lessonId;
    private String name;

    public Lesson(String name)
    {
        this.name = name;
    }

    public int getLessonId()
    {
        return this.lessonId;
    }

    public void setLessonId(int id)
    {
        this.lessonId = id;
    }

    public String getName()
    {
        return this.name;
    }

}
