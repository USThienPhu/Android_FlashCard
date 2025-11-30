package com.example.flashcard.data;

import androidx.lifecycle.LiveData;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface LessonDao {
    @Insert
    long insert(Lesson lesson);
    @Delete
    void delete(Lesson lesson);
    @Query("SELECT * FROM lessons ORDER BY lessonId DESC")
    LiveData<List<Lesson>> getAllLessons();
}
