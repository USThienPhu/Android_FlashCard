package com.example.flashcard.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "lessons")
public class Lesson {
    @PrimaryKey(autoGenerate = true)
    private int lessonId;
    private String name;
    private int displayOrder;

    @Ignore
    public Lesson(String name)
    {
        this.name = name;
    }

    public Lesson(String name, int displayOrder)
    {
        this.displayOrder = displayOrder;
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
    public int getDisplayOrder()
    {
        return this.displayOrder;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public void setDisplayOrder(int displayOrder)
    {
        this.displayOrder = displayOrder;
    }
}
