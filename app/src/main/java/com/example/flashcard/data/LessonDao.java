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
    @Query("SELECT * FROM lessons order by displayOrder asc")
    LiveData<List<Lesson>> getAllLessons();

    @Query("DELETE FROM lessons")
    void deleteAllLessons();

    @Query("select count(*) from lessons")
    int noLesson();

    @Query("SELECT COALESCE(MAX(displayOrder), 0) FROM lessons")
    int getMaxDisplayOrder();

    @Query("SELECT * FROM lessons WHERE name = :name LIMIT 1")
    Lesson getLessonByName(String name);
}
