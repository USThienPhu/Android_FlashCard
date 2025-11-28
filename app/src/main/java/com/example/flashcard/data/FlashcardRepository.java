package com.example.flashcard.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class FlashcardRepository {

    private FlashcardDao mFlashcardDao;
    private LiveData<List<Flashcard>> mAllFlashcards;

    // Constructor: Khởi tạo Database và DAO
    public FlashcardRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mFlashcardDao = db.flashcardDao();
        mAllFlashcards = mFlashcardDao.getAllFlashcards();
    }

    // Trả về dữ liệu để ViewModel quan sát
    // Room tự động thực hiện truy vấn trả về LiveData ở background thread, nên không cần Executor ở đây
    public LiveData<List<Flashcard>> getAllFlashcards() {
        return mAllFlashcards;
    }

    // Tìm kiếm
    public LiveData<List<Flashcard>> searchFlashcards(String query) {
        return mFlashcardDao.searchFlashcards(query);
    }

    // Thêm thẻ (Phải chạy ở Background Thread)
    public void insert(Flashcard flashcard) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mFlashcardDao.insert(flashcard);
        });
    }

    // Xóa thẻ
    public void delete(Flashcard flashcard) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mFlashcardDao.delete(flashcard);
        });
    }

    // Cập nhật thẻ
    public void update(Flashcard flashcard) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mFlashcardDao.update(flashcard);
        });
    }
}