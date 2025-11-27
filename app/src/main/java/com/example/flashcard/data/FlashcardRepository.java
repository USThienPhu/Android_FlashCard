package com.example.flashcard.data;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
public class FlashcardRepository {
    private  FlashcardDao mFlashcardDao;
    private  LiveData<List<Flashcard>> mAllFlashcards;

    public FlashcardRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mFlashcardDao = db.flashcardDao();
        mAllFlashcards = mFlashcardDao.getAllFlashcards();
    }

    public LiveData<List<Flashcard>> getAllFlashcards() {
        return mAllFlashcards;
    }

    public LiveData<List<Flashcard>> searchFlashcards(String query) {
        return mFlashcardDao.searchFlashcards(query);
    }
    public void insert(Flashcard flashcard) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mFlashcardDao.insert(flashcard);
        });
    }

    public void delete(Flashcard flashcard) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mFlashcardDao.delete(flashcard);
        });
    }

    public void update(Flashcard flashcard) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mFlashcardDao.update(flashcard);
        });
    }
}
