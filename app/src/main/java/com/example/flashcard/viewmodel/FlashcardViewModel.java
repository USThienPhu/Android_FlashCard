package com.example.flashcard.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flashcard.data.Flashcard;
import com.example.flashcard.data.FlashcardRepository;
import java.util.List;

public class FlashcardViewModel extends AndroidViewModel{
    private  FlashcardRepository mRepository;
    private final LiveData<List<Flashcard>> mAllFlashcards;

    public FlashcardViewModel(@NonNull Application application) {
        super(application);
        // Khởi tạo Repository
        mRepository = new FlashcardRepository(application);
        // Lấy dữ liệu ngay khi ViewModel được tạo
        mAllFlashcards = mRepository.getAllFlashcards();
    }

    // UI sẽ gọi hàm này để lấy danh sách thẻ và hiển thị
    public LiveData<List<Flashcard>> getAllFlashcards() {
        return mAllFlashcards;
    }

    // UI gọi hàm này để tìm kiếm
    public LiveData<List<Flashcard>> searchFlashcards(String query) {
        return mRepository.searchFlashcards(query);
    }

    // UI gọi hàm này để thêm thẻ mới
    public void insert(Flashcard flashcard) {
        mRepository.insert(flashcard);
    }

    // UI gọi hàm này để xóa thẻ
    public void delete(Flashcard flashcard) {
        mRepository.delete(flashcard);
    }

    // UI gọi hàm này để cập nhật (ví dụ: đánh dấu đã thuộc)
    public void update(Flashcard flashcard) {
        mRepository.update(flashcard);
    }
    public LiveData<List<Flashcard>> getFlashcardsByLessonId(int lessonId) {
        return mRepository.getFlashcardsByLessonId(lessonId);
    }
}
