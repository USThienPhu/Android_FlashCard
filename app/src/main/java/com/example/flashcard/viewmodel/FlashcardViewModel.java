package com.example.flashcard.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.flashcard.data.Flashcard;
import com.example.flashcard.data.FlashcardRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardViewModel extends AndroidViewModel{
    private  FlashcardRepository mRepository;
    private final MediatorLiveData<List<Flashcard>> mDisplayedFlashcards = new MediatorLiveData<>();
    private  LiveData<List<Flashcard>> mAllFlashcards;

    public FlashcardViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FlashcardRepository(application);
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
    public void insert(String front, String back, int id)
    {
        Flashcard card = new Flashcard(front, back, id);
        mRepository.insert(card);
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

    public LiveData<List<Flashcard>> getDisplayedFlashcards() {
        return mDisplayedFlashcards;
    }

    public void setLessonId(int lessonId) {
        // 1. Gỡ bỏ source cũ nếu có (tránh xung đột)
        if (mAllFlashcards != null) {
            mDisplayedFlashcards.removeSource(mAllFlashcards);
        }

        // 2. Lấy dữ liệu từ Repo theo ID
        mAllFlashcards = mRepository.getFlashcardsByLessonId(lessonId);

        // 3. Gắn vào Mediator để hiển thị lên UI
        mDisplayedFlashcards.addSource(mAllFlashcards, flashcards -> {
            mDisplayedFlashcards.setValue(flashcards);
        });
    }

    public void shuffle() {
        List<Flashcard> current = mDisplayedFlashcards.getValue();
        if (current != null && !current.isEmpty()) {
            List<Flashcard> shuffled = new ArrayList<>(current);
            Collections.shuffle(shuffled);
            mDisplayedFlashcards.setValue(shuffled);
        }
    }
}
