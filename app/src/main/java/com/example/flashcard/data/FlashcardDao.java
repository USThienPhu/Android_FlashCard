package com.example.flashcard.data;

import androidx.lifecycle.LiveData;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FlashcardDao {
    @Insert
    void insert(Flashcard flashcard);
    @Delete
    void delete(Flashcard flashcard);
    @Query("SELECT * FROM flashcards ORDER BY id DESC")
    LiveData<List<Flashcard>> getAllFlashcards();

    @Query("SELECT * FROM flashcards WHERE frontText LIKE '%' || :searchQuery || '%' OR backText LIKE '%' || :searchQuery || '%'")
    LiveData<List<Flashcard>> searchFlashcards(String searchQuery);

    @Query("SELECT * FROM flashcards WHERE lessonOwnerId = :lessonId")
    LiveData<List<Flashcard>> getFlashcardsByLessonId(int lessonId);
    @Update
    void update(Flashcard flashcard);


}
