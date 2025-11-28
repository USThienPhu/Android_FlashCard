package com.example.flashcard.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LessonRepository {

    private  LessonDao mLessonDao;
    private final LiveData<List<Lesson>> mAllLessons;

    public LessonRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mLessonDao = db.lessonDao();
        mAllLessons = mLessonDao.getAllLessons();
    }

    public LiveData<List<Lesson>> getAllLessons() {
        return mAllLessons;
    }

    public void insert(Lesson lesson) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mLessonDao.insert(lesson);
        });
    }
}
