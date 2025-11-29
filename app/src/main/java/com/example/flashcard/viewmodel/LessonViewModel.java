package com.example.flashcard.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flashcard.data.Lesson;
import com.example.flashcard.data.LessonRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessonViewModel extends AndroidViewModel {

    private final LessonRepository mRepository;
    private final LiveData<List<Lesson>> mAllLessons;

    public LessonViewModel(@NonNull Application application) {
        super(application);
        mRepository = new LessonRepository(application);
        mAllLessons = mRepository.getAllLessons();
    }


    public LiveData<List<Lesson>> getAllLessons() {
        return mAllLessons;
    }

    public void insert(Lesson lesson) {
        mRepository.insert(lesson);
    }
    public void insert(String name) {
        Lesson lesson = new Lesson(name);
        mRepository.insert(lesson);
    }

    public void delete(Lesson lesson) {
        mRepository.delete(lesson);
    }
}
