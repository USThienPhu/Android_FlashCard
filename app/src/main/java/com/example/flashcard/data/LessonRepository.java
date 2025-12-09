package com.example.flashcard.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LessonRepository {
    private static final String TAG = "DEBUG_FIREBASE";

    private LessonDao mLessonDao;
    private final LiveData<List<Lesson>> mAllLessons;
    private final FirebaseFirestore firestore;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LessonRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mLessonDao = db.lessonDao();
        mAllLessons = mLessonDao.getAllLessons();
        this.firestore = FirebaseFirestore.getInstance();
        // Sync lessons from Firestore on initialization

        checkDataOnRoom();

    }

    public LiveData<List<Lesson>> getAllLessons() {
        return mAllLessons;
    }

    public void insert(Lesson lesson) {
        AppDatabase.databaseWriteExecutor.execute(() -> mLessonDao.insert(lesson));
    }

    public void delete(Lesson lesson) {
        AppDatabase.databaseWriteExecutor.execute(() -> mLessonDao.delete(lesson));
    }

    // Sync lessons from Firestore to Room database, clearing old data first to avoid duplicates
    private void syncLessonsFromFirebase() {
        Log.d(TAG, ">>> [LessonRepository] Starting lesson sync from Firestore...");
        firestore.collection("lessons").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int size = task.getResult().size();
                        Log.d(TAG, ">>> [LessonRepository] Found " + size + " lessons on Firestore.");
                        if (size == 0) {
                            Log.w(TAG, "!!! [LessonRepository] WARNING: No lessons found on Firestore!");
                            return;
                        }
                        executor.execute(() -> {
                            // Clear existing lessons to prevent duplicates
//                            mLessonDao.deleteAllLessons();
                            int count = 0;
                            for (DocumentSnapshot doc : task.getResult()) {
                                String name = doc.getString("name");
                                if (name != null && !name.isEmpty()) {
                                    Lesson lesson = new Lesson(name);
                                    long localId = mLessonDao.insert(lesson);
                                    count++;
                                    Log.d(TAG, "   + [LessonRepository] Saved lesson: " + name + " (Local ID: " + localId + ")");
                                }
                            }
                            Log.d(TAG, ">>> [LessonRepository] COMPLETED: Successfully saved " + count + " lessons to local database.");
                        });
                    } else {
                        Log.e(TAG, "!!! [LessonRepository] ERROR: Failed to fetch lessons from Firestore", task.getException());
                    }
                });
    }

    private void    checkDataOnRoom()
    {
        executor.execute(()->{
            int count = mLessonDao.noLesson();
            if (count == 0)
            {
                Log.d(TAG, "Start Sync Firebase");
                syncLessonsFromFirebase();
            }
        });
    }
}
